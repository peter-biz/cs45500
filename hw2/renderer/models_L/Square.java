/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Create a wireframe model of a square in the xy-plane centered at the origin.
<p>
   Here is a picture showing how the square's four vertices are labeled.
<pre>{@code
                   y
                   |
                   |
      v1           |            v2
        +----------------------+
        |          |           |
        |          |           |
        |          |           |
        |          |           |
  ------|----------+-----------|-------> x
        |          |           |
        |          |           |
        |          |           |
        +----------------------+
      v0           |            v3
                   |
                   |
}</pre>
*/
public class Square extends Model
{
   /**
      Create a square in the xy-plane with corners {@code (±1, ±1, 0)}.
   */
   public Square( )
   {
      this(1);
   }


   /**
      Create a square in the xy-plane with corners {@code (±r, ±r, 0)}.

      @param r  determines the corners of the square
      @throws IllegalArgumentException if {@code r} is less than or equal to 0
   */
   public Square(final double r)
   {
      super(String.format("Square(%.2f)", r));

      if (r <= 0)
         throw new IllegalArgumentException("r must be greater than 0");

      // Create the square's geometry.
      addVertex(new Vertex(-r, -r, 0),
                new Vertex(-r,  r, 0),
                new Vertex( r,  r, 0),
                new Vertex( r, -r, 0));

      addPrimitive(new LineSegment(0, 1),
                   new LineSegment(1, 2),
                   new LineSegment(2, 3),
                   new LineSegment(3, 0));
   }
}//Square
