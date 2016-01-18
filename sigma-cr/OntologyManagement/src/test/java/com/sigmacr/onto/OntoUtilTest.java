package com.sigmacr.onto;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import org.junit.BeforeClass;
import org.junit.Test;
import  static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 7/4/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class OntoUtilTest {

    static OntoManager om;
    static String NS ;

    @BeforeClass
    public static void  initiate(){
        om=OntoManager.getInstance();
        om.load();
        NS=om.getOntologyIRI()+"#";
    }

    @Test
    public void testGetAnnotationPropertyValueInAxiom() throws Exception {
        OntModel model=om.getModel();
        OntClass tvCls=model.getOntClass(NS+"TV");
        OntClass featureCls=model.getOntClass(NS+"PowerConsumption");
        Literal isNeg=OntoUtil.getAnnotationPropertyValueInAxiom(tvCls,featureCls, PO.AP_IS_NEGATIVE);
        assertTrue( isNeg.getBoolean() );
    }

    @Test
    public void getFeatures() throws Exception {
        OntModel model=om.getModel();
        OntClass tvCls=model.getOntClass(NS+"TV");
        OntoUtil.getFeatures(tvCls);
    }


}
