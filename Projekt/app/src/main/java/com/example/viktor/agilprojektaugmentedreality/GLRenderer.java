package com.example.viktor.agilprojektaugmentedreality;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ras on 31/03/15.
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    // Create the 3D object
    private Object object;

    /** This is a handle to our per-vertex cube shading program. */
    private int mPerVertexProgramHandle;

    //Used for the GLView object rotation.
    public float mDeltaX = 0;
    public float mDeltaY = 0;

    /** Store the accumulated rotation. */
    private final float[] mAccumulatedRotation = new float[16];


    public GLRenderer() {

        // Initialize our object
        object = new Object();

    }

    protected String getVertexShader() {
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;        \n"       // A constant representing the combined model/view/projection matrix.
                        + "uniform mat4 u_MVMatrix;       \n"       // A constant representing the combined model/view matrix.
                        + "uniform vec3 u_LightPos;       \n"       // The position of the light in eye space.

                        + "attribute vec4 a_Position;     \n"       // Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"       // Per-vertex color information we will pass in.
                        + "attribute vec3 a_Normal;       \n"       // Per-vertex normal information we will pass in.

                        + "varying vec4 v_Color;          \n"       // This will be passed into the fragment shader.

                        + "void main()                    \n"       // The entry point for our vertex shader.
                        + "{                              \n"
                        // Transform the vertex into eye space.
                        + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
                        // Transform the normal's orientation into eye space.
                        + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
                        // Will be used for attenuation.
                        + "   float distance = length(u_LightPos - modelViewVertex);             \n"
                        // Get a lighting direction vector from the light to the vertex.
                        + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
                        // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
                        // pointing in the same direction then it will get max illumination.
                        + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.3);       \n"
                        // Attenuate the light based on distance.
                        + "   diffuse = diffuse * (1.0 / (1.0 + (0.005 * distance * distance)));  \n"
                        // Multiply the color by the illumination level. It will be interpolated across the triangle.
                        + "   v_Color = a_Color * diffuse;                                       \n"
                        // gl_Position is a special variable used to store the final position.
                        // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                        + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                        + "}                                                                     \n";

        return vertexShader;
    }


    protected String getFragmentShader() {

        final String fragmentShader =
                "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "varying vec4 v_Color;          \n"       // This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "void main()                    \n"       // The entry point for our fragment shader.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"       // Pass the color directly through the pipeline.
                        + "}                              \n";

        return fragmentShader;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the background color to black ( rgba ).
        GLES20.glClearColor(0.6f, 0.6f, 0.6f, 1.0f);

        // Enable the depth test for back face culling
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(object.getViewMatrix(), 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();

        final int vertexShaderHandle = object.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = object.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mPerVertexProgramHandle = object.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color", "a_Normal"});

        // Define a simple shader program for our point.
        final String pointVertexShader =
                "uniform mat4 u_MVPMatrix;      \n"
                        +   "attribute vec4 a_Position;     \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_Position = u_MVPMatrix   \n"
                        + "               * a_Position;   \n"
                        + "   gl_PointSize = 5.0;         \n"
                        + "}                              \n";

        final String pointFragmentShader =
                "precision mediump float;       \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_FragColor = vec4(1.0,    \n"
                        + "   1.0, 1.0, 1.0);             \n"
                        + "}                              \n";

        final int pointVertexShaderHandle = object.compileShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        final int pointFragmentShaderHandle = object.compileShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
        object.setPointProgramHandle(object.createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                new String[] {"a_Position"}));

        Matrix.setIdentityM(mAccumulatedRotation, 0);
    }


    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mPerVertexProgramHandle);

        // Set program handles for object drawing.
        object.setMVPMatrixHandle(GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix"));
        object.setMVMatrixHandle(GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix"));
        object.setLightPosHandle(GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos"));
        object.setPositionHandle(GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position"));
        object.setColorHandle(GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color"));
        object.setNormalHandle(GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal"));

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(object.getLightModelMatrix(), 0);
        Matrix.translateM(object.getLightModelMatrix(), 0, 3.0f, 3.0f, -3.0f);
        //Matrix.rotateM(object.mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(object.getLightModelMatrix(), 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(object.getLightPosInWorldSpace(), 0, object.getLightModelMatrix(), 0, object.getLightPosInModelSpace(), 0);
        Matrix.multiplyMV(object.getLightPosInEyeSpace(), 0, object.getViewMatrix(), 0, object.getLightPosInWorldSpace(), 0);

        // Draw some objects.
        Matrix.setIdentityM(object.getModelMatrix(), 0);
        Matrix.translateM(object.getModelMatrix(), 0, 0.0f, 0.0f, -2.8f);
        Matrix.rotateM(object.getModelMatrix(), 0, 90.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(object.getModelMatrix(), 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(object.getModelMatrix(), 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(object.getModelMatrix(), 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        object.draw();

        // Draw a point to indicate the light.
        GLES20.glUseProgram(object.getPointProgramHandle());
        object.drawLight();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(object.getProjectionMatrix(), 0, left, right, bottom, top, near, far);
    }
}