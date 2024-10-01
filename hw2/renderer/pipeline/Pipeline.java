/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.pipeline;

import renderer.scene.*;
import renderer.scene.util.CheckModel;
import renderer.framebuffer.*;
import static renderer.pipeline.PipelineLogger.*;

import java.awt.Color;

/**
   This renderer takes as its input a {@link Scene} data structure
   and a {@link FrameBuffer.Viewport} within a {@link FrameBuffer}
   data structure. This renderer mutates the {@link FrameBuffer.Viewport}
   so that it is filled in with the rendered image of the geometric
   scene represented by the {@link Scene} object.
<p>
   This implements our third rendering pipeline. This pipeline can
   process the colors stored in each {@link Model}. This renderer
   has the same four pipeline stages but {@link Vertex} colors
   are now interpolated to pixels by the raterization stage.
*/
public final class Pipeline
{
   // Mostly for compatibility with renderers 1 through 3.
   public static Color DEFAULT_COLOR = Color.white;

   /**
      Mutate the {@link FrameBuffer}'s default {@link FrameBuffer.Viewport}
      so that it holds the rendered image of the {@link Scene} object.

      @param scene  {@link Scene} object to render
      @param fb     {@link FrameBuffer} to hold rendered image of the {@link Scene}
   */
   public static void render(final Scene scene, final FrameBuffer fb)
   {
      render(scene, fb.vp); // render into the default viewport
   }


   /**
      Mutate the {@link FrameBuffer}'s given {@link FrameBuffer.Viewport}
      so that it holds the rendered image of the {@link Scene} object.

      @param scene  {@link Scene} object to render
      @param vp     {@link FrameBuffer.Viewport} to hold rendered image of the {@link Scene}
   */
   public static void render(final Scene scene, final FrameBuffer.Viewport vp)
   {
      PipelineLogger.debugScene = scene.debug;

      logMessage("\n== Begin Rendering of Scene: " + scene.name + " ==");

      logMessage("-- Current Camera:\n" + scene.camera);

      // For every Position in the Scene, render the Position's Model.
      for (final Position position : scene.positionList)
      {
         PipelineLogger.debugPosition = position.debug;

         if ( position.visible )
         {
            logMessage("==== Render position: " + position.name + " ====");

            logMessage("---- Translation vector = " + position.getTranslation());

            if ( position.getModel().visible )
            {
               logMessage("====== Render model: "
                                  + position.getModel().name + " ======");

               CheckModel.check(position.getModel());

               // Mostly for compatibility with renderers 1 through 3.
               if (  position.getModel().colorList.isEmpty()
                 && !position.getModel().vertexList.isEmpty())
               {
                  for (int i = 0; i < position.getModel().vertexList.size(); ++i)
                  {
                     position.getModel().addColor( DEFAULT_COLOR );
                  }
                  System.err.println("***WARNING: Added default color to model: "
                                    + position.getModel().name + ".");
               }

               logVertexList("0. Model      ", position.getModel());

               // 1. Apply the Position's model-to-camera coordinate transformation.
               final Model model1 = Model2Camera.model2camera(position);

               logVertexList("1. Camera     ", model1);

               // 2. Apply the Camera's projection transformation.
               final Model model2 = Projection.project(model1,
                                                       scene.camera);

               logVertexList("2. Projected  ", model2);

               // 3. Apply the image-plane to pixel-plane transformation.
               final Model model3 = Viewport.imagePlane2pixelPlane(model2,
                                                                   vp);

               logVertexList("3. Pixel-plane", model3);
               logPrimitiveList("3. Pixel-plane", model3);
               logColorList("3. Pixel-plane", model3);

               // 4. Rasterize and clip every visible primitive into pixels.
               Rasterize.rasterize(model3, vp);

               logMessage("====== End model: "
                                  + position.getModel().name + " ======");
            }
            else
            {
               logMessage("====== Hidden model: "
                                  + position.getModel().name + " ======");
            }

            logMessage("==== End position: " + position.name + " ====");
         }
         else
         {
            logMessage("==== Hidden position: " + position.name + " ====");
         }
      }
      logMessage("== End Rendering of Scene ==");
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Pipeline() {
      throw new AssertionError();
   }
}
