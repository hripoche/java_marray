// UPGMA.java

// Implementation of some algorithms for building phylogenetic trees from
// Durbin et al: Biological Sequence Analysis, CUP 1998, chapter 7.
// Peter Sestoft, sestoft@dina.kvl.dk 1999-12-07 version 0.3
// References:  http://www.dina.kvl.dk/~sestoft/bsa.html

package classifier.upgma;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.imageio.ImageIO;

public class UPGMA {
  int K;			// The number of clusters created so far
  UPCluster[] cluster;		// The nodes (clusters) of the resulting tree
  DataMatrix dataMatrix;

/*
  public UPGMA(double[][] ds) {
    int N = ds.length;
    cluster = new UPCluster[2*N-1];
    for (int i=0; i<N; i++) 
      cluster[i] = new UPCluster(i, ds[i]);
    K = N;
    while (K < 2*N-1)
      findAndJoin();
  }
*/
  public UPGMA(DataMatrix dm) {
    this.dataMatrix = dm;
    int N = dm.getNames().length;
    cluster = new UPCluster[2*N-1];
    for (int i=0; i<N; i++) 
      cluster[i] = new UPCluster(dm.getNames()[i], dm.getMatrix()[i]);
    K = N;
    while (K < 2*N-1)
      findAndJoin();
  }

  public UPCluster getRoot()
  { return cluster[K-1]; }
  
  public double d(int i, int j) 
  { return cluster[Math.max(i, j)].dmat[Math.min(i, j)]; }

  void findAndJoin() { // Find closest two live clusters and join them
    int mini = -1, minj = -1;
    double mind = Double.POSITIVE_INFINITY;
    for (int i=0; i<K; i++) 
      if (cluster[i].live())
	for (int j=0; j<i; j++) 
	  if (cluster[j].live()) {
	    double d = d(i, j);
	    if (d < mind) {
	      mind = d;
	      mini = i;
	      minj = j;
	    }
	  }
    join(mini, minj);
  }

  public void join(int i, int j) { // Join i and j to form node K
//System.out.println("Joining " + (i+1) + " and " + (j+1) + " to form " + (K+1) + " at height " + (int)(d(i, j) * 50)/100.0);
    double[] dmat = new double[K];
    for (int m=0; m<K; m++)
      if (cluster[m].live() && m != i && m != j) 
	dmat[m] = (d(i, m) * cluster[i].size + d(j, m) * cluster[j].size)
	          / (cluster[i].size + cluster[j].size);
    cluster[K] = new UPCluster(cluster[i], cluster[j], d(i, j) / 2, dmat);
    cluster[i].kill();
    cluster[j].kill();
    K++;
  }

  public void display (File f) {
    int w=1000, h=dataMatrix.getNames().length * 15;
    BufferedImage bufferedImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = bufferedImage.createGraphics();

    // black drawing on white background
    g.setBackground(Color.white);
    g.clearRect(0,0,w,h);
    g.setColor(Color.black);

    getRoot().draw(g,w,h);

    try {
      ImageIO.write((RenderedImage)bufferedImage,"png",f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
