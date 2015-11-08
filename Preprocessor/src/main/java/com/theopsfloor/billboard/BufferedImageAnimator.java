package com.theopsfloor.billboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.theopsfloor.billboard.App.Frame;

public class BufferedImageAnimator implements Animation {
  List<Frame> frames = new ArrayList<Frame>();
  Graphics graphics;
  BufferedImage img;

  int width;
  int height;

  public BufferedImageAnimator(int width, int height) {
    this.width = width;
    this.height = height;

    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    graphics = img.getGraphics();
  }

  public void snapFrame(int displayFor) {
    List<List<Color>> rows = new ArrayList<>();
    for(int j = 0; j < height; j++) {
      List<Color> row = new ArrayList<>();
      for(int i = 0; i < width; i ++) {
        row.add(new Color(img.getRGB(i, j)));
      }
      rows.add(row);
    }

    frames.add(new Frame(displayFor, rows));
  }

  /* (non-Javadoc)
   * @see com.theopsfloor.billboard.Animation#getFrames()
   */
  @Override
  public List<Frame> getFrames() {
    return frames;
  }

  public Graphics getGraphics() {
    return graphics;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
