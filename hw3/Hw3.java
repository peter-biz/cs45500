/*
   Course: CS45500
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 3
*/

import renderer.scene.*;
import renderer.scene.util.ModelShading;
import renderer.scene.util.DrawSceneGraph;
import renderer.models_L.*;
import renderer.pipeline.*;
import renderer.framebuffer.*;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.function.BiPredicate;
import javax.swing.JCheckBox;

/**
   Implement a GUI with five draggable shapes.
*/
public class Hw3
{
   // The GUI state data (the Model part of MVC).
   private final int PIXELS_PER_UNIT = 80; // Pixels per unit of camera space.
   private final JFrame jf;
   private final FrameBufferPanel fbp;
   private Scene scene = null;
   private boolean debugMouse = false;
   private boolean debugMouseExtra = false;
   private boolean debugComponent = false;
   private int screenshotNumber = 0;
   private double right  =  5; // Initial size of Camera's view rectangle.
   private double left   = -right;
   private double top    =  right;
   private double bottom = -top;
   private boolean mouseInside; // Used to detect mouse exit.
   private int mouseX_fb;  // Used for mouse dragging.
   private int mouseY_fb;
   private final double[] centerX = new double[5];  // Data for the five movable shapes.
   private final double[] centerY = new double[5];
   private final double[]  radius = new double[5];
   private final boolean[]    hit = new boolean[5];
   @SuppressWarnings({"unchecked", "rawtypes"})
   private final BiPredicate<Integer,Vertex>[] hitFn =  // One hit function for each shape.
                (BiPredicate<Integer,Vertex>[])new BiPredicate[5]; // Need to use a raw type!
   private int[] colorChoiceIndex = {0, 1, 2, 3, 4}; // red, blue, green, cyan, magenta
   private Color[] color = {Color.red, Color.blue, Color.green, Color.cyan,
                            Color.magenta, Color.pink, Color.yellow};
   private int currentModel = 0; // Which shape is selected by a radio button.

   int screenshot_num = 000; // Used to number screenshots.


