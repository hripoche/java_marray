<html>
<head>
<title>Marray application</title>
<script>
function op() { //This function is used with folders that do not open pages themselves. See online docs.
}
</script>
</head>

<!--
(Please keep all copyright notices.)
This frameset document includes the FolderTree script.
Script found in: http://www.treeview.net
Author: Marcelino Alves Martins
-->

<!-- Instructions:
     -Make changes to the title and add other head tags above.
     -Do not makes changes to the left frame.
	 -You can change the name of the right-frame document
      from "startPage.htm" to something else,
	  but use the same name in the argument of the first
	  gFld in the defineMyTree.js file.
	 -Do not change the name of the frames ("treeframe"
      and "basefrm") or else the links in the tree will
	  not work.
	 -Note: the script was not designed to work in a 
      standalone page without frames. -->


<frameset cols="200,*" onResize="if (navigator.family == 'nn4') window.location.reload()"> 
  <frame src="menuFramesetLeftFrame.jsp" name="treeframe" > 
  <frame src="login.jsp" name="basefrm"> 
</frameset> 


</html>
