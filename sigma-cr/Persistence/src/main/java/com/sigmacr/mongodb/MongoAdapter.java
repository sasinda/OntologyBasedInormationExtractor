package com.sigmacr.mongodb;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.sigmacr.domain.Document;
import com.sigmacr.domain.Product;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hasinthaindrajee
 * Date: 8/20/13
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MongoAdapter {
    DB db;

    public MongoAdapter(){
        init();
    }
    public void init(){
        try{
            MongoClient mongoClient = new MongoClient( DBConf.host , DBConf.port );
            System.out.println("successfully connected to MongoDB" +DBConf.host +" port :"+DBConf.port);
            db = mongoClient.getDB(DBConf.dbName);
            } catch (UnknownHostException e){
            System.out.println("error while connecting to database using " +DBConf.host + " port "+DBConf.port+e);
        }

    }
    public DB getDB(){
        return db;
    }

    public String addDocument(Document doc){
        DBCollection catogery_collection;
        catogery_collection = db.getCollection(doc.getCategory());
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject dbDocument= (BasicDBObject)JSON.parse(json);
        WriteResult result=catogery_collection.insert(dbDocument);

        return result.getError();

    }
    public String addDocument(String collection, Document doc){
        DBCollection dbcollection;
        dbcollection = db.getCollection(collection);
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject dbDocument= (BasicDBObject)JSON.parse(json);
        WriteResult result=dbcollection.insert(dbDocument);

        return result.getError();

    }


    public DBCursor get_from_KeyValue(String collection,String key, String value){
        DBCollection dbcollection;
        dbcollection = db.getCollection(collection);
        BasicDBObject query = new BasicDBObject();
        query.append(key,value);
        return dbcollection.find(query);
    }

    public void addProduct(Product product){
        DBCollection product_collection;
        product_collection = db.getCollection("Products");
        Gson gson = new Gson();
        String json = gson.toJson(product);
        BasicDBObject dbDocument= (BasicDBObject)JSON.parse(json);
        WriteResult result=product_collection.insert(dbDocument);
    }

    public DBCursor get_from_KeyValue(String collection,String key, int value){
        DBCollection dbcollection;
        dbcollection = db.getCollection(collection);
        BasicDBObject query = new BasicDBObject();
        query.append(key,value);
        return dbcollection.find(query);
    }

    public DBCursor get_from_document(Document doc){
        DBCollection dbcollection;
        dbcollection = db.getCollection(doc.getCategory());
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject query= (BasicDBObject)JSON.parse(json);
        System.out.println(query);
        return dbcollection.find(query);
    }

    public DBCursor get_from_part_of_string(String collection ,String key ,String part_of_string){
        BasicDBList and_query_list=new BasicDBList();
        BasicDBObject temp_string_part=null;
        String[] name_parts=part_of_string.split(" ");
        for(String part:name_parts){
           BasicDBObject temp_model_name = new BasicDBObject("$regex",".*"+part+".*");
           temp_string_part = new BasicDBObject(key,temp_model_name);
           and_query_list.add(temp_string_part);
        }

        DBCollection dbcollection;
        dbcollection = db.getCollection(collection);
        BasicDBObject query = new BasicDBObject("$and",and_query_list);
        System.out.println(query.toString());
        return dbcollection.find(query);




    }

    public DBCursor getDocsWith_greaterThan(Document doc,String key, Double score){
        DBCollection dbcollection;
        dbcollection = db.getCollection(doc.getCategory());
        //db.products.find({ "$and" : [ { "modelName" : { "$regex" : ".*sony.*"}},{ "modelName" : { "$regex" : ".*arx.*"}}]})
         BasicDBList list = new BasicDBList();
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject doc_query= (BasicDBObject)JSON.parse(json);
        list.add(doc_query);
         BasicDBObject query_and = new BasicDBObject(key,new BasicDBObject("$gt",score));
         list.add(query_and);
         BasicDBObject query = new BasicDBObject("$and",list);
        return dbcollection.find(query);
    }

    public DBCursor getDocsWith_greaterThanOrEqual(Document doc,String key, Double score){
        DBCollection dbcollection;
        dbcollection = db.getCollection(doc.getCategory());
        BasicDBList list = new BasicDBList();
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject doc_query= (BasicDBObject)JSON.parse(json);
        list.add(doc_query);
        BasicDBObject query_and = new BasicDBObject(key,new BasicDBObject("$gte",score));
        list.add(query_and);
        BasicDBObject query = new BasicDBObject("$and",list);
        return dbcollection.find(query);
    }

    public DBCursor getDocsWith_lessThanOrEqual(Document doc,String key, Double score){
        DBCollection dbcollection;
        dbcollection = db.getCollection(doc.getCategory());
        BasicDBList list = new BasicDBList();
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject doc_query= (BasicDBObject)JSON.parse(json);
        list.add(doc_query);
        BasicDBObject query_and = new BasicDBObject(key,new BasicDBObject("$lte",score));
        list.add(query_and);
        BasicDBObject query = new BasicDBObject("$and",list);
        return dbcollection.find(query);
    }

    public DBCursor getDocsWith_lessThan(Document doc,String key, Double score){
        DBCollection dbcollection;
        dbcollection = db.getCollection(doc.getCategory());
        BasicDBList list = new BasicDBList();
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject doc_query= (BasicDBObject)JSON.parse(json);
        list.add(doc_query);
        BasicDBObject query_and = new BasicDBObject(key,new BasicDBObject("$lt",score));
        list.add(query_and);
        BasicDBObject query = new BasicDBObject("$and",list);
        return dbcollection.find(query);
    }

    public DBCursor getDocsWith_betweenRange(Document doc,String key, Double score1,Double score2){
        DBCollection dbcollection;
        dbcollection = db.getCollection(doc.getCategory());
        BasicDBList list = new BasicDBList();
        Gson gson = new Gson();
        String json = gson.toJson(doc);
        BasicDBObject doc_query= (BasicDBObject)JSON.parse(json);
        list.add(doc_query);
        BasicDBObject less_query_and = new BasicDBObject(key,new BasicDBObject("$gt",score1));
        list.add(less_query_and);
        BasicDBObject greater_query_and = new BasicDBObject(key,new BasicDBObject("$lt",score2));
        list.add(greater_query_and);
        BasicDBObject query = new BasicDBObject("$and",list);
        System.out.println(query);
        return dbcollection.find(query);
    }

}


