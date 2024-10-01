/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.util;

import renderer.scene.*;
import renderer.scene.primitives.*;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Random;

/**
   This is a library of static methods that
   add color shading to a {@link Model}.
*/
public class ModelShading
{
   /**
      Set each {@link Color} in the {@link Model}'s color list
      to the same {@link Color}.

      @param model  {@link Model} whose color list is being manipulated
      @param c      {@link Color} for all of this model's {@link Vertex} objects
   */
   public static void setColor(final Model model, final Color c)
   {
      if (model.colorList.isEmpty())
      {
         for (int i = 0; i < model.vertexList.size(); ++i)
         {
            model.colorList.add(c);
         }
      }
      else
      {
         for (int i = 0; i < model.colorList.size(); ++i)
         {
            model.colorList.set(i, c);
         }
      }
   }


   /**
      Set each {@link Color} in the {@link Model}'s color list
      to the same random {@link Color}.

      @param model  {@link Model} whose color list is being manipulated
   */
   public static void setRandomColor(final Model model)
   {
      setColor(model, randomColor());
   }


   /**
      Set each {@link Color} in the {@link Model}'s color list
      to a different random {@link Color}.

      @param model  {@link Model} whose color list is being manipulated
   */
   public static void setRandomColors(final Model model)
   {
      if (model.colorList.isEmpty())
      {
         for (int i = 0; i < model.vertexList.size(); ++i)
         {
            model.colorList.add(randomColor());
         }
      }
      else
      {
         for (int i = 0; i < model.colorList.size(); ++i)
         {
            model.colorList.set(i, randomColor());
         }
      }
   }


   /**
      Set each {@link Vertex} in the {@link Model}
      to a different random {@link Color}.
      <p>
      This creates a "rainbow model" effect.
      <p>
      NOTE: This will destroy whatever "color structure"
      the model might possess.

      @param model  {@link Model} whose color list is being manipulated
   */
   public static void setRandomVertexColors(final Model model)
   {
      model.colorList.clear(); // remove all the current colors
      for (int i = 0; i < model.vertexList.size(); ++i)
      {
         model.colorList.add( randomColor() );
      }
      for (final Primitive p : model.primitiveList)
      {
         for (int i = 0; i < p.vIndexList.size(); ++i)
         {
            p.cIndexList.set(i, p.vIndexList.get(i));
         }
      }
   }


   /**
      Set each {@link Primitive} in the {@link Model}
      to a different (uniform) random {@link Color}.
      <p>
      NOTE: This will destroy whatever "color structure"
      the model might possess.

      @param model  {@link Model} whose color list is being manipulated
   */
   public static void setRandomPrimitiveColors(final Model model)
   {
      model.colorList.clear(); // remove all the current colors
      int cIndex = 0;
      for (final Primitive p : model.primitiveList)
      {
         model.colorList.add( randomColor() );
         for (int i = 0; i < p.cIndexList.size(); ++i)
         {
            p.cIndexList.set(i, cIndex);
         }
         ++cIndex;
      }
   }


   /**
      Set each {@link Primitive} in the {@link Model}
      to a different random {@link Color} at each endpoint.
      <p>
      This creates a "rainbow primitive" effect.
      <p>
      NOTE: This will destroy whatever "color structure"
      the model might possess.

      @param model  {@link Model} whose color list is being manipulated
   */
   public static void setRainbowPrimitiveColors(final Model model)
   {
      model.colorList.clear(); // remove all the current colors
      int cIndex = 0;
      for (final Primitive p : model.primitiveList)
      {
         for (int i = 0; i < p.cIndexList.size(); ++i)
         {
            model.colorList.add( randomColor() );
            p.cIndexList.set(i, cIndex);
            ++cIndex;
         }
      }
   }


   /**
      Create a {@link Color} object with randomly generated {@code r},
      {@code g}, and {@code b} values.

      @return a reference to a randomly generated {@link Color} object
   */
   public static Color randomColor()
   {
      final Random generator = new Random();
      final float r = generator.nextFloat();
      final float g = generator.nextFloat();
      final float b = generator.nextFloat();
      return new Color(r, g, b);
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private ModelShading() {
      throw new AssertionError();
   }
}
