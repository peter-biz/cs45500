/*


*/

import renderer.scene.*;
import renderer.scene.util.DrawSceneGraph;
import renderer.pipeline.*;
import renderer.framebuffer.*;


import java.awt.Color;

/**
   Create the frames for an animation of the letters P, N, and W.
*/

/**
 * In the zip file there are three java source files, P.java, N.java, and W.java. 
 * Each of these files defines a sub-class of the Model class (from the scene package). 
 * The file P.java is partly done for you. 
 * You need to complete it and the other two files so that each one defines a model 
 * that represents the letter of the alphabet the file is named after. 
 * You need to determine how many Vertex, Color, and LineSegment objects each model 
 * needs and then write the code that instantiates those objects and puts them into the model.
 *  Make each letter one unit tall (in the y-direction), one unit wide (in the x-direction), 
 * and a quarter unit deep (in the z-direction). Each letter should have its base line on the 
 * x-axis. The y-axis should touch the left side of each letter. The front face of each letter 
 * should be in the plane z = 0. The back face of each letter should be in the plane z = -0.25. 
 * Look at the supplied animation to see how each model's line segments should be colored.
 * 
 * 
 * 
 * After you have defined your letter models, and before you actually start to work on the animation,
 *  write code in Hw2.java that just creates the initial frame of the animation. 
 * The three letters should initially have their front faces in 
 * the plane z = -1.5. The plane z = -1.5 intersects
 *  the camera's view volume in a square that is 3 units wide and
 * 3 units tall (the view volume, in the z = -1.5 plane, extends
 *  from -1.5 to 1.5 along the x-axis, and from -1.5 to 1.5 along the y-axis.). 
 * This square is just wide enough to place the three letters next to each other. 
 * First place the three letters next to each other with the x-axis running through 
 * the middle of each letter. After you get this image, 
 * translate the P up so that it just touches the top of the view volume 
 * and translate the W down so that it just touches the bottom of the view volume. 
 * That should give you the correct initial positions for the three models.
 *  (See the first frame from the sub-folder hw2\animation-frames.)
 */
public class Hw2
{
   public static void main(String[] args)
   {
      // Create a FrameBuffer to render our scene into.
      final int width  = 900;
      final int height = 900;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.darkGray);

      Scene scene = new Scene();

      // Create the Models and give them an initial location.

      //P model
      Model p = new P();
      Position pPos = new Position(p, new Vector(-1.5, 0.5, -1.5));//top left corner
      scene.addPosition(pPos);

      //N model
      Model n = new N();
      Position nPos = new Position(n, new Vector(0, 0.5, -1.5)); //middle(not  lol)
      //scene.addPosition(nPos);

      //W model
      Model w = new W();
      Position wPos = new Position(w, new Vector(0, 0.5, -1.5)); //bottom right corner(not lot)
      scene.addPosition(wPos);

        

      // If you need to, print the Scene data structure to the console.
      //System.out.println( scene );

      // Use GraphViz to draw a picture of the Scene data structure.
      DrawSceneGraph.drawVectorDetails = true;
      DrawSceneGraph.drawVertexList = true;
      DrawSceneGraph.draw(scene, "Hw2_SG");

      Rasterize.doClipping = true;
      Rasterize.doGamma = false;
      //scene.debug = true;      // Uncomment this line for debugging output.
      //Rasterize.debug = true;  // Uncomment this line for more debugging output.

      // Create 200 frames of the animation.
      for (int i = 0; i < 200; ++i)
      {
         // Render again.
         fb.clearFB();
         Pipeline.render(scene, fb);
         fb.dumpFB2File(String.format("Hw2_frame%03d.ppm", i));

         // Update each Model's location.

         // Update the P model's location
         //If we look at the letter P in the animation, it moves 2 units right, then 2 units down, then 2 units left, and finally 2 units up. 
         //This brings the P back to where it started, so the animation can cycle through the frames to create a continuous loop.
         //(0.01, 0.01, 0.01)





      }
   }
}
