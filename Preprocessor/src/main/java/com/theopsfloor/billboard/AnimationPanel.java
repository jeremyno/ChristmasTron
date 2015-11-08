package com.theopsfloor.billboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.theopsfloor.billboard.App.Frame;

public class AnimationPanel extends JPanel implements Runnable {
  public static int PIXEL_SIZE = 40;
  private static final long serialVersionUID = 1L;

  ArrayList<Frame> frames = new ArrayList<>();
  long nextFrame = 0;
  int index = 0;
  boolean run = true;

  public AnimationPanel() {
    Thread t = new Thread(this);
    t.start();
    this.setPreferredSize(new Dimension(10*PIXEL_SIZE,10*PIXEL_SIZE));
  }

  @Override
  public void run() {
    while (run) {
      repaint();
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        run = false;
      }
    }
  }

  public void setFrames(List<Frame> frames) {
    this.frames = new ArrayList<Frame>(frames);
    nextFrame = 0;
    index = -1;
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.black);
    g.drawRect(0, 0, getWidth(), getHeight());

    if (frames != null && !frames.isEmpty()) {
      if ( System.currentTimeMillis() > nextFrame ) {
        index = (index+1) % frames.size();
        nextFrame = System.currentTimeMillis() + frames.get(index).getTime();
      }

      List<List<Color>> colors = frames.get(index).getImg();

      int y = colors.size();

      if (y > 0) {
        int x = colors.get(0).size();

        int ysize= PIXEL_SIZE;//this.getHeight() / y;
        int xsize = PIXEL_SIZE;//this.getWidth() / x;

        for(int j = 0; j < y; j++) {
          List<Color> line = colors.get(j);
          for(int i = 0; i < x; i++) {
            g.setColor(line.get(i));
            g.fillRect(i*xsize, j*ysize, xsize, ysize);
          }
        }
      }
    }
  }
}
