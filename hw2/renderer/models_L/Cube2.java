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
   This version of the cube model has each face of
   the cube cut up by an n by m grid of lines.
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
   @see Cube3
   @see Cube4
*/
public class Cube2 extends Model
{
   /**
      Create a cube with its center at the origin, having edge
      length 2, with its corners at {@code (±1, ±1, ±1)}. and
      with two perpendicular grid lines going across the middle
      of each of the cube's faces.
   */
   public Cube2( )
   {
      this(1, 1, 1);
   }


   /**
      Create a cube with its center at the origin, having edge
      length 2, with its corners at {@code (±1, ±1, ±1)}, and
      with each of the cube's faces containing the given number
      of grid lines parallel to the x, y, and z directions.

      @param xGrid  number of grid lines perpendicular to the x-axis
      @param yGrid  number of grid lines perpendicular to the y-axis
      @param zGrid  number of grid lines perpendicular to the z-axis
      @throws IllegalArgumentException if {@code xGrid} is less than 0
      @throws IllegalArgumentException if {@code yGrid} is less than 0
      @throws IllegalArgumentException if {@code zGrid} is less than 0
   */
   public Cube2(final int xGrid, final int yGrid, final int zGrid)
   {
      super(String.format("Cube2(%d,%d,%d)", xGrid, yGrid, zGrid));

      if (xGrid < 0)
         throw new IllegalArgumentException("xGrid must be greater than or equal to 0");
      if (yGrid < 0)
         throw new IllegalArgumentException("yGrid must be greater than or equal to 0");
      if (zGrid < 0)
         throw new IllegalArgumentException("zGrid must be greater than or equal to 0");

      final double xStep = 2.0 / (1 + xGrid),
                   yStep = 2.0 / (1 + yGrid),
                   zStep = 2.0 / (1 + zGrid);

      // Grid lines perpendicular to the x-axis.
      double x = -1.0;
      for (int i = 0; i <= xGrid + 1; ++i)
      {
         final int start = vertexList.size();

         // Start at the top, front edge, go down the front face, and around the cube.
         double y = 1.0;
         for (int j = 0; j <= yGrid; ++j)
         {
            addVertex( new Vertex(x, y, 1.0) );
            y -= yStep;
         }
         double z = 1.0;
         for (int j = 0; j <= zGrid; ++j)
         {
            addVertex( new Vertex(x, -1.0, z) );
            z -= zStep;
         }
         y = -1.0;
         for (int j = 0; j <= yGrid; ++j)
         {
            addVertex( new Vertex(x, y, -1.0) );
            y += yStep;
         }
         z = -1.0;
         for (int j = 0; j <= zGrid + 1; ++j)
         {
            addVertex( new Vertex(x, 1.0, z) );
            z += zStep;
         }
         final int stop = vertexList.size();
         // Note: stop - start =  2*yGrid + 2*zGrid + 5

         for (int j = start; j < stop - 1; ++j)
         {
            addPrimitive(new LineSegment(j, j+1));
         }

         x += xStep;
      }

      // Grid lines perpendicular to the y-axis.
      double y = -1.0;
      for (int i = 0; i <= yGrid + 1; ++i)
      {
         final int start = vertexList.size();

         // Start at the front, right edge, go left across the front face, and around the cube.
         double x2 = 1.0;
         for (int j = 0; j <= xGrid; ++j)
         {
            addVertex( new Vertex(x2, y, 1.0) );
            x2 -= xStep;
         }
         double z = 1.0;
         for (int j = 0; j <= zGrid; ++j)
         {
            addVertex( new Vertex(-1.0, y, z) );
            z -= zStep;
         }
         x2 = -1.0;
         for (int j = 0; j <= xGrid; ++j)
         {
            addVertex( new Vertex(x2, y, -1.0) );
            x2 += xStep;
         }
         z = -1.0;
         for (int j = 0; j <= zGrid + 1; ++j)
         {
            addVertex( new Vertex(1.0, y, z) );
            z += zStep;
         }
         final int stop = vertexList.size();
         // Note: stop - start =  2*xGrid + 2*zGrid + 5

         for (int j = start; j < stop - 1; ++j)
         {
            addPrimitive(new LineSegment(j, j+1));
         }

         y += yStep;
      }

      // Grid lines perpendicular to the z-axis.
      double z = -1.0;
      for (int i = 0; i <= zGrid + 1; ++i)
      {
         final int start = vertexList.size();

         // Start at the top, right edge, go left across the top face, and around the cube.
         double x2 = 1.0;
         for (int j = 0; j <= xGrid; ++j)
         {
            addVertex( new Vertex(x2, 1.0, z) );
            x2 -= xStep;
         }
         double y2 = 1.0;
         for (int j = 0; j <= yGrid; ++j)
         {
            addVertex( new Vertex(-1.0, y2, z) );
            y2 -= yStep;
         }
         x2 = -1.0;
         for (int j = 0; j <= xGrid; ++j)
         {
            addVertex( new Vertex(x2, -1.0, z) );
            x2 += xStep;
         }
         y2 = -1.0;
         for (int j = 0; j <= yGrid + 1; ++j)
         {
            addVertex( new Vertex(1.0, y2, z) );
            y2 += yStep;
         }
         final int stop = vertexList.size();
         // Note: stop - start =  2*xGrid + 2*yGrid + 5

         for (int j = start; j < stop - 1; ++j)
         {
            addPrimitive(new LineSegment(j, j+1));
         }

         z += zStep;
      }
   }
}//Cube2
