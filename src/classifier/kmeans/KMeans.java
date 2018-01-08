/**
 * KMeans.java
 * @author H. Ripoche
 * 14/02/2002
 */

package classifier.kmeans;

import java.util.Random;
import java.util.Vector;

import classifier.*;

public class KMeans {

  static private Random random = new Random();

  private Instance[] instances;
  private int nbClusters;
  private int maxIter;
  private int iter;
  private Clusters clusters;

  public KMeans (Instance[] instances, int nbClusters, int maxIter) {
    this.instances = instances;
    this.nbClusters = nbClusters;
    this.maxIter = maxIter;
    this.iter = 0;
    this.clusters = null;
  }
  
  public Instance[] getInstances() {
    return instances;	
  }
  
  public int getNbClusters() {
    return nbClusters;
  }
  
  public int getMaxIter() {
    return maxIter;
  }

  public int getIter() {
    return iter;
  }

  public Clusters getClusters() {
    return clusters;
  }

  /**
   * Attribute each instance to a random cluster
   */
  static void init (Instance[] instances, Clusters clusters, int nbClusters) {
    int c;
    for (int cc=0; cc<nbClusters; cc++) {
      clusters.add(new Cluster());
    }
    for (int i=0; i<instances.length; i++) {
    	c = (int)Math.floor(random.nextDouble()*nbClusters);
    	((Cluster) clusters.elementAt(c)).add(instances[i]);
    }
//System.out.println("clusters="+clusters);
  }
  
  Clusters iterate (Clusters clusters) {
  	
    Clusters result = new Clusters();
    int nbInst = 0;
    
    // compute centroids
    Instance[] centroids = new Instance[clusters.size()];
    for (int c=0; c<clusters.size(); c++) {
      centroids[c] = ((Cluster) clusters.elementAt(c)).createCentroid();
      result.add(new Cluster());
    }
    
    // dispatch instances according to centroids
    for (int i=0; i<instances.length; i++) {
      Instance inst = instances[i];
      
      // get closest centroid
      int closestCentroid=0;
      double minDist = Double.MAX_VALUE;
      for (int ct=0; ct<centroids.length; ct++) {
      	if (centroids[ct] != null) { // cas particulier cluster vide
          double dist = inst.distance(centroids[ct]);
    	  if (ct == 0 || dist < minDist) {
    	    minDist = dist;
      	    closestCentroid = ct;  	
      	  }
      	}
      }
      ((Cluster) result.elementAt(closestCentroid)).add(inst);
    }
    return result;
  }

  public void classify () {
    Clusters mem = null;
    clusters = new Clusters();
    init(instances,clusters,nbClusters);
    int i=0;
    boolean stop=false;
    do {
      mem = (Clusters) clusters.clone();
      clusters = iterate(clusters);
      //System.out.println("step="+i+"\n"); //+clusters.toString());
      i++;
      iter = i;
      stop = mem.equals(clusters) || i>=maxIter;
    } while (!stop);
  } 
}
