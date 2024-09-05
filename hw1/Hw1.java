/*


*/

import framebuffer.FrameBuffer;

import java.awt.Color;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
   Outline of CS 45500 Assignment 1.
*/
public class Hw1
{
   
   public static void main(String[] args)
   {
      // Use a properties file to find out
      // which PPM files to use as assets.
      final Properties properties = new Properties();
      try
      {
         properties.load(
            new FileInputStream(
               new File("assets.properties")));  //TODO: this is broken for some reason?
      }
      catch (IOException e)
      {
         e.printStackTrace(System.err);
         System.exit(-1);
      }
      final String file_1 = properties.getProperty("file1"); // 1st ppm file
      final String file_2 = properties.getProperty("file2"); // 2nd ppm file

      // This framebuffer holds the image that will be embedded
      // into two viewports of the larger framebuffer.
      final FrameBuffer fbEmbedded = new FrameBuffer(file_1);

      /******************************************/

      // Your code goes here.
      //  1. Create a 1100-by-700 framebuffer with the appropriate background color.
      //  2. Fill the framebuffer with the checkerboard pattern.
      //  3. Put a one pixel wide border around the checkerboard.
      //  4. Put four one pixel wide diagonals at the four coners of the checkerboard.
      //  5. Create a viewport and put in it the striped pattern.
      //  6. Create a viewport and put in it the striped disk pattern.
      //  7. Create a viewport and put in it a flipped copy of the 1st ppm file.
      //  8. Create another viewport put in it another flipped copy of the 1st ppm file.
      //  9. Create a viewport that covers the 6 checkerboard squares that need to be copied.
      // 10. Create another viewport to hold a "framed" copy of the previous viewport.
      //     Give this viewport a grayish background color.
      // 11. Create another viewport within the last, gray viewport, and initialize
      //     it to hold a copy of the viewport from step 9.
      // 12. Load Dumbledore (the 2nd ppm file) into another FrameBuffer.
      // 13. Create a viewport to hold Dumbledore's ghost. --blending
      // 14. Blend Dumbledore from its framebuffer into the viewport.

      //1 1100x700 fb
      Color bg = new Color(192,56,14); //off red orange, backround color
      final FrameBuffer fb = new FrameBuffer(1100,700, bg); 


      //2 checkerboard, 100x100 squares, 50 off border
      Color checker = new Color(255,189,96); //yellowy tan oragne, checkerboard & border color
      Color test = new Color(0,255,255); //test color for new shapes so i can see them :) //TODO: get rid of

      // for(int i = 50; i<150; i++) {
      //    for(int j = 50; j<150; j++) {
      //       fb.setPixelFB(i,j,test);
      //    }
      // }

      // for(int h = 50; h < 1050; h++) {
      //    for(int v = 50; v < 650; v++) {
      //       if(h!=50 && h%50 == 0 && v!=50 && v%50 == 0) {
      //          fb.setPixelFB(h, v, test);
      //       }
      //    }
      // }

      //3   50px from edge
      for(int h = 50; h < 1050; h++) { //top & bottom line
         fb.setPixelFB(h,49,checker); //top
         fb.setPixelFB(h,650,checker); //bottom
      }
      for(int v = 50; v < 650; v++) { //left & right line
         fb.setPixelFB(49,v,checker); //left
         fb.setPixelFB(1050,v+1,checker); //right
      }
      
      //4 diagonals
      for(int i = 50; i > 0; i--) { //top left
         fb.setPixelFB(50-i,50-i,test);
      }
      
      for(int i = 0; i < 50; i++) { //bottom left
         fb.setPixelFB(49-i,650+i,test);
      }

      for(int i = 0; i < 50; i++) { //top right
         fb.setPixelFB(1050+i,50-i,test);
      }
      
      for(int i = 49; i > 0; i--) { //bottom right
         fb.setPixelFB(1050+i,650+i,test);
      }


      //5 viewport striped pattern

      



      /******************************************/
      // Save the resulting image in a file.
      final String savedFileName = "Hw1.ppm";
      fb.dumpFB2File( savedFileName );
      System.err.println("Saved " + savedFileName);
   }
}
