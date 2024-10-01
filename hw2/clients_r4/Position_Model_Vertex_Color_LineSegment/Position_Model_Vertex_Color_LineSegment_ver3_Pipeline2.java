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
<p>
   Here is a picture showing how the
   cube's eight vertices are labeled.
<pre>{@code
                  y
                  |
                  | v7=(0, 1, -0.5)
                  +---------------------+ v6=(1, 1, -0.5)
                 /|                    /|
                / |                   / |
               /  |                  /  |
              /   |                 /   |
             /    |                /    |
         v3 +---------------------+ v2  |
            |     |               |     |
            |     |               |     |
            |     |v4=(0,0, -0.5) |     | v5=(1, 0, -0.5)
            |     +---------------|-----+
            |    /                |    /
            |   /                 |   /
            |  /                  |  /
            | /                   | /
            |/                    |/
            +---------------------+--------------> x
           /v0=(0, 0, 0)          v1=(1, 0, 0)
          /
         /
        z
}</pre>
*/
public class Position_Model_Vertex_Color_LineSegment_ver3_Pipeline2
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("Position_Model_Vertex_Color_LineSegment_ver3");

      // Use these eight vertex objects
      // to build four different models.
      final Vertex v0 = new Vertex(0, 0, 0),
                   v1 = new Vertex(1, 0, 0),
                   v2 = new Vertex(1, 1, 0),
                   v3 = new Vertex(0, 1, 0),
                   v4 = new Vertex(0, 0, -0.5),
                   v5 = new Vertex(1, 0, -0.5),
                   v6 = new Vertex(1, 1, -0.5),
                   v7 = new Vertex(0, 1, -0.5);

      // Use these two color objects
      // to build the four models.
      final Color c0 = Color.red,
                  c1 = Color.blue;

      // Use these 28 LineSegment objects
      // to build the four different models.
      // Make the left edge red and the right edge blue.
      final LineSegment ls0  = new LineSegment(0, 1, 0, 1),  // front edges
                        ls1  = new LineSegment(1, 2, 1, 1),
                        ls2  = new LineSegment(2, 3, 1, 0),
                        ls3  = new LineSegment(3, 0, 0, 0),
                        ls4  = new LineSegment(0, 2, 0, 1),  // front diagonals
                        ls5  = new LineSegment(1, 3, 1, 0),
                        ls6  = new LineSegment(4, 5, 0, 1),  // back edges
                        ls7  = new LineSegment(5, 6, 1, 1),
                        ls8  = new LineSegment(6, 7, 1, 0),
                        ls9  = new LineSegment(7, 4, 0, 0),
                        ls10 = new LineSegment(4, 6, 0, 1),  // back diagonals
                        ls11 = new LineSegment(5, 7, 1, 0),
                        ls12 = new LineSegment(0, 4, 0, 0),  // front to back edges
                        ls13 = new LineSegment(1, 5, 1, 1),
                        ls14 = new LineSegment(2, 6, 1, 1),
                        ls15 = new LineSegment(3, 7, 0, 0),
                        ls16 = new LineSegment(0, 5, 0, 1),  // bottom diagonals
                        ls17 = new LineSegment(1, 4, 1, 0),
                        ls18 = new LineSegment(3, 6, 0, 1),  // top diagonals
                        ls19 = new LineSegment(2, 7, 1, 0),
                        ls20 = new LineSegment(0, 7, 0, 0),  // left side diagonals
                        ls21 = new LineSegment(3, 4, 1, 0),
                        ls22 = new LineSegment(1, 6, 1, 1),  // right side diagonals
                        ls23 = new LineSegment(2, 5, 1, 1),
                        ls24 = new LineSegment(0, 6, 0, 1),  // interior diagonals
                        ls25 = new LineSegment(1, 7, 1, 0),
                        ls26 = new LineSegment(2, 4, 1, 0),
                        ls27 = new LineSegment(3, 5, 0, 1);

      // Create four model objects.
      final Model model_0 = new Model("Cube");
      final Model model_1 = new Model("Z");
      final Model model_2 = new Model("X");
      final Model model_3 = new Model("U");

      // Add the eight vertex objects to
      // each of the four model objects.
      model_0.addVertex(v0, v1, v2, v3, v4, v5, v6, v7);
      model_1.addVertex(v0, v1, v2, v3, v4, v5, v6, v7);
      model_2.addVertex(v0, v1, v2, v3, v4, v5, v6, v7);
      model_3.addVertex(v0, v1, v2, v3, v4, v5, v6, v7);

      // Add the two color objects to
      // each of the four model objects.
      model_0.addColor(c0, c1);
      model_1.addColor(c0, c1);
      model_2.addColor(c0, c1);
      model_3.addColor(c0, c1);

      // Make the four models look different
      // by giving them different line segments.
      model_0.addPrimitive(ls3, ls9, ls12, ls15, ls20, ls21,   // cube
                           ls0, ls6,             ls16, ls17,
                           ls1, ls7, ls13, ls14, ls22, ls23,
                           ls2, ls8,             ls18, ls19);

      model_1.addPrimitive(ls2, ls8,  ls14, ls15, ls18, ls19,  // Z
                           ls4, ls10,             ls24, ls26,
                           ls0, ls6,  ls12, ls13, ls16, ls17);

      model_2.addPrimitive(ls0, ls8, ls20, ls22, ls24, ls25,   // X
                           ls2, ls6, ls21, ls23, ls26, ls27);
//    model_2.addPrimitive(ls4, ls10, ls12, ls14, ls24, ls26,  // X
//                         ls5, ls11, ls13, ls15, ls25, ls27);

      model_3.addPrimitive(ls3, ls9, ls12, ls15, ls20, ls21,   // U
                           ls0, ls6,             ls16, ls17,
                           ls1, ls7, ls13, ls14, ls22, ls23);

      // Add the model objects to the scene by
      // placing each model in a position object.
      scene.addPosition(
               new Position(model_0, "p0", new Vector( 1,  1, -3)),  // cube
               new Position(model_1, "p1", new Vector(-2,  1, -3)),  // Z
               new Position(model_2, "p2", new Vector( 1, -2, -3)),  // X
               new Position(model_3, "p3", new Vector(-2, -2, -3))); // U

      final int width  = 600;
      final int height = 600;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.white);

      System.out.println( scene );
      scene.debug = true;
      Rasterize.debug = true;

      Pipeline2.render(scene, fb);
      fb.dumpFB2File("Position_Model_Vertex_Color_LineSegment_ver3_Pipeline2.ppm");

      // Draw pictures of the scene's tree data structures
      // as the scene moves down the rendering pipeline.
      DrawSceneGraph_with_Vertices_Colors.draw(scene,
                                        "Position_Model_Vertex_Color_LineSegment_ver3_Pipeline2_SG_with_Vertices_Colors");

      DrawSceneGraph_with_Vertices_Colors.draw(Pipeline2.scene1,
                                        "Position_Model_Vertex_Color_LineSegment_ver3_Pipeline2_stage1_SG_with_Vertices_Colors");

      DrawSceneGraph_with_Vertices_Colors.draw(Pipeline2.scene2,
                                        "Position_Model_Vertex_Color_LineSegment_ver3_Pipeline2_stage2_SG_with_Vertices_Colors");

      DrawSceneGraph_with_Vertices_Colors.draw(Pipeline2.scene3,
                                        "Position_Model_Vertex_Color_LineSegment_ver3_Pipeline2_stage3_SG_with_Vertices_Colors");
   }
}
