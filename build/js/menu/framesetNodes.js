// You can find instructions for this file here:
// http://www.treeview.net

// Decide if the names are links or just the icons
USETEXTLINKS = 1  //replace 0 with 1 for hyperlinks

// Decide if the tree is to start all open or just showing the root folders
STARTALLOPEN = 1 //replace 0 with 1 to show the whole tree

ICONPATH = '../images/menu/' //change if the gif's folder is a subfolder, for example: 'images/'

/*
foldersTree = gFld("<i>FolderTree Demo</i>", "demoFramesetRightFrame.html")
  aux1 = insFld(foldersTree, gFld("Application example", "demoFramesetRightFrame.html"))
    aux2 = insFld(aux1, gFld("United States", "http://www.treeview.net/ft/example/pictures/beenthere_america.gif"))
      insDoc(aux2, gLnk("R", "Boston", "http://www.treeview.net/ft/example/pictures/beenthere_boston.jpg"))
      insDoc(aux2, gLnk("R", "New York", "http://www.treeview.net/ft/example/pictures/beenthere_newyork.jpg"))
      insDoc(aux2, gLnk("R", "Washington", "http://www.treeview.net/ft/example/pictures/beenthere_washington.jpg"))
    aux2 = insFld(aux1, gFld("Europe", "http://www.treeview.net/ft/example/pictures/beenthere_europe.gif"))
      insDoc(aux2, gLnk("R", "Edinburgh", "http://www.treeview.net/ft/example/pictures/beenthere_edinburgh.gif"))
      insDoc(aux2, gLnk("R", "London", "http://www.treeview.net/ft/example/pictures/beenthere_london.jpg"))
      insDoc(aux2, gLnk("R", "Munich", "http://www.treeview.net/ft/example/pictures/beenthere_munich.jpg"))
      insDoc(aux2, gLnk("R", "Athens", "http://www.treeview.net/ft/example/pictures/beenthere_athens.jpg"))
      insDoc(aux2, gLnk("R", "Florence", "http://www.treeview.net/ft/example/pictures/beenthere_florence.jpg"))
      //The next three links have their http protocol appended by the script
      insDoc(aux2, gLnk("Rh", "Pisa", "www.treeview.net/ft/example/pictures/beenthere_pisa.jpg"))
      insDoc(aux2, gLnk("Rh", "Rome", "www.treeview.net/ft/example/pictures/beenthere_rome.jpg"))
      insDoc(aux2, gLnk("Rh", "Lisboa", "www.treeview.net/ft/example/pictures/beenthere_lisbon.jpg"))
  aux1 = insFld(foldersTree, gFld("3 Types of folders", "javascript:parent.op()"))
    aux2 = insFld(aux1, gFld("Linked", "http://www.treeview.net/ft/example/pictures/beenthere_unitedstates.gif"))
      insDoc(aux2, gLnk("R", "New York", "http://www.treeview.net/ft/example/pictures/beenthere_newyork.jpg"))
    aux2 = insFld(aux1, gFld("Empty, linked", "http://www.treeview.net/ft/example/pictures/beenthere_europe.gif"))
    //NS4 needs the href to be non-empty to process other events such as open folder,
    //hence the op function
    aux2 = insFld(aux1, gFld("Not linked", "javascript:parent.op()"))
      insDoc(aux2, gLnk("R", "New York", "http://www.treeview.net/ft/example/pictures/beenthere_newyork.jpg"))
  aux1 = insFld(foldersTree, gFld("Targets", "javascript:parent.op()"))
      insDoc(aux1, gLnk("R", "Right frame", "http://www.treeview.net/ft/example/pictures/beenthere_edinburgh.gif"))
      insDoc(aux1, gLnk("B", "New window", "http://www.treeview.net/ft/example/pictures/beenthere_london.jpg"))
      insDoc(aux1, gLnk("T", "Top frame", "http://www.treeview.net/ft/example/pictures/beenthere_munich.jpg"))
      insDoc(aux1, gLnk("S", "This frame", "http://www.treeview.net/ft/example/pictures/beenthere_athens.jpg"))

  aux1 = insFld(foldersTree, gFld("Other Icons", "javascript:parent.op()"))
  aux1.iconSrc = ICONPATH + "diffFolder.gif"
  aux1.iconSrcClosed = ICONPATH + "diffFolder.gif"
    docAux = insDoc(aux1, gLnk("B", "D/L FolderTree", "http://www.treeview.net/treemenu/download.asp"))
    docAux.iconSrc = ICONPATH + "diffDoc.gif"
*/

foldersTree = gFld("<i>Analyses sur microarrays - IGR</i>", "welcome.jsp")
  aux1 = insFld(foldersTree, gFld("Fichiers microarrays", "javascript:parent.op()"))
    insDoc(aux1, gLnk("R", "Corr&eacute;lation entre deux microarrays Agilent", "correlation.jsp"))
    insDoc(aux1, gLnk("R", "Conversion Agilent vers ArrayPlot", "convert_arp.jsp"))
    insDoc(aux1, gLnk("R", "Conversion Agilent vers Agilent", "convert_agilent.jsp"))
    insDoc(aux1, gLnk("R", "Conversion vers le format Stanford", "marray_to_stanford_step1.jsp"))
  aux1 = insFld(foldersTree, gFld("Fichiers Stanford", "javascript:parent.op()"))
    insDoc(aux1, gLnk("R", "Liste de g&egrave;nes tri&eacute;s", "stanford_to_lists.jsp"))
    insDoc(aux1, gLnk("R", "Classification kmeans", "kmeans.jsp"))
    insDoc(aux1, gLnk("R", "Classification hi&eacute;rarchique 'single linkage'", "hierarchical.jsp"))
    insDoc(aux1, gLnk("R", "Classification hi&eacute;rarchique 'UPGMA'", "upgma.jsp"))
  aux1 = insFld(foldersTree, gFld("Admin", "javascript:parent.op()"))
    insDoc(aux1, gLnk("R", "D&eacute;connection", "disconnect.jsp"))
