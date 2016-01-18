package com.sigmacr.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/21/13
 * Time: 7:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sentiment {
    String  affect;
    double  score;
    List<String> words;
    List<Double> scores;
    boolean isMultiple;

    public Sentiment(String affect, double score) {
//
//        this.scores = new ArrayList<Double>(2);
//        this.words.add(affect);
//        this.scores.add(score);
        this.affect=affect;
        this.score=score;
    }

    public String getAffect() {
        return affect;
    }

    public void setAffect(String affect) {
        this.affect = affect;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addWordScorePair(String affect, double score) {
        getWords();  //to init if null
        getScores();
        words.add(affect);
        scores.add(score);
    }

    public List<String> getWords() {
        if(words ==null){
            this.words = new ArrayList<String>(2);
        }
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<Double> getScores() {
        if(scores==null){
            scores=new ArrayList<Double>(2);
        }
        return scores;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
    }
}
