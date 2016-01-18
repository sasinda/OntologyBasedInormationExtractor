package sigmacr.sentianalyzer.gate;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.util.InvalidOffsetException;
import gate.util.Out;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


class AnnotAnalyzer {

    private String outputHome;
    private int corpusSize;
    private Corpus corpus;
    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());


    public AnnotAnalyzer(String outputHome, DocumentHandler documentHandler) {

        this.outputHome = outputHome;

        this.corpus = documentHandler.getCorpus();
        this.corpusSize = corpus.size();
    }

    public void analyzeSentiAnnot() throws SentiAnalyzerException {

        Out.println("Analyzing annoations...");


        for (int i = 0; i < corpusSize; i++) {
            BufferedWriter bw = null;
            try {
                //get input document
                gate.Document doc = (gate.Document) corpus.get(i);
                //get the file name of the input document
                String[] filePathSplits = doc.getSourceUrl().toString().split("/");
                String filename = filePathSplits[filePathSplits.length - 1];
                //create a file in output home path with a name equal to input file name
                File outputFile = new File(outputHome + filename);
                FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
                bw = new BufferedWriter(fw);
                //get the default annotation set of the gate document
                AnnotationSet defaultAnnotSet = doc.getAnnotations();
                Set annotTypesRequired = new HashSet();
                annotTypesRequired.add("Sentiment");
                //get "Sentiment" annotation set
                AnnotationSet sentitAnnotationSet = defaultAnnotSet.get(annotTypesRequired);
                //Iterate through each Sentiment annotation
                Iterator iterator = sentitAnnotationSet.iterator();
                while (iterator.hasNext()) {
                    gate.Annotation sentiAnnot = (Annotation) iterator.next();
                    //get the string of the annotation
                    String sentiSentence = doc.getContent().getContent(sentiAnnot.getStartNode().getOffset(), sentiAnnot.getEndNode().getOffset()).toString();
                    //get opinion feature in the annotation
                    String opinion = (String) sentiAnnot.getFeatures().get("opinion");
                    //get score in the sentiment annotation
                    Double rate = (Double) sentiAnnot.getFeatures().get("rate");
                    //write the sentiment in to the output file
                    bw.write("<Sentence>" + sentiSentence + "<Sentence>" + "<opinion>" + opinion + "<opinion>" + "<rate>" + rate + "<rate>");
                    bw.newLine();
                }
            } catch (InvalidOffsetException e) {
                log.warning("Cannot find the output file specified" + e.getMessage());
                throw new SentiAnalyzerException("Cannot find the output file specified", e);
            } catch (IOException e) {
                log.warning("Cannot find the output file specified" + e.getMessage());
                throw new SentiAnalyzerException("Cannot find the output file specified", e);
            } finally {
                try {
                    bw.close();
                } catch (IOException e) {
                    log.warning("Cannot find the output file specified" + e.getMessage());
                    throw new SentiAnalyzerException("Cannot find the output file specified", e);
                }
            }

        }

    }



    /**
     * @return the inputFileName
     */
    /**
     * @return the outputFileName
     */
    public String getOutputFileName() {
        return outputHome;
    }

    /**
     * @return the noOfFiles
     */
    public int getNoOfFiles() {
        return corpusSize;
    }
}
