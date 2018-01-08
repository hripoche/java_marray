// Cluster.java

package classifier.hierarchical;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author H. Ripoche
 * 18/02/2002
 * A node of the tree
 */
public class Cluster {

  private Clusters subclusters;
  private Instance instance;
  private double height;
  private int size;

  public Cluster getLeft () {
    if (isTerminal()) return null;
    return (Cluster) subclusters.elementAt(0);
  }

  public Cluster getRight () {
    if (isTerminal()) return null;
    return (Cluster) subclusters.elementAt(1);
  }

  public double getHeight () {
    return height;
  }

  public void setHeight (double h) {
    height = h;
  }

/*
  public static double similarity (Cluster c1, Cluster c2) {
    Instances i1 = c1.getInstances();
    Instances i2 = c2.getInstances();
    double sim=0;

    for (int i=0; i<i1.size(); i++) {
      for (int j=0; j<i2.size(); j++) {
      	sim += Instance.similarity((Instance) i1.elementAt(i), (Instance) i2.elementAt(j));
      }
    }
    sim /= (i1.size() * i2.size()); // * ???
    return sim;
  }
*/

  public double distance (Cluster cluster) {
    Instances i1 = this.getInstances();
    Instances i2 = cluster.getInstances();
    double min=Double.MAX_VALUE, val=0;

    for (int i=0; i<i1.size(); i++) {
      for (int j=0; j<i2.size(); j++) {
      	val = Instance.distance((Instance) i1.elementAt(i), (Instance) i2.elementAt(j));
      	if (val < min) min=val;
      }
    }
    return min;
  }

  /**
   * Constructor for leaf node
   */
  public Cluster (String name, int index) {
    subclusters = new Clusters();
    instance = new Instance(name,index);
    size = 1;
  }

  /**
   * Constructor for non-leaf node
   */
  public Cluster (Cluster c1, Cluster c2, double height) {
    subclusters = new Clusters();
    subclusters.add(c1); // left
    subclusters.add(c2); // right
    this.height = height;
    this.size = c1.size + c2.size;
  }

  public Clusters getSubclusters() {
    return subclusters;
  }

  public Instances getInstances() {
    if (isTerminal()) {
      Instances inst = new Instances();
      inst.add(instance);
      return inst;
    } else {
      Instances inst = new Instances();
      for (int i=0; i<subclusters.size(); i++) {
        inst.addAll(((Cluster) subclusters.elementAt(i)).getInstances());
      }
      return inst;
    }
  }

  boolean isTerminal () {
    return (subclusters.size() == 0);
  }

  public int getSize () {
    return size;
  }

/*
  public boolean equals (Cluster c) {
    if (isTerminal()) {
      return (c.isTerminal() && this.instance.equals(c.instance));
    } else {
      Instances a = getInstances();
      Instances b = c.getInstances();
      return (a.containsAll(b) && b.containsAll(a));	
    }
  }
*/

  public String toString () {
    if (isTerminal()) {
      return instance.toString();
    } else {
      return subclusters.toString();
    }
  }

/*
  public void draw(Graphics g, int w, int h) {
    draw(g, w, h, 0, (double)w/getSize(), (double)(h-50)/height, 10); 
  }

  // Draw tree and return x-coordinate of root
  public int draw(Graphics g, int w, int h, int leftsize, 
		  double xsc, double ysc, int fromy) {
    if (isTerminal()) {
      int x = (int)((leftsize + 0.5) * xsc);
      g.drawLine(x, h-30, x, fromy);
      g.fillOval(x-2, h-30-2, 4, 4);
      g.drawString(instance.getName(), x-5, h-10);
      return x;
    } else {
      int y = (int)(h - 30 - height * ysc);
      int leftx  = getLeft().draw(g, w, h, leftsize, xsc, ysc, y);
      int rightx = getRight().draw(g, w, h, leftsize+getLeft().getSize(), xsc, ysc, y);
      g.drawLine(leftx, y, rightx, y);
      int x = (leftx + rightx) / 2; 
      g.drawLine(x, y, x, fromy);
      g.fillOval(x-2, y-2, 4, 4);
      return x;
    }
  }
*/

  public void draw(Graphics2D g, int w, int h) {
    int treebase=300;//w-200
    draw(g, w, h, 0, (double)(treebase-20)/height, (double)h/getSize(), 10, treebase);
  }

  /**
   * Draw tree and return y-coordinate of root
   */
  public int draw(Graphics2D g, int w, int h, int leftsize, double xsc, double ysc, int fromx, int treebase) {
    if (isTerminal()) {
      int y = (int)((leftsize + 0.5) * ysc);
      g.drawLine(treebase, y, fromx, y);
      //g.fillOval(treebase-2, y-2, 4, 4);
      g.drawString(instance.getName(), treebase+10, y+5);
      return y;
    } else {
      int x = (int)(treebase - height * xsc);
      int lefty  = getLeft().draw(g, w, h, leftsize, xsc, ysc, x, treebase);
      int righty = getRight().draw(g, w, h, leftsize+getLeft().getSize(), xsc, ysc, x, treebase);
      g.drawLine(x, lefty, x, righty);
      int y = (lefty + righty) / 2;
      g.drawLine(x, y, fromx, y);
      //g.fillOval(x-2, y-2, 4, 4);
      return y;
    }
  }
}
