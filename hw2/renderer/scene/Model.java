/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene;

import renderer.scene.primitives.*;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

/**
   A {@code Model} object represents a distinct geometric object in a
   {@link Scene}. A {@code Model} data structure is mainly a {@link List}
   of {@link Vertex} objects, a {@link List} of {@link Primitive} objects,
   and a list of {@link Color} objects.
<p>
   Each {@link Vertex} object contains the xyz-coordinates, in the
   {@code Model}'s own coordinate system, for one point from the
   {@code Model}.
<p>
   Each {@link Color} object represents a color associated to one
   (or more) {@link Vertex} objects.
<p>
   The {@link Vertex} objects represents points from the geometric object
   that we are modeling. In the real world, a geometric object has an infinite
   number of points. In 3D graphics, we "approximate" a geometric object by
   listing just enough points to adequately describe the object. For example,
   in the real world, a rectangle contains an infinite number of points, but
   it can be adequately modeled by just its four corner points. (Think about
   a circle. How many points does it take to adequately model a circle? Look
   at the {@link renderer.models_L.Circle} model.)
<p>
   Each {@link Primitive} object is either a {@link LineSegment} or a
   {@link Point}.
<p>
   Each {@link LineSegment} object contains four integers, two integers that
   are the indices of two {@link Vertex} objects from the {@code Model}'s
   vertex list, and two integers that are indices of two {@link Color}
   objects from the {@link Model}'s color list. The two vertices are the
   line segment's two endpoints, and each of the two colors is associated
   with one of the two endpoints.
<p>
   Each {@link Point} object contains two integers, one integer index of
   a {@link Vertex} from the {@code Model}'s vertex list, and one integer
   index of a {@link Color} from the {@code Model}'s color list.
<p>
   We use {@link LineSegment} objects to represent the space between the
   model's vertices. For example, while a rectangle can be approximated by
   its four corner points, those same four points could also represent two
   parallel line segments, or they could represent two lines that cross each
   other. By using four line segments that connect around the four points,
   we get a good, unambiguous representation of a rectangle.
<p>
   If we modeled a circle using just points, we would probably need to use
   hundreds of points. But if we connect every two adjacent points with a
   short line segment, we can get a good model of a circle with just a few
   dozen points.
<p>
   Our {@code Model}'s represent geometric objects as a "wire-frame" of line
   segments, that is, a geometric object is drawn as a collection of "edges".
   This is a fairly simplistic way of doing 3D graphics and we will improve
   this in later renderers.
<p>
   See
<br> <a href="https://en.wikipedia.org/wiki/Wire-frame_model" target="_top">
              https://en.wikipedia.org/wiki/Wire-frame_model</a>
<br>or
<br> <a href="https://www.google.com/search?q=computer+graphics+wireframe&tbm=isch" target="_top">
              https://www.google.com/search?q=computer+graphics+wireframe&tbm=isch</a>
*/
public class Model
{
   public final List<Vertex> vertexList;
   public final List<Primitive> primitiveList;
   public final List<Color> colorList;
   public final String name;

   public boolean visible;

   /**
      Construct an empty {@code Model} object.
   */
   public Model()
   {
      this(new ArrayList<>(),  // vertexList
           new ArrayList<>(),  // primitiveList
           new ArrayList<>(),  // colorList
           "",                 // name
           true);              // visible
   }


   /**
      Construct an empty {@code Model} object with the given
      {link String} name.

      @param name  a {link String} that is a name for this {@code Model}
      @throws NullPointerException if {@code name} is {@code null}
   */
   public Model(final String name)
   {
      this(new ArrayList<>(),  // vertexList
           new ArrayList<>(),  // primitiveList
           new ArrayList<>(),  // colorList
           name,               // name
           true);              // visible
   }


