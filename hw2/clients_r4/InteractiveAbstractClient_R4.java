/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.PointCloud;
import renderer.scene.util.MeshMaker;
import renderer.scene.util.ModelShading;
import renderer.pipeline.*;
import renderer.framebuffer.*;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

/**
   This class holds all the client state information
   and implementations of the client's event handlers.
*/
public abstract class InteractiveAbstractClient_R4 implements
                                                   KeyListener,
                                                   ComponentListener
{
   protected boolean perspective = true;

   protected boolean displayTranslation = false;
   protected double pushback = -2.0;
   protected double[] deltaX = {0.0};
   protected double[] deltaY = {0.0};
   protected double[] deltaZ = {pushback};

   protected Scene scene = null;
   protected int numberOfInteractiveModels = 1;
   protected boolean interactiveModelsAllVisible = false;
   protected boolean debugWholeScene = true;
   protected int currentModel = 0;
   private Model savedModel = null; // used to hold a PointCloud model
   private int pointSize = 0;       // used by the point clouds

   protected boolean takeScreenshot = false;
   protected int screenshotNumber = 0;

   protected JFrame jf = null;
   protected FrameBufferPanel fbp = null;


   // Implement the KeyListener interface.
   @Override public void keyPressed(KeyEvent e){}
   @Override public void keyReleased(KeyEvent e){}
   @Override public void keyTyped(KeyEvent e)
   {
      //System.out.println( e );

      final char c = e.getKeyChar();
      if ('h' == c)
      {
         print_help_message();
         return;
      }
      else if ('d' == c)
      {
         if (debugWholeScene)
         {
            scene.debug = ! scene.debug;
         }
         else // debug just the current model
         {
            final Position p = scene.getPosition(currentModel);
            p.debug = ! p.debug;
         }
      }
      else if ('D' == c)
      {
         Rasterize.debug = ! Rasterize.debug;
      }
      else if ('i' == c)
      {
         final Model m = scene.getPosition(currentModel).getModel();
         final int verts = m.vertexList.size();
         int points = 0;
         int lines = 0;
         for (final Primitive p : m.primitiveList)
         {
            if (p instanceof Point)
            {
               ++points;
            }
            else if (p instanceof LineSegment)
            {
               ++lines;
            }
         }
         System.out.printf("The current Model has:\n");
         System.out.printf("  %,d vertices,\n", verts);
         System.out.printf("  %,d Point primitives,\n", points);
         System.out.printf("  %,d Line segments.\n", lines);
      }
      else if ('I' == c)
      {
         System.out.println();
         System.out.println(scene.getPosition(currentModel).getModel());
      }
      else if ('b' == c)
      {
         Rasterize.doClipping = ! Rasterize.doClipping;
         System.out.print("Clipping is turned ");
         System.out.println(Rasterize.doClipping ? "On" : "Off");
      }
      else if ('/' == c)
      {
         scene.getPosition(currentModel).visible = interactiveModelsAllVisible;
         currentModel = (currentModel + 1) % numberOfInteractiveModels;
         scene.getPosition(currentModel).visible = true;
         savedModel = null;
         pointSize = 0;
      }
      else if ('?' == c)
      {
         scene.getPosition(currentModel).visible = interactiveModelsAllVisible;
         currentModel = currentModel - 1;
         if (currentModel < 0) currentModel = numberOfInteractiveModels - 1;
         scene.getPosition(currentModel).visible = true;
         savedModel = null;
         pointSize = 0;
      }
      else if ('a' == c)
      {
         Rasterize.doAntiAliasing = ! Rasterize.doAntiAliasing;
         System.out.print("Anti-aliasing is turned ");
         System.out.println(Rasterize.doAntiAliasing ? "On" : "Off");
      }
      else if ('g' == c)
      {
         Rasterize.doGamma = ! Rasterize.doGamma;
         System.out.print("Gamma correction is turned ");
         System.out.println(Rasterize.doGamma ? "On" : "Off");
      }
      else if ('p' == c)
      {
         perspective = ! perspective;
         final String p = perspective ? "perspective" : "orthographic";
         System.out.println("Using " + p + " projection");
      }
      else if ('P' == c)
      {
         if (savedModel != null)
         {
            scene.getPosition(currentModel).setModel(savedModel);
            savedModel = null;
            ++pointSize;
         }
         else
         {
            final Model model = scene.getPosition(currentModel).getModel();
            savedModel = model;
            scene.getPosition(currentModel).setModel(
                                 PointCloud.make(model, pointSize));
         }
      }
      else if ('c' == c)
      {
         // Change the solid random color of the cube.
         ModelShading.setRandomColor(
                         scene.getPosition(currentModel).getModel());
      }
      else if ('C' == c)
      {
         // Change each color in the cube to a random color.
         ModelShading.setRandomColors(
                         scene.getPosition(currentModel).getModel());
      }
      else if ('e' == c && e.isAltDown())
      {
         // Change the random color of each vertex of the cube.
         ModelShading.setRandomVertexColors(
                         scene.getPosition(currentModel).getModel());
      }
      else if ('e' == c)
      {
         // Change the solid random color of each edge of the cube.
         ModelShading.setRandomPrimitiveColors(
                         scene.getPosition(currentModel).getModel());
      }
      else if ('E' == c)
      {
         // Change the random color of each end of each edge of the cube.
         ModelShading.setRainbowPrimitiveColors(
                         scene.getPosition(currentModel).getModel());
      }
      else if ('>' == c || '<' == c
             ||'.' == c || ',' == c)
      {
         final Position p = scene.getPosition(currentModel);
         if (p.getModel() instanceof MeshMaker)
         {
            final MeshMaker model = (MeshMaker)p.getModel();
            final int n;
            final int k;
            if ('>' == c)
            {
               n = model.getHorzCount() + 1;
               k = model.getVertCount();
            }
            else if ('<' == c)
            {
               n = model.getHorzCount() - 1;
               k = model.getVertCount();
            }
            else if ('.' == c)
            {
               n = model.getHorzCount();
               k = model.getVertCount() + 1;
            }
            else  // ',' == c
            {
               n = model.getHorzCount();
               k = model.getVertCount() - 1;
            }
            try
            {
               final Model newM = model.remake(n, k);
               // Copy the color of the previous model.
               ModelShading.setColor(newM, p.getModel().colorList.get(0));
               scene.getPosition(currentModel).setModel(newM);
               final MeshMaker m = (MeshMaker)newM;
               final int newN = m.getHorzCount();
               final int newK = m.getVertCount();
               System.out.printf("Mesh: n = %3d and k = %3d.\n", newN, newK);
            }
            catch (IllegalArgumentException ex){}
         }
      }
      else if ('m' == c) // Display translation information.
      {
         displayTranslation = ! displayTranslation;
      }
      else if ('+' == c)
      {
         takeScreenshot = true;
      }

      // Set up the camera's view volume.
      if (perspective)
      {
         scene = scene.changeCamera( Camera.projPerspective() );
      }
      else
      {
         scene = scene.changeCamera( Camera.projOrtho() );
      }

      doTranslation(e);

      displayTranslation(e);

      setupViewing();
   }//keyTyped()


   // Implement the ComponentListener interface.
   @Override public void componentMoved(ComponentEvent e){}
   @Override public void componentHidden(ComponentEvent e){}
   @Override public void componentShown(ComponentEvent e){}
   @Override public void componentResized(ComponentEvent e)
   {
      //System.out.println( e );
      //System.out.printf("JFrame [w = %d, h = %d]: " +
      //                  "FrameBufferPanel [w = %d, h = %d].\n",
      //                  jf.getWidth(), jf.getHeight(),
      //                  fbp.getWidth(), fbp.getHeight());

      // Get the new size of the FrameBufferPanel.
      final int w = fbp.getWidth();
      final int h = fbp.getHeight();

      // Create a new FrameBuffer that fits the FrameBufferPanel.
      final FrameBuffer fb = new FrameBuffer(w, h);
      fbp.setFrameBuffer(fb);

      setupViewing();
   }//componentResized()


   private void setupViewing()
   {
      // Render again.
      final FrameBuffer fb = fbp.getFrameBuffer();
      fb.clearFB();
      Pipeline.render(scene, fb);
      if (takeScreenshot)
      {
         fb.dumpFB2File(String.format("Screenshot%03d.png", screenshotNumber),
                        "png");
         ++screenshotNumber;
         takeScreenshot = false;
      }
      fbp.repaint();
   }


   // A client program can override how translations are performed.
   protected void doTranslation(final KeyEvent e)
   {
      final char c = e.getKeyChar();

      if ('=' == c) // Reset the translation vector.
      {
         deltaX[0] = 0;
         deltaY[0] = 0;
         deltaZ[0] = pushback;
      }
      else if ('x' == c) // Translate the current model:
      {
         deltaX[0] += -0.1;  // left
      }
      else if ('X' == c)
      {
         deltaX[0] += +0.1;  // right
      }
      else if ('y' == c)
      {
         deltaY[0] += -0.1;  // down
      }
      else if ('Y' == c)
      {
         deltaY[0] += +0.1;  // up
      }
      else if ('z' == c)
      {
         deltaZ[0] += -0.1;  // back
      }
      else if ('Z' == c)
      {
         deltaZ[0] += +0.1;  // forward
      }

      scene.setPosition(currentModel,
         scene.getPosition(currentModel).translate(deltaX[0],
                                                   deltaY[0],
                                                   deltaZ[0]));
   }


   // A client program can override the printing of translation information.
   protected void displayTranslation(final KeyEvent e)
   {
      final char c = e.getKeyChar();

      if (displayTranslation && ('m'==c||'='==c
                               ||'x'==c||'y'==c||'z'==c
                               ||'X'==c||'Y'==c||'Z'==c))
      {
         System.out.printf("deltaX = %.2f, " +
                           "deltaY = %.2f, " +
                           "deltaZ = %.2f\n",
                           deltaX[0],
                           deltaY[0],
                           deltaZ[0]);
      }
   }


   // A client program can override the printing of help information.
   protected void print_help_message()
   {
      System.out.println("Use the 'd/D' keys to toggle debugging information on and off for the current model.");
      System.out.println("Use the '/' and '?' keys to cycle forwards and backwards through the models.");
      System.out.println("Use the '>/<' and shift keys to increase and decrease the mesh divisions in each direction.");
      System.out.println("Use the 'i/I' keys to get information about the current model.");
      System.out.println("Use the 'p' key to toggle between parallel and orthographic projection.");
      System.out.println("Use the x/X, y/Y, z/Z, keys to translate the model along the x, y, z axes.");
      System.out.println("Use the 'm' key to toggle the display of translation information.");
      System.out.println("Use the '=' key to reset the model translation.");
      System.out.println("Use the 'c' key to change the random solid model color.");
      System.out.println("Use the 'C' key to randomly change model's colors.");
      System.out.println("Use the 'e' key to change the random solid edge colors.");
      System.out.println("Use the 'E' key to change the random edge colors.");
      System.out.println("Use the 'Alt-e' key combination to change the random vertex colors.");
      System.out.println("Use the 'a' key to toggle anti-aliasing on and off.");
      System.out.println("Use the 'g' key to toggle gamma correction on and off.");
      System.out.println("Use the 'b' key to toggle line clipping on and off.");
      System.out.println("Use the 'P' key to convert the current model to a point cloud.");
      System.out.println("Use the '+' key to save a \"screenshot\" of the framebuffer.");
      System.out.println("Use the 'h' key to redisplay this help message.");
   }
}
