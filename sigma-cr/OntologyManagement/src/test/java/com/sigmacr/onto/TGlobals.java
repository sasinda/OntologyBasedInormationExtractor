package com.sigmacr.onto;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/14/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TGlobals {
    static OntoManager om;
    static String tv_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#TV";
    static String f_Display_Quality_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#Display_Quality";
    static String f_AntiGlare_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#AntiGlare";
    static String f_PictureQuality_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#PictureQuality";
    static String f_VideoQuality_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#VideoQuality";
    static String f_MotionHandling_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#MotionHandling";
    static String f_MotionRate_uri="http://cse.mrt.ac.lk/com.sigmacr/ontologies/product_ontolology#MotionRate";

    static {
        om=OntoManager.getInstance();
        om.load();
    }

}
