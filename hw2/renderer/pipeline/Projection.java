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
   Project each {@link Vertex} of a {@link Model} from camera
   coordinates to the {@link Camera}'s image plane {@code z = -1}.
<p>
   Let us derive the formulas for the perspective projection
   transformation (the formulas for the parallel projection
   transformation are pretty obvious). We will derive the
   x-coordinate formula; the y-coordinate formula is similar.
<p>
   Let {@code (x_c, y_c, z_c)} denote a point in the 3-dimensional
   camera coordinate system. Let {@code (x_p, y_p, -1)} denote the
   point's perspective projection into the image plane, {@code z = -1}.
   Here is a "picture" of just the xz-plane from camera space. This
   picture shows the point {@code (x_c, z_c)} and its projection to
   the point {@code (x_p, -1)} in the image plane.
<pre>{@code
           x
           |                             /
           |                           /
       x_c +                         + (x_c, z_c)
           |                       / |
           |                     /   |
           |                   /     |
           |                 /       |
           |               /         |
           |             /           |
       x_p +           +             |
           |         / |             |
           |       /   |             |
           |     /     |             |
           |   /       |             |
           | /         |             |
           +-----------+-------------+------------> -z
        (0,0)         -1            z_c
}</pre>
<p>
   We are looking for a formula that computes {@code x_p} in terms of
   {@code x_c} and {@code z_c}. There are two similar triangles in this
   picture that share a vertex at the origin. Using the properties of
   similar triangles we have the following ratios. (Remember that these
   are ratios of positive lengths, so we write {@code -z_c}, since
   {@code z_c} is on the negative z-axis).
<pre>{@code
                 x_p       x_c
                -----  =  -----
                  1       -z_c
}</pre>
<p>
   If we solve this ratio for the unknown, {@code x_p}, we get the
   projection formula,
<pre>{@code
                 x_p = -x_c / z_c.
}</pre>
<p>
   The equivalent formula for the y-coordinate is
<pre>{@code
                 y_p = -y_c / z_c.
}</pre>
*/
public final class Projection
{
   /**
      Project each {@link Vertex} from a {@link Model} to
      the {@link Camera}'s image plane {@code z = -1}.
      <p>
      This pipeline stage assumes that the model's vertices
      have all been transformed to the camera coordinate system.

      @param model  {@link Model} whose {@link Vertex} objects are to be projected onto the image plane
      @param camera  a reference to the {@link Scene}'s {@link Camera} object
      @return a new {@link Model} object holding the projected {@link Vertex} objects
   */
   public static Model project(final Model model, final Camera camera)
   {
      // A new vertex list to hold the projected vertices.
      final List<Vertex> newVertexList =
                            new ArrayList<>(model.vertexList.size());

      // Replace each Vertex object with one that contains
      // the original Vertex's projected (x,y) coordinates.
      for (final Vertex v : model.vertexList)
      {
         if ( camera.perspective )
         {
            // Calculate the perspective projection.
            newVertexList.add(
              new Vertex(
                v.x / -v.z,  // xp = xc / -zc
                v.y / -v.z,  // yp = yc / -zc
                -1));        // zp = -1
         }
         else
         {
            // Calculate the parallel projection.
            newVertexList.add(
              new Vertex(
                v.x,  // xp = xc
                v.y,  // yp = yc
                0));  // zp = 0
         }
      }

      return new Model(newVertexList,
                       model.primitiveList,
                       model.colorList,
                       model.name,
                       model.visible);
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Projection() {
      throw new AssertionError();
   }
}
