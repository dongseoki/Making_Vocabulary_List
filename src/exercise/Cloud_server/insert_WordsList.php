<?php
  $connect = mysqli_connect("localhost", "wordlike", "likeword", "WordlikeDB");
  if(!$connect) die() ;

  $insert_query = "INSERT INTO WordsList ( membershipNumber, Title) VALUES ('".$_GET[memNum]."' , '".$_GET[Title]."')";
 
  mysqli_query($connect, $insert_query);
  echo mysqli_error($connect) . "\n";
  mysqli_close($connect);
?>
