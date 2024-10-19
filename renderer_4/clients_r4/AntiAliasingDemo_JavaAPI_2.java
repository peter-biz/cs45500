/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
   This program draws a fan of lines two ways, one way
   without anti-aliasing, and a second way using Java's
   anti-aliasing algorithm.

   Use a mouse click, or the 'a' key, to turn anti-aliasing
   off and on.

   Java does not seem to be able to combine anti-aliasing
   with gamma correction, so the anti-aliased lines do not
   look as good as they could (unless you run this program
   on a monitor with gamma equal to 1.0).

   To see how the anti-aliasing works, look at the rendered
   images using a pixel magnifying program.
*/
@SuppressWarnings("serial")
public class AntiAliasingDemo_JavaAPI_2 extends JPanel
                                        implements KeyListener, MouseListener
{
   private final int SIZE = 900;
   private final int NUMBER_OF_LINES = 30;

   private boolean doAntiAliasing = false;

   private Color backgroundColor = Color.green;
   private Color foregroundColor = Color.red;

   public AntiAliasingDemo_JavaAPI_2()
   {
      final Dimension dim = new Dimension(SIZE, SIZE);
      setPreferredSize(dim);

      this.addMouseListener(this);
      this.addKeyListener(this);
      //https://stackoverflow.com/questions/11487369/jpanel-doesnt-response-to-keylistener-event
      this.setFocusable(true);

      print_help_message();
   }

   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      // Recover Graphics2D.
      final Graphics2D g2 = (Graphics2D)g;

      g2.setColor(backgroundColor);
      g2.fillRect(0, 0, SIZE, SIZE);
      g2.setColor(foregroundColor);

      if (doAntiAliasing) // Draw using Java's anti-aliased lines.
      {
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
      }
      else // Draw using Java's default lines.
      {
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
      }

    //System.out.println( g2.getRenderingHints() );

      for (int i = 0; i <= NUMBER_OF_LINES; ++i)
      {
         g2.drawLine(0,    SIZE,
                     SIZE, SIZE - (int)(i * ((double)SIZE/(double)NUMBER_OF_LINES)));
         g2.drawLine(0, SIZE,
                     (int)(i * ((double)SIZE/(double)NUMBER_OF_LINES)), 0);
      }
      g2.dispose();
   }


   // Implement the KeyListener interface.
   @Override public void keyPressed(KeyEvent e){}
   @Override public void keyReleased(KeyEvent e){}
   @Override public void keyTyped(KeyEvent e)
   {
      //System.out.println( e );
      final char c = e.getKeyChar();
      if ('h' == c)
      {
         print_help_message();
         return;
      }
      else if ('a' == c)
      {
         doAntiAliasing = ! doAntiAliasing;
         System.out.print("Anti-aliasing is turned ");
         System.out.println(doAntiAliasing ? "On" : "Off");
         repaint();
      }
      else if ('c' == c)
      {
         // Change the foreground.
         final Random generator = new Random();
         final float r = generator.nextFloat();
         final float g = generator.nextFloat();
         final float b = generator.nextFloat();
         foregroundColor = new Color(r, g, b);
         System.out.println("new foreground color = " + backgroundColor);
         repaint();
      }
      else if ('b' == c)
      {
         // Change the background color.
         final Random generator = new Random();
         final float r = generator.nextFloat();
         final float g = generator.nextFloat();
         final float b = generator.nextFloat();
         backgroundColor = new Color(r, g, b);
         System.out.println("new background color = " + backgroundColor);
         repaint();
      }
   }


   // Implement the MouseListener interface.
   @Override public void mouseClicked(MouseEvent e)
   {
      //System.out.println( e );
      doAntiAliasing = ! doAntiAliasing;
      System.out.print("Anti-aliasing is turned ");
      System.out.println(doAntiAliasing ? "On" : "Off");
      repaint();
   }
   @Override public void mousePressed(MouseEvent e){}
   @Override public void mouseReleased(MouseEvent e){}
   @Override public void mouseEntered(MouseEvent e){}
   @Override public void mouseExited(MouseEvent e){}


   private void print_help_message()
   {
      System.out.println("Use the 'a' key to toggle anti-aliasing on and off.");
      System.out.println("Use mouse click to toggle anti-aliasing on and off.");
      System.out.println("Use the 'c' key to change the random line color.");
      System.out.println("Use the 'b' key to change the random background color.");
      System.out.println("Use the 'h' key to redisplay this help message.");
   }


   public static void main(String[] args)
   {
      javax.swing.SwingUtilities.invokeLater(() ->
      {
         final JFrame jf = new JFrame();
         jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         jf.setTitle("AntiAliasingDemo JavaAPI 2");
         jf.setResizable(false);
         jf.add(new AntiAliasingDemo_JavaAPI_2(), BorderLayout.CENTER);
         jf.pack();
         jf.setLocationRelativeTo(null);
         jf.setVisible(true);
      });
   }
}
