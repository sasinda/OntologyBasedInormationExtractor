package com.sigmacr.onto;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Wrapper for OntClass es that represent Products.
 */
public class ProductClass {
    Logger logger = LoggerFactory.getLogger(ProductClass.class);

    OntClass pCls;
    String name;

    private Set<FeatureClass> features;
    private Set<FeatureClass> top_level_features;

    ProductClass(OntClass ontClass) {
        if (!ontClass.hasSuperClass(PO.PRODUCT)) {
            throw new IllegalArgumentException("provided ontClass is not a product. uri=" + ontClass.getURI());
        }
        ;
        pCls = ontClass;
        features = new HashSet<FeatureClass>();
        top_level_features = new HashSet<FeatureClass>();
    }

    /**
     * @return the set of all features belonging to this product
     */
    public Set<FeatureClass> getFeatures() {
        if (features.isEmpty()) {
            NodeIterator restrictions = pCls.listPropertyValues(RDFS.subClassOf);

            Resource restriction;
            RDFNode next;
            while (restrictions.hasNext()) {
                next = restrictions.next();

                restriction = next.asResource();
               // System.out.println(restriction.getPropertyResourceValue(OWL.onProperty));
                Resource phasFeature = restriction.getPropertyResourceValue(OWL.onProperty);
                //if the restriction is on HAS_FEATURE property
                if (phasFeature != null && phasFeature.equals(PO.HAS_FEATURE)) {
                    Resource feature = restriction.getPropertyResourceValue(OWL.someValuesFrom);
                    if (feature == null) { //if Product has only one feature kind. ex: Vehicle HAS_FEATURE only VehicleFeature.
                        feature = restriction.getPropertyResourceValue(OWL.allValuesFrom);
                    }
                    if (feature != null) {
                        FeatureClass f = new FeatureClass(feature.as(OntClass.class));
                        features.add(f);
                        //DFS on child features.
                        Queue<FeatureClass> queue = new LinkedList();
                        queue.add(f);
                        while (!queue.isEmpty()) {
                            FeatureClass pf = queue.poll();
                            ExtendedIterator<OntClass> subs = pf.getOntClass().listSubClasses(true);
                            while (subs.hasNext()) {
                                FeatureClass cf = new FeatureClass(subs.next(), pf);
                                queue.add(cf);
                                features.add(cf);
                            }
                        }

                    }
                }
            }
        }
        return features;

    }

    public Set<FeatureClass> getFeatures(boolean topLevel) {
        if(topLevel==false){
            return  getFeatures();
        }
        if (top_level_features.isEmpty()) {
            NodeIterator restrictions = pCls.listPropertyValues(RDFS.subClassOf);

            Resource restriction;
            RDFNode next;
            while (restrictions.hasNext()) {
                next = restrictions.next();

                restriction = next.asResource();
                Resource phasFeature = restriction.getPropertyResourceValue(OWL.onProperty);
                //if the restriction is on HAS_FEATURE property
                if (phasFeature != null && phasFeature.equals(PO.HAS_FEATURE)) {
                    Resource feature = restriction.getPropertyResourceValue(OWL.someValuesFrom);
                    if (feature == null) { //if Product has only one feature kind. ex: Vehicle HAS_FEATURE only VehicleFeature.
                        feature = restriction.getPropertyResourceValue(OWL.allValuesFrom);
                    }
                    if (feature != null) {
                        FeatureClass f = new FeatureClass(feature.as(OntClass.class));
                        top_level_features.add(f);
                    }
                }
            }
        }
        return top_level_features;
    }

    public FeatureClass getFeature(String uri){
        if(features.isEmpty()){
            getFeatures();
        }

        uri=OntoUtil.shortenURI(uri);
        for(FeatureClass f:features){
            if(f.getOntClass().getLocalName().equals(uri))return f;
        }
        throw  new IllegalArgumentException(uri+" feature is not available under this product");
    }

    public boolean hasFeature(FeatureClass f) {
        if (features.isEmpty()) {
            getFeatures();
        }
        for (FeatureClass myF : features) {
            if (myF.equals(f)) return true;
        }
        return false;
    }

    public boolean hasFeature(String uri) {
        if (features.isEmpty()) {
            getFeatures();
        }
        for (FeatureClass myF : features) {
            if (myF.getOntClass().getURI().equals(uri)) return true;
        }
        return false;
    }

    /**
     * @param feature
     * @return whether this feature has a negative scale regarding this product.
     */
    public boolean isNegative(FeatureClass feature) {
        boolean isNeg;
        Literal neg = OntoUtil.getAnnotationPropertyValueInAxiom(pCls, feature.getOntClass(), PO.AP_IS_NEGATIVE);
        isNeg = neg.getBoolean();
        return isNeg;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductClass)) return false;
        return ((ProductClass) obj).getOntClass().getURI().equals(pCls.getURI());    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode() {
        return name.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public OntClass getOntClass() {
        return pCls;
    }


    public boolean hasSuperClass(ProductClass parent) {
        return  pCls.hasSuperClass(parent.getOntClass());  //To change body of created methods use File | Settings | File Templates.
    }
}
