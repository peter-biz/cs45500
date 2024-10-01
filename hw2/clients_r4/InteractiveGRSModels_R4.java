/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import renderer.scene.*;
import renderer.scene.util.Assets;
import renderer.scene.util.ModelShading;
import renderer.models_L.GRSModel;
import renderer.models_L.Axes2D;
import renderer.framebuffer.FrameBufferPanel;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.io.File;

/**

*/
public class InteractiveGRSModels_R4 extends InteractiveAbstractClient_R4
{
   private static final String assets = Assets.getPath();

   /**
      This constructor instantiates the Scene object
      and initializes it with appropriate geometry.
      Then this constructor instantiates the GUI.
   */
   public InteractiveGRSModels_R4()
   {
      scene = new Scene("InteractiveGRSModels_R4",
                        Camera.projOrtho());
      // Switch to a parallel (orthographic) projection.
      this.perspective = false;

      // Instantiate all the grs models.
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/bronto_v2.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/rex_v2.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/usa_v2.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/bronto.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/rex.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/usa.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/vinci.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/dragon.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/birdhead.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/knight.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/house.grs"))));
      scene.addPosition(new Position(new GRSModel(
                             new File(assets + "grs/scene.grs"))));

      // Give each model a random color.
      for (final Position p : scene.positionList)
      {
         ModelShading.setRandomColor(p.getModel());
      }

      // Create a set of red x and y axes, pushed away from the camera.
      final Model axes = new Axes2D(-1, +1, -1, +1, 20, 20, Color.red);
      final Position axes_p = new Position(axes, new Vector(0, 0, -2));
      scene.addPosition(axes_p);

      // Make the interactive models invisible, except for the current model.
      numberOfInteractiveModels = scene.positionList.size() - 1;
      for (int i = 0; i < numberOfInteractiveModels; ++i)
      {
         scene.getPosition(i).visible = false;
      }
      currentModel = 0; // bronto_v2
      scene.getPosition(currentModel).visible = true;
      interactiveModelsAllVisible = false;
      debugWholeScene = false;

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
      jf = new JFrame("Renderer 4 - GRS Models");
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
         () -> new InteractiveGRSModels_R4()
      );
   }
}
