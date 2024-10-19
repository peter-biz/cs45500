/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.util.ModelShading;
import renderer.scene.util.DrawSceneGraph;
import renderer.models_L.*;
import renderer.pipeline.*;
import renderer.framebuffer.*;

import java.awt.Color;

/**
   http://merganser.math.gvsu.edu/david/psseminar/index.html#jiggity
*/
public class ThreeDimensionalScene_R4
{
   public static void main(String[] args)
   {
      final Scene scene = new Scene("ThreeDimensionalScene_R4");

      // Create several Model objects.
      final Model m0 = new Cube(),
                  m1 = new Tetrahedron(),
                  m2 = new Octahedron(),
                  m3 = new Dodecahedron(),
                  m4 = new Icosahedron(),
                  m5 = new PanelXZ(-7, 7, -7, 7);

      ModelShading.setColor(m0, Color.red);
      ModelShading.setColor(m1, Color.red);
      ModelShading.setColor(m2, Color.red);
      ModelShading.setColor(m3, Color.red);
      ModelShading.setColor(m4, Color.red);
      ModelShading.setColor(m5, Color.blue);

      // Add the models to the Scene.
      scene.addPosition(
               new Position(m0, "p0", new Vector(-4,  0, -18)), // Cube
               new Position(m1, "p1", new Vector( 0, -2, -14)), // Tetrahedron
               new Position(m2, "p2", new Vector( 4, -3, -10)), // Octahedron
               new Position(m3, "p3", new Vector(-4, -3, -10)), // Dodecahedron
               new Position(m4, "p4", new Vector( 4,  0, -18)), // Icosahedron
               new Position(m5, "p5", new Vector( 0, -7, -14)));// Panel

      // Use GraphViz to draw pictures of the Scene data structure.
      DrawSceneGraph.drawVector = false;
      DrawSceneGraph.drawVectorDetails = false;
      DrawSceneGraph.draw(scene, "ThreeDimensionalScene_R4_SG");
      DrawSceneGraph.drawVectorDetails = true;
      DrawSceneGraph.draw(scene, "ThreeDimensionalScene_R4_SG_with_Vectors");
      DrawSceneGraph.drawVertexList = true;
      DrawSceneGraph.draw(scene, "ThreeDimensionalScene_R4_SG_with_Vertices");


      // Create a FrameBuffer to render our Scene into.
      final int width  = 800;
      final int height = 800;
      final FrameBuffer fb = new FrameBuffer(width, height, Color.white);
      // The background color of the framebuffer is white, so the
      // background color of the default viewport is also white.

      // Render
      scene.debug = true;
      Rasterize.doAntiAliasing = true; // Try making this false.
      Rasterize.doGamma = true;        // Try making this false.
      Pipeline.render(scene, fb);

      // Save the framebuffer's contents in an image file.
      fb.dumpFB2File("ThreeDimensionalScene_R4.ppm");
   }
}
