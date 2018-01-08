// MicroArray.java

package marray;

import util.Report;

/**
 * @author H. Ripoche
 */
public abstract class MicroArray {
  abstract public void initialFilter();
  abstract public void filterByLogRatio (double signalNoiseRatio);
  abstract public void filterBySignalNoiseRatio (double signalNoiseRatio);
  abstract public String[] getSystematicNamesWithFilter();
  abstract public String[] getSystematicNames();
  abstract public String getName();
  abstract public String getGeneNameBySystematicName (String systematicName);
  abstract public double getLogRatioBySystematicName (String systematicName);
  abstract public void normalize();
  abstract public void printStats(Report report);
}
