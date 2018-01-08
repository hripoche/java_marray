// MemComputation.java

package classifier.hierarchical;

import java.util.HashMap;

/**
 * @author H. Ripoche
 * 11/03/2002
 * Cette classe permet de simuler une matrice symetrique: get(ci,cj) == get(cj,ci)
 */
public class MemComputation {

  private static HashMap mem = new HashMap();

  private Cluster cluster1;
  private Cluster cluster2;
  //private double value;

    //public MemComputation (Cluster cluster1, Cluster cluster2, double value) {
  public MemComputation (Cluster cluster1, Cluster cluster2) {
    this.cluster1 = cluster1;
    this.cluster2 = cluster2;
    //this.value = value;
  }

  static double get (Cluster cluster1, Cluster cluster2) {
    Double d = (Double) mem.get(new MemComputation(cluster1,cluster2));
    if (d == null) {
      return Double.NaN;
    } else {
      return d.doubleValue();
    }
  }

  static void add (Cluster cluster1, Cluster cluster2, double value) {
    mem.put(new MemComputation(cluster1,cluster2), new Double(value));
  }

  static void init () {
    mem = new HashMap();
  }

  public String toString () {
    return "{"+cluster1+", "+cluster2+"}";
  }

  public static String asString () {
    return mem.toString();
  }
}
