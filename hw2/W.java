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
      addVertex(new Vertex(0.000, 1.00, 0.0),  // 0
                new Vertex(0.250, 1.00, 0.0),  // 1
                new Vertex(0.500, 1.00, 0.0), //2
                new Vertex(0.750, 1.00, 0.0), //3
                new Vertex(1.000, 1.00, 0.0), //4
                new Vertex(0.000, 0.00, 0.0), //5 (ignore for now, i needa change thes scaling)
                new Vertex(0.325, 0.50, 0.0), //6
                new Vertex(0.875, 0.50, 0.0), //7 (0.65)
                new Vertex(0.250, 0.00, 0.0), //8
                new Vertex(0.500, 0.00, 0.0), //9
                new Vertex(0.750, 0.00, 0.0), //10
                new Vertex(1.000, 0.00, 0.0), //11
                new Vertex(0.625, 0.50, 0.0)); //12 (0.4875)



      // Create the back face vertices.



      // Create the Color objects.



      // Create the front face line segments.
      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(0, 8),
                   new LineSegment(1, 6),
                   new LineSegment(8, 9),
                   new LineSegment(6, 2),
                   new LineSegment(9, 12), 
                   new LineSegment(2, 3),
                   new LineSegment(3, 7),
                   new LineSegment(12, 10),
                   new LineSegment(10, 11),
                   new LineSegment(7, 4)
      );



      // Create the back face line segments.



      // Connect front face to back face.



   }
}
