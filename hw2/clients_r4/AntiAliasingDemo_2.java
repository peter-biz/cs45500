/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.ModelShading;
import renderer.pipeline.*;
import renderer.framebuffer.*;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

/**
   This program renders a fan of lines three ways, one way using
   the rasterizer without any anti-aliasing, a second way using
   the raterizer with a simple anti-aliasing algorithm, and a third
   way that combines anti-aliasing with gamma correction.

   Use the 'a' key (or a mouse click) to switch anti-aliasing off
   and on. Use the 'g' key (or a mouse control-click) to switch
   gamma crrection off and on.

   Notice that the lines look best when anti-aliasing is combined
   with gamma corrrection. The Java API for anti-aliasing seems to
   lack the abiltity to combine anti-aliasing with gamm correction.

   To see how the anti-aliasing works, look at the rendered
   images using a pixel magnifying program.
*/
public class AntiAliasingDemo_2 implements KeyListener, MouseListener, ComponentListener
{
   private final Scene scene;          // The event handlers need
   private final JFrame jf;            // access to these fields.
   private final FrameBufferPanel fbp;

   private static final int numberOfLines = 30;

   private static Color backgroundColor  = Color.green;
   private static Color foregroundColor1 = Color.red;
   private static Color foregroundColor2 = Color.red; // try making this different

   private boolean takeScreenshot = false;
   private int screenshotNumber = 0;

   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public AntiAliasingDemo_2()
   {
      // Create the Scene object that we shall render
      scene = new Scene();

      // Create a Model object.
      final Model model = new Model("line-fan");

      // Add the model to the Scene.
      scene.addPosition(new Position(model));

      // Create a fan of lines, coming out of (-1, -1, -1), in
      // the square with -1 <= x <= 1, -1 <= y <= 1, and z = -1.
      model.addVertex( new Vertex(-1, -1, -1) );
      model.addColor( foregroundColor1,
                      foregroundColor2 );

      for (int i = 0; i < numberOfLines; ++i)
      {
         model.addVertex(new Vertex(1, -1 + 2*(double)i/(double)numberOfLines, -1));
         model.addPrimitive(new LineSegment(0, 2*i+1, 0, 1));  // slope < 1
         model.addVertex(new Vertex(-1 + 2*(double)i/(double)numberOfLines, 1, -1));
         model.addPrimitive(new LineSegment(0, 2*i+2, 0, 1));  // slope > 1
      }
      model.addVertex( new Vertex(1, 1, -1) );
      model.addPrimitive(new LineSegment(0, 2*numberOfLines+1, 0, 1)); // 45 degree line


      // Define initial dimensions for a FrameBuffer.
      final int width  = 900;
      final int height = 900;

      // Create a FrameBufferPanel that will hold a FrameBuffer.
      fbp = new FrameBufferPanel(width, height);
      final FrameBuffer fb = fbp.getFrameBuffer();
      fb.setBackgroundColorFB(backgroundColor);
      fb.vp.setBackgroundColorVP(backgroundColor);
      fb.clearFB();

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Renderer 4 - Anti-Aliasing Demo 2");
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.getContentPane().add(fbp, BorderLayout.CENTER);
      jf.pack();
      jf.setLocationRelativeTo(null);
      jf.setVisible(true);

      // Register this object as the event listener for JFrame events.
      jf.addKeyListener(this);
      jf.addMouseListener(this);
      jf.addComponentListener(this);

      print_help_message();
   }


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
      else if ('d' == c && e.isAltDown())
      {
         System.out.println(scene.getPosition(0).getModel());
      }
      else if ('d' == c)
      {
         scene.debug = ! scene.debug;
         Rasterize.debug = scene.debug;
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
      else if ('c' == c)
      {
         // Change the solid random color of the line fan.
         ModelShading.setRandomColor(scene.getPosition(0).getModel());
         final Color color = scene.getPosition(0).getModel().colorList.get(0);
         System.out.println("New foreground color = " + color);
      }
      else if ('C' == c)
      {
         // Change each color in the line fan to a random color.
         ModelShading.setRandomColors(scene.getPosition(0).getModel());
      }
      else if ('e' == c && e.isAltDown())
      {
         // Change the random color of each vertex of the line fan.
         ModelShading.setRandomVertexColors(scene.getPosition(0).getModel());
      }
      else if ('e' == c)
      {
         // Change the solid random color of each edge of the line fan.
         ModelShading.setRandomPrimitiveColors(scene.getPosition(0).getModel());
      }
      else if ('E' == c)
      {
         // Change the random color of each end of each edge of the line fan.
         ModelShading.setRainbowPrimitiveColors(scene.getPosition(0).getModel());
      }
      else if ('b' == c)
      {
         // Change the background color.
         backgroundColor = ModelShading.randomColor();
         System.out.println("New background color = " + backgroundColor);
      }
      else if ('+' == c)
      {
         takeScreenshot = true;
      }

      // Render again.
      final FrameBuffer fb = fbp.getFrameBuffer();
      fb.setBackgroundColorFB(backgroundColor);
      fb.vp.setBackgroundColorVP(backgroundColor);
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


   // Implement the MouseListener interface.
   @Override public void mousePressed(MouseEvent e){}
   @Override public void mouseReleased(MouseEvent e){}
   @Override public void mouseEntered(MouseEvent e){}
   @Override public void mouseExited(MouseEvent e){}
   @Override public void mouseClicked(MouseEvent e)
   {
      //System.out.println( e );

      if (! e.isControlDown())
      {
         Rasterize.doAntiAliasing = ! Rasterize.doAntiAliasing;
         System.out.print("Anti-aliasing is turned ");
         System.out.println(Rasterize.doAntiAliasing ? "On" : "Off");
      }
      else if (e.isControlDown())
      {
         Rasterize.doGamma = ! Rasterize.doGamma;
         System.out.print("Gamma correction is turned ");
         System.out.println(Rasterize.doGamma ? "On" : "Off");
      }

      // Render again.
      final FrameBuffer fb = fbp.getFrameBuffer();
      fb.clearFB();
      Pipeline.render(scene, fb);
      fbp.repaint();
   }


   // Implement the ComponentListener interface.
   @Override public void componentMoved(ComponentEvent e){}
   @Override public void componentHidden(ComponentEvent e){}
   @Override public void componentShown(ComponentEvent e){}
   @Override public void componentResized(ComponentEvent e)
   {
      //System.out.println( e );

      // Get the new size of the FrameBufferPanel.
      final int w = fbp.getWidth();
      final int h = fbp.getHeight();

      // Create a new FrameBuffer that fits the FrameBufferPanel.
      final FrameBuffer fb = new FrameBuffer(w, h, backgroundColor);
      fb.clearFB();
      fbp.setFrameBuffer(fb);
      Pipeline.render(scene, fb);
      fbp.repaint();
   }


   private void print_help_message()
   {
      System.out.println("Use the 'd' key to toggle debugging information on and off.");
      System.out.println("Use the 'a' key to toggle anti-aliasing on and off.");
      System.out.println("Use the 'g' key to toggle gamma correction on and off.");
      System.out.println("Use mouse click to toggle anti-aliasing on and off.");
      System.out.println("Use mouse control-click to toggle gamma correction on and off.");
      System.out.println("Use the 'c' key to change the random line color.");
      System.out.println("Use the 'C' key to randomly change model's colors.");
      System.out.println("Use the 'e' key to change the random solid edge colors.");
      System.out.println("Use the 'E' key to change the random edge colors.");
      System.out.println("Use the 'b' key to change the random background color.");
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
         () -> new AntiAliasingDemo_2()
      );
   }//main()
}
