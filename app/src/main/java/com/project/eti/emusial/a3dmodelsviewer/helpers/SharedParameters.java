package com.project.eti.emusial.a3dmodelsviewer.helpers;

/**
 * Created by emusial on 12/31/17.
 */

public class SharedParameters {

    // We are setting rotating object as default
    private static String mAction = "rotate_object";

    // Light color is white as default
    private static float[] mLightColor = new float[]{
            1, 1, 1, 1
    };

    public static  void setLightColor(float[] color) {
        mLightColor = color;
    }
    public static float[] getLightColor () {
        return mLightColor;
    }
    public static void setAction(String action){
        mAction = action;
    }
    public static String getAction() {
        return mAction;
    }

}
