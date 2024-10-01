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
   Create a wireframe model of an arc from a circle in
   the xy-plane centered at the origin.

   @see DiskSector
   @see RingSector
   @see ConeSector
   @see CylinderSector
   @see SphereSector
   @see TorusSector
*/
public class CircleSector extends Model implements MeshMaker
{
   public final double r;
   public final double theta1;
   public final double theta2;
   public final int n;

   /**
      Create half of a circle in the xy-plane with radius 1 and
      with 8 line segments around the circumference.
   */
   public CircleSector( )
   {
      this(1, 0, Math.PI, 8);
   }


   /**
      Create half of a circle in the xy-plane with radius {@code r}
      and with 8 line segments around the circumference.

      @param r  radius of the circle
   */
   public CircleSector(final double r)
   {
      this(r, 0, Math.PI, 8);
   }


   /**
      Create an arc (a sector) of a circle in the xy-plane with
      radius {@code r}, starting angle {@code theta1}, ending
      angle {@code theta2}, and with {@code n} line segments
      around the circumference.
   <p>
      The arc is drawn counterclockwise starting at angle
      {@code theta1} and ending at angle {@code theta2}. Notice
      that this means that if {@code theta1 <= theta2}, then we are
      drawing the arc between the angles, but if {@code theta1 > theta2},
      then we are removing the arc between the angles.
   <p>
      Notice that any two angles define two arcs from a circle.
      We want a definition for this method that unambiguously
      determines, for any two angles, which of the two arcs to
      draw.

      @param r       radius of the circle
      @param theta1  beginning angle of the sector (in radians)
      @param theta2  ending angle of the sector (in radians)
      @param n       number of line segments in the circle's circumference
      @throws IllegalArgumentException if {@code n} is less than 3
   */
   public CircleSector(final double r,
                       double theta1, double theta2,
                       final int n)
   {
      super(String.format("CircleSector(%.2f,%.2f,%.2f,%d)",
                                        r, theta1, theta2, n));

      if (n < 4)
         throw new IllegalArgumentException("n must be greater than 3");

      theta1 = theta1 % (2*Math.PI);
      theta2 = theta2 % (2*Math.PI);
      if (theta1 < 0) theta1 = 2*Math.PI + theta1;
      if (theta2 < 0) theta2 = 2*Math.PI + theta2;
      if (theta2 <= theta1) theta2 = theta2 + 2*Math.PI;

      this.r = r;
      this.theta1 = theta1;
      this.theta2 = theta2;
      this.n = n;

      // Create the arc's geometry.
      final double deltaTheta = (theta2 - theta1) / (n - 1);

      // Create all the vertices.
      for (int i = 0; i < n; ++i)
      {
         final double c = Math.cos(theta1 + i * deltaTheta),
                      s = Math.sin(theta1 + i * deltaTheta);
         addVertex( new Vertex(r * c, r * s, 0) );
      }

      // Create the line segments around the arc.
      for (int i = 0; i < n - 1; ++i)
      {
         addPrimitive(new LineSegment(i, i+1));
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return n;}

   @Override
   public CircleSector remake(final int n, final int k)
   {
      final int newN;
      if (n != this.n)
         newN = n;
      else
         newN = k;;

      return new CircleSector(this.r,
                              this.theta1, this.theta2,
                              newN);
   }
}//CircleSector
