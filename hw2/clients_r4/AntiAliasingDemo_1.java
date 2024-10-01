/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.pipeline.*;
import renderer.framebuffer.*;

import java.awt.Color;

/**
   This program renders a fan of lines three ways, one way using
   the rasterizer without anti-aliasing, a second way using the
   raterizer with a simple anti-aliasing algorithm, and a third
   way that combines anti-aliasing with gamma correction.

   The renderings are displayed side-by-side in two viewports within
   the framebuffer. The first image has aliased lines next to anti-
   aliased lines without gamma correction. The second image has
   aliased lines next to anti-aliased lines with gamma correction.

   To see how the anti-aliasing works, look at the final rendered
   image using a pixel magnifying program.
*/
public class AntiAliasingDemo_1
{
   private static final int size = 900;
   private static final int numberOfLines = 30;

   private static final Color backgroundColor  = Color.green;
   private static final Color foregroundColor1 = Color.red;
   private static final Color foregroundColor2 = Color.red; // try making this different

   public static void main(String[] args)
   {
      // Create the Scene object that we shall render
      final Scene scene = new Scene();

      // Create a Model object.
      final Model model = new Model("line-fan");

      // Add the model to the Scene.
      scene.addPosition(new Position(model));

      // Create a fan of lines, coming out of (-1, -1, -1), in
      // the square with -1 <= x <= 1, -1 <= y <= 1, and z = -1.
      model.addVertex( new Vertex(-1, -1, -1) );
      model.addColor(foregroundColor1, foregroundColor2);

      for (int i = 0; i < numberOfLines; ++i)
      {
         model.addVertex(new Vertex(1, -1 + 2*(double)i/(double)numberOfLines, -1));
         model.addPrimitive(new LineSegment(0, 2*i+1, 0, 1));  // slope < 1
         model.addVertex(new Vertex(-1 + 2*(double)i/(double)numberOfLines, 1, -1));
         model.addPrimitive(new LineSegment(0, 2*i+2, 0, 1));  // slope > 1
      }
      model.addVertex(new Vertex(1, 1, -1));
      model.addPrimitive(new LineSegment(0, 2*numberOfLines+1, 0, 1)); // 45 degree line


      // Create a FrameBuffer that will hold two viewports.
      final int width  = 2*size;  // wide enough to hold two viewports
      final int height =   size;
      final FrameBuffer fb = new FrameBuffer(width, height, backgroundColor);

      // Set a viewport within the framebuffer.
      fb.setViewport(0, 0, size, size); //upper-left-hand-corner, width, height
      // Render our scene, without anti-aliasing, into the current viewport.
      Rasterize.doAntiAliasing = false;
      Pipeline.render(scene, fb.vp);

      // Set a viewport within the framebuffer.
      fb.setViewport(size, 0, size, size); //upper-left-hand-corner, width, height
      // Render our scene, with anti-aliasing and gamma correction, into the current viewport.
      Rasterize.doAntiAliasing = true;
      Rasterize.doGamma = true;
      Pipeline.render(scene, fb.vp);
      fb.dumpFB2File("AntiAliasingDemo_1_gamma.ppm");

      // Render our scene, again with anti-aliasing and without gamma correction.
      fb.vp.clearVP();
      Rasterize.doAntiAliasing = true;
      Rasterize.doGamma = false;
      Pipeline.render(scene, fb.vp);
      fb.dumpFB2File("AntiAliasingDemo_1_no_gamma.ppm");
   }
}
