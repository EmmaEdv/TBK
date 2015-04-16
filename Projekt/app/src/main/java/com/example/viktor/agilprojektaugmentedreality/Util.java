/**
 * Copyright 2013 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.viktor.agilprojektaugmentedreality;

public interface Util {
    public static final boolean DEBUG = true;
    public static final String LOG_TAG = "gles20tut";

    public final float[] cubePositionData = {
                    // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                    // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                    // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                    // usually represent the backside of an object and aren't visible anyways.

            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
    };

    public final float[] cubeColorData = {
            // Front face (red)
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            // Right face (green)
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            // Back face (blue)
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            // Left face (yellow)
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            // Top face (cyan)
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            // Bottom face (magenta)
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f
            };

    // X, Y, Z
    // The normal is used in light calculations and is a vector which points
    // orthogonal to the plane of the surface. For a cube model, the normals
    // should be orthogonal to the points of each face.
    public final float[] cubeNormalData =
            {
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 0.0f, 1f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 1f, 0.0f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, 0.0f, -1f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    0.0f, -1f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
                    -1f, 0.0f, 0.0f,
            };
}
