/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**
   https://commons.wikimedia.org/wiki/File:Turtle_Graphics_Spiral.svg
*/
public class SpiralTurtle extends Turtle
{
   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of spirals
   */
   public SpiralTurtle(final Model model, final int n)
   {
      this(model, n, 0.0, 0.0, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of spirals
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
   */
   public SpiralTurtle(final Model model, final int n,
                       final double xPos, final double yPos)
   {
      this(model, n, xPos, yPos, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of spirals
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
      @param z      the z-plane for this {@code Turtle}
   */
   public SpiralTurtle(final Model model, final int n,
                       final double xPos, final double yPos, double z)
   {
      super(model, xPos, yPos, z);
      spiral(n);
   }


   private void spiral(final int n)
   {
      for (int i = 0; i < n; ++i)
      {
         forward( 1.0 - ((double)i/(double)n) );
         turn(121);
      }
   }

}//NinjaTurtle
