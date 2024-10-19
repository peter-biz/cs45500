/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a regular dodecahedron
   with its center at the origin, having edge length
   <pre>{@code
     2*(sqrt(5)-1)/(1+sqrt(5)) = (1/2)*(sqrt(5)-1)^2 = 0.7639,
   }</pre>
   and with its vertices on a sphere of radius
   <pre>{@code
     2*sqrt(3)/(1+sqrt(5)) = 1.0705.
   }</pre>
<p>
   See <a href="https://en.wikipedia.org/wiki/Regular_dodecahedron" target="_top">
                https://en.wikipedia.org/wiki/Regular_dodecahedron</a>

   @see Tetrahedron
   @see Cube
   @see Octahedron
   @see Icosahedron
   @see Icosidodecahedron
*/
public class Dodecahedron extends Model
{
   /**
      Create a regular dodecahedron with its center at
      the origin, having edge length
      <pre>{@code
        2*(sqrt(5)-1)/(1+sqrt(5)) = (1/2)*(sqrt(5)-1)^2 = 0.7639,
      }</pre>
      and with its vertices on a sphere of radius
      <pre>{@code
        2*sqrt(3)/(1+sqrt(5)) = 1.0705.
     }</pre>
   */
   public Dodecahedron()
   {
      super("Dodecahedron");

      // Create the dodecahedron's geometry.
      // It has 20 vertices and 30 edges.
      final double t = (1 + Math.sqrt(5))/2,   // golden ratio
                   r = 1/t,
                  r2 = r * r;
      //https://en.wikipedia.org/wiki/Regular_dodecahedron#Cartesian_coordinates
      // (±r, ±r, ±r)
      addVertex(new Vertex(-r, -r, -r),
                new Vertex(-r, -r,  r),
                new Vertex(-r,  r, -r),
                new Vertex(-r,  r,  r),
                new Vertex( r, -r, -r),
                new Vertex( r, -r,  r),
                new Vertex( r,  r, -r),
                new Vertex( r,  r,  r));

      // (0, ±r2, ±1)
      addVertex(new Vertex( 0, -r2, -1),
                new Vertex( 0, -r2,  1),
                new Vertex( 0,  r2, -1),
                new Vertex( 0,  r2,  1));

      // (±r2, ±1, 0)
      addVertex(new Vertex(-r2, -1,  0),
                new Vertex(-r2,  1,  0),
                new Vertex( r2, -1,  0),
                new Vertex( r2,  1,  0));

      // (±1, 0, ±r2)
      addVertex(new Vertex(-1,  0, -r2),
                new Vertex( 1,  0, -r2),
                new Vertex(-1,  0,  r2),
                new Vertex( 1,  0,  r2));
/*
      // These vertices create a dodecahedron with vertices
      // on a sphere of radius sqrt(3), and with edge length
      //    2/t = 4/(1 + sqrt(5)) = sqrt(5) - 1 = 1.2361.
      //https://en.wikipedia.org/wiki/Regular_dodecahedron#Cartesian_coordinates
      // (±1, ±1, ±1)
      addVertex(new Vertex(-1, -1, -1),
                new Vertex(-1, -1,  1),
                new Vertex(-1,  1, -1),
                new Vertex(-1,  1,  1),
                new Vertex( 1, -1, -1),
                new Vertex( 1, -1,  1),
                new Vertex( 1,  1, -1),
                new Vertex( 1,  1,  1));

      // (0, ±r, ±t)
      addVertex(new Vertex( 0, -r, -t),
                new Vertex( 0, -r,  t),
                new Vertex( 0,  r, -t),
                new Vertex( 0,  r,  t));

      // (±r, ±t, 0)
      addVertex(new Vertex(-r, -t,  0),
                new Vertex(-r,  t,  0),
                new Vertex( r, -t,  0),
                new Vertex( r,  t,  0));

      // (±t, 0, ±r)
      addVertex(new Vertex(-t,  0, -r),
                new Vertex( t,  0, -r),
                new Vertex(-t,  0,  r),
                new Vertex( t,  0,  r));
*/
      // Create 30 line segments (that make up 12 faces).
//https://github.com/mrdoob/three.js/blob/master/src/geometries/DodecahedronGeometry.js
      addPrimitive(new LineSegment( 3, 11),
                   new LineSegment(11,  7),
                   new LineSegment( 7, 15),
                   new LineSegment(15, 13),
                   new LineSegment(13,  3));

      addPrimitive(new LineSegment( 7, 19),
                   new LineSegment(19, 17),
                   new LineSegment(17,  6),
                   new LineSegment( 6, 15));

      addPrimitive(new LineSegment(17,  4),
                   new LineSegment( 4,  8),
                   new LineSegment( 8, 10),
                   new LineSegment(10,  6));

      addPrimitive(new LineSegment( 8,  0),
                   new LineSegment( 0, 16),
                   new LineSegment(16,  2),
                   new LineSegment( 2, 10));

      addPrimitive(new LineSegment( 0, 12),
                   new LineSegment(12,  1),
                   new LineSegment( 1, 18),
                   new LineSegment(18, 16));

      addPrimitive(new LineSegment( 2, 13));

      addPrimitive(new LineSegment(18,  3));

      addPrimitive(new LineSegment( 1,  9),
                   new LineSegment( 9, 11));

      addPrimitive(new LineSegment( 4, 14),
                   new LineSegment(14, 12));

      addPrimitive(new LineSegment( 9,  5),
                   new LineSegment( 5, 19));

      addPrimitive(new LineSegment( 5, 14));
   }
}//Dodecahedron
