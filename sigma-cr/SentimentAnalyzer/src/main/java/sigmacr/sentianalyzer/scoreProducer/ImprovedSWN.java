package sigmacr.sentianalyzer.scoreProducer;

import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.wordNet.SynsetProducer;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: DELL
 * Date: 8/20/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImprovedSWN {

   private HashMap<String, Double> wordsHashmap = new HashMap<String,Double>();
    protected Logger log = Logger.getLogger(SWN.class.getName());


    public void createDictionary() throws SentiAnalyzerException {

             wordsHashmap = new HashMap<String,Double>();

             String pathToISWN ="G:/acadeamic/FYP/experiments/gateFiles/resources/improved-senti-wordnet.txt";
             BufferedReader buff = null;
             try {
                 buff = new BufferedReader(new FileReader(pathToISWN));
                 String line = "";

                 while ((line = buff.readLine()) != null) {

                     if (!"".equalsIgnoreCase(line)) {
                         String[] data = line.split("\t");
                         String[] scoreSplit= data[3].split(":");
                         Double score = Double.parseDouble(scoreSplit[1]);
                         score = score+0.9;
                         if(score<2.9){
                          // score = score*-1;
                         }

                         score = score-2.9;
                         wordsHashmap.put(data[0],score);
                     }
                 }
                 buff.close();
             } catch (FileNotFoundException e) {
                 log.warning("Cannot find the file specified," + pathToISWN + e.getMessage());
                 throw new SentiAnalyzerException("Cannot find the arf file specified", e);
             } catch (IOException e) {
                 log.warning("Cannot find the file specified," + pathToISWN + e.getMessage());
                 throw new SentiAnalyzerException("Cannot find the arf file specified", e);
             }


    }

    public void printData(){

        File gazFile = new File("G:/acadeamic/FYP/experiments/gateFiles/resources/sentiList.txt");
        FileWriter fw = null;
        try {

            fw = new FileWriter(gazFile.getAbsoluteFile());

            BufferedWriter bw = new BufferedWriter(fw);

            for (String s : wordsHashmap.keySet()) {
                System.out.println(s+"   "+wordsHashmap.get(s));
                bw.write(s+"   "+wordsHashmap.get(s));
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public Double findScore(String word) throws SentiAnalyzerException {
        Double score = wordsHashmap.get(word);
        if(score!=null){
            return score;
        }  else{
            SynsetProducer synsetProducer = new SynsetProducer();
            String[] synset = null;

            synset = synsetProducer.getSynset(word);
            if (synset != null) {
                for (int j = 0; j < synset.length; j++) {
                    score=wordsHashmap.get(synset[j]);
                    if(score!=null){
                       break;
                    }
                }
                if(score!=null){
                   return score;
                } else{
                    return 5.0;
                }
            } else{
                return 5.0;
            }
        }
    }

    public HashMap<String, Double> getWordsHashmap() {
        return wordsHashmap;
    }
}
