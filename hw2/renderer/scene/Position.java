/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene;

/**
   A {@code Position} data structure represents a geometric object
   in a distinct location in three-dimensional camera space as part
   of a {@link Scene}.
<p>
   A {@code Position} object holds references to a {@link Model} object
   and a {@link Vector} object. The {@link Model} represents the geometric
   object in the {@link Scene}. The {@link Vector} determines the model's
   location in the {@link Camera} coordinate system.
<p>
   When the renderer renders this {@code Position}'s {@link Model} into
   a {@link renderer.framebuffer.FrameBuffer}, the first stage of the
   rendering pipeline, {@link renderer.pipeline.Model2Camera}, adds this
   {@code Position}'s translation {@link Vector} to every {@link Vertex}
   in the {@link Model}'s vertex list, which converts the coordinates in
   each {@link Vertex} from the model's own local coordinate system to
   the {@link Camera}'s coordinate system (which is "shared" by all
   the other models in the scene). This vector addition has the effect
   of "placing" the model in camera space at an appropriate location.
*/
public final class Position
{
   private Model model;
   private Vector translation;
   public final String name;
   public boolean visible;
   public boolean debug;

   /**
      Construct a {@code Position} with the zero translation {@link Vector}
      and the given {@link Model} object.

      @param model  {@link Model} object to place at this {@code Position}
      @throws NullPointerException if {@code model} is {@code null}
   */
   public Position(final Model model)
   {
      this(model,
           model.name,          // default Position name
           new Vector(0, 0, 0), // default translation vector
           true,                // visible
           false);              // debug
   }


   /**
      Construct a {@code Position} with the zero translation {@link Vector},
      the given {@link String} name, and the given {@link Model} object.

      @param model  {@link Model} object to place at this {@code Position}
      @param name   {@link String} name for this {@code Position}
      @throws NullPointerException if {@code model} is {@code null}
      @throws NullPointerException if {@code name} is {@code null}
   */
   public Position(final Model model, final String name)
   {
      this(model,
           name,
           new Vector(0, 0, 0), // default translation vector
           true,                // visible
           false);              // debug
   }


   /**
      Construct a {@code Position} with the given translation {@link Vector},
      the given {@link String} name, and the given {@link Model} object.

      @param model        {@link Model} object to place at this {@code Position}
      @param name         {@link String} name for this {@code Position}
      @param translation  translation {@link Vector} for this {@code Position}
      @throws NullPointerException if {@code model} is {@code null}
      @throws NullPointerException if {@code name} is {@code null}
      @throws NullPointerException if {@code translation} is {@code null}
   */
   public Position(final Model model,
                   final String name,
                   final Vector translation)
   {
      this(model,
           name,
           translation,
           true,   // visible
           false); // debug
   }


   /**
      Construct a {@code Position} with the given translation {@link Vector}
      and the given {@link Model} object.

      @param model        {@link Model} object to place at this {@code Position}
      @param translation  translation {@link Vector} for this {@code Position}
      @throws NullPointerException if {@code model} is {@code null}
      @throws NullPointerException if {@code translation} is {@code null}
   */
   public Position(final Model model,
                   final Vector translation)
   {
      this(model,
           model.name,  // default Position name
           translation,
           true,        // visible
           false);      // debug
   }


   /**
      Construct a {@code Position} object with all the given data.

      @param model        {@link Model} object to place at this {@code Position}
      @param name         {@link String} name for this {@code Position}
      @param translation  translation {@link Vector} for this {@code Position}
      @param visible      boolean that determines this {@code Position}'s visibility
      @param debug        boolean that determines if this {@code Position} is logged
      @throws NullPointerException if {@code model} is {@code null}
      @throws NullPointerException if {@code translation} is {@code null}
      @throws NullPointerException if {@code name} is {@code null}
   */
   public Position(final Model model,
                   final String name,
                   final Vector translation,
                   final boolean visible,
                   final boolean debug)
   {
      if (null == model)
         throw new NullPointerException("model must not be null");
      if (null == name)
         throw new NullPointerException("name must not be null");
      if (null == translation)
         throw new NullPointerException("translation vector must not be null");

      this.model = model;
      this.translation = translation;
      this.name = name;
      this.visible = visible;
      this.debug = debug;
   }


   /**
      Get a reference to this {@code Position}'s {@link Model} object.

      @return a reference to this {@code Position}'s {@link Model} object
   */
   public Model getModel()
   {
      return this.model;
   }


   /**
      Set this {@code Position}'s {@link Model} object.

      @param model  {@link Model} object to place at this {@code Position}
      @return a reference to this {@link Position} object to facilitate chaining method calls
      @throws NullPointerException if {@code model} is {@code null}
   */
   public Position setModel(final Model model)
   {
      if (null == model)
         throw new NullPointerException("model must not be null");

      this.model = model;
      return this;
   }


   /**
      Get a reference to this {@code Position}'s {@link Vector} object.

      @return a reference to this {@code Position}'s {@link Vector} object
   */
   public Vector getTranslation()
   {
      return this.translation;
   }


   /**
      Set this {@code Position}'s translation {@link Vector} object.

      @param x  translation amount in the x-direction
      @param y  translation amount in the y-direction
      @param z  translation amount in the z-direction
      @return a reference to this {@link Position} object to facilitate chaining method calls
   */
   public Position translate(final double x,
                             final double y,
                             final double z)
   {
      this.translation = new Vector(x, y, z);
      return this;
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Position} object
   */
   @Override
   public String toString()
   {
      String result = "";
      result += "Position: " + name + "\n";
      result += "This Position's visibility is: " + visible + "\n";
      result += "This Position's translation is\n";
      result += translation + "\n";
      result += "This Position's Model is\n";
      result += (null == model) ? "null\n" : model;
      return result;
   }
}
