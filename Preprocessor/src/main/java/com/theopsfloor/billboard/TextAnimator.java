package com.theopsfloor.billboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.theopsfloor.billboard.App.Frame;

public class TextAnimator implements Animation {
  BufferedImageAnimator animator;

  public TextAnimator(int width, int height) {
    animator = new BufferedImageAnimator(width, height);
  }

  public Font getFontThatFits(Font template,String text) {
    Font f =getFontThatFits(template,text, 20);
    if (f != null) {
      return f;
    }
    return template;

  }

  private Font getFontThatFits(Font template, String text, int tries) {
    System.out.printf("  %s %d %d%n",template,template.getSize(),tries);
    if (tries <= 0) {
      return null;
    }
    if (template.getSize() < 6) {
      return getFontThatFits(template.deriveFont(10f), text,tries-1);
    }

    Rectangle2D bounds = getBounds(template, text);

    int em = (int) getBounds(template,"M").getWidth();
    int height = (int) (bounds.getHeight()+1);
    System.out.println("  " + height);

    if (em > animator.getWidth() || height > animator.getHeight()) {
      if ( template.getSize() > 6) {
        return getFontThatFits(template.deriveFont((float)template.getSize()-1),text,tries-1);
      } else {
        return template;
      }
    } else if (em < animator.getWidth() && height < animator.getHeight()  ) {
      return getFontThatFits(template.deriveFont((float)template.getSize()+1),text,tries-1);
    } else {
      return template;
    }
  }



  public Rectangle2D getBounds(Font font, String text) {
    AffineTransform affinetransform = new AffineTransform();
    FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
    TextLayout tl = new TextLayout(text, font, frc);
    return tl.getBounds();
    //return font.getStringBounds(text, frc);
  }

  public void animateText(String text, Color foreground, Color background, Font font, int frameDelay) {
    int height = animator.getHeight();

    Graphics g = animator.getGraphics();

    Rectangle2D stringBounds = getBounds(font, text);
    int fontHeight = (int) stringBounds.getHeight();

    int yoffset = height - 0;
    //System.out.printf("%d %d %d %n", height,fontHeight,yoffset);

    System.out.println(stringBounds.getWidth() + "..."+ text);
    int strWidth = (int) (stringBounds.getWidth()+1);

    int width = animator.getWidth();
    int steps = 2* width + strWidth;

    boolean correctLength = true;
    for(int i = 0; i < steps || correctLength ; i++) {
      g.setColor(background);
      g.fillRect(0, 0, width, height);

      g.setColor(foreground);
      int x = width - i;
      g.drawString(text, x, yoffset);

      animator.snapFrame(frameDelay);

      List<List<Color>> colors = getFrames().get(getFrames().size()-1).getImg();
      correctLength = false;
      for(List<Color> cl : colors) {
        for(Color c : cl) {
          correctLength |= !c.equals(background);
        }
      }
    }
  }

  @Override
  public List<Frame> getFrames() {
    return animator.getFrames();
  }

}
