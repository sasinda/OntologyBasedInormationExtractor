package sigmacr.prclassifier.util;

import sigmacr.prclassifier.ClassifierException;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;

public class ArffUtil {

    public static void saveInstancesToArff(Instances instances, String path) throws ClassifierException {
        try {
            ArffSaver arffSaver = new ArffSaver();
            arffSaver.setInstances(instances);
            File destination = new File(path);
            arffSaver.setFile(destination);
            arffSaver.writeBatch();
        } catch (IOException e) {
            throw new ClassifierException("Saving arff file failed", e);
        }
    }

    public static Instances loadArff(String path) throws ClassifierException {
        ArffLoader loader = new ArffLoader();
        File arffFile = new File(path);
        try {
            loader.setFile(arffFile);
            return loader.getDataSet();
        } catch (IOException e) {
            throw new ClassifierException("Loading arff file failed", e);
        }
    }
}
