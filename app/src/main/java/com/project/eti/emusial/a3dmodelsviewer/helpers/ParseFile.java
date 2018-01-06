package com.project.eti.emusial.a3dmodelsviewer.helpers;

import android.content.Context;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.FloatProperty;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by emusial on 1/4/18.
 */

public class ParseFile {

    protected static int lastPositionIndex = 0;
    protected static int lastNormalIndex = 0;
    protected static int lastTexIndex = 0;
    protected static int length = 0;

    protected static String mFileName = "TexturedCube.txt";
    protected static String mPath = "/mnt/sdcard/3DModelViewer/";
    final static String TAG = ParseFile.class.getName();

    public static String ReadFile() {
        String line = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(mPath + mFileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }

            fileInputStream.close();
            line = stringBuilder.toString();
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }

        // GetPositionData(line);
        // GetNormalData(line);
        // GetNumberOfVertices(line);

        return line;
    }

    public static float[] GetPositionData(String data) {
        String positionData = null;
        lastPositionIndex = data.lastIndexOf('x');

        positionData = data.substring(0, lastPositionIndex);
        positionData = positionData.replaceAll("f", "");
        positionData = positionData.replaceAll(" ", "");

        float[] parsedPositionData = new float[positionData.length()];
        int lastIdx = positionData.lastIndexOf(',');
        int prevIdx = 0;
        int nextIdx = 0;
        int i = 0;

        do {
            nextIdx = positionData.indexOf(",", prevIdx + 1);
            parsedPositionData[i] = Float.parseFloat((positionData.substring(prevIdx, nextIdx)).replaceAll(",",""));
            prevIdx = nextIdx;
            i++;

        } while(nextIdx < lastIdx);

        return parsedPositionData;
    }

    public static float[] GetNormalData(String data) {
        String normalData = null;
        lastNormalIndex = data.lastIndexOf('z');
        normalData = data.substring(lastPositionIndex + 1, lastNormalIndex);

        normalData = normalData.replaceAll("f", "");
        normalData = normalData.replaceAll(" ", "");

        float[] parsedNormalData = new float[normalData.length()];
        int lastIdx = normalData.lastIndexOf(',');
        int prevIdx = 0;
        int nextIdx = 0;
        int i = 0;

        do {
            nextIdx = normalData.indexOf(",", prevIdx + 1);
            parsedNormalData[i] = Float.parseFloat((normalData.substring(prevIdx, nextIdx)).replaceAll(",",""));
            prevIdx = nextIdx;
            i++;

        } while(nextIdx < lastIdx);

        return parsedNormalData;
    }

    public static float[] GetTexData(String data) {
        String texData = null;
        lastTexIndex = data.lastIndexOf('y');

        texData = data.substring(lastNormalIndex + 1, lastTexIndex);

        texData = texData.replaceAll("f", "");
        texData = texData.replaceAll(" ", "");

        float[] parsedTexData = new float[texData.length()];
        int lastIdx = texData.lastIndexOf(',');
        int prevIdx = 0;
        int nextIdx = 0;
        int i = 0;

        do {
            nextIdx = texData.indexOf(",", prevIdx + 1);
            parsedTexData[i] = Float.parseFloat((texData.substring(prevIdx, nextIdx)).replaceAll(",",""));
            prevIdx = nextIdx;
            i++;

        } while(nextIdx < lastIdx);

        return parsedTexData;
    }

    public static float GetNumberOfVertices(String data) {
        String numberOfVertices = null;
        length = data.length();
        numberOfVertices = data.substring(lastTexIndex + 1, length);

        return Float.parseFloat(numberOfVertices);
    }

    public static boolean SaveToFile(String data) {
        try {
            File file = new File(mPath + mFileName);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
            return true;
        }
        catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return false;
    }
}
