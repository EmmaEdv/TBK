package com.example.viktor.agilprojektaugmentedreality;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square {

    /** Used for debug logs. */
    private static final String TAG = "EPICARAPPRENDERER";

    /** Store our model data in a float buffer. */
    private FloatBuffer mCubePositions;
    private FloatBuffer mCubeColors;
    private FloatBuffer mCubeNormals;

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    /** Size of the normal data in elements. */
    private final int mNormalDataSize = 3;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mColorHandle;

    /** This will be used to pass in model normal information. */
    private int mNormalHandle;

    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;

    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];

    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     *  we multiply this by our transformation matrices. */
    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
    private final float[] mLightPosInWorldSpace = new float[4];

    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
    private final float[] mLightPosInEyeSpace = new float[4];

    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private float[] mLightModelMatrix = new float[16];


    /** This will be used to pass in the light position. */
    private int mLightPosHandle;

    /** This is a handle to our light point program. */
    private int mPointProgramHandle;


	public Square() {

        //setEGLContextClientVersion(2);

        mCubePositions = ByteBuffer.allocateDirect(MainActivity.cubeVerts.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(MainActivity.cubeVerts).position(0);

        mCubeColors = ByteBuffer.allocateDirect(MainActivity.cubeColorCoords.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeColors.put(MainActivity.cubeColorCoords).position(0);

        mCubeNormals = ByteBuffer.allocateDirect(MainActivity.cubeNormals.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeNormals.put(MainActivity.cubeNormals).position(0);


        /*
		// a float is 4 bytes, therefore we multiply the number if 
		// vertices with 4.
        // Allocate memory
		ByteBuffer vbb = ByteBuffer.allocateDirect(cubeVerts.length * 4);
		vbb.order(ByteOrder.nativeOrder());
        // Set up the vertex buffer
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(cubeVerts);
		vertexBuffer.position(0);


        vbb = ByteBuffer.allocateDirect(colors.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        colorBuffer = vbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if 
		// vertices with 2.
        // Allocate memory
		ByteBuffer ibb = ByteBuffer.allocateDirect(cubeIndices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
        // Set up the Index Buffer
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(cubeIndices);
		indexBuffer.position(0);
		*/
	}
	
	/**
	 * This function draws our square on screen.
	 *
	 */
	public void draw() {


        // Pass in the position information
        mCubePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, mCubePositions);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mCubeColors.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, mCubeColors);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        mCubeNormals.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false,
                0, mCubeNormals);

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, MainActivity.numCubeVerts/3);

        /*
        // Set color of object
        //gl.glColor4f(0.23f, 0.56f, 0.82f, 1.0f);

		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);

		// Enabled the vertices buffer for writing and to be used during 
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, cubeIndices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
		*/
	}

    public void drawLight()
    {
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    /**
     * Helper function to compile a shader.
     *
     * @param shaderType The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    public int compileShader(final int shaderType, final String shaderSource)
    {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    public int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }

    /**************************************************
     Setters for some variables needed in our renderer
    **************************************************/

    void setPositionHandle(int p) { this.mPositionHandle = p; }

    void setColorHandle(int c) {
        this.mColorHandle = c;
    }

    void setNormalHandle(int n) {
        this.mNormalHandle = n;
    }

    void setMVPMatrixHandle(int mvp) {
        this.mMVPMatrixHandle = mvp;
    }

    void setMVMatrixHandle(int mv) {
        this.mMVMatrixHandle = mv;
    }

    void setLightPosHandle(int lp) {
        this.mLightPosHandle = lp;
    }

    void setPointProgramHandle(int pp) {
        this.mPointProgramHandle = pp;
    }



    /**************************************************
     Getters for some variables needed in our renderer
     **************************************************/

    float[] getModelMatrix() {
        return mModelMatrix;
    }

    float[] getViewMatrix() {
        return mViewMatrix;
    }

    float[] getProjectionMatrix() {
        return mProjectionMatrix;
    }

    float[] getLightPosInModelSpace() {
        return mLightPosInModelSpace;
    }

    float[] getLightPosInWorldSpace() {
        return mLightPosInWorldSpace;
    }

    float[] getLightPosInEyeSpace() {
        return mLightPosInEyeSpace;
    }

    float[] getLightModelMatrix() {
        return mLightModelMatrix;
    }

    int getPointProgramHandle() {
        return mPointProgramHandle;
    }
}
