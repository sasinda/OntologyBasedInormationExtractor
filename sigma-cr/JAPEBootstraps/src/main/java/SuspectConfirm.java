/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/7/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.*;
import java.util.*;

import gate.*;
import gate.jape.*;
import gate.util.*;
import com.sigmacr.ling.SynonymRanks;
import com.sigmacr.onto.FeatureClass;
import com.sigmacr.onto.OntoManager;
import com.sigmacr.onto.ProductClass;

// Import: block code will be embedded here
class SuspectConfirm implements Serializable, gate.jape.RhsAction {
    private ActionContext ctx;

    AnnotationSet tagAnnots;

    public void doit(
            gate.Document doc,
            Map<String, gate.AnnotationSet> bindings,
            gate.AnnotationSet annotations,
            gate.AnnotationSet inputAS,
            gate.AnnotationSet outputAS,
            gate.creole.ontology.Ontology ontology) throws JapeException {
        // your RHS Java code will be embedded here ...
        //get tokesn and lookups
        Annotation suspect = tagAnnots.iterator().next();
        Long start = suspect.getStartNode().getOffset();
        Long end = suspect.getEndNode().getOffset();
        AnnotationSet toks = inputAS.get("Token", start, end);
        AnnotationSet lokps = inputAS.get("Lookup", start, end);
        List<Annotation> tokens = new ArrayList<Annotation>(toks);
        List<Annotation> lookups = new ArrayList<Annotation>(lokps);
        Collections.sort(tokens, new OffsetComparator());
        Collections.sort(lookups, new OffsetComparator());

        String category = (String) doc.getFeatures().get("category");
        //System.out.println(category);
        OntoManager om = OntoManager.getInstance();
        final ProductClass product = om.getProductClass(category);

        //If token is an adjective and not a lookup ignore it.
        // check lookup feature belongs to product
        boolean has = false;
        String URI = null;


        class LookupRankComparator implements Comparator<Annotation> {
            SynonymRanks ranker = new SynonymRanks();

            @Override
            public int compare(Annotation o1, Annotation o2) {
                String o1SynType = (String) o1.getFeatures().get("propertyURI");
                String o2SynType = (String) o2.getFeatures().get("propertyURI");
                int o1Rank = ranker.getRank(o1SynType);
                int o2Rank = ranker.getRank(o2SynType);
                int diff = o1Rank - o2Rank;
                if (diff == 0) {
                    FeatureClass f1 = product.getFeature((String) o1.getFeatures().get("URI"));
                    FeatureClass f2 = product.getFeature((String) o2.getFeatures().get("URI"));
                    if(f1.hasParentFeature(f2)){
                        return 1;
                    }else if(f2.hasParentFeature(f1)){
                        return -1;
                    }
                    //if no such parent child relationship just return 0 at end.
                }
                return o2Rank-o1Rank;
            }
        }
        Collections.sort(lookups, new LookupRankComparator()); //lookups are ranked according to importance. p1 syn > p1 der related from.
        // if no lookup found in suspect, get any lookup within the sentence and find feature similarity.
//        if (!has) {
//            AnnotationSet sentence = inputAS.getCovering("Sentence", start, end);
//
//            Long senStart = sentence.firstNode().getOffset();
//            Long sEnd = sentence.lastNode().getOffset();
//            AnnotationSet lookupsBfr = inputAS.get("Lookup", senStart, start);
//            for (Annotation lookupBfr : lookupsBfr) {
//                //avoid lookups for be verbs. i.e word "has, have , had ... " can be marked as a lookup to word "consume" .These are generally avoided.
//                // TODO: improve heuristic
//
//                AnnotationSet tokenset = inputAS.get("Token", lookupBfr.getStartNode().getOffset(), lookupBfr.getEndNode().getOffset());
//                Annotation token=tokenset.iterator().next();
//                if( token.getFeatures().get("root").equals("have")) continue;
//                URI = (String) lookupBfr.getFeatures().get("URI");
//                has = product.hasFeature(URI);
//            }
//        }
        for (Annotation lookup : lookups) {
            URI = (String) lookup.getFeatures().get("URI");
            has = product.hasFeature(URI);

        }
        if (has) {
            FeatureMap fmap = Factory.newFeatureMap();
            fmap.put("class", URI);
            try {
                outputAS.add(start, end, "Feature", fmap);
            } catch (InvalidOffsetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    @Override
    public void setActionContext(ActionContext actionContext) {
    }

    public ActionContext getActionContext() {
        return null;
    }

    public String ruleName() {
        return null;
    }

    public String phaseName() {
        return null;
    }
}