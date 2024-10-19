/*

*/

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
   Translate and scale the named OBJ file to have a [-1,1] x [-1,1] x [-1,1] bounding box.
*/
public class UnitizeOBJ
{
   public static void main(String[] args)
   {
      // Check for a file name on the command line.
      if ( 1 != args.length )
      {
         System.err.println("Usage: java UnitizeOBJ <OBJ-file-name>");
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
         System.err.printf("ERROR! Could not open OBJ file: %s\n", args[0]);
         System.exit(-1);
      }

      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      double maxZ = Double.NEGATIVE_INFINITY;
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double minZ = Double.POSITIVE_INFINITY;
      try
      {
         final Scanner scanner = new Scanner(fis);
         while ( scanner.hasNext() )
         {
            final String token = scanner.next();
            if ( token.startsWith("#")
              || token.startsWith("vt")
              || token.startsWith("vn")
              || token.startsWith("f")
              || token.startsWith("s")
              || token.startsWith("g")
              || token.startsWith("o")
              || token.startsWith("usemtl")
              || token.startsWith("mtllib") )
            {
               scanner.nextLine(); // skip over these lines
            }
            else if ( token.startsWith("v") )
            {
               final double x = scanner.nextDouble();
               final double y = scanner.nextDouble();
               final double z = scanner.nextDouble();
               if ( x > maxX ) maxX = x;
               if ( y > maxY ) maxY = y;
               if ( z > maxZ ) maxZ = z;
               if ( x < minX ) minX = x;
               if ( y < minY ) minY = y;
               if ( z < minZ ) minZ = z;
               //System.err.printf("v % .4f  % .4f  % .4f\n", x, y, z);
            }
            else
            {
               final String temp = scanner.nextLine(); // skip over unknown lines
               System.err.println(token + temp);       // and log them
            }
         }
         fis.close();
      }
      catch (IOException e)
      {
         System.err.printf("ERROR! Could not read OBJ file: %s\n", args[0]);
         e.printStackTrace(System.err);
         System.exit(-1);
      }


      final double xFactor = -(maxX + minX)/2.0;
      final double yFactor = -(maxY + minY)/2.0;
      final double zFactor = -(maxZ + minZ)/2.0;
      final double scaleFactor = 1/(Math.max(Math.max(maxX-minX, maxY-minY), maxZ-minZ)/2.0);


      // Build a name for the output file.
      String filename = args[0].substring( 1 + args[0].indexOf('\\'), args[0].indexOf(".") );
      filename += "_.obj";

      // Check if the output file already exits.
      try
      {
         fis = new FileInputStream(filename);
         System.err.printf("ERROR! OBJ file already exits: %s\n", filename);
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
         System.err.printf("ERROR! Could not open OBJ file: %s\n", args[0]);
         System.exit(-1);
      }


      // Re-read the input file and translate it into the output file.
      try
      {
         final PrintStream ps = new PrintStream( fos );
         final Scanner scanner = new Scanner(fis);
         while ( scanner.hasNext() )
         {
            final String token = scanner.next();
            if ( token.startsWith("#")
              || token.startsWith("vt")
              || token.startsWith("vn")
              || token.startsWith("f")
              || token.startsWith("s")
              || token.startsWith("g")
              || token.startsWith("o")
              || token.startsWith("usemtl")
              || token.startsWith("mtllib") )
            {
               final String oneLine = scanner.nextLine();
               ps.println( token + " " + oneLine );
            }
            else if ( token.startsWith("v") )
            {
               double x = scanner.nextDouble();
               double y = scanner.nextDouble();
               double z = scanner.nextDouble();

               x += xFactor;
               y += yFactor;
               z += zFactor;

               x *= scaleFactor;
               y *= scaleFactor;
               z *= scaleFactor;

               ps.printf("v % .6f  % .6f  % .6f\n", x, y, z);

               //System.err.printf("v % .4f  % .4f  % .4f\n", x, y, z);
            }
            else
            {
               final String oneLine = scanner.nextLine();
               ps.println(token + oneLine);
               //System.err.println(token + oneLine);  // and log unknown lines
            }
         }
         fis.close();
         fos.close();
      }
      catch (IOException e)
      {
         System.err.printf("ERROR! Could not write OBJ file: %s\n", filename);
         e.printStackTrace(System.err);
         System.exit(-1);
      }
   }
}
