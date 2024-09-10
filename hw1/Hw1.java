/*


*/

import framebuffer.FrameBuffer;

import java.awt.Color;
import java.awt.Frame;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
   Outline of CS 45500 Assignment 1.
*/
public class Hw1
{

/**
 * stripePattern, creates a (3)stripe pattern with given h(x) and v(y) coordinates, color, and framebuffer
 * @param h, horizontal coordinate
 * @param v, vertical coordinate
 * @param c, color
 * @param fb, framebuffer
 */
   private static void stripePattern(int h, int v, Color c, FrameBuffer fb) {
      int x = 0;
      while(x<3) {
         for(int j = h; j < h+30; j++) {
            for(int i = 0; i < 130; i++) {
               fb.setPixelFB(j+i,v+i,c);
            }
         }
         h+=90;
         x++;
      }
   }
   
   public static void main(String[] args)
   {
      // Use a properties file to find out
      // which PPM files to use as assets.
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

         for (int i = 0; i < 10; i++) { //horizontal
          for (int j = 0; j < 6; j++) { //vertical
              int x = 50 + i * 100; //x-coordinate
              int y = 50 + j * 100; //y-coordinate
      
              for (int k = 0; k < 100; k++) { //width
                  for (int l = 0; l < 100; l++) { //height
                      if ((i + j) % 2 == 0) {
                          fb.setPixelFB(x + k, y + l, checker);
                      }
                  }
              }
          }
      }

      //3   50px from edge border
      for(int h = 50; h < 1050; h++) { //top & bottom line
         fb.setPixelFB(h,49,checker); //top
         fb.setPixelFB(h,650,checker); //bottom
      }
      for(int v = 50; v < 650; v++) { //left & right line
         fb.setPixelFB(49,v,checker); //left
         fb.setPixelFB(1050,v+1,checker); //right
      }
      
      //4 diagonals at corners 1px wide
      for(int i = 50; i > 0; i--) { //top left
         fb.setPixelFB(50-i,50-i,checker);
      }
      
      for(int i = 0; i < 50; i++) { //bottom left
         fb.setPixelFB(49-i,650+i,checker);
      }

      for(int i = 0; i < 50; i++) { //top right ---KINDA GOOFY BUT HIS WAS TOO SO IDK
         fb.setPixelFB(1050+i,50-i,checker);
      }
      
      for(int i = 49; i > 0; i--) { //bottom right
         fb.setPixelFB(1050+i,650+i,checker);
      }


      //5 viewport striped pattern
      Color lightRed = new Color(241,95,116);
      Color lightGreen = new Color(152,203,74);
      Color lightBlue = new Color(84,139,230);

      //3 of each stripe
      //top left is 650,475, goes down to y=605 //red
      //top left is 680,475, goes down to y=605 //green
      //top left is 710,475, goes down to y=605 //blue

      stripePattern(650,475,lightRed,fb);
      stripePattern(680,475,lightGreen,fb);
      stripePattern(710,475,lightBlue,fb);

      //6 viewport striped disk pattern
      FrameBuffer.Viewport disk = fb.new Viewport(200,400,300,300); //centered at 350,550

      for(int i = 0; i < 300; i++) { //horizontal
         for(int j = 0; j < 300; j++) { //vertical
            int x = 200 + i; //x-coordinate
            int y = 400 + j; //y-coordinate
            double distance = Math.sqrt(Math.pow((x-350),2) + Math.pow((y-550),2)); //distance from center
            if (distance >= 60) { 
               if (distance < 90) { 
                  disk.setPixelVP(i, j, lightGreen); 
               } 
               else if (distance < 120) {
                  disk.setPixelVP(i, j, lightRed);
               } 
                else if (distance < 150) { 
                  disk.setPixelVP(i, j, lightBlue);
               }
            }
         }
      }

      //7 viewport flipped copy of 1st ppm file
      //topleft = 125,175
      //botrihgt = 380,430 //256x256 image size, check if current pixel is white, if so, set to pixel from fbEmbedded
      FrameBuffer.Viewport vp1 = fb.new Viewport(125,175,256,256);

      for(int i = 0; i < 256; i++) {  //flip horizontally
         for(int j = 0; j < 256; j++) { 
            Color c = fb.getPixelFB(i,j);
            vp1.setPixelVP(i,j,fbEmbedded.getPixelFB(255-i,255-j));
            if(vp1.getPixelVP(i,j).equals(Color.WHITE)) {
               vp1.setPixelVP(i,j,c);
            }

         }
      }

      for (int i = 0; i < 256; i++) { //flip vertically
         for (int j = 0; j < 256; j++) {
            Color c = fb.getPixelFB(i,j);
            vp1.setPixelVP(i,j,fbEmbedded.getPixelFB(i,255-j));
            if(vp1.getPixelVP(i,j).equals(Color.WHITE)) {
               vp1.setPixelVP(i,j, c);
            }

         }
      }

      /* if(fbEmbedded.getPixelFB(i,j).equals(Color.WHITE)) {
               vp1.setPixelVP(i,j, Color.BLACK);
            } */


      //8 viewport another flipped copy of 1st ppm file
      //top left = 381, 175
      //256x256 image size
      FrameBuffer.Viewport vp2 = fb.new Viewport(381,175,256,256);

      for(int i = 0; i < 256; i++) {  //flip horizontally
         for(int j = 0; j < 256; j++) { 
            
            vp2.setPixelVP(i,j,fbEmbedded.getPixelFB(255-i,255-j));
         }
      }
   


      //9 viewport that covers the 6 checkerboard squares that need to be copied
      


      //10 viewport that holds a "framed" copy of the previous viewport


      //11 viewport within the last, gray viewport, and initialize it to hold a copy of the viewport from step 9


      //12 load Dumbledore (2nd ppm file) into another FrameBuffer
      //FrameBuffer dumbledore = new FrameBuffer(file_2);


      //13 viewport to hold Dumbledore's ghost


      //14 blend Dumbledore from its framebuffer into the viewport


      



      /******************************************/
      // Save the resulting image in a file.
      final String savedFileName = "Hw1.ppm";
      fb.dumpFB2File( savedFileName );
      System.err.println("Saved " + savedFileName);
   }
}
