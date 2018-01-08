// GeneComparator.java

package marray;

import java.util.Comparator;

public class GeneComparator implements Comparator {

  public final static int ASCENDING = 0;
  public final static int DESCENDING = 1;

  private int order;

  public GeneComparator () {
    order = ASCENDING;
  }

  public GeneComparator (int order) {
    this.order = order;
  }

  public int compare (Object o1, Object o2) {
    int val;
    if (o1 instanceof Gene && o2 instanceof Gene) {
      if (order == ASCENDING) {
        val = compareDouble(((Gene) o1).dist,((Gene) o2).dist);
        if (val == 0) val = ((Gene) o1).name.compareTo(((Gene) o2).name);
      } else if (order == DESCENDING) {
        val = compareDouble(((Gene) o2).dist,((Gene) o1).dist);
        if (val == 0) val = ((Gene) o2).name.compareTo(((Gene) o1).name);
      } else {
      	System.err.println("Bad order");
      	return 0;
      }
    } else {
      throw new ClassCastException();
    }
    return val;
  }

  public static int compareDouble(double a, double b) {
    if (a == b) return 0;
    if (a < b) return -1;
    return 1;
  }
}
