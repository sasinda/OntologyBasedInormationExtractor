package com.sigmacr.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 6/3/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class LingUtil {

    static List<String> stopWords;
    static {
        stopWords=new ArrayList<String>(25) ;
        Properties properties=new Properties();
        try {
            properties.load(LingUtil.class.getResourceAsStream("/LinguE.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String stpWrdLine = properties.getProperty("stopWords");
        Collections.addAll(stopWords, stpWrdLine.split(" "));
    }

    static String splitCamelCase(String s) {
        s=s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        s=s.replaceAll("_","");
        s=s.replaceAll("[ ]{2,4}"," ");
        return  s;
    }

    static boolean isStopWord(String s){
         if(stopWords.contains(s))return true;
         return false;
    };


}
