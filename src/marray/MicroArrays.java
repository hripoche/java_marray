// MicroArrays.java

package marray;

import java.util.*;

import util.*;

/**
 * @author H. Ripoche
 */
public class MicroArrays extends Vector {

  private String[] commonGenes;

  public MicroArrays(){
    super();
  }
  
  //public void computeCommonGenesIntersection () {
  //  computeCommonGenesIntersectionWithNFoldChange(0);
  //}

  public void computeCommonGenesIntersectionWithNFoldChangeAndStandardDeviation (double x, double sd) {
    MicroArray microArray0 = (MicroArray)this.elementAt(0);
    String[] names0 = microArray0.getSystematicNamesWithFilter();
    String name;
    Vector commonNames = new Vector();
    boolean val;
    for (int i=0; i<names0.length; i++) {
      name = names0[i];
      val = true;
      for (int j=1; j<this.size(); j++) {
      	if (!Lib.member(name,((MicroArray)this.elementAt(j)).getSystematicNamesWithFilter())) {
      	   val = false;
      	   break;
      	}
      }
      if (val == true) {
/* filter on nFoldChange only
      	if (x == 0) { // do not use nFoldChange
      	  commonNames.add(name);
      	} else if (nFoldChange(name,x)) {
          commonNames.add(name);
      	}
*/
      	if (x == 0) { // do not use nFoldChange
      	  if (sd == 0) { // do not use standardDeviationAboveFilter
            commonNames.add(name);
          } else if (standardDeviationAboveFilter(name,sd)) {
            commonNames.add(name);
          }
      	} else if (nFoldChange(name,x)) {
          if (sd == 0) { // do not use standardDeviationAboveFilter
            commonNames.add(name);
          } else if (standardDeviationAboveFilter(name,sd)) {
            commonNames.add(name);
          }
      	}

      }
    }
    String[] result = new String[commonNames.size()];
    commonNames.copyInto(result);
    commonGenes = result;
  }

  //public void computeCommonGenesThreshold (int threshold) {
  //  computeCommonGenesThresholdWithNFoldChange(threshold,0);
  //}

  public void computeCommonGenesThresholdWithNFoldChangeAndStandardDeviation (int threshold, double x, double sd) {
    MicroArray microArray0 = (MicroArray)this.elementAt(0);
    String[] systematicNames = microArray0.getSystematicNames();
    String name;
    Hashtable h = new Hashtable();
    Integer val;
    for (int i=0; i<systematicNames.length; i++) {
      name = systematicNames[i];
      h.put(name,new Integer(0));
      for (int j=0; j<this.size(); j++) {
      	if (Lib.member(name,((MicroArray) this.elementAt(j)).getSystematicNamesWithFilter())) {
      	   val = (Integer) h.get(name);
      	   h.put(name,new Integer(val.intValue()+1));
      	}
      }
    }

/* filter on nFoldChange
    Vector commonNames = new Vector();
    for (int i=0; i<systematicNames.length; i++) {
      name = systematicNames[i];
      val = (Integer) h.get(name);
      if (val.intValue() >= threshold) {
      	if (x == 0) { // do not use nFoldChange
      	  commonNames.add(name);
      	} else if (nFoldChange(name,x)) {
          commonNames.add(name);
      	}
      }
    }
*/
    Vector commonNames = new Vector();
    for (int i=0; i<systematicNames.length; i++) {
      name = systematicNames[i];
      val = (Integer) h.get(name);
      if (val.intValue() >= threshold) {
      	if (x == 0) { // do not use nFoldChange
      	  if (sd == 0) { // do not use standardDeviationAboveFilter
            commonNames.add(name);
          } else if (standardDeviationAboveFilter(name,sd)) {
            commonNames.add(name);
          }
      	} else if (nFoldChange(name,x)) {
          if (sd == 0) { // do not use standardDeviationAboveFilter
            commonNames.add(name);
          } else if (standardDeviationAboveFilter(name,sd)) {
            commonNames.add(name);
          }
      	}
      }
    }

    String[] result = new String[commonNames.size()];
    commonNames.copyInto(result);
    commonGenes = result;
  }

  public String[] getCommonGenes () {
    return commonGenes;
  }
  
  /**
   * @return <code>true</code> if there is at least a <code>n</code> fold change
   * within the microarrays for the given gene
   */
  public boolean nFoldChange (String systematicName, double n) {
    double[] ratios = getLogRatiosBySystematicName(systematicName);
    for (int i=0; i<this.size(); i++) {
      for (int j=i+1; j<this.size(); j++) {
      	if (ratios[i]/ratios[j] >= n || ratios[j]/ratios[i] >= n) {
      	   return true;
      	}
      }	
    }
    return false;
  }

  /**
   * @return <code>true</code> if standard deviation is greater than <code>sd</code>
   * for the given gene
   */
  public boolean standardDeviationAboveFilter (String systematicName, double sd) {
    double[] ratios = getLogRatiosBySystematicName(systematicName);
    return (Lib.standardDeviation(ratios) >= sd);
  }

  public double[] getLogRatiosBySystematicName (String systematicName) {
    double[] logRatios = new double[this.size()];
    for (int i=0; i<this.size(); i++) {
    	logRatios[i] = ((MicroArray) this.elementAt(i)).getLogRatioBySystematicName(systematicName);
    }
    return logRatios;
  }

  public String toStanfordHeader() {
    StringBuffer result = new StringBuffer();
    result.append("UNIQID\tNAME");
    for (int i=0; i<this.size(); i++) {
      result.append('\t');
      result.append(((MicroArray)this.elementAt(i)).getName());
    }
    return result.toString();
  }

  public String toStanford (String systematicName) {
    StringBuffer result = new StringBuffer();
    MicroArray microArray0 = (MicroArray)this.elementAt(0);
    String geneName = microArray0.getGeneNameBySystematicName(systematicName);
    result.append(systematicName);
    result.append('\t');
    result.append(geneName);
    for (int i=0; i<this.size(); i++) {
      result.append('\t');
      result.append(((MicroArray)this.elementAt(i)).getLogRatioBySystematicName(systematicName));
    }
    return result.toString();
  }
}
