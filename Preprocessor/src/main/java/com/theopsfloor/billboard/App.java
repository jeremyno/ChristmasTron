package com.theopsfloor.billboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Hello world!
 *
 */
public class App
{

  private static final int DEFAULT_TIME = 30000;
  private static final long MAX_TIME_PER_FRAME = 255 * 10;

  public static void main( String[] args ) throws Exception
  {
    String pwd = ".";
    String dfile = "";

    if (args.length == 1) {
      dfile = args[0];
      System.out.println(dfile);

      File file =new File(dfile);
      pwd = file.getParent();
      dfile = file.getName();
    } else {
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));
      chooser.showOpenDialog(null);

      File f = chooser.getSelectedFile();
      pwd = f.getParent();
      dfile = f.getName();
    }

    List<Frame> frames = getFrames(new File(pwd,dfile));
    String handle = dfile.replaceAll("[.].*", "").replaceAll("[0-9]+x[0-9]+", "");
    serializeAnimation(handle,pwd,frames);
    animateFrames(frames);
  }

  private static void serializeAnimation(String handle, String pwd, List<Frame> frames) throws Exception {
    try {
      ImageSerializer serial = new ImageSerializer(ImageSerializer.DEFAULT);
      String def = serial.serialize(handle,frames);
      File file = new File(pwd,handle + ".h");
      FileUtils.writeStringToFile(file, def);
      File binFile = new File(pwd,handle+".bin");
      FileUtils.writeByteArrayToFile(binFile, serial.serializeBinary(frames));

      System.out.println("Serialized animation to " + file.getAbsolutePath() + " and " + binFile.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to serialize animation:");
      e.printStackTrace();
    }
  }

  public static Frame getFrame(File f, int time) throws Exception {
    BufferedImage img = ImageIO.read(f);
    int x = img.getWidth();
    int y = img.getHeight();

    List<List<Color>> data = new ArrayList<List<Color>>();
    for(int j = 0; j < y; j++) {
      List<Color> dline = new ArrayList<Color>();
      for(int i = 0; i < x; i++) {
        Color c = new Color(img.getRGB(i, j));
        dline.add(c);
      }
      data.add(dline);
    }

    return new Frame(time,data);
  }

  public static List<Frame> getFrames(File fileF) throws Exception {
    List<Frame> frames = new ArrayList<App.Frame>();

    if (fileF.getName().endsWith(".txt")) {
      List<String> lines = FileUtils.readLines(fileF);

      for(String line : lines) {
        if (line.startsWith("#") || line.matches("\\s*")) {
          continue;
        }

        String time = StringUtils.substringBefore(line,":");
        String file = StringUtils.substringAfter(line,":");

        if (line.equals("fonttest")) {
          Color background = Color.black;

          Color foreground = Color.white;

          TextAnimator anim = new TextAnimator(10, 10);


          for (Font font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
            System.out.println(font);

            String s = font.getName();
            anim.animateText(s, foreground, background, anim.getFontThatFits(font,s) ,66);
          }


          frames.addAll(anim.getFrames());
          // don't process for now

        } else if (time.equals("txt")) {
          Color foreground = new Color(Integer.parseInt(StringUtils.substringBefore(file,":"),16));
          String remaining = StringUtils.substringAfter(file,":");

          Color background = new Color(Integer.parseInt(StringUtils.substringBefore(remaining,":"),16));
          remaining = StringUtils.substringAfter(remaining,":");

          int delay = Integer.parseInt(StringUtils.substringBefore(remaining,":"),16);
          remaining = StringUtils.substringAfter(remaining,":");

          TextAnimator anim = new TextAnimator(10, 10);
          Font font = anim.getFontThatFits(new Font("Franklin Gothic Medium", Font.PLAIN, 8),remaining);
          font = new Font("Franklin Gothic Medium", Font.PLAIN, 8);
          anim.animateText(remaining, foreground, background, font ,delay);

          frames.addAll(anim.getFrames());
          // don't process for now
        } else if (file.endsWith(".txt")) {
          int count = Integer.parseInt(time);

          List<Frame> subseqFrames = getFrames(new File(fileF.getParent(),file));

          for(int i = 0; i < count; i++) {
            frames.addAll(subseqFrames);
          }
        } else {
          long totalTime = Integer.parseInt(time);
          while (totalTime > 0) {
            int fTime = (int) Math.min(totalTime, MAX_TIME_PER_FRAME);
            Frame f = getFrame(new File(fileF.getParent(),file),fTime);
            totalTime -= MAX_TIME_PER_FRAME;
            frames.add(f);
          }
        }
      }
    } else {
      frames.add(getFrame(fileF , DEFAULT_TIME));
    }

    return frames;
  }

  public static void animateFrames(List<Frame> frames) throws InterruptedException {
    JFrame disp = new MainFrame();
    List<List<Color>> first = frames.get(0).getImg();
    AnimationPanel panel = new AnimationPanel();
    disp.add(panel);
    //disp.setSize(Display.PIXEL_SIZE*first.get(0).size(), Display.PIXEL_SIZE * first.size()+Display.HEADER_SIZE);
    disp.pack();
    disp.show();
    disp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    panel.setFrames(frames);
  }

  public static class Frame {
    int time;
    List<List<Color>> img;
    public int getTime() {
      return time;
    }
    public List<List<Color>> getImg() {
      return img;
    }
    public Frame(int time, List<List<Color>> img) {
      super();
      this.time = time;
      this.img = img;
    }
  }
}
