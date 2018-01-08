// Copyright (C) 2002 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.oreilly.servlet.multipart;

import java.io.*;

/**
 * Implements a renaming policy that adds increasing integers to the body of
 * any file that collides.  For example, if foo.gif is being uploaded and a
 * file by the same name already exists, this logic will rename the upload
 * foo1.gif.  A second upload by the same name would be foo2.gif.
 * 
 * @author Jason Hunter
 * @version 1.0, 2002/04/30, initial revision, thanks to Yoonjung Lee
 *                           for this idea
 */
public class DefaultFileRenamePolicy implements FileRenamePolicy {
  
  public File rename(File f) {
    if (!f.exists()) {
      return f;
    }
    String name = f.getName();
    String body = null;
    String ext = null;

    int dot = name.lastIndexOf(".");
    if (dot != -1) {
      body = name.substring(0, dot);
      ext = name.substring(dot);  // includes "."
    }
    else {
      body = name;
      ext = "";
    }

    int count = 0;
    while (f.exists()) {
      count++;
      String newName = body + count + ext;
      f = new File(f.getParent(), newName);
    }
    return f;
  }
}
