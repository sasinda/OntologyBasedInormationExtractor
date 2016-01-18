package com.sigmacr.aggr;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 7/1/13
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyHolder {
   static String mongoAggCollectionName;
    static String metaDirName;
    static String aggFileName;
    static String aggOverallFeatrue;

    static {
        Properties p = getProperties();
        mongoAggCollectionName =p.getProperty("mongoAggColName");
        metaDirName=p.getProperty("metaDirName");
        aggFileName=p.getProperty("aggFileName");
        aggOverallFeatrue=p.getProperty("aggOverallFeature");

    }

    public static Properties getProperties(){
        Properties p=new Properties();
        try {
            p.load(PropertyHolder.class.getResourceAsStream("/aggregator.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return p;
    }
}
