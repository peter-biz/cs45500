/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.pipeline;

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.framebuffer.*;

import java.awt.Color;

/**
   Methods used by the pipeline stages to log information.
*/
public class PipelineLogger
{
   public static boolean debugScene = false;
   public static boolean debugPosition = false;

   /**
      Use this logger's debug variables to determine
      if the given message should be printed to stderr.

      @param message  {@link String} to output to stderr
   */
   public static void logMessage(final String message)
   {
      if (debugScene || debugPosition)
      {
         System.err.println( message );
      }
   }


   /**
      This method prints a {@link String} representation of the given
      {@link Model}'s {@link Vertex} list.

      @param stage  name for the pipeline stage
      @param model  the {@link Model} whose vertex list is to be printed
   */
   public static void logVertexList(final String stage, final Model model)
   {
      if (debugScene || debugPosition)
      {
         int i = 0;
         for (final Vertex v : model.vertexList)
         {
            System.err.printf("%s: vIndex = %3d, %s\n", stage, i, v.toString());
            ++i;
         }
      }
   }


   /**
      This method prints a {@link String} representation of the given
      {@link Model}'s {@link Color} list.

      @param stage  name for the pipeline stage
      @param model  the {@link Model} whose {@link Color} list is to be printed
   */
   public static void logColorList(final String stage, final Model model)
   {
      if (debugScene || debugPosition)
      {
         int i = 0;
         for (final Color c : model.colorList)
         {
            System.err.printf("%s: cIndex = %3d, %s\n", stage, i, c.toString());
            ++i;
         }
      }
   }


   /**
      This method prints a {@link String} representation of the given
      {@link Model}'s {@link Primitive} list.

      @param stage  name for the pipeline stage
      @param model  the {@link Model} whose primitive list is to be printed
   */
   public static void logPrimitiveList(final String stage, final Model model)
   {
      if (debugScene || debugPosition)
      {
         if ( model.primitiveList.isEmpty() )
         {
            System.err.printf("%s: []\n", stage);
         }
         else
         {
            for (final Primitive p : model.primitiveList)
            {
               System.err.printf("%s: %s\n", stage, p.toString());
            }
         }
      }
   }


   /**
      This method prints a {@link String} representation of the given
      {@link Primitive}.

      @param stage  name for the pipeline stage
      @param model  {@link Model} that the {@link Primitive} {@code ls} comes from
      @param p      {@link Primitive} whose string representation is to be printed
   */
   public static void logPrimitive(final String stage,
                                   final Model model,
                                   final Primitive p)
   {
      if (debugScene || debugPosition)
      {
         System.err.printf("%s: %s\n", stage, p.toString());
         for (final int vIndex : p.vIndexList)
         {
            final Vertex v = model.vertexList.get(vIndex);
            System.err.printf("   vIndex = %3d, %s\n", vIndex, v.toString());
         }
         for (final int cIndex : p.cIndexList)
         {
            final Color c = model.colorList.get(cIndex);
            System.err.printf("   cIndex = %3d, %s\n", cIndex, c.toString());
         }
      }
   }


   /**
      This method prints a {@link String} representation of the given pixel
      from a point that is being rasterized.

      @param clippedMessage  {@link String} specifying if the pixel was clipped or not
      @param x_pp   horizontal coordinate of the pixel in the pixel-plane
      @param y_pp   vertical coordinate of the pixel in the pixel-plane
      @param x_vp   horizontal coordinate of the pixel in the viewport
      @param y_vp   vertical coordinate of the pixel in the viewport
      @param r      red component of the pixel's {@link Color}
      @param g      green component of the pixel's {@link Color}
      @param b      blue component of the pixel's {@link Color}
      @param vp     {@link FrameBuffer.Viewport} that the pixel is being placed in
   */
   public static void logPixel(final String clippedMessage,
                               final double x_pp, final double y_pp,
                               final int    x_vp, final int    y_vp,
                               final double r, final double g, final double b,
                               final FrameBuffer.Viewport vp)
   {
      if (debugScene || debugPosition)
      {
         final int wVP = vp.getWidthVP();
         final int hVP = vp.getHeightVP();
         final int xVP = vp.vp_ul_x;
         final int yVP = vp.vp_ul_y;
         final FrameBuffer fb = vp.getFrameBuffer();
         final int wFB = fb.getWidthFB();
         final int hFB = fb.getHeightFB();
         System.err.print(clippedMessage);
         System.err.printf(
         "fb_[w=%d,h=%d] vp_[x=%4d, y=%4d, w=%d,h=%d]  (x_pp=%9.4f, y_pp=%9.4f)  (x_vp=%4d, y_vp=%4d)  r=%.4f g=%.4f b=%.4f\n",
              wFB, hFB,      xVP,   yVP,   wVP, hVP,    x_pp,       y_pp,         x_vp,     y_vp,      r,     g,     b);
      }
   }


