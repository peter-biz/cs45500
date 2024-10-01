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
public class Hw2
{
   public static void main(String[] args)
   {
      // Create a FrameBuffer to render our scene into.
      final int width  = 900;
      final int height = 900;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.darkGray);

      final Scene scene = new Scene();

      // Create the Models and give them an initial location.




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




      }
   }
}
