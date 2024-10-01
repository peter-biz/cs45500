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
   Create a wireframe model of a sector of a disk
   in the xy-plane centered at the origin.
<p>
   See <a href="https://en.wikipedia.org/wiki/Circular_sector" target="_top">
                https://en.wikipedia.org/wiki/Circular_sector</a>

   @see Disk
   @see CircleSector
   @see RingSector
   @see ConeSector
   @see CylinderSector
   @see SphereSector
   @see TorusSector
*/
public class DiskSector extends Model implements MeshMaker
{
   public final double r;
   public final double theta1;
   public final double theta2;
   public final int n;
   public final int k;

   /**
      Create half a disk in the xy-plane with radius 1,
      with 8 spokes coming out of the center, and
      with 6 concentric circles around the disk.
   */
   public DiskSector( )
   {
      this(1, 0, Math.PI, 6, 8);
   }


   /**
      Create a sector of a disk in the xy-plane with radius {@code r},
      starting angle {@code theta1}, ending angle {@code theta2},
      with {@code k} spokes coming out of the center, and with
      {@code n} concentric circles around the disk.
   <p>
      If there are {@code k} spokes, then each (partial) circle
      around the center will have {@code k-1} line segments.
      If there are {@code n} concentric circles around the center,
      then each spoke will have {@code n} line segments.
   <p>
      There must be at least four spokes and at least one concentric circle.

      @param r       radius of the disk
      @param theta1  beginning angle of the sector (in radians)
      @param theta2  ending angle of the sector (in radians)
      @param n       number of concentric circles
      @param k       number of spokes in the disk
      @throws IllegalArgumentException if {@code n} is less than 1
      @throws IllegalArgumentException if {@code k} is less than 4
   */
   public DiskSector(final double r,
                     double theta1, double theta2,
                     final int n, final int k)
   {
      super(String.format("Disk Sector(%.2f,%.2f,%.2f,%d,%d)",
                                       r, theta1, theta2, n, k));

      if (n < 1)
         throw new IllegalArgumentException("n must be greater than 0");
      if (k < 4)
         throw new IllegalArgumentException("k must be greater than 3");

      theta1 = theta1 % (2*Math.PI);
      theta2 = theta2 % (2*Math.PI);
      if (theta1 < 0) theta1 = 2*Math.PI + theta1;
      if (theta2 < 0) theta2 = 2*Math.PI + theta2;
      if (theta2 <= theta1) theta2 = theta2 + 2*Math.PI;

      this.r = r;
      this.theta1 = theta1;
      this.theta2 = theta2;
      this.n = n;
      this.k = k;

      // Create the disk's geometry.

      final double deltaR = r / n,
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
            final double ri = (i + 1) * deltaR;
            v[i][j] = new Vertex( ri * c,
                                  ri * s,
                                  0 );
         }
      }
      final Vertex center = new Vertex(0,0,0);

      // Add all of the vertices to this model.
      for (int i = 0; i < n; ++i)
      {
         for (int j = 0; j < k; ++j)
         {
            addVertex( v[i][j] );
         }
      }
      addVertex( center );
      final int centerIndex = n * k;

      // Create the spokes connecting the center to the outer circle.
      for (int j = 0; j < k; ++j) // choose a spoke
      {  //                                             v[0][j]
         addPrimitive(new LineSegment( centerIndex, (0 * k) + j ));

         for (int i = 0; i < n - 1; ++i)
         {  //                                 v[i][j]        v[i+1][j]
            addPrimitive(new LineSegment( (i * k) + j, ((i+1) * k) + j ));
         }
      }

      // Create the line segments around each (partial) concentric circle.
      for (int i = 0; i < n; ++i)  // choose a circle
      {
         for (int j = 0; j < k - 1; ++j)
         {  //                                v[i][j]        v[i][j+1]
            addPrimitive(new LineSegment( (i * k) + j, (i * k) + (j + 1) ));
         }
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public DiskSector remake(final int n, final int k)
   {
      return new DiskSector(this.r,
                            this.theta1, this.theta2,
                            n, k);
   }
}//DiskSector
