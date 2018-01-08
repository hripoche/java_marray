// ParameterFilter.java

package marray;

/**
 * @author H. Ripoche
 */
public class ParameterFilter {

  private int initialSelected;

  private double pValueLogRatioFilter;
  private int pValueLogRatioSelected;
  private boolean pValueLogRatioApply;

  private double logRatioFilter;
  private int logRatioSelected;
  private boolean logRatioApply;

  private double signalNoiseRatioFilter;
  private int signalNoiseRatioSelected;
  private boolean signalNoiseRatioApply;

  private int minimalNumberOfExperimentsFilter;
  private boolean minimalNumberOfExperimentsApply;

  private double minimalVariationBetweenExperimentsFilter;
  private boolean minimalVariationBetweenExperimentsApply;

  private double standardDeviationFilter;
  private boolean standardDeviationApply;

  public ParameterFilter () {
    pValueLogRatioApply = false;
    logRatioApply = false;
    signalNoiseRatioApply = false;
    minimalNumberOfExperimentsApply = false;
    minimalVariationBetweenExperimentsApply = false;
    standardDeviationApply = false;
  }

  public int getInitialSelected () {
    return initialSelected;
  }

  public void setInitialSelected (int s) {
    initialSelected = s;
  }

  public double getPValueLogRatioFilter () {
    return pValueLogRatioFilter;
  }

  public void setPValueLogRatioFilter (double f) {
    pValueLogRatioFilter = f;
  }

  public int getPValueLogRatioSelected () {
    return pValueLogRatioSelected;
  }

  public void setPValueLogRatioSelected (int s) {
    pValueLogRatioSelected = s;
  }

  public boolean getPValueLogRatioApply () {
    return pValueLogRatioApply;
  }

  public void setPValueLogRatioApply (boolean apply) {
    pValueLogRatioApply = apply;
  }

  public double getLogRatioFilter () {
    return logRatioFilter;
  }

  public void setLogRatioFilter (double f) {
    logRatioFilter = f;
  }

  public int getLogRatioSelected () {
    return logRatioSelected;
  }

  public void setLogRatioSelected (int s) {
    logRatioSelected = s;
  }

  public boolean getLogRatioApply () {
    return logRatioApply;
  }

  public void setLogRatioApply (boolean apply) {
    logRatioApply = apply;
  }

  public double getSignalNoiseRatioFilter () {
    return signalNoiseRatioFilter;
  }

  public void setSignalNoiseRatioFilter (double f) {
    signalNoiseRatioFilter = f;
  }

  public int getSignalNoiseRatioSelected () {
    return signalNoiseRatioSelected;
  }

  public void setSignalNoiseRatioSelected (int s) {
    signalNoiseRatioSelected = s;
  }

  public boolean getSignalNoiseRatioApply () {
    return signalNoiseRatioApply;
  }

  public void setSignalNoiseRatioApply (boolean apply) {
    signalNoiseRatioApply = apply;
  }

  public int getMinimalNumberOfExperimentsFilter () {
    return minimalNumberOfExperimentsFilter;
  }

  public void setMinimalNumberOfExperimentsFilter (int n) {
    minimalNumberOfExperimentsFilter = n;
  }

  public boolean getMinimalNumberOfExperimentsApply () {
    return minimalNumberOfExperimentsApply;
  }

  public void setMinimalNumberOfExperimentsApply (boolean apply) {
    minimalNumberOfExperimentsApply = apply;
  }

  public double getMinimalVariationBetweenExperimentsFilter () {
    return minimalVariationBetweenExperimentsFilter;
  }

  public void setMinimalVariationBetweenExperimentsFilter (double x) {
    minimalVariationBetweenExperimentsFilter = x;
  }

  public boolean getMinimalVariationBetweenExperimentsApply () {
    return minimalVariationBetweenExperimentsApply;
  }

  public void setMinimalVariationBetweenExperimentsApply (boolean apply) {
    minimalVariationBetweenExperimentsApply = apply;
  }

  public double getStandardDeviationFilter () {
    return standardDeviationFilter;
  }

  public void setStandardDeviationFilter (double x) {
    standardDeviationFilter = x;
  }

  public boolean getStandardDeviationApply () {
    return standardDeviationApply;
  }

  public void setStandardDeviationApply (boolean apply) {
    standardDeviationApply = apply;
  }
}
