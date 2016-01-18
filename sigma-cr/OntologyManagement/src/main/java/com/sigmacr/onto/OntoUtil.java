package com.sigmacr.onto;//package com.sigmacr.dict;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/26/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class OntoUtil {

    static OntModel m;
    static String NS;

    static {
        OntoManager om = OntoManager.getInstance();
        //om.load();
        NS = om.getOntologyIRI() + "#";
        m = OntoManager.getInstance().getModel();
    }

    public static Literal getAnnotationPropertyValueInAxiom(Resource source, Resource targetFeature, AnnotationProperty ann) {


        ResIterator axioms = m.listSubjectsWithProperty(RDF.type, OWL2.Axiom);

        //find all axioms for this particular source product.
        List<Resource> axiomsForSrc = new ArrayList<Resource>();
        while (axioms.hasNext()) {
            Resource axiom = axioms.next();
            StmtIterator stmts = axiom.listProperties();

            while (stmts.hasNext()) {
                Statement stmt = stmts.next();
                if (stmt.getPredicate().equals(OWL2.annotatedSource)) {
                    if (stmt.getObject().equals(source)) {
                        axiomsForSrc.add(axiom);
                    }
                }
                ;
            }
        }

        for (Resource axiom : axiomsForSrc) {
            Resource restriction = axiom.getPropertyResourceValue(OWL2.annotatedTarget);
            Resource feature = restriction.getPropertyResourceValue(OWL.someValuesFrom);
            if (feature == null) {
                feature = restriction.getPropertyResourceValue(OWL.allValuesFrom);
            }
            if (feature.equals(targetFeature)) {
                return axiom.getProperty(ann).getObject().asLiteral();
            }
        }

        return null;
    }

    public static Set<OntClass> getFeatures(OntClass product) {
        NodeIterator restrictions = product.listPropertyValues(RDFS.subClassOf);
        Set<OntClass> fset = new HashSet<OntClass>(40);

        Resource restriction;
        RDFNode next;
        while (restrictions.hasNext()) {
            next = restrictions.next();
            //System.out.println(next);
            restriction = next.asResource();
            Resource phasFeature =restriction.getPropertyResourceValue(OWL.onProperty);
            Resource feature = restriction.getPropertyResourceValue(OWL.someValuesFrom);
            if (feature != null) {
                fset.add(feature.as(OntClass.class));
                ExtendedIterator<OntClass> subs = feature.as(OntClass.class).listSubClasses();
                while (subs.hasNext()) {
                    fset.add(subs.next());
                }
            }
        }

//       OntClass supFeature;
//       while (mFeatures.hasNext()){
//           supFeature=mFeatures.next();
//       }
//
        return fset;
    }

    /**
     *
     * @return the local part of the full URI
     */
    public static String shortenURI(String uri){
        if(uri.startsWith("http")){
            uri=uri.substring(uri.indexOf('#')+1);
        }
        return uri;
    }


}
