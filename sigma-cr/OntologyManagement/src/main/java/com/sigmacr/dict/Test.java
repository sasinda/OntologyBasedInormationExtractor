package com.sigmacr.dict;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;
import com.sigmacr.onto.OntoManager;
import com.sigmacr.onto.OntoUtil;
import com.sigmacr.onto.PO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/3/13
 * Time: 2:04 PM
 * Test Ext lib JWNL and etc.
 */
public class Test {

    public static void main(String[] args) {
        //System.out.println(splitCamelCase("Automotive Vehicle"));
          wordnetSearch();
            //onto();
    }


    static void onto(){
        OntoManager om=OntoManager.getInstance();
        om.load();
        String NS = om.getOntologyIRI() + "#";
        OntModel model = om.getModel();
        OntClass tvCls = om.getModel().getOntClass(NS + "TV");
        OntClass ftCls = om.getModel().getOntClass(NS+ "PowerConsumption");
        Literal l= OntoUtil.getAnnotationPropertyValueInAxiom(tvCls,ftCls, PO.AP_IS_NEGATIVE);
        System.out.println(l);

        System.out.println("-------------------------------------------------------");
//        while (stmtIterator.hasNext()){
//            Statement s=stmtIterator.next();
//
//            System.out.println(s);
//        }



    }


    static String splitCamelCase(String s) {
        s=s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        s=s.replaceAll("_","");
        s=s.replaceAll("[ ]{2,4}"," ");
        return  s;
    }

    static void wordnetSearch() {
        try {
            JWNL.initialize(new FileInputStream("D:/libraries/jwnl14-rc2/config/file_properties.xml"));
            OntoManager om=OntoManager.getInstance();
            om.load();
        } catch (JWNLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Dictionary dic = Dictionary.getInstance();
        IndexWord indexWord = null;
        Synset[] set = null;
        try {
            indexWord = dic.getIndexWord(POS.ADJECTIVE, "expensive");

            set = indexWord.getSenses();
            System.out.println(set[0].getLexFileName());
           // System.out.println(set[1].getLexFileName());
            System.out.println(set[0].getWord(0).getLemma());

            System.out.println("_________________entailments");
            Synset ss=set[0];
            for(Object pnt:PointerType.getAllPointerTypes()){
                PointerType pt= (PointerType) pnt;
                Pointer[] pointers = ss.getPointers(pt);
                System.out.println(pt.getLabel());
                for(Pointer p:pointers){

                    System.out.println(p.getTargetSynset());
                }

            }


            // LinguisticEnhancer le=new LinguisticEnhancer();
          //  Synset derivationallyRelatedFrom = le.getDerivationallyRelatedFrom(ss.getWord(0).getLemma());
          //  System.out.println(derivationallyRelatedFrom);


//            Pointer[] pointerArr = set[0].getPointers(PointerType.HYPERNYM);
//            for (Pointer x : pointerArr) {
//                Synset target = x.getTargetSynset();
//                System.out.println(target);
//            }
//
//            System.out.println("----------------------hypernym hierarchy");
//            Synset ss=set[0];
//            System.out.println(ss);
//
//            boolean con=true;
//            while(con){
//                Pointer[] pointers = ss.getPointers(PointerType.HYPERNYM);
//                if(pointers.length>0){
//                    ss= pointers[0].getTargetSynset();
//                    System.out.println(ss);
//                }else{
//                    con=false;
//                }
//            }

        } catch (JWNLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    static  void pClass(){

    }
}
