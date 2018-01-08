// DataMatrix.java

package classifier.hierarchical;

import java.util.*;

import marray.Stanford;
import classifier.*;

/**
 * @author H. Ripoche
 */
public class DataMatrix {

  private String[] names;
  private double[][] matrix;

  public String[] getNames () {
    return names;
  }

  public double getMatrixValue(int i, int j) {
    if (j >= i) {
      return matrix[i][j];
    } else {
      return matrix[j][i];
    }
  }

  public DataMatrix (Stanford stanford) {
    Item[] items = stanford.getItems();
    int len = items.length;

    names = new String[items.length];
    for (int i=0; i<len; i++) {
      names[i] = items[i].toString();
    }

    matrix = new double[len][len];
    for (int i=0; i<len; i++) {
      for (int j=i+1; j<len; j++) {
        matrix[i][j] = items[i].distance(items[j]);
      }
    }
    for (int i=0; i<len; i++) {
      matrix[i][i] = 0;
    }
    //for (int i=0; i<len; i++) {
    //  for (int j=0; j<i; j++) {
    //    matrix[i][j] = matrix[j][i];
    //  }
    //}
  }
}
