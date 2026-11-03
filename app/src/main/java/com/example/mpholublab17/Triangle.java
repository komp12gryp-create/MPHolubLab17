package com.example.mpholublab17;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
    private static final int COORDS_PER_VERTEX = 3;
    private static final float[] COORDS = {
            0.0f, 0.622008459f, 0.1f,
            -0.5f, -0.311004243f, 0.1f,
            0.5f, -0.311004243f, 0.1f
    };
    private static final String VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main(){" +
            "gl_Position=uMVPMatrix*vPosition;" +
            "}";
    private static final String FRAGMENT_SHADER =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main(){" +
            "gl_FragColor=vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final int program;
    private final float[] color;

    public Triangle(Context context) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(COORDS.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(COORDS);
        vertexBuffer.position(0);

        int resourceColor = context.getColor(R.color.triangle_color);
        color = new float[]{
                Color.red(resourceColor) / 255.0f,
                Color.green(resourceColor) / 255.0f,
                Color.blue(resourceColor) / 255.0f,
                Color.alpha(resourceColor) / 255.0f
        };

        int vertexShader = MainActivity.loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShader = MainActivity.loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, COORDS_PER_VERTEX * 4, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        int matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, COORDS.length / COORDS_PER_VERTEX);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
