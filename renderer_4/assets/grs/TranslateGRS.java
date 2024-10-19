/*

*/

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**

*/
public class TranslateGRS
{
   public static void main(String[] args)
   {
      // Check for a file name on the command line.
      if ( 0 == args.length || 3 < args.length )
      {
         System.err.println("Usage: java TranslateGRS <GRS-file-name> [<x-amount> [<y-amount>]]");
         System.exit(-1);
      }

      // Open the file named on the command line.
      FileInputStream fis = null;
      try
      {
         fis = new FileInputStream(args[0]);
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace(System.err);
         System.err.printf("ERROR! Could not open GRS file: %s\n", args[0]);
         System.exit(-1);
      }

      double xFactor = 0.0;
      double yFactor = 0.0;
      try
      {
         if ( 2 <= args.length )
            xFactor = Double.parseDouble( args[1] );
         if ( 3 <= args.length )
            yFactor = Double.parseDouble( args[2] );
      }
      catch (NumberFormatException e)
      {
         e.printStackTrace(System.err);
         System.err.println("Usage: java TranslateOBJ <GRS-file-name> [<x-amount> [<y-amount>]]");
         System.exit(-1);
      }

      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      try
      {
         final Scanner scanner = new Scanner(fis);

         // skip over the comment lines
         String line = scanner.nextLine();
         while ( ! line.startsWith("*") )
         {
            //System.err.println(line);
            line = scanner.nextLine();
         }

         // read the figure extents
         final double left   = scanner.nextDouble();
         final double top    = scanner.nextDouble();
         final double right  = scanner.nextDouble();
         final double bottom = scanner.nextDouble();
         System.err.printf("Extent: % .5f  % .5f  % .5f  % .5f\n", left, top, right, bottom);

         // read the number of line-strips
         final int numLineStrips = scanner.nextInt();
         //System.err.println("number of line strips = " + numLineStrips);

         // read each line-strip
         for(int j = 0; j < numLineStrips; j++)
         {
            // read the number of vertices in this line-strip
            final int numVertices = scanner.nextInt();
            //System.err.println("number of vertices = " + numVertices);
            for (int i = 0; i < numVertices; i++)
            {
               final double x = scanner.nextDouble();
               final double y = scanner.nextDouble();
               if ( x > maxX ) maxX = x;
               if ( y > maxY ) maxY = y;
               if ( x < minX ) minX = x;
               if ( y < minY ) minY = y;
               //System.err.printf("% .4f  % .4f\n", x, y);
            }
         }
         fis.close();
      }
      catch (IOException e)
      {
         System.err.printf("ERROR! Could not read GRS file: %s\n", args[0]);
         e.printStackTrace(System.err);
         System.exit(-1);
      }

      System.err.printf("max % .4f  % .4f\n", maxX, maxY);
      System.err.printf("min % .4f  % .4f\n", minX, minY);


      if ( 1 < args.length )
      {
         // Build a name for the output file.
         String filename = args[0].substring( 1 + args[0].indexOf('\\'), args[0].indexOf(".") );
         filename += "_.grs";

         // Check if the output file already exits.
         try
         {
            fis = new FileInputStream(filename);
            System.err.printf("ERROR! GRS file already exits: %s\n", filename);
            System.exit(-1);
         }
         catch (FileNotFoundException e)
         {
         }

         // Create the output file.
         FileOutputStream fos = null;
         try  // open the file
         {
            fos = new FileOutputStream(filename);
         }
         catch (FileNotFoundException e)
         {
            e.printStackTrace(System.err);
            System.err.printf("ERROR! Could not open file %s\n", filename);
            System.exit(-1);
         }
         System.err.printf("Created file %s\n", filename);


         // Open the file named on the command line.
         try
         {
            fis = new FileInputStream(args[0]);
         }
         catch (FileNotFoundException e)
         {
            e.printStackTrace(System.err);
            System.err.printf("ERROR! Could not open GRS file: %s\n", args[0]);
            System.exit(-1);
         }


         // Re-read the input file and translate it into the output file.
         try
         {
            PrintStream ps = new PrintStream( fos );
            final Scanner scanner = new Scanner(fis);

            // copy over the comment lines
            String line = null;
            do
            {
               line = scanner.nextLine();
               ps.println( line );
            }
            while ( ! line.startsWith("*") );

            // copy the figure extents
            final double left   = scanner.nextDouble();
            final double top    = scanner.nextDouble();
            final double right  = scanner.nextDouble();
            final double bottom = scanner.nextDouble();

            ps.printf("% .6f  % .6f  % .6f  % .6f\n", left   + xFactor,
                                                      top    + yFactor,
                                                      right  + xFactor,
                                                      bottom + yFactor);

            // copy the number of line-strips
            final int numLineStrips = scanner.nextInt();
            ps.println( numLineStrips );

            // copy each line-strip
            for(int j = 0; j < numLineStrips; j++)
            {
               // copy the number of vertices in this line-strip
               final int numVertices = scanner.nextInt();
               ps.println( numVertices );
               for (int i = 0; i < numVertices; i++)
               {
                  final double x = xFactor + scanner.nextDouble();
                  final double y = yFactor + scanner.nextDouble();
                  ps.printf("  % .4f  % .4f\n", x, y);
               }
            }
            fis.close();
            fos.close();
         }
         catch (IOException e)
         {
            System.err.printf("ERROR! Could not write GRS file: %s\n", filename);
            e.printStackTrace(System.err);
            System.exit(-1);
         }
      }
   }
}
