package com.sigmacr.aggr;

import com.google.gson.Gson;
import com.sigmacr.domain.Document;
import com.sigmacr.domain.Sentiment;
import com.sigmacr.onto.FeatureClass;
import com.sigmacr.onto.OntoManager;
import com.sigmacr.onto.ProductClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/21/13
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class Aggregator {
    OntoManager om;
    Map<String, Double> finalRslt;
    Map<String, Aggregate> rsltMap;

    public Aggregator() {
        om=OntoManager.getInstance();
        finalRslt=new HashMap<String, Double>();
        om.load();
    }

    public Document aggregateScores(List<Document> docs){
        Document doc0=docs.get(0);
        String category = doc0.getCategory();
        initResultMap(category);

       for(Document doc:docs){
           Map<String, Sentiment> fscores = doc.getFeatureScores();
           Set<String> fnames = fscores.keySet();
           for(String fname:fnames){
               Sentiment sentiment = fscores.get(fname);
               Aggregate aggregate = rsltMap.get(fname);
               if(aggregate==null){
                   aggregate= new Aggregate("new."+fname);
                   rsltMap.put(fname, aggregate);
               }

               double score=sentiment.getScore();
               aggregate.cum_score+=score;
               aggregate.count++;
               System.out.println(aggregate);
           }
       }

        Document aggDoc=new Document(doc0.getCategory(),doc0.getModelName(),"Aggregator",new Date());
        StringBuilder sb=new StringBuilder();
        Set<String> fnames = rsltMap.keySet();
        for(String fname:fnames){
            Aggregate aggregate = rsltMap.get(fname);
            aggregate.aggScore= aggregate.cum_score / aggregate.count;
            aggDoc.getFeatureScores().put(fname, new Sentiment("agg",aggregate.aggScore));
            finalRslt.put(aggregate.fid, aggregate.aggScore);
        }

        return aggDoc;
       //add to mongo



    }


    public void writeResultsToFile(File f){
        try {
            //write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter(f);

            writer.write(getResultString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Result Map = PictureQuality --> {agg}
     * @param prodCategory
     */
    private void initResultMap(String prodCategory){
        ProductClass productCat = om.getProductClass(prodCategory);
        Set<FeatureClass> features = productCat.getFeatures();
        rsltMap=new HashMap<String, Aggregate>();
        for (FeatureClass f:features){
           rsltMap.put(f.getName() ,new Aggregate(f.toDotString()));
        }
        rsltMap.put(PropertyHolder.aggOverallFeatrue , new Aggregate(PropertyHolder.aggOverallFeatrue));

    }

    public String getResultString() {
        Gson gson=new Gson();
        return gson.toJson(finalRslt);
    }


    private class Aggregate{
        String fid;
        double cum_score=0;
        long count=0;
        double aggScore=0;

        private Aggregate(String fid) {
            this.fid = fid;
        }

        @Override
        public String toString() {
            return fid+" "+"cumScore:"+cum_score+"Count "+count+"aggScore"+aggScore;
        }
    }



}
