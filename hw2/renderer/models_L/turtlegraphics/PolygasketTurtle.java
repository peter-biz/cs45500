/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**
   https://www.clear.rice.edu/comp360/lectures/fall2008/TurtleFractalsL2New.pdf#page=6
*/
public class PolygasketTurtle extends Turtle
{
   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of sides in the N-gon
      @param m      number of levels for the Polygasket
   */
   public PolygasketTurtle(final Model model, final int n, final int m)
   {
      this(model, n, m, 0.0, 0.0, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of sides in the N-gon
      @param m      number of levels for the Polygasket
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
   */
   public PolygasketTurtle(final Model model, final int n, final int m,
                           final double xPos, final double yPos)
   {
      this(model, n, m, xPos, yPos, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of sides in the N-gon
      @param m      number of levels for the Polygasket
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
      @param z      the z-plane for this {@code Turtle}
   */
   public PolygasketTurtle(final Model model, final int n, final int m,
                           final double xPos, final double yPos, final double z)
   {
      super(model, xPos, yPos, z);
      polygasket(n, m);
   }


   private void polygasket(final int n, final int level)
   {
      if (0 == level)
      {
         // draw a N-gon
         for (int i = 0; i < n; ++i)
         {
            forward(1);
            turn(360.0/n);
         }
      }
      else
      {
         for (int i = 0; i < n; ++i)
         {
            resize(0.5);
            polygasket(n, level - 1);
            resize(2.0);
            move(1);
            turn(360.0/n);
         }
      }
   }

}//PolygasketTurtle
