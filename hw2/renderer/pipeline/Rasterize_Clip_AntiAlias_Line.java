/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.pipeline;

import renderer.scene.*;
import renderer.scene.primitives.LineSegment;
import renderer.framebuffer.*;
import static renderer.pipeline.PipelineLogger.*;

import java.awt.Color;

/**
   Rasterize a projected {@link LineSegment} into shaded pixels
   in a {@link FrameBuffer.Viewport} and (optionally) anti-alias
   and gamma-encode the line at the same time. Also, (optionally)
   do not rasterize any part of the {@link LineSegment} that is
   not contained in the {@link Camera}'s view rectangle.
<p>
   This pipeline stage takes a {@link LineSegment} whose vertices
   have been projected into the {@link Camera}'s view plane
   coordinate system and rasterizes the {@link LineSegment}
   into shaded, anti-aliased pixels in a {@link FrameBuffer.Viewport}.
<p>
   In addition, this rasterizer has the option to "clip" the
   {@link LineSegment} by not rasterizing into the
   {@link FrameBuffer.Viewport} any part of the projected
   {@link LineSegment} that is not within the {@link Camera}'s
   view rectangle.
<p>
   This rasterization algorithm is based on
<pre>
     "Fundamentals of Computer Graphics", 3rd Edition,
      by Peter Shirley, pages 163-165.
</pre>
<p>
   This rasterizer implements a simple version of Xiaolin_Wu's
   anti-aliasing algorithm. See
     <a href="https://en.wikipedia.org/wiki/Xiaolin_Wu's_line_algorithm" target="_top">
              https://en.wikipedia.org/wiki/Xiaolin_Wu's_line_algorithm</a>
<p>
   Recall that a {@link FrameBuffer.Viewport} is a two-dimensional array
   of pixel data. So a viewport has an integer "coordinate system". That
   is, we locate a pixel in a viewport using two integers, which we think
   of as row and column. On the other hand, a {@link Camera}'s view rectangle
   has a two-dimensional real number (not integer) coordinate system. Since
   a framebuffer's viewport and a camera's view rectangle have such different
   coordinate systems, rasterizing line segments from a two-dimensional real
   number coordinate system to an two-dimensional integer grid can be tricky.
   The "logical pixel-plane" and the "viewport transformation" try to make
   this rasterization step a bit easier.
<pre>{@code
                                 (0,0)
                                   +-------------------------------------------+
        y-axis                     |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
          |                        |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
          |  (+1,+1)               |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
    +-----|-----+                  |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
    |     |     |                  |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
    |     |     |                  |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
----------+----------- x-axis      |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
    |     |     |                  |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
    |     |     |                  |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
    +-----|-----+                  |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
 (-1,-1)  |                        |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
          |                        |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
                                   |-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
 Camera's View Rectangle           +-------------------------------------------+
  (in the View Plane)                                                      (w-1,h-1)
                                              FrameBuffer's Viewport
}</pre>
<p>
   The viewport transformation places the logical pixel-plane between the
   camera's view rectangle and the framebuffer's viewport. The pixel-plane
   has a real number coordinate system (like the camera's view plane) but
   is has dimensions more like the dimensions of the framebuffer's viewport.
<pre>{@code
                                                    (w+0.5, h+0.5)
           +----------------------------------------------+
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |  The "logical pixels"
           | . . . . . . . . . . . . . . . . . . . . . .  |  are the points in
           | . . . . . . . . . . . . . . . . . . . . . .  |  the pixel-plane with
           | . . . . . . . . . . . . . . . . . . . . . .  |  integer coordinates.
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           | . . . . . . . . . . . . . . . . . . . . . .  |
           +----------------------------------------------+
      (0.5, 0.5)
                 pixel-plane's "logical viewport"
                   containing "logical pixels"
   }</pre>
<p>
   Notice that we have two uses of the word "viewport",
   <ul>
   <li>The "logical viewport" is a rectangle in the pixel-plane (so
       its points have real number coordinates). The "logical pixels"
       are the points in the logical viewport with integer coordinates.
   <li>The "physical viewport" is part of the {@link FrameBuffer}'s pixel
       array (so its entries have integer coordinates). The "physical
       pixels" are the entries in the physical viewport.
   </ul>
*/
public final class Rasterize_Clip_AntiAlias_Line
{
   /**
      Rasterize and (possibly) clip a projected {@link LineSegment} into pixels
      in the {@link FrameBuffer.Viewport}.

      @param model  {@link Model} that the {@link LineSegment} {@code ls} comes from
      @param ls     {@link LineSegment} to rasterize into the {@link FrameBuffer.Viewport}
      @param vp     {@link FrameBuffer.Viewport} to hold rasterized pixels
   */
   public static void rasterize(final Model model,
                                final LineSegment ls,
                                final FrameBuffer.Viewport vp)
   {
      final String     CLIPPED = "Clip: ";
      final String NOT_CLIPPED = "      ";

      // Get the viewport's background color.
      final Color bg = vp.bgColorVP;

      // Make local copies of several values.
      final int w = vp.getWidthVP();
      final int h = vp.getHeightVP();

      final int vIndex0 = ls.vIndexList.get(0);
      final int vIndex1 = ls.vIndexList.get(1);
      final Vertex v0 = model.vertexList.get(vIndex0);
      final Vertex v1 = model.vertexList.get(vIndex1);

      final int cIndex0 = ls.cIndexList.get(0);
      final int cIndex1 = ls.cIndexList.get(1);
      final float[] c0 = model.colorList.get(cIndex0).getRGBComponents(null);
      final float[] c1 = model.colorList.get(cIndex1).getRGBComponents(null);
      float r0 = c0[0], g0 = c0[1], b0 = c0[2];
      float r1 = c1[0], g1 = c1[1], b1 = c1[2];

      // Round each point's coordinates to the nearest logical pixel.
      double x0 = Math.round(v0.x);
      double y0 = Math.round(v0.y);
      double x1 = Math.round(v1.x);
      double y1 = Math.round(v1.y);

      if (Rasterize.debug)
      {
         logMessage(String.format("(x0_pp, y0_pp) = (%9.4f, %9.4f)", x0,y0));
         logMessage(String.format("(x1_pp, y1_pp) = (%9.4f, %9.4f)", x1,y1));
      }

      // Round each vertex to the nearest logical pixel. This
      // makes the algorithm a lot simpler, but it can cause
      // a slight, but noticeable, shift of the line segment.
      x0 = Math.round(x0);
      y0 = Math.round(y0);
      x1 = Math.round(x1);
      y1 = Math.round(y1);

      // Rasterize a degenerate line segment (a line segment
      // that projected onto a single point) as a single pixel.
      if ( (x0 == x1) && (y0 == y1) )
      {
         // We don't know which endpoint of the line segment
         // is in front, so just pick v0.
         final int x0_vp = (int)x0 - 1;  // viewport coordinate
         final int y0_vp = h - (int)y0;  // viewport coordinate

         if (Rasterize.debug)
         {
            final String clippedMessage;
            if ( ! Rasterize.doClipping
              || (x0_vp >= 0 && x0_vp < w && y0_vp >= 0 && y0_vp < h) ) // clipping test
            {
               clippedMessage = NOT_CLIPPED;
            }
            else
            {
               clippedMessage = CLIPPED;
            }
            logPixel(clippedMessage, x0, y0, x0_vp, y0_vp, r0, g0, b0, vp);
         }
         // Log the pixel before setting it so that an array out-
         // of-bounds error will be right after the pixel's address.

         if ( ! Rasterize.doClipping
           || (x0_vp >= 0 && x0_vp < w && y0_vp >= 0 && y0_vp < h) ) // clipping test
         {
            vp.setPixelVP(x0_vp, y0_vp, new Color(r0, g0, b0));
         }
         return;
      }

      // If abs(slope) <= 1, then rasterize this line in
      // the direction of the x-axis. Otherwise, rasterize
      // this line segment in the direction of the y-axis.
      if (Math.abs(y1 - y0) <= Math.abs(x1 - x0)) // if abs(slope) <= 1
      {
         if (x1 < x0) // We want to rasterize along the x-axis from left-to-right,
         {            // so, if necessary, swap (x0, y0) with (x1, y1).
            final double tempX = x0;
            final double tempY = y0;
            x0 = x1;
            y0 = y1;
            x1 = tempX;
            y1 = tempY;
            // Swap the colors too.
            final float tempR = r0;
            final float tempG = g0;
            final float tempB = b0;
            r0 = r1;
            g0 = g1;
            b0 = b1;
            r1 = tempR;
            g1 = tempG;
            b1 = tempB;
         }

         // Compute this line segment's slopes.
         final double      m = (y1 - y0) / (x1 - x0);
         final double slopeR = (r1 - r0) / (x1 - x0);
         final double slopeG = (g1 - g0) / (x1 - x0);
         final double slopeB = (b1 - b0) / (x1 - x0);

         if (Rasterize.debug)
         {
            logMessage("Slope m    = " + m);
            logMessage("Slope mRed = " + slopeR);
            logMessage("Slope mGrn = " + slopeG);
            logMessage("Slope mBlu = " + slopeB);
            logMessage(String.format("(x0_vp, y0_vp) = (%9.4f, %9.4f)", x0-1,h-y0));
            logMessage(String.format("(x1_vp, y1_vp) = (%9.4f, %9.4f)", x1-1,h-y1));
         }

         // Rasterize this line segment, along the x-axis, from left-to-right.
         // In the following loop, as x moves across the logical
         // horizontal pixels, we compute a y value for each x.
         double y = y0;
         for (int x = (int)x0; x <= (int)x1; x += 1, y += m)
         {
            // Interpolate this pixel's color between the two endpoint's colors.
            float r = (float)Math.abs(r0 + slopeR*(x - x0));
            float g = (float)Math.abs(g0 + slopeG*(x - x0));
            float b = (float)Math.abs(b0 + slopeB*(x - x0));
            // We need the Math.abs() because otherwise, we sometimes get -0.0.

            if (Rasterize.doAntiAliasing)
            {
               // y must be between two vertical logical-pixel coordinates.
               // Let y_low and y_hi be the logical-pixel coordinates
               // that bracket around y.
               int y_low = (int)Math.floor(y);
               int y_hi = y_low + 1;

               // Let weight be the distance from y to its floor (when
               // y is positive, this is the fractional part of y). We
               // will use weight to determine how much emphasis to place
               // on each of the two logical-pixels that bracket y.
               final float weight = (float)(y - y_low);

               // Interpolate colors for the low and high pixels.
               // The smaller weight is, the closer y is to the lower
               // pixel, so we give the lower pixel more emphasis when
               // weight is small.
               float r_low = (1 - weight) * r + weight * (bg.getRed()  /255.0f);
               float g_low = (1 - weight) * g + weight * (bg.getGreen()/255.0f);
               float b_low = (1 - weight) * b + weight * (bg.getBlue() /255.0f);
               float r_hi  = weight * r + (1 - weight) * (bg.getRed()  /255.0f);
               float g_hi  = weight * g + (1 - weight) * (bg.getGreen()/255.0f);
               float b_hi  = weight * b + (1 - weight) * (bg.getBlue() /255.0f);
/*
               // You can try replacing the above anti-aliasing code with this
               // code to see that this simple idea doesn't work here (as it
               // did in the previous renderer). This code just distributes the
               // line's color between two adjacent pixels (instead of blending
               // each pixel's color with the back ground color). This code ends
               // up having pixels fade to black, instead of fading to the back
               // ground color.
               float r_low = (1 - weight) * r;
               float g_low = (1 - weight) * g;
               float b_low = (1 - weight) * b;
               float r_hi  = weight * r;
               float g_hi  = weight * g;
               float b_hi  = weight * b;
*/
               if (Rasterize.doGamma)
               {
                  // Apply gamma-encoding (gamma-compression) to the two colors.
                  // https://www.scratchapixel.com/lessons/digital-imaging/digital-images
                  // http://blog.johnnovak.net/2016/09/21/what-every-coder-should-know-about-gamma/
                  final double gammaInv = 1.0 / Rasterize.GAMMA;
                  r_low = (float)Math.pow(r_low, gammaInv);
                  g_low = (float)Math.pow(g_low, gammaInv);
                  b_low = (float)Math.pow(b_low, gammaInv);
                  r_hi  = (float)Math.pow(r_hi,  gammaInv);
                  g_hi  = (float)Math.pow(g_hi,  gammaInv);
                  b_hi  = (float)Math.pow(b_hi,  gammaInv);
               }

               final int x_vp     = x - 1;      // viewport coordinate
               final int y_vp_low = h - y_low;  // viewport coordinate
               final int y_vp_hi  = h - y_hi;   // viewport coordinate

               if (Rasterize.debug)
               {
                  final String clippedMessage;
                  if ( ! Rasterize.doClipping
                    || (x_vp >= 0 && x_vp < w && y_vp_hi >= 0 && y_vp_low < h) ) // clipping test
                  {
                     clippedMessage = NOT_CLIPPED;
                  }
                  else // at least one of the two pixels is clipped off
                  {
                     clippedMessage = CLIPPED;
                  }
                  logPixelsAA(clippedMessage,
                              x, y,
                              x_vp, y_vp_low, y_vp_hi,
                              r_low, g_low, b_low,
                              r_hi,  g_hi,  b_hi,
                              vp);
               }
               // Log the pixel before setting it so that an array out-
               // of-bounds error will be right after the pixel's address.

               // Test each pixel, both the hi and the low one, for clipping.
               if ( ! Rasterize.doClipping
                 || (x_vp >= 0 && x_vp < w && y_vp_low >= 0 && y_vp_low < h) ) // clipping test
               {
                  vp.setPixelVP(x_vp, y_vp_low, new Color(r_low, g_low, b_low));
               }
               if ( ! Rasterize.doClipping
                 || (x_vp >= 0 && x_vp < w && y_vp_hi >= 0 && y_vp_hi < h) ) // clipping test
               {
                  vp.setPixelVP(x_vp, y_vp_hi, new Color(r_hi,  g_hi,  b_hi));
               }
            }
            else // No anti-aliasing.
            {
               if (Rasterize.doGamma)
               {
                  // Apply gamma-encoding (gamma-compression) to the colors.
                  // https://www.scratchapixel.com/lessons/digital-imaging/digital-images
                  // http://blog.johnnovak.net/2016/09/21/what-every-coder-should-know-about-gamma/
                  final double gammaInv = 1.0 / Rasterize.GAMMA;
                  r = (float)Math.pow(r, gammaInv);
                  g = (float)Math.pow(g, gammaInv);
                  b = (float)Math.pow(b, gammaInv);
               }

               // The value of y will almost always be between
               // two vertical logical-pixel coordinates. By rounding
               // off the value of y, we are choosing the nearest
               // logical vertical pixel coordinate.
               final int x_vp = x - 1;                  // viewport coordinate
               final int y_vp = h - (int)Math.round(y); // viewport coordinate

               if (Rasterize.debug)
               {
                  final String clippedMessage;
                  if ( ! Rasterize.doClipping
                    || (x_vp >= 0 && x_vp < w && y_vp >= 0 && y_vp < h) ) // clipping test
                  {
                     clippedMessage = NOT_CLIPPED;
                  }
                  else
                  {
                     clippedMessage = CLIPPED;
                  }
                  logPixel(clippedMessage, x, y, x_vp, y_vp, r, g, b, vp);
               }
               // Log the pixel before setting it so that an array out-
               // of-bounds error will be right after the pixel's address.

               if ( ! Rasterize.doClipping
                 || (x_vp >= 0 && x_vp < w && y_vp >= 0 && y_vp < h) ) // clipping test
               {
                  vp.setPixelVP(x_vp, y_vp, new Color(r, g, b));
               }
            }
         }// Advance (x,y) to the next pixel. Since delta_x = 1, we need delta_y = m.
      }
      else // abs(slope) > 1, so rasterize along the y-axis.
      {
         if (y1 < y0) // We want to rasterize along the y-axis from bottom-to-top,
         {            // so, if necessary, swap (x0, y0) with (x1, y1).
            final double tempX = x0;
            final double tempY = y0;
            x0 = x1;
            y0 = y1;
            x1 = tempX;
            y1 = tempY;
            // Swap the colors too.
            final float tempR = r0;
            final float tempG = g0;
            final float tempB = b0;
            r0 = r1;
            g0 = g1;
            b0 = b1;
            r1 = tempR;
            g1 = tempG;
            b1 = tempB;
         }

         // Compute this line segment's slopes.
         final double      m = (x1 - x0) / (y1 - y0);
         final double slopeR = (r1 - r0) / (y1 - y0);
         final double slopeG = (g1 - g0) / (y1 - y0);
         final double slopeB = (b1 - b0) / (y1 - y0);

         if (Rasterize.debug)
         {
            logMessage("Slope m    = " + m + " (so 1/m = " + 1/m + ")");
            logMessage("Slope mRed = " + slopeR);
            logMessage("Slope mGrn = " + slopeG);
            logMessage("Slope mBlu = " + slopeB);
            logMessage(String.format("(x0_vp, y0_vp) = (%9.4f, %9.4f)", x0-1,h-y0));
            logMessage(String.format("(x1_vp, y1_vp) = (%9.4f, %9.4f)", x1-1,h-y1));
         }

         // Rasterize this line segment, along the y-axis, from bottom-to-top.
         // In the following loop, as y moves across the logical
         // vertical pixels, we compute a x value for each y.
         double x = x0;
         for (int y = (int)y0; y <= (int)y1; x += m, y += 1)
         {
            // Interpolate this pixel's color between the two endpoint's colors.
            float r = (float)Math.abs(r0 + slopeR*(y - y0));
            float g = (float)Math.abs(g0 + slopeG*(y - y0));
            float b = (float)Math.abs(b0 + slopeB*(y - y0));
            // We need the Math.abs() because otherwise, we sometimes get -0.0.

            if (Rasterize.doAntiAliasing)
            {
               // x must be between two horizontal logical-pixel coordinates.
               // Let x_left and x_right be the logical-pixel coordinates
               // that bracket around x.
               int x_left = (int)Math.floor(x);
               int x_right = x_left + 1;

               // Let weight be the distance from x to its floor (when
               // x is positive, this is the fractional part of x). We
               // will use weight to determine how much emphasis to place
               // on each of the two logical-pixels that bracket x.
               final float weight = (float)(x - x_left);

               // Interpolate colors for the left and right pixels.
               // The smaller weight is, the closer y is to the left
               // pixel, so we give the left pixel more emphasis when
               // weight is small.
               float r_left  = (1 - weight) * r + weight * (bg.getRed()  /255.0f);
               float g_left  = (1 - weight) * g + weight * (bg.getGreen()/255.0f);
               float b_left  = (1 - weight) * b + weight * (bg.getBlue() /255.0f);
               float r_right = weight * r + (1 - weight) * (bg.getRed()  /255.0f);
               float g_right = weight * g + (1 - weight) * (bg.getGreen()/255.0f);
               float b_right = weight * b + (1 - weight) * (bg.getBlue() /255.0f);
/*
               // You can try replacing the above anti-aliasing code with this
               // code to see that this simple idea doesn't work here (as it
               // did in the previous renderer). This code just distributes the
               // line's color between two adjacent pixels (instead of blending
               // each pixel's color with the back ground color). This code ends
               // up having pixels fade to black, instead of fading to the back
               // ground color.
               float r_left  = (1 - weight) * r;
               float g_left  = (1 - weight) * g;
               float b_left  = (1 - weight) * b;
               float r_right = weight * r;
               float g_right = weight * g;
               float b_right = weight * b;
*/
               if (Rasterize.doGamma)
               {
                  // Apply gamma-encoding (gamma-compression) to the two colors.
                  // https://www.scratchapixel.com/lessons/digital-imaging/digital-images
                  // http://blog.johnnovak.net/2016/09/21/what-every-coder-should-know-about-gamma/
                  final double gammaInv = 1.0 / Rasterize.GAMMA;
                  r_left  = (float)Math.pow(r_left,  gammaInv);
                  g_left  = (float)Math.pow(g_left,  gammaInv);
                  b_left  = (float)Math.pow(b_left,  gammaInv);
                  r_right = (float)Math.pow(r_right, gammaInv);
                  g_right = (float)Math.pow(g_right, gammaInv);
                  b_right = (float)Math.pow(b_right, gammaInv);
               }

               final int x_vp_left  = x_left  - 1;  // viewport coordinate
               final int x_vp_right = x_right - 1;  // viewport coordinate
               final int y_vp       = h - y;        // viewport coordinate

               if (Rasterize.debug)
               {
                  final String clippedMessage;
                  if ( ! Rasterize.doClipping
                    || (x_vp_left >= 0 && x_vp_right < w && y_vp >= 0 && y_vp < h) ) // clipping test
                  {
                     clippedMessage = NOT_CLIPPED;
                  }
                  else // at least one of the two pixels is clipped off
                  {
                     clippedMessage = CLIPPED;
                  }
                  logPixelsAA(clippedMessage,
                              x, y,
                              x_vp_left, x_vp_right, y_vp,
                              r_left,  g_left,  b_left,
                              r_right, g_right, b_right,
                              vp);
               }
               // Log the pixel before setting it so that an array out-
               // of-bounds error will be right after the pixel's address.

               // Test each pixel, both the left and the right one, for clipping.
               if ( ! Rasterize.doClipping
                 || (x_vp_left >= 0 && x_vp_left < w && y_vp >= 0 && y_vp < h) ) // clipping test
               {
                  vp.setPixelVP(x_vp_left,  y_vp, new Color(r_left,  g_left,  b_left));
               }
               if ( ! Rasterize.doClipping
                 || (x_vp_right >= 0 && x_vp_right < w && y_vp >= 0 && y_vp < h) ) // clipping test
               {
                  vp.setPixelVP(x_vp_right, y_vp, new Color(r_right, g_right, b_right));
               }
            }
            else // No anti-aliasing.
            {
               if (Rasterize.doGamma)
               {
                  // Apply gamma-encoding (gamma-compression) to the colors.
                  // https://www.scratchapixel.com/lessons/digital-imaging/digital-images
                  // http://blog.johnnovak.net/2016/09/21/what-every-coder-should-know-about-gamma/
                  final double gammaInv = 1.0 / Rasterize.GAMMA;
                  r = (float)Math.pow(r, gammaInv);
                  g = (float)Math.pow(g, gammaInv);
                  b = (float)Math.pow(b, gammaInv);
               }

               // The value of x will almost always be between
               // two horizontal logical-pixel coordinates. By rounding
               // off the value of x, we are choosing the nearest
               // logical horizontal pixel coordinate.
               final int x_vp = (int)Math.round(x) - 1; // viewport coordinate
               final int y_vp = h - y;                  // viewport coordinate

               if (Rasterize.debug)
               {
                  final String clippedMessage;
                  if ( ! Rasterize.doClipping
                    || (x_vp >= 0 && x_vp < w && y_vp >= 0 && y_vp < h) ) // clipping test
                  {
                     clippedMessage = NOT_CLIPPED;
                  }
                  else
                  {
                     clippedMessage = CLIPPED;
                  }
                  logPixel(clippedMessage, x, y, x_vp, y_vp, r, g, b, vp);
               }
               // Log the pixel before setting it so that an array out-
               // of-bounds error will be right after the pixel's address.

               if ( ! Rasterize.doClipping
                 || (x_vp >= 0 && x_vp < w && y_vp >= 0 && y_vp < h) ) // clipping test
               {
                  vp.setPixelVP(x_vp, y_vp, new Color(r, g, b));
               }
            }
         }// Advance (x,y) to the next pixel. Since delta_y = 1, we need delta_x = m.
      }
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Rasterize_Clip_AntiAlias_Line() {
      throw new AssertionError();
   }
}
