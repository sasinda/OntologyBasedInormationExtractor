package com.sigmacr.dict;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sigmacr.onto.OntoManager;

import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/27/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        OntoManager om = OntoManager.getInstance();
        om.load();
        OntModel model = om.getModel();
        final String NS = om.getOntologyIRI() + "#";
        logger.info(NS);

        Scanner s = new Scanner(System.in);

        while (true) {

            System.out.println("enter:");
            String clsName = s.nextLine();
            System.out.println("enhancing for: |"+clsName+"|");
            if(clsName.equalsIgnoreCase("exit"))break;
            OntClass pCls = model.getOntClass(NS + clsName);
            if(pCls==null){
                System.err.println("class does not exist");
                continue;
            }

            LinguisticEnhancer le = new LinguisticEnhancer();
            List<SynSuggestion> suggestions = le.suggestLinguisticData(pCls);


            // exclude name + labels , syn includes etc when adding syn includes

            //for suspects
            for (SynSuggestion sug : suggestions) {
                logger.debug(sug.toString());
                if (true) {
                    List<String> excludeList = le.getAllExcludes(pCls, le.getClassLabels(pCls).toArray(new String[0]));
                    le.addSuggestion(pCls, sug, excludeList);
                }
            }

            om.save();
        }
    }
}
