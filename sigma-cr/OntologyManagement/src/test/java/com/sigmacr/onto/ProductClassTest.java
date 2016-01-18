package com.sigmacr.onto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 8/14/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductClassTest {
    OntoManager om = TGlobals.om;
    FeatureClass f_motionRate;
    FeatureClass f_MotionHandling;
    FeatureClass f_VideoQuality;
    FeatureClass f_DisplayQuality;

    @Before
    public void initialize() {
        f_motionRate = om.getFeatureClass(TGlobals.f_MotionRate_uri);
        f_MotionHandling = om.getFeatureClass(TGlobals.f_MotionHandling_uri);
        f_VideoQuality = om.getFeatureClass(TGlobals.f_VideoQuality_uri);
        f_DisplayQuality = om.getFeatureClass(TGlobals.f_Display_Quality_uri);
    }


    @org.junit.Test
    public void testGetFeatures() throws Exception {
        ProductClass tv = om.getProductClass(TGlobals.tv_uri);
        Set<FeatureClass> features = tv.getFeatures();
        assertNotNull(features);


        System.out.println(features.contains(f_motionRate));
        assertTrue(features.contains(f_motionRate));
        //check wether feature parent was set correctly.
        for (FeatureClass f : features) {
            if (f.equals(f_motionRate)) {
                assertTrue(f.getParentFeature().equals(f_MotionHandling));
            } else if (f.equals(f_MotionHandling)) {
                assertTrue(f.getParentFeature().equals(f_VideoQuality));
            } else if (f.equals(f_MotionHandling)) {
                assertTrue(f.getParentFeature().equals(f_DisplayQuality));
            } else if (f.equals(f_DisplayQuality)) {
                assertNull(f.getParentFeature());
            }
        }


    }

    @Test
    public void testGetFeatures_topLevel() throws Exception {
        ProductClass tv = om.getProductClass(TGlobals.tv_uri);
        Set<FeatureClass> features = tv.getFeatures(true);
        assertNotNull(features);
    }

    @Test
    public void testGetFeature() throws Exception {
        ProductClass tv = om.getProductClass(TGlobals.tv_uri);
        FeatureClass f_motionR = tv.getFeature(TGlobals.f_MotionRate_uri);
        assertTrue(f_motionR.hasParentFeature(f_DisplayQuality));

    }


    @Test
    public void testHasFeature_param_URI() throws Exception {

    }

    @Test
    public void testHasFeature_param_Fclass() throws Exception {

    }

    @Test
    public void testIsNegative() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {
        ProductClass tv = om.getProductClass(TGlobals.tv_uri);
        ProductClass tv2 = om.getProductClass(TGlobals.tv_uri);
        assertTrue(tv.equals(tv2));
    }

    @Test
    public void testGetOntClass() throws Exception {

    }


}
