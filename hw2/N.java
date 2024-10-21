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
      addVertex(new Vertex(0.00, 1.00, -0.25),  // 10
                new Vertex(0.00, 0.00, -0.25),  // 11
                new Vertex(0.25, 0.00, -0.25),  // 12
                new Vertex(0.25, 0.75, -0.25),  // 13
                new Vertex(0.75, 0.00, -0.25),  // 14
                new Vertex(1.00, 0.00, -0.25),  // 15
                new Vertex(1.00, 1.00, -0.25),  // 16
                new Vertex(0.75, 1.00, -0.25),  // 17
                new Vertex(0.75, 0.5, -0.25),  // 18
                new Vertex(0.5, 1.00, -0.25)); // 19

      // Create the Color objects.
      Color purple = new Color(255, 0, 255);
      Color red = new Color(255, 0, 0);
      Color green = new Color(0, 255, 0);


      this.addColor(purple, red, green);

      // Create the front face line segments.
      addPrimitive(new LineSegment(0, 1, 2),
                   new LineSegment(1, 2, 2),
                   new LineSegment(2, 3, 2),
                   new LineSegment(3, 4, 0),
                   new LineSegment(4, 5, 1),
                   new LineSegment(5, 6, 1),
                   new LineSegment(6, 7, 1),
                   new LineSegment(7, 8, 1),
                   new LineSegment(8, 9, 0),
                   new LineSegment(9, 0, 2));

      // Create the back face line segments.
      addPrimitive(new LineSegment(10, 11, 2),
                   new LineSegment(11, 12, 2),
                     new LineSegment(12, 13, 2),
                     new LineSegment(13, 14, 0),
                     new LineSegment(14, 15, 1),
                     new LineSegment(15, 16, 1),
                     new LineSegment(17, 16, 1),
                     new LineSegment(17, 18, 1),
                     new LineSegment(18, 19, 0),
                     new LineSegment(19, 10, 2));

      // Connect front face to back face.
      addPrimitive(new LineSegment(0, 10, 2),
                  new LineSegment(1, 11, 2),
                  new LineSegment(2, 12, 2),
                  new LineSegment(3, 13, 2),
                  new LineSegment(4, 14, 1),
                  new LineSegment(5, 15, 1),
                  new LineSegment(7, 17, 1),
                  new LineSegment(6, 16, 1),
                  new LineSegment(8, 18, 1),
                  new LineSegment(9, 19, 2));
      
   }
}