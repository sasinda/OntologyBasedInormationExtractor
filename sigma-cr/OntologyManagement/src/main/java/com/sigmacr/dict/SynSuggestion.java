package com.sigmacr.dict;

import net.didion.jwnl.data.Synset;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/12/13
 * Time: 7:11 PM
 * This class holds a whole synset for a particular label of a ontclass.
 */
public class SynSuggestion implements Comparable<SynSuggestion> {
    String type;    // synInclude, p1Syn, p2Syn etc..
    boolean add=true;// wether to embed this in linguistic enrichment. calculated using scores and filters.
    float scoreAO =0;  // sim to AO. pcls labels= Car Automobile , the number of labels matched with the words in the synset
    float scorePO1 =0;  // wether any superclass has words in the synset. If a superclass has a word in the synset score is the superclas's distance
    float scorePO2 =0;  // wordnet hyperny hierarchy and ontology superclass hierarcy mapping. score= hypernym level * superclass distance.
    Synset synset;

    final static String TYPE_SynInclude="synInclude";
    final static String TYPE_P1Syn="p1Syn";
    final static String TYPE_P2Syn="p2Syn";
    final static String TYPE_P3Syn="p3Syn";
    final static String TYPE_DerivationalyRelatedFrom="derivationallyRelatedFrom";
    final static String TYPE_P1DRel="p1DRel";
    final static String TYPE_P2DRel="p2DRel";
    final static String TYPE_ATTRIBUTE="attribute";
    final static String Type_ASSOCIATION="association";
    final static String TYPE_ASSOCIATION_SYN="associationSyn";
    final static String Type_ASSOCIATION_SIMILARTO ="associationSimilarTo";

    public SynSuggestion(Synset synset) {
        this.synset=synset;

    }

    public SynSuggestion(String type, Synset synset) {
        this.type = type;
        this.synset = synset;
    }

    public float getScore(){
        return Math.min(scoreAO,Math.min(scorePO1, scorePO2));
    }


    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public float getScoreAO() {
        return scoreAO;
    }

    public void setScoreAO(int scoreAO) {
        this.scoreAO = scoreAO;
    }


    public float getScorePO1() {
        return scorePO1;
    }

    public void setScorePO1(int scorePO1) {
        this.scorePO1 = scorePO1;
    }

    public float getScorePO2() {
        return scorePO2;
    }

    public void setScorePO2(int scorePO2) {
        this.scorePO2 = scorePO2;
    }


    @Override
    public int compareTo(SynSuggestion o) {
        return (int)((this.getScore()- o.getScore())*1000);
    }

    @Override
    public String toString() {
        return this.type+" s="+this.getScore()+" s0="+this.getScoreAO()+" s1="+this.getScorePO1()+" s2="+this.getScorePO2()+" ss: "+this.getSynset();
    }
}
