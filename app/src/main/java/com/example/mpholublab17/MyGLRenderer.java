package com.example.mpholublab17;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private Triangle triangle;
    private Square square;
    private float angle;

    public MyGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        int background = context.getColor(R.color.opengl_background);
        GLES20.glClearColor(
                Color.red(background) / 255.0f,
                Color.green(background) / 255.0f,
                Color.blue(background) / 255.0f,
                Color.alpha(background) / 255.0f
        );
        triangle = new Triangle(context);
        square = new Square(context);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        long time = SystemClock.uptimeMillis();
        float sceneAngle = 0.00050f * (int) time;

        Matrix.setLookAtM(
                mViewMatrix,
                0,
                (float) (4.0 * Math.sin(sceneAngle)),
                0.0f,
                (float) (-4.0 * Math.cos(sceneAngle)),
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f,
                0.0f
        );

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        square.draw(mMVPMatrix);

        Matrix.setRotateM(mRotationMatrix, 0, angle, 0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        triangle.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
