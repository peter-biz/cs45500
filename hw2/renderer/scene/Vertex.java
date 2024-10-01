/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene;

/**
   A {@code Vertex} object has three doubles which represent the
   coordinates of a point in 3-dimensional space.
<p>
   When a {@code Vertex} object is created in a client program,
   before the {@code Vertex} object moves down the graphics rendering
   pipeline, the coordinates in the {@code Vertex} will be in
   some model's local coordinate system.
<p>
   As a {@code Vertex} object moves down the graphics rendering
   pipeline, the coordinates in the {@code Vertex} will be transformed
   from one coordinate system to another.
<p>
   A {@code Vertex} object is immutable, so after it gets created it
   cannot be modified (mutated). So a {@code Vertex} object does not
   really "move" down the graphics pipeline. When a {@code Vertex}
   object needs to be transformed, we replace it, with a new
   {@code Vertex} object, instead of mutating it.
*/
public final class Vertex
{
   public final double x, y, z; // these are "blank finals"

   /**
      Construct a new {@code Vertex} using the given
      {@code x}, {@code y}, and {@code z} coordinates.

      @param x  x-coordinate of the new {@code Vertex}
      @param y  y-coordinate of the new {@code Vertex}
      @param z  z-coordinate of the new {@code Vertex}
   */
   public Vertex(final double x, final double y, final double z)
   {
      this.x = x; // fill in the "blank final" variables
      this.y = y;
      this.z = z;
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Vertex} object
   */
   @Override
   public String toString()
   {
      final int precision = 5;  // the default precision for the format string
      return toString(precision);
   }


   /**
      For debugging.
      <p>
      Allow the precision of the formatted output to be specified.

      @param precision  precision value for the format string
      @return {@link String} representation of this {@code Vertex} object
   */
   public String toString(final int precision)
   {
      final int iWidth = 3; // default width of integer part of the format string
      return toString(precision, iWidth);
   }


   /**
      For debugging.
      <p>
      Allow the precision and width of the formatted output to be specified.
      By width, we mean the width of the integer part of each number.

      @param precision  precision value for the format string
      @param iWidth     width of the integer part of the format string
      @return {@link String} representation of this {@code Vertex} object
   */
   public String toString(final int precision, final int iWidth)
   {
      // Here is one way to get programmable precision and width.
      final int p = precision;      // the precision for the following format string
      final int t = p + iWidth + 2; // the width for the following format string
      final String format = "(x,y,z) = (% "+t+"."+p+"f  % "+t+"."+p+"f  % "+t+"."+p+"f)";
      return String.format(format, x, y, z);
   }
}
