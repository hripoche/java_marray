// LargeTextFile.java

package util;

import java.io.*;

/**
 * @author H. Ripoche 2000/10/13
 */
public class LargeTextFile {

  /**
   * <ul>
   *     <li> -1 indéfini
   *     <li>  0 openRead
   *     <li>  1 openWrite
   * </ul>
   */
  private int mode = -1;
  private BufferedReader bf;
  private PrintWriter pw;

  public LargeTextFile () {
    bf = null;
    pw = null;
  }

  public void openRead (String filename) throws IOException {
    if (mode == 1) return;
    bf = new BufferedReader(new FileReader(filename));
    mode = 0;
  }

  public void openWrite (String filename) throws IOException {
    if (mode == 0) return;
    pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
    mode = 1;
  }

  public void close () throws IOException {
    if (bf != null) {
       bf.close();
       bf = null;
    }
    if (pw != null) {
       pw.close();
       pw = null;
    }
  }

  public String getLine () throws IOException {
    if (mode == 1) return null;
    return bf.readLine();
  }

  public void putLine (String s) throws IOException {
    if (mode == 0) return;
    pw.write(s+System.getProperty("line.separator"));
  }

  public void put (String s) throws IOException {
    if (mode == 0) return;
    pw.write(s);
  }

  public void flush () throws IOException {
    pw.flush();
  }
}
