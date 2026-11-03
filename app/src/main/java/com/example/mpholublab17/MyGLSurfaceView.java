package com.example.mpholublab17;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    private float previousX;
    private float previousY;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - previousX;
            float dy = y - previousY;
            renderer.setAngle(renderer.getAngle() + (dx + dy) * 180.0f / 320.0f);
            requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }
}
