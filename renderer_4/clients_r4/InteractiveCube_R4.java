/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.framebuffer.FrameBufferPanel;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
<pre>{@code
                  y
                  |
                  |
                  | v[4]
                1 +---------------------+ v[5]=(1,1,0)
                 /|                    /|
                / |                   / |
               /  |                  /  |
              /   |                 /   |
             /    |                /    |
       v[7] +---------------------+ v[6]|
            |     |               |     |
            |     |               |     |
            |     | v[0]          |     | v[1]
            |     +---------------|-----+------> x
            |    /                |    /1
            |   /                 |   /
            |  /                  |  /
            | /                   | /
            |/                    |/
          1 +---------------------+
           /v[3]=(0,0,1)          v[2]=(1,0,1)
          /
         /
        z
}</pre>
   Render a wireframe cube.
*/
public class InteractiveCube_R4 extends InteractiveAbstractClient_R4
{
   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public InteractiveCube_R4()
   {
      scene = new Scene("InteractiveCube_R4");

      // Create a Model object to hold the geometry.
      final Model model = new Model("cube");

      // Create the geometry for the Model.
      // Vertices.
      model.addVertex(new Vertex(0.0, 0.0, 0.0), // four vertices around the bottom face
                      new Vertex(1.0, 0.0, 0.0),
                      new Vertex(1.0, 0.0, 1.0),
                      new Vertex(0.0, 0.0, 1.0),
                      new Vertex(0.0, 1.0, 0.0), // four vertices around the top face
                      new Vertex(1.0, 1.0, 0.0),
                      new Vertex(1.0, 1.0, 1.0),
                      new Vertex(0.0, 1.0, 1.0));

      // Create three colors, one color for the top edges,
      // one color for the bottom edges, and
      // one color for the vertical edges.
      model.addColor(new Color(255,  0,   0 ),  // red, bottom
                     new Color( 0,  255,  0 ),  // green, top
                     new Color( 0,   0,  255)); // blue, vertical

      // Lines segments.
      model.addPrimitive(new LineSegment(0, 1, 0),  // bottom face
                         new LineSegment(1, 2, 0),  // bottom face
                         new LineSegment(2, 3, 0),  // bottom face
                         new LineSegment(3, 0, 0),  // bottom face
                         new LineSegment(4, 5, 1),  // top face
                         new LineSegment(5, 6, 1),  // top face
                         new LineSegment(6, 7, 1),  // top face
                         new LineSegment(7, 4, 1),  // top face
                         new LineSegment(0, 4, 2),  // back face
                         new LineSegment(1, 5, 2),  // back face
                         new LineSegment(3, 7, 2),  // front face
                         new LineSegment(2, 6, 2)); // front face

      // Create a Position for the Model,
      // pushed away from where the camera is.
      deltaX[0] =  0;
      deltaY[0] =  0;
      deltaZ[0] = -2;
      scene.addPosition(new Position(model,
                            new Vector(deltaX[0],
                                       deltaY[0],
                                       deltaZ[0])));

      renderer.pipeline.Rasterize.doClipping = true;
      displayTranslation = true;


      // Create a FrameBufferPanel that holds a FrameBuffer.
      final int width  = 1024;
      final int height = 1024;
      fbp = new FrameBufferPanel(width, height, Color.black);

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Renderer 4 - Interactive Cube");
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.getContentPane().add(fbp, BorderLayout.CENTER);
      jf.pack();
      jf.setLocationRelativeTo(null);
      jf.setVisible(true);

      // Register this object as the event listener for JFrame events.
      jf.addKeyListener(this);
      jf.addComponentListener(this);

      print_help_message();
      System.out.println();
      System.out.println(model);
   }


   // Change how the program prints help information.
   @Override protected void print_help_message()
   {
      System.out.println("Use the 'd/D' keys to toggle debugging information on and off.");
      System.out.println("Use the 'i' key to get information about the cube model.");
      System.out.println("Use the 'p' key to toggle between parallel and orthographic projection.");
      System.out.println("Use the x/X, y/Y, z/Z, keys to translate the cube along the x, y, z axes.");
      System.out.println("Use the 'm' key to toggle the display of cube's translation information.");
      System.out.println("Use the '=' key to reset the cube's translation.");
      System.out.println("Use the 'c' key to change the random solid cube color.");
      System.out.println("Use the 'C' key to randomly change cube's colors.");
      System.out.println("Use the 'e' key to change the random solid edge colors.");
      System.out.println("Use the 'E' key to change the random edge colors.");
      System.out.println("Use the 'Alt-e' key combination to change the random vertex colors.");
      System.out.println("Use the 'a' key to toggle anti-aliasing on and off.");
      System.out.println("Use the 'g' key to toggle gamma correction on and off.");
      System.out.println("Use the 'b' key to toggle line clipping on and off.");
      System.out.println("Use the 'P' key to convert the cube to a point cloud.");
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
         () -> new InteractiveCube_R4()
      );
   }
}
