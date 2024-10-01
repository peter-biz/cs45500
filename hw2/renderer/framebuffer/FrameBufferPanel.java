/*
 * FrameBufferPanel. The MIT License.
 * Copyright (c) 2022 rlkraft@pnw.edu
 * See LICENSE for details.
*/

package renderer.framebuffer;

import  renderer.framebuffer.FrameBuffer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import javax.swing.JPanel;

/**
   This class is an interface between our renderer and the Java GUI system.
   This class allows our rendering code to be used as the primary renderer
   for Java programs. That is, this class allows us to write Java programs
   that use our renderer instead of the renderer built into Java's GUI
   library. Of course, our renderer will be much slower than the one built
   into Java (which uses the computer's GPU).
<p>
   A {@code FrameBufferPanel} "is a" {@link javax.swing.JPanel}. A
   {@code FrameBufferPanel} "has a" {@link FrameBuffer}. And a
   {@link FrameBuffer} "has a" array of pixel data. The pixel data in a
   {@link FrameBuffer} is put there by calling our rendering algorithms.
<p>
   Each instance of {@code FrameBufferPanel} has a reference to a
   {@link FrameBuffer} object and the {@link FrameBuffer} object
   determines the (preferred) dimensions of the {@code FrameBufferPanel}.
<p>
   A {@code FrameBufferPanel} should be instantiated by an application that
   uses our renderer. The application should initialize a {@link renderer.scene.Scene}
   object with appropriate models and geometry. The application should then
   render the {@link renderer.scene.Scene} into the {@link FrameBuffer} object
   contained in the {@code FrameBufferPanel}.
<p>
   This class is meant to be instantiated as a sub-panel of a
   {@link javax.swing.JFrame}. The {@link javax.swing.JFrame} may or may
   not implement event listeners. If the {@link javax.swing.JFrame} does
   implement event listeners, then the event listeners can make our
   renderer interactive.
<p>
   When a GUI event happens, any implemented event listener should update
   this {@code FrameBufferPanel} by modifying a {@link renderer.scene.Scene}
   object appropriately and then having our renderer render the
   {@link renderer.scene.Scene} object into this object's {@link FrameBuffer}.
   When the renderer is done updating the {@link FrameBuffer}, the event
   listener should call this object's {@link java.awt.Component#repaint}
   method, which will lead to the calling of this object's {@link #paintComponent}
   method, which will pass the {@link FrameBuffer}'s pixel data to an
   {@link java.awt.Image} that will be drawn on the {@link java.awt.Graphics}
   context of the {@code FrameBufferPanel} (which is a {@link JPanel}).
   This will display the {@link java.awt.Image}, which holds the
   {@link FrameBuffer}'s contents, in the {@link JPanel} within a
   {@link javax.swing.JFrame}'s window.
<p>
   This panel may be resizeable. When this panel resizes, its
   {@link FrameBuffer} object will also need to resize. But
   {@link FrameBuffer} objects cannot be resized. So each time this
   panel resizes, a new {@link FrameBuffer} object needs to be created.
   The {@link java.awt.event.ComponentListener#componentResized} event
   handler from the {@link java.awt.event.ComponentListener} interface
   should instantiate a new {@link FrameBuffer} object with the
   appropriate dimensions and then call this object's {@link #setFrameBuffer}
   method and pass it a reference to the new {@link FrameBuffer} object.
*/
@SuppressWarnings("serial")
public final class FrameBufferPanel extends JPanel
{
   private FrameBuffer fb;
   private MemoryImageSource source;

   /**
      @param fbWidth   width for the initial {@link FrameBuffer} used by this {@link JPanel}
      @param fbHeight  height for the initial {@link FrameBuffer} used by this {@link JPanel}
   */
   public FrameBufferPanel(final int fbWidth, final int fbHeight)
   {
      // Create the initial FrameBuffer for the FrameBufferPanel.
      this.setFrameBuffer( new FrameBuffer(fbWidth, fbHeight) );
   }


   /**
      @param fbWidth   width for the initial {@link FrameBuffer} used by this {@link JPanel}
      @param fbHeight  height for the initial {@link FrameBuffer} used by this {@link JPanel}
      @param bgColor   background {@link Color} for the initial {@link FrameBuffer} used by this {@link JPanel}
   */
   public FrameBufferPanel(final int fbWidth, final int fbHeight, final Color bgColor)
   {
      // Create the initial FrameBuffer for the FrameBufferPanel.
      this.setFrameBuffer( new FrameBuffer(fbWidth, fbHeight, bgColor) );
   }


   @Override
   public Dimension getPreferredSize()
   {
      return new Dimension(fb.width, fb.height);
   }


   @Override
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      final Graphics2D g2 = (Graphics2D)g.create();
      final Image img = this.createImage(this.source);
      g2.drawImage(img, 0, 0, this);
      g2.dispose();
   }


   /**
      Accessor method for the {@link FrameBuffer} currently being used as
      the source for the {@link java.awt.Image} painted on this {@link JPanel}.

      @return a reference to the {@link FrameBuffer} owned by this {@link JPanel}
   */
   public FrameBuffer getFrameBuffer()
   {
      return fb;
   }


   /**
      Change the {@link FrameBuffer} being used as the source for
      the {@link java.awt.Image} painted on this {@link JPanel}.
   <p>
      This will usually be in response to a call to the
      componentResized() event handler.

      @param fb  new {@link FrameBuffer} object for this {@link JPanel}
   */
   public void setFrameBuffer(final FrameBuffer fb)
   {
      this.fb = fb;

      // Use the framebuffer as the source for an Image.
      this.source = new MemoryImageSource(fb.width, fb.height,
                                          fb.pixel_buffer,
                                          0, fb.width);
   }
}
