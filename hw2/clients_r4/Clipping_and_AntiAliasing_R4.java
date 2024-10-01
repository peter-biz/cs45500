/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.framebuffer.FrameBuffer;
import renderer.pipeline.*;

import java.awt.Color;

/**
   Compile and run this program. Look at its output, both
   in the console window and using a screen magnifier on
   the PPM file that it creates.
   <p>
   This program draws four lines that are nearly parallel
   to the four edges of the framebuffer. But each line
   crosses from being just one pixel inside the framebuffer
   to being just one pixel outside the fraembufer. If we
   render these lines with anti-aliasing, then each line
   will span an entire edge of the framebuffer, but each
   line will fade from white to black as it spans its entire
   edge. If we render these lines without anti-aliasing,
   then each line will render as a line across just half
   of its edge of the framebuffer.
   <p>
   Notice that the console logging shows all the details of
   how each pixel in the line is clipped and anti-aliased.
*/
public class Clipping_and_AntiAliasing_R4
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("Clipping_and_AntiAliasing_R4",
                                    Camera.projOrtho());

      final Model model_1 = new Model("Clipping_and_AntiAliasing_1");
      scene.addPosition(new Position(model_1, "p1"));

      model_1.addVertex(new Vertex(-0.99,  0.99, 0.0), // top edge
                        new Vertex( 0.99,  1.01, 0.0),

                        new Vertex( 0.99,  0.99, 0.0), // right edge
                        new Vertex( 1.01, -0.99, 0.0),

                        new Vertex( 0.99, -0.99, 0.0), // bottom edge
                        new Vertex(-0.99, -1.01, 0.0),

                        new Vertex(-0.99, -0.99, 0.0), // left edge
                        new Vertex(-1.01,  0.98, 0.0));

      model_1.addColor(Color.red,    // top edge
                       Color.red,
                       Color.blue,   // right edge
                       Color.blue,
                       Color.green,  // bottom edge
                       Color.green,
                       Color.blue,   // left edge
                       Color.green);

      model_1.addPrimitive(new LineSegment(0, 1, 0, 1) , // top edge
                           new LineSegment(2, 3, 2, 3),  // right edge
                           new LineSegment(4, 5, 4, 5),  // bottom edge
                           new LineSegment(6, 7, 6, 7)); // left edge

      final Model model_2 = new Model("Clipping_and_AntiAliasing_2");
      scene.addPosition(new Position(model_2, "p2"));

      model_2.addVertex(new Vertex(-0.99,  1.01, 0.0), // top edge
                        new Vertex( 0.99,  0.99, 0.0),

                        new Vertex( 1.01,  0.99, 0.0), // right edge
                        new Vertex( 0.99, -0.99, 0.0),

                        new Vertex( 0.99, -1.01, 0.0), // bottom edge
                        new Vertex(-0.99, -0.99, 0.0),

                        new Vertex(-1.01, -0.99, 0.0), // left edge
                        new Vertex(-0.99,  0.98, 0.0));

      model_2.addColor(Color.red,    // top edge
                       Color.red,
                       Color.blue,   // right edge
                       Color.blue,
                       Color.green,  // bottom edge
                       Color.green,
                       Color.blue,   // left edge
                       Color.green);

      model_2.addPrimitive(new LineSegment(0, 1, 0, 1),  // top edge
                           new LineSegment(2, 3, 2, 3),  // right edge
                           new LineSegment(4, 5, 4, 5),  // bottom edge
                           new LineSegment(6, 7, 6, 7)); // left edge


      // Keep the viewports small to minimize the amount of console output.
      final int width  = 220;
      final int height = 100;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.darkGray);
      FrameBuffer.Viewport vp1 = fb.new Viewport(  0, 0, 100, 100, Color.white);
      FrameBuffer.Viewport vp2 = fb.new Viewport(120, 0, 100, 100, Color.white);
      vp1.clearVP();
      vp2.clearVP();

      scene.debug = true;
      Rasterize.debug = true;
      Rasterize.doClipping = true;

      model_1.visible = true;
      model_2.visible = false;
      Rasterize.doAntiAliasing = true;
      Rasterize.doGamma = true;  // Try making this false.
      Pipeline.render(scene, vp1);
      Rasterize.doAntiAliasing = false;
      Pipeline.render(scene, vp2);
      fb.dumpFB2File("Clipping_and_AntiAliasing_v1_R4.ppm");

      vp1.clearVP();
      vp2.clearVP();
      model_1.visible = false;
      model_2.visible = true;
      Rasterize.doAntiAliasing = true;
      Rasterize.doGamma = true;  // Try making this false.
      Pipeline.render(scene, vp1);
      Rasterize.doAntiAliasing = false;
      Pipeline.render(scene, vp2);
      fb.dumpFB2File("Clipping_and_AntiAliasing_v2_R4.ppm");
   }
}
