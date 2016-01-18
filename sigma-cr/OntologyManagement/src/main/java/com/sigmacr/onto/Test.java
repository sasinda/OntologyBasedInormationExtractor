package com.sigmacr.onto;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 7/4/13
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        String NS;
        OntoManager om=OntoManager.getInstance();
        om.load();
        NS=om.getOntologyIRI()+"#";

        OntModel model=om.getModel();
        //OntClass tvCls=model.getOntClass(NS+"TV");
       // OntClass featureCls=model.getOntClass(NS+"PowerConsumption");
       // OntoUtil.getFeatures(tvCls) ;
        ProductClass tv = om.getProductClass("TV");
        boolean has=tv.hasFeature("http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#PictureQuality");
        System.out.println(has);
    }
}
