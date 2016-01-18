package sigmacr.sentianalyzer.preprocess;



import sigmacr.sentianalyzer.constants.PathConstants;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.gate.AffectScoresFinder;
import sigmacr.sentianalyzer.wordNet.SynsetProducer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AffectsListsCreator {

    private String preprocessHome;
    private String gazHome;
    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());


    public AffectsListsCreator(String preprocessHome, String gazHome) {
        this.preprocessHome = preprocessHome;
        this.gazHome = gazHome;

    }

    public void createGazList() throws SentiAnalyzerException {

        File f = new File(preprocessHome);
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
        Iterator fileIterator = files.iterator();  //Get all the files in preprocess home
        SynsetProducer synsetProducer = new SynsetProducer();
        while (fileIterator.hasNext()) {     //iterate through all the files
            BufferedReader buffReader = null;
            BufferedWriter buffWriter = null;

            try {
                String inputFilePath = fileIterator.next().toString();
                String[] filePathSplit = inputFilePath.split("\\\\");
                //get te input text file name
                String inputFileName = filePathSplit[filePathSplit.length - 1];
                //get gazetteer file name
                String outputFileName = inputFileName.split("\\.")[0] + ".lst";
                //get gazetter file path
                String outputFilePath = gazHome + PathConstants.AFFECT_FOLDER + outputFileName;

                buffReader = new BufferedReader(new FileReader(new File(inputFilePath).getAbsolutePath()));
                File gazFile = new File(outputFilePath);
                FileWriter fw = new FileWriter(gazFile.getAbsoluteFile());
                buffWriter = new BufferedWriter(fw);
                String line = "";
                while ((line = buffReader.readLine()) != null) {

                    String[] synset = synsetProducer.getSynset(line);
                    if (synset != null) {
                        for (int i = 0; i < synset.length; i++) {
                            buffWriter.write(synset[i]);
                            buffWriter.newLine();
                        }
                    }
                    buffWriter.write(line);
                    buffWriter.newLine();

                }
                buffWriter.close();
                formatFile(outputFilePath);  //format the output file by removing redundant and sorting
            } catch (FileNotFoundException e) {
                log.warning("Cannot find the arf file specified" + e.getMessage());
                throw new SentiAnalyzerException("arf file specified", e);
            } catch (IOException e) {
                log.warning("Cannot find the arf file specified" + e.getMessage());
                throw new SentiAnalyzerException("Cannot find the arf file specified", e);
            }
        }
    }

    /**
     * Sort the file and remove redundant
     */
    private void formatFile(String filePath) throws SentiAnalyzerException {
        ArrayList<String> affects = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath).getAbsolutePath()));

            String line = "";
            while ((line = br.readLine()) != null) {
                if (!affects.contains(line)) {
                    affects.add(line);
                }
            }

            Collections.sort(affects);
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath).getAbsolutePath()));
            for(int i=0;i<affects.size();i++){
                bw.write(affects.get(i));
                bw.newLine();
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            log.warning("Cannot find the gazetteer file specified" + e.getMessage());
            throw new SentiAnalyzerException("Cannot find the gazetteer file specified", e);

        }

    }
}
