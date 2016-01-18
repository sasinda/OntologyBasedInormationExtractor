package sigmacr.prclassifier.test;

import java.io.File;

public class ClassifierTestUtils {
    private static final String TRAINING_DATA_FOLDER = "data/training/reviews";
    private static final String PREDICTIONS_DATA_FOLDER = "data/test";



    public static String getTrainingReviewsDir(){
        File trainingData = new File(getResourceHome(),TRAINING_DATA_FOLDER);
        return trainingData.getAbsolutePath();
    }

    public static String getPredictionsDataFolder(){
        File predictionsData = new File(getResourceHome(),PREDICTIONS_DATA_FOLDER);
        return predictionsData.getAbsolutePath();
    }

    public static String getResourceHome(){
        return ClassifierTestUtils.class.getResource("/resources").getFile();
    }
}
