// Stanford.java

package marray;

import java.io.*;
import java.util.*;

import util.*;
import classifier.Item;

public class Stanford  {
  
  private String[] names;
  private String[] descriptions;
  private double[][] experiments;

  private String[] experimentNames;

  private int index;

  public Stanford (int length, int expLen) {
    names = new String[length];
    descriptions = new String[length];
    experiments = new double[expLen][length];
  }

  public static Stanford read (String filename, Report report) {
    SmallTextFile stf = new SmallTextFile();
    try {
      stf.read(filename);
    } catch (IOException e) {
      report.appendLine("Can't read: "+filename);
      e.printStackTrace();
    }
    String[] lines = stf.getLines();
    
    String line0 = lines[0];
    String[] stringArray0 = Lib.asStringArrayWithNulls(line0,"\t");

    int expLen = stringArray0.length-2;
    String[] expNames = new String[expLen];
    for (int i=2; i<stringArray0.length; i++) {
      expNames[i-2] = stringArray0[i];
    }
    
    Stanford stanford = new Stanford(lines.length,expLen);
    stanford.experimentNames = expNames;
    
    if (!stanford.checkHeaderOk(stringArray0)) {
      report.appendLine("Bad format file - check column names");
      return null;
    }
    
    String[] stringArray;
    String line;
    for (int i=1; i<lines.length; i++) {
      line = lines[i];
      stringArray = Lib.asStringArrayWithNulls(line,"\t");
      stanford.add(stringArray,expLen);
    }
    lines = null;
    stf = null;
    return stanford;
  }

  public boolean checkHeaderOk (String[] stringArray) {
    return (stringArray.length > 2 &&
            stringArray[0].equals("UNIQID") && 
            stringArray[1].equals("NAME"));
  }

  public void add (String[] stringArray, int expLen) {
    
    names[index] = stringArray[0];
    descriptions[index] = stringArray[1];

    for (int i=0; i<expLen; i++) {
      experiments[i][index] = Double.parseDouble(stringArray[i+2]);
    }
    index++;
  }

  /**
   * Number of genes
   */
  public int getLength() {
    return index;
  }
  
  /**
   * Number of experiments
   */
  public int getSize() {
    return experimentNames.length;
  }

  public double[] getLogRatiosByIndex (int index) {
    int size = getSize();
    double[] logRatios = new double[size];
    for (int i=0; i<size; i++) {
    	logRatios[i] = experiments[i][index];
    }
    return logRatios;
  }

  /**
   * Call toGeneList for each class = experiment
   */
  public boolean toGeneLists (int aDistance, String directory, String prefix, String suffix, SmallTextFile expNames, Vector files, Report report) {
    boolean result = true;
    for (int i=0; i<experimentNames.length; i++) {
      result = result && toGeneList(i,experimentNames[i],aDistance,directory,prefix,suffix,files,report);
      expNames.appendLine(experimentNames[i]);
    }
    return result;
  }

  public boolean toGeneList (int aClass, String aClassName, int aDistance, String directory, String prefix, String suffix, Vector files, Report report) {

    int len = getLength(); // nb genes
    int size = getSize();  // nb experiments

    double[] anExperimentClass = new double[size];
    for (int i=0; i<size; i++) {
      anExperimentClass[i] = (i == aClass ?
                              Lib.max(experiments[i]) :
                              Lib.min(experiments[i]));
    }

    Gene[] geneArray = new Gene[len]; 
    String name;
    double dist;
    String description;

    for (int i=0; i<len; i++) {
      name = names[i];
      description = descriptions[i];
      dist = Lib.distance(anExperimentClass,this.getLogRatiosByIndex(i),aDistance);
      geneArray[i] = new Gene(name,dist,description);
    }
    Arrays.sort(geneArray,new GeneComparator(GeneComparator.ASCENDING));

    File ff = null;
    try {
      ff = File.createTempFile(prefix,suffix,new File(directory));
    } catch (IOException e) {
      e.printStackTrace();
      report.appendLine("Can't create temp file");
      return false;
    }
    if (report.getMode() == Report.SERVLET_MODE) ff.deleteOnExit();

    SmallTextFile stf = new SmallTextFile();
    String filename = ff.getAbsolutePath();
    System.out.println("file: "+filename);
    stf.appendLine("gene\tdistance\tdescription");
    for (int i=0; i<len; i++) {
      stf.appendLine(geneArray[i].toString());
    }
    try {
      stf.write(filename);
    } catch (IOException e) {
      e.printStackTrace();
      report.appendLine("Can't write: "+filename);
      return false;
    }
    files.addElement(ff);
    return true;
  }

  public Item[] getItems () {
    int len = getLength();
    Item[] result = new Item[len];
    for (int i=0; i<len; i++) {
      result[i] = new Item(i,names[i],descriptions[i],getLogRatiosByIndex(i));
    }
    return result;
  }
}
