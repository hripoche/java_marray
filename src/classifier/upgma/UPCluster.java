// UPCluster.java

package classifier.upgma;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

class UPCluster extends Cluster {
  String label;			// Cluster identifier
  int size;			// The number of sequences in the cluster
  double height;		// The height of the node
  UPCluster left, right;	// Left and right children, or null
  double[] dmat;		// Distances to lower-numbered nodes, or null

  public UPCluster(String label, double[] dmat) {	// Leaves = single sequences
    this.label = label;
    this.size = 1;
    this.dmat = dmat;
  }

  public UPCluster(UPCluster left, UPCluster right, double height, double[] dmat) { 
    this.label = null; 
    this.left = left;
    this.right = right;
    this.size = left.size + right.size;
    this.height = height;
    this.dmat = dmat;
  }

  public boolean live()
  { return dmat != null; }

  public void kill() 
  { dmat = null; }

  public void print() 
  { print(0); }

  void print(int n) {
    if (right != null)
      right.print(n + 6);
    indent(n); 
    System.out.println("[" + label + "] (" + (int)(100*height)/100.0 + ")"); 
    if (left != null)
      left.print(n + 6);
  }

  void indent(int n) {
    for (int i=0; i<n; i++)
      System.out.print(" ");
  }

/*
  public void draw(Graphics g, int w, int h) {
    draw(g, w, h, 0, (double)w/size, (double)(h-50)/height, 10); 
  }

  public int draw(Graphics g, int w, int h, int leftsize, 
		  double xsc, double ysc, int fromy) {
    if (left != null && right != null) {	// Internal node
      int y = (int)(h - 30 - height * ysc);
      int leftx  = left.draw(g, w, h, leftsize, xsc, ysc, y);
      int rightx = right.draw(g, w, h, leftsize+left.size, xsc, ysc, y);
      g.drawLine(leftx, y, rightx, y);
      int x = (leftx + rightx) / 2; 
      g.drawLine(x, y, x, fromy);
      g.fillOval(x-4, y-4, 8, 8);
      return x;
    } else {					// Leaf node
      int x = (int)((leftsize + 0.5) * xsc);
      g.drawLine(x, h-30, x, fromy);
      g.fillOval(x-4, h-30-4, 8, 8);
      g.drawString(label, x-5, h-10);
      return x;
    }
  }
*/
  public void draw (Graphics2D g, int w, int h) {
    int treebase=300;//w-200
    draw(g, w, h, 0, (double)(treebase-20)/height, (double)h/size, 10, treebase);
  }

  /**
   * Draw tree and return y-coordinate of root
   */
  public int draw (Graphics2D g, int w, int h, int leftsize, double xsc, double ysc, int fromx, int treebase) {
    if (left == null || right == null) {
      int y = (int)((leftsize + 0.5) * ysc);
      g.drawLine(treebase, y, fromx, y);
      //g.fillOval(treebase-2, y-2, 4, 4);
      g.drawString(label, treebase+10, y+5);
      return y;
    } else {
      int x = (int)(treebase - height * xsc);
      int lefty  = left.draw(g, w, h, leftsize, xsc, ysc, x, treebase);
      int righty = right.draw(g, w, h, leftsize+left.size, xsc, ysc, x, treebase);
      g.drawLine(x, lefty, x, righty);
      int y = (lefty + righty) / 2;
      g.drawLine(x, y, fromx, y);
      //g.fillOval(x-2, y-2, 4, 4);
      return y;
    }
  }
}
