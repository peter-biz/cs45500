/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.MeshMaker;

import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;    // could use this instead
import java.util.function.DoubleUnaryOperator; // could use this instead
//https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html

/**
   Create a wireframe model of a parametric curve in space.
<p>
   See <a href="https://en.wikipedia.org/wiki/Parametric_equation" target="_top">
                https://en.wikipedia.org/wiki/Parametric_equation</a>

   @see ParametricSurface
*/
public class ParametricCurve extends Model implements MeshMaker
{
   public final DoubleFunction<Double> x;
   public final DoubleFunction<Double> y;
   public final DoubleFunction<Double> z;
   public final double t1;
   public final double t2;
   public final int n;

   /**
      Create a trefoil knot as a parametric curve in space.
   <p>
      See <a href="https://en.wikipedia.org/wiki/Trefoil_knot#Descriptions" target="_top">
                   https://en.wikipedia.org/wiki/Trefoil_knot#Descriptions</a>
   */
   public ParametricCurve()
   {
      this(t ->  0.5*Math.sin(t) + Math.sin(2*t),
           t ->  0.5*Math.cos(t) - Math.cos(2*t),
           t -> -0.5*Math.sin(3*t),
           0, 2*Math.PI, 60);
   }


   /**
      Create a parametric curve in the xy-plane,
      <pre>{@code
         x = x(t)
         y = y(t)
      }</pre>
      with the parameter {@code  t} having the given parameter
      range and the given number of line segments.

      @param x   component function in the x-direction
      @param y   component function in the y-direction
      @param t1  beginning value of parameter range
      @param t2  ending value of parameter range
      @param n   number of line segments in the curve
      @throws IllegalArgumentException if {@code n} is less than 1
   */
   public ParametricCurve(final DoubleFunction<Double> x,
                          final DoubleFunction<Double> y,
                          final double t1, final double t2,
                          final int n)
   {
      this(x, y, t->0.0, t1, t2, n);
   }


   /**
      Create a parametric curve in space,
      <pre>{@code
         x = x(t)
         y = y(t)
         z = z(t)
      }</pre>
      with the parameter {@code t} having the given parameter
      range and the given number of line segments.

      @param x   component function in the x-direction
      @param y   component function in the y-direction
      @param z   component function in the z-direction
      @param t1  beginning value of parameter range
      @param t2  ending value of parameter range
      @param n   number of line segments in the curve
      @throws IllegalArgumentException if {@code n} is less than 1
   */
   public ParametricCurve(final DoubleFunction<Double> x,
                          final DoubleFunction<Double> y,
                          final DoubleFunction<Double> z,
                          final double t1, final double t2,
                          final int n)
   {
      super(String.format("Parametric Curve(%d)", n));

      if (n < 1)
         throw new IllegalArgumentException("n must be greater than 0");

      this.x = x;
      this.y = y;
      this.z = z;
      this.t1 = t1;
      this.t2 = t2;
      this.n = n;

      // Create the curve's geometry.
      final double deltaT = (t2 - t1) / n;

      for (int i = 0; i < n + 1; ++i)
      {
         addVertex( new Vertex( x.apply(t1 + i * deltaT),
                                y.apply(t1 + i * deltaT),
                                z.apply(t1 + i * deltaT) ) );
      }

      for (int i = 0; i < n; ++i)
      {
         addPrimitive(new LineSegment(i, i+1));
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return n;}

   @Override
   public ParametricCurve remake(final int n, final int k)
   {
      final int newN;
      if (n != this.n)
         newN = n;
      else
         newN = k;

      return new ParametricCurve(this.x, this.y, this.z,
                                 this.t1, this.t2,
                                 newN);
   }
}//ParametricCurve
