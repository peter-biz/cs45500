/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;
import renderer.scene.util.MeshMaker;

/**
   Create a wireframe model of a tetrahedron as a
   triangular pyramid with an equilateral triangle
   base (centered at the origin in the xz-plane)
   whose three vertices are connected to a 4th vertex
   on the positive y-axis.

   @see Tetrahedron
*/
public class TriangularPyramid extends Model implements MeshMaker
{
   public final double r;
   public final double h;
   public final int n;
   public final int k;

   /**
      Create a regular tetrahedron having side length
      {@code sqrt(3)/sqrt(2)}, with one face in the
      xz-plane with its center at the origin, and the
      4th vertex on the positive y-axis at height 1.
   */
   public TriangularPyramid( )
   {
      this(Math.sqrt(3)/Math.sqrt(2)); // makes the height = 1
      //or
      //this(Math.sqrt(3));  // make the height = sqrt(2) > 1
   }


   /**
      Create a regular tetrahedron having side length {@code s},
      with one face in the xz-plane with its center at the origin,
      and with the 4th vertex on the positive y-axis at
      height {@code s*sqrt(2)/sqrt(3)}.

      @param s  the length of the regular tetrahedron's sides
   */
   public TriangularPyramid(final double s)
   {
      this(s/Math.sqrt(3), s*Math.sqrt(2)/Math.sqrt(3));
   }


   /**
      Create a tetrahedron with one face being an equilateral triangle
      inscribed in a circle of radius {@code r} centered at the origin
      of the xz-plane and with the 4th vertex on the y-axis at height
      {@code h}.
   <p>
      If {@code h = r * sqrt(2)}, then the tetrahedron is a regular tetrahedron.
      with side length {@code s = r * sqrt(3)}.
   <p>
      Another way to state this is, if an equilateral triangle is inscribed
      in a circle of radius {@code r}, then the edge length of the triangle
      is {@code r*sqrt(3)} and the height of the regular tetrahedron made
      from the triangle is {@code r*sqrt(2)}.

      @param r  radius of circle in xz-plane that the equilateral base is inscribed in
      @param h  coordinate on the y-axis of the apex
   */
   public TriangularPyramid(final double r, final double h)
   {
      super(String.format("Triangular_Pyramid(%.2f,%.2f)", r, h));

      this.r = r;
      this.h = h;
      this.n = 1;
      this.k = 1;

      // Create the tetrahedron's geometry.
      final double sqrt3 = Math.sqrt(3.0);
      addVertex(new Vertex( r,   0,    0),  // three vertices around the bottom face
                new Vertex(-r/2, 0,  r*0.5*sqrt3),
                new Vertex(-r/2, 0, -r*0.5*sqrt3),
                new Vertex( 0,   h,    0)); // vertex at the top

      // Create 6 line segments for 3 faces.
      addPrimitive(new LineSegment(0, 1),   // bottom face
                     new LineSegment(1, 2),
                     new LineSegment(2, 0),
                     new LineSegment(0, 3),   // edge 1
                     new LineSegment(1, 3),   // edge 2
                     new LineSegment(2, 3));  // edge 3
   }


