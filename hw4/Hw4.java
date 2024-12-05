/*
   Course: CS45500
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 4
*/

import renderer.scene.*;
import renderer.scene.util.ModelShading;
import renderer.scene.util.DrawSceneGraph;
import renderer.models_L.*;
import renderer.pipeline.*;
import renderer.framebuffer.*;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.Timer;

/**

*/
public class Hw4
{
   // The GUI state data (the Model part of MVC).
   private final int wSIZE = 800; // aspect ration = 2.0
   private final int hSIZE = 400;

   private double deltaAngle = 2.0;

   private int fps = 60;
   private Timer timer = null;
   private final ActionListener timerHandler;

   private Scene scene;
   private final Position pPos;
   private final Position nPos;
   private final Position wPos;

   private double pRotation = 0.0;
   private double nRotation = 0.0;
   private double wRotation = 0.0;

   private final double focalLength = 3.0; // Try making this 2 or 1.

   private double right  =  2; // Initial size of Camera's view rectangle.
   private double left   = -right;
   private double top    =  1;
   private double bottom = -top;

   private boolean perspective = true;
   private boolean animation = true;
   private boolean debug = false;
   private boolean showViewData = false;
   private boolean viewDataChanged = false;
   private boolean hideBackground = false;

   private int scrollBarValue = 50;

   private int screenshotNumber = 0;

