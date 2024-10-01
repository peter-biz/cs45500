/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.pipeline;

import renderer.scene.*;

import java.util.List;
import java.util.ArrayList;

/**
   Transform each {@link Vertex} of a {@link Model} from the model's
   (private) local coordinate system to the {@link Camera}'s (shared)
   coordinate system.
<p>
   For each {@code Vertex} object in a {@code Model} object, use a
   {@link Position}'s translation {@link Vector} to translate the
   object's {@code Vertex} coordinates from the model's coordinate
   system to the camera's coordinate system.
<p>
   Return a new {@code Model} object, which contains all the translated
   vertices from the original model, to the renderer. The original model
   object, which belongs to the client program, remains unchanged. So the
   renderer gets the mutated model and the client sees its model as being
   preserved.
*/
public class Model2Camera
{
   /**
      Use a {@link Position}'s translation {@link Vector} to transform
      each {@link Vertex} from a {@link Model}'s coordinate system to
      the {@link Camera}'s coordinate system.

      @param position  {@link Position} with a {@link Model} and a translation {@link Vector}
      @return a new {@link Model} with {@link Vertex} objects in the camera's coordinate system
   */
   public static Model model2camera(final Position position)
   {
      final Model model = position.getModel();
      final Vector translation = position.getTranslation();

      // A new vertex list to hold the translated vertices.
      final List<Vertex> newVertexList =
                            new ArrayList<>(model.vertexList.size());

      // Replace each Vertex object with one that
      // contains camera coordinates.
      for (final Vertex v : model.vertexList)
      {
         newVertexList.add( translation.plus(v) );
      }

      // Return to the renderer a modified model.
      return new Model(newVertexList,
                       model.primitiveList,
                       model.colorList,
                       position.name + "::" + model.name,
                       model.visible);
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Model2Camera() {
      throw new AssertionError();
   }
}
