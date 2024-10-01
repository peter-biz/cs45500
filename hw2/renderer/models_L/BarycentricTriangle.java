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
   Create a wireframe model of a barycentricly subdivided
   equilateral triangle.

   See <a href="https://en.wikipedia.org/wiki/Barycentric_subdivision" target="_top">
                https://en.wikipedia.org/wiki/Barycentric_subdivision</a>
*/
public class BarycentricTriangle extends Model implements MeshMaker
{
   public final double theta;
   public final int n;

   /**
      Create a barycentricly subdivided equilateral triangle
      in the xy-plane with corners on the unit circle.
      <p>
      The value of {@code n} should be less than 8.

      @param n  number of barycentric subdivisions of the triangle
      @throws IllegalArgumentException if {@code n} is less than 0
   */
   public BarycentricTriangle(final int n)
   {
      this(0, n);
   }


   /**
      Create a barycentricly subdivided equilateral triangle
      in the xy-plane with corners on the unit circle and
      rotated by angle {@code theta} degrees.
      <p>
      The value of {@code n} should be less than 8.

      @param theta  rotation (in degrees) of the equilateral triangle
      @param n      number of barycentric subdivisions of this triangle
      @throws IllegalArgumentException if {@code n} is less than 0
   */
   public BarycentricTriangle(final double theta, final int n)
   {
      super(String.format("Barycentric Triangle(%.2f,%d)", theta, n));

      if (n < 0)
         throw new IllegalArgumentException("n must be greater than or equal to0");

      this.theta = theta;
      this.n = n;

      final double theta1 = theta * Math.PI/180.0,
                   theta2 = 2.0 * Math.PI / 3.0;
      addVertex(new Vertex(Math.cos(theta1),
                           Math.sin(theta1),
                           0.0),
                new Vertex(Math.cos(theta1 + theta2),
                           Math.sin(theta1 + theta2),
                           0.0),
                new Vertex(Math.cos(theta1 + 2*theta2),
                           Math.sin(theta1 + 2*theta2),
                           0.0));
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(1, 2),
                   new LineSegment(2, 0));

      if (n > 0)
         barycentric(0, 1, 2, n);
   }


   /**
      Recursively use barycentric subdivision to put into this
      {@link Model} vertices and line segments that subdivide
      the triangle whose vertices are indexed by {@code vIndex0},
      {@code vIndex1} and {@code vIndex2}.
      <p>
      The value of {@code n} should be less than 8.

      @param vIndex0  index of a {link Vertex} of a triangle
      @param vIndex1  index of a {link Vertex} of a triangle
      @param vIndex2  index of a {link Vertex} of a triangle
      @param n        number of barycentric subdivisions of this triangle
   */
   public void barycentric(final int vIndex0,
                           final int vIndex1,
                           final int vIndex2,
                           final int n)
   {
      final Vertex v0 = vertexList.get(vIndex0),
                   v1 = vertexList.get(vIndex1),
                   v2 = vertexList.get(vIndex2);
      final int index = vertexList.size();

      if (n > 0)
      {
         // Barycentric subdivision.
         // https://en.wikipedia.org/wiki/Barycentric_subdivision

         // Add four vertices to the model.
         addVertex(new Vertex(
         //         (1/3)*v0 + (1/3)*v1 + (1/3)*v2
                    (v0.x + v1.x + v2.x)/3.0,
                    (v0.y + v1.y + v2.y)/3.0,
                    (v0.z + v1.z + v2.z)/3.0));
         addVertex(new Vertex(
         //         (1/2)*v0 + (1/2)*v1
                    (v0.x + v1.x)/2.0,
                    (v0.y + v1.y)/2.0,
                    (v0.z + v1.z)/2.0));
         addVertex(new Vertex(
         //         (1/2)*v1 + (1/2)*v2
                    (v1.x + v2.x)/2.0,
                    (v1.y + v2.y)/2.0,
                    (v1.z + v2.z)/2.0));
         addVertex(new Vertex(
         //         (1/2)*v2 + (1/2)*v0
                    (v2.x + v0.x)/2.0,
                    (v2.y + v0.y)/2.0,
                    (v2.z + v0.z)/2.0));
         // Give a name to the index of each of the four new vertices.
         final int vIndexCenter = index,
                   vIndex01     = index + 1,
                   vIndex12     = index + 2,
                   vIndex20     = index + 3;
         // 6 new line segments
         addPrimitive(new LineSegment(vIndex0,  vIndexCenter),
                      new LineSegment(vIndex1,  vIndexCenter),
                      new LineSegment(vIndex2,  vIndexCenter),
                      new LineSegment(vIndex01, vIndexCenter),
                      new LineSegment(vIndex12, vIndexCenter),
                      new LineSegment(vIndex20, vIndexCenter));

         barycentric(vIndex0, vIndex01, vIndexCenter, n-1);
         barycentric(vIndex0, vIndex20, vIndexCenter, n-1);
         barycentric(vIndex1, vIndex01, vIndexCenter, n-1);
         barycentric(vIndex1, vIndex12, vIndexCenter, n-1);
         barycentric(vIndex2, vIndex12, vIndexCenter, n-1);
         barycentric(vIndex2, vIndex20, vIndexCenter, n-1);
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return this.n;}

   @Override public int getVertCount() {return (int)Math.round(theta);}

   @Override
   public BarycentricTriangle remake(final int n, final int k)
   {
      return new BarycentricTriangle(k, n);
   }
}//BarycentricTriangle
