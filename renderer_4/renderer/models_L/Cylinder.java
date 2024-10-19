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
   Create a wireframe model of a right circular cylinder
   with its axis along the y-axis.
<p>
   See <a href="https://en.wikipedia.org/wiki/Cylinder" target="_top">
                https://en.wikipedia.org/wiki/Cylinder</a>
<p>
   This model can also be used to create right k-sided polygonal prisms.
<p>
   See <a href="https://en.wikipedia.org/wiki/Prism_(geometry)" target="_top">
                https://en.wikipedia.org/wiki/Prism_(geometry)</a>

   @see CylinderSector
*/
public class Cylinder extends Model implements MeshMaker
{
   public final double r;
   public final double h;
   public final int n;
   public final int k;

   /**
      Create a right circular cylinder with radius 1 and its
      axis along the y-axis from {@code y = 1} to {@code y = -1}.
   */
   public Cylinder( )
   {
      this(1, 1, 15, 16);
   }


   /**
      Create a right circular cylinder with radius {@code r} and
      its axis along the y-axis from {@code y = h} to {@code y = -h}.

      @param r  radius of the cylinder
      @param h  height of the cylinder (from h to -h along the y-axis)
   */
   public Cylinder(final double r, final double h)
   {
      this(r, h, 15, 16);
   }


   /**
      Create a right circular cylinder with radius {@code r} and
      its axis along the y-axis from {@code y = h} to {@code y = -h}.
   <p>
      The last two parameters determine the number of lines of longitude
      and the number of circles of latitude in the model.
   <p>
      If there are {@code n} circles of latitude in the model (including
      the top and bottom edges), then each line of longitude will have
      {@code n+1} line segments. If there are {@code k} lines of longitude,
      then each circle of latitude will have {@code k} line segments.
   <p>
      There must be at least three lines of longitude and at least
      two circles of latitude.
   <p>
      By setting {@code k} to be a small integer, this model can also be
      used to create k-sided polygonal prisms.

      @param r  radius of the cylinder
      @param h  height of the cylinder (from h to -h along the y-axis)
      @param n  number of circles of latitude around the cylinder
      @param k  number of lines of longitude
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 4
   */
   public Cylinder(final double r, final double h, final int n, final int k)
   {
      super(String.format("Cylinder(%.2f,%.2f,%d,%d)", r, h, n, k));

      if (n < 2)
         throw new IllegalArgumentException("n must be greater than 1");
      if (k < 4)
         throw new IllegalArgumentException("k must be greater than 3");

      this.r = r;
      this.h = h;
      this.n = n;
      this.k = k;

      // Create the cylinder's geometry.

      final double deltaH = (2.0 * h) / (n - 1),
                   deltaTheta = (2.0 * Math.PI) / (k - 1);

      // An array of vertices to be used to create line segments.
      final Vertex[][] v = new Vertex[n][k];

      // Create all the vertices (from the top down).
      for (int j = 0; j < k; ++j) // choose an angle of longitude
      {
         final double c = Math.cos(j * deltaTheta),
                      s = Math.sin(j * deltaTheta);
         for (int i = 0; i < n; ++i) // choose a circle of latitude
         {
            v[i][j] = new Vertex( r * c,
                                  h - i * deltaH,
                                 -r * s );
         }
      }
      final Vertex topCenter    = new Vertex(0,  h, 0),
                   bottomCenter = new Vertex(0, -h, 0);

      // Add all of the vertices to this model.
      for (int i = 0; i < n; ++i)
      {
         for (int j = 0; j < k; ++j)
         {
            addVertex( v[i][j] );
         }
      }
      addVertex(topCenter,
                bottomCenter);
      final int topCenterIndex    = n * k,
                bottomCenterIndex = n * k + 1;

      // Create all the horizontal circles of latitude around the cylinder.
      for (int i = 0; i < n; ++i) // choose a circle of latitude
      {
         for (int j = 0; j < k - 1; ++j)
         {  //                                v[i][j]      v[i][j+1]
            addPrimitive(new LineSegment( (i * k) + j, (i * k) + (j+1) ));
         }
      }

      // Create the lines of longitude from the top to the bottom.
      for (int j = 0; j < k; ++j) // choose a line of longitude
      {  //                                              v[0][j]
         addPrimitive(new LineSegment( topCenterIndex, (0 * k) + j ));
         for (int i = 0; i < n - 1; ++i)
         {  //                                v[i][j]       v[i+1][j]
            addPrimitive(new LineSegment( (i * k) + j, ((i+1) * k) + j ));
         }
         addPrimitive(new LineSegment( ((n-1) * k) + j, bottomCenterIndex ));
         //                                v[n-1][j]
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public Cylinder remake(final int n, final int k)
   {
      return new Cylinder(this.r, this.h, n, k);
   }
}//Cylinder
