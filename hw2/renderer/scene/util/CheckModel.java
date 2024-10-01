/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.util;

import renderer.scene.*;
import renderer.scene.primitives.*;

/**
   Several static utility methods for checking
   and/or debugging a {@link Model}.
*/
public class CheckModel
{
   /**
      Determine if there are any obvious problems with the {@link Model}
      to be rendered. The purpose of these checks is to make the renderer
      a bit more user friendly. If a user makes a simple mistake and tries
      to render a {@link Model} that is missing vertices or line segments,
      or colors, then the user gets a helpful error message.

      @param model  the {@link Model} to be checked
   */
   public static void check(final Model model)
   {
      boolean error = false;
      if (model.vertexList.isEmpty() && ! model.primitiveList.isEmpty())
      {
         System.err.println(
            "***WARNING: This model does not have any vertices.");
         error = true;
      }
      if (! model.vertexList.isEmpty() && model.primitiveList.isEmpty())
      {
         System.err.println(
            "***WARNING: This model does not have any geometric primitives.");
         error = true;
      }
      if (! model.vertexList.isEmpty() && model.colorList.isEmpty())
      {
         System.err.println(
            "***WARNING: This model does not have any colors.");
         //error = true;
      }
      if (error)
      {
         System.err.println(model);
      }
   }


   /**
      Check each {@link Primitive} in the {@link Model} to make sure that
      each index in the {@link Primitive}'s {@code vIndexList} refers to a
      valid {@link Vertex} in the {@link Model}'s {@code vertexList} and
      also that each index in the {@link Primitive}'s {@code cIndexList}
      refers to a valid {@link java.awt.Color} in the {@link Model}'s
      {@code colorList}

      @param model  the {@link Model} to be checked for consistent indexes
      @return true if no problem is found, false if an invalid index is found
   */
   public static boolean checkPrimitives(final Model model)
   {
      final int numberOfVertices = model.vertexList.size();

      boolean result = true;
      for (final Primitive p : model.primitiveList)
      {
         for (int i = 0; i < p.vIndexList.size(); ++i)
         {
            if (i >= numberOfVertices)
            {
               System.out.println("This Primitive has invalid vertex index: " + i);
               System.out.println( p );
               result = false;
            }
         }
      }

      final int numberOfColors = model.colorList.size();

      for (final Primitive p : model.primitiveList)
      {
         for (int i = 0; i < p.cIndexList.size(); ++i)
         {
            if (i >= numberOfColors)
            {
               System.out.println("This Primitive has invalid color index: " + i);
               System.out.println( p );
               result = false;
            }
         }
      }
      return result;
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private CheckModel() {
      throw new AssertionError();
   }
}