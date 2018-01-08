// Application.java

package application;

import java.io.*;
import java.util.*;

import util.*;
import marray.*;

/**
 * @author H. Ripoche
 */
public class Application {

  final static String[] modes = {"agilent_to_arp", "agilent_to_agilent", "correlation", "pool", "agilent_to_stanford", "affymetrix_to_stanford", "genepix_to_stanford", "stanford_to_lists", "kmeans", "hierarchical", "upgma"};

  static String mode;

  static void usage() {
    System.out.println("\nusage:\n");
    System.out.println("java Application agilent_to_arp <inputFile> <outputFile>");
    System.out.println("java Application agilent_to_agilent <inputFile> <outputFile>");
    System.out.println("java Application correlation <inputFile1> <inputFile2>");
    System.out.println("java Application pool <inputFile1> <inputFile2> where <inputFile> is a list of files");
    System.out.println("java Application agilent_to_stanford <inputFile> <outputFile> where <inputFile> is a list of files");
    System.out.println("java Application affymetrix_to_stanford <inputFile> <outputFile> where <inputFile> is a list of files\n");
    System.out.println("java Application genepix_to_stanford <inputFile> <outputFile> where <inputFile> is a list of files\n");
    System.out.println("java Application stanford_to_lists <inputFile> <aDistance> where <inputFile> is a file in Stanford format where the experience names may be used as filenames\n");
    System.out.println("java Application kmeans <inputFile> <nbClusters> <maxIter>\n");
    System.out.println("java Application hierarchical <inputFile> mode dist <image file>\n");
    System.out.println("java Application upgma <inputFile> mode dist <image file>\n");
  }

  static void pool (String[] args, Report report) {
    SmallTextFile stf;
    Agilent agilent;
    try {
      stf = new SmallTextFile();
      stf.read(args[1]);
      agilent = Agilent.read(args[2]);
      if (agilent == null) return;
    } catch (Exception e) {
      System.err.println(""+e);
      return;
    }
    String[] lines = stf.getLinesWithoutComments();
    MicroArrays microArrays = new MicroArrays();
    Agilent a;
    Agilent median;
    String[] str;
    for (int i=0; i<lines.length; i++) {
      a = Agilent.read(lines[i]);
      a.initialFilter();
      a.printStats(report);
      if (a == null) return; // message should have been provided
      microArrays.add(a);
    }
    median = Agilent.median(microArrays);
    if (median == null) return;
    Computation.correlationAux(median,agilent,report);
  }

  public static void main(String[] args) {

    try {
      mode = args[0];
    } catch (Exception e) {
      usage();
      return;
    }

    Report report = new Report(Report.APPLICATION_MODE);

    ParameterFilter parameterFilter = new ParameterFilter();
   
    if (mode.equals(modes[0])) {
      Computation.agilentToArp(args,report);
    } else if (mode.equals(modes[1])) {
      Computation.agilentToAgilent(args,report);
    } else if (mode.equals(modes[2])) {
      Computation.correlation(args,report);
    } else if (mode.equals(modes[3])) {
      pool(args,report);
    } else if (mode.equals(modes[4])) {
      Computation.toStanford(args,Computation.MARRAY_AGILENT,parameterFilter,report);
    } else if (mode.equals(modes[5])) {
      Computation.toStanford(args,Computation.MARRAY_AFFYMETRIX,parameterFilter,report);
    } else if (mode.equals(modes[6])) {
      Computation.toStanford(args,Computation.MARRAY_GENEPIX,parameterFilter,report);
    } else if (mode.equals(modes[7])) {
      Computation.stanfordToLists(args,".","stanford_to_lists_","",new SmallTextFile(),new Vector(),report);
    } else if (mode.equals(modes[8])) {
      Computation.kmeans(args,report);
    } else if (mode.equals(modes[9])) {
      Computation.hierarchical(args,report);
    } else if (mode.equals(modes[10])) {
      Computation.upgma(args,report);
    } else {
      usage();
      return;
    }
  }
}
