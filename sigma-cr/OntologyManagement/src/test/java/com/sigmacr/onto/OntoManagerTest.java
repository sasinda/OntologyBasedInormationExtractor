package com.sigmacr.onto;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.junit.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/11/13
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class OntoManagerTest {
    static OntoManager om;
    static String NS ;

    @BeforeClass
    public static void  initiate(){
        om=OntoManager.getInstance();
        om.load();
        NS=om.getOntologyIRI()+"#";
    }

    @org.junit.Test
    public void testGetSuperClassesWithDistance() throws Exception {
        OntModel model=om.getModel();

        OntClass tvCls=model.getOntClass(NS+"Fridge");
        Map<Integer,List<OntClass>> map = om.getSuperClassesWithDistance(tvCls);
        System.out.println(map);
    }



}
