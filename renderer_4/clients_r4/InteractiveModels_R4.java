/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.util.Assets;
import renderer.scene.util.ModelShading;
import renderer.models_L.*;
import renderer.framebuffer.FrameBufferPanel;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.File;

/**

*/
public class InteractiveModels_R4 extends InteractiveAbstractClient_R4
{
   private static final String assets = Assets.getPath();

   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public InteractiveModels_R4()
   {
      scene = new Scene("InteractiveModels_R4");

      // Create several Model objects.
      scene.addPosition(new Position(new ObjSimpleModel(
                             new File(assets + "apple.obj"))));
      scene.addPosition(new Position(new ObjSimpleModel(
                             new File(assets + "cow.obj"))));
      scene.addPosition(new Position(new ObjSimpleModel(
                             new File(assets + "galleon.obj"))));
      scene.addPosition(new Position(new ObjSimpleModel(
                             new File(assets + "teapot.obj"))));
      scene.addPosition(new Position(new ObjSimpleModel(
                             new File(assets + "stanford_bunny.obj"))));
      scene.addPosition(new Position(new ObjSimpleModel(
                             new File(assets + "cessna.obj"))));
      scene.addPosition(new Position(new Sphere(1.0, 30, 30)));
      scene.addPosition(new Position(new Cylinder(0.5, 1.0, 20, 20)));
      scene.addPosition(new Position(new Torus(0.75, 0.25, 25, 25)));
      scene.addPosition(new Position(new Cube2(15, 15, 15)));
      scene.addPosition(new Position(new ObjSimpleModel(new File(
                             assets + "small_rhombicosidodecahedron.obj"))));
      scene.addPosition(new Position(new PanelXY(-7, 7, -1, 3)));  // wall
      scene.addPosition(new Position(new PanelXZ(-7, 7, -3, 1)));  // floor
      scene.addPosition(new Position(new ObjSimpleModel(           // airplane
                             new File(assets + "cessna.obj"))));

      // Give each model a random color.
      for (final Position p : scene.positionList)
      {
         ModelShading.setRandomColor( p.getModel() );
      }

      // Make the interactive models invisible, except for the current model.
      numberOfInteractiveModels = scene.positionList.size() - 3;
      for (int i = 0; i < numberOfInteractiveModels; ++i)
      {
         scene.getPosition(i).visible = false;
      }
      currentModel = 1; // cow
      scene.getPosition(currentModel).visible = true;
      interactiveModelsAllVisible = false;
      debugWholeScene = false;

      // Position the wall, floor and airplane.
      final int size = scene.positionList.size();
      scene.getPosition(size - 3).translate(0,  0, -5); // wall
      scene.getPosition(size - 2).translate(0, -1, -2); // floor
      scene.getPosition(size - 1).translate(3,  0, -2); // airplane

      // Have the models pushed away from where the camera is.
      deltaX = new double[numberOfInteractiveModels];
      deltaY = new double[numberOfInteractiveModels];
      deltaZ = new double[numberOfInteractiveModels];
      java.util.Arrays.fill(deltaZ, -2.0);
      scene.getPosition(currentModel).translate(0, 0, -2);

      renderer.pipeline.Rasterize.doClipping = true;


      // Create a FrameBufferPanel that holds a FrameBuffer.
      final int width  = 1024;
      final int height = 1024;
      fbp = new FrameBufferPanel(width, height, Color.black);

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Renderer 4 - Interactive Models");
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.getContentPane().add(fbp, BorderLayout.CENTER);
      jf.pack();
      jf.setLocationRelativeTo(null);
      jf.setVisible(true);

      // Register this object as the event listener for JFrame events.
      jf.addKeyListener(this);
      jf.addComponentListener(this);

      print_help_message();
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

      scene.getPosition(currentModel).translate(deltaX[currentModel],
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
      System.out.println("Use the '/' and '?' keys to cycle forwards and backwards through the models.");
      System.out.println("Use the '>/<' and shift keys to increase and decrease the mesh divisions in each direction.");
      System.out.println("Use the 'i/I' keys to get information about the current model.");
      System.out.println("Use the 'p' key to toggle between parallel and orthographic projection.");
      System.out.println("Use the x/X, y/Y, z/Z, keys to translate the current model along the x, y, z axes.");
      System.out.println("Use the 'm' key to toggle the display of the current model's translation information.");
      System.out.println("Use the '=' key to reset the current model's translation.");
      System.out.println("Use the 'c' key to change the random solid model color.");
      System.out.println("Use the 'C' key to randomly change the current model's colors.");
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
         () -> new InteractiveModels_R4()
      );
   }
}
