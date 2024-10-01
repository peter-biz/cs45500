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
   Create a wireframe model of a sector of a ring (an annulus)
   in the xy-plane centered at the origin.
<p>
   See <a href="https://en.wikipedia.org/wiki/Annulus_(mathematics)" target="_top">
                https://en.wikipedia.org/wiki/Annulus_(mathematics)</a>
<p>
   See <a href="https://en.wikipedia.org/wiki/Circular_sector" target="_top">
                https://en.wikipedia.org/wiki/Circular_sector</a>

   @see Ring
*/
public class RingSector extends Model implements MeshMaker
{
   public final double r1;
   public final double r2;
   public final double theta1;
   public final double theta2;
   public final int n;
   public final int k;

   /**
      Create half a ring (annulus) in the xy-plane
      with outer radius 1, inner radius 0.33, with 7
      spokes coming out of the center, and with 5
      concentric circles.
   */
   public RingSector( )
   {
      this(1.0, 0.33, 0, Math.PI, 5, 7);
   }


   /**
      Create a sector of a ring (annulus) in the xy-plane
      with outer radius {@code r1}, inner radius {@code r2},
      starting angle {@code theta1}, ending angle {@code theta2},
      with {@code k} spokes coming out of the center, and
      with {@code n} concentric circles.
   <p>
      If there are {@code k} spokes, then each (partial) circle
      around the center will have {@code k-1} line segments.
      If there are {@code n} concentric circles around the center,
      then each spoke will have {@code n-1} line segments.
   <p>
      There must be at least four spokes and at least two concentric circle.

      @param r1      outer radius of the ring
      @param r2      inner radius of the ring
      @param theta1  beginning angle of the sector (in radians)
      @param theta2  ending angle of the sector (in radians)
      @param n       number of concentric circles
      @param k       number of spokes in the ring
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 4
   */
   public RingSector(final double r1, final double r2,
                     double theta1, double theta2,
                     final int n, final int k)
   {
      super(String.format("Ring Sector(%.2f,%.2f,%.2f,%.2f,%d,%d)",
                                       r1, r2, theta1, theta2, n, k));

      if (n < 2)
         throw new IllegalArgumentException("n must be greater than 1");
      if (k < 4)
         throw new IllegalArgumentException("k must be greater than 3");

      theta1 = theta1 % (2*Math.PI);
      theta2 = theta2 % (2*Math.PI);
      if (theta1 < 0) theta1 = 2*Math.PI + theta1;
      if (theta2 < 0) theta2 = 2*Math.PI + theta2;
      if (theta2 <= theta1) theta2 = theta2 + 2*Math.PI;

      this.r1 = r1;
      this.r2 = r2;
      this.theta1 = theta1;
      this.theta2 = theta2;
      this.n = n;
      this.k = k;

      // Create the rings's geometry.

      final double deltaR = (r1 - r2) / (n - 1),
                   deltaTheta = (theta2 - theta1) / (k - 1);

      // An array of vertices to be used to create line segments.
      final Vertex[][] v = new Vertex[n][k];

      // Create all the vertices.
      for (int j = 0; j < k; ++j) // choose a spoke (an angle)
      {
         final double c = Math.cos(theta1 + j * deltaTheta),
                      s = Math.sin(theta1 + j * deltaTheta);
         for (int i = 0; i < n; ++i) // move along the spoke
         {
            final double ri = r2 + i * deltaR;
            v[i][j] = new Vertex(ri * c,
                                 ri * s,
                                 0);
         }
      }

      // Add all of the vertices to this model.
      for (int i = 0; i < n; ++i)
      {
         for (int j = 0; j < k; ++j)
         {
            addVertex( v[i][j] );
         }
      }

      // Create line segments around each concentric ring.
      for (int i = 0; i < n; ++i)  // choose a ring
      {
         for (int j = 0; j < k - 1; ++j)
         {  //                               v[i][j]       v[i][j+1]
            addPrimitive(new LineSegment( (i * k) + j, (i * k) + (j+1) ));
         }
      }

      // Create the spokes.connecting the inner circle to the outer circle.
      for (int j = 0; j < k; ++j) // choose a spoke
      {
         for (int i = 0; i < n - 1; ++i)
         {  //                                v[i][j]      v[i+1][j]
            addPrimitive(new LineSegment( (i * k) + j, ((i+1) * k) + j ));
         }
      }
   }



   // Implement the MeshMaker interface (thre methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public RingSector remake(final int n, final int k)
   {
      return new RingSector(this.r1, this.r2,
                            this.theta1, this.theta2,
                            n, k);
   }
}//RingSector
