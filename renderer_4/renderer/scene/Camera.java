/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene;

/**
   This {@code Camera} data structure represents a camera
   located at the origin, looking down the negative z-axis.
<p>
   This {@code Camera} has associated to it a "view volume"
   that determines what part of space the camera "sees" when
   we use the camera to take a picture (that is, when we
   render a {@link Scene}).
<p>
   This {@code Camera} can "take a picture" two ways, using
   a perspective projection or a parallel (orthographic)
   projection. Each way of taking a picture has a different
   shape for its view volume.
<p>
   For the perspective projection, the view volume is an
   infinitely long pyramid that is formed by the pyramid
   with its apex at the origin and its base in the plane
   {@code z = -1} with edges {@code x = -1}, {@code x = +1},
   {@code y = -1}, and {@code y = +1}.
<p>
   For the orthographic projection, the view volume is an
   infinitely long rectangular cylinder parallel to the
   z-axis and with sides {@code x = -1}, {@code x = +1},
   {@code y = -1}, and {@code y = +1} (an infinite parallelepiped).
<p>
   When the graphics rendering {@link renderer.pipeline.Pipeline}
   uses this {@code Camera} to render a {@link Scene}, the renderer
   "sees" the geometry from the scene that is contained in this
   camera's view volume. (Notice that this means the orthographic
   camera will see geometry that is behind the camera. In fact, the
   perspective camera also sees geometry that is behind the camera.)
   The renderer's {@link renderer.pipeline.Rasterize} pipeline stage
   is responsible for making sure that the scene's geometry that is
   outside of this camera's view volume is not visible.
<p>
   The plane {@code z = -1} is the camera's "image plane". The
   rectangle in the image plane with corners {@code (-1, -1, -1)}
   and {@code (+1, +1, -1)} is the camera's "view rectangle". The
   view rectangle is like the film in a real camera, it is where
   the camera's image appears when you take a picture. The contents
   of the camera's view rectangle is what gets rasterized, by the
   renderer's {@link renderer.pipeline.Rasterize} pipeline stage,
   into a {@link renderer.framebuffer.FrameBuffer}'s
   {@link renderer.framebuffer.FrameBuffer.Viewport}.
*/
public final class Camera
{
   // Choose either perspective or parallel projection.
   public final boolean perspective;

   /**
      A private {@code Camera} constructor for
      use by the static factory methods.
   */
   private Camera(final boolean perspective)
   {
      this.perspective = perspective;
   }


   /**
      This is a static factory method.
      <p>
      Set up this {@code Camera}'s view volume as a perspective projection
      of an infinite view pyramid extending along the negative z-axis.

      @return a new {@code Camera} object with the default perspective parameter
   */
   public static Camera projPerspective()
   {
      return new Camera(true);
   }


   /**
      This is a static factory method.
      <p>
      Set up this {@code Camera}'s view volume as a parallel (orthographic)
      projection of an infinite view parallelepiped extending along the z-axis.

      @return a new {@code Camera} object with the default orthographic parameter
   */
   public static Camera projOrtho()
   {
      return new Camera(false);
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Camera} object
   */
   @Override
   public String toString()
   {
      String result = "";
      result += "Camera: \n";
      result += "perspective = " + perspective;
      return result;
   }
}
