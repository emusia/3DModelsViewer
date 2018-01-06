package com.project.eti.emusial.a3dmodelsviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.project.eti.emusial.a3dmodelsviewer.helpers.SharedParameters;
import com.project.eti.emusial.a3dmodelsviewer.meshes.CubeMesh;
import com.project.eti.emusial.a3dmodelsviewer.meshes.TexturedCubeMesh;
import com.project.eti.emusial.a3dmodelsviewer.shaders.ShaderProgram;

public class OGLRenderer implements GLSurfaceView.Renderer
{

    public volatile float mObjectAngleX = 0;
    public volatile float mObjectAngleY = 0;

    //protected  float rotation = 0;
    // Macierze modelu, widoku i projekcji.
    protected float[] modelMatrix = new float[16];
    protected float[] viewMatrix = new float[16];
    protected float[] projectionMatrix = new float[16];

    // Iloczyn macierzy modelu, widoku i projekcji.
    protected float[] MVPMatrix = new float[16];
    protected float[] MVMatrix = new float[16];


    // Informacja o tym, z ilu elementów składają się poszczególne atrybuty.
    protected final int POSITION_DATA_SIZE = 3;
    protected final int COLOUR_DATA_SIZE = 4;
    protected final int NORMAL_DATA_SIZE = 3;
    protected final int TEXCOORD_DATA_SIZE = 2;

    // Wartości wykorzystywane przez naszą kamerę. Pierwsze trzy elementy opisują położenie obserwatora,
    // kolejne trzy wskazują na punkt, na który on patrzy, a ostatnie wartości definiują, w którym kierunku
    // jest "góra" (tzw. "up vector").
    protected float[] camera;

    protected ShaderProgram colShaders;
    protected ShaderProgram texShaders;

    // Kontekst aplikacji.
    protected Context appContext = null;

    // Modele obiektów.
    protected TexturedCubeMesh texturedCubeMesh;

    // Adresy tekstur w pamięci modułu graficznego.
    protected int textureDataHandle;
    protected int textureIndex;


    public OGLRenderer()
    {
        camera = new float[]{0.f, 0.f, 1.5f, // pozycja obserwatora
                             0.f, 0.f, 0.f,  // punkt na który obserwator patrzy
                             0.f, 1.f, 0.f}; // "up vector"

        texturedCubeMesh = new TexturedCubeMesh();
    }

    @Override
    // Stworzenie kontekstu graficznego.
    public void onSurfaceCreated(GL10 nieUzywac, EGLConfig config)
    {
        // Kolor tła.
        GLES20.glClearColor(0.05f, 0.05f, 0.1f, 1.0f);

        // Ukrywanie wewnętrznych ścian.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Właczenie sprawdzania głębokości.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        // Wczytanie tekstur do pamięci.
        textureIndex = SharedParameters.getTexture();
        textureDataHandle = readTexture(textureIndex);

        // Utworzenie shaderów korzystających z kolorów wierzchołków.
        colShaders = new ShaderProgram();
        String[] colShadersAttributes = new String[] {"vertexPosition", "vertexColour", "vertexNormal"};
        colShaders.init(R.raw.col_vertex_shader, R.raw.col_fragment_shader, colShadersAttributes, appContext, "kolorowanie");

        // Utworzenie shaderów korzystających z tekst
        texShaders = new ShaderProgram();
        String[] texShadersAttributes = new String[] {"vertexPosition", "vertexTexCoord", "vertexNormal"};
        texShaders.init(R.raw.tex_vertex_shader, R.raw.tex_fragment_shader, texShadersAttributes, appContext, "teksturowanie");
    }

    @Override
    // Metoda wywoływana przy każdym przeskalowaniu okna.
    public void onSurfaceChanged(GL10 nieUzywac, int width, int height)
    {
        Log.d("KSG", "Rozdzielczość: " + width + " x " + height);

        // Rozciągnięcie widoku OpenGL ES do rozmiarów ekranu.
        GLES20.glViewport(0, 0, width, height);

        // Przygotowanie macierzy projekcji perspektywicznej z uwzględnieniem Field of View.
        final float ratio = (float) width / height;
        final float fov = 60;
        final float near = 1.0f;
        final float far = 10000.0f;
        final float top = (float) (Math.tan(fov * Math.PI / 360.0f) * near);
        final float bottom = -top;
        final float left = ratio * bottom;
        final float right = ratio * top;
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

    }

    @Override
    // Metoda renderująca aktualną klatkę.
    public void onDrawFrame(GL10 nieUzywac)
    {
        // Wyczyszczenie buforów głębi i kolorów.
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Ustawienie kamery.
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0, camera[0], camera[1], camera[2], camera[3], camera[4], camera[5], camera[6], camera[7], camera[8]);

        // Transformacja i rysowanie brył.
        GLES20.glUseProgram(texShaders.programHandle); // Użycie shaderów korzystających z teksturowania.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0); // Wykorzystanie tekstury o indeksie 0.

