package sigmacr.sentianalyzer.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ExecutionException;
import gate.creole.SerialAnalyserController;
import gate.creole.gazetteer.FlexibleGazetteer;
import gate.util.GateException;
import sigmacr.sentianalyzer.constants.PathConstants;
import sigmacr.sentianalyzer.exceptions.SentiAnalyzerException;
import sigmacr.sentianalyzer.util.PropertyHolder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GateAppCreator {

    private String inputHome;
    private String outputHome;
    private String gazetteerHome;
    private String grammarHome;
    private DocumentHandler documentHandler;
    private String[] inputFiles;
    private SerialAnalyserController pipeline;
    protected Logger log = Logger.getLogger(AffectScoresFinder.class.getName());


    public GateAppCreator(String inputHome, String outputHome, String gazetteerHome, String grammarHome, DocumentHandler documentHandler) {
        this.inputHome = inputHome;
        this.outputHome = outputHome;
        this.gazetteerHome = gazetteerHome;
        this.grammarHome = grammarHome;
        this.documentHandler = documentHandler;

    }

    public void runGate() throws SentiAnalyzerException {
        getFiles();   //get all the input file names
        createGatePipeline();
        executePipeline();
    }

    private void getFiles() {

        File f = new File(inputHome);
        ArrayList<File> file_names = new ArrayList<File>(Arrays.asList(f.listFiles()));    //get all the file names in the input directory
        Iterator fileIterator = file_names.iterator();
        inputFiles = new String[file_names.size()];
        int i = 0;
        while (fileIterator.hasNext()) {   //iterate through each file name
            String fileName = fileIterator.next().toString();
            inputFiles[i] = fileName; //put file names in to inputFiles array
            i++;
        }

    }

    private void createGatePipeline() throws SentiAnalyzerException {
        try {
            Properties p= PropertyHolder.getProperties();
            Gate.setGateHome(new File(p.getProperty("gate.home")));
            Gate.init();
            documentHandler.initiateCorpus();

            //Initiate gate resources
            AnnieResoruceSupplier annieResoruceSupplier = new AnnieResoruceSupplier();
            ProcessingResource documentReset = annieResoruceSupplier.getDefaultPrs("gate.creole.annotdelete.AnnotationDeletePR");
            ProcessingResource engTokenizer = annieResoruceSupplier.getDefaultPrs("gate.creole.tokeniser.DefaultTokeniser");
            ProcessingResource posTagger = annieResoruceSupplier.getDefaultPrs("gate.creole.POSTagger");
            ProcessingResource sentenceSplitter = annieResoruceSupplier.getDefaultPrs("gate.creole.splitter.SentenceSplitter");
            //load tools plugin
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURI().toURL());
            ProcessingResource morpher = (ProcessingResource) Factory.createResource("gate.creole.morph.Morph");

            FeatureMap gazetteerFeatures = Factory.newFeatureMap();
            FeatureMap gazetteerAffects = Factory.newFeatureMap();
            FeatureMap gazetteerEmoticons = Factory.newFeatureMap();
            FeatureMap gazetteerNegation = Factory.newFeatureMap();
            FeatureMap grammarPhrases = Factory.newFeatureMap();
            FeatureMap grammarSentiment = Factory.newFeatureMap();
            gazetteerFeatures.put("encoding", "UTF-8");
            gazetteerFeatures.put("gazetteerFeatureSeparator", "&");
            gazetteerFeatures.put("caseSensitive", Boolean.FALSE);
            gazetteerAffects.put("encoding", "UTF-8");
            gazetteerAffects.put("gazetteerFeatureSeparator", "&");
            gazetteerAffects.put("caseSensitive", Boolean.FALSE);
            gazetteerEmoticons.put("encoding", "UTF-8");
            gazetteerEmoticons.put("gazetteerFeatureSeparator", "&");
            gazetteerEmoticons.put("caseSensitive", Boolean.FALSE);
            gazetteerNegation.put("encoding", "UTF-8");
            gazetteerNegation.put("gazetteerFeatureSeparator", "&");
            gazetteerNegation.put("caseSensitive", Boolean.FALSE);

            URL featurelist =
                    new URL(gazetteerHome + PathConstants.FEATURE_LIST);
            gazetteerFeatures.put("listsURL", featurelist);
            URL affectlist =
                    new URL(gazetteerHome + PathConstants.AFFECT_LIST);
            gazetteerAffects.put("listsURL", affectlist);
            URL emoticonlist =
                    new URL(gazetteerHome + PathConstants.EMOTICON_LIST);
            gazetteerEmoticons.put("listsURL", emoticonlist);
            URL negationlist =
                    new URL(gazetteerHome + PathConstants.NEGATION_LIST);
            gazetteerEmoticons.put("listsURL", negationlist);
            URL phraseGrammarURL =
                    new URL(grammarHome + PathConstants.PHRASE_SPLIT_GRAMMAR);
            grammarPhrases.put("grammarURL", phraseGrammarURL);
            URL sentiGrammarURL =
                    new URL(grammarHome + PathConstants.SENTI_EXTRACT_GRAMMAR);
            grammarSentiment.put("grammarURL", sentiGrammarURL);

            ProcessingResource featureGaz = annieResoruceSupplier.getProcessingResource("gate.creole.gazetteer.DefaultGazetteer", gazetteerFeatures);
            ProcessingResource affectGaz = annieResoruceSupplier.getProcessingResource("gate.creole.gazetteer.DefaultGazetteer", gazetteerAffects);
            ProcessingResource emoticonsGaz = annieResoruceSupplier.getProcessingResource("gate.creole.gazetteer.DefaultGazetteer", gazetteerEmoticons);
            ProcessingResource negationGaz = annieResoruceSupplier.getProcessingResource("gate.creole.gazetteer.DefaultGazetteer", gazetteerNegation);

            ProcessingResource phaseSplitter = annieResoruceSupplier.getProcessingResource("gate.creole.ANNIETransducer", grammarPhrases);
            ProcessingResource sentimentAnalyzer = annieResoruceSupplier.getProcessingResource("gate.creole.ANNIETransducer", grammarSentiment);


            FeatureMap flexibleFeatures = Factory.newFeatureMap();
            java.util.ArrayList inputFeatures = new java.util.ArrayList();
            inputFeatures.add("Token.root");
            flexibleFeatures.put("inputFeatureNames", inputFeatures);
            flexibleFeatures.put("gazetteerInst", featureGaz);
            FlexibleGazetteer flexiFeatureGaz = (FlexibleGazetteer) Factory.createResource(
                    "gate.creole.gazetteer.FlexibleGazetteer", flexibleFeatures);

            FeatureMap flexibleAffects = Factory.newFeatureMap();
            java.util.ArrayList inputAffects = new java.util.ArrayList();
            inputAffects.add("Token.root");
            flexibleAffects.put("inputFeatureNames", inputAffects);
            flexibleAffects.put("gazetteerInst", affectGaz);
            FlexibleGazetteer flexiAffectsGaz = (FlexibleGazetteer) Factory.createResource(
                    "gate.creole.gazetteer.FlexibleGazetteer", flexibleAffects);


            SerialAnalyserController tempPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController", Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE");
            setPipeline(tempPipeline);
            documentHandler.createCorpus(inputFiles);
            pipeline.setCorpus(documentHandler.getCorpus());
            pipeline.add(documentReset);
            pipeline.add(engTokenizer);
            pipeline.add(sentenceSplitter);
            pipeline.add(posTagger);
            pipeline.add(morpher);
            pipeline.add(flexiFeatureGaz);
            pipeline.add(flexiAffectsGaz);
            pipeline.add(emoticonsGaz);
            pipeline.add(negationGaz);
            pipeline.add(phaseSplitter);
            pipeline.add(sentimentAnalyzer);


        } catch (MalformedURLException e) {
            log.warning("Couldn't find the URL specified");
            throw new SentiAnalyzerException("Couldn't find the URL specified"+e);
        } catch (GateException e) {
            log.warning("Gate exception occurred while loading processing resources");
            throw new SentiAnalyzerException("Gate exception occurred while loading processing resources"+e);
        } catch (IOException e) {
            log.warning("Loading file failed");
            throw new SentiAnalyzerException("Loading file failed"+e);
        }

    }

    private void executePipeline() throws SentiAnalyzerException {
        try {
            getPipeline().execute();
            System.out.println("pipline executed");

        } catch (ExecutionException e) {
            log.warning("Failed to  execute the gate pipeline");
            throw new SentiAnalyzerException("Failed to  execute the gate pipeline"+e);
        }
    }

    /**
     * @return the inputHome
     */
    public String getInputHome() {
        return inputHome;
    }

    /**
     * @return the outputHome
     */
    public String getOutputHome() {
        return outputHome;
    }

    /**
     * @return the inputFiles
     */
    public String[] getInputFiles() {
        return inputFiles;
    }


    /**
     * @return the pipeline
     */
    public SerialAnalyserController getPipeline() {
        return pipeline;
    }

    /**
     * @param pipeline the pipeline to set
     */
    public void setPipeline(SerialAnalyserController pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * @return the resourceHome
     */
    public String getResourceHome() {
        return gazetteerHome;
    }
    /**
     * @param outputFiles the outputFiles to set
     */
}
