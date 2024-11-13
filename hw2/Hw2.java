/*
   Course: CS45500
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 2
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
      final FrameBuffer fb = new FrameBuffer(width, height, Color.black);

      Scene scene = new Scene();

      // Create the Models and give them an initial location.
      //P model
      Model p = new P();
      Position pPos = new Position(p, new Vector(-1.5, 0.5, -1.5));//top left corner
      scene.addPosition(pPos);

      //N model
      Model n = new N();
      Position nPos = new Position(n, new Vector(-0.5, -0.5, -1.5)); //middle
      scene.addPosition(nPos);

      //W model
      Model w = new W();
      Position wPos = new Position(w, new Vector(0.5, -1.5, -1.5)); //bottom right corner
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

      // Get the initial location of each Model.
      Vector modelPosP = pPos.getTranslation();
      Vector modelPosN = nPos.getTranslation();
      Vector modelPosW = wPos.getTranslation();

      // Create 200 frames of the animation.
      for (int i = 0; i < 200; ++i)
      {
         // Render again.
         fb.clearFB();
         Pipeline.render(scene, fb);
         fb.dumpFB2File(String.format("Hw2_frame%03d.ppm", i));

         // Update each Model's location.
         if(i<50){
            //Update the P model's location to move 2 units right
            modelPosP = pPos.getTranslation();
            pPos.translate(modelPosP.x + 0.04, modelPosP.y, modelPosP.z);

            // Update the N model's location
            modelPosN = nPos.getTranslation();
            nPos.translate(modelPosN.x, modelPosN.y, modelPosN.z - 0.02);

            // Update the W model's location
            modelPosW = wPos.getTranslation();
            wPos.translate(modelPosW.x - 0.04, modelPosW.y, modelPosW.z);
         }
         else if(i<100){
            //Update the P model's location to move 2 units down
            modelPosP = pPos.getTranslation();
            pPos.translate(modelPosP.x, modelPosP.y - 0.04, modelPosP.z);

            // Update the N model's location
            modelPosN = nPos.getTranslation();
            nPos.translate(modelPosN.x, modelPosN.y, modelPosN.z - 0.02);

            // Update the W model's location
            modelPosW = wPos.getTranslation();
            wPos.translate(modelPosW.x, modelPosW.y + 0.04, modelPosW.z);
         }
         else if(i<150){
            //Update the P model's location to move 2 units left
            modelPosP = pPos.getTranslation();
            pPos.translate(modelPosP.x - 0.04, modelPosP.y, modelPosP.z);

            // Update the N model's location
            modelPosN = nPos.getTranslation();
            nPos.translate(modelPosN.x, modelPosN.y, modelPosN.z + 0.02);

            // Update the W model's location
            modelPosW = wPos.getTranslation();
            wPos.translate(modelPosW.x + 0.04, modelPosW.y, modelPosW.z);
         }
         else{
            //Update the P model's location to move 2 units up
            modelPosP = pPos.getTranslation();
            pPos.translate(modelPosP.x, modelPosP.y + 0.04, modelPosP.z);

            // Update the N model's location
            modelPosN = nPos.getTranslation();
            nPos.translate(modelPosN.x, modelPosN.y, modelPosN.z + 0.02);

            // Update the W model's location
            modelPosW = wPos.getTranslation();
            wPos.translate(modelPosW.x, modelPosW.y - 0.04, modelPosW.z);
         }
      }
   }
}
