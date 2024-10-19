/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.pipeline;

import renderer.scene.*;
import renderer.scene.primitives.Point;
import renderer.framebuffer.*;
import static renderer.pipeline.PipelineLogger.*;

import java.awt.Color;

/**
   Rasterize a projected {@link Point} into shaded pixels
   in a {@link FrameBuffer.Viewport}, but (optionally)
   do not rasterize any part of the {@link Point} that
   is not contained in the {@link Camera}'s view rectangle.
*/
public class Rasterize_Clip_Point
{
   /**
      Rasterize a {@link Point} into shaded pixels
      in a {@link FrameBuffer.Viewport}.

      @param model  {@link Model} that the {@link Point} {@code pt} comes from
      @param pt     {@code Point} to rasterize into the {@code FrameBuffer.Viewport}
      @param vp     {@link FrameBuffer.Viewport} to hold rasterized, shaded pixels
   */
   public static void rasterize(final Model model,
                                final Point pt,
                                final FrameBuffer.Viewport vp)
   {
      final String     CLIPPED = "Clip: ";
      final String NOT_CLIPPED = "      ";

      // Make local copies of several values.
      final int w = vp.getWidthVP();
      final int h = vp.getHeightVP();

      final int vIndex = pt.vIndexList.get(0);
      final Vertex v = model.vertexList.get(vIndex);

      final int cIndex = pt.cIndexList.get(0);
      final float[] c = model.colorList.get(cIndex).getRGBComponents(null);
      float r = c[0], g = c[1], b = c[2];

      if (Rasterize.doGamma)
      {
         // Apply gamma-encoding (gamma-compression) to the two colors.
         // https://www.scratchapixel.com/lessons/digital-imaging/digital-images
         // http://blog.johnnovak.net/2016/09/21/what-every-coder-should-know-about-gamma/
         final double gammaInv = 1.0 / Rasterize.GAMMA;
         r = (float)Math.pow(r, gammaInv);
         g = (float)Math.pow(g, gammaInv);
         b = (float)Math.pow(b, gammaInv);
      }

      // Transform the vertex to the pixel-plane coordinate system.
      double x = 0.5 + w/2.001 * (v.x + 1); // x_pp = 0.5 + w/2 * (x_p+1)
      double y = 0.5 + h/2.001 * (v.y + 1); // y_pp = 0.5 + h/2 * (y_p+1)
      // NOTE: Notice the 2.001 fudge factor in the last two equations.
      // This is explained on page 142 of
      //    "Jim Blinn's Corner: A Trip Down The Graphics Pipeline"
      //     by Jim Blinn, 1996, Morgan Kaufmann Publishers.

      if (Rasterize.debug)
      {
         logMessage(String.format("(x_pp, y_pp) = (%9.4f, %9.4f)", x, y));
      }

      // Round the point's coordinates to the nearest logical pixel.
      x = Math.round( x );
      y = Math.round( y );

      final int radius = pt.radius;

      for (int y_ = (int)y - radius; y_ <= (int)y + radius; ++y_)
      {
         for (int x_ = (int)x - radius; x_ <= (int)x + radius; ++x_)
         {
            if (Rasterize.debug)
            {
               final String clippedMessage;
               if ( ! Rasterize.doClipping
                 || (x_ > 0 && x_ <= w && y_ > 0 && y_ <= h) ) // clipping test
               {
                  clippedMessage = NOT_CLIPPED;
               }
               else
               {
                  clippedMessage = CLIPPED;
               }
               logPixel(clippedMessage, x, y, x_ - 1, h - y_, r, g, b, vp);
            }
            // Log the pixel before setting it so that an array out-
            // of-bounds error will be right after the pixel's address.

            if ( ! Rasterize.doClipping
              || (x_ > 0 && x_ <= w && y_ > 0 && y_ <= h) ) // clipping test
            {
               vp.setPixelVP(x_ - 1, h - y_, new Color(r, g, b));
            }
         }
      }
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Rasterize_Clip_Point() {
      throw new AssertionError();
   }
}
