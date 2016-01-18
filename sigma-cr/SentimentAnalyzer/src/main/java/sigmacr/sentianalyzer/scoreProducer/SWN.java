package sigmacr.sentianalyzer.scoreProducer;


import sigmacr.sentianalyzer.constants.KeywordsConstants;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.wordNet.SynsetProducer;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

public class SWN {

    // private String home;
    private String pathToSWN;
    private HashMap<String, Double> affectDict;
    private HashMap<String, Vector<Double>> wordsHashmapPositive;
    private HashMap<String, Vector<Double>> wordsHashmapNegative;
    protected Logger log = Logger.getLogger(SWN.class.getName());

    public SWN(String pathToSWN) throws SentiAnalyzerException {
        this.pathToSWN = pathToSWN;
        this.createDictionary("positive");
        this.createDictionary("negative");
    }


    /**
     *    Create hashmap for senti word's rates
     * @param affectDirection
     * @throws SentiAnalyzerException
     */
    public void createDictionary(String affectDirection) throws SentiAnalyzerException {
        affectDict = new HashMap<String, Double>();


        HashMap<String, Vector<Double>> wordsHashmap = new HashMap<String, Vector<Double>>();

        try {
            BufferedReader buff = new BufferedReader(new FileReader(pathToSWN));
            String line = "";

            while ((line = buff.readLine()) != null) {

                if (!"".equalsIgnoreCase(line)) {
                    Double score;
                    String[] data = line.split("\t");
                    if (affectDirection.equalsIgnoreCase("positive")) {
                        score = Double.parseDouble(data[2]); //get the positive value
                    } else {
                        score = Double.parseDouble(data[3]);   //get the negative value
                    }
                    String[] words = data[4].split(" ");
                    for (String w : words) {
                        String[] w_n = w.split("#");
                        if (wordsHashmap.containsKey(w_n[0])) {
                            Vector<Double> scores = wordsHashmap.get(w_n[0]);
                            scores.add(score);   //add the score to the vector
                            wordsHashmap.put(w_n[0], scores);   //put it in the hash map again

                        } else {
                            Vector<Double> scores = new Vector<Double>();
                            scores.add(score);
                            wordsHashmap.put(w_n[0], scores);
                        }
                    }
                }
                if (affectDirection.equalsIgnoreCase("positive")) {
                    wordsHashmapPositive = wordsHashmap;
                } else {
                    wordsHashmapNegative = wordsHashmap;
                }
            }

        } catch (FileNotFoundException e) {
            log.warning("Cannot find the file specified," + pathToSWN + e.getMessage());
            throw new SentiAnalyzerException("Cannot find the arf file specified", e);
        } catch (IOException e) {
            log.warning("Cannot find the file specified," + pathToSWN + e.getMessage());
            throw new SentiAnalyzerException("Cannot find the arf file specified", e);
        }
    }

    /**
     * Find the scores of an opinion word
     *
     * @param word
     * @param wordDirection
     * @return the score of the word
     */
    public Double findScore(String word, String wordDirection) throws SentiAnalyzerException {
        Vector<Double> affScoreVec = null;
        word = word.replace(" ", "_");
        Double totalScore = 0.0, averageScore = 0.0;

        if (KeywordsConstants.POSITIVE.equalsIgnoreCase(wordDirection)) {
            affScoreVec = getWordsHashmapPositive().get(word); //get the score vector of the affect word in positive map
        } else {
            affScoreVec = getWordsHashmapNegative().get(word);  //get the score vector of the affect word in negative map
        }

        if (affScoreVec != null) {

            averageScore = calAverageScore(affScoreVec);

            return averageScore;
        } else {
            SynsetProducer synsetProducer = new SynsetProducer();
            String[] synset = null;

            synset = synsetProducer.getSynset(word);
            if (synset != null) {
                for (int j = 0; j < synset.length; j++) {
                    if (KeywordsConstants.POSITIVE.equalsIgnoreCase(wordDirection)) {
                        affScoreVec = getWordsHashmapPositive().get(synset[j]);
                    } else {
                        affScoreVec = getWordsHashmapNegative().get(synset[j]);
                    }
                    if (affScoreVec != null) {
                        break;
                    }
                }
            }


            if (affScoreVec != null) {  //if score vector is not null
                averageScore = calAverageScore(affScoreVec);   //calculate the average score from the score vector
                return averageScore;
            } else {
                return 5.0;     //else return the default value
            }

        }
    }

    /**
     * Calculate the average score of vector scores
     *
     * @param affScoreVec
     * @return average score
     */
    public Double calAverageScore(Vector<Double> affScoreVec) {
        Double totalScore = 0.0, averageScore = 0.0;
        for (int i = 0; i < affScoreVec.size(); i++) {
            totalScore = totalScore + affScoreVec.get(i);
        }

        averageScore = totalScore / affScoreVec.size();
        return averageScore;
    }

    /**
     * @return the wordsHashmapPositive
     */
    public HashMap<String, Vector<Double>> getWordsHashmapPositive() {
        return wordsHashmapPositive;
    }

    /**
     * @return the wordsHashmapNegative
     */
    public HashMap<String, Vector<Double>> getWordsHashmapNegative() {
        return wordsHashmapNegative;
    }


}
