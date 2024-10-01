/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a sphere centered at the origin
   by recursively subdividing the faces of a tetrahedron.
<p>
   Also use this subdivision process to draw Sierpinski triangles
   on the surface of the sphere.
*/
public class SphereSubdivided extends Model
{
   /**
      Create a sphere centered at the origin by recursively
      subdividing the faces of a tetrahedron four times.
   */
   public SphereSubdivided()
   {
      this(4);
   }


   /**
      Create a sphere centered at the origin by recursively
      subdividing the faces of a tetrahedron {@code n} times.

      @param n  number of recursive subdivisions
      @throws IllegalArgumentException if {@code n} is less than 0
   */
   public SphereSubdivided(final int n)
   {
      this(n, false, false);
   }


   /**
      Create a sphere centered at the origin by recursively
      subdividing the faces of a tetrahedron {@code n} times.
   <p>
      The {@code hole} parameter leaves out one of the original
      four triangle faces of the tetrahedron. This creates a hole
      in the final sphere that is useful for looking at the back
      side of the sphere.
   <p>
      The {@code sierpinski} parameter creates Sierpinski triangles
      on the sphere.

      @param n           number of recursive subdivisions
      @param hole        do not render one of the four triangles of the tetrahedron
      @param sierpinski  create Sierpinski triangles
      @throws IllegalArgumentException if {@code n} is less than 0
   */
   public SphereSubdivided(final int n, final boolean hole, final boolean sierpinski)
   {
      super(String.format("Sphere Subdivided(%d)", n));

      if (n < 0)
         throw new IllegalArgumentException("n must be greater than or equal to 0");

      // Start with the tetrahedron's geometry.
      final double sqr3inv = 1.0/Math.sqrt(3);
      final Vertex v0 = new Vertex( sqr3inv,  sqr3inv,  sqr3inv),
                   v1 = new Vertex(-sqr3inv,  sqr3inv, -sqr3inv),
                   v2 = new Vertex( sqr3inv, -sqr3inv, -sqr3inv),
                   v3 = new Vertex(-sqr3inv, -sqr3inv,  sqr3inv);

      // Subdivide each of the tetrahedron's four triangles.
      sub(n, v0, v1, v2, sierpinski);
      sub(n, v1, v3, v2, sierpinski);
      sub(n, v2, v3, v0, sierpinski);
      if (! hole) sub(n, v3, v1, v0, sierpinski);
   }


   /**
      Recursive helper function.

      @param n           number of recursive subdivisions
      @param v0          vertex of a triangle on the sphere
      @param v1          vertex of a triangle on the sphere
      @param v2          vertex of a triangle on the sphere
      @param sierpinski  create Sierpinski triangles
   */
   private void sub(final int n,
                    final Vertex v0, final Vertex v1, final Vertex v2,
                    final boolean sierpinski)
   {
      assert (n >= 0);

      if (0 == n)
      {
         final int index = vertexList.size();
         addVertex(v0, v1, v2);
         addPrimitive(new LineSegment(index+0, index+1),  // v0, v1
                      new LineSegment(index+1, index+2),  // v1, v2
                      new LineSegment(index+2, index+0)); // v2, v0
      }
      else
      {
         // Subdivide each of the three edges.
         final Vertex v3 = new Vertex(0.5*(v0.x + v1.x),
                                      0.5*(v0.y + v1.y),
                                      0.5*(v0.z + v1.z));
         final Vertex v4 = new Vertex(0.5*(v1.x + v2.x),
                                      0.5*(v1.y + v2.y),
                                      0.5*(v1.z + v2.z));
         final Vertex v5 = new Vertex(0.5*(v2.x + v0.x),
                                      0.5*(v2.y + v0.y),
                                      0.5*(v2.z + v0.z));
         // Normalize the subdivision points.
         final double L3 = Math.sqrt(v3.x * v3.x + v3.y * v3.y + v3.z * v3.z),
                      L4 = Math.sqrt(v4.x * v4.x + v4.y * v4.y + v4.z * v4.z),
                      L5 = Math.sqrt(v5.x * v5.x + v5.y * v5.y + v5.z * v5.z);
         final Vertex v3n = new Vertex(v3.x / L3, v3.y / L3, v3.z / L3),
                      v4n = new Vertex(v4.x / L4, v4.y / L4, v4.z / L4),
                      v5n = new Vertex(v5.x / L5, v5.y / L5, v5.z / L5);
         // Recursively do another subdivision.
         sub(n-1, v0,  v3n, v5n, sierpinski);
         sub(n-1, v5n, v4n, v2,  sierpinski);
         sub(n-1, v3n, v1,  v4n, sierpinski);
         if (! sierpinski) sub(n-1, v3n, v4n, v5n, sierpinski);
      }
   }
}//SphereSubdivided
