/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a icosidodecahedron
   with its center at the origin, having edge length
   <pre>{@code
     4/(1+sqrt(5)) = 1.2361,
   }</pre>
   and with its vertices on a sphere of radius
   <pre>{@code
     4/(1+sqrt(5)) * sin(2Pi/5) = 1.1756.
   }</pre>
<p>
   See <a href="https://en.wikipedia.org/wiki/Icosidodecahedron" target="_top">
                https://en.wikipedia.org/wiki/Icosidodecahedron</a>

   @see Tetrahedron
   @see Cube
   @see Octahedron
   @see Dodecahedron
   @see Icosahedron
*/
public class Icosidodecahedron extends Model
{
   /**
      Create a icosidodecahedron with its center at
      the origin, having edge length
      <pre>{@code
        4/(1+sqrt(5)) = 1.2361,
      }</pre>
      and with its vertices on a sphere of radius
      <pre>{@code
        4/(1+sqrt(5)) * sin(2Pi/5) = 1.1756.
      }</pre>
   */
   public Icosidodecahedron()
   {
      super("Icosidodecahedron");

      // Create the icosidodecahedron's geometry.
      // It has 30 vertices and 60 edges.
      //https://en.wikipedia.org/wiki/Icosidodecahedron#Cartesian_coordinates
      //http://www.georgehart.com/virtual-polyhedra/vrml/icosidodecahedron.wrl
      final double t = (1 + Math.sqrt(5))/2;  // golden ratio
      final double r = t - 1; // (-1 + Math.sqrt(5))/2;
      addVertex(new Vertex( 0,          0,          1.051462),
                new Vertex( r,          0,          0.8506508),
                new Vertex( 0.2763932,  0.5527864,  0.8506508),
                new Vertex(-r,          0,          0.8506508),
                new Vertex(-0.2763932, -0.5527864,  0.8506508),
                new Vertex( 1,          0,          0.3249197),
                new Vertex( 0.7236068, -0.5527864,  0.5257311),
                new Vertex(-0.1708204,  0.8944272,  0.5257311),
                new Vertex( 0.4472136,  0.8944272,  0.3249197),
                new Vertex(-1,          0,          0.3249197),
                new Vertex(-0.7236068,  0.5527864,  0.5257311),
                new Vertex( 0.1708204, -0.8944272,  0.5257311),
                new Vertex(-0.4472136, -0.8944272,  0.3249197),
                new Vertex( 1,          0,         -0.3249197),
                new Vertex( 0.8944272,  0.5527864,  0),
                new Vertex( 0.5527864, -0.8944272,  0),
                new Vertex(-0.5527864,  0.8944272,  0),
                new Vertex( 0.4472136,  0.8944272, -0.3249197),
                new Vertex(-1,          0,         -0.3249197),
                new Vertex(-0.8944272, -0.5527864,  0),
                new Vertex(-0.4472136, -0.8944272, -0.3249197),
                new Vertex( r,          0,         -0.8506508),
                new Vertex( 0.7236068, -0.5527864, -0.5257311),
                new Vertex( 0.1708204, -0.8944272, -0.5257311),
                new Vertex(-0.7236068,  0.5527864, -0.5257311),
                new Vertex(-0.1708204,  0.8944272, -0.5257311),
                new Vertex( 0.2763932,  0.5527864, -0.8506508),
                new Vertex(-r,          0,         -0.8506508),
                new Vertex(-0.2763932, -0.5527864, -0.8506508),
                new Vertex( 0,          0,         -1.051462));

      // Create 60 line segments (as 12 pentagon faces).
      addPrimitive(new LineSegment( 0,  2),
                   new LineSegment( 2,  7),
                   new LineSegment( 7, 10),
                   new LineSegment(10,  3),
                   new LineSegment( 3,  0),

                   new LineSegment( 0,  4),
                   new LineSegment( 4, 11),
                   new LineSegment(11,  6),
                   new LineSegment( 6,  1),
                   new LineSegment( 1,  0),

                   new LineSegment( 1,  5),
                   new LineSegment( 5, 14),
                   new LineSegment(14,  8),
                   new LineSegment( 8,  2),
                   new LineSegment( 2,  1),

                   new LineSegment( 3,  9),
                   new LineSegment( 9,  19),
                   new LineSegment(19, 12),
                   new LineSegment(12,  4),
                   new LineSegment( 4,  3),

                   new LineSegment( 5,  6),
                   new LineSegment( 6, 15),
                   new LineSegment(15, 22),
                   new LineSegment(22, 13),
                   new LineSegment(13,  5),

                   new LineSegment( 7,  8),
                   new LineSegment( 8, 17),
                   new LineSegment(17, 25),
                   new LineSegment(25, 16),
                   new LineSegment(16,  7),

                   new LineSegment( 9, 10),
                   new LineSegment(10, 16),
                   new LineSegment(16, 24),
                   new LineSegment(24, 18),
                   new LineSegment(18,  9),

                   new LineSegment(11, 12),
                   new LineSegment(12, 20),
                   new LineSegment(20, 23),
                   new LineSegment(23, 15),
                   new LineSegment(15, 11),

                   new LineSegment(13, 21),
                   new LineSegment(21, 26),
                   new LineSegment(26, 17),
                   new LineSegment(17, 14),
                   new LineSegment(14, 13),

                   new LineSegment(18, 27),
                   new LineSegment(27, 28),
                   new LineSegment(28, 20),
                   new LineSegment(20, 19),
                   new LineSegment(19, 18),

                   new LineSegment(21, 22),
                   new LineSegment(22, 23),
                   new LineSegment(23, 28),
                   new LineSegment(28, 29),
                   new LineSegment(29, 21),

                   new LineSegment(24, 25),
                   new LineSegment(25, 26),
                   new LineSegment(26, 29),
                   new LineSegment(29, 27),
                   new LineSegment(27, 24));
   }
}//Icosidodecahedron
