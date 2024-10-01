/*
   Read the data from kochcurve.svg, scale it
   and write it in the GRS format.
*/
import java.util.Scanner;
public class kochcurve
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);

      double minX = Double.POSITIVE_INFINITY;
      double maxX = Double.NEGATIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;

      while ( in.hasNext() )
      {
         final double x = in.nextDouble();
         final String comma = in.next();
         final double y = in.nextDouble();

         System.out.printf("%7.4f  % 7.4f\n", x/1057, Math.abs(y/1057));

         if (x < minX) minX = x;
         if (x > maxX) maxX = x;
         if (y < minY) minY = y;
         if (y > maxY) maxY = y;
      }
      System.out.println(minX/maxX + "  " + -minY/maxX + "  " + maxX/maxX + "  " + -maxY/maxX);
   }
}
