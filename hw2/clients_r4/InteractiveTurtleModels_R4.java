/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.util.ModelShading;
import renderer.models_L.turtlegraphics.*;
import renderer.framebuffer.FrameBufferPanel;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;

/**

*/
public class InteractiveTurtleModels_R4 extends InteractiveAbstractClient_R4
{
   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public InteractiveTurtleModels_R4()
   {
      scene = new Scene("InteractiveTurtleModels_R4");

      int N = 9;

      for (int i = 0; i <= 5; ++i)
      {
         final Model m = new Model("polygasket_"+N+"_"+i);
         new PolygasketTurtle(m, N, i, -1.0, 0.0, -2.0);
         scene.addPosition(new Position(m));
      }

      N = 7;

      for (int i = 0; i <= 6; ++i)
      {
         final Model m = new Model("polygasket_"+N+"_"+i);
         new PolygasketTurtle(m, N, i, -1.0, 0.0, -2.0);
         scene.addPosition(new Position(m));
      }

      for (int i = 0; i <= 8; ++i)
      {
         final Model m = new Model("pentagasket " + i);
         new PentagasketTurtle(m, i, -0.6, -0.6, -1.0);
         scene.addPosition(new Position(m));
      }

      for (int i = 0; i <= 8; ++i)
      {
         final Model m = new Model("sierpinski " + i);
         new SierpinskiTurtle(m, i, 4.0, -1.5, -2.0, -2.0);
         scene.addPosition(new Position(m));
      }

      for (int i = 0; i <= 10; ++i)
      {
         final Model m = new Model("sierpinski curve " + i);
         new SierpinskiCurveTurtle(m, i, 4.0, -1.5, -2.0, -2.0);
         scene.addPosition(new Position(m));
      }

      for (int i = 0; i <= 8; ++i)
      {
         final Model m = new Model("hilbert curve " + i);
         new HilbertCurveTurtle(m, i, 2.0, 0.1, -1.9, -2.0);
         scene.addPosition(new Position(m));
      }


      final Model ninjaModel = new Model("Ninja Turtle");
      new NinjaTurtle(ninjaModel, 180, 0.0, 0.0, -2.0);
      scene.addPosition(new Position(ninjaModel));

      final Model ninjaModel2 = new Model("Ninja Turtle");
      new NinjaTurtle(ninjaModel2, 360, 0.0, 0.0, -2.0);
      scene.addPosition(new Position(ninjaModel2));

      final Model spiralModel = new Model("Spiral Turtle");
      new SpiralTurtle(spiralModel, 270, -0.3, -0.5, -0.6);
      scene.addPosition(new Position(spiralModel));

      final Model spiralModel2 = new Model("Spiral Turtle");
      new SpiralTurtle(spiralModel2, 335, -0.3, -0.5, -0.6);
      scene.addPosition(new Position(spiralModel2));


      final Model turtleModel = new Model("Turtle Expeiment");
      final Turtle turtle = new Turtle(turtleModel, -2.0, 0.0, -3.0);

      final int angle = 144;
      final double length = 1.0;
      // draw a pentagram
      final int n = 5;
      for (int i = 1; i <= n; ++i)
      {
         turtle.forward(length);
         turtle.turn(angle);
      }
      for (int j = 1; j < 32; ++j)
      {
// https://www.clear.rice.edu/comp360/lectures/fall2008/TurtleFractalsL2New.pdf#page=3
         for (int i = 1; i <= 8; ++i)
         {
            turtle.forward(1);
            turtle.turn(60);
            turtle.resize(0.99);
            turtle.forward(1.0/4.0);
            turtle.turn(45);
            turtle.resize(0.99);
            turtle.forward(1.0/3.0);
            turtle.turn(-90);
            turtle.resize(0.99);
            turtle.forward(3.0/5.0);
            turtle.turn(30);
            turtle.resize(0.99);
         }
      }
/*
      for (int i = 1; i <= 8; ++i)
      {
         turtle.forward(1);
         turtle.turn(60);
         turtle.forward(1.0/4.0);
         turtle.turn(45);
         turtle.forward(1.0/3.0);
         turtle.turn(-90);
         turtle.forward(3.0/5.0);
         turtle.turn(30);
      }
*/
/*
      for (int j = 1; j < 32; ++j)
      {
         for (int i = 1; i <= 8; ++i)
         {
            turtle.forward(1);
            turtle.turn(60);
            turtle.forward(1.0/4.0);
            turtle.turn(45);
            turtle.forward(1.0/3.0);
            turtle.turn(-90);
            turtle.forward(3.0/5.0);
            turtle.turn(30);
         }
         turtle.resize(0.9);
      }
*/
      scene.addPosition(new Position(turtleModel));

      // Give each model a random color.
      for (final Position p : scene.positionList)
      {
         ModelShading.setRandomColor(p.getModel());
      }

      // Make the interactive models invisible, except for the current model.
      numberOfInteractiveModels = scene.positionList.size();
      for (final Position p : scene.positionList)
      {
         p.visible = false;
      }
      currentModel = 0;
      scene.getPosition(currentModel).visible = true;
      interactiveModelsAllVisible = false;
      debugWholeScene = true;

      renderer.pipeline.Rasterize.doClipping = true;


      // Create a FrameBufferPanel that holds a FrameBuffer.
      final int width  = 1024;
      final int height = 1024;
      fbp = new FrameBufferPanel(width, height, Color.black);

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Renderer 4 - Turtle Geometry");
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
         () -> new InteractiveTurtleModels_R4()
      );
   }
}
