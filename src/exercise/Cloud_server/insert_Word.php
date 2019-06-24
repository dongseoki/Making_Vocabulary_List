<?php
  $connect = mysqli_connect("localhost", "wordlike", "likeword", "WordlikeDB");
  if(!$connect) die() ;

  $insert_query = "INSERT INTO Word (enrollNumber, wordSpelling, wordMeaning, wordSentence) VALUES (".$_GET[enrNum].", '".$_GET[spel]."', '".$_GET[mean]."', '".$_GET[sent]."')";
 
  mysqli_query($connect, $insert_query);
  echo mysqli_error($connect) . "\n";
  mysqli_close($connect);
?>
