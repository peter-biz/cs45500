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
   Create a wireframe model of a ring (an annulus)
   in the xy-plane centered at the origin.
<p>
   See <a href="https://en.wikipedia.org/wiki/Annulus_(mathematics)" target="_top">
                https://en.wikipedia.org/wiki/Annulus_(mathematics)</a>

   @see RingSector
*/
public class Ring extends Model implements MeshMaker
{
   public final double r1;
   public final double r2;
   public final int n;
   public final int k;

   /**
      Create a ring (annulus) in the xy-plane with outer
      radius 1 and with inner radius 0.33, with 12 spokes
      coming out of the center, and with 5 concentric circles.
   */
   public Ring( )
   {
      this(1.0, 0.33, 4, 12);
   }


   /**
      Create a ring (annulus) in the xy-plane with outer
      radius {@code r1} and with inner radius {@code r2},
      with 12 spokes coming out of the center, and with
      5 concentric circles.

      @param r1  outer radius of the ring
      @param r2  inner radius of the ring
   */
   public Ring(final double r1, final double r2)
   {
      this(r1, r2, 4, 12);
   }


   /**
      Create a ring (annulus) in the xy-plane with outer
      radius {@code r1} and with inner radius {@code r2},
      with {@code k} spokes coming out of the center, and
      with {@code n} concentric circles (not counting the
      inner most circle).
   <p>
      If there are {@code k} spokes, then each circle around
      the center will have {@code k} line segments. If there
      are {@code n} concentric circles around the center (not
      counting the inner most circle), then each spoke will
      have {@code n} line segments.
   <p>
      There must be at least three spokes and at least one concentric circle.

      @param r1  outer radius of the ring
      @param r2  inner radius of the ring
      @param n   number of concentric circles
      @param k   number of spokes in the ring
      @throws IllegalArgumentException if {@code n} is less than 1
      @throws IllegalArgumentException if {@code k} is less than 3
   */
   public Ring(final double r1, final double r2,
               final int n, final int k)
   {
      super(String.format("Ring(%.2f,%.2f,%d,%d)", r1, r2, n, k));

      if (n < 1)
         throw new IllegalArgumentException("n must be greater than 0");
      if (k < 3)
         throw new IllegalArgumentException("k must be greater than 2");

      this.r1 = r1;
      this.r2 = r2;
      this.n = n;
      this.k = k;

      // Create the rings's geometry.

      final double deltaR = (r1 - r2) / n,
                   deltaTheta = (2 * Math.PI) / k;

      // An array of vertices to be used to create line segments.
      final Vertex[][] v = new Vertex[n+1][k];

      // Create all the vertices.
      for (int j = 0; j < k; ++j) // choose a spoke (an angle)
      {
         final double c = Math.cos(j * deltaTheta),
                      s = Math.sin(j * deltaTheta);
         for (int i = 0; i < n + 1; ++i) // move along the spoke
         {
            final double ri = r2 + i * deltaR;
            v[i][j] = new Vertex(ri * c,
                                 ri * s,
                                 0);
         }
      }

      // Add all of the vertices to this model.
      for (int i = 0; i < n + 1; ++i)
      {
         for (int j = 0; j < k; ++j)
         {
            addVertex( v[i][j] );
         }
      }

      // Create line segments around each concentric ring.
      for (int i = 0; i < n + 1; ++i)  // choose a ring
      {
         for (int j = 0; j < k - 1; ++j)
         {  //                                v[i][[j]     v[i][j+1]
            addPrimitive(new LineSegment( (i * k) + j, (i * k) + (j+1) ));
         }
         // close the circle
         addPrimitive(new LineSegment( (i * k) + (k-1), (i * k) + 0 ));
      }  //                                v[i][k-1]         v[i][0]

      // Create the spokes.connecting the inner circle to the outer circle.
      for (int j = 0; j < k; ++j) // choose a spoke
      {
         for (int i = 0; i < n; ++i)
         {  //                                v[i][j]       v[i+1][j]
            addPrimitive(new LineSegment( (i * k) + j, ((i+1) * k) + j ));
         }
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public Ring remake(final int n, final int k)
   {
      return new Ring(this.r1, this.r2,
                      n, k);
   }
}//Ring
