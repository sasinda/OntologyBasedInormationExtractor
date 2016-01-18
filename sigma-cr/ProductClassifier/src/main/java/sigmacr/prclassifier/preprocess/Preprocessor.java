package sigmacr.prclassifier.preprocess;

import com.sigmacr.commons.DataFacade;
import org.apache.log4j.Logger;
import sigmacr.prclassifier.ClassifierException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Preprocessor {

    private Logger log = Logger.getLogger(Preprocessor.class.getName());

    private String outputPath;

    public Preprocessor() {
        File originalReviewsPath = new File(DataFacade.getInstance().getOriginalReviewsPath());
        File outputFolder = new File(originalReviewsPath.getParentFile(),"classifier_temp");
        if(outputFolder.exists()){
            if(outputFolder.isDirectory()){
                for (File file : outputFolder.listFiles()) {
                    file.delete();
                }
            } else{
                outputFolder.delete();
                outputFolder.mkdir();
            }
        } else {
            outputFolder.mkdir();
        }
        outputPath = outputFolder.getAbsolutePath();
    }

    /**
     * Preprocess the data before forwarding to the classifier
     * @param dataPath Path of the directory where the data is
     * @throws ClassifierException
     */
    public void preProcessTrainingReviews(String dataPath, String outputPath) throws ClassifierException {
        this.outputPath = outputPath;
        prepareOutputDirectory();


        //process the files
        File dir = new File(dataPath);
        if(dir.isDirectory()){
            processTrainingDirectory(dataPath);
        } else {
            log.error("Classification failed, Given path '"+dataPath+"' is not a directory");
            throw new ClassifierException("Classification failed, Given path is not a directory");
        }
    }

    /**
     * process the files inside the given directory. If it has directories inside the method will call recursively
     * for those directories.
     * @param dirPath The directory path
     */
    private void processTrainingDirectory(String dirPath){
        log.info("Processing directory "+dirPath);
        File dir = new File(dirPath);
        for (File file : dir.listFiles()){
            if(file.isFile()){
                String out = outputPath +"/"+dir.getName()+"/"+file.getName();
                processFile(file.getAbsolutePath(), out);
            } else if(file.isDirectory()){
                processTrainingDirectory(file.getAbsolutePath());
            }
        }
    }

    /**
     * Process the file in the given path. Apply the rules to remove unnecessary contents and copy the preprocessed
     * file to the output directory.
     * @param filePath
     */
    private void processFile(String filePath, String outputFilePath){
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            File destination = new File(outputFilePath);
            File destinationParent = destination.getParentFile();
            if(!destinationParent.exists()){
                destinationParent.mkdirs();
            }
            PrintWriter writer = new PrintWriter(destination);
            while (reader.hasNextLine()){
                String line = reader.nextLine();
                writer.println(processLine(line));
            }
            reader.close();
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            log.error("File not found "+filePath);
        }
        log.debug("Processed "+filePath);
    }

    public void preprocessPredictionReviews(String dataPath, String outputPath){
        this.outputPath = outputPath;
        prepareOutputDirectory();
        File dir = new File(dataPath);
        for (File file : dir.listFiles()){
            if(file.isFile()){
                String out = outputPath+"/"+file.getName();
                processFile(file.getAbsolutePath(), out);
            }
        }
    }

    private void prepareOutputDirectory(){
        //Prepare output directory
        File outputFolder = new File(outputPath);
        if(outputFolder.exists()){
            if(outputFolder.isDirectory()){
                for (File file : outputFolder.listFiles()) {
                    file.delete();
                }
            } else{
                outputFolder.delete();
                outputFolder.mkdir();
            }
        } else {
            outputFolder.mkdir();
        }
    }

    /**
     * Apply rules to remove unnecessary contents
     * @param line
     * @return
     */
    private String processLine (String line){
        return line.toLowerCase().replaceAll("[[\\s\\W^[$@%]]]+"," ");
    }
}


