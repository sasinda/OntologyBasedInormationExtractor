package sigmacr.prclassifier;

import com.sigmacr.commons.DataFacade;
import org.apache.log4j.Logger;
import sigmacr.prclassifier.classify.ProductClassifier;
import sigmacr.prclassifier.preprocess.PredictionPreprocessor;
import sigmacr.prclassifier.preprocess.Preprocessor;
import sigmacr.prclassifier.preprocess.WekaPreProcessor;
import weka.core.Instances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SigmaCRClassifier {

    protected Logger log = Logger.getLogger(SigmaCRClassifier.class.getName());

    public void trainNewModel(){

    }

    public void trainNewModel(String reviewPath, String wekaOutputPath) throws ClassifierException {
        File reviewDirectory = new File(reviewPath);
        if(reviewDirectory.exists() && reviewDirectory.isDirectory()){
            try {
                File prepocessOutputDir = new File(DataFacade.getInstance().getDataRootPath(),"classifier_temp");
                prepocessOutputDir.mkdir();
                Preprocessor preprocessor = new Preprocessor();
                preprocessor.preProcessTrainingReviews(reviewPath, prepocessOutputDir.getAbsolutePath());
                WekaPreProcessor wekaPreProcessor = new WekaPreProcessor();
                Instances trainingInstances = wekaPreProcessor.generateTrainingData(prepocessOutputDir.getAbsolutePath(), wekaOutputPath);
                ProductClassifier classifier = new ProductClassifier();
                classifier.trainClassifier(trainingInstances,wekaOutputPath,false);
            } catch (ClassifierException e) {
                log.error("Training Model failed ",e);
                throw e;
            }
        }
    }

    public void classifyReviews(){
    }

    public void classifyReviews(String reviewPath, String modelPath, String wekaFilter) throws ClassifierException {
        File reviewDirectory = new File(reviewPath);
        if(reviewDirectory.exists()&&reviewDirectory.isDirectory()){
            try{
                Preprocessor preprocessor = new Preprocessor();
                File preprocessOutput = new File(DataFacade.getInstance().getDataRootPath(),
                        "predictor_temp/unclassified");
                preprocessor.preprocessPredictionReviews(reviewPath,preprocessOutput.getAbsolutePath());
                PredictionPreprocessor predictionPreprocessor = new PredictionPreprocessor();
                Instances predictionInstances = predictionPreprocessor.preprocess(preprocessOutput.getParentFile().getAbsolutePath
                        (), wekaFilter);
                ProductClassifier classifier = new ProductClassifier();
                String[] predictions = classifier.doPredictions(predictionInstances, modelPath);
                File metaDir = new File(reviewPath,"meta");
                metaDir.mkdir();
                PrintWriter writer = new PrintWriter(new File(metaDir,"meta.txt"));
                for (String prediction : predictions) {
                    writer.println(prediction);
                }
                writer.flush();
                writer.close();
            } catch (ClassifierException e) {
                log.error("Classification failed.",e);
                throw new ClassifierException("Classification failed.",e);
            } catch (FileNotFoundException e) {
                log.error("Unable to write to meta-file.",e);
                throw new ClassifierException("Unable to write to meta file",e);
            }
        }
    }

}
