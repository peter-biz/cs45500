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
   Create a wireframe model of a regular octahedron
   with its center at the origin, having side length
   {@code  sqrt(2) = 1.4142},with its center plane given
   by the four vertices {@code  (±1, 0, ±1)}. and with
   the top and bottom vertices being {@code  (0, ±1, 0)}.
<p>
   See <a href="https://en.wikipedia.org/wiki/Octahedron" target="_top">
                https://en.wikipedia.org/wiki/Octahedron</a>

   @see Tetrahedron
   @see Cube
   @see Icosahedron
   @see Dodecahedron
*/
public class Octahedron extends Model implements MeshMaker
{
   public final int n1;
   public final int n2;
   public final int n3;

   /**
      Create a regular octahedron with its center at the
      origin, having side length {@code  sqrt(2) = 1.4142},
      with its center plane given by the four vertices
      {@code  (±1, 0, ±1)}. and with the top and bottom
      vertices being {@code  (0, ±1, 0)}.
   */
   public Octahedron()
   {
      super("Octahedron");

      this.n1 = 0;
      this.n2 = 0;
      this.n3 = 0;

      // Create the octahedron's geometry.
      // It has 6 vertices and 12 edges.
      addVertex(new Vertex( 1,  0,  0),  // 4 vertices around the center plane
                new Vertex( 0,  0, -1),
                new Vertex(-1,  0,  0),
                new Vertex( 0,  0,  1),
                new Vertex( 0,  1,  0),  // vertex at the top
                new Vertex( 0, -1,  0)); // vertex at the bottom
/*
      // These vertices create an Octahedron with side length 1.
      final double sqrt3 = Math.sqrt(3.0);
      final double sqrt2 = Math.sqrt(2.0);
      addVertex(new Vertex( 0.5, 0,  0.5), // 4 vertices around the center plane
                new Vertex(-0.5, 0,  0.5),
                new Vertex(-0.5, 0, -0.5),
                new Vertex( 0.5, 0, -0.5),
                new Vertex( 0,  1/sqrt2, 0),  // vertex at the top
                new Vertex( 0, -1/sqrt2, 0)); // vertex at the bottom
*/
      // Create 12 line segments.
      // Four line segments around the center plane.
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(1, 2),
                   new LineSegment(2, 3),
                   new LineSegment(3, 0));
      // Edges going to the top vertex.
      addPrimitive(new LineSegment(0, 4),
                   new LineSegment(1, 4),
                   new LineSegment(2, 4),
                   new LineSegment(3, 4));
      // Edges going to the bottom vertex.
      addPrimitive(new LineSegment(0, 5),
                   new LineSegment(1, 5),
                   new LineSegment(2, 5),
                   new LineSegment(3, 5));
   }


   /**
      Create a regular octahedron with its center at the
      origin, having side length {@code  sqrt(2) = 1.4142},
      with its center plane given by the four vertices
      {@code  (±1, 0, ±1)}. and with the top and bottom
      vertices being {@code  (0, ±1, 0)}.
      <p>
      Add line segments fanning out from the top and bottom
      vertices to the sides around the center plane.

      @param n number of lines fanning out from the top and bottom on each side of the octahedron
      @throws IllegalArgumentException if {@code n1} is less than 0
      @throws IllegalArgumentException if {@code n2} is less than 0
   */
   public Octahedron(final int n)
   {
      this(n, 0, 0);
   }


   /**
      Create a regular octahedron with its center at the
      origin, having side length {@code  sqrt(2) = 1.4142},
      with its center plane given by the four vertices
      {@code  (±1, 0, ±1)}. and with the top and bottom
      vertices being {@code  (0, ±1, 0)}.
      <p>
      Add line segments fanning out from each vertex to
      its opposite sides.

      @param n1 number of lines fanning out from the top and bottom on each side of the octahedron
      @param n2 number of lines fanning out from v0 and v2 on each side of the octahedron
      @param n3 number of lines fanning out from v1 and v3 on each side of the octahedron
      @throws IllegalArgumentException if {@code n1} is less than 0
      @throws IllegalArgumentException if {@code n2} is less than 0
      @throws IllegalArgumentException if {@code n3} is less than 0
   */
   public Octahedron(final int n1, final int n2, final int n3)
   {
      this(n1, n1, n2, n2, n3, n3);
   }


   /**
      Create a regular octahedron with its center at the
      origin, having side length {@code  sqrt(2) = 1.4142},
      with its center plane given by the four vertices
      {@code  (±1, 0, ±1)}. and with the top and bottom
      vertices being {@code  (0, ±1, 0)}.
      <p>
      Add line segments fanning out from each vertex to
      its opposite sides.

      @param n1a number of lines fanning out from the top on each side of the octahedron
      @param n1b number of lines fanning out from the bottom on each side of the octahedron
      @param n2a number of lines fanning out from v0 on each side of the octahedron
      @param n2b number of lines fanning out from v1 on each side of the octahedron
      @param n3a number of lines fanning out from v2 on each side of the octahedron
      @param n3b number of lines fanning out from v3 on each side of the octahedron
      @throws IllegalArgumentException if {@code n1a} is less than 0
      @throws IllegalArgumentException if {@code n1b} is less than 0
      @throws IllegalArgumentException if {@code n2a} is less than 0
      @throws IllegalArgumentException if {@code n2b} is less than 0
      @throws IllegalArgumentException if {@code n3a} is less than 0
      @throws IllegalArgumentException if {@code n3b} is less than 0
   */
   public Octahedron(final int n1a, final int n1b,
                     final int n2a, final int n2b,
                     final int n3a, final int n3b)
   {
      super(String.format("Octahedron(%d,%d,%d,%d,%d,%d)",n1a,n1b,n2a,n2b,n3a,n3b));

      if (n1a < 0)
         throw new IllegalArgumentException("n1 must be greater than or equal to 0");
      if (n1b < 0)
         throw new IllegalArgumentException("n1 must be greater than or equal to 0");
      if (n2a < 0)
         throw new IllegalArgumentException("n2 must be greater than or equal to 0");
      if (n2b < 0)
         throw new IllegalArgumentException("n2 must be greater than or equal to 0");
      if (n3a < 0)
         throw new IllegalArgumentException("n3 must be greater than or equal to 0");
      if (n3b < 0)
         throw new IllegalArgumentException("n3 must be greater than or equal to 0");

      this.n1 = n1a;
      this.n2 = n2a;
      this.n3 = n3a;

      // Create the octahedron's geometry.
      // It has 6 vertices and 12 edges.
      final Vertex v0 = new Vertex( 1,  0,  0); // 4 vertices around the center plane
      final Vertex v1 = new Vertex( 0,  0, -1);
      final Vertex v2 = new Vertex(-1,  0,  0);
      final Vertex v3 = new Vertex( 0,  0,  1);
      final Vertex v4 = new Vertex( 0,  1,  0); // vertex at the top
      final Vertex v5 = new Vertex( 0, -1,  0); // vertex at the bottom
      addVertex(v0, v1, v2, v3, v4, v5);
/*
      // These vertices create an Octahedron with side length 1.
      final double sqrt3 = Math.sqrt(3.0);
      final double sqrt2 = Math.sqrt(2.0);
      final Vertex v0 = new Vertex( 0.5, 0,  0.5); // 4 vertices around the center plane
      final Vertex v1 = new Vertex(-0.5, 0,  0.5);
      final Vertex v2 = new Vertex(-0.5, 0, -0.5);
      final Vertex v3 = new Vertex( 0.5, 0, -0.5);
      final Vertex v4 = new Vertex( 0,  1/sqrt2, 0); // vertex at the top
      final Vertex v5 = new Vertex( 0, -1/sqrt2, 0); // vertex at the bottom
      addVertex(v0, v1, v2, v3, v4, v5);
*/
      // Create 12 line segments.
      // four line segments around the center plane
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(1, 2),
                   new LineSegment(2, 3),
                   new LineSegment(3, 0));
      // edges going to the top vertex
      addPrimitive(new LineSegment(0, 4),
                   new LineSegment(1, 4),
                   new LineSegment(2, 4),
                   new LineSegment(3, 4));
      // edges going to the bottom vertex
      addPrimitive(new LineSegment(0, 5),
                   new LineSegment(1, 5),
                   new LineSegment(2, 5),
                   new LineSegment(3, 5));

      fan(n1a, 4, v0, v1, v2, v3); // fan out from v4 (top)
      fan(n1b, 5, v0, v1, v2, v3); // fan out from v5 (bottom)
      fan(n2a, 0, v3, v4, v1, v5); // fan out from v0
      fan(n3a, 1, v0, v4, v2, v5); // fan out from v1
      fan(n2b, 2, v1, v4, v3, v5); // fan out from v2
      fan(n3b, 3, v2, v4, v0, v5); // fan out from v3
   }


   /**
      Create {@code n} line segments fanning out from {@link Vertex}
      {@code v0} towards the four edges spanned by the other four
      vertices.

      @param n   number of lines fanning out from {@link Vertex} {@code v0}
      @param v0  index in the {@link Vertex} list of the vertex to fan out from
      @param v1  a {@link Vertex} opposite to {@code v0}
      @param v2  a {@link Vertex} opposite to {@code v0}
      @param v3  a {@link Vertex} opposite to {@code v0}
      @param v4  a {@link Vertex} opposite to {@code v0}
   */
   private void fan(final int n, final int v0, final Vertex v1,
                                               final Vertex v2,
                                               final Vertex v3,
                                               final Vertex v4)
   {
      for (int i = 0; i < n; ++i)
      {
         // Use linear interpolation (lerp).
         final double t = (double)(i+1) / (double)(n+1);
         final double x = (1-t) * v1.x + t * v2.x;
         final double y = (1-t) * v1.y + t * v2.y;
         final double z = (1-t) * v1.z + t * v2.z;
         final Vertex v = new Vertex(x, y, z);
         final int index = vertexList.size();
         addVertex(v);
         addPrimitive(new LineSegment(v0, index));
      }
      for (int i = 0; i < n; ++i)
      {
         // Use linear interpolation (lerp).
         final double t = (double)(i+1) / (double)(n+1);
         final double x = (1-t) * v2.x + t * v3.x;
         final double y = (1-t) * v2.y + t * v3.y;
         final double z = (1-t) * v2.z + t * v3.z;
         final Vertex v = new Vertex(x, y, z);
         final int index = vertexList.size();
         addVertex(v);
         addPrimitive(new LineSegment(v0, index));
      }
      for (int i = 0; i < n; ++i)
      {
         // Use linear interpolation (lerp).
         final double t = (double)(i+1) / (double)(n+1);
         final double x = (1-t) * v3.x + t * v4.x;
         final double y = (1-t) * v3.y + t * v4.y;
         final double z = (1-t) * v3.z + t * v4.z;
         final Vertex v = new Vertex(x, y, z);
         final int index = vertexList.size();
         addVertex(v);
         addPrimitive(new LineSegment(v0, index));
      }
      for (int i = 0; i < n; ++i)
      {
         // Use linear interpolation (lerp).
         final double t = (double)(i+1) / (double)(n+1);
         final double x = (1-t) * v4.x + t * v1.x;
         final double y = (1-t) * v4.y + t * v1.y;
         final double z = (1-t) * v4.z + t * v1.z;
         final Vertex v = new Vertex(x, y, z);
         final int index = vertexList.size();
         addVertex(v);
         addPrimitive(new LineSegment(v0, index));
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return this.n1;}

   @Override public int getVertCount() {return this.n2;}

   @Override
   public Octahedron remake(final int n, final int k)
   {
      return new Octahedron(n, k, this.n3);
   }
}//Octahedron
