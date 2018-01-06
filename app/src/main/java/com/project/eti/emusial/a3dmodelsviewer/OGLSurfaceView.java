package com.project.eti.emusial.a3dmodelsviewer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

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

        setEGLContextClientVersion(2);

        renderer = new OGLRenderer(context);
        renderer.setContext(getContext());
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        float[] position = SharedParameters.getLightPosition();

        float lightX = position[0];
        float lightY = position[1];
        float lightZ = position[2];

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
                    lightX += (dx / 50.0f);
                    lightY -= (dy / 50.0f);

                    lightX = (lightX > 4.0f) ? 4.0f : lightX;
                    lightX = (lightX < -4.0f) ? -4.0f : lightX;

                    lightY = (lightY > 4.0f) ? 4.0f : lightY;
                    lightY = (lightY < -4.0f) ? -4.0f : lightY;

                    position[0] = lightX;
                    position[1] = lightY;
                    position[2] = lightZ;

                    SharedParameters.setLightPosition(position);
                }
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;

        return true;
    }
    
}