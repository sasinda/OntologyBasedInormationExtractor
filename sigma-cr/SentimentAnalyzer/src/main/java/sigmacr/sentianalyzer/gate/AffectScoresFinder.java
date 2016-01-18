package sigmacr.sentianalyzer.gate;


import sigmacr.sentianalyzer.constants.KeywordsConstants;
import sigmacr.sentianalyzer.constants.PathConstants;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.scoreProducer.SWN;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

public class AffectScoresFinder {

    private String pathToGaz;
    private String affectsGazPath;
    private SWN sentiWordDict;
    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());

    public AffectScoresFinder(String pathToGaz, SWN swn) {
        this.pathToGaz = pathToGaz;
        this.sentiWordDict = swn;

    }

    public void insertAffectsScores() throws SentiAnalyzerException {
        //get the path of affects gazetteer
        affectsGazPath = pathToGaz + PathConstants.AFFECT_FOLDER;
        String wordDirection = null;
        //get all the files in affect gazetteer folder
        File f = new File(affectsGazPath);
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
        Iterator fileIterator = files.iterator();
        //iterate through all the files
        while (fileIterator.hasNext()) {
            BufferedReader buff = null;

            Vector<String> affectVector = new Vector<String>();


            String fileName = fileIterator.next().toString();
            String[] filePathSplit = fileName.split("\\\\");
            String listName = filePathSplit[filePathSplit.length - 1];
            //if the file is not the definition file
            if (!listName.equalsIgnoreCase(PathConstants.DEF_FILE)) {
                  //if the file contains positive words
                if (listName.contains(KeywordsConstants.GOOD)) {
                    wordDirection = KeywordsConstants.POSITIVE;
                } else {
                    wordDirection = KeywordsConstants.NEGATIVE;
                }
                try {
                    buff = new BufferedReader(new FileReader(fileName));

                    String line = "";
                    while ((line = buff.readLine()) != null) {
                       // System.out.println("word: " + line);
                        String[] sentenceSplit = line.split("&");
                        String affectWord = sentenceSplit[0]; //get the affect word
                        Double score = Math.abs(sentiWordDict.findScore(affectWord, wordDirection) * 10);   //find the score of each word
                       // System.out.println("score: " + score);

                        affectVector.add(affectWord + "&rate=" + score);
                    }
                    buff.close();
                } catch (FileNotFoundException e) {
                    log.warning("Couldn't find the file " + fileName);
                    throw new SentiAnalyzerException("Loading file failed"+e);
                } catch (IOException e) {
                    log.warning("Couldn't find the file " + fileName);
                    throw new SentiAnalyzerException("Loading file failed"+e);
                }
                //write the scores to gazetteer files
                File gazFile = new File(affectsGazPath + listName);
                FileWriter fw = null;
                try {
                    fw = new FileWriter(gazFile.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (int i = 0; i < affectVector.size(); i++) {
                        bw.write(affectVector.get(i));
                        bw.newLine();
                    }
                    bw.close();
                } catch (IOException e) {
                    log.warning("Couldn't find the file " + gazFile.getAbsolutePath());
                    throw new SentiAnalyzerException("Loading file failed"+e);
                }


            }




        }
    }
}