   /**
      Create a tetrahedron with one face being an equilateral triangle
      inscribed in a circle of radius {@code r} centered at the origin
      of the xz-plane and with the 4th vertex on the y-axis at height
      {@code h}.
   <p>
      If {@code h = r * sqrt(2)}, then the tetrahedron is a regular tetrahedron.
      with side length {@code s = r * sqrt(3)}.
   <p>
      Another way to state this is, if an equilateral triangle is inscribed
      in a circle of radius {@code r}, then the edge length of the triangle
      is {@code r*sqrt(3)} and the height of the regular tetrahedron made
      from the triangle is {@code r*sqrt(2)}.

      @param r  radius of circle in xz-plane that the equilateral base is inscribed in
      @param h  coordinate on the y-axis of the apex
      @param n  number of lines of latitude around the body of the pyramid
      @param k  number of triangles in the triangle fan at the top of each side
      @throws IllegalArgumentException if {@code n} is less than 1
      @throws IllegalArgumentException if {@code k} is less than 1
   */
   public TriangularPyramid(final double r, final double h,
                            final int n, final int k)
   {
      super(String.format("Triangular_Pyramid(%.2f,%.2f,%d,%d)",
                                              r,   h,   n, k));

      if (n < 1)
         throw new IllegalArgumentException("n must be greater than 0");
      if (k < 1)
         throw new IllegalArgumentException("k must be greater than 0");

      this.r = r;
      this.h = h;
      this.n = n;
      this.k = k;

      // Create the pyramid's geometry.
      final Vertex apex = new Vertex(0, h, 0),
           centerVertex = new Vertex(0, 0, 0);
      addVertex(apex,
                centerVertex);
      final int apexIndex = 0,
                centerIndex = 1;
      int index = 2;

      // Create all the lines of "longitude" from the apex, down
      // to the base, and then to the center of the base.
      final double sqrt3 = Math.sqrt(3.0);
      // Three vertices around the bottom face.
      final Vertex v0 = new Vertex( r,   0,    0),
                   v1 = new Vertex(-r/2, 0,  r*0.5*sqrt3),
                   v2 = new Vertex(-r/2, 0, -r*0.5*sqrt3);
      for (int j = 0; j < k; ++j)
      {
         final double t = j * (1.0 / k);
         // use linear interpolation (lerp)
         addVertex( new Vertex(
         //         (1-t)*v0  +  t*v1
                    (1-t)*v0.x + t*v1.x,
                    (1-t)*v0.y + t*v1.y,
                    (1-t)*v0.z + t*v1.z ));
         addVertex( new Vertex(
         //         (1-t)*v1  +  t*v2
                    (1-t)*v1.x + t*v2.x,
                    (1-t)*v1.y + t*v2.y,
                    (1-t)*v1.z + t*v2.z ));
         addVertex( new Vertex(
         //         (1-t)*v2  +  t*v0
                    (1-t)*v2.x + t*v0.x,
                    (1-t)*v2.y + t*v0.y,
                    (1-t)*v2.z + t*v0.z ));

         // first side
         addPrimitive(new LineSegment(apexIndex, index+0),
                      new LineSegment(index+0, centerIndex));
         // second side
         addPrimitive(new LineSegment(apexIndex, index+1),
                      new LineSegment(index+1, centerIndex));
         // third side
         addPrimitive(new LineSegment(apexIndex, index+2),
                      new LineSegment(index+2, centerIndex));

         index += 3;
      }
      // Create all the lines of "latitude" around the pyramid, starting
      // from the base and working upwards.
      for (int i = 0; i < n; ++i)
      {
         final double t = i * (1.0 / n);
         // Use linear interpolation (lerp).
         addVertex( new Vertex(
         //         (1-t)*v0   + t*apex
                    (1-t)*v0.x + t*apex.x,
                    (1-t)*v0.y + t*apex.y,
                    (1-t)*v0.z + t*apex.z ));
         addVertex( new Vertex(
         //         (1-t)*v1   + t*apex
                    (1-t)*v1.x + t*apex.x,
                    (1-t)*v1.y + t*apex.y,
                    (1-t)*v1.z + t*apex.z ));
         addVertex( new Vertex(
         //         (1-t)*v2   + t*apex
                    (1-t)*v2.x + t*apex.x,
                    (1-t)*v2.y + t*apex.y,
                    (1-t)*v2.z + t*apex.z ));

         addPrimitive(new LineSegment(index+0, index+1),
                      new LineSegment(index+1, index+2),
                      new LineSegment(index+2, index+0));

         index += 3;
      }
   }



   // Implement the MeshMaker interface (three methods).
   @Override public int getHorzCount() {return n;}

   @Override public int getVertCount() {return k;}

   @Override
   public TriangularPyramid remake(final int n, final int k)
   {
      return new TriangularPyramid(this.r, this.h,
                                   n, k);
   }
}//TriangularPyramid
