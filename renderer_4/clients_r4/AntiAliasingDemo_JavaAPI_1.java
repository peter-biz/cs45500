/*
 * Renderer 4. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

import java.awt.*;
import javax.swing.*;

/**
   This program draws a fan of lines two times, once using
   Java's default lines, and a second time using Java's
   anti-aliased lines. The two fans are displayed side-by-side.

   To see how the anti-aliasing works, look at the final rendered
   image using a pixel magnifying program.
*/
@SuppressWarnings("serial")
public class AntiAliasingDemo_JavaAPI_1 extends JPanel
{
   private final int SIZE = 900;
   private final int NUMBER_OF_LINES = 30;

   private final Color backGroundColor = Color.green;
   private final Color foreGroundColor = Color.red;

   public AntiAliasingDemo_JavaAPI_1()
   {
      final Dimension dim = new Dimension(2*SIZE, SIZE);
      setPreferredSize(dim);
   }

   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      // Recover Graphics2D.
      final Graphics2D g2 = (Graphics2D)g;

      g2.setColor(backGroundColor);
      g2.fillRect(0, 0, 2*SIZE + 1, SIZE + 1);
      g2.setColor(foreGroundColor);

      // Draw the left half of the screen using Java's default lines.
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_OFF);

      for (int i = 0; i <= NUMBER_OF_LINES; ++i)
      {
         g2.drawLine(0,    SIZE,
                     SIZE, SIZE - (int)(i * ((double)SIZE/(double)NUMBER_OF_LINES)));
         g2.drawLine(0, SIZE,
                     (int)(i * ((double)SIZE/(double)NUMBER_OF_LINES)), 0);
      }

      // Draw the right half of the screen using Java's anti-aliased lines.
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON);

      for (int i = 0; i <= NUMBER_OF_LINES; ++i)
      {
         g2.drawLine(SIZE, SIZE,
                   2*SIZE, SIZE - (int)(i * ((double)SIZE/(double)NUMBER_OF_LINES)));
         g2.drawLine(SIZE, SIZE,
                     SIZE + (int)(i * ((double)SIZE/(double)NUMBER_OF_LINES)), 0);
      }
      g2.dispose();
   }


   public static void main(String[] args)
   {
      javax.swing.SwingUtilities.invokeLater(() ->
      {
         final JFrame jf = new JFrame();
         jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         jf.setTitle("AntiAliasingDemo JavaAPI 1");
         jf.setResizable(false);
         jf.add(new AntiAliasingDemo_JavaAPI_1(), BorderLayout.CENTER);
         jf.pack();
         jf.setLocationRelativeTo(null);
         jf.setVisible(true);
      });
   }
}
