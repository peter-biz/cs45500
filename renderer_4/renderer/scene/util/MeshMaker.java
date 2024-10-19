/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.util;

import renderer.scene.Model;

/**
   A {@link Model} that implements {@code MeshMaker} can
   rebuild its geometric mesh with different values for
   the number of lines of latitude and longitude while
   keeping all the other model parameters unchanged.
*/
public interface MeshMaker
{
   /**
      @return the number of lines of latitude that the {@link Model} contains
   */
   public int getHorzCount();

   /**
      @return the number of lines of longitude that the {@link Model} contains
   */
   public int getVertCount();

   /**
      Build an instance of the {@link Model} with new values for the number
      of lines of latitude and longitude while keeping all the other model
      parameters the same.

      @param n  number of lines of latitude for the returned {@link Model}
      @param k  number of lines of longitude for the returned {@link Model}
      @return a new instance of the {@link Model} with the updated parameters
   */
   public Model remake(int n, int k);
}
