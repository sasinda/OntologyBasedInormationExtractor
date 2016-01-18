package com.sigmacr.aggr;

import com.google.gson.Gson;
import com.sigmacr.domain.Document;
import com.sigmacr.domain.Sentiment;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/20/13
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocSummaryWriter {
    String summFileName;
    String temp_summFileName;
    String metaDirName;
    File summFile;
    File tempFile;

    ArrayList<Document> list;
    Document currDoc;
    Map<String, Sentiment> currFMap;

    boolean _filesOk = false;
    File _currDir = null;

    static DocSummaryWriter singleton = new DocSummaryWriter();

    public static DocSummaryWriter getInstance() {
        return singleton;
    }


    DocSummaryWriter() {
        Properties properties = PropertyHolder.getProperties();
        summFileName = (String) properties.get("docSummaryFileName");
        temp_summFileName = properties.getProperty("tempDocSummaryFileName");
        metaDirName = properties.getProperty("metaDirName");
        list = new ArrayList<Document>(500);
    }

    public void initializeFor(URL docurl, String category, String modelName, String author, Date date) {
       // System.out.println(docurl.toString().substring(6));
        if (_filesOk) {
            try {
                boolean correctInvocation=_currDir.equals(new File(docurl.toURI()));
                if(!correctInvocation){
                   throw new IllegalArgumentException(_currDir.toString()+" all docs should be from this folder. Otherwise close DocSummaryWriter");
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            try {

                File f = new File(docurl.toURI());
                File metaDir = new File(f.getParent() + "/" + metaDirName);
                metaDir.mkdirs();
                summFile = new File(metaDir, summFileName);
                _filesOk= summFile.createNewFile();
                _currDir=f.getParentFile();
               // System.out.println(_currDir);
                //tempFile=new File(metaDir, temp_summFileName);
//            if(isNew){
//                FileWriter writer = new FileWriter(summFile);
//                writer.write("[");
//            }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        currDoc = new Document(category, modelName, author, date);
        currFMap = currDoc.getFeatureScores();
        list.add(currDoc);
    }

    public void markFeature(String feature, Sentiment score) {
        currFMap.put(feature, score);
    }

    public ArrayList<Document> getDocSummaryList() {
        return list;
    }

    public void close(){
        writeDoclistToFile();
    }

    private void writeDoclistToFile() {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        try {
            //write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter(summFile);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void _append(Document d) {
        Gson gson = new Gson();
        String json = gson.toJson(d);
        try {
            //write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter(summFile, true);
            writer.write(",");
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
