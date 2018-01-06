package com.project.eti.emusial.a3dmodelsviewer.helpers;

import com.project.eti.emusial.a3dmodelsviewer.R;

/**
 * Created by emusial on 12/31/17.
 */

public class SharedParameters {

    // We are setting rotating object as default
    private static String mAction = "rotate_object";
    private static int mTexture = R.drawable.lava;
    private static int mLightStrength = 50;

    // Light color is white as default
    private static float[] mLightColor = new float[]{
            2, 2, 2, 1
    };

   private static float[] mLightPosition = new float[]{
            2, 2, 1
    };

    public static void setLightPosition(float[] position) {
        mLightPosition = position;
    }

    public static float[] getLightPosition() {
        return mLightPosition;
    }

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
    public static void setLightStrength(int strength) {
        mLightStrength = strength;
    }
    public static int getLightStrength() { return mLightStrength; }
    public static void setTexture (int texture){
        mTexture = texture;
    }
    public static int getTexture() {
        return mTexture;
    }

    }
