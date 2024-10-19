/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.util.Assets;
import renderer.scene.util.PointCloud;
import renderer.scene.util.ModelShading;
import renderer.models_L.*;
import renderer.framebuffer.FrameBufferPanel;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.io.File;

/**

*/
public class InteractiveModelsAll_R4 extends InteractiveAbstractClient_R4
{
   private static final String assets = Assets.getPath();

   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public InteractiveModelsAll_R4()
   {
      scene = new Scene("InteractiveModelsAll_R4");

      // Instantiate at least one of every Model class.
      // 2D models
      scene.addPosition(new Position(
                           new Square(1.0)));
      scene.addPosition(new Position(
                           new SquareGrid(1.0, 11, 15)));
      scene.addPosition(new Position(
                           new Circle(1.0, 16)));
      scene.addPosition(new Position(
                           new CircleSector(1.0,  Math.PI/4, -Math.PI/4, 13)));
      scene.addPosition(new Position(
                           new CircleSector(1.0, -Math.PI/4,  Math.PI/4,  5)));
      scene.addPosition(new Position(
                           new Disk(1.0, 4, 16)));
      scene.addPosition(new Position(
                           new DiskSector(1.0, Math.PI/2, 3*Math.PI/2, 4, 8)));
      scene.addPosition(new Position(
                           new Ring(1.0, 0.25, 3, 16)));
      scene.addPosition(new Position(
                           new RingSector(1.0, 0.25, Math.PI/2, 3*Math.PI/2, 3, 8)));
      scene.addPosition(new Position(
                           new BarycentricTriangle(45, 4))); // try 5 or 6
      // cubes
      scene.addPosition(new Position(new Box(1.0, 1.0, 1.0)));
      scene.addPosition(new Position(new Cube()));
      scene.addPosition(new Position(new Cube2(4, 5, 6)));
      scene.addPosition(new Position(new Cube2(40, 50, 60)));
      scene.addPosition(new Position(new Cube3(12, 14, 15)));
      scene.addPosition(new Position(new Cube4(12, 14, 15)));
      // polyhedra
      scene.addPosition(new Position(new Tetrahedron()));
      scene.addPosition(new Position(new Tetrahedron(true)));
      scene.addPosition(new Position(new Tetrahedron(20, 40)));
      scene.addPosition(new Position(new Tetrahedron(30, 30, 30, 30)));
      scene.addPosition(new Position(new Octahedron()));
      scene.addPosition(new Position(new Octahedron(30)));
      scene.addPosition(new Position(new Octahedron(20, 20, 20)));
      scene.addPosition(new Position(new Dodecahedron()));
      scene.addPosition(new Position(new Icosahedron()));
      scene.addPosition(new Position(new Icosidodecahedron()));
      // pyramids
      scene.addPosition(new Position(new Pyramid(
                2.0, 1.0, 5, 6)));
      scene.addPosition(new Position(new PyramidFrustum(
                2.0, 1.0, 0.5, 4, 5)));
      scene.addPosition(new Position(new PyramidFrustum(
                1.0, 2.0, 0.5, 4, 5)));
      scene.addPosition(new Position(new TriangularPyramid(
                Math.sqrt(3)/Math.sqrt(2))));
      scene.addPosition(new Position(new TriangularPyramid(
                1.0, 1.0, 7, 7)));
      scene.addPosition(new Position(new TriangularPrism(
                0.6, 0.5, 0.5, 3, true)));
      scene.addPosition(new Position(new ViewFrustumModel()));
      // cones
      scene.addPosition(new Position(new Cone(
                1.0, 1.0, 10, 16)));
      scene.addPosition(new Position(new ConeSector(
                1.0, 1.0, 0.5, 0, 2*Math.PI, 5, 16)));
      scene.addPosition(new Position(new ConeSector(
                1.0, 1.0, 0.5, Math.PI/2, 3*Math.PI/2, 5, 8)));
      scene.addPosition(new Position(new ConeFrustum(
                1.0, 0.5, 0.5, 6, 16)));
      scene.addPosition(new Position(new ConeFrustum(
                0.5, 0.5, 1.0, 6, 16)));
      // cylinders
      scene.addPosition(new Position(new Cylinder(
                0.5, 1.0, 11, 12)));
      scene.addPosition(new Position(new CylinderSector(
                0.5, 1.0, Math.PI/2, 3*Math.PI/2, 11, 6)));
      // spheres
      scene.addPosition(new Position(new Sphere(
                1.0, 15, 12)));
      scene.addPosition(new Position(PointCloud.make(new Sphere(
                1.0, 60, 60))));
      scene.addPosition(new Position(PointCloud.make(new Sphere(
                1.0, 120, 120))));
      scene.addPosition(new Position(new SphereSector(
                1.0, Math.PI/2, 3*Math.PI/2, Math.PI/4, 3*Math.PI/4, 7, 6)));
      scene.addPosition(new Position(new SphereSubdivided(4)));
      scene.addPosition(new Position(new SphereSubdivided(6, true, false)));
      scene.addPosition(new Position(new SphereSubdivided(7, false, true)));
      // torus
      scene.addPosition(new Position(new Torus(
                0.75, 0.25, 12, 16)));
      scene.addPosition(new Position(PointCloud.make(new Torus(
                0.75, 0.25, 120, 120))));
      scene.addPosition(new Position(new TorusSector(
                0.75, 0.25, Math.PI/2, 3*Math.PI/2, 12, 8)));
      scene.addPosition(new Position(new TorusSector(
                0.75, 0.25, 0, 2*Math.PI, Math.PI, 2*Math.PI, 6, 16)));
      scene.addPosition(new Position(new TorusSector(
                0.75, 0.25, 0, 2*Math.PI, -Math.PI/2, Math.PI/2, 6, 16)));
      scene.addPosition(new Position(new TorusSector(
                0.75, 0.25, Math.PI/2, 3*Math.PI/2, -Math.PI/2, Math.PI/2, 6, 8)));
      // model files
      scene.addPosition(new Position(new GRSModel(
                new File(assets + "grs/bronto.grs"))));
      scene.addPosition(new Position(new ObjSimpleModel(
                new File(assets + "cow.obj"))));
      // parametric curves and surfaces
      scene.addPosition(new Position(new ParametricCurve()));
      scene.addPosition(new Position(new ParametricSurface()));
      scene.addPosition(new Position(new ParametricSurface(
                (s,t) -> s*Math.cos(t*Math.PI),
                (s,t) -> t,
                (s,t) -> s*Math.sin(t*Math.PI),
                -1, 1, -1, 1, 49, 49)));
      scene.addPosition(new Position(new ParametricSurface(
                (u,v) -> 0.3*(1-u)*(3+Math.cos(v))*Math.sin(4*Math.PI*u),
                (u,v) -> 0.3*(3*u+(1-u)*Math.sin(v)),
                (u,v) -> 0.3*(1-u)*(3+Math.cos(v))*Math.cos(4*Math.PI*u),
                0, 1, 0, 2*Math.PI, 49, 49)));
      scene.addPosition(new Position(new SurfaceOfRevolution()));
      scene.addPosition(new Position(new SurfaceOfRevolution(
                t -> 0.5*(1+t*t), -1, 1, 30, 30)));
      scene.addPosition(new Position(new SurfaceOfRevolution(
                t -> t, t->4*t*(1-t), 0, 1, 30, 30)));
      // coordinate axes
      scene.addPosition(new Position(new Axes3D(1, 1, 1)));
      scene.addPosition(new Position(new Axes2D(-1, 1, -1, 1, 8, 8)));
      scene.addPosition(new Position(new PanelXY(-4, 4, -5, 5, -5)));
      scene.addPosition(new Position(new PanelXZ(-1, 1, -6, 1, -0.5)));
      scene.addPosition(new Position(new PanelYZ(-1, 1, -6, 1, -0.5)));

      // Give each model a random color.
      for (final Position p : scene.positionList)
      {
         ModelShading.setRandomColor( p.getModel() );
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

      // Have the models pushed away from where the camera is.
      deltaX[0] =  0;
      deltaY[0] =  0;
      deltaZ[0] = -2;
      scene.getPosition(currentModel).translate(deltaX[0],
                                                deltaY[0],
                                                deltaZ[0]);

      renderer.pipeline.Rasterize.doClipping = true;


      // Create a FrameBufferPanel that holds a FrameBuffer.
      final int width  = 1024;
      final int height = 1024;
      fbp = new FrameBufferPanel(width, height, Color.black);

      // Create a JFrame that will hold the FrameBufferPanel.
      jf = new JFrame("Renderer 4 - All Models");
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
         () -> new InteractiveModelsAll_R4()
      );
   }
}