   private Hw3()
   {
      // Initialize the program's state.
      centerX[0] =  0.0; centerY[0] =  0.0;  // Square_1
      centerX[1] = -2.5; centerY[1] = -2.5;  // Square_2
      centerX[2] =  2.5; centerY[2] =  2.5;  // Square_3
      centerX[3] =  2.5; centerY[3] = -2.5;  // Diamond
      centerX[4] = -2.5; centerY[4] =  2.5;  // Circle

      radius[0] = 0.5;  // Square_1
      radius[1] = 1.0;  // Square_2
      radius[2] = 1.5;  // Square_3
      radius[3] = 1.5;  // Diamond
      radius[4] = 1.5;  // Circle

      hit[0] = false;
      hit[1] = false;
      hit[2] = false;
      hit[3] = false;
      hit[4] = false;

      // The hit functions in an array of lambda expressions.
      hitFn[0] = (i, v) -> Math.abs(centerX[i] - v.x) < radius[0]  // Square_1
                        && Math.abs(centerY[i] - v.y) < radius[0];
      hitFn[1] = (i, v) -> Math.abs(centerX[i] - v.x) < radius[1]  // Square_2
                        && Math.abs(centerY[i] - v.y) < radius[1];
      hitFn[2] = (i, v) -> Math.abs(centerX[i] - v.x) < radius[2]  // Square_3
                        && Math.abs(centerY[i] - v.y) < radius[2];
      hitFn[3] = (i, v) -> Math.abs(centerX[i] - v.x)              // Diamond
                         + Math.abs(centerY[i] - v.y) < radius[3];
      hitFn[4] = (i, v) -> distance(centerX[i], centerY[i], v) < radius[4]; // Circle


      // Build this program's scene graph.
      scene = new Scene("Hw3",
                        Camera.projOrtho(left, right, bottom, top));

      scene.addPosition(new Position(new Square(radius[0]),
                                     "Square_1",
                                     new Vector(centerX[0], centerY[0], 0)),
                        new Position(new Square(radius[1]),
                                     "Square_2",
                                     new Vector(centerX[1], centerY[1], 0)),
                        new Position(new Square(radius[3]),
                                     "Square_3",
                                     new Vector(centerX[2], centerY[2], 0)),
                        new Position(new Circle(radius[3], 4),
                                     "Diamond",
                                     new Vector(centerX[3], centerY[3], 0)),
                        new Position(new Circle(radius[4], 64),
                                     "Circle",
                                     new Vector(centerX[4], centerY[4], 0)));

      ModelShading.setColor(scene.getPosition(0).getModel(), color[colorChoiceIndex[0]]);
      ModelShading.setColor(scene.getPosition(1).getModel(), color[colorChoiceIndex[1]]);
      ModelShading.setColor(scene.getPosition(2).getModel(), color[colorChoiceIndex[2]]);
      ModelShading.setColor(scene.getPosition(3).getModel(), color[colorChoiceIndex[3]]);
      ModelShading.setColor(scene.getPosition(4).getModel(), color[colorChoiceIndex[4]]);

      // Draw a picture of the scene's tree (DAG) data structure.
      //DrawSceneGraph.drawVertexList = false;
      //DrawSceneGraph.draw(scene, "Hw3_SG");


      // Create the GUI.
      // Create a FrameBufferPanel that holds a FrameBuffer.
      final int fbWidth  = (int)(2 * right * PIXELS_PER_UNIT);
      final int fbHeight = (int)(2 *   top * PIXELS_PER_UNIT);
      fbp = new FrameBufferPanel(fbWidth, fbHeight);

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Hw3");
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Place the FrameBufferPanel in the JFrame.
      jf.add(fbp, BorderLayout.CENTER);

      // Create a panel to hold the gui components.
      final Box eastPanel = new Box(BoxLayout.Y_AXIS);
      jf.add(eastPanel, BorderLayout.EAST);

      // Fill the gui panel.
      // Label for the radio buttons.
      final JLabel jLabel_1 = new JLabel("Choose Model");
      jLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
      jLabel_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));

      // The group of radio buttons.
      final JRadioButton model0Button = new JRadioButton("Square 1");
      final JRadioButton model1Button = new JRadioButton("Square 2");
      final JRadioButton model2Button = new JRadioButton("Square 3");
      final JRadioButton model3Button = new JRadioButton("Diamond");
      final JRadioButton model4Button = new JRadioButton("Circle");
      model0Button.setSelected(true);
      // Group the radio buttons together.
      final ButtonGroup group = new ButtonGroup();
      group.add(model0Button);
      group.add(model1Button);
      group.add(model2Button);
      group.add(model3Button);
      group.add(model4Button);
      // Put the radio buttons in a column in their own panel.
      final JPanel radioPanel = new JPanel(new GridLayout(0, 1));
      radioPanel.add(model0Button);
      radioPanel.add(model1Button);
      radioPanel.add(model2Button);
      radioPanel.add(model3Button);
      radioPanel.add(model4Button);
      radioPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); //top,left,bottom,right

      // Label for the combo box.
      final JLabel jLabel_2 = new JLabel("Choose Color");
      jLabel_2.setAlignmentX(Component.CENTER_ALIGNMENT);
      jLabel_2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));

      // Combo box of color names.
      final JComboBox<String> colorNames = new JComboBox<>(
                                             new String[]{"Red",
                                                          "Blue",
                                                          "Green",
                                                          "Cyan",
                                                          "Magenta",
                                                          "Pink",
                                                          "Yellow"});
      colorNames.setSelectedIndex(0);
      colorNames.setAlignmentX(Component.CENTER_ALIGNMENT);
      colorNames.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); //top,left,bottom,right
      colorNames.setMaximumSize(colorNames.getPreferredSize()); // Try commenting this out!

      // Create the screenshot button.
      final JButton screenshot = new JButton("Screenshot");
      screenshot.setAlignmentX(Component.CENTER_ALIGNMENT);
      screenshot.setBorder(BorderFactory.createEmptyBorder(5,20,5,20)); //top,left,bottom,right
      screenshot.setMaximumSize(screenshot.getPreferredSize());
      
      // Create the reset button.
      final JButton reset = new JButton("Reset");
      reset.setAlignmentX(Component.CENTER_ALIGNMENT);
      reset.setBorder(BorderFactory.createEmptyBorder(5,20,5,20)); //top,left,bottom,right
      reset.setPreferredSize(new java.awt.Dimension(100, 30)); // Set preferred size
      reset.setMaximumSize(reset.getPreferredSize());


      // Add all these components to the east panel.
      eastPanel.add(Box.createVerticalGlue());
      eastPanel.add(jLabel_1);
      eastPanel.add(radioPanel);
      eastPanel.add(Box.createVerticalGlue());
      eastPanel.add(jLabel_2);
      eastPanel.add(colorNames);
      eastPanel.add(Box.createVerticalGlue());
      eastPanel.add(screenshot);
      eastPanel.add(Box.createVerticalGlue());
      eastPanel.add(reset);
      eastPanel.add(Box.createVerticalGlue());

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
            }
            else if ('d' == c)
            {
               scene.debug = ! scene.debug;
            }
            else if ('i' == c)
            {
               debugMouse = ! debugMouse;
               System.out.print("Mouse event debugging information is turned ");
               System.out.println(debugMouse ? "on." : "off.");
            }
            else if ('j' == c)
            {
               debugMouseExtra = ! debugMouseExtra;
               System.out.print("Extra mouse event debugging information is turned ");
               System.out.println(debugMouse ? "on." : "off.");
            }
            else if ('c' == c)
            {
               debugComponent = ! debugComponent;
               System.out.print("Component event debugging information is turned ");
               System.out.println(debugComponent ? "on." : "off.");
            }
            else if ('s' == c) // Display shape information.
            {
               displayShapes();
            }
            else if ('w' == c) // Display window information.
            {
               displayWindow();
            }
            else if ('=' == c) // Reset the translations of the 5 shapes.
            {
               // Reset centerX[] and centerY[] for each shape.
               centerX[0] =  0.0; centerY[0] =  0.0;  // Square_1
               centerX[1] = -2.5; centerY[1] = -2.5;  // Square_2
               centerX[2] =  2.5; centerY[2] =  2.5;  // Square_3
               centerX[3] =  2.5; centerY[3] = -2.5;  // Diamond
               centerX[4] = -2.5; centerY[4] =  2.5;  // Circle
               

               // Reset each translation vector.
               for (int i = 0; i < 5; i++) 
               {
                   Position p = scene.getPosition(i);
                   p.translate(centerX[i], centerY[i], 0);
                   scene.setPosition(i, p);
               }

            }
            else if ('+' == c) // Take a screenshot.
            {
               // Save the FrameBuffer to a file.
               try
               {
                  final FrameBuffer fb = fbp.getFrameBuffer();
                  // Check if there is already a screenshot.png file, if so, increment the number.
                  String filename = String.format("hw3/Screenshot%03d.png", screenshot_num);
                  // Check if the file exists.
                  fb.dumpFB2File(filename, "PNG");
                  System.out.println("Screenshot saved as " + filename);
                  screenshot_num++;

               }
               catch (Exception ex)
               {
                  System.err.println("Error saving screenshot: " + ex.getMessage());
                  ex.printStackTrace();
               }


            }

            // Render again.
            final FrameBuffer fb = fbp.getFrameBuffer();
            fb.clearFB();
            Pipeline.render(scene, fb);
            fbp.repaint();
         }
      });


      // An inner class for the mouse handlers.
      class MouseHandler implements MouseListener, MouseMotionListener
      {
         // Implement the MouseListener interface.
         @Override public void mouseEntered(MouseEvent e)
         {
            System.out.println( e );

            // Indicates that the mouse is inside the window.
            mouseInside = true;

            //if (debugMouse) TODO: do i need this?
         }
         @Override public void mouseExited(MouseEvent e)
         {
            System.out.println( e );

            // Indicates that the mouse is outside the window.
            mouseInside = false;

            // if (debugMouse) TODO: do i need this?

            // Stop dragging the shapes (set each hit[] to false).
            for(int i = 0; i < hit.length; ++i)
            {
               hit[i] = false;
            }


         }
         @Override public void mousePressed(MouseEvent e)
         {
            System.out.println( e );

            jf.requestFocus(); // This is important.

            // Get the mouse location in the FrameBuffer and convert
            // it to a point in the Camera's image-rectangle.
            // Log the results if debugMouse is true.
            mouseX_fb = e.getX();
            mouseY_fb = e.getY();

            // if (debugMouse) TODO: do i need this?

            // Calculate the mouse location in the image-plane.
            final Vertex vertex = pixel2camera(fbp.getFrameBuffer(),
                                               mouseX_fb,
                                               mouseY_fb);


            // Determine which shapes have been hit by this
            // mouse click. Set their entry in hit[] to true.
            // Log the results if debugMouse is true.
            for (int i = 0; i < hit.length; ++i)
            {
               hit[i] = hitFn[i].test(i, vertex);
               if (hit[i]) // if debugMouse && hit[i] TODO: do i need this?
               {
                  System.out.printf("Shape %d was hit.\n", i);
               }
            }



         }
         @Override public void mouseReleased(MouseEvent e)
         {
            System.out.println( e );

            // Stop dragging the shapes (set each hit[] to false).

            for(int i = 0; i < hit.length; ++i)
            {
               hit[i] = false;
            }



         }
         @Override public void mouseClicked(MouseEvent e){}

         // Implement the MouseMotionListener interface.
         @Override public void mouseMoved(MouseEvent e)
         {
            //System.out.println( e );
            if (debugMouseExtra)
            {
               // Get the mouse location in the FrameBuffer.
               final int mouseOverX_fb = e.getX();
               final int mouseOverY_fb = e.getY();
               // Calculate the mouse location in the image-plane.
               final Vertex vertex = pixel2camera(fbp.getFrameBuffer(),
                                                  mouseOverX_fb,
                                                  mouseOverY_fb);
               System.out.printf("(mouseX_fb, mouseY_fb) = (%4d, %4d) ===> "
                               + "(x_ip, y_ip) = (% .4f, % .4f)\n",
                                 mouseOverX_fb,
                                 mouseOverY_fb,
                                 vertex.x,
                                 vertex.y);
            }
         }
         @Override public void mouseDragged(MouseEvent e)
         {
            System.out.println( e );

            if (mouseInside)
            {
               // First, determine how much the mouse has moved.
               // Get the current mouse location (in the MouseEvent
               // object), then find the horizontal and vertical
               // displacement of the current mouse location from the
               // last mouse location, then divide the displacements
               // by the scaling factor of how many framebuffer pixels
               // there are per unit of the image-plane.
               final int currentX = e.getX();
               final int currentY = e.getY();


               // Calculate the horizontal and vertical displacements
               final double dx = (currentX - mouseX_fb) / (double)PIXELS_PER_UNIT;
               final double dy = (mouseY_fb - currentY) / (double)PIXELS_PER_UNIT;



               // Move (in the image-plane) each shape that
               // was hit by the last mouse pressed event.
               for (int i = 0; i < hit.length; ++i)
               {
                  if ( hit[i] )
                  {
                     // Update the center coordinates of the shape
                     centerX[i] += dx;
                     centerY[i] += dy;

                     // Update the shape's position in the scene graph
                     Position p = scene.getPosition(i);
                     p.translate(centerX[i], centerY[i], 0);
                     scene.setPosition(i, p);
                  }
               }

               // Update the current mouse location.
               mouseX_fb = e.getX();
               mouseY_fb = e.getY();

               // Render again.
               final FrameBuffer fb = fbp.getFrameBuffer();
               fb.clearFB();
               Pipeline.render(scene, fb);
               fbp.repaint();
            }
         }
      }
      // Register the FrameBufferPanel as the source for mouse events!
      final MouseHandler mh = new MouseHandler();
      fbp.addMouseListener( mh );
      fbp.addMouseMotionListener( mh );


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
            System.out.println( e );

            if (debugComponent)
               System.out.printf("JFrame [w = %d, h = %d]: " +
                                 "FrameBufferPanel [w = %d, h = %d].\n",
                                 fbp.getTopLevelAncestor().getWidth(),
                                 fbp.getTopLevelAncestor().getHeight(),
                                 fbp.getWidth(),
                                 fbp.getHeight());

            // Get the new size of the FrameBufferPanel.
            final int w = fbp.getWidth();
            final int h = fbp.getHeight();

            // Set the left, right top, bottom parameters
            // for the Camera's view rectangle.
            right  =  (w / (double)PIXELS_PER_UNIT) / 2.0;
            left   =  (-w / (double)PIXELS_PER_UNIT) / 2.0;
            top    =  (h / (double)PIXELS_PER_UNIT) / 2.0;
            bottom =  (-h / (double)PIXELS_PER_UNIT) / 2.0;
            



            scene = scene.changeCamera(Camera.projOrtho(left, right,
                                                        bottom, top));

            // Create a new FrameBuffer that fits the new window size.
            final FrameBuffer fb = new FrameBuffer(w, h);
            fbp.setFrameBuffer(fb);
            Pipeline.render(scene, fb);
            fbp.repaint();
         }
      });


      // Define east panel event handlers (four of them).

      // Handel the radio buttons.
      //
      //  https://docs.oracle.com/javase/tutorial/uiswing/components/button.html#radiobutton
      //
      //  https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
      //
      final var modelHandler = new ActionListener()
      {
         // Implement the ActionListener interface.
         @Override public void actionPerformed(ActionEvent e)
         {
            System.out.println(e);

            // Get the selected shape from the radio buttons and
            // then update the color shown in the color combo box.
            // First, find out which shape is selected.
            final String cmd = e.getActionCommand();

            // Set the currentModel to the one in
            // the radio button's action command.
            if (cmd.equals("Square 1"))
            {
               currentModel = 0;
            }
            else if (cmd.equals("Square 2"))
            {
               currentModel = 1;
            }
            else if (cmd.equals("Square 3"))
            {
               currentModel = 2;
            }
            else if (cmd.equals("Diamond"))
            {
               currentModel = 3;
            }
            else if (cmd.equals("Circle"))
            {
               currentModel = 4;
            }


            // Update the color choice shown in the combo box.
            colorNames.setSelectedIndex(colorChoiceIndex[currentModel]);

         }
      };
      // Give each radio button the same event listener.
      model0Button.addActionListener(modelHandler);
      model1Button.addActionListener(modelHandler);
      model2Button.addActionListener(modelHandler);
      model3Button.addActionListener(modelHandler);
      model4Button.addActionListener(modelHandler);


      // Handel the combo box.
      //
      //  https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
      //
      colorNames.addActionListener(new ActionListener()
      {
         // Implement the ActionListener interface.
         @Override public void actionPerformed(ActionEvent e)
         {
            System.out.println(e);

            @SuppressWarnings("unchecked")
            final JComboBox<String> cb = (JComboBox<String>)e.getSource();
            final String colorName = (String)cb.getSelectedItem();

            // Use colorName to change the currentModel's color index
            // in colorChoiceIndex to the index of the selected color.
            if (colorName.equals("Red") )
            {
               colorChoiceIndex[currentModel] = 0;
            }
            else if (colorName.equals("Blue") )
            {
               colorChoiceIndex[currentModel] = 1;
            }
            else if (colorName.equals("Green") )
            { 
               colorChoiceIndex[currentModel] = 2;
            }  
            else if(colorName.equals("Cyan"))
            {
               colorChoiceIndex[currentModel] = 3;
            }
            else if(colorName.equals("Magenta"))
            {
               colorChoiceIndex[currentModel] = 4;
            }
            else if(colorName.equals("Pink"))
            {
               colorChoiceIndex[currentModel] = 5;
            }
            else if(colorName.equals("Yellow"))
            {
               colorChoiceIndex[currentModel] = 6;
            }

            // Set the color of the current model.
            ModelShading.setColor(scene.getPosition(currentModel).getModel(),
                                  color[colorChoiceIndex[currentModel]]);


            // Render again.
            final FrameBuffer fb = fbp.getFrameBuffer();
            fb.clearFB();
            Pipeline.render(scene, fb);
            fbp.repaint();
         }
      });


      // Handel the screenshot button.
      // The event listener is an anonymous inner class.
      screenshot.addActionListener(new ActionListener()
      {
         // Implement the ActionListener interface.
         @Override public void actionPerformed(ActionEvent e)
         {
            System.out.println( e );

            // Save the FrameBuffer to a file.
            try
            {
               final FrameBuffer fb = fbp.getFrameBuffer();
               // Check if there is already a screenshot.png file, if so, increment the number.
               String filename = String.format("hw3/Screenshot%03d.png", screenshot_num);
               // Check if the file exists.
               fb.dumpFB2File(filename, "PNG");
               System.out.println("Screenshot saved as " + filename);
               screenshot_num++;
            }
            catch (Exception ex)
            {
                System.err.println("Error saving screenshot: " + ex.getMessage());
                ex.printStackTrace();
            }
         }
      });


      // Handel the reset button.
      // The event listener is an anonymous inner class.
      reset.addActionListener(new ActionListener()
      {
         // Implement the ActionListener interface.
         @Override public void actionPerformed(ActionEvent e)
         {
            System.out.println( e );

            // Reset centerX[] and centerY[] for each shape.
            centerX[0] =  0.0; centerY[0] =  0.0;  // Square_1
            centerX[1] = -2.5; centerY[1] = -2.5;  // Square_2
            centerX[2] =  2.5; centerY[2] =  2.5;  // Square_3
            centerX[3] =  2.5; centerY[3] = -2.5;  // Diamond
            centerX[4] = -2.5; centerY[4] =  2.5;  // Circle


            // Reset each translation vector.
            for (int i = 0; i < 5; i++) 
            {
                Position p = scene.getPosition(i);
                p.translate(centerX[i], centerY[i], 0);
                scene.setPosition(i, p);
            }

            // Render again.
            final FrameBuffer fb = fbp.getFrameBuffer();
            fb.clearFB();
            Pipeline.render(scene, fb);
            fbp.repaint();
         }
      });


      jf.requestFocus(); // This is important.
      print_help_message();
   }


   /**
      Convert the coordinates of a point (pixel) in the framebuffer
      to its coordinates as a point in the camera's view-rectangle.

      @param fb    FrameBuffer that is the source of the pixel
      @param x_fb  x-coordinate of a pixel in the FrameBuffer
      @param y_fb  y-coordinate of a pixel in the FrameBuffer
      @return a Vertex holding the view-rectangle coordinates of (x_fb, y_fb)
   */
   public Vertex pixel2camera(FrameBuffer fb, int x_fb, int y_fb)
   {
      // First find the "center" of the framebuffer (which
      // represents the origin (0,0) in the image-plane),
      // then find the displacements of x_fb and y_fb from
      // the framebuffer's center, then divide the displacements
      // by the scaling factor of how many framebuffer pixels
      // there are per unit of the image-plane, PIXELS_PER_UNIT.
      final int xOrigin_fb = fb.width  / 2;
      final int yOrigin_fb = fb.height / 2;
      final double deltaX_fb = (double)(x_fb - xOrigin_fb);
      final double deltaY_fb = (double)(yOrigin_fb - y_fb);
      final double xCoord_ip = deltaX_fb / (double)PIXELS_PER_UNIT;
      final double yCoord_ip = deltaY_fb / (double)PIXELS_PER_UNIT;
      return new Vertex(xCoord_ip, yCoord_ip, 0);
   }


   /**
      Compute the distance between two points
      in the camera's image-plane.

      @param x  x-coordinate of a point in the camera's image-plane
      @param y  y-coordinate of a point in the camera's image-plane
      @param v  a Vertex in the camera's image-plane
      @return the distance in the image-plane between (x, y) and (v.x, v.y)
   */
   public double distance(double x1, double y1, Vertex v2)
   {
      return Math.sqrt( (v2.x - x1)*(v2.x - x1)
                      + (v2.y - y1)*(v2.y - y1) );
   }


   // Display in the console window information about the five shapes.
   public void displayShapes()
   {
      for (int i = 0; i < scene.positionList.size(); ++i)
      {
         System.out.println("Information for Model number = " + i);
         System.out.println("   name = " + scene.getPosition(i).name);
         System.out.println("   centerX_ip = " + centerX[i]);
         System.out.println("   centerY_ip = " + centerY[i]);
         System.out.println("   hit = " + hit[i]);
         System.out.println("   colorChoiceIndex = " + colorChoiceIndex[i]);
         System.out.println("   color = " + color[colorChoiceIndex[i]]);
      }
      System.out.println("The current Model is = " + currentModel);
   }


   // Display in the console window
   // information about this program's window.
   public void displayWindow()
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
   }


   // Display in the console window this program's help message.
   public void print_help_message()
   {
      System.out.println("Use the 'd' key to toggle renderer debugging information on and off.");
      System.out.println("Use the 'i' key to toggle mouse event debugging information on and off.");
      System.out.println("Use the 'j' key to toggle extra mouse event debugging information on and off.");
      System.out.println("Use the 'c' key to toggle component event debugging information on and off.");
      System.out.println("Use the 's' key to show shape data.");
      System.out.println("Use the 'w' key to show window data.");
      System.out.println("Use the '=' key to reset the location of the shapes.");
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
         () -> new Hw3()
      );
   }
}
