/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.scene.util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
   Use a {@link Properties} file to find the path to the assets directory.
*/
public class Assets
{
   /**
      Use a {@link Properties} file to find the path to the assets directory.

      @return a {@link String} containing the path to the assets folder
   */
   public static String getPath()
   {
      final Properties properties = new Properties();
      try
      {
         properties.load(
            new FileInputStream(
               new File("assets.properties")));
      }
      catch (IOException e)
      {
         e.printStackTrace(System.err);
         System.exit(-1);
      }
      return properties.getProperty("assets");
   }



   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private Assets() {
      throw new AssertionError();
   }
}
