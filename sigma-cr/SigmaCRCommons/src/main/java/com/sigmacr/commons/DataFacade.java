package com.sigmacr.commons;

import java.io.File;

public class DataFacade {

    private static DataFacade instance;
    private static final Object lock = new Object();

    private String dataRootPath;
    private String trainingReviewsPath;
    private String originalReviewsPath;
    private String wekaDataPath;


    /**
     * Private constructor for singleton pattern
     */
    private DataFacade(){

        File dataRootFolder = new File(System.getProperty("user.home"),".sigma-cr");
        if(!dataRootFolder.exists()){
            dataRootFolder.mkdir();
        }

        File trainingFolder = new File(dataRootFolder,"Training/Reviews");
        if(!trainingFolder.exists()){
            trainingFolder.mkdirs();
        }

        File reviewsFolder = new File(dataRootFolder,"Reviews/Original");
        if(!reviewsFolder.exists()){
            reviewsFolder.mkdirs();
        }

        File wekaDataFolder = new File(dataRootFolder,"Training/weka");
        if(!wekaDataFolder.exists()){
            wekaDataFolder.mkdirs();
        }


        dataRootPath = dataRootFolder.getAbsolutePath();
        trainingReviewsPath = trainingFolder.getAbsolutePath();
        originalReviewsPath = reviewsFolder.getAbsolutePath();
        wekaDataPath = wekaDataFolder.getAbsolutePath();
    }

    /**
     * Get the singleton instance
     * @return
     */
    public static DataFacade getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance = new DataFacade();
                }
            }
        }
        return instance;
    }

    /**
     * get the root data folder's path
     * @return
     */
    public String getDataRootPath() {
        return dataRootPath;
    }

    /**
     * Get the path to the directory containing training reviews
     * @return
     */
    public String getTrainingReviewsPath() {
        return trainingReviewsPath;
    }

    /**
     * Get the path to the downloaded reviews by IR module
     * @return
     */
    public String getOriginalReviewsPath() {
        return originalReviewsPath;
    }

    public String getWekaModelsPath() {
        return wekaDataPath;
    }
}
