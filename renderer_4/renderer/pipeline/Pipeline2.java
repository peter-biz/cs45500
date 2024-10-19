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
<p>
   This second version of the rendering pipeline does each stage
   of the pipeline on the entire scene before it moves on to the
   next stage. So each stage of the pipeline produces a new Scene
   object that is the transformation of the Scene object from the
   previous stage.
*/
public final class Pipeline2
{
   // Mostly for compatibility with renderers 1 through 3.
   public static Color DEFAULT_COLOR = Color.white;

   // Make the three intermediate Scene objects
   // available for special processing.
   public static Scene scene1 = null; // Will hold the result of stage 1.
   public static Scene scene2 = null; // Will hold the result of stage 2.
   public static Scene scene3 = null; // Will hold the result of stage 3.

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

      logMessage("\n== Begin Rendering of Scene (Pipeline 2): " + scene.name + " =");

      logMessage("-- Current Camera:\n" + scene.camera);

      // 1. Apply each Position's model-to-camera coordinate transformation.
      scene1 = new Scene(scene.name, scene.camera);

      logMessage("=== 1. Begin model-to-camera transformation of Scene ====");
      for (final Position position : scene.positionList)
      {
         PipelineLogger.debugPosition = position.debug;

         if ( position.visible )
         {
            logMessage("===== 1. Render position: " + position.name + " ====");

            logMessage("----- Translation vector = " + position.getTranslation());

            if ( position.getModel().visible )
            {
               logMessage("===== 1. Model-to-camera transform of: "
                                 + position.getModel().name + " ====");

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

               logVertexList("0. Model    ", position.getModel());

               final Model tempModel = Model2Camera.model2camera(position);

               logVertexList("1. Camera     ", tempModel);

               scene1.addPosition( new Position(tempModel) );

               logMessage("===== 1. End Model: "
                                 + tempModel.name + " ====");
            }
            else
            {
               logMessage("===== 1. Hidden model: "
                                 + position.getModel().name + " ====");
            }

            logMessage("===== 1. End position: "
                              + position.name + " ====");
         }
         else
         {
            logMessage("===== 1. Hidden position: "
                              + position.name + " ====");
         }
      }
      logMessage("=== 1. End model-to-camera transformation of Scene ====");


      // 2. Apply the Camera's projection transformation.
      scene2 = new Scene(scene.name, scene.camera);

      logMessage("=== 2. Begin projection transformation of Scene ====");
      for (final Position position : scene1.positionList)
      {
         logMessage("===== 2. Project model: "
                           + position.getModel().name + " ====");

         final Model tempModel = Projection.project(position.getModel(),
                                                    scene.camera);

         logVertexList("2. Projected", tempModel);
         logPrimitiveList("2. Projected  ", tempModel);

         scene2.addPosition( new Position(tempModel) );

         logMessage("===== 2. End Model: " + tempModel.name + " ====");
      }
      logMessage("=== 2. End projection transformation of Scene ====");


      // 3. Apply the image-plane to pixel-plane transformation.
      scene3 = new Scene(scene.name, scene.camera);

      logMessage("=== 3. Begin image-plane to pixel-plane transformation of Scene ====");
      for (final Position position : scene2.positionList)
      {
         logMessage("===== 3. Transform model: "
                           + position.getModel().name + " ====");

         final Model tempModel = Viewport.imagePlane2pixelPlane(position.getModel(),
                                                                vp);

         logVertexList("3. Pixel-plane", tempModel);
         logPrimitiveList("3. Pixel-plane  ", tempModel);

         scene3.addPosition( new Position(tempModel) );

         logMessage("===== 3. End Model: " + tempModel.name + " ====");
      }
      logMessage("=== 3. End image-plane to pixel-plane transformation of Scene ====");


      // 4. Rasterize and clip every visible primitive into pixels.
      logMessage("=== 4. Begin rasterization of Scene ====");
      for (final Position position : scene3.positionList)
      {
         logMessage("===== 4. Rasterize model: "
                           + position.getModel().name + " ====");

         Rasterize.rasterize(position.getModel(), vp);

         logMessage("===== 4. End Model: "
                           + position.getModel().name + " ====");
      }
      logMessage("=== 4. End rasterization of Scene ====");

      logMessage("== End Rendering of Scene (Pipeline 2) ==");
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Pipeline2() {
      throw new AssertionError();
   }
}
