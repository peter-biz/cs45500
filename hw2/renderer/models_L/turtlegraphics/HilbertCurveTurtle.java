/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**
   https://inventwithpython.com/recursion/chapter9.html#calibre_link-350
*/
public class HilbertCurveTurtle extends Turtle
{
   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Hibert curve
      @param length  side length
   */
   public HilbertCurveTurtle(final Model model, final int n, final double length)
   {
      this(model, n, length, 0.0, 0.0, 0.0);
   }


   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Hibert curve
      @param length  side length
      @param xPos    the intial x-coordinate for this {@link Turtle}
      @param yPos    the intial y-coordinate for this {@link Turtle}
   */
   public HilbertCurveTurtle(final Model model, final int n, final double length,
                            final double xPos, final double yPos)
   {
      this(model, n, length, xPos, yPos, 0.0);
   }


   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Hibert curve
      @param length  side length
      @param xPos    the intial x-coordinate for this {@link Turtle}
      @param yPos    the intial y-coordinate for this {@link Turtle}
      @param z       the z-plane for this {@code Turtle}
   */
   public HilbertCurveTurtle(final Model model, final int n, final double length,
                            final double xPos, final double yPos, final double z)
   {
      super(model, xPos, yPos, z);
      curve(n, 90, length / Math.pow(2, n));
   }


   private void curve(final int n, final double angle, final double length)
   {
      curveQuadrant(n, angle, length);

      forward(length);

      curveQuadrant(n, angle, length);

      turn(-angle);
      forward(length);
      turn(-angle);

      curveQuadrant(n, angle, length);

      forward(length);

      curveQuadrant(n, angle, length);

      turn(-angle);
      forward(length);
      turn(-angle);
   }


   private void curveQuadrant(final int n, final double angle, final double length)
   {
      if (n > 0)
      {
         turn(angle);

         curveQuadrant(n - 1, -angle, length);

         forward(length);
         turn(-angle);

         curveQuadrant(n - 1,  angle, length);

         forward(length);

         curveQuadrant(n - 1,  angle, length);

         turn(-angle);
         forward(length);

         curveQuadrant(n - 1, -angle, length);

         turn(angle);
      }
   }

}//HilbertCurveTurtle
