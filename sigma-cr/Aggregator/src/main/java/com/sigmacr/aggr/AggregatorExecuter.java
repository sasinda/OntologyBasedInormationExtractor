package com.sigmacr.aggr;


import com.sigmacr.domain.Document;
import com.sigmacr.mongodb.MongoAdapter;
import gate.Gate;
import gate.util.GateException;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/21/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class AggregatorExecuter {


    public void executeOn(File folder){

        Properties p = PropertyHolder.getProperties();
        Gate.setGateHome(new File(p.getProperty("gate.home")));
        try {
            Gate.init();
        } catch (GateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        File[] files = folder.listFiles();
        DocSummaryListBuilder builder=new DocSummaryListBuilder();
        for(File f:files){
            if(f.isFile()){
                  builder.addDoc(f);
            }
        }
        ArrayList<Document> list = builder.getDocSummaryList();
        Aggregator agg=new Aggregator();
        Document aggDoc=agg.aggregateScores(list);

        String result=agg.getResultString();

        MongoAdapter mongo=new MongoAdapter();
        mongo.addDocument(PropertyHolder.mongoAggCollectionName, aggDoc);
        for(Document doc:list){
            mongo.addDocument(doc);
        }

        String metaDirName=PropertyHolder.metaDirName;
        File metaDir = new File(folder + "/" + metaDirName);
        metaDir.mkdirs();
        File f=new File(metaDir,PropertyHolder.aggFileName);
        agg.writeResultsToFile(f);

    }


}
