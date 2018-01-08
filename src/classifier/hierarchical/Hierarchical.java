// Hierarchical.java

package classifier.hierarchical;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * @author H. Ripoche
 * 18/02/2002
 */
public class Hierarchical {

  private Clusters allClusters;
  private Cluster root;
  private DataMatrix dataMatrix;
  
  public Hierarchical(DataMatrix dataMatrix) {
    Instance.setDataMatrix(dataMatrix);
    this.dataMatrix = dataMatrix;
  }

  public void classify () {
    double min;//max;
    Cluster x, y;
    Cluster c, c1, c2;
    int size;
    
    // init
    MemComputation.init();
    allClusters = new Clusters();
    for (int i=0; i<Instance.getNames().length; i++) {
      c = new Cluster(Instance.getNames()[i],i);
      allClusters.add(c);
    }

    do {
      min = Double.MAX_VALUE;//max = Double.MIN_VALUE;
      c1=c2=x=y=null;
      size = allClusters.size();

      for (int i=0; i<size; i++) {
//System.err.println("i="+i);
      	c1 = (Cluster) allClusters.elementAt(i);
        for (int j=0; j<size; j++) {
          c2 = (Cluster) allClusters.elementAt(j);
      	  if (i!=j) {
      	    double dist = MemComputation.get(c1,c2);
      	    if (Double.isNaN(dist)) { // not found
      	      dist = c1.distance(c2);
      	      MemComputation.add(c1,c2,dist);
      	    }
      	    if (dist < min) {
      	      min = dist;
      	      x = c1;
      	      y = c2;
      	    }
      	  }
        }
      }
      c = new Cluster(x,y,min/2);
      allClusters.remove(x);
      allClusters.remove(y);
      allClusters.add(c);
System.out.println("add:"+c);
    } while (c.getSize() < Instance.getNames().length);
    root = c;
System.out.println("fin:"+root);
//System.out.println("MemComputation:"+MemComputation.asString());
  }

  public void display (File f) {
    int w=1000, h=dataMatrix.getNames().length * 15;
    BufferedImage bufferedImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = bufferedImage.createGraphics();

    // black drawing on white background
    g.setBackground(Color.white);
    g.clearRect(0,0,w,h);
    g.setColor(Color.black);

    root.draw(g,w,h);

    try {
      ImageIO.write((RenderedImage)bufferedImage,"png",f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
