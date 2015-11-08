package com.theopsfloor.billboard;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.theopsfloor.billboard.App.Frame;

public class ImageSerializer {
  public static final int[][] DEFAULT_BACKWARD_STRINGS = {
    {41, 40, 21, 20, 1, 100, 81, 80, 61, 60},
    {42, 39, 22, 19, 2, 99, 82, 79, 62, 59},
    {43, 38, 23, 18, 3, 98, 83, 78, 63, 58},
    {44, 37, 24, 17, 4, 97, 84, 77, 64, 57},
    {45, 36, 25, 16, 5, 96, 85, 76, 65, 56},
    {46, 35, 26, 15, 6, 95, 86, 75, 66, 55},
    {47, 34, 27, 14, 7, 94, 87, 74, 67, 54},
    {48, 33, 28, 13, 8, 93, 88, 73, 68, 53},
    {49, 32, 29, 12, 9, 92, 89, 72, 69, 52},
    {50, 31, 30, 11, 10, 91, 90, 71, 70, 51},
  };

  public static final int[][] DEFAULT_SNAKE = {
    {100, 81, 80, 61, 60, 41, 40, 21, 20, 1},
    {99, 82, 79, 62, 59, 42, 39, 22, 19, 2},
    {98, 83, 78, 63, 58, 43, 38, 23, 18, 3},
    {97, 84, 77, 64, 57, 44, 37, 24, 17, 4},
    {96, 85, 76, 65, 56, 45, 36, 25, 16, 5},
    {95, 86, 75, 66, 55, 46, 35, 26, 15, 6},
    {94, 87, 74, 67, 54, 47, 34, 27, 14, 7},
    {93, 88, 73, 68, 53, 48, 33, 28, 13, 8},
    {92, 89, 72, 69, 52, 49, 32, 29, 12, 9},
    {91, 90, 71, 70, 51, 50, 31, 30, 11, 10},
  };

  public static final int[][] DEFAULT;

  static {
    int dft[][] = new int[10][10];
    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < 10; j++) {
        dft[i][j] = DEFAULT_SNAKE[i][j] - 1;
      }
    }

    DEFAULT = dft;
  }

  private int map[][];
  private int len;

  public ImageSerializer(int map[][]) {
    this.map = map;
    len = map.length * map[0].length;
  }

  public String serialize(String name, List<Frame> frames) {
    StringBuilder b = new StringBuilder();

    b.append("const uint8_t PROGMEM ");
    b.append(name);
    b.append("[] = {");
    b.append(serialize(frames));
    b.append("};");

    return b.toString();
  }

  public byte[] serializeBinary(List<Frame> frames) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bos.write(len);

    for(Frame f : frames) {
      Color data[] = new Color[len];

      List<List<Color>> img = f.getImg();
      for (int y = 0; y < img.size(); y++) {
        for (int x = 0; x < img.get(y).size(); x++) {
          int index = map[y][x];
          data[index] = f.img.get(y).get(x);
        }
      }

      for (Color d : data) {
        bos.write(d.getRed());
        bos.write(d.getGreen());
        bos.write(d.getBlue());
      }

      bos.write(f.getTime()/10);
    }

    return bos.toByteArray();
  }

  public String serialize(List<Frame> frames) {
    StringBuilder b = new StringBuilder();

    for (Frame f : frames) {
      b.append(serialize(f)).append(",");
    }

    b.setLength(b.length() - 1);

    return b.toString();
  }

  public String serialize(Frame f) {
    Color data[] = new Color[len];

    List<List<Color>> img = f.getImg();
    for (int y = 0; y < img.size(); y++) {
      for (int x = 0; x < img.get(y).size(); x++) {
        int index = map[y][x];
        data[index] = f.img.get(y).get(x);
      }
    }

    StringBuilder b = new StringBuilder();
    for (Color d : data) {
      b.append(d.getRed());
      b.append(",");
      b.append(d.getGreen());
      b.append(",");
      b.append(d.getBlue());
      b.append(",");
    }

    b.append(f.getTime()/10);

    return b.toString();
  }
}