   /**
      This method prints a {@link String} representation of the given pixel
      from a "horizontal" line that is being rasterized along the x-axis.

      @param clippedMessage  {@link String} specifying if the pixel was clipped or not
      @param x_pp   horizontal coordinate of the pixel in the pixel-plane
      @param y_pp   vertical coordinate of the pixel in the pixel-plane
      @param x_vp   horizontal coordinate of the pixel in the {@link FrameBuffer.Viewport}
      @param y_vp   vertical coordinate of the pixel in the {@link FrameBuffer.Viewport}
      @param r      red component of the pixel's {@link Color}
      @param g      green component of the pixel's {@link Color}
      @param b      blue component of the pixel's {@link Color}
      @param vp     {@link FrameBuffer.Viewport} that the pixel is being placed in
   */
   public static void logPixel(final String clippedMessage,
                               final int x_pp, final double y_pp,
                               final int x_vp, final int    y_vp,
                               final float r, final float g, final float b,
                               final FrameBuffer.Viewport vp)
   {
      if (debugScene || debugPosition)
      {
         final int wVP = vp.getWidthVP();
         final int hVP = vp.getHeightVP();
         final int xVP = vp.vp_ul_x;
         final int yVP = vp.vp_ul_y;
         final FrameBuffer fb = vp.getFrameBuffer();
         final int wFB = fb.getWidthFB();
         final int hFB = fb.getHeightFB();
         System.err.print(clippedMessage);
         System.err.printf(
            "fb_[w=%d,h=%d] vp_[x=%4d, y=%4d, w=%d,h=%d]  (x_pp=%4d, y_pp=%9.4f)  (x_vp=%4d, y_vp=%4d)  r=%.4f g=%.4f b=%.4f\n",
                 wFB, hFB,      xVP,   yVP,   wVP, hVP,    x_pp,     y_pp,         x_vp,     y_vp,      r,     g,     b);
      }
   }


   /**
      This method prints a {@link String} representation of the given pixel
      from a "vertical" line that is being rasterized along the y-axis.

      @param clippedMessage  {@link String} specifying if the pixel was clipped or not
      @param x_pp  horizontal coordinate of the pixel in the pixel-plane
      @param y_pp  vertical coordinate of the pixel in the pixel-plane
      @param x_vp  horizontal coordinate of the pixel in the {@link FrameBuffer.Viewport}
      @param y_vp  vertical coordinate of the pixel in the {@link FrameBuffer.Viewport}
      @param r   red component of the pixel's {@link Color}
      @param g   green component of the pixel's {@link Color}
      @param b   blue component of the pixel's {@link Color}
      @param vp  {@link FrameBuffer.Viewport} that the pixel is being placed in
   */
   public static void logPixel(final String clippedMessage,
                               final double x_pp, final int y_pp,
                               final int    x_vp, final int y_vp,
                               final float r, final float g, final float b,
                               final FrameBuffer.Viewport vp)
   {
      if (debugScene || debugPosition)
      {
         final int wVP = vp.getWidthVP();
         final int hVP = vp.getHeightVP();
         final int xVP = vp.vp_ul_x;
         final int yVP = vp.vp_ul_y;
         final FrameBuffer fb = vp.getFrameBuffer();
         final int wFB = fb.getWidthFB();
         final int hFB = fb.getHeightFB();
         System.err.print(clippedMessage);
         System.err.printf(
            "fb_[w=%d,h=%d] vp_[x=%4d, y=%4d, w=%d,h=%d]  (x_pp=%9.4f, y_pp=%4d)  (x_vp=%4d, y_vp=%4d)  r=%.4f g=%.4f b=%.4f\n",
                 wFB, hFB,      xVP,   yVP,   wVP, hVP,    x_pp,       y_pp,       x_vp,     y_vp,      r,     g,     b);
      }
   }


