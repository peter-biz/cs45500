/*
 * Renderer Models. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.models_L;

import renderer.scene.*;
import renderer.scene.primitives.*;

import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
   Create a wirefram model from a GRS file.
<p>
   GRS files are a simple file format for describing two-dimensional
   drawings made up of "polylines". The format was created for the textbook
   "Computer Graphics Using OpenGL", 3rd Ed, by Francis S Hill
   and Stephen M Kelley (see pages 61-63).
<p>
   See <a href="https://en.wikipedia.org/wiki/Polyline" target="_top">
                https://en.wikipedia.org/wiki/Polyline</a>
<p>
   The structure of a GRS file is:
   <ol>
   <li>A number of comment lines followed by a line
       starting with an asterisk, {@code '*'}.
   <li>A line containing the "extent" (bounding box)
       of the drawing given as four doubles in model
       coordinates (left, top, right, bottom).
   <li>The number of line-strips (i.e., polylines)
       in the drawing.
   <li>The list of line-strips. Each line-strip starts
       with the number of vertices in the line-strip,
       followed by the (x, y) model coordinates for
       each vertex.
   </ol>
*/
public class GRSModel extends Model
{
   // the figure's extents (bounding box)
   public double left   = 0.0;
   public double top    = 0.0;
   public double right  = 0.0;
   public double bottom = 0.0;
   public int numLineStrips = 0;

   /**
      Create a wireframe model from the contents of an GRS file.

      @param grsFile  {@link File} object for the GRS data file
   */
   public GRSModel(final File grsFile)
   {
      super(grsFile.getPath());

      // Open the GRS file.
      FileInputStream fis = null;
      try
      {
         fis = new FileInputStream( grsFile );
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace(System.err);
         System.err.printf("ERROR! Could not find GRS file: %s\n", grsFile);
         System.exit(-1);
      }

      final Scanner scanner = new Scanner(fis);

      // Get the geometry from the GRS file.
      try
      {
         // Skip over the comment lines.
         String line = scanner.nextLine();
         while ( ! line.startsWith("*") )
         {
            //System.err.println(line);
            line = scanner.nextLine();
         }

         // Read the figure extents.
         this.left   = scanner.nextDouble();
         this.top    = scanner.nextDouble();
         this.right  = scanner.nextDouble();
         this.bottom = scanner.nextDouble();

         // Read the number of line-strips.
         this.numLineStrips = scanner.nextInt();

         int index = -1;

         // Read each line-strip.
         for(int j = 0; j < this.numLineStrips; ++j)
         {
            // Read the number of vertices in this line-strip.
            final int numVertices = scanner.nextInt();

            // Put this line-strip in the Model object.
            double x = scanner.nextDouble(); // read the first vertex in the line-strip
            double y = scanner.nextDouble();
            addVertex( new Vertex(x, y, 0) );
            ++index;
            for (int i = 1; i < numVertices; ++i)
            {
               // Read the next model coordinate pair.
               x = scanner.nextDouble();
               y = scanner.nextDouble();
               addVertex( new Vertex(x, y, 0) );
               ++index;
               // Create a new LineSegment in the Model.
               addPrimitive(new LineSegment(index - 1, index));
            }
         }
         fis.close();
      }
      catch (IOException e)
      {
         e.printStackTrace(System.err);
         System.err.printf("ERROR! Could not read GRS file: %s\n", grsFile);
         System.exit(-1);
      }
   }
}//GRSModel
