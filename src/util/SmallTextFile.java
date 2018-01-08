// SmallTextFile.java

package util;

import java.io.*;
import java.util.*;

/**
 * @author H. Ripoche 97/03/04
 */
public class SmallTextFile {

  final private static String separators = "\n\r";
  private StringBuffer content;

  public SmallTextFile () {
    content = new StringBuffer();
  }

  public SmallTextFile (String filename) throws IOException {
    content = new StringBuffer();
    read(filename);
  }

  /**
   * Loads a file in memory.
   * @exception IOException If the file can't be read.
   */
  public void read (String filename) throws IOException {

    File f = new File(filename);

    int size = (int) f.length(); // small file !!!
    int bytes_read = 0;

    FileInputStream fis = new FileInputStream(f);
    DataInputStream dis = new DataInputStream(fis);

    byte[] data = new byte[size];
    while (bytes_read < size) {
      //bytes_read += fis.read(data,bytes_read,size-bytes_read);
      bytes_read += dis.read(data,bytes_read,size-bytes_read);
    }
    dis.close();

    content = new StringBuffer(new String(data));
  }

  /**
   * Writes object to file.
   * @exception IOException If file can't be written.
   */
  public void write (String filename) throws IOException {

    File f = new File(filename);

    FileOutputStream fos = new FileOutputStream(f);
    DataOutputStream dos = new DataOutputStream(fos);

    //byte[] data = content.toString().getBytes();
    //fos.write(data);

    dos.writeBytes(content.toString());
    dos.flush();
    dos.close();
  }

  public void appendLine (String s) {
    content.append(s);
    content.append(System.getProperty("line.separator"));
  }

  public void append (String s) {
    content.append(s);
  }

  public String getText () {
    return content.toString();
  }

  public StringBuffer getStringBuffer () {
    return content;
  }

  public void setText (String s) {
    content = new StringBuffer(s);
  }

  public String[] getLines () {
    StringTokenizer st = new StringTokenizer(content.toString(),separators);
    String[] result = new String[st.countTokens()];
    int i = 0;

    while (st.hasMoreTokens()) {
      result[i++] = st.nextToken();
    }

    return result;
  }

  public String[] getLinesWithoutComments () {
    StringTokenizer st = new StringTokenizer(content.toString(),separators);
    String[] result;
    Vector v = new Vector();
    int i = 0;
    String line;

    while (st.hasMoreTokens()) {
      line = st.nextToken();
      if (!line.startsWith("#")) {
        v.addElement(line);
      }
    }
    result = new String[v.size()];
    v.copyInto(result);

    return result;
  }
}
