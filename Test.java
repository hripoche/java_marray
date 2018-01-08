// Test.java

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

/**
 * @author H. Ripoche
 */
public class Test {

  static private int imageMaxX = 500;
  static private int imageMaxY = 500;
  static private double maxX;
  static private double minX;
  static private double maxY;
  static private double minY;

  static private double[] x = {10,20,30,40,50};
  static private double[] y = {10,20,30,40,50};

  public final static double max (double[] x) {
    double result = x[0];
    for (int i=0; i<x.length; i++) {
      if (x[i] > result) result = x[i];
    }
    return result;
  }

  public final static double min (double[] x) {
    double result = x[0];
    for (int i=0; i<x.length; i++) {
      if (x[i] < result) result = x[i];
    }
    return result;
  }

  public static void initConvert (double[] x, double[] y) {
    minX = min(x);
    maxX = max(x);
    minY = min(y);
    maxY = max(y);
  }

  public static int convertX (double x) {
    return (int) ((x - minX) / (maxX - minX) * imageMaxX);
  }

  public static int convertY (double y) {
    return (int) ((1 - (y - minY)/(maxY - minY)) * imageMaxY);
  }

  public static void drawPoint (Graphics2D g, double x, double y) {
    int deltaX = 2;
    int deltaY = 2;
    g.drawLine(convertX(x)-deltaX,convertY(y),convertX(x)+deltaX,convertY(y));
    g.drawLine(convertX(x),convertY(y)-deltaY,convertX(x),convertY(y)+deltaY);
  }

  public static void drawPoints (Graphics2D g, double[] x, double[] y) {
    if (x.length != y.length) {
      System.err.println("Test.drawPoints: x and y differ in length");
    }

    for (int i=0; i<x.length; i++) {
      drawPoint(g,x[i],y[i]);
    }
  }

  public static void drawAxes (Graphics2D g) {
    
  }

  public static void run (File f, String labelX, String labelY, double[] x, double[] y) {

    BufferedImage bufferedImage = new BufferedImage(imageMaxX,imageMaxY,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = bufferedImage.createGraphics();

    initConvert(x,y);

    //g.drawLine(10,10,250,250);
    //g.drawRect(50,50,100,100);

    drawPoints(g,x,y);

    drawAxes(g);

    g.drawString(labelX,imageMaxX/2,imageMaxY);
    g.drawString(labelY,0,imageMaxY/2);

    try {
      ImageIO.write((RenderedImage)bufferedImage,"png",f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main (String[] args) {
    run(new File("foo.png"),"labelX","labelY",x,y);
  }
}
