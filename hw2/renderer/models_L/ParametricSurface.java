/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.MeshMaker;

import java.util.function.DoubleBinaryOperator;
import java.util.function.ToDoubleBiFunction; // could use this instead
//https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html

/**
   Create a wireframe model of a parametric surface in space.
<p>
   See <a href="https://en.wikipedia.org/wiki/Parametric_surface" target="_top">
                https://en.wikipedia.org/wiki/Parametric_surface</a>

   @see ParametricCurve
*/
public class ParametricSurface extends Model implements MeshMaker
{
   public final DoubleBinaryOperator x;
   public final DoubleBinaryOperator y;
   public final DoubleBinaryOperator z;
   public final double s1;
   public final double s2;
   public final double t1;
   public final double t2;
   public final int n;
   public final int k;

   /**
      Create a graph of the function with the following formula,
      <pre>{@code
          f(x,z) = sin(PI*x) * sin(PI*z)
      }</pre>
      as a parametric surface.
   */
   public ParametricSurface()
   {
      this((s,t) -> Math.sin(Math.PI*s) * Math.sin(Math.PI*t),
           -1.0, 1.0, -1.0, 1.0,
            49, 49);
   }


   /**
      Create a graph of a function of two variables
      {@code y = f(x, z)} as a parametric surface with
      the given parameter ranges in the {@code x} and
      {@code z} directions.

      @param f   function of x and z
      @param x1  beginning value of x-parameter range
      @param x2  ending value of x-parameter range
      @param z1  beginning value of y-parameter range
      @param z2  ending value of z-parameter range
      @param n   number of mesh lines in x-range
      @param k   number of mesh lines in y-range
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 2
   */
   public ParametricSurface(final DoubleBinaryOperator f,
                            final double x1, final double x2,
                            final double z1, final double z2,
                            final int n, final int k)
   {
      this((x,z) -> x, f, (x,z) -> z, x1, x2, z1, z2, n, k);
   }


   /**
      Create a parametric surface in space,
      <pre>{@code
         x = x(s,t)
         y = y(s,t)
         z = z(s,t)
      }</pre>
      with the parameters {@code s} and {@code t} having
      the given parameter ranges and the given number of
      mesh lines in each parametric direction.

      @param x   component function in the x-direction
      @param y   component function in the y-direction
      @param z   component function in the z-direction
      @param s1  beginning value of first parameter range
      @param s2  ending value of first parameter range
      @param t1  beginning value of second parameter range
      @param t2  ending value of second parameter range
      @param n   number of mesh lines in first range
      @param k   number of mesh lines in second range
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 2
   */
   public ParametricSurface(final DoubleBinaryOperator x,
                            final DoubleBinaryOperator y,
                            final DoubleBinaryOperator z,
                            final double s1, final double s2,
                            final double t1, final double t2,
                            final int n, final int k)
   {
      this(x, y, z, s1, s2, t1, t2, n, k,
           String.format("Parametric Surface(%d,%d)", n, k));
   }


   /**
      Create a parametric surface in space,
      <pre>{@code
         x = x(s,t)
         y = y(s,t)
         z = z(s,t)
      }</pre>
      with the parameters {@code s} and {@code t} having
      the given parameter ranges and the given number of
      mesh lines in each parametric direction.

      @param x     component function in the x-direction
      @param y     component function in the y-direction
      @param z     component function in the z-direction
      @param s1    beginning value of first parameter range
      @param s2    ending value of first parameter range
      @param t1    beginning value of second parameter range
      @param t2    ending value of second parameter range
      @param n     number of mesh lines in first range
      @param k     number of mesh lines in second range
      @param name  {@link String} name for this surface
      @throws IllegalArgumentException if {@code n} is less than 2
      @throws IllegalArgumentException if {@code k} is less than 2
   */
   public ParametricSurface(final DoubleBinaryOperator x,
                            final DoubleBinaryOperator y,
                            final DoubleBinaryOperator z,
                            final double s1, final double s2,
                            final double t1, final double t2,
                            final int n, final int k,
                            String name)
   {
      super(name);

      if (n < 2)
         throw new IllegalArgumentException("n must be greater than 1");
      if (k < 2)
         throw new IllegalArgumentException("k must be greater than 1");

      this.x = x;
      this.y = y;
      this.z = z;
      this.s1 = s1;
      this.s2 = s2;
      this.t1 = t1;
      this.t2 = t2;
      this.n = n;
      this.k = k;

      // Create the surface's geometry.

      final double deltaS = (s2 - s1) / (n - 1), // lines of latitude (dy)
                   deltaT = (t2 - t1) / (k - 1); // lines of longitude (dx)

      // An array of vertices to be used to create the line segments.
      final Vertex[][] v = new Vertex[n][k];

      // Create all the vertices.
      for (int i = 0; i < n; ++i) // choose a line of latitude
      {
         for (int j = 0; j < k; ++j) // choose a line of longitude
         {
            v[i][j] = new Vertex(x.applyAsDouble(s1 + i*deltaS, t1 + j*deltaT),
                                 y.applyAsDouble(s1 + i*deltaS, t1 + j*deltaT),
                                 z.applyAsDouble(s1 + i*deltaS, t1 + j*deltaT));
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

      // Create the horizontal line segments.
      for (int i = 0; i < n; ++i) // choose a line of latitude
      {
         for (int j = 0; j < k - 1; ++j) // choose a line of longitude
         {  //                               v[i][j]        v[i][j+1]
            addPrimitive(new LineSegment( (i * k) + j, (i * k) + (j+1) ));
         }
      }

      // Create the vertical line segments.
      for (int j = 0; j < k; ++j) // choose a line of longitude
      {
         for (int i = 0; i < n - 1; ++i) // choose a line of latitude
         {  //                              v[i][j]         v[i+1][j]
            addPrimitive(new LineSegment( (i * k) + j, ((i+1) * k) + j ));
         }
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public ParametricSurface remake(final int n, final int k)
   {
      return new ParametricSurface(this.x, this.y, this.z,
                                   this.s1, this.s2,
                                   this.t1, this.t2,
                                   n, k);
   }
}//ParametricSurface