   /**
      Log two anti-aliased pixels from a "horizontal" line
      that is being rasterized along the x-axis.

      @param clippedMessage  {@link String} specifying if the pixel was clipped or not
      @param x_pp   horizontal coordinate of the pixel in the pixel-plane
      @param y_pp   vertical coordinate of the pixel in the pixel-plane
      @param x_vp   horizontal coordinate of the anti-aliased pixels in the {@link FrameBuffer.Viewport}
      @param y1_vp  vertical coordinate of the first anti-aliased pixel in the {@link FrameBuffer.Viewport}
      @param y2_vp  vertical coordinate of the second anti-aliased pixel in the {@link FrameBuffer.Viewport}
      @param r1     red component of the first anti-aliased pixel's {@link Color}
      @param g1     green component of the first anti-aliased pixel's {@link Color}
      @param b1     blue component of the first anti-aliased pixel's {@link Color}
      @param r2     red component of the second anti-aliased pixel's {@link Color}
      @param g2     green component of the second anti-aliased pixel's {@link Color}
      @param b2     blue component of the second anti-aliased pixel's {@link Color}
      @param vp     {@link FrameBuffer.Viewport} that the pixel is being placed in
   */
   public static void logPixelsAA(final String clippedMessage,
                                  final int x_pp, final double y_pp,
                                  final int x_vp, final int y1_vp, final int y2_vp,
                                  final float r1, final float g1, final float b1,
                                  final float r2, final float g2, final float b2,
                                  final FrameBuffer.Viewport vp)
   {
      if (debugScene || debugPosition)
      {
         final int wVP = vp.getWidthVP();
         final int hVP = vp.getHeightVP();
         final int xVP = vp.vp_ul_x;
         final int yVP = vp.vp_ul_y;
         final FrameBuffer fb = vp.getFrameBuffer();
         final int wFB = fb.getWidthFB();
         final int hFB = fb.getHeightFB();
         System.err.print(clippedMessage);
         System.err.printf(
         "fb_[w=%d,h=%d] vp_[x=%4d, y=%4d, w=%d,h=%d]  (x_pp=%4d, y_pp=%9.4f)  x_vp=%4d {y_low=%4d r=%.4f g=%.4f b=%.4f} {y_hi =%4d r=%.4f g=%.4f b=%.4f}\n",
              wFB, hFB,      xVP,   yVP,   wVP, hVP,    x_pp,     y_pp,        x_vp,     y1_vp,    r1,    g1,    b1,      y2_vp,    r2,    g2,    b2);
      }
   }


   /**
      Log two anti-aliased pixels from a "vertical" line
      that is being rasterized along the y-axis.

      @param clippedMessage  {@link String} specifying if the pixel was clipped or not
      @param x_pp   horizontal coordinate of the pixel in the pixel-plane
      @param y_pp   vertical coordinate of the pixel in the pixel-plane
      @param x1_vp  horizontal coordinate of the first anti-aliased pixel in the {@link FrameBuffer.Viewport}
      @param x2_vp  horizontal coordinate of the second anti-aliased pixel in the {@link FrameBuffer.Viewport}
      @param y_vp   vertical coordinate of the anti-aliased pixels in the {@link FrameBuffer.Viewport}
      @param r1     red component of the first anti-aliased pixel's {@link Color}
      @param g1     green component of the first anti-aliased pixel's {@link Color}
      @param b1     blue component of the first anti-aliased pixel's {@link Color}
      @param r2     red component of the second anti-aliased pixel's {@link Color}
      @param g2     green component of the second anti-aliased pixel's {@link Color}
      @param b2     blue component of the second anti-aliased pixel's {@link Color}
      @param vp     {@link FrameBuffer.Viewport} that the pixel is being placed in
   */
   public static void logPixelsAA(final String clippedMessage,
                                  final double x_pp, final int y_pp,
                                  final int x1_vp, final int x2_vp, final int y_vp,
                                  final float r1, final float g1, final float b1,
                                  final float r2, final float g2, final float b2,
                                  final FrameBuffer.Viewport vp)
   {
      if (debugScene || debugPosition)
      {
         final int wVP = vp.getWidthVP();
         final int hVP = vp.getHeightVP();
         final int xVP = vp.vp_ul_x;
         final int yVP = vp.vp_ul_y;
         final FrameBuffer fb = vp.getFrameBuffer();
         final int wFB = fb.getWidthFB();
         final int hFB = fb.getHeightFB();
         System.err.print(clippedMessage);
         System.err.printf(
         "fb_[w=%d,h=%d] vp_[x=%4d, y=%4d, w=%d,h=%d]  (x_pp=%9.4f, y_pp=%4d)  y_vp=%4d {x_low=%4d r=%.4f g=%.4f b=%.4f} {x_hi =%4d r=%.4f g=%.4f b=%.4f}\n",
              wFB, hFB,      xVP,   yVP,   wVP, hVP,    x_pp,       y_pp,      y_vp,     x1_vp,    r1,    g1,    b1,      x2_vp,    r2,    g2,    b2);
      }
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rdd Ed, Joshua Bloch.
   private PipelineLogger() {
      throw new AssertionError();
   }
}
