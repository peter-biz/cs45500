/*


*/

import renderer.scene.*;
import renderer.scene.primitives.*;

import java.awt.Color;

/**
   A three-dimensional wireframe model of the letter W.

   0____1     2____3     4____5
    \    \    /     \    /   /
     \    \  /       \  /   /
      \    \/    12   \/   /
       \   6    /\     7  /
        \      /  \      /
         \____/    \____/
         8   9     10   11
*/
public class W extends Model
{
   /**
      The letter W.
   */
   public W()
   {
      super("W");

      // Create the front face vertices.
      addVertex(new Vertex(0.0, 1.00, 0.0),  // 0
                new Vertex(0.2, 1.00, 0.0),  // 1
                new Vertex(0.4, 1.00, 0.0), //2
                new Vertex(0.6, 1.00, 0.0), //3
                new Vertex(0.8, 1.00, 0.0), //4
                new Vertex(1.0, 1.00, 0.0), //5 
                new Vertex(0.3, 0.50, 0.0), //6
                new Vertex(0.7, 0.50, 0.0), //7 
                new Vertex(0.2, 0.00, 0.0), //8
                new Vertex(0.4, 0.00, 0.0), //9
                new Vertex(0.6, 0.00, 0.0), //10
                new Vertex(0.8, 0.00, 0.0), //11
                new Vertex(0.5, 0.50, 0.0)); //12 



      // Create the back face vertices.
      addVertex(new Vertex(0.0, 1.00, -0.25),  // 13
      new Vertex(0.2, 1.00, -0.25),  // 14
      new Vertex(0.4, 1.00, -0.25), //15
      new Vertex(0.6, 1.00, -0.25), //16
      new Vertex(0.8, 1.00, -0.25), //17
      new Vertex(1.0, 1.00, -0.25), //18
      new Vertex(0.3, 0.50, -0.25), //19
      new Vertex(0.7, 0.50, -0.25), //20
      new Vertex(0.2, 0.00, -0.25), //21
      new Vertex(0.4, 0.00, -0.25), //22
      new Vertex(0.6, 0.00, -0.25), //23
      new Vertex(0.8, 0.00, -0.25), //24
      new Vertex(0.5, 0.50, -0.25)); //25



      // Create the Color objects.



      // Create the front face line segments.
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(0, 8),
                   new LineSegment(8, 9),
                   new LineSegment(9, 12),
                   new LineSegment(12, 10),
                   new LineSegment(10, 11),
                   new LineSegment(11, 5),
                   new LineSegment(5, 4),
                   new LineSegment(4, 7),
                   new LineSegment(7, 3),
                   new LineSegment(3, 2),
                   new LineSegment(2, 6),
                   new LineSegment(1, 6)
      );



      // Create the back face line segments.
      addPrimitive(new LineSegment(13, 14),
                   new LineSegment(13, 21),
                   new LineSegment(21, 22),
                   new LineSegment(22, 25),
                   new LineSegment(25, 23),
                   new LineSegment(23, 24),
                   new LineSegment(24, 18),
                   new LineSegment(18, 17),
                   new LineSegment(17, 20),
                   new LineSegment(20, 16),
                   new LineSegment(16, 15),
                   new LineSegment(15, 19),
                   new LineSegment(14, 19)
      );




      // Connect front face to back face.
      addPrimitive(new LineSegment(0, 13),
                   new LineSegment(8, 21),
                   new LineSegment(9, 22),
                     new LineSegment(12, 25),
                     new LineSegment(10, 23),
                     new LineSegment(11, 24),
                     new LineSegment(5, 18),
                     new LineSegment(4, 17),
                     new LineSegment(7, 20),
                     new LineSegment(3, 16),
                     new LineSegment(2, 15),
                     new LineSegment(1, 14),
                     new LineSegment(6, 19));



   }
}