   /**
      Construct a {@code Model} object with all the given data.

      @param vertexList     a {@link Vertex} {link List} for this {@code Model}
      @param primitiveList  a {@link Primitive} {link List} for this {@code Model}
      @param colorList      a {@link Color} {link List} for this {@code Model}
      @param name           a {link String} that is a name for this {@code Model}
      @param visible        a {@code boolean} that determines this {@code Model}'s visibility
      @throws NullPointerException if {@code vertexList} is {@code null}
      @throws NullPointerException if {@code primitiveList} is {@code null}
      @throws NullPointerException if {@code colorList} is {@code null}
      @throws NullPointerException if {@code name} is {@code null}
   */
   public Model(final List<Vertex> vertexList,
                final List<Primitive> primitiveList,
                final List<Color> colorList,
                final String name,
                final boolean visible)
   {
      if (null == vertexList)
         throw new NullPointerException("vertexList must not be null");
      if (null == primitiveList)
         throw new NullPointerException("primitiveList must not be null");
      if (null == colorList)
         throw new NullPointerException("colorList must not be null");
      if (null == name)
         throw new NullPointerException("name must not be null");

      this.vertexList = vertexList;
      this.primitiveList = primitiveList;
      this.colorList = colorList;
      this.name = name;
      this.visible = visible;
   }


   /**
      Add a {@link Vertex} (or vertices) to this {@code Model}'s
      {@link List} of vertices.

      @param vArray  array of {@link Vertex} objects to add to this {@code Model}
      @throws NullPointerException if any {@link Vertex} is {@code null}
   */
   public final void addVertex(final Vertex... vArray)
   {
      for (final Vertex v : vArray)
      {
         if (null == v)
            throw new NullPointerException("Vertex must not be null");

         vertexList.add( v );
      }
   }


   /**
      Get a {@link Primitive} from this {@code Model}'s
      {@link List} of primitives.

      @param index  integer index of a {@link Primitive} from this {@code Model}
      @return the {@link Primitive} object at the given index
   */
   public final Primitive getPrimitive(final int index)
   {
      return primitiveList.get(index);
   }


   /**
      Add a {@link Primitive} (or Primitives) to this {@code Model}'s
      {@link List} of primitives.
      <p>
      NOTE: This method does not add any vertices to the {@code Model}'s
      {@link Vertex} list. This method assumes that the appropriate vertices
      have been added to the {@code Model}'s {@link Vertex} list.

      @param pArray  array of {@link Primitive} objects to add to this {@code Model}
      @throws NullPointerException if any {@link Primitive} is {@code null}
   */
   public final void addPrimitive(final Primitive... pArray)
   {
      for (final Primitive p : pArray)
      {
         if (null == p)
            throw new NullPointerException("Primitive must not be null");

         primitiveList.add(p);
      }
   }


   /**
      Add a {@link Color} (or colors) to this {@code Model}'s
      {@link List} of colors.

      @param cArray  array of {@link Color} objects to add to this {@code Model}
      @throws NullPointerException if any {@link Color} is {@code null}
   */
   public final void addColor(final Color... cArray)
   {
      for (final Color c : cArray)
      {
         if (null == c)
            throw new NullPointerException("Color must not be null");

         this.colorList.add(c);
      }
   }


   /**
      For debugging.

      @return {@link String} representation of this {@code Model} object
   */
   @Override
   public String toString()
   {
      String result = "";
      result += "Model: " + name + "\n";
      result += "This Model's visibility is: " + visible + "\n";
      result += "Model has " + vertexList.size() + " vertices.\n";
      result += "Model has " + colorList.size() + " colors.\n";
      result += "Model has " + primitiveList.size() + " primitives.\n";
      int i = 0;
      for (final Vertex v : this.vertexList)
      {
         result += i + ": " + v.toString() + "\n";
         ++i;
      }
      i = 0;
      for (final Color c : this.colorList)
      {
         result += i + ": " + c.toString() + "\n";
         ++i;
      }
      i = 0;
      for (final Primitive p : this.primitiveList)
      {
         result += i + ": " + p.toString() + "\n";
         ++i;
      }
      return result;
   }
}
