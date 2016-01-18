package com.sigmacr.domain;

import com.mongodb.BasicDBObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hasinthaindrajee
 * Date: 8/22/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryDocument {
    private String category;
    private String modelName;
    private String author;
    private BasicDBObject date;
    private Map<String, BasicDBObject> features;

    public QueryDocument(String category, String modelName, String author, BasicDBObject date) {
        this.category = category;
        this.modelName = modelName;
        this.author = author;
        this.date = date;
        // features = new HashMap<String, Sentiment>();
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setFeatures(Map<String, BasicDBObject> features) {
        this.features = features;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