        // Wczytanie tekstur do pamięci
        if (textureIndex != SharedParameters.getTexture())
        {
            textureIndex = SharedParameters.getTexture();
            textureDataHandle = readTexture(textureIndex);
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle); // Podpięcie tekstury skrzyni.
        GLES20.glUniform1i(texShaders._diffuseTextureHandle, 0); // Przekazanie shaderom indeksu tekstury (0).
        Matrix.setIdentityM(modelMatrix, 0); // Zresetowanie pozycji modelu.
        Matrix.translateM(modelMatrix, 0, 0.f, -0.f, -6.5f); // Przesunięcie modelu.
        Matrix.rotateM(modelMatrix, 0, modelMatrix, 0, mObjectAngleX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(modelMatrix, 0, modelMatrix, 0, mObjectAngleY, 1.0f, 0.0f, 0.0f);
        drawShape(texturedCubeMesh.getPositionBuffer(), null, texturedCubeMesh.getNormalBuffer(), texturedCubeMesh.getTexCoordsBuffer(),
                    texShaders, texturedCubeMesh.getNumberOfVertices());

        /*
        GLES20.glUseProgram(colShaders.programHandle); // Użycie shaderów korzystających z teksturowania.
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        Matrix.translateM(modelMatrix, 0, 4.0f, 2.0f, 0.0f); // Przesunięcie modelu.
        drawShape(secondCubeMesh.getPositionBuffer(), null, secondCubeMesh.getNormalBuffer(), secondCubeMesh.getTexCoordsBuffer(),
                colShaders, secondCubeMesh.getNumberOfVertices());
        GLES20.glDisable(GLES20.GL_BLEND);
        */

    }

    protected void drawShape(final FloatBuffer positionBuffer, final FloatBuffer colourBuffer, final FloatBuffer normalBuffer, final FloatBuffer texCoordsBuffer,
                             ShaderProgram shaderProgram, final int numberOfVertices)
    {
        if (positionBuffer == null)
        {
            return;
        }

        // Podpięcie bufora pozycji wierzchołków.
        positionBuffer.position(0);
        GLES20.glVertexAttribPointer(shaderProgram._vertexPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, positionBuffer);
        GLES20.glEnableVertexAttribArray(shaderProgram._vertexPositionHandle);

        // Podpięcie buforów kolorów lub współrzędnych tekstury (w zależności od wykorzystanych shaderów).
        if (colourBuffer != null && shaderProgram._vertexColourHandle >= 0)
        {
            colourBuffer.position(0);
            GLES20.glVertexAttribPointer(shaderProgram._vertexColourHandle, COLOUR_DATA_SIZE, GLES20.GL_FLOAT, false, 0, colourBuffer);
            GLES20.glEnableVertexAttribArray(shaderProgram._vertexColourHandle);
        }
        else if (texCoordsBuffer != null && shaderProgram._vertexTexCoordHandle >= 0)
        {
            texCoordsBuffer.position(0);
            GLES20.glVertexAttribPointer(shaderProgram._vertexTexCoordHandle, TEXCOORD_DATA_SIZE, GLES20.GL_FLOAT, false, 0, texCoordsBuffer);
            GLES20.glEnableVertexAttribArray(shaderProgram._vertexTexCoordHandle);
        }

        // Podpięcie bufora normalnych.
        normalBuffer.position(0);
        GLES20.glVertexAttribPointer(shaderProgram._vertexNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, normalBuffer);
        GLES20.glEnableVertexAttribArray(shaderProgram._vertexNormalHandle);

        // Przemnożenie macierzy modelu, widoku i projekcji.
        Matrix.multiplyMM(MVMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);

        // Przekazanie zmiennych uniform.
        GLES20.glUniformMatrix4fv(shaderProgram._MVPMatrixHandle, 1, false, MVPMatrix, 0);
        GLES20.glUniformMatrix4fv(shaderProgram._MVMatrixHandle, 1, false, MVMatrix, 0);

//        Log.d("POSITION", Float.toString(SharedParameters.getLightPosition()[0]) + ", " + Float.toString(SharedParameters.getLightPosition()[1]) + ", " + Float.toString(SharedParameters.getLightPosition()[2]));
        GLES20.glUniform3fv(shaderProgram._LightPosHandle, 1, SharedParameters.getLightPosition(), 0);
        GLES20.glUniform4fv(shaderProgram._ColourHandle, 1, SharedParameters.getLightColor(), 0);

        // Narysowanie obiektu.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numberOfVertices);
    }

    public void setContext(Context context)
    {
        appContext = context;
    }

    // Metoda wczytująca teksturę z katalogu drawable.
    public int readTexture(int resourceId)
    {
        Log.d("KSG", "Wczytywanie tekstury.");
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0); // Wygenerowanie tekstury i pobranie jej adresu.

        if (textureHandle[0] == 0)
        {
            Log.e("KSG", "Błąd przy wczytywaniu tekstury.");
            return -1;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Wyłączenie skalowania.

        if (appContext == null)
        {
            Log.e("KSG", "appContext = null");
        }
        final Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), resourceId, options);
        Log.d("KSG", " bitmap resolution: "+bitmap.getWidth() + " x " + bitmap.getHeight());

        // Podpięcie tekstury.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        // Ustawienie rodzaju filtrowania tekstury.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Wczytanie zawartości bitmapy do tekstury.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Zwolnienie pamięci zajmowanej przez zmienną bitmap.
        bitmap.recycle();

        return textureHandle[0];
    }
}
