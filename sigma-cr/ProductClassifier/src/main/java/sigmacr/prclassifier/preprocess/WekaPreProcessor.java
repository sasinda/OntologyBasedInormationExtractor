package sigmacr.prclassifier.preprocess;

import org.apache.log4j.Logger;
import sigmacr.prclassifier.ClassifierException;
import sigmacr.prclassifier.util.FilterUtil;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;

/**
 * .Perform preprocessing using weka filters on the training data sets
 */
public class WekaPreProcessor {

    protected Logger log = Logger.getLogger(WekaPreProcessor.class.getName());
    private Instances loaderInstance;

    /**
     * Generate training data from the specified directory
     * @throws ClassifierException
     */
    public Instances generateTrainingData(String trainingDataPath,String pathToSaveModel) throws ClassifierException {
        log.info("Loading data from "+trainingDataPath);
        TextDirectoryLoader loader = new TextDirectoryLoader();
        try {
            loader.setDirectory(new File(trainingDataPath));
            loaderInstance = loader.getDataSet();
            log.info("Data loaded.");
            applyAndSaveFilters(new File(pathToSaveModel,"latest.filter").getAbsolutePath());
        } catch (IOException e) {
            log.error("Loading train data failed.", e);
            throw new ClassifierException("Loading train data failed", e);
        }
        return loaderInstance;
    }



    private void applyAndSaveFilters(String pathToSaveFilter) throws ClassifierException {
        try {
            log.info("Applying StringToWordVector filter...");
//            String[] options = Utils.splitOptions("-R first-last -W 5000 -prune-rate 20.0 -C -N 0 " +
//                    "-stemmer weka.core.stemmers.NullStemmer -M 3 -tokenizer \"weka.core.tokenizers.NGramTokenizer " +
//                    "-delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\" -max 1 -min 1\"");
//
            String[] options = Utils.splitOptions("-R first-last -W 5000 -prune-rate 20.0 -T -I -N 0 -L -stemmer weka" +
                    ".core.stemmers.NullStemmer -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
            StringToWordVector filter = new StringToWordVector();
            filter.setOptions(options);
            filter.setInputFormat(loaderInstance);
            loaderInstance = Filter.useFilter(loaderInstance, filter);
            log.info("StringToWordVector Filter applied");
            loaderInstance.setClass(loaderInstance.attribute(0));
            FilterUtil.saveFilter(filter, pathToSaveFilter);
        } catch (Exception e) {
            log.error("Failed to apply filter.", e);
            throw new ClassifierException("Data Filtering failed.", e);
        }
    }


}
