package com.sigmacr.onto;

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

    public static Properties getProperties(){
        Properties p=new Properties();
        try {
            p.load(PropertyHolder.class.getResourceAsStream("/linguE.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return p;
    }
}
