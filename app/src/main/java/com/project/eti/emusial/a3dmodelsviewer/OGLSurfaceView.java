package com.project.eti.emusial.a3dmodelsviewer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.project.eti.emusial.a3dmodelsviewer.helpers.SharedParameters;

class OGLSurfaceView extends GLSurfaceView
{
	protected OGLRenderer renderer=null;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public OGLSurfaceView(Context context)
    {
        super(context);

        // Kontekst OpenGL ES 2.0.
        setEGLContextClientVersion(2);

        // Przypisanie renderera do widoku.
        renderer = new OGLRenderer();
        renderer.setContext(getContext());
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        float[] position = SharedParameters.getLightPosition();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if ( y > getHeight() / 2) {
                    dx = dx * (-1);
                }

                if ( x > getWidth() / 2) {
                    dy = dy * (-1);
                }

                if (SharedParameters.getAction() == "rotate_object") {
                    renderer.mObjectAngleX += dx * TOUCH_SCALE_FACTOR;
                    renderer.mObjectAngleY += dy * TOUCH_SCALE_FACTOR;
                } else if (SharedParameters.getAction() == "rotate_light") {
                    // position = new float[] {, 1};
                    SharedParameters.setLightPosition(position);
                    Log.d("POSITION", Float.toString(position[0]) + ", " + Float.toString(position[1]) + ", " + Float.toString(position[2]));
                }

                requestRender();

        }

        mPreviousX = x;
        mPreviousY = y;
        return true;

    }
    
    public OGLRenderer getRenderer()
    {
    	return renderer;
    }
}