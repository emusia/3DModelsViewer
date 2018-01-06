package com.project.eti.emusial.a3dmodelsviewer.meshes;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.project.eti.emusial.a3dmodelsviewer.helpers.FileDialog;
import com.project.eti.emusial.a3dmodelsviewer.helpers.ParseFile;

import java.io.File;

// Klasa reprezentująca oteksturowaną skrzynię.
public class ObjectMesh extends BaseMesh
{
    protected String fileName;

    public ObjectMesh(Context context)
    {
        FileDialog fileDialog;
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog((Activity) context, mPath, ".txt");
        fileDialog.addFileListener(new FileDialog.SelectedFileListener() {
            @Override
            public void selectedFile(File file) {
                fileName = file.toString();
                Log.d(getClass().getName(), "selected file " + file.toString());

                String line = ParseFile.ReadFile(fileName);
                Log.d("OBJECTMESH", line);

                if (line != null) {
                    final float[] positionData =  ParseFile.GetPositionData(line);
                    final float[] normalData = ParseFile.GetNormalData(line);
                    final float[] texCoordData = ParseFile.GetTexData(line);
                    numberOfVertices = (int) ParseFile.GetNumberOfVertices(line);

                    positionBuffer = createBuffer(positionData);
                    normalBuffer = createBuffer(normalData);
                    texCoordsBuffer = createBuffer(texCoordData);
                }
            }
        });
        fileDialog.showDialog();
    }
}
