/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

import java.awt.Color;

/**
   Create a positive x, y, and z axis in 3-dimensional space.
*/
public class Axes3D extends Model
{
   /**
      Create a positive x, y, and z axis
      with one unit length for each axis.
      The default {@link Color} is white.
   */
   public Axes3D( )
   {
      this(1.0, 1.0, 1.0);
   }


   /**
      Create a positive x, y, and z axis
      with the given length for each axis.
      The default {@link Color} is white.

      @param xMax  length of the x-axis
      @param yMax  length of the y-axis
      @param zMax  length of the z-axis
   */
   public Axes3D(final double xMax, final double yMax, final double zMax)
   {
      this(xMax, yMax, zMax, Color.white);
   }


   /**
      Create a positive x, y, and z axis
      with the given length for each axis.
      Use the given {@link Color} for all three axes.

      @param xMax  length of the x-axis
      @param yMax  length of the y-axis
      @param zMax  length of the z-axis
      @param c     {@link Color} for all three axes
   */
   public Axes3D(final double xMax, final double yMax, final double zMax,
                 final Color c)
   {
      this(xMax, yMax, zMax, c, c, c);
   }


   /**
      Create a positive x, y, and z axis
      with the given length for each axis.
      Use the given {@link Color} for each axis.

      @param xMax  length of the x-axis
      @param yMax  length of the y-axis
      @param zMax  length of the z-axis
      @param cX    {@link Color} for the x-axis
      @param cY    {@link Color} for the y-axis
      @param cZ    {@link Color} for the z-axis
   */
   public Axes3D(final double xMax, final double yMax, final double zMax,
                 final Color cX,    final Color cY,    final Color cZ)
   {
      this(0.0, xMax, 0.0, yMax, 0.0, zMax, cX, cY, cZ);
   }


   /**
      Create an x, y, and z axis with the
      given endpoints for each axis.
      The default {@link Color} is black.

      @param xMin  left endpoint of the x-axis
      @param xMax  right endpoint of the x-axis
      @param yMin  bottom endpoint of the y-axis
      @param yMax  top endpoint of the y-axis
      @param zMin  back endpoint of the z-axis
      @param zMax  front endpoint of the z-axis
   */
   public Axes3D(final double xMin, final double xMax,
                 final double yMin, final double yMax,
                 final double zMin, final double zMax)
   {
      this(xMin, xMax, yMin, yMax, zMin, zMax, Color.black);
   }


   /**
      Create an x, y, and z axis with the
      given endpoints for each axis.
      Use the given {@link Color} for all three axes.

      @param xMin  left endpoint of the x-axis
      @param xMax  right endpoint of the x-axis
      @param yMin  bottom endpoint of the y-axis
      @param yMax  top endpoint of the y-axis
      @param zMin  back endpoint of the z-axis
      @param zMax  front endpoint of the z-axis
      @param c     {@link Color} for all three axes
   */
   public Axes3D(final double xMin, final double xMax,
                 final double yMin, final double yMax,
                 final double zMin, final double zMax,
                 final Color c)
   {
      this(xMin, xMax, yMin, yMax, zMin, zMax, c, c, c);
   }


   /**
      Create an x, y, and z axis with the
      given endpoints for each axis.
      Use the given {@link Color} for each axis.

      @param xMin  left endpoint of the x-axis
      @param xMax  right endpoint of the x-axis
      @param yMin  bottom endpoint of the y-axis
      @param yMax  top endpoint of the y-axis
      @param zMin  back endpoint of the z-axis
      @param zMax  front endpoint of the z-axis
      @param cX    {@link Color} for the x-axis
      @param cY    {@link Color} for the y-axis
      @param cZ    {@link Color} for the z-axis
   */
   public Axes3D(final double xMin, final double xMax,
                 final double yMin, final double yMax,
                 final double zMin, final double zMax,
                 final Color cX, final Color cY, final Color cZ)
   {
      super("Axes 3D");

      addVertex(new Vertex(xMin, 0,    0),
                new Vertex(xMax, 0,    0),
                new Vertex( 0,  yMin,  0),
                new Vertex( 0,  yMax,  0),
                new Vertex( 0,   0,   zMin),
                new Vertex( 0,   0,   zMax));

      addColor(cX, cY, cZ);

      addPrimitive(new LineSegment(0, 1, 0),  // use color cX
                   new LineSegment(2, 3, 1),  // use color cY
                   new LineSegment(4, 5, 2)); // use color cZ
   }
}//Axes3D
