package com.sigmacr;

import gate.Corpus;
import gate.CorpusController;
import gate.Gate;
import gate.util.GateException;

import java.io.File;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/28/13
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws GateException {
        //OBIEPipelineBuilder mp=new OBIEPipelineBuilder();
        //mp.execute(null);


        Properties p = PropertyHolder.getProperties();
        Gate.setGateHome(new File(p.getProperty("gate.home")));
        Gate.init();
        //build corpus
        CorpusBuilder cb=new CorpusBuilder();
        File folder=new File("C:\\Users\\Sasinda\\Desktop\\Test docs\\Samsung_UN40EH5300");

        Corpus corpus=cb.createCorpus(folder,true);

        cb.changeFileExtension("txt","xml",folder); // change extentions of all files to xml.

        //build pipeline.

        OBIEPipelineBuilder pipeB=new OBIEPipelineBuilder();
        CorpusController pipeOBIE=pipeB.createPipeline();
        pipeOBIE.setCorpus(corpus);
        pipeOBIE.execute();

//        File f=new File("C:\\Users\\Sasinda\\Desktop\\Test docs\\Samsung_UN40EH5300\\Page-4.txt");
//        String fileName= f.getName().substring(0,f.getName().length()-3);
//        fileName+="xml";
//        f.renameTo(new File(f.getParent(),fileName));
//        System.out.println(f);

    }
}
