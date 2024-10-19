/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**
   https://www.clear.rice.edu/comp360/lectures/fall2008/TurtleFractalsL2New.pdf#page=5
*/
public class SierpinskiTurtle extends Turtle
{
   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Sierpinski triangle
      @param length  side length
   */
   public SierpinskiTurtle(final Model model, final int n, final double length)
   {
      this(model, n, length, 0.0, 0.0, 0.0);
   }


   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Sierpinski triangle
      @param length  side length
      @param xPos    the intial x-coordinate for this {@link Turtle}
      @param yPos    the intial y-coordinate for this {@link Turtle}
   */
   public SierpinskiTurtle(final Model model, final int n, final double length,
                           final double xPos, final double yPos)
   {
      this(model, n, length, xPos, yPos, 0.0);
   }


   /**
      @param model   a reference to the {@link Model} that this {@code Turtle} is builing
      @param n       number of levels for the Sierpinski triangle
      @param length  side length
      @param xPos    the intial x-coordinate for this {@link Turtle}
      @param yPos    the intial y-coordinate for this {@link Turtle}
      @param z       the z-plane for this {@code Turtle}
   */
   public SierpinskiTurtle(final Model model, final int n, final double length,
                           final double xPos, final double yPos, final double z)
   {
      super(model, xPos, yPos, z);
      sierpinski(n, length);
   }


   private void sierpinski(final int level, final double length)
   {
      if (0 == level)
      {
         // draw an equlateral triangle
         for (int i = 0; i < 3; ++i)
         {
            forward(length);
            turn(120);
         }
      }
      else
      {
         for (int i = 0; i < 3; ++i)
         {
            resize(0.5);
            sierpinski(level - 1, length);
            resize(2.0);
            move(length);
            turn(120);
         }
      }
   }

}//SierpinskiTurtle
