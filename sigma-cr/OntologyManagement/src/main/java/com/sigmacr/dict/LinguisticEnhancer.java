package com.sigmacr.dict;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.sigmacr.onto.FeatureClass;
import com.sigmacr.onto.PO;
import com.sigmacr.onto.PropertyHolder;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sigmacr.onto.OntoManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/30/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinguisticEnhancer {

    final Logger logger = LoggerFactory.getLogger(LinguisticEnhancer.class);
    final String NS;
    final String[] CommonSuffixes = {"Feature", "Features", "Performance", "Quality"};//these are removed from label ends.

    private Dictionary dic;
    private OntModel model;

    public LinguisticEnhancer() {
        OntoManager om = OntoManager.getInstance();
        NS = om.getOntologyIRI() + "#";
        model = om.getModel();
        try {
            JWNL.initialize(new FileInputStream(PropertyHolder.getProperties().getProperty("JWNLConfig")));
            dic = Dictionary.getInstance();
        } catch (JWNLException e) {
            logger.error(String.valueOf(e.getStackTrace()));
        } catch (FileNotFoundException e) {
            logger.error(String.valueOf(e.getStackTrace()));
        }
    }


    public List<SynSuggestion> suggestLinguisticData(Resource rdfResource) {
        if (rdfResource.canAs(OntClass.class)) {
            OntClass ontCls = rdfResource.as(OntClass.class);
            OntModel om = OntoManager.getInstance().getModel();

            if (ontCls.hasSuperClass(om.getOntClass(NS + "Product"))) { //if rdfResource is a product
                return suggestForProduct(ontCls);
            } else if (ontCls.hasSuperClass(om.getOntClass(NS + "Feature"))) { //if feature
                return suggestForFeature(ontCls);
            } else {
                logger.error("unsupported class type");
            }
        }
        return null;
    }

    /**
     * suggest synincludes or p1syn,p2syn for product p
     *
     * @param p
     * @return
     */
    public List<SynSuggestion> suggestForProduct(OntClass p) {


        Set<String> lblsP = getClassLabels(p);
        List<SynSuggestion> suggestions = null;
        try {

            List<Synset> synsets = new ArrayList<Synset>();
            IndexWord indexWord = null;
            for (String lbl : lblsP) {
                indexWord = dic.getIndexWord(POS.NOUN, lbl);
                if(indexWord!=null){
                    Collections.addAll(synsets, indexWord.getSenses());
                }
            }
            Collections.sort(synsets, new SynsetComparator());

            suggestions = new ArrayList<SynSuggestion>();
            for (int i = 0; i < synsets.size(); i++) {
                Synset ss = synsets.get(i);
                if (ss.getLexFileName().contentEquals("noun.artifact")) {
                    //  logger.debug("ss->noun.artifact:" + ss);
                    SynSuggestion sug = new SynSuggestion(ss);

                    boolean con = true;
                    SynsetComparator ssComp = new SynsetComparator();
                    while (con) { //look ahead for same synset repeated. car has lbl automobile. so same synset will be repeated.
                        if (i == synsets.size() - 1) break;
                        i++;
                        Synset ss2 = synsets.get(i);
                        if (ssComp.compare(ss, ss2) == 0) {
                            if (sug.scoreAO > 0) sug.scoreAO = 0;
                            sug.scoreAO++;
                        } else {
                            i--;//reset index from look ahead
                            con = false;
                        }
                    }
                    sug.type = SynSuggestion.TYPE_SynInclude;
                    suggestions.add(sug);
                }
            }

            //if two or more synsets in artifact category.
            if (suggestions.size() > 1) {
                OntoManager om = OntoManager.getInstance();
                Map<Integer, List<OntClass>> supClsMap = om.getSuperClassesWithDistance(p);

                for (int i = 0; i < suggestions.size(); i++) {
                    SynSuggestion sug = suggestions.get(i);
                    List<String> ssWords = getWords(sug.getSynset());


                    int score = 0;
                    //check if any superclass contains a word in the synset.  //score 1
                    A:
                    for (int j = 1; j <= supClsMap.size(); j++) {
                        List<OntClass> sups = supClsMap.get(j);
                        for (OntClass sup : sups) {
                            if (lookupWordsInCls(sup, ssWords)) {
                                score = j;
                                sug.scorePO1 = sug.scorePO1 + ((supClsMap.size() - j) / supClsMap.size());
                                break A;
                            }
                        }
                    }

                    //check for hypernyms of this synset that match the super class labels of p cls.   //score 2
                    Synset ss = sug.getSynset();
                    boolean con = true;
                    int hypernimlvl = 0;
                    while (con) {
                        Pointer[] pointers = ss.getPointers(PointerType.HYPERNYM);
                        if (pointers.length > 0) {
                            hypernimlvl++;
                            ss = pointers[0].getTargetSynset();
                            ssWords = getWords(ss);
                            A:
                            for (int j = 1; j <= supClsMap.size(); j++) {
                                List<OntClass> sups = supClsMap.get(j);
                                for (OntClass sup : sups) {
                                    if (lookupWordsInCls(sup, ssWords)) {
                                        score = j * hypernimlvl;
                                        sug.scorePO2 = sug.scorePO2 + ((supClsMap.size() - j) / supClsMap.size());
                                        con = false;
                                        break A;
                                    }
                                }
                            }
                        } else {
                            con = false;
                        }
                    }

                }

                // remove non matching word senses from suspects.

                for (SynSuggestion sug : suggestions) {
                    if (sug.scoreAO == 0 && sug.scorePO1 == 0 &&sug.scorePO2 == 0){
                        sug.add = false;
                    }
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return suggestions;
    }


    public List<SynSuggestion> suggestForFeature(OntClass f) {
        //get synsets for class labels
        Set<String> lblsF = getClassLabels(f);
        List<SynSuggestion> suggestions = new ArrayList<SynSuggestion>();
        List<Synset> synsets = new ArrayList<Synset>();

        try {
            IndexWord indexWord = null;
            for (String lbl : lblsF) {
                indexWord = dic.getIndexWord(POS.NOUN, lbl);
                if (indexWord != null) {
                    Collections.addAll(synsets, indexWord.getSenses());  //synSets for all labels added.
                }
            }
            List<String> wnCategories = new ArrayList<String>();//filters
            wnCategories.add("noun.attribute");
            wnCategories.add("noun.artifact");
            wnCategories.add("noun.phenomenon");
            wnCategories.add("noun.process");
            wnCategories.add("noun.location");
            wnCategories.add("noun.relation");

            if (synsets.size() > 0) {
                Collections.sort(synsets, new SynsetComparator());
                //suggestions of type synInclude
                suggestions.addAll(makeSuggestions(synsets, wnCategories, SynSuggestion.TYPE_SynInclude));

                //add derivationally related froms
                SynSuggestion sug;
                for (String lbl : lblsF) {
                    Synset dRelSS = getDerivationallyRelatedFrom(lbl);
                    if (dRelSS != null) {
                        sug = new SynSuggestion(SynSuggestion.TYPE_DerivationalyRelatedFrom, dRelSS);
                        suggestions.add(sug);
                    }
                }
            }
            //for two part labels
            //p1Syns, p2Syns
            List<String[]> lbls2pt = new ArrayList<String[]>();
            for (String lbl : lblsF) {
                String[] split = lbl.split(" ");
                List<String> splitList = new LinkedList<String>();
                Collections.addAll(splitList, split);
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    if (LingUtil.isStopWord(s)) {
                        splitList.remove(i);
                    }
                }
                split = splitList.toArray(split);
                if (splitList.size() == 2) {
                    lbls2pt.add(split);
                }
            }
            //p1,p2 sug
            SynSuggestion p1Syn;
            SynSuggestion p2Syn;
            SynSuggestion p1DRel;
            SynSuggestion p2DRel;
            String p1, p2;
            IndexWord iword1;
            IndexWord iword2;
            List<Synset> p1syns = new ArrayList<Synset>();
            List<Synset> p2syns = new ArrayList<Synset>();

            for (String[] lbl2pt : lbls2pt) {
                p1 = lbl2pt[0];
                p2 = lbl2pt[1];
                iword1 = dic.getIndexWord(POS.NOUN, p1);
                iword2 = dic.getIndexWord(POS.NOUN, p2);
                if (iword1 != null) {
                    Collections.addAll(p1syns, iword1.getSenses());
                    suggestions.addAll(makeSuggestions(p1syns, wnCategories, SynSuggestion.TYPE_P1Syn));
                    p1DRel = new SynSuggestion(SynSuggestion.TYPE_P1DRel, getDerivationallyRelatedFrom(p1));
                    if (p1DRel.synset != null) suggestions.add(p1DRel);
                }

                if (iword2 != null) {
                    Collections.addAll(p2syns, iword2.getSenses());
                    suggestions.addAll(makeSuggestions(p2syns, wnCategories, SynSuggestion.TYPE_P2Syn));
                    p2DRel = new SynSuggestion(SynSuggestion.TYPE_P2DRel, getDerivationallyRelatedFrom(p2));
                    if (p2DRel.synset != null) suggestions.add(p2DRel);
                }

            }//end p1,p2 sug

            scoreSuggestion(suggestions, f);

            //Suggest attributes for senses
            List<SynSuggestion> attrSugs=new ArrayList<SynSuggestion>();
            for (SynSuggestion sug : suggestions) {
                List<Synset> attributes = getAttributeSenses(sug.getSynset());
                for (Synset attrSense : attributes) {
                    attrSugs.add(new SynSuggestion(SynSuggestion.TYPE_ATTRIBUTE, attrSense));
                }
            }
            suggestions.addAll(attrSugs);
            //end attr sug//

            //association syns //
            FeatureClass fc = new FeatureClass(f);
            List<Synset> asoSenses = new ArrayList<Synset>();
            List<String> associations = fc.getAssociations();
            for(int i=0;i<associations.size();i++){ // to lower case all associations.
                associations.set(i,associations.get(i).toLowerCase());
            }
            for (String aso : associations) {
                indexWord = dic.getIndexWord(POS.ADJECTIVE, aso);
                for(Synset sens: indexWord.getSenses()){
                    int score=0;
                    for(String otherAso:associations){
                        for(Word synonym:sens.getWords()){
                            if(synonym.getLemma().equals(otherAso)){
                                      score++;
                            }
                        }
                       // check antonym match with other association
                        Pointer[] pointers = sens.getPointers(PointerType.ANTONYM);
                        for(Pointer p:pointers ){
                            for(Word antonym:p.getTargetSynset().getWords()){
                                if(antonym.getLemma().equals(otherAso)){
                                    score++;
                                }
                            }
                        }
                    }
                    score--;//less one for match with it self.  ex aso=light, heavy: senses: {light ,luminous , bright} {light (vs heavy)}

                    if(score>0){
                        suggestions.add(new  SynSuggestion(SynSuggestion.TYPE_ASSOCIATION_SYN,sens));
                        //add similar to ... senses also
                        Pointer[] pointers = sens.getPointers(PointerType.SIMILAR_TO);
                        for(Pointer p:pointers){
                            suggestions.add(new SynSuggestion(SynSuggestion.Type_ASSOCIATION_SIMILARTO,p.getTargetSynset()));
                        }

                    }
                }
            }
            //end association syns //


        } catch (JWNLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return suggestions;
    }

    /**
     * get derivationally related from  words to the given string der
     *
     * @param der
     * @return
     * @throws JWNLException
     */
    Synset getDerivationallyRelatedFrom(String der) throws JWNLException {
        String drel;
        IndexWord indexWord = dic.getIndexWord(POS.NOUN, der);
        List<Synset> synsets = new ArrayList<Synset>();
        if (indexWord != null) {
            for (Synset ss : indexWord.getSenses()) {
                Pointer[] pointers = ss.getPointers(PointerType.NOMINALIZATION);
                if (pointers.length > 0) {
                    Synset dRelSS = pointers[0].getTargetSynset();
                    synsets.add(dRelSS);
                    return dRelSS;
                }
            }
        }
        for (Synset ss : synsets) {
            String cat = ss.getLexFileName();
            if (cat.contentEquals("noun.attribute")) {
                return ss;
            }
        }
        if (synsets.size() > 0)
            return synsets.get(0);
        else return null;
    }

    List<Synset> getAttributeSenses(Synset sense) throws JWNLException {
        Pointer[] pointers = sense.getPointers(PointerType.ATTRIBUTE);
        List<Synset> attrsenses = new ArrayList<Synset>();
       for (Pointer p:pointers) {
            Synset attrSS = p.getTargetSynset();
            attrsenses.add(attrSS);
        }
        return attrsenses;
    }


    /**
     * Make a list of SynSuggestions(wordnet senses) filtered by categories.
     *
     * @param synsets
     * @param wnCategories
     * @param sugType
     * @return
     */
    private List<SynSuggestion> makeSuggestions(List<Synset> synsets, List<String> wnCategories, String sugType) {
        List<SynSuggestion> suggestions = new ArrayList<SynSuggestion>();
        for (int i = 0; i < synsets.size(); i++) {
            Synset ss = synsets.get(i);
            String wnCat = ss.getLexFileName();
            if (wnCategories.contains(wnCat)) {
                //  logger.debug("ss->noun.artifact:" + ss);
                SynSuggestion sug = new SynSuggestion(ss);
                sug.type = sugType;
                boolean con = true;
                SynsetComparator ssComp = new SynsetComparator();
                while (con) { //look ahead for same synset repeated. car has lbl automobile. so same synset will be repeated.
                    if (i == synsets.size() - 1) break;
                    i++;
                    Synset ss2 = synsets.get(i);
                    if (ssComp.compare(ss, ss2) == 0) {
                        if (sug.scoreAO > 0) sug.scoreAO = 0;
                        sug.scoreAO++;
                    } else {
                        i--;//reset index from look ahead
                        con = false;
                    }
                }
                suggestions.add(sug);
            }
        }
        return suggestions;
    }

    /**
     * score WordNet senses for similarity with concept.
     *
     * @param suggestions
     * @param ocls
     * @throws JWNLException
     */
    private void scoreSuggestion(List<SynSuggestion> suggestions, OntClass ocls) throws JWNLException {
        if (suggestions.size() > 1) {
            OntoManager om = OntoManager.getInstance();
            Map<Integer, List<OntClass>> supClsMap = om.getSuperClassesWithDistance(ocls);

            for (int i = 0; i < suggestions.size(); i++) {
                SynSuggestion sug = suggestions.get(i);
                List<String> ssWords = getWords(sug.getSynset());


                int score = 0;
//check if any superclass contains a word in the synset.  //score 1  //parent overlap 1
                A:
                for (int j = 1; j <= supClsMap.size(); j++) {
                    List<OntClass> sups = supClsMap.get(j);
                    for (OntClass sup : sups) {
                        if (lookupWordsInCls(sup, ssWords)) {
                            score = j;
                            sug.scorePO1 = score;
                            break A;
                        }
                    }
                }

                //check for hypernyms of this synset that match the super class labels of p cls.   //score 2 //parent overlap 2
                Synset ss = sug.getSynset();
                boolean con = true;
                int hypernimlvl = 0;
                while (con) {
                    Pointer[] pointers = ss.getPointers(PointerType.HYPERNYM);
                    if (pointers.length > 0) {
                        hypernimlvl++;
                        ss = pointers[0].getTargetSynset();
                        ssWords = getWords(ss);
                        A:
                        for (int j = 1; j <= supClsMap.size(); j++) {
                            List<OntClass> sups = supClsMap.get(j);
                            for (OntClass sup : sups) {
                                if (lookupWordsInCls(sup, ssWords)) {
                                    score = j * hypernimlvl;
                                    sug.scorePO2 = score;
                                    con = false;
                                    break A;
                                }
                            }
                        }
                    } else {
                        con = false;
                    }
                }


            }

            // remove non matching word senses from suspects.
            Collections.sort(suggestions);
        }
    }

    /**
     * Lexical enrichment of ontoclass. Add the sug as annotations. SynSug contains ann type name.
     *
     * @param ocls
     * @param sug
     * @param excludeList
     */
    public void addSuggestion(OntClass ocls, SynSuggestion sug, List<String> excludeList) {
        if (sug.add == false) {
            return;//ignore if not to add.
        }
        AnnotationProperty apSyn = model.getAnnotationProperty(NS + sug.type);
        Synset ss = sug.getSynset();
        if (excludeList == null) {
            for (Word word : ss.getWords()) {
                ocls.addProperty(apSyn, word.getLemma());
            }
        } else {
            for (Word word : ss.getWords()) {
                if (!excludeList.contains(word.getLemma())) {
                    ocls.addProperty(apSyn, word.getLemma());
                }
            }
        }

    }

    /**
     * exclude these words when enhancing.
     *
     * @param ocls
     * @param additionalExcludes
     * @return
     */
    public List<String> getAllExcludes(OntClass ocls, String... additionalExcludes) {
        List<String> excludeList = new ArrayList<String>(5);

        AnnotationProperty apExclude = model.getAnnotationProperty(NS + "synExcludes");    //to add synExcludes  to list
        AnnotationProperty apSyn = model.getAnnotationProperty(NS + SynSuggestion.TYPE_SynInclude);// to add slreeady added synIncludes
        AnnotationProperty apAsso = PO.AP_ASSOCIATON;

        //add all additionalExcludes
        if (additionalExcludes != null) {
            for (int i = 0; i < additionalExcludes.length; i++) {
                additionalExcludes[i] = additionalExcludes[i].toLowerCase();
            }
            Collections.addAll(excludeList, additionalExcludes);
        }
        // add syn excludes
        NodeIterator itrExcludes = ocls.listPropertyValues(apExclude);
        while (itrExcludes.hasNext()) {
            String synExs = itrExcludes.next().asLiteral().getLexicalForm();
            String synEx[] = synExs.split(",");
            for (int i = 0; i < synEx.length; i++) {
                synEx[i] = synEx[i].trim();
            }
            Collections.addAll(excludeList, synEx);
        }
        // add synIncludes
        NodeIterator itrSyn = ocls.listPropertyValues(apSyn);    // add to exclude list, already added synonyms also
        while (itrSyn.hasNext()) {
            excludeList.add(itrSyn.next().asLiteral().getLexicalForm());
        }
        //add association
        NodeIterator itrAsso = ocls.listPropertyValues(apAsso);
        while (itrAsso.hasNext()) {
            excludeList.add(itrAsso.next().asLiteral().getLexicalForm());
        }

        logger.debug("exclude list:" + excludeList);
        return excludeList;
    }


    private boolean lookupWordsInCls(OntClass ocls, List<String> words) {
        List<String> lbls = new ArrayList<String>();
//labels
        ExtendedIterator<RDFNode> iterator = ocls.listLabels(null);
        while (iterator.hasNext()) {
            lbls.add(iterator.next().asLiteral().getLexicalForm().toLowerCase());
        }
        //class name
        String name = LingUtil.splitCamelCase(ocls.getLocalName().toLowerCase());
        lbls.add(name);
//syn includes
        AnnotationProperty apSyn = model.getAnnotationProperty(NS + SynSuggestion.TYPE_SynInclude);
        NodeIterator itrSyn = ocls.listPropertyValues(apSyn);
        while (itrSyn.hasNext()) {
            RDFNode next = itrSyn.next();
            lbls.add(next.asLiteral().getLexicalForm().toLowerCase());
        }
        boolean contains = false;
        for (String word : words) {
            if (lbls.contains(word.toLowerCase())) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * If class has a label AbraCadabraQualityPerformance --> Abra Cadabra --> abra cadabra : will be returned as a label in the set.
     *
     * @param ocls
     * @return
     */
    public Set<String> getClassLabels(OntClass ocls) {
        String name = ocls.getLocalName();
        ExtendedIterator<RDFNode> iterator = ocls.listLabels(null);
        List<String> labels = new ArrayList<String>(3);
        while (iterator.hasNext()) {
            labels.add(iterator.next().asLiteral().getLexicalForm());
        }

        // get trimmed name,labels  then split camel case.
        name = trimName(name);
        name = LingUtil.splitCamelCase(name).toLowerCase();
        for (int i = 0; i < labels.size(); i++) {
            labels.set(i, LingUtil.splitCamelCase(trimName(labels.get(i))).toLowerCase());//trim then split camel case . then make lowercase
        }
        Set<String> lbls = new HashSet<String>(3);
        lbls.add(name);
        lbls.addAll(labels);
        return lbls;
    }

    //utility methods
    private String trimName(String name) {
        Pattern p;
        Matcher m;
        for (String s : CommonSuffixes) {
            p = Pattern.compile("(?i)" + s);
            m = p.matcher(name);
            if (m.find()) {
                name = name.substring(0, m.start());
            }
        }
        return name;
    }

    private List<String> getWords(Synset ss) {
        List<String> ssWords = new ArrayList<String>();
        for (Word w : ss.getWords()) {
            ssWords.add(w.getLemma().replaceAll("_", " "));
        }
        return ssWords;
    }


    //utility classes.
    static class SynsetComparator implements Comparator<Synset> {

        @Override
        public int compare(Synset o1, Synset o2) {
            long val = o1.getOffset() - o2.getOffset();
            int ret;
            if (val > 0) {
                ret = 1;
            } else if (val == 0) {
                ret = 0;
            } else {
                ret = -1;
            }
            return ret;
        }
    }
}
