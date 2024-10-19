/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a flat wireframe checkerboard panel in the xz-plane.
*/
public class PanelXZ extends Model
{
   /**
      Create a flat checkerboard panel in the xz-plane that runs
      from -1 to 1 in the x-direction and -1 to 1 in the z-direction.
   */
   public PanelXZ( )
   {
      this(-1, 1, -1, 1);
   }


   /**
      Create a flat checkerboard panel in the xz-plane with the given dimensions.

      @param xMin  location of left edge
      @param xMax  location of right edge
      @param zMin  location of back edge
      @param zMax  location of front edge
   */
   public PanelXZ(final int xMin, final int xMax,
                  final int zMin, final int zMax)
   {
      this(xMin, xMax, zMin, zMax, 0.0);
   }


   /**
      Create a flat checkerboard panel parallel to the xz-plane with the given dimensions.

      @param xMin  location of left edge
      @param xMax  location of right edge
      @param zMin  location of back edge
      @param zMax  location of front edge
      @param y     y-plane that holds the panel
   */
   public PanelXZ(final int xMin, final int xMax,
                  final int zMin, final int zMax,
                  final double y)
   {
      super("PanelXZ");

      // Create the checkerboard panel's geometry.

      // An array of indexes to be used to create line segments.
      final int[][] index = new int[(xMax-xMin)+1][(zMax-zMin)+1];

      // Create the checkerboard of vertices.
      int i = 0;
      for (int x = xMin; x <= xMax; ++x)
      {
         for (int z = zMin; z <= zMax; ++z)
         {
            addVertex(new Vertex(x, y, z));
            index[x-xMin][z-zMin] = i;
            ++i;
         }
      }

      // Create the line segments that run in the z-direction.
      for (int x = 0; x <= xMax - xMin; ++x)
      {
         for (int z = 0; z < zMax - zMin; ++z)
         {
            addPrimitive(new LineSegment(index[x][z], index[x][z+1]));
         }
      }

      // Create the line segments that run in the x-direction.
      for (int z = 0; z <= zMax - zMin; ++z)
      {
         for (int x = 0; x < xMax - xMin; ++x)
         {
            addPrimitive(new LineSegment(index[x][z], index[x+1][z]));
         }
      }
   }
}//PanelXZ
