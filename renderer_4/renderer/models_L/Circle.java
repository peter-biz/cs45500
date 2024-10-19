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
   Create a wireframe model of a circle in
   the xy-plane centered at the origin.
*/
public class Circle extends Model implements MeshMaker
{
   public final double r;
   public final int n;

   /**
      Create a circle in the xy-plane with radius 1 and
      with 16 line segments around the circumference.
   */
   public Circle( )
   {
      this(1, 16);
   }


   /**
      Create a circle in the xy-plane with radius {@code r}
      and with 16 line segments around the circumference.

      @param r  radius of the circle
   */
   public Circle(final double r)
   {
      this(r, 16);
   }


   /**
      Create a circle in the xy-plane with radius {@code r}
      and with {@code n} line segments around the circumference.

      @param r  radius of the circle
      @param n  number of line segments in the circle's circumference
      @throws IllegalArgumentException if {@code n} is less than 3
   */
   public Circle(final double r, final int n)
   {
      super(String.format("Circle(%f.2,%d)", r, n));

      if (n < 3)
         throw new IllegalArgumentException("n must be greater than 2");

      this.r = r;
      this.n = n;

      // Create the circle's geometry.
      final double deltaTheta = (2.0 * Math.PI) / n;

      // Create all the vertices.
      for (int i = 0; i < n; ++i)
      {
         final double c = Math.cos(i * deltaTheta),
                      s = Math.sin(i * deltaTheta);
         addVertex( new Vertex(r * c, r * s, 0) );
      }

      // Create the line segments around the circle.
      for (int i = 0; i < n - 1; ++i)
      {
         addPrimitive(new LineSegment(i, i+1));
      }
      addPrimitive(new LineSegment(n-1, 0));
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return n;}

   @Override
   public Circle remake(final int n, final int k)
   {
      final int newN;
      if (n != this.n)
         newN = n;
      else
         newN = k;

      return new Circle(this.r, newN);
   }
}//Circle
