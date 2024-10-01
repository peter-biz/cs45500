/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.framebuffer.*;
import renderer.pipeline.*;

import java.awt.Color;

/**
   Compile and run this program. Look at its output, both
   in the console window and in the PPM file that it creates.
   <p>
   This version draws anti-aliased lines using color. To see the
   anti-aliased pixels, it helps to use a pixel magnifying utility.
   You can magnify the output image using IrfanView, but a pixel
   magnifying utility will work better.
   <p>
   Notice that the console logging shows all the details of
   how each pixel in each line is anti-aliased.
*/
public class LinesInTwoViewports_R4
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("LinesInTwoViewports_R4");
      final Model model = new Model("Lines");
      scene.addPosition(new Position(model, "p0"));

      model.addVertex(new Vertex( 5, 4,  -6),
                      new Vertex(-1, .5, -2),
                      new Vertex( 5, 4,  -2)); // try (0, 4, -3)

      model.addColor(Color.blue,
                     Color.green,
                     Color.red);

      model.addPrimitive(new LineSegment(0, 1, 0, 1),
                         new LineSegment(1, 2, 1, 2));

      final int widthFB  = 300;
      final int heightFB = 200;
      final FrameBuffer fb = new FrameBuffer(widthFB, heightFB, Color.darkGray);

      final int widthVP  = 100;
      final int heightVP = 100;
      FrameBuffer.Viewport vp1 = fb.new Viewport( 50, 50, widthVP, heightVP, Color.gray);
      FrameBuffer.Viewport vp2 = fb.new Viewport(150, 50, widthVP, heightVP, Color.black);
      vp1.clearVP();
      vp2.clearVP();

      scene.debug = true;
      Rasterize.debug = true;

      Rasterize.doAntiAliasing = true;
      Rasterize.doGamma = true;
      Rasterize.doClipping = true;

      Pipeline.render(scene, vp1);

      Rasterize.doClipping = false;
      Pipeline.render(scene, vp2);

      fb.dumpFB2File("LinesInTwoViewports_R4.ppm");
   }
}
