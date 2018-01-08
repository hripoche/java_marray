/**
 * Clusters.java
 * @author H. Ripoche
 * 15/02/2002
 */

package classifier.kmeans;

import java.util.Vector;

public class Clusters extends Vector {
  
  public Clusters () {
    super();
  };

  public String toString() {
    StringBuffer result = new StringBuffer();
    for (int i=0; i<size(); i++) {
      result.append(this.elementAt(i)).append('\n');
    }
    return result.toString();
  }

  public String asString() {
    StringBuffer result = new StringBuffer();
    for (int i=0; i<size(); i++) {
      result.append(((Cluster)elementAt(i)).asString()).append("\n<hr>\n\n");
    }
    return result.toString();
  }
}
