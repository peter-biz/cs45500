/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**
   https://en.wikipedia.org/wiki/Sierpi%C5%84ski_curve#Arrowhead_curve
*/
public class SierpinskiCurveTurtle extends Turtle
{
   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Sierpinski curve
      @param length  side length
   */
   public SierpinskiCurveTurtle(final Model model, final int n, final double length)
   {
      this(model, n, length, 0.0, 0.0, 0.0);
   }


   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Sierpinski curve
      @param length  side length
      @param xPos    the intial x-coordinate for this {@link Turtle}
      @param yPos    the intial y-coordinate for this {@link Turtle}
   */
   public SierpinskiCurveTurtle(final Model model, final int n, final double length,
                                final double xPos, final double yPos)
   {
      this(model, n, length, xPos, yPos, 0.0);
   }


   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Sierpinski curve
      @param length  side length
      @param xPos    the intial x-coordinate for this {@link Turtle}
      @param yPos    the intial y-coordinate for this {@link Turtle}
      @param z       the z-plane for this {@code Turtle}
   */
   public SierpinskiCurveTurtle(final Model model, final int n, final double length,
                                final double xPos, final double yPos, final double z)
   {
      super(model, xPos, yPos, z);
      curve(n, length, 60);
   }


   private void curve(final int n, final double length, final int angle)
   {
      if ( 0 == n )
      {
         forward(length);
      }
      else
      {
         turn(angle);
         curve(n - 1, length / 2.0, -angle);
         turn(-angle);
         curve(n - 1, length / 2.0,  angle);
         turn(-angle);
         curve(n - 1, length / 2.0, -angle);
         turn(angle);
      }
   }

}//SierpinskiCurveTurtle


/* Wikipedia version.
   public SierpinskiCurveTurtle(final Model model, final int n, final double length,
                                final double xPos, final double yPos, final double z)
   {
      super(model, xPos, yPos, z);
      if ( 0 == n % 2 )  // n is even
      {
         curve(n, length, 60);
      }
      else  // n is odd
      {
         turn(60);
         curve(n, length, -60);
      }
   }

   private void curve(final int n, final double length, final int angle)
   {
      if ( 0 == n )
      {
         forward(length);
      }
      else
      {
         curve(n - 1, length / 2.0, -angle);
         turn(angle);
         curve(n - 1, length / 2.0,  angle);
         turn(angle);
         curve(n - 1, length / 2.0, -angle);
      }
   }
*/
