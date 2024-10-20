import renderer.scene.*;
import renderer.scene.primitives.*;

import java.awt.Color;

/**
   A three-dimensional wireframe model of the letter N.
   
   0________9  7__ 6
   |        \ |   |
   |   3     \|   |
   |   |\     8   |
   |   |  \      |
   |___|     \___|
   1   2      4   5
*/
public class N extends Model
{
   /**
      The letter N.
   */
   public N()
   {
      super("N");

      // Create the front face vertices.
      addVertex(new Vertex(0.00, 1.00, 0.0),  // 0
                new Vertex(0.00, 0.00, 0.0),  // 1
                new Vertex(0.25, 0.00, 0.0),  // 2
                new Vertex(0.25, 0.75, 0.0),  // 3
                new Vertex(0.75, 0.00, 0.0),  // 4
                new Vertex(1.00, 0.00, 0.0),  // 5
                new Vertex(1.00, 1.00, 0.0),  // 6
                new Vertex(0.75, 1.00, 0.0),  // 7
                new Vertex(0.75, 0.5, 0.0),  // 8
                new Vertex(0.5, 1.00, 0.0)); // 9

      // Create the back face vertices.
      addVertex(new Vertex(0.00, 1.00, 0.2),  // 9
                new Vertex(0.00, 0.00, 0.2),  // 10
                new Vertex(0.25, 0.00, 0.2),  // 11
                new Vertex(0.25, 0.75, 0.2),  // 12
                new Vertex(0.75, 0.00, 0.2),  // 13
                new Vertex(1.00, 0.00, 0.2),  // 14
                new Vertex(1.00, 1.00, 0.2),  // 15
                new Vertex(0.75, 1.00, 0.2),  // 16
                new Vertex(0.75, 0.25, 0.2)); // 17

      // Create the Color objects.


      // Create the front face line segments.
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(1, 2),
                   new LineSegment(2, 3),
                   new LineSegment(3, 4),
                   new LineSegment(4, 5),
                   new LineSegment(5, 6),
                   new LineSegment(6, 7),
                   new LineSegment(7, 8),
                   new LineSegment(8, 9),
                   new LineSegment(9, 0));

      // Create the back face line segments.
      // addPrimitive(new LineSegment(9, 10),
      //              new LineSegment(10, 11),
      //              new LineSegment(11, 12),
      //              new LineSegment(12, 17),
      //              new LineSegment(17, 13),
      //              new LineSegment(13, 14),
      //              new LineSegment(14, 15),
      //              new LineSegment(15, 16),
      //              new LineSegment(16, 9));

      // // Connect front face to back face.
      // addPrimitive(new LineSegment(0, 9),
      //              new LineSegment(1, 10),
      //              new LineSegment(2, 11),
      //              new LineSegment(3, 12),
      //              new LineSegment(4, 13),
      //              new LineSegment(5, 14),
      //              new LineSegment(6, 15),
      //              new LineSegment(7, 16),
      //              new LineSegment(8, 17));
   }
}