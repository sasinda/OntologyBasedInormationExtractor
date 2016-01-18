package com.sigmacr.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/20/13
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Document {
    private String category;
    private String modelName;
    private String author;
    private Date date;
    private String location;
    private String srcLink;
    private Float starRate;
    private Map<String, Sentiment> features;

    public Document(String category, String modelName, String author, Date date) {
        this.category = category;
        this.modelName = modelName;
        this.author = author;
        this.date = date;
        // features = new HashMap<String, Sentiment>();
    }

    public Document(String category, String modelName, String author, Date date, String location, String srcLink, float starRate) {
        this.category = category;
        this.modelName = modelName;
        this.author = author;
        this.date = date;
        this.location = location;
        this.srcLink = srcLink;
        this.starRate = starRate;
    }

    public Document(String category, String modelName, String author, Date date, String location) {
        this.category = category;
        this.modelName = modelName;
        this.author = author;
        this.date = date;
        this.location = location;
    }

    public Map<String, Sentiment> getFeatureScores() {
        if (features == null) {
            features = new HashMap<String, Sentiment>();
        }
        return features;
    }

    public void setFeatures(Map<String, Sentiment> features) {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSrcLink() {
        return srcLink;
    }

    public void setSrcLink(String srcLink) {
        this.srcLink = srcLink;
    }

    public float getStarRate() {
        return starRate;
    }

    public void setStarRate(float starRate) {
        this.starRate = starRate;
    }

    @Override
    public String toString() {
        return category + " " +modelName + " " + author + " " + location+" features="+features;
    }
}
