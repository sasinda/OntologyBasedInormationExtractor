package com.sigmacr.onto;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/14/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeatureClassTest {
    @org.junit.Test
    public void testGetOntClass() throws Exception {

    }

    @Test
    public void testGetChildFeatures() throws Exception {

    }

    @Test
    public void testGetChildFeatures_Direct_false() throws Exception {

    }

    @Test
    public void testGetParentFeature() throws Exception {

    }

    @Test
    public void testToDotString(){
        OntoManager om=TGlobals.om;
        ProductClass tv=  om.getProductClass(TGlobals.tv_uri);
        FeatureClass f_motionrate = tv.getFeature(TGlobals.f_MotionRate_uri);

        assertEquals(f_motionrate.toDotString()+" is not correct","Display_Quality.VideoQuality.MotionHandling.MotionRate", f_motionrate.toDotString());

    }

    @Test
    public void testEquals() throws Exception {
        OntoManager om=TGlobals.om;
        FeatureClass f_motionRate = om.getFeatureClass(TGlobals.f_MotionRate_uri);
        FeatureClass f_motionRate2 = om.getFeatureClass(TGlobals.f_MotionRate_uri);
        f_motionRate.equals(f_motionRate2);

    }
}
