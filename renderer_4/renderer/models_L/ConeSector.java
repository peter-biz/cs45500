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
   Create a wireframe model of a partial right circular cone with its
   base parallel to the xz-plane and its apex on the positive y-axis.
<p>
   By a partial cone we mean a cone over a circular sector of the
   cone's base and also cutting off the top part of the cone (the
   part between the apex and a circle of latitude) leaving a frustum
   of the (partial) cone.

   @see Cone
   @see ConeFrustum
   @see CircleSector
   @see DiskSector
   @see RingSector
   @see CylinderSector
   @see SphereSector
   @see TorusSector
*/
public class ConeSector extends Model implements MeshMaker
{
   public final double r;
   public final double h;
   public final double t;
   public final double theta1;
   public final double theta2;
   public final int n;
   public final int k;

   /**
      Create half of a right circular cone with its base in the xz-plane,
      a base radius of 1, height 1, and apex on the positive y-axis.
   */
   public ConeSector( )
   {
      this(1, 1, Math.PI/2, 3*Math.PI/2, 15, 8);
   }


   /**
      Create a part of the cone with its base in the xz-plane,
      a base radius of {@code r}, height {@code h}, and apex
      on the y-axis.
   <p>
      The partial cone is a cone over the circular sector
      from angle {@code theta1} to angle {@code theta2} (in the
      counterclockwise direction). In other words, the (partial)
      circles of latitude in the model extend from angle
      {@code theta1} to angle {@code theta2} (in the
      counterclockwise direction).
   <p>
      The last two parameters determine the number of lines of longitude
      and the number of (partial) circles of latitude in the model.
   <p>
      If there are {@code n} circles of latitude in the model (including
      the bottom edge), then each line of longitude will have {@code n}
      line segments. If there are {@code k} lines of longitude, then each
      (partial) circle of latitude will have {@code k-1} line segments.
   <p>
      There must be at least four lines of longitude and at least
      one circle of latitude.

      @param r       radius of the base in the xz-plane
      @param h       height of the apex on the y-axis
      @param theta1  beginning longitude angle of the sector (in radians)
      @param theta2  ending longitude angle of the sector (in radians)
      @param n       number of circles of latitude around the cone
      @param k       number of lines of longitude
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 4
   */
   public ConeSector(final double r,
                     final double h,
                     final double theta1, final double theta2,
                     final int n, final int k)
   {
      this(r, h, h, theta1, theta2, n+1, k);
   }


   /**
      Create a part of the cone with its base in the xz-plane,
      a base radius of {@code r}, height {@code  h}, and apex
      on the y-axis.
   <p>
      If {@code 0 < t < h}, then the partial cone is a frustum
      with its base in the xz-plane and the top of the frustum at
      {@code y = t}.
   <p>
      The partial cone is a cone over the circular sector
      from angle {@code theta1} to angle {@code theta2} (in the
      counterclockwise direction). In other words, the (partial)
      circles of latitude in the model extend from angle
      {@code theta1} to angle {@code theta2} (in the
      counterclockwise direction).
   <p>
      The last two parameters determine the number of lines of longitude
      (not counting one edge of any removed sector) and the number of
      (partial) circles of latitude (not counting the top edge of the
      frustum) in the model.
   <p>
      If there are {@code n} circles of latitude in the model (including
      the bottom edge but not the top edge of the frustum), then each
      line of longitude will have {@code n+1} line segments. If there are
      {@code k} lines of longitude (not counting one edge of any removed
      sector), then each (partial) circle of latitude will have {@code k}
      line segments.
   <p>
      There must be at least four lines of longitude and at least
      two circles of latitude.

      @param r       radius of the base in the xz-plane
      @param h       height of the apex on the y-axis
      @param t       top of the frustum of the come
      @param theta1  beginning longitude angle of the sector (in radians)
      @param theta2  ending longitude angle of the sector (in radians)
      @param n       number of circles of latitude around the cone
      @param k       number of lines of longitude
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 4
      @throws IllegalArgumentException if {@code h} is less than {@code t}
   */
   public ConeSector(final double r,
                     final double h,
                     final double t,
                     double theta1, double theta2,
                     final int n, final int k)
   {
      super(String.format("Cone Sector(%.2f,%.2f,%.2f,%.2f,%.2f,%d,%d)",
                                       r, h, t, theta1, theta2, n, k));

      if (n < 2)
         throw new IllegalArgumentException("n must be greater than 1");
      if (k < 4)
         throw new IllegalArgumentException("k must be greater than 3");
      if (h < t)
         throw new IllegalArgumentException("h must be greater than or equal to t");

      theta1 = theta1 % (2*Math.PI);
      theta2 = theta2 % (2*Math.PI);
      if (theta1 < 0) theta1 = 2*Math.PI + theta1;
      if (theta2 < 0) theta2 = 2*Math.PI + theta2;
      if (theta2 <= theta1) theta2 = theta2 + 2*Math.PI;

      this.r = r;
      this.h = h;
      this.t = t;
      this.theta1 = theta1;
      this.theta2 = theta2;
      this.n = n;
      this.k = k;

      // Create the cone's geometry.

      final double deltaH = t / (n - 1),
                   deltaTheta = (theta2 - theta1) / (k - 1);

      // An array of indexes to be used to create line segments.
      final int[][] indexes = new int[n][k];

      // Create all the vertices (from the bottom up).
      int index = 0;
      for (int j = 0; j < k; ++j) // choose an angle of longitude
      {
         final double c = Math.cos(theta1 + j * deltaTheta),
                      s = Math.sin(theta1 + j * deltaTheta);
         for (int i = 0; i < n; ++i) // choose a circle of latitude
         {
            final double slantRadius = r * (1 - i * deltaH / h);
            addVertex( new Vertex(slantRadius * c,
                                  i * deltaH,
                                  slantRadius * s) );
            indexes[i][j] = index++;
         }
      }
      addVertex( new Vertex(0, 0, 0) ); // bottom center
      final int bottomCenterIndex = index;
      ++index;

      // Create all the horizontal (partial) circles of latitude around the cone.
      for (int i = 0; i < n; ++i)
      {
         for (int j = 0; j < k - 1; ++j)
         {
            addPrimitive(new LineSegment(indexes[i][j], indexes[i][j+1]));
         }
      }

      // Create the slanted lines of longitude from the base to the
      // top circle of latitude, and the triangle fan in the base.
      for (int j = 0; j < k; ++j)
      {
         addPrimitive(new LineSegment(bottomCenterIndex, indexes[0][j]));

         for (int i = 0; i < n - 1; ++i)
         {
            addPrimitive(new LineSegment(indexes[i][j], indexes[i+1][j]));
         }
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public ConeSector remake(final int n, final int k)
   {
      return new ConeSector(this.r,
                            this.h,
                            this.t,
                            this.theta1, this.theta2,
                            n, k);
   }
}//ConeSector
