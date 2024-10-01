/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene;

/**
   A {@code Vector} object holds three doubles, which makes it a vector
   in 3-dimensional space.
*/
public final class Vector
{
   public final double x, y, z;

   /**
      Construct a new {@code Vector} using the given x, y, and z coordinates.

      @param x  x-coordinate of the new {@code Vector}
      @param y  y-coordinate of the new {@code Vector}
      @param z  z-coordinate of the new {@code Vector}
   */
   public Vector(final double x, final double y, final double z)
   {
      this.x = x;
      this.y = y;
      this.z = z;
   }


   /**
      Construct a new {@code Vector} from a {@link Vertex}.

      @param v  {@link Vertex} object to convert into a {@code Vector}
   */
   public Vector(final Vertex v)
   {
      this(v.x, v.y, v.z);
   }


   /**
      The dot-product of two {@code Vector}s returns a scalar.

      @param v  {@code Vector} object to multiply with this {@code Vector}
      @return a double that is the dot-product of this {@code Vector} and {@code v}
   */
   public double dotProduct(final Vector v)
   {
      return (x * v.x) + (y * v.y) + (z * v.z);
   }


   /**
      The cross-product of two {@code Vector}s returns a (new) {@code Vector}.

      @param v  {@code Vector} object to multiply with this {@code Vector}
      @return a new {@code Vector} object that is the cross-product of this {@code Vector} and {@code v}
   */
   public Vector crossProduct(final Vector v)
   {
      return new Vector(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
   }


   /**
      A scalar times a {@code Vector} returns a (new) {@code Vector}.

      @param s  number to multiply this {@code Vector} by
      @return a new {@code Vector} object that is the scalar times this {@code Vector}
   */
   public Vector times(final double s) // return s * this
   {
      return new Vector(s*x, s*y, s*z);
   }


   /**
      A {@code Vector} plus a {@code Vector} returns a (new) {@code Vector}.

      @param v  {@code Vector} object to add to this {@code Vector}
      @return a new {@code Vector} object that is the sum of this {@code Vector} and {@code v}
   */
   public Vector plus(final Vector v) // return this + v
   {
      return new Vector(x + v.x, y + v.y, z + v.z);
   }


   /**
      A {@code Vector} minus a {@code Vector} returns a (new) {@code Vector}.

      @param v  {@code Vector} object to subtract from this {@code Vector}
      @return a new {@code Vector} object that is this {@code Vector} minus {@code v}
   */
   public Vector minus(final Vector v) // return this - v
   {
      return new Vector(x - v.x, y - v.y, z - v.z);
   }


   /**
      Return the normalized version of this {@code Vector}.
      <p>
      That is, return the {@code Vector} with length 1 that
      points in the same direction as this {@code Vector}.

      @return a new {@code Vector} that has length one and has the same direction as this {@code Vector}
   */
   public Vector normalize() // return this / |this|
   {
      final double norm = Math.sqrt( x*x + y*y + z*z );
      return new Vector(x/norm, y/norm, z/norm);
   }


   /**
      A {@code Vector} plus a {@link Vertex} returns a (new) {@link Vertex}.
      <p>
      The vector translates the vertex to a new location.

      @param v  {@link Vertex} object to add to this {@code Vector}
      @return a new {@link Vertex} object that is the translation of {@code v} by this {@code Vector}
   */
   public Vertex plus(final Vertex v)
   {
      return new Vertex(x + v.x, y + v.y, z + v.z);
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Vector} object
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
      @return {@link String} representation of this {@code Vector} object
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
      @return {@link String} representation of this {@code Vector} object
   */
   public String toString(final int precision, final int iWidth)
   {
      // Here is one way to get programmable precision and width.
      final int p = precision;      // the precision for the following format string
      final int t = p + iWidth + 2; // the width for the following format string
      final String format = "[x,y,z] = [% "+t+"."+p+"f  % "+t+"."+p+"f  % "+t+"."+p+"f]";
      return String.format(format, x, y, z);
   }
}
