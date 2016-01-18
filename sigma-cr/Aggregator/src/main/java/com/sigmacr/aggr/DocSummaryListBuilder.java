package com.sigmacr.aggr;

import com.sigmacr.domain.Document;
import com.sigmacr.domain.Sentiment;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ResourceInstantiationException;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

public class DocSummaryListBuilder {


    ArrayList<Document> list;
    Document currDoc;
    Map<String, Sentiment> currFMap;

    DocSummaryListBuilder() {
        list = new ArrayList<Document>(1000);
    }

    public void addDoc(File f) {
        try {
            gate.Document doc = Factory.newDocument(f.toURI().toURL());
            doc.getAnnotations();
            String category = (String) doc.getFeatures().get("category");
            String modelName = (String) doc.getFeatures().get("modelName");
            String author = (String) doc.getFeatures().get("author");
            String date = (String) doc.getFeatures().get("date");
            String location = (String) doc.getFeatures().get("gate.SourceURL");
            String srcLink = (String) doc.getFeatures().get("srcLink");
            Float starRate = (Float) doc.getFeatures().get("starRate");


            DateFormat df = DateFormat.getDateInstance();
            Date dt=null;
            if(date!=null){
                dt = df.parse(date);
            }
            currDoc=new Document(category,modelName,author,dt,location,srcLink,starRate);
            currFMap = currDoc.getFeatureScores();
            list.add(currDoc);

            /**
             *   Extract results from gate doc
             */

            AnnotationSet sentimentset = doc.getAnnotations().get("Sentiment");
//        AnnotationSet features=doc.getAnnotations().get("Feature");
//        AnnotationSet affects=doc.getAnnotations().get("Affect");

            Iterator<Annotation> sentiments = sentimentset.iterator();
            while (sentiments.hasNext()) {
                Annotation sentiment = sentiments.next();
                FeatureMap atribs = sentiment.getFeatures();
                String affect= (String) atribs.get("affect");
                String opinion= (String) atribs.get("opinion");
                Double rate= (Double) atribs.get("rate");
                String feature= (String) atribs.get("feature");
                if(feature==null || feature.endsWith("#Feature")){
                     feature=PropertyHolder.aggOverallFeatrue;
                }
                Double score;
                if(opinion.equals("positive")){
                    score=rate;
                }else{
                    score=-rate;
                }
                String uri=feature;
                if(uri.startsWith("http")){
                    feature=uri.substring(uri.indexOf('#')+1);
                }

                Sentiment fSentiment = currFMap.get(feature);
                if( fSentiment ==null){
                    currFMap.put(feature, new Sentiment(affect,score));
                }else {
                    //add additional affects to affect list in sentiment.
                   if(fSentiment.getWords().size()==0){
                       fSentiment.addWordScorePair(fSentiment.getAffect(), fSentiment.getScore());
                   }
                    fSentiment.addWordScorePair(affect, score);
                }

                System.out.println(currDoc);
            }
        } catch (ResourceInstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void addDoc(String category, String modelName, String author, Date date) {
        currDoc = new Document(category, modelName, author, date);
        currFMap = currDoc.getFeatureScores();
        list.add(currDoc);
    }

    private void addFeatureScore(String feature, Sentiment score) {
        currFMap.put(feature, score);
    }

    public ArrayList<Document> getDocSummaryList() {
        return list;
    }


}


