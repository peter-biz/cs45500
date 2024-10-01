/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L.turtlegraphics;

import renderer.scene.Model;

/**
   https://michael0x2a.com/blog/turtle-examples#example-8-jumping-around-and-changing-speed
*/
public class NinjaTurtle extends Turtle
{
   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of radial lines
   */
   public NinjaTurtle(final Model model, int n)
   {
      this(model, n, 0.0, 0.0, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of radial lines
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
   */
   public NinjaTurtle(final Model model, int n,
                      final double xPos, final double yPos)
   {
      this(model, n, xPos, yPos, 0.0);
   }


   /**
      @param model  a reference to the {@link Model} that this {@code Turtle} is builing
      @param n      number of radial lines
      @param xPos   the intial x-coordinate for this {@link Turtle}
      @param yPos   the intial y-coordinate for this {@link Turtle}
      @param z      the z-plane for this {@code Turtle}
   */
   public NinjaTurtle(final Model model, int n,
                      final double xPos, final double yPos, double z)
   {
      super(model, xPos, yPos, z);
      ninja(n);
   }


   private void ninja(final int n)
   {
      for (int i = 0; i < n; ++i)
      {
         forward(1.0);
         turn(30);
         forward(0.2);
         turn(-60);
         forward(0.5);
         turn(30);

         penUp();
         moveTo(xHome, yHome);
         penDown();

         turn(360 / n);
      }
   }

}//NinjaTurtle
