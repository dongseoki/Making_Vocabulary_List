<?php
  $connect = mysqli_connect("localhost", "wordlike", "likeword", "WordlikeDB");
  if(!$connect) die("Not connected:".mysqli_error());


  $sq = "SELECT * FROM WordsList WHERE ( Title LIKE '%". $_GET[vari]. "%' ) OR ( membershipNumber = '".$_GET[vari]."' );";
  $re = mysqli_query($connect, $sq);
  echo mysqli_error($connect) . "\n";
    while ($result = mysqli_fetch_array($re)) {
        print $result[1];
	print " : "; 
        print $result[2];
        print "<BR>";
    }

  mysqli_close($connect);
?>
