package sigmacr.prclassifier.test;

import sigmacr.prclassifier.SigmaCRClassifier;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class WekaClassifierTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testClassification() throws Exception {
        SigmaCRClassifier classifier = new SigmaCRClassifier();
        File modelPath = new File(ClassifierTestUtils.getResourceHome(),"Training/weka");
        modelPath.mkdirs();
        classifier.trainNewModel(ClassifierTestUtils.getTrainingReviewsDir(), modelPath.getAbsolutePath());
        File predictionDir = new File(ClassifierTestUtils.getPredictionsDataFolder());

        for (File directory : predictionDir.listFiles()) {
            if(directory.isDirectory()){
                System.out.println("Predicting for "+directory.getAbsolutePath());
                classifier.classifyReviews(directory.getAbsolutePath(),new File(modelPath,"latest.model").getAbsolutePath(),
                        new File(modelPath,"latest.filter").getAbsolutePath());
            }
        }





//        preprocessor.preProcessTrainingReviews(ClassifierTestUtils.getTestDataDir());
//        File res = new File(ClassifierTestUtils.getTestDataDir());
//        res.renameTo(new File(DataFacade.getInstance().getTrainingReviewsPath()));
//        Instances trainingInstances = wekaPreProcessor.preprocess(true);
//        Instances ins = predictionPreprocessor.preprocess(ClassifierTestUtils.getTestDataDir());
//        classifier.trainClassifier(trainingInstances);
//        predictions = classifier.doPredictions(ins);
//
//        Attribute classAttribute = trainingInstances.attribute("@@class@@");
//        Map<String,List<String>> totalInstances = new HashMap<String, List<String>>();
//        for (int i = 0; i < ins.numInstances(); i++) {
//            String actual = ins.get(i).stringValue(classAttribute);
//            if(totalInstances.containsKey(actual)){
//                totalInstances.get(actual).add(predictions[i]);
//            }else {
//                ArrayList<String> list = new ArrayList<String>();
//                list.add(predictions[i]);
//                totalInstances.put(actual,list);
//            }
//        }
//        for (Map.Entry<String, List<String>> entry : totalInstances.entrySet()) {
//            int total = entry.getValue().size();
//            int correct = 0;
//            for (String prediction : entry.getValue()) {
//                System.out.println(prediction+" => "+entry.getKey());
//                if(prediction.equals(entry.getKey())){
//                    correct++;
//                }
//            }
//            System.out.println(entry.getKey()+" : "+correct+" correct out of "+total+" ("+String.format("%.2f",
//                    correct*100.0/total)+"%)");

            //Assert that at least half of the instances are correct
//            assertTrue(entry.getKey()+" accuracy test",correct>=total/2);
//        }
    }
}
