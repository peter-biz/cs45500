/*
 * Renderer 1. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.framebuffer.*;
import renderer.pipeline.*;
import renderer.scene.util.DrawSceneGraph_with_Vertices_Colors;

import java.awt.Color;

/**
   This example shows how to build a simple,
   but not trivial, Scene data structure.
*/
public class Position_Model_Vertex_Color_LineSegment_ver1
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("Position_Model_Vertex_Color_LineSegment_ver1");

      // Use these four vertex objects
      // to build four different models.
      final Vertex v0 = new Vertex(0, 0, 0),
                   v1 = new Vertex(1, 0, 0),
                   v2 = new Vertex(1, 1, 0),
                   v3 = new Vertex(0, 1, 0);

      // Use these two color objects
      // to build the four models.
      final Color c0 = Color.red,
                  c1 = Color.blue;

      // Create four model objects.
      final Model model_0 = new Model("Model_0"); // square
      final Model model_1 = new Model("Model_1"); // Z
      final Model model_2 = new Model("Model_2"); // X
      final Model model_3 = new Model("Model_3");

      // Add the four vertex objects to
      // each of the four model objects.
      model_0.addVertex(v0, v1, v2, v3); // square
      model_1.addVertex(v0, v1, v2, v3); // Z
      model_2.addVertex(v0, v1, v2, v3); // X
      model_3.addVertex(v0, v1, v2, v3);

      // Add the two color objects to
      // each of the four model objects.
      model_0.addColor(c0, c1); // square
      model_1.addColor(c0, c1); // Z
      model_2.addColor(c0, c1); // X
      model_3.addColor(c0, c1);

      // Make the four models look different by
      // giving them different line segments.
      // Make the left edge red and the right edge blue.
      model_0.addPrimitive(new LineSegment(0, 1, 0, 1),  // square
                           new LineSegment(1, 2, 1, 1),
                           new LineSegment(2, 3, 1, 0),
                           new LineSegment(3, 0, 0, 0));

      model_1.addPrimitive(new LineSegment(3, 2, 0, 1),  // Z
                           new LineSegment(2, 0, 1, 0),
                           new LineSegment(0, 1, 0, 1));

      model_2.addPrimitive(new LineSegment(0, 2, 0, 1),  // X
                           new LineSegment(1, 3, 1, 0));

      model_3.addPrimitive(new LineSegment(0, 1, 0, 1),
                           new LineSegment(1, 2, 1, 1),
                           new LineSegment(3, 0, 0, 0),
                           new LineSegment(0, 2, 0, 1),
                           new LineSegment(1, 3, 1, 0));

      // Add the model objects to the scene by
      // placing each model in a position object.
      scene.addPosition(
               new Position(model_0, "p0", new Vector( 1,  1, -3)), // square
               new Position(model_1, "p1" ,new Vector(-2,  1, -3)), // Z
               new Position(model_2, "p2", new Vector( 1, -2, -3)), // X
               new Position(model_3, "p3", new Vector(-2, -2, -3)));

      final int width  = 600;
      final int height = 600;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.white);

      System.out.println( scene );
      scene.debug = true;
      Rasterize.debug = true;

      Pipeline.render(scene, fb);
      fb.dumpFB2File("Position_Model_Vertex_Color_LineSegment_ver1.ppm");

      // Draw a picture of the scene's tree data structure.
      DrawSceneGraph_with_Vertices_Colors.draw(scene, "Position_Model_Vertex_Color_LineSegment_ver1_SG_with_Vertices_Colors");
   }
}

/*
             Scene
            /     \
           /       \
     Camera         List<Position>
                   /       |   \  \------------------------\
                  /        |    \                           \
                 /         |     \-----\                     \
          Position      Position        Position              Position
          /  \           /    \             |   \                /    \
         /    \         /      \            |    \              /      \
  Vector    Model   Vector   Model        Vector  Model       Vector   Model
  / | \      / \    /| \     /   \        / | \    / \        / | \     | |
 x  y  z           x y  z   /     \      x  y  z             x  y  z    | |
 1  1 -3          -2 1 -3  /       \     1 -2 -3            -2 -2 -3    | |
                          /         \                                   | |
              List<Vertex>           List<Primitive>                    | |
             /  |     |   \              |     \    \                   | |
            /   |     |    \             |      \    \                  | |
           /    |     |     \            |       \    \                 | |
     Vertex  Vertex Vertex  Vertex   LineSegment  LS   LineSegment      | |
      /|\     /|\     /|\     /|\        |        |       \             | |
     x y z   x y z   x y z   x y z     int[2]    int[2]   int[2]        | |
     0 0 0   1 0 0   1 1 0   0 1 0     {3, 2}    {2, 0}   {0, 1}       /  |
                                                                      /   |
                                                                     /    |
                                            /-----------------------/     |
                                           /                              |
                               List<Vertex>                    List<Primitive>
                              /  |     |   \                  / |      |     | \
                             /   |     |    \                /  |      |     |  \
                            /    |     |     \              /   |      |     |   \
                      Vertex  Vertex Vertex  Vertex        LS   LS     LS    LS   LS
                       /|\     /|\     /|\     /|\        /     |      |     |     \
                      x y z   x y z   x y z   x y z    int[2] int[2] int[2] int[2] int[2]
                      0 0 0   1 0 0   1 1 0   0 1 0    {0, 1} {1, 2} {3, 0} {0, 2} {1, 3}
*/
