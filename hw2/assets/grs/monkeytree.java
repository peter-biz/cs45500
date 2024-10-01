/*
   Read the data from monkeytree.svg, scale it
   and write it in the GRS format.
*/
import java.util.Scanner;
public class monkeytree
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);

      double minX = Double.POSITIVE_INFINITY;
      double maxX = Double.NEGATIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;

      System.out.println(1331); // 1,331 polylines

      while ( in.hasNext() )
      {
         System.out.println(12); // 12 vertices in a polyline
         for (int i = 0; i < 12; ++i )
         {
            final int x = in.nextInt();
            final String comma = in.next();
            final int y = in.nextInt();

            System.out.printf("%7.4f  % 7.4f\n", x/1000.0, y/1000.0);

            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
         }
         in.nextLine();
      }
      System.out.println(minX/1000.0 + " " + maxY/1000.0 + "  " + maxX/1000.0 + "  " + minY/1000.0);
   }
}
