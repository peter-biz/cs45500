/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.MeshMaker;

/**
   Create a wireframe model of a right circular cone with its base
   parallel to the xz-plane and its apex on the positive y-axis.
<p>
   See <a href="https://en.wikipedia.org/wiki/Cone" target="_top">
                https://en.wikipedia.org/wiki/Cone</a>
<p>
   This model can also be used to create right k-sided polygonal pyramids.
<p>
   See <a href="https://en.wikipedia.org/wiki/Pyramid_(geometry)" target="_top">
                https://en.wikipedia.org/wiki/Pyramid_(geometry)</a>

   @see ConeFrustum
*/
public class Cone extends Model implements MeshMaker
{
   public final double r;
   public final double h;
   public final int n;
   public final int k;

   /**
      Create a right circular cone with its base in the xz-plane,
      a base radius of 1, height 1, and apex on the positive y-axis.
   */
   public Cone( )
   {
      this(1, 1, 15, 16);
   }


   /**
      Create a right circular cone with its base in the xz-plane,
      a base radius of {@code r}, height {@code h}, and apex on
      the y-axis.

      @param r  radius of the base in the xz-plane
      @param h  height of the apex on the y-axis
   */
   public Cone(final double r, final double h)
   {
      this(r, h, 15, 16);
   }


   /**
      Create a right circular cone with its base in the xz-plane,
      a base radius of {@code r}, height {@code h}, and apex on
      the y-axis.
   <p>
      The last two parameters determine the number of lines of longitude
      and the number of circles of latitude in the model.
   <p>
      If there are {@code n} circles of latitude in the model (including
      the bottom edge), then each line of longitude will have {@code n+1}
      line segments. If there are {@code k} lines of longitude, then each
      circle of latitude will have {@code k} line segments.
   <p>
      There must be at least three lines of longitude and at least
      one circle of latitude.
   <p>
      By setting {@code k} to be a small integer, this model can also
      be used to create k-sided polygonal pyramids.

      @param r  radius of the base in the xz-plane
      @param h  height of the apex on the y-axis
      @param n  number of circles of latitude around the cone
      @param k  number of lines of longitude
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 4
   */
   public Cone(final double r, final double h, final int n, final int k)
   {
      super(String.format("Cone(%.2f,%.2f,%d,%d)",
                                r,   h,   n, k));

      if (n < 2)
         throw new IllegalArgumentException("n must be greater than 1");
      if (k < 4)
         throw new IllegalArgumentException("k must be greater than 3");

      this.r = r;
      this.h = h;
      this.n = n;
      this.k = k;

      // Create the cone's geometry.

      final double deltaH = h / (n - 1),
                   deltaTheta = (2.0*Math.PI) / (k - 1);

      // An array of indexes to be used to create line segments.
      final int[][] indexes = new int[n][k];

      // Create all the vertices (from the bottom up).
      int index = 0;
      for (int j = 0; j < k; ++j) // choose an angle of longitude
      {
         final double c = Math.cos(j * deltaTheta),
                      s = Math.sin(j * deltaTheta);
         for (int i = 0; i < n; ++i) // choose a circle of latitude
         {
            final double slantRadius = r * (1 - i * deltaH / h);
            addVertex( new Vertex(slantRadius * c,
                                  i * deltaH,
                                  slantRadius * s) );
            indexes[i][j] = index++;
         }
      }
      addVertex( new Vertex(0, h, 0) ); // apex
      final int apexIndex = index;
      ++index;
      addVertex( new Vertex(0, 0, 0) ); // bottom center
      final int bottomCenterIndex = index;
      ++index;

      // Create all the horizontal circles of latitude around the cone.
      for (int i = 0; i < n; ++i)
      {
         for (int j = 0; j < k - 1; ++j)
         {
            addPrimitive(new LineSegment(indexes[i][j], indexes[i][j+1]));
         }
      }

      // Create the slanted lines of longitude from the base to the
      // apex, and the triangle fan in the base.
      for (int j = 0; j < k; ++j)
      {
         addPrimitive(new LineSegment(bottomCenterIndex, indexes[0][j]));

         for (int i = 0; i < n - 1; ++i)
         {
            addPrimitive(new LineSegment(indexes[i][j], indexes[i+1][j]));
         }

         addPrimitive(new LineSegment(indexes[n-1][j], apexIndex));
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public Cone remake(final int n, final int k)
   {
      return new Cone(this.r,
                      this.h,
                      n, k);
   }
}//Cone
