/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.primitives;

import java.util.List;
import java.util.ArrayList;

/**
   A {@code Primitive} is something that we can build
   geometric shapes out of (a "graphics primitive").
<p>
   See <a href="https://en.wikipedia.org/wiki/Geometric_primitive" target="_top">
                https://en.wikipedia.org/wiki/Geometric_primitive</a>
<p>
   We have two geometric primitives,
   <ul>
   <li>{@link LineSegment},
   <li>{@link Point}.
   </ul>
<p>
   Each {@code Primitive} holds two lists of integer indices.
<p>
   One list is of indices into its {@link renderer.scene.Model}'s
   {@link List} of {@link renderer.scene.Vertex} objects. These
   are the vertices that determine the primitive's geometry.
<p>
   The other list is of indices into its {@link renderer.scene.Model}'s
   {@link List} of {@link java.awt.Color} objects.
<p>
   The two lists of integer indices must always have the same
   length. For every {@link renderer.scene.Vertex} index in a
   {@code Primitive} there must be a {@link java.awt.Color} index.
*/
/*
   NOTE: The Primitive class could be an inner class of
         the Model class. Then each Primitive object would
         automatically have access to the actual Vertex and
         Color lists that the Primitive is indexing into.
*/
public abstract class Primitive
{
   // A Primitive object is made up of indices to vertices and colors in a Model.
   public final List<Integer> vIndexList; // indices for this primitive's vertices
   public final List<Integer> cIndexList; // indices for this primitive's colors

   /**
      Construct an empty {@code Primitive}.
   */
   protected Primitive()
   {
      this.vIndexList = new ArrayList<>();
      this.cIndexList = new ArrayList<>();
   }


   /**
      Construct a {@code Primitive} with the given array of indices for the
      {@link renderer.scene.Vertex} and {@link java.awt.Color} index lists.
      <p>
      NOTE: This constructor does not put any {@link renderer.scene.Vertex}
      or {@link java.awt.Color} objects into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This constructor assumes that
      the given indices are valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param indices  array of {@link renderer.scene.Vertex} and {@link java.awt.Color} indices to place in this {@code Primitive}
   */
   protected Primitive(final int... indices)
   {
      this();

      for (final int i : indices)
      {
         addIndices(i, i);
      }
   }


   /**
      Construct a {@code Primitive} object using the two given
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
      @throws IllegalArgumentException if {@code vIndexList} and {@code cIndexList} are not the same size
   */
   protected Primitive(final List<Integer> vIndexList,
                       final List<Integer> cIndexList)
   {
      if (null == vIndexList)
         throw new NullPointerException("vIndexList must not be null");
      if (null == cIndexList)
         throw new NullPointerException("cIndexList must not be null");
      if (vIndexList.size() != cIndexList.size() )
         throw new IllegalArgumentException("vIndexList and cIndexList must be the same size");

      this.vIndexList = vIndexList;
      this.cIndexList = cIndexList;
   }


   /**
      Add the given array of indices to the {@link renderer.scene.Vertex}
      and {@link java.awt.Color} index lists.
      <p>
      NOTE: This method does not put any {@link renderer.scene.Vertex}
      or {@link java.awt.Color} objects into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This method assumes that
      the given indices are valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param indices  array of {@link renderer.scene.Vertex} and {@link java.awt.Color} indices to add to this {@code Primitive}
   */
   public void addIndex(final int... indices)
   {
      for (final int i : indices)
      {
         addIndices(i, i);
      }
   }


   /**
      Add the given indices to the {@link renderer.scene.Vertex} and
      {@link java.awt.Color} index lists.
      <p>
      NOTE: This method does not put any {@link renderer.scene.Vertex}
      or {@link java.awt.Color} objects into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This method assumes that
      the given indices are valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param vIndex  integer {@link renderer.scene.Vertex} index to add to this {@code Primitive}
      @param cIndex  integer {@link java.awt.Color} index to add to this {@code Primitive}
   */
   public void addIndices(final int vIndex, final int cIndex)
   {
      vIndexList.add(vIndex);
      cIndexList.add(cIndex);
   }


   /**
      Set the {@link java.awt.Color} index list to the given array of indices.
      <p>
      NOTE: This method does not put any {@link java.awt.Color} objects
      into this {@link Primitive}'s {@link renderer.scene.Model} object.
      This method assumes that the given indices are valid (or will be
      valid by the time this {@link Primitive} gets rendered).

      @param cIndices  array of {@link java.awt.Color} indices for this {@code Primitive}
      @throws IllegalArgumentException if {@code cIndices} does not have the correct length for this {@code Primitive}
   */
   public void setColorIndices(final int... cIndices)
   {
      if (vIndexList.size() != cIndices.length )
         throw new IllegalArgumentException("wrong number of color indices for this primitive");

      cIndexList.clear();
      for (int i = 0; i < cIndices.length; ++i)
      {
         cIndexList.add( cIndices[i] );
      }
   }


   /**
      Give this {@code Primitive} the uniform {@link java.awt.Color} indexed
      by the given color index.
      <p>
      NOTE: This method does not put a {@link java.awt.Color} object
      into this {@link Primitive}'s {@link renderer.scene.Model} object.
      This method assumes that the given index is valid (or will be valid
      by the time this {@link Primitive} gets rendered).

      @param cIndex  integer color index to use for this {@code Primitive}'s {@link java.awt.Color}
   */
   public void setColorIndex(final int cIndex)
   {
      for (int i = 0; i < cIndexList.size(); ++i)
      {
         cIndexList.set(i, cIndex);
      }
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Primitive} object
   */
   @Override
   public abstract String toString();
}
