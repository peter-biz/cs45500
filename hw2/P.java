/*


*/

import renderer.scene.*;
import renderer.scene.primitives.*;

import java.awt.Color;

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



      // Create the Color objects.



      // Create the front face line segments
      // (you need to add the Color indices!).
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



      // Connect front face to back face.


   }
}
