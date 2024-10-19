/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.primitives;

import java.util.List;

/**
   A {@code Point} object has two integers that represent the location
   and color of a single {@link renderer.scene.Vertex}. The first integer
   is an index into the {@link renderer.scene.Vertex} list of a
   {@link renderer.scene.Model} object and the second integer is an index
   into the {@link java.awt.Color} list of that {@link renderer.scene.Model}
   object.
*/
public class Point extends Primitive
{
   public int radius = 0;

   /**
      Construct a {@code Point} object using an integer index.
      Use the given index for both the {@link renderer.scene.Vertex}
      and the {@link java.awt.Color} lists.
      <p>
      NOTE: This constructor does not put a {@link renderer.scene.Vertex}
      or a {@link java.awt.Color} object into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This constructor assumes that
      the given index is valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param i  index for the {@link renderer.scene.Vertex} and {@link java.awt.Color} of the new {@code Point}
   */
   public Point(final int i)
   {
      this(i, i);
   }


   /**
      Construct a {@code Point} object using two integer indices, one
      for the {@link renderer.scene.Vertex} list and one for the
      {@link java.awt.Color} list.
      <p>
      NOTE: This constructor does not put a {@link renderer.scene.Vertex}
      or a {@link java.awt.Color} object into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This constructor assumes that
      the given indices are valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param v  index for the {@link renderer.scene.Vertex} of the new {@code Point}
      @param c  index for the {@link java.awt.Color} of the new {@code Point}
   */
   public Point(final int v, final int c)
   {
      super();

      vIndexList.add(v);
      cIndexList.add(c);
   }


   /**
      Construct a {@code Point} object using the two given
      {@link List}s of integer indices.
      <p>
      NOTE: This constructor does not put any {@link renderer.scene.Vertex}
      or {@link java.awt.Color} objects into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This constructor assumes that
      the given indices are valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param vIndexList  {@link List} of integer indices into a {@link renderer.scene.Vertex} list
      @param cIndexList  {@link List} of integer indices into a {@link java.awt.Color} list
      @throws NullPointerException if {@code vIndexList} is {@code null}
      @throws NullPointerException if {@code cIndexList} is {@code null}
      @throws IllegalArgumentException if the size of {@code vIndexList} or {@code cIndexList} is not 1
   */
   public Point(final List<Integer> vIndexList,
                final List<Integer> cIndexList)
   {
      super(vIndexList, cIndexList);

      if ( 1 != vIndexList.size() )
         throw new IllegalArgumentException("the vertex index list must have length 1");
      if ( 1 != cIndexList.size() )
         throw new IllegalArgumentException("the color index list must have length 1");
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Point} object
   */
   @Override
   public String toString()
   {
      return "Point: ([" + vIndexList.get(0) + "], "
                   + "[" + cIndexList.get(0) + "]) radius = " + radius;
   }
}
