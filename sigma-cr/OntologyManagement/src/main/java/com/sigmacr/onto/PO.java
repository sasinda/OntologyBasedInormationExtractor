package com.sigmacr.onto;

import com.hp.hpl.jena.ontology.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 7/1/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class PO {
    public static final String NS="http://cse.mrt.ac.lk/sigmacr/ontologies/product_ontolology#";
    public static final AnnotationProperty AP_IS_NEGATIVE;
    public static final AnnotationProperty AP_ATTRIBUTE;
    public static final AnnotationProperty AP_ASSOCIATON;
    public static final ObjectProperty HAS_FEATURE;
    public static final OntClass PRODUCT;



    final static String TYPE_SynInclude="synInclude";
    final static String TYPE_P1Syn="p1Syn";
    final static String TYPE_P2Syn="p2Syn";
    final static String TYPE_P3Syn="p3Syn";
    final static String TYPE_DerivationalyRelatedFrom="derivationallyRelatedFrom";
    final static String TYPE_P1DRel="p1DRel";
    final static String TYPE_P2DRel="p2DRel";
    final static String TYPE_ATTRIBUTE="attribute";
    final static String TYPE_ASSOCIATION ="association";


    static {
        OntoManager om = OntoManager.getInstance();
        OntModel m=om.getModel();
        AP_IS_NEGATIVE =m.getAnnotationProperty(NS + "isNegative");
        AP_ASSOCIATON=  m.getAnnotationProperty(NS + TYPE_ASSOCIATION);
        AP_ATTRIBUTE=m.getAnnotationProperty(NS +  TYPE_ATTRIBUTE);
        HAS_FEATURE =m.getObjectProperty(NS + "hasFeature");
        PRODUCT=m.getOntClass(NS+"Product");
    }
}
