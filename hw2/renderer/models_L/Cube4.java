/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a cube with its center
   at the origin, having edge length 2, and with its
   corners at {@code (±1, ±1, ±1)}.
<p>
   This version of the cube model has the top and bottom
   faces of the cube cut up by a triangle fan and the
   front, back, right, and left faces cut up by a grid
   of perpendicular lines.
<p>
   Here is a picture showing how the cube's eight
   corners are labeled.
<pre>{@code
                  v4=(-1,1,-1)
                  +---------------------+ v5=(1,1,-1)
                 /|                    /|
                / |                   / |
               /  |                  /  |
              /   |                 /   |
             /    |                /    |
         v7 +---------------------+ v6  |
            |     |               |     |
            |     |               |     |
            |     | v0=(-1,-1,-1) |     |
            |     +---------------|-----+ v1=(1,-1,-1)
            |    /                |    /
            |   /                 |   /
            |  /                  |  /
            | /                   | /
            |/                    |/
            +---------------------+
            v3=(-1,-1,1)          v2=(1,-1,1)
}</pre>

   @see Cube
   @see Cube2
   @see Cube3
*/
public class Cube4 extends Model
{
   /**
      Create a cube with its center at the origin, having edge
      length 2, with its corners at {@code (±1, ±1, ±1)}. with
      a triangle fan of eight triangles in top and bottom faces,
      and two perpendicular lines cutting each of the front,
      back, right, and left faces.
   */
   public Cube4( )
   {
      this(2, 1, 2);
   }


   /**
      Create a cube with its center at the origin, having edge
      length 2, with its corners at {@code (±1, ±1, ±1)}, and
      with the top and bottom faces containing a triangle fan
      with the given number of triangles along each of the x,
      and z directions.
      <p>
      There must be at least one triangle along the x and z directions.

      @param xCount  number of triangles along the x-direction
      @param yGrid   number of grid lines perpendicular to the y-axis
      @param zCount  number of triangles along the z-direction
      @throws IllegalArgumentException if {@code xCount} is less than 1
      @throws IllegalArgumentException if {@code yGrid} is less than 0
      @throws IllegalArgumentException if {@code zCount} is less than 1
   */
   public Cube4(final int xCount, final int yGrid, final int zCount)
   {
      super(String.format("Cube4(%d,%d,%d)", xCount, yGrid, zCount));

      if (xCount < 1)
         throw new IllegalArgumentException("xCount must be greater than or equal to 1");
      if (yGrid < 0)
         throw new IllegalArgumentException("yGrid must be greater than or equal to 0");
      if (zCount < 1)
         throw new IllegalArgumentException("zCount must be greater than or equal to 1");

      final double xStep = 2.0 / xCount,
                   yStep = 2.0 / (1 + yGrid),
                   zStep = 2.0 / zCount;

      // An array of vertices to be used to create primitives.
      final Vertex[][] v = new Vertex[2 + yGrid][2*xCount + 2*zCount];

      // Create all the vertices.
      for (int i = 0; i < 2 + yGrid; ++i) // choose a height of latitude
      {
         for(int j = 0; j < xCount; ++j)
         {
            v[i][j] = new Vertex(-1 + j*xStep, // move right
                                 -1 + i*yStep,
                                 -1);
         }
         for(int j = 0; j < zCount; ++j)
         {
            v[i][xCount + j] = new Vertex( 1,
                                          -1 + i*yStep,
                                          -1 + j*zStep); // move forward
         }
         for(int j = 0; j < xCount; ++j)
         {
            v[i][xCount+zCount + j] = new Vertex( 1 - j*xStep, // move left
                                                 -1 + i*yStep,
                                                  1);
         }
         for(int j = 0; j < zCount; ++j)
         {
            v[i][2*xCount+zCount + j] = new Vertex(-1,
                                                   -1 + i*yStep,
                                                    1 - j*zStep); // backwards
         }
      }

      // Add all of the vertices to this model.
      for (int i = 0; i < 2 + yGrid; ++i)
      {
         for (int j = 0; j < 2*xCount + 2*zCount; ++j)
         {
            addVertex( v[i][j] );
         }
      }
      addVertex(new Vertex(0, -1,  0),  // center bottom
                new Vertex(0,  1,  0)); // center top
      final int cBottom = (2 + yGrid) * (2*xCount + 2*zCount);
      final int cTop = cBottom + 1;

      // Create the line fans in the cube's bottom and top sides.
      for (int j = 0; j < 2*xCount + 2*zCount; ++j)
      {
         addPrimitive(
            new LineSegment(cBottom,
                            j),                                // v[0][j]
            new LineSegment(cTop,
                            (yGrid+1)*(2*xCount+2*zCount)+j)); // v[2+yGrid-1][j]
      }

      // Create all the loops around the cube's vertical sides.
      for (int i = 0; i < 2 + yGrid; ++i) // choose a height of latitude
      {
         for (int j = 0; j < 2*xCount + 2*zCount - 1; ++j)
         {
            addPrimitive(
               new LineSegment(i*(2*xCount+2*zCount)+j,     // v[i][j]
                               i*(2*xCount+2*zCount)+j+1)); // v[i][j+1]
         }
         addPrimitive(
            new LineSegment((1+i)*(2*xCount+2*zCount) - 1, // v[i][2*xCount + 2*zCount-1]
                               i *(2*xCount+2*zCount)));   // v[i][0]
      }

      // Create all the vertical lines in the cube's vertical sides.
      for (int j = 0; j < 2*xCount + 2*zCount; ++j)
      {
         for (int i = 0; i < 2 + yGrid - 1; ++i) // choose a height of latitude
         {
            addPrimitive(
               new LineSegment(   i *(2*xCount+2*zCount)+j,    // v[i  ][j]
                               (i+1)*(2*xCount+2*zCount)+j));  // v[i+1][j]
         }
      }


   }
}//Cube4
