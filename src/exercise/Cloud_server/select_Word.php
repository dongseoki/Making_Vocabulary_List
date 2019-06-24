<?php
  $connect = mysqli_connect("localhost", "wordlike", "likeword", "WordlikeDB");
  if(!$connect) die("Not connected:".mysqli_error());

  $sq = "SELECT *  FROM Word WHERE (enrollNumber = '".$_GET[enrNum]."')";

  $re = mysqli_query($connect, $sq);
  echo mysqli_error($connect) . "\n";
    while ($result = mysqli_fetch_array($re)) {
	print $result[2];
	print " : ";
	print $result[3];
	print " : ";
	print $result[4];
        print "<BR>";
    }

  mysqli_close($connect);
?>
