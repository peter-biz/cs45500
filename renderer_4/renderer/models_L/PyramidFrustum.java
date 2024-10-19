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
   Create a wireframe model of a frustum of a right square pyramid
   with its base in the xz-plane.
<p>
   See <a href="https://en.wikipedia.org/wiki/Frustum" target="_top">
                https://en.wikipedia.org/wiki/Frustum</a>

   @see Pyramid
*/
public class PyramidFrustum extends Model implements MeshMaker
{
   public final double s1;
   public final double s2;
   public final double h;
   public final int n;
   public final int k;

   /**
      Create a frustum of a right square pyramid with its base in the
      xz-plane, a base side length of 2, top side length of 1, and height 1/2.
   */
   public PyramidFrustum( )
   {
      this(2.0, 1.0, 0.5, 7, 4);
   }


   /**
      Create a frustum of a right square pyramid with its base in the
      xz-plane, a base side length of {@code s1}, top side length of
      {@code s2}, and height {@code h}.
   <p>
      This model works with either {@code s1 > s2} or {@code s1 < s2}.
      In other words, the frustum can have its "apex" either above or
      below the xz-plane.

      @param s1  side length of the base of the frustum
      @param s2  side length of the top of the frustum
      @param h   height of the frustum
   */
   public PyramidFrustum(final double s1, final double s2, final double h)
   {
      super(String.format("Pyramid Frustum(%.2f,%.2f,%.2f)", s1, s2, h));

      this.s1 = s1;
      this.s2 = s2;
      this.h = h;
      this.n = 1;
      this.k = 1;

      // Create the pyramid's geometry.
      addVertex(new Vertex(-s1/2, 0, -s1/2),  // base
                new Vertex(-s1/2, 0,  s1/2),
                new Vertex( s1/2, 0,  s1/2),
                new Vertex( s1/2, 0, -s1/2),
                new Vertex(-s2/2, h, -s2/2),  // top
                new Vertex(-s2/2, h,  s2/2),
                new Vertex( s2/2, h,  s2/2),
                new Vertex( s2/2, h, -s2/2));

      // Create 6 faces.
      addPrimitive(new LineSegment(0, 1), // base
                   new LineSegment(1, 2),
                   new LineSegment(2, 3),
                   new LineSegment(3, 0),
                   new LineSegment(0, 4), // 4 sides
                   new LineSegment(1, 5),
                   new LineSegment(2, 6),
                   new LineSegment(3, 7),
                   new LineSegment(4, 5), // top
                   new LineSegment(5, 6),
                   new LineSegment(6, 7),
                   new LineSegment(7, 4));
   }


   /**
      Create a frustum of a right square pyramid with its base in the
      xz-plane, a base side length of {@code s}, top of the frustum at
      height {@code h}, and with the pyramid's apex at on the y-axis at
      height {@code a}.

      @param n  number of lines of latitude
      @param k  number of lines of longitude
      @param s  side length of the base of the frustum
      @param h  height of the frustum
      @param a  height of the apex of the pyramid
      @throws IllegalArgumentException if {@code n} is less than 0
      @throws IllegalArgumentException if {@code k} is less than 1
   */
   public PyramidFrustum(final int n, final int k,
                         final double s, final double h, final double a)
   {
      this(s, (1 - h/a)*s, h, n, k);
   }


   /**
      Create a frustum of a right square pyramid with its base in the
      xz-plane, a base side length of {@code s1}, top side length of
      {@code s2}, and height {@code h}.
   <p>
      This model works with either {@code s1 > s2} or {@code s1 < s2}.
      In other words, the frustum can have its "apex" either above or
      below the xz-plane.

      @param s1  side length of the base of the frustum
      @param s2  side length of the top of the frustum
      @param h   height of the frustum
      @param n   number of lines of latitude
      @param k   number of lines of longitude
      @throws IllegalArgumentException if {@code n} is less than 0
      @throws IllegalArgumentException if {@code k} is less than 1
   */
   public PyramidFrustum(double s1, double s2, double h,
                         final int n, final int k)
   {
      super(String.format("Pyramid Frustum(%.2f,%.2f,%.2f,%d,%d)",
                                           s1,  s2,  h,   n, k));

      if (n < 0)
         throw new IllegalArgumentException("n must be greater than or equal to 0");
      if (k < 1)
         throw new IllegalArgumentException("k must be greater than 0");

      this.s1 = s1;
      this.s2 = s2;
      this.h = h;
      this.n = n;
      this.k = k;

      // Create the frustum's geometry.
      int index = 0;

      // Create all the lines of longitude from the top, down to the base,
      // across the base, then back up to the top, and across the top.
      s1 = s1/2;
      s2 = s2/2;
      final double delta1 = (2 * s1) / k,
                   delta2 = (2 * s2) / k;
      // lines of "longitude" perpendicular to the x-axis
      for (int j = 0; j <= k; ++j)
      {
         final double d1 = j * delta1,
                      d2 = j * delta2;
         addVertex(new Vertex(-s2+d2, h, -s2),
                   new Vertex(-s1+d1, 0, -s1),
                   new Vertex(-s1+d1, 0,  s1),
                   new Vertex(-s2+d2, h,  s2));
         addPrimitive(new LineSegment(index+0, index+1),
                      new LineSegment(index+1, index+2),
                      new LineSegment(index+2, index+3),
                      new LineSegment(index+3, index+0));
         index += 4;
      }
      // lines of "longitude" perpendicular to the z-axis
      for (int j = 0; j <= k; ++j)
      {
         final double d1 = j * delta1,
                      d2 = j * delta2;
         addVertex(new Vertex( s2, h, -s2+d2),
                   new Vertex( s1, 0, -s1+d1),
                   new Vertex(-s1, 0, -s1+d1),
                   new Vertex(-s2, h, -s2+d2));
         addPrimitive(new LineSegment(index+0, index+1),
                      new LineSegment(index+1, index+2),
                      new LineSegment(index+2, index+3),
                      new LineSegment(index+3, index+0));
         index += 4;
      }
      // Create all the lines of "latitude" around the pyramid, starting
      // from the base and working up to the top.
      final double deltaH = h / (n + 1),
                   deltaS = (s1 - s2) / (n + 1);
      double s = s1;
      for (int i = 0; i <= n; ++i)
      {
         h = i * deltaH;
         addVertex(new Vertex( s, h,  s),
                   new Vertex( s, h, -s),
                   new Vertex(-s, h, -s),
                   new Vertex(-s, h,  s));
         addPrimitive(new LineSegment(index+0, index+1),
                      new LineSegment(index+1, index+2),
                      new LineSegment(index+2, index+3),
                      new LineSegment(index+3, index+0));
         s -= deltaS;
         index += 4;
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public PyramidFrustum remake(final int n, final int k)
   {
      return new PyramidFrustum(this.s1, this.s2,
                                this.h,
                                n, k);
   }
}//PyramidFrustum
