package sigmacr.prclassifier.preprocess;

import org.apache.log4j.Logger;
import sigmacr.prclassifier.ClassifierException;
import sigmacr.prclassifier.util.FilterUtil;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;

import java.io.File;
import java.io.IOException;

/**
 * Prepare the text (reviews) to be used by the classifier to make the predictions
 */
public class PredictionPreprocessor {

    private Instances instances;
    private Filter filter;

    private Logger log = Logger.getLogger(PredictionPreprocessor.class.getName());

    /**
     * Generate the initial data instances using the text files from the given path.
     * NOTE : the text file path should follow the file name pattern "[path] / * / *.txt"
     * (Without spaces on either side of '/' )
     * @param path Path to search the reviews
     * @throws ClassifierException If data is not available at the given path, doesn't follow the pattern or any
     * other I/O failure
     */
    private void generateRawInstances(String path) throws ClassifierException {
        log.info("Preparing initial dataset from "+path);
        TextDirectoryLoader loader = new TextDirectoryLoader();
        try {
            loader.setDirectory(new File(path));
            instances = loader.getDataSet();
            log.info("Initial data set prepared");
        } catch (IOException e) {
            log.error("Loading train data failed.",e);
            throw new ClassifierException("Loading train data failed", e);
        }
    }

    /**
     * Preprocess the data from the given path and create the instances that is ready for classification
     * @param path Path to the data folder
     * @see PredictionPreprocessor#generateRawInstances
     * @return The preprocessed instances
     * @throws ClassifierException If any error occur in preprocessing
     */
    public Instances preprocess(String path,String filterPath) throws ClassifierException {
        try {
            generateRawInstances(path);
            filter = FilterUtil.loadFilter(filterPath);
            instances = Filter.useFilter(instances, filter);
            return instances;
        }catch (Exception e) {
            log.error("Using filter for dataset failed.",e);
            throw new ClassifierException("Using filter for dataset failed",e);
        }
    }
}
