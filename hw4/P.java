/*

*/

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   A three-dimensional wireframe model of the letter P.

  1______________ 2
   |             \
   |   8_______ 9 \ 3
   |    |      |  |
   |    |______|  |
   |   11     10  / 4
   |  6 _________/
   |    |         5
   |____|
  0     7
*/
public class P extends Model
{
   /**
      The letter P.
   */
   public P()
   {
      super("P");

      // Create the front face vertices.
      addVertex(new Vertex(0.00, 0.00, 0.0),
                new Vertex(0.00, 1.00, 0.0),
                new Vertex(0.75, 1.00, 0.0),
                new Vertex(1.00, 0.8,  0.0),
                new Vertex(1.00, 0.6,  0.0),
                new Vertex(0.75, 0.4,  0.0),
                new Vertex(0.25, 0.4,  0.0),
                new Vertex(0.25, 0.0,  0.0));

      addVertex(new Vertex(0.25, 0.8,  0.0),
                new Vertex(0.75, 0.8,  0.0),
                new Vertex(0.75, 0.6,  0.0),
                new Vertex(0.25, 0.6,  0.0));

      // Create the back face vertices.
      addVertex(new Vertex(0.00, 0.00, -0.25),
                new Vertex(0.00, 1.00, -0.25),
                new Vertex(0.75, 1.00, -0.25),
                new Vertex(1.00, 0.8,  -0.25),
                new Vertex(1.00, 0.6,  -0.25),
                new Vertex(0.75, 0.4,  -0.25),
                new Vertex(0.25, 0.4,  -0.25),
                new Vertex(0.25, 0.0,  -0.25));

      addVertex(new Vertex(0.25, 0.8,  -0.25),
                new Vertex(0.75, 0.8,  -0.25),
                new Vertex(0.75, 0.6,  -0.25),
                new Vertex(0.25, 0.6,  -0.25));

      // Create the front face line segments.
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(1, 2),
                   new LineSegment(2, 3),
                   new LineSegment(3, 4),
                   new LineSegment(4, 5),
                   new LineSegment(5, 6),
                   new LineSegment(6, 7),
                   new LineSegment(7, 0));

      addPrimitive(new LineSegment( 8,  9),
                   new LineSegment( 9, 10),
                   new LineSegment(10, 11),
                   new LineSegment(11,  8));

      // Create the back face line segments.
      addPrimitive(new LineSegment(12, 13),
                   new LineSegment(13, 14),
                   new LineSegment(14, 15),
                   new LineSegment(15, 16),
                   new LineSegment(16, 17),
                   new LineSegment(17, 18),
                   new LineSegment(18, 19),
                   new LineSegment(19, 12));

      addPrimitive(new LineSegment(20, 21),
                   new LineSegment(21, 22),
                   new LineSegment(22, 23),
                   new LineSegment(23, 20));

      // Connect front face to back face.
      addPrimitive(new LineSegment( 0, 12),
                   new LineSegment( 1, 13),
                   new LineSegment( 2, 14),
                   new LineSegment( 3, 15),
                   new LineSegment( 4, 16),
                   new LineSegment( 5, 17),
                   new LineSegment( 6, 18),
                   new LineSegment( 7, 19),
                   new LineSegment( 8, 20),
                   new LineSegment( 9, 21),
                   new LineSegment(10, 22),
                   new LineSegment(11, 23));
   }
}
