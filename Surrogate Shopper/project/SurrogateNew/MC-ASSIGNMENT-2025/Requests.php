 <?php
 $username = "s2814267";
 $password = "s2814267";
 $database = "d2814267";
 $link = mysqli_connect("127.0.0.1", $username, $password, $database);
 $output=array();
 /* Select queries return a resultset */
 if ($result = mysqli_query($link, "SELECT * from REQUESTS")) {
 while ($row=$result->fetch_assoc()){
 $output[]=$row;
 }
 }
 mysqli_close($link);
 echo json_encode($output);
 ?>