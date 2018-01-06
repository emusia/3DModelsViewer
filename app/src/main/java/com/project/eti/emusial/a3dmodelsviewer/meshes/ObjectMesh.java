package com.project.eti.emusial.a3dmodelsviewer.meshes;

import android.util.Log;

import com.project.eti.emusial.a3dmodelsviewer.helpers.ParseFile;

// Klasa reprezentująca oteksturowaną skrzynię.
public class ObjectMesh extends BaseMesh
{
    public ObjectMesh(String file)
    {
        String line = ParseFile.ReadFile(file);
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
}
