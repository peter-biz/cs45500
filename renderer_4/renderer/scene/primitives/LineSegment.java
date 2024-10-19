/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.primitives;

import java.util.List;

/**
   A {@code LineSegment} object has four integers that
   represent the endpoints of the line segment and the
   color at each endpoint. Two of the integers are indices
   into the {@link renderer.scene.Vertex} list of a
   {@link renderer.scene.Model} object and the other two
   integers are indices into the {@link java.awt.Color}
   list of that {@link renderer.scene.Model} object.
*/
public class LineSegment extends Primitive
{
   /**
      Construct a {@code LineSegment} object using two integer indices.
      Use the given indices for both the vertex and the color lists.

      @param i0  index of 1st endpoint {@link renderer.scene.Vertex} and color of the new {@code LineSegment}
      @param i1  index of 2nd endpoint {@link renderer.scene.Vertex} and color of the new {@code LineSegment}
   */
   public LineSegment(final int i0, final int i1)
   {
      this(i0, i1, i0, i1);
   }


   /**
      Construct a {@code LineSegment} object using two integer indices
      for the vertices and one integer index for the colors.

      @param i0  index of 1st endpoint {@link renderer.scene.Vertex} of the new {@code LineSegment}
      @param i1  index of 2nd endpoint {@link renderer.scene.Vertex} of the new {@code LineSegment}
      @param c   index of the color of the new {@code LineSegment}
   */
   public LineSegment(final int i0, final int i1,
                      final int c)
   {
      this(i0, i1, c, c);
   }


   /**
      Construct a {@code LineSegment} object using two integer indices
      for the vertices and two integer indices for the colors.
      <p>
      NOTE: This constructor does not put any {@link renderer.scene.Vertex}
      or {@link java.awt.Color} objects into this {@link Primitive}'s
      {@link renderer.scene.Model} object. This constructor assumes that
      the given indices are valid (or will be valid by the time this
      {@link Primitive} gets rendered).

      @param i0  index of 1st endpoint {@link renderer.scene.Vertex} of the new {@code LineSegment}
      @param i1  index of 2nd endpoint {@link renderer.scene.Vertex} of the new {@code LineSegment}
      @param c0  index of 1st endpoint {@link java.awt.Color} of the new {@code LineSegment}
      @param c1  index of 2nd endpoint {@link java.awt.Color} of the new {@code LineSegment}
   */
   public LineSegment(final int i0, final int i1,
                      final int c0, final int c1)
   {
      super();

      vIndexList.add(i0);
      vIndexList.add(i1);
      cIndexList.add(c0);
      cIndexList.add(c1);
   }


   /**
      Construct a {@code LineSegment} object using the two given
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
      @throws IllegalArgumentException if the size of {@code vIndexList} or {@code cIndexList} is not 2
   */
   public LineSegment(final List<Integer> vIndexList,
                      final List<Integer> cIndexList)
   {
      super(vIndexList, cIndexList);

      if ( 2 != vIndexList.size() )
         throw new IllegalArgumentException("the vertex index list must have length 2");
      if ( 2 != cIndexList.size() )
         throw new IllegalArgumentException("the color index list must have length 2");
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code LineSegment} object
   */
   @Override
   public String toString()
   {
      return "LineSegment: ([" + vIndexList.get(0) + ", " + vIndexList.get(1) + "], "
                         + "[" + cIndexList.get(0) + ", " + cIndexList.get(1) + "])";
   }
}
