package com.theopsfloor.billboard;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Display extends JFrame {
  public static int PIXEL_SIZE = 40;
  public static int HEADER_SIZE=25;
  private static final long serialVersionUID = 1L;
  List<List<Color>> colors = new ArrayList<>();

  public void setImage(List<List<Color>> img) {
    colors = img;

    this.repaint();
  }

  @Override
  public void paint(Graphics g) {
    int y = colors.size();

    if (y > 0) {
      int x = colors.get(0).size();

      int ysize= PIXEL_SIZE;//this.getHeight() / y;
      int xsize = PIXEL_SIZE;//this.getWidth() / x;

      for(int j = 0; j < y; j++) {
        List<Color> line = colors.get(j);
        for(int i = 0; i < x; i++) {
          g.setColor(line.get(i));
          g.fillRect(i*xsize, HEADER_SIZE+j*ysize, xsize, ysize);
        }
      }
    }
  }
}
