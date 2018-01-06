package com.project.eti.emusial.a3dmodelsviewer.meshes;

import com.project.eti.emusial.a3dmodelsviewer.helpers.ParseFile;

// Klasa reprezentująca oteksturowaną skrzynię.
public class TexturedCubeMesh extends BaseMesh
{
    public TexturedCubeMesh()
    {
        String line = ParseFile.ReadFile();
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
