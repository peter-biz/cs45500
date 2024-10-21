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
      addVertex(new Vertex(0.00, 0.00, 0.0), // 0
                new Vertex(0.00, 1.00, 0.0), // 1
                new Vertex(0.75, 1.00, 0.0), // 2
                new Vertex(1.00, 0.8,  0.0), // 3
                new Vertex(1.00, 0.6,  0.0), // 4
                new Vertex(0.75, 0.4,  0.0), // 5
                new Vertex(0.25, 0.4,  0.0), // 6
                new Vertex(0.25, 0.0,  0.0)); // 7

      addVertex(new Vertex(0.25, 0.8,  0.0), // 8
                new Vertex(0.75, 0.8,  0.0), // 9
                new Vertex(0.75, 0.6,  0.0), // 10
                new Vertex(0.25, 0.6,  0.0)); // 11

      // Create the back face vertices.
      addVertex(new Vertex(0.00, 0.00,-0.25), // 12
                new Vertex(0.00, 1.00, -0.25), // 13
                new Vertex(0.75, 1.00, -0.25), // 14
                new Vertex(1.00, 0.8, -0.25), // 15
                new Vertex(1.00, 0.6, -0.25), // 16
                new Vertex(0.75, 0.4, -0.25), // 17
                new Vertex(0.25, 0.4, -0.25), // 18
                new Vertex(0.25, 0.0, -0.25)); // 19

      addVertex(new Vertex(0.25, 0.8, -0.25), // 20
                  new Vertex(0.75, 0.8, -0.25), // 21
                  new Vertex(0.75, 0.6, -0.25), // 22
                  new Vertex(0.25, 0.6, -0.25)); // 23

      // Create the Color objects.
      Color purple = new Color(255, 0, 255);
      Color red = new Color(255, 0, 0);
      Color green = new Color(0, 255, 0);


      this.addColor(purple, red, green);



      // Create the front face line segments
      // (you need to add the Color indices!).
      addPrimitive(new LineSegment(0, 1, 2),
                   new LineSegment(1, 2, 0),
                   new LineSegment(2, 3, 0),
                   new LineSegment(3, 4, 1),
                   new LineSegment(5, 6, 0),
                   new LineSegment(6, 7, 0),
                   new LineSegment(7, 0, 0),
                   new LineSegment(5, 4, 0));

      addPrimitive(new LineSegment(8, 9, 0),
                   new LineSegment(9, 10, 0),
                   new LineSegment(10, 11, 0),
                   new LineSegment(11, 8, 0));

      // Create the back face line segments.
      addPrimitive(new LineSegment(12, 13, 2),
                   new LineSegment(13, 14, 0),
                   new LineSegment(14, 15, 0),
                   new LineSegment(15, 16, 1),
                   new LineSegment(17, 18, 0),
                   new LineSegment(18, 19, 0),
                   new LineSegment(19, 12, 0),
                   new LineSegment(17, 16, 0));

      addPrimitive(new LineSegment(20, 21, 0),
                     new LineSegment(21, 22, 0),
                     new LineSegment(22, 23, 0),
                     new LineSegment(23, 20, 0));



      // Connect front face to back face.
      addPrimitive(new LineSegment(0, 12, 2),
                  new LineSegment(1, 13, 2),
                  new LineSegment(2, 14, 0),
                  new LineSegment(3, 15, 1),
                  new LineSegment(4, 16, 1),
                  new LineSegment(5, 17, 0),
                  new LineSegment(6, 18, 0));

      addPrimitive(new LineSegment(7, 19, 0),
                  new LineSegment(8, 20, 0),
                  new LineSegment(9, 21, 0),
                  new LineSegment(10, 22, 0),
                  new LineSegment(11, 23, 0));


   }
}
