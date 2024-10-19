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
      addVertex(new Vertex(0.00, 0.00, 0.2),
                new Vertex(0.00, 1.00, 0.2),
                new Vertex(0.75, 1.00, 0.2),
                new Vertex(1.00, 0.8,  0.2),
                new Vertex(1.00, 0.6,  0.2),
                new Vertex(0.75, 0.4,  0.2),
                new Vertex(0.25, 0.4,  0.2),
                new Vertex(0.25, 0.0,  0.2));

      addVertex(new Vertex(0.25, 0.8,  0.2),
                  new Vertex(0.75, 0.8,  0.2),
                  new Vertex(0.75, 0.6,  0.2),
                  new Vertex(0.25, 0.6,  0.2));

      // Create the Color objects.
      int magenta = 12976326;
      int red = 13369344;
      int green = 44288;



      // Create the front face line segments
      // (you need to add the Color indices!).
      addPrimitive(new LineSegment(0, 1, green),
                   new LineSegment(1, 2, green),
                   new LineSegment(2, 3, magenta),
                   new LineSegment(3, 4, magenta),
                   new LineSegment(4, 5, magenta),
                   new LineSegment(5, 6, magenta),
                   new LineSegment(6, 7, red),
                   new LineSegment(7, 0, red));

      addPrimitive(new LineSegment( 8,  9, red),
                   new LineSegment( 9, 10, green),
                   new LineSegment(10, 11, green),
                   new LineSegment(11,  8, magenta));

      // Create the back face line segments.



      // Connect front face to back face.


   }
}
