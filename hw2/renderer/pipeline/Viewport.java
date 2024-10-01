/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.pipeline;

import renderer.scene.*;
import renderer.framebuffer.*;

import java.util.List;
import java.util.ArrayList;

/**
   Transform each (projected) {@link Vertex} of a {@link Model}
   from the camera's image-plane to the logical pixel-plane.
<p>
   Return a new {@code Model} object, which contains all the
   transformed vertices from the original model, to the renderer.
*/
public class Viewport
{
   /**
      Use the dimensions of a {@link FrameBuffer.Viewport} to transform
      each {@link Vertex} from the camera's image-plane to the logical
      pixel-plane.

      @param model     {@link Model} whose {@link Vertex} objects are in the camera's image-plane
      @param viewport  a reference to a {@link FrameBuffer.Viewport} in a {@link FrameBuffer}
      @return a new {@link Model} with {@link Vertex} objects in the logical pixel-plane
   */
   public static Model imagePlane2pixelPlane(final Model model,
                                             final FrameBuffer.Viewport viewport)
   {
      final int w = viewport.getWidthVP();
      final int h = viewport.getHeightVP();

      // A new vertex list to hold the transformed vertices.
      final List<Vertex> newVertexList =
                            new ArrayList<>(model.vertexList.size());

      // Replace each Vertex object with one that
      // lies in the logical pixel-plane.
      for (final Vertex v : model.vertexList)
      {
         // Transform the vertex to the pixel-plane coordinate system.
         final double x = 0.5 + w/2.001 * (v.x + 1); // x_pp = 0.5 + w/2 * (x_p+1)
         final double y = 0.5 + h/2.001 * (v.y + 1); // y_pp = 0.5 + h/2 * (y_p+1)
         // NOTE: Notice the 2.001 fudge factor in the last two equations.
         // This is explained on page 142 of
         //    "Jim Blinn's Corner: A Trip Down The Graphics Pipeline"
         //     by Jim Blinn, 1996, Morgan Kaufmann Publishers.

         newVertexList.add( new Vertex(x, y, 0.0) );
      }

      // Return to the renderer a modified model.
      return new Model(newVertexList,
                       model.primitiveList,
                       model.colorList,
                       model.name,
                       model.visible);
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Viewport() {
      throw new AssertionError();
   }
}
