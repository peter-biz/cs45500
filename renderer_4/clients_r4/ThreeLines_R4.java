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
   three lines, one with a solid color and the other two with
   their color interpolated from one end to the other end.
   <p>
   This program's framebuffer is small on purpose so that
   there isn't too much console output from the rasterizer.
*/
public class ThreeLines_R4
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("Three_Lines_R4");
      final Model model = new Model("Three_Lines");
      scene.addPosition(new Position(model,
                                     "top",
                                     new Vector(0, 0, -1)));

      model.addVertex(new Vertex( 0,  0, 0),
                      new Vertex( 1,  0, 0),
                      new Vertex( 0,  1, 0),
                      new Vertex(-1, -1, 0));

      model.addColor(Color.red,
                     Color.green,
                     Color.blue);

      model.addPrimitive(new LineSegment(0, 1, 0, 0),
                         new LineSegment(0, 2, 2, 1),
                         new LineSegment(0, 3, 1, 2));

      final int width  = 200;
      final int height = 200;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.white);

      Rasterize.doClipping = true;
      Rasterize.doGamma = true;

      scene.debug = true;
      Rasterize.debug = true;
      Pipeline.render(scene, fb);
      fb.dumpFB2File("ThreeLines_R4.ppm");

      // Draw a picture of the scene's tree (DAG) data structure.
      DrawSceneGraph.drawVertexList = true;
      DrawSceneGraph.draw(scene, "ThreeLines_R4_SG");
   }
}
