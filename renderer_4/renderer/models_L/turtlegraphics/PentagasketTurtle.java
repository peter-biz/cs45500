/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**

*/
public class PentagasketTurtle extends Turtle
{
   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of levels for the Pentagasket
   */
   public PentagasketTurtle(final Model model, final int n)
   {
      this(model, n, 0.0, 0.0, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of levels for the Pentagasket
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
   */
   public PentagasketTurtle(final Model model, final int n,
                            final double xPos, final double yPos)
   {
      this(model, n, xPos, yPos, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of levels for the Pentagasket
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
      @param z      the z-plane for this {@code Turtle}
   */
   public PentagasketTurtle(final Model model, final int n,
                            double xPos, double yPos, double z)
   {
      super(model, xPos, yPos, z);
      pentagasket(n);
   }


   private void pentagasket(final int level)
   {
      if (0 == level)
      {
         // draw a pentagon
         for (int i = 0; i < 5; ++i)
         {
            forward(1);
            turn(72);
         }
      }
      else
      {
         for (int i = 0; i < 5; ++i)
         {
            resize(0.5);
            pentagasket(level - 1);
            resize(2.0);
            move(1);
            turn(72);
         }
      }
   }

}//PentagasketTurtle
