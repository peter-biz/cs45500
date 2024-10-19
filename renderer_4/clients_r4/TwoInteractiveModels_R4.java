/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.util.ModelShading;
import renderer.scene.util.DrawSceneGraph;
import renderer.models_L.*;
import renderer.framebuffer.FrameBufferPanel;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.File;

/**

*/
public class TwoInteractiveModels_R4 extends InteractiveAbstractClient_R4
{
   protected final int[] visibility = {0, 0};

   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public TwoInteractiveModels_R4()
   {
      scene = new Scene("TwoInteractiveModels_R4");

      // Add two Positions to the Scene.
      scene.addPosition(new Position( new Octahedron(), "p1" ));
      scene.addPosition(new Position( new TriangularPyramid(), "p2" ));

      // Give each model a random color.
      for (final Position p : scene.positionList)
      {
         ModelShading.setRandomColor( p.getModel() );
      }

      // Make both interactive models visible.
      numberOfInteractiveModels = scene.positionList.size();
      currentModel = 0;
      scene.getPosition(0).visible = true;
      scene.getPosition(1).visible = true;
      interactiveModelsAllVisible = true;
      debugWholeScene = true;

      // Have the models pushed away from where the camera is.
      deltaX = new double[numberOfInteractiveModels];
      deltaY = new double[numberOfInteractiveModels];
      deltaZ = new double[numberOfInteractiveModels];
      java.util.Arrays.fill(deltaZ, -2.0);
      scene.getPosition(0).translate(0, 0, -2);
      scene.getPosition(1).translate(0, 0, -2);

      renderer.pipeline.Rasterize.doClipping = true;

      // Draw a picture of the scene's tree (DAG) data structure.
      DrawSceneGraph.drawVertexList = true;
      DrawSceneGraph.draw(scene, "TwoInteractiveModels_R4_SG");


      // Create a FrameBufferPanel that holds a FrameBuffer.
      final int width  = 1024;
      final int height = 1024;
      fbp = new FrameBufferPanel(width, height, Color.black);

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Renderer 4 - Two Interactive Models");
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.getContentPane().add(fbp, BorderLayout.CENTER);
      jf.pack();
      jf.setLocationRelativeTo(null);
      jf.setVisible(true);

      // Create event handler objects for events from the JFrame.
      jf.addKeyListener(this);
      jf.addComponentListener(this);

      print_help_message();
   }


   // Re-implement part of the KeyListener interface.
   @Override public void keyTyped(KeyEvent e)
   {
      final char c = e.getKeyChar();
      if (';' == c)
      {
         ++visibility[currentModel];
         if (3 == visibility[currentModel]) visibility[currentModel] = 0;
      }

      // Set the visibility of the current model.
      if (0 == visibility[currentModel])
      {
         scene.getPosition(currentModel).visible = true;
         scene.getPosition(currentModel).getModel().visible = true;
      }
      else if (1 == visibility[currentModel])
      {
         scene.getPosition(currentModel).visible = true;
         scene.getPosition(currentModel).getModel().visible = false;
      }
      else if (2 == visibility[currentModel])
      {
         scene.getPosition(currentModel).visible = false;
      }

      super.keyTyped(e);
   }


   // Change how the program deals with translations.
   @Override protected void doTranslation(final KeyEvent e)
   {
      final char c = e.getKeyChar();

      if ('=' == c) // Reset the current translation vector.
      {
         deltaX[currentModel] =  0;
         deltaY[currentModel] =  0;
         deltaZ[currentModel] = -2;
      }
      else if ('x' == c) // Translate the current model.
      {
         deltaX[currentModel] += -0.1;  // left
      }
      else if ('X' == c)
      {
         deltaX[currentModel] += +0.1;  // right
      }
      else if ('y' == c)
      {
         deltaY[currentModel] += -0.1;  // down
      }
      else if ('Y' == c)
      {
         deltaY[currentModel] += +0.1;  // up
      }
      else if ('z' == c)
      {
         deltaZ[currentModel] += -0.1;  // back
      }
      else if ('Z' == c)
      {
         deltaZ[currentModel] += +0.1;  // forward
      }

      scene.getPosition(currentModel).translate(
                                         deltaX[currentModel],
                                         deltaY[currentModel],
                                         deltaZ[currentModel]);
   }


   // Change how the program prints translation information.
   @Override protected void displayTranslation(final KeyEvent e)
   {
      final char c = e.getKeyChar();

      if (displayTranslation && ('m'==c||'='==c
                               ||'/'==c||'?'==c
                               ||'x'==c||'y'==c||'z'==c
                               ||'X'==c||'Y'==c||'Z'==c))
      {
         System.out.println("Current model is " + currentModel +".");
         System.out.printf("deltaX = %.2f, " +
                           "deltaY = %.2f, " +
                           "deltaZ = %.2f\n",
                           deltaX[currentModel],
                           deltaY[currentModel],
                           deltaZ[currentModel]);
      }
   }


   // Change how the program prints help information.
   @Override protected void print_help_message()
   {
      System.out.println("Use the 'd/D' keys to toggle debugging information on and off for the current model.");
      System.out.println("Use the '/' and '?' keys to cycle forwards and backwards through the two models.");
      System.out.println("Use the ';' key to cycle through the current model's visibility.");
      System.out.println("Use the '>/<' and shift keys to increase and decrease the mesh divisions in each direction.");
      System.out.println("Use the 'i/I' keys to get information about the current model.");
      System.out.println("Use the 'p' key to toggle between parallel and orthographic projection.");
      System.out.println("Use the x/X, y/Y, z/Z, keys to translate the current model along the x, y, z axes.");
      System.out.println("Use the 'm' key to toggle the display of the current model's translation information.");
      System.out.println("Use the '=' key to reset the current model's translation.");
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


   /**
      Create an instance of this class which has
      the affect of creating the GUI application.
   */
   public static void main(String[] args)
   {
      // We need to call the program's constructor in the
      // Java GUI Event Dispatch Thread, otherwise we get a
      // race condition between the constructor (running in
      // the main() thread) and the very first ComponentEvent
      // (running in the EDT).
      javax.swing.SwingUtilities.invokeLater(
         () -> new TwoInteractiveModels_R4()
      );
   }
}
