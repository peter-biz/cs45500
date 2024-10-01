/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.DrawSceneGraph;
import renderer.framebuffer.*;
import renderer.pipeline.*;

import java.awt.Color;

/**
   Compile and run this program. Look at its output, both
   in the console window and in the PPM file that it creates.
   <p>
   The output in the console window shows you the debugging
   information from the renderer. It shows the results from
   each stage of the rendering pipeline. This program draws
   two lines, one with a slope less than 1 and the other
   with a slope greater than 1.
   <p>
   This program's framebuffer is small on purpose so that
   there isn't too much console output from the rasterizer.
*/
public class TwoSlopes_R4
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("Two_Slopes_R4");
      final Model model = new Model("Two_Slopes");
      scene.addPosition(new Position(model,
                                     "top",
                                     new Vector(0, 0, -1)));

      model.addVertex(new Vertex(-1, -1, 0),
                      new Vertex( 2,  0, 0),
                      new Vertex( 0,  2, 0));

      model.addColor(Color.red,
                     Color.green,
                     Color.blue);

      model.addPrimitive(new LineSegment(0, 1, 0, 1),
                         new LineSegment(0, 2, 0, 2));

      // Draw a picture of the scene's tree (DAG) data structure.
      DrawSceneGraph.drawVertexList = true;
      DrawSceneGraph.draw(scene, "TwoSlopes_R4_SG");


      final int width  = 100;
      final int height = 100;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.white);
      // The background color of the framebuffer is white, so the
      // background color of the default viewport is also white.

      Rasterize.doClipping = true;
      Rasterize.doAntiAliasing = false;
      Rasterize.doGamma = true;

      scene.debug = true;
      Rasterize.debug = true;
      Pipeline.render(scene, fb);
      fb.dumpFB2File("TwoSlopes_R4.ppm");
      System.err.println("Saved " + "TwoSlopes_R4.ppm");
   }
}