   private final JFrame jf;
   private final FrameBufferPanel fbp;

   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public Hw4()
   {
      // Initialize the program's state.

      // Build this program's scene graph.
      scene = new Scene("Hw_4_soln",
                        Camera.projPerspective(-2.0,
                                                2.0,
                                               -1.0,
                                                1.0,
                                                focalLength)
                               .translate(0, 0, focalLength));

      // Create a model of a circle.
      final Model disk = new Disk(1.0, 10, 40);
      ModelShading.setColor(disk, Color.darkGray.darker());
      scene.addPosition(new Position(disk));

      // Create a model of an xy-axis.
      final Model axis = new Axes2D(-2.0, 2.0, -1.0, 1.0, 40, 2);
      ModelShading.setColor(axis, Color.red);
      scene.addPosition(new Position(axis));

      // Create the P.
      final Model p = new P();
      ModelShading.setColor(p, Color.yellow);
      scene.addPosition( new Position(p,
                                     "p0",
                                      Matrix.translate(-1.5, -0.5, 0)) );
      pPos = scene.getPosition(scene.positionList.size()-1);

      // Create the N.
      final Model n = new N();
      ModelShading.setColor(n, Color.magenta);
      scene.addPosition( new Position(n,
                                     "p1",
                                      Matrix.translate(-0.5, -0.5, 0)) );
      nPos = scene.getPosition(scene.positionList.size()-1);

      // Create the W.
      final Model w = new W();
      ModelShading.setColor(w, Color.green);
      scene.addPosition( new Position(w,
                                     "p2",
                                      Matrix.translate(0.5, -0.5, 0)) );
      wPos = scene.getPosition(scene.positionList.size()-1);

      // Draw a picture of the scene's tree (DAG) data structure.
      //DrawSceneGraph.drawVertexList = false;
      //DrawSceneGraph.draw(scene, "Hw_4_SG");


      // Create the GUI.
      


      // Create a FrameBufferPanel that holds a FrameBuffer.
      fbp = new FrameBufferPanel(wSIZE, hSIZE, Color.darkGray);
      fbp.getFrameBuffer().getViewport().setBackgroundColorVP(Color.black);

      // Create a JFrame that will hold the FrameBufferPanel
      // and the JScrollBar.
      jf = new JFrame("Renderer 9 - Hw 4");
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.add(fbp, BorderLayout.CENTER);

      // Create a vertical JScrollBar, with initial value 50, and
      // add it to the EAST region of the JFrame's BorderLayout.
      final JScrollBar jsb = new JScrollBar(JScrollBar.VERTICAL, 50, 1, 0, 100);
      jf.add(jsb, BorderLayout.EAST);



      // Make the gui visible.
      jf.pack();
      jf.setLocationRelativeTo(null);
      jf.setVisible(true);


      // Create and register all the event handlers
      // (the Controller part of MVC).


      // Register the JFrame as the source for key events.
      // The event listener is an anonymous inner class.
      jf.addKeyListener(new KeyListener()
      {
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
               System.out.println();
               System.out.println( scene );
            }
            else if ('d' == c)
            {
               debug = true; // debug one frame of animation
            }
            else if ('p' == c)
            {
               perspective = ! perspective;
               final String p = perspective ? "perspective" : "orthographic";
               System.out.println("Using " + p + " projection");
            }
            else if ('e' == c)
            {
               deltaAngle -= 0.2;
               System.out.println("degrees per frame = " + deltaAngle);
            }
            else if ('E' == c)
            {
               deltaAngle += 0.2;
               System.out.println("degrees per frame = " + deltaAngle);
            }
            else if ('r' == c)
            {
               fps -= 1;
               if (0 == fps) fps = 1;
               System.out.println("fps = " + fps);
               setFPS();
            }
            else if ('R' == c)
            {
               fps += 1;
               System.out.println("fps = " + fps);
               setFPS();
            }
            else if ('s' == c || 'S' == c)
            {
               animation = ! animation; // turn animation off and on
            }
            else if ('v' == c)
            {
               showViewData = ! showViewData;
            }
            else if ('m' == c)
            {
               hideBackground = ! hideBackground;
               scene.getPosition(0).visible = !hideBackground;
               scene.getPosition(1).visible = !hideBackground;
            }
            else if ('+' == c) // Take a screenshot.
            {
               // Save the FrameBuffer to a file.
               final FrameBuffer fb = fbp.getFrameBuffer();
               fb.dumpFB2File(String.format("Screenshot%03d.png",
                                            screenshotNumber),
                              "png");
               ++screenshotNumber;
               return;
            }


            if (showViewData && ('v' ==c
                              ||(('f' == c || 'F' ==c) && perspective)
                              || 'p' == c))
            {
               viewDataChanged = true;
            }

            setupViewing();
         }
      });


      // Register the JFrame as the source for component events.
      // The event listener is an anonymous inner class.
      jf.addComponentListener(new ComponentListener()
      {
         // Implement the ComponentListener interface.
         @Override public void componentMoved(ComponentEvent e){}
         @Override public void componentHidden(ComponentEvent e){}
         @Override public void componentShown(ComponentEvent e){}
         @Override public void componentResized(ComponentEvent e)
         {
            //System.out.println( e );

            // Get the new size of the FrameBufferPanel.
            final int wFB = fbp.getWidth();
            final int hFB = fbp.getHeight();

            // Create a new FrameBuffer that fits the FrameBufferPanel.
            final Color bg1 = fbp.getFrameBuffer().getBackgroundColorFB();
            final Color bg2 = fbp.getFrameBuffer().getViewport()
                                                  .getBackgroundColorVP();
            final FrameBuffer fb = new FrameBuffer(wFB, hFB, bg1);
            fb.vp.setBackgroundColorVP(bg2);
            fbp.setFrameBuffer(fb);

            viewDataChanged = true;

            setupViewing();
         }
      });


      // Register the JScrollBar as the source for adjustment events.
      // The event listener is an anonymous inner class.
      jsb.addAdjustmentListener(new AdjustmentListener() {
         @Override public void adjustmentValueChanged(AdjustmentEvent e) {
             // Get the new value of the JScrollBar.
             scrollBarValue = e.getValue();
     
             // Map 0-100 range to a larger vertical movement.
             double verticalShift = 4.0 * (scrollBarValue - 50) / 50.0;
     
             // Adjust the bottom and top of the view rectangle.
             bottom = -1.0 - verticalShift;
             top = 1.0 - verticalShift;
     
             System.out.println("Scrollbar value: " + scrollBarValue);
             System.out.println("Vertical Shift: " + verticalShift);
             System.out.println("Bottom: " + bottom);
             System.out.println("Top: " + top);
     
             // Trigger the setupViewing method to apply the changes.
             setupViewing();
         }
     });


      // Implement the ActionListener interface for the timer.
      // The event listener is an anonymous inner class.
      timerHandler = new ActionListener()
      {
         @Override public void actionPerformed(ActionEvent e)
         {
            //System.out.println( e );

            // Update the parameters for the next frame.
            pRotation += deltaAngle;
            nRotation += deltaAngle;
            wRotation += deltaAngle;

            setupViewing();
         }
      };
      timer = new Timer(1000/fps, timerHandler); // ActionListener
      timer.start();

      print_help_message();
   }


   /**
      Update the frames-per-second value.
   */
   private void setFPS()
   {
      timer.stop();
      if (fps > 0)
      {
         timer = new Timer(1000/fps, timerHandler);
         if (animation)
         {
            timer.start(); // Start the animation.
         }
      }
   }


   /**
      Get in one place the code to set up
      the view volume and the viewport.
   */
   private void setupViewing()
   {
      // Do part 1 of the assignment.
      // Set the view-rectangle and the viewport.

      // Get the size of the FrameBuffer.
      final FrameBuffer fb = fbp.getFrameBuffer();
      final int wFB = fb.width;
      final int hFB = fb.height;

      // Compute the framebuffer's aspect ratio.
      final double aspectRatioFB = (double)wFB / (double)hFB;



      // Values for the viewport.
      final int vp_ul_x = 0;
      final int vp_ul_y;
      final int wVP;
      final int hVP;
      // Values for the view-rectangle.
      final double left;
      final double right;
      final double bottom;
      final double top;

      // In each branch of this if-statement be sure to set a
      // value for each of the last seven variable declarations.
      if ( aspectRatioFB > 2.0 ) // crop
      {
          // Make the viewport all of the framebuffer.
          vp_ul_y = 0;
          wVP = wFB;
          hVP = hFB;
          
          // Use the view-rectangle, with AspectRatio==aspectRatioFB,
          // to crop the scene. Use scrollBarValue to determine vertical position.
          left   = -2.0;
          right  = 2.0;
          double newHeight = 4.0 / aspectRatioFB;
          
          // Calculate maximum allowed vertical shift to keep content visible
          double maxShift = (2.0 - newHeight) / 2.0;
          
          // Map scrollBarValue (0-100) to shift within safe bounds
          double verticalShift = ((scrollBarValue / 100.0) * (maxShift * 2)) - maxShift;
          
          // Apply bounded shift to view rectangle
          top    = (newHeight / 2.0) - verticalShift;
          bottom = (-newHeight / 2.0) - verticalShift;
      }
      else if ( aspectRatioFB < 2.0 ) // letterbox
      {
         // Letterbox a viewport with AspectRatio==2 within the framebuffer.
         // Use scrollBarValue to determine the letterbox's vertical position
         // Calculate viewport height to maintain 2:1 aspect ratio
         wVP = wFB;
         hVP = wFB/2;  

         // Calculate the maximum vertical space available
         int maxSpace = hFB - hVP;
         
         // Center the viewport vertically by default
         int centeredY = maxSpace / 2;
         
         // Apply scrollbar offset (-50% to +50% of available space)
         int offset = (int)(((scrollBarValue - 50) / 50.0) * (maxSpace / 2));
         
         // Ensure y-coordinate stays within bounds
         vp_ul_y = Math.max(0, Math.min(maxSpace, centeredY + offset));

         // No cropping in the view-rectangle
         left   = -2.0;
         right  = 2.0;
         bottom = -1.0;
         top    = 1.0;
      }
      else // aspectRatio == 2.0
      {
         // Make the viewport all of the framebuffer.
         vp_ul_y = 0;
         wVP = wFB;
         hVP = hFB;
         // No cropping in the view-rectangle.
         left   = -2.0;
         right  = 2.0;
         bottom =  -1.0;
         top    = 1.0;
      }

      // Set the default viewport in the framebuffer.
      fb.setViewport(vp_ul_x, vp_ul_y, wVP, hVP);

      // Set the camera's view-rectangle.
      if (perspective) // perspective projection
      {
         scene = scene.changeCamera(
                     Camera.projPerspective(left,
                                            right,
                                            bottom,
                                            top,
                                            focalLength)
                           .translate(0, 0, focalLength));
      }
      else // orthographic projection
      {
         scene = scene.changeCamera(
                     Camera.projOrtho(left,
                                      right,
                                      bottom,
                                      top));
      }


      // Do part 2 of the assignent.
      // Rotate the P, N, W models.

      // Rotate the letter P around the x-axis.
      




      // Rotate the letter N around the y-axis.




      // Rotate the letter W simultaneously around
      // the y-axis and the z-axis.





      if (animation)
      {
         timer.start(); // Start the animation.
      }
      else
      {
         timer.stop(); // Stop the animation.
      }

      if (showViewData && viewDataChanged)
      {
         displayViewDtata();
         viewDataChanged = false;
      }

      // Render again.
      fb.clearFB();
      fb.vp.clearVP();
      if (debug) // debug one frame of the animation
      {
         scene.debug = true;
         Clip.debug = true;
      }
      Pipeline.render(scene, fb);
      debug = false;
      scene.debug = false;
      Clip.debug = false;
      fbp.repaint();
   }


   private void displayViewDtata()
   {
      // Get the size of the JFrame.
      final int wJF = jf.getWidth();
      final int hJF = jf.getHeight();
      // Get the size of the FrameBufferPanel.
      final int wFBP = fbp.getWidth();
      final int hFBP = fbp.getHeight();
      // Get the size of the FrameBuffer.
      final int wFB = fbp.getFrameBuffer().getWidthFB();
      final int hFB = fbp.getFrameBuffer().getHeightFB();
      // Get the size of the Viewport.
      final int wVP = fbp.getFrameBuffer().getViewport().getWidthVP();
      final int hVP = fbp.getFrameBuffer().getViewport().getHeightVP();
      // Get the location of the Viewport in the FrameBuffer.
      final int vp_ul_x = fbp.getFrameBuffer().getViewport().vp_ul_x;
      final int vp_ul_y = fbp.getFrameBuffer().getViewport().vp_ul_y;
      // Get the size of the camera's view rectangle.
      final Camera c = scene.camera;
      final double wVR = c.right - c.left;
      final double hVR = c.top - c.bottom;
      // Compute all the aspect ratios.
      final double rJF  = (double)wJF/(double)hJF;
      final double rFBP = (double)wFBP/(double)hFBP;
      final double rFB  = (double)wFB/(double)hFB;
      final double rVP  = (double)wVP/(double)hVP;
      final double rC   = wVR / hVR;

      System.out.printf(
         "Window information:\n" +
          "            JFrame [w=%4d, h=%4d], aspect ratio = %.2f\n" +
          "  FrameBufferPanel [w=%4d, h=%4d], aspect ratio = %.2f\n" +
          "       FrameBuffer [w=%4d, h=%4d], aspect ratio = %.2f\n" +
          "          Viewport [w=%4d, h=%4d, x=%d, y=%d], aspect ratio = %.2f\n" +
          "            Camera [w=%.2f, h=%.2f], aspect ratio = %.2f\n",
          wJF, hJF, rJF,
          wFBP, hFBP, rFBP,
          wFB, hFB, rFB,
          wVP, hVP, vp_ul_x, vp_ul_y, rVP,
          wVR, hVR, rC);
      System.out.println( c );
   }


   // Display in the console window this program's help message.
   private void print_help_message()
   {
      System.out.println("Use the 'd' key to debug one frame of animation.");
      System.out.println("Use the 'Alt-d' key combination to print the Scene data structure.");
      System.out.println("Use the 'p' key to toggle between parallel and orthographic projection.");
      System.out.println("Use the 'm' key to toggle hiding the background models.");
      System.out.println("Use the 'v' key to toggle showing the view/window data.");
      System.out.println("Use the r/R keys to decrease/increase the frame rate.");
      System.out.println("Use the e/E keys to decrease/increase the degrees per frame.");
      System.out.println("Use the 's' key to stop/start the animation.");
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
         () -> new Hw4()
      );
   }
}
