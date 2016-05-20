<?php
$con=mysqli_connect("placeholder", "placeholder", "placeholder");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$result = mysqli_query($con,"SELECT module_name FROM module");
$data = mysqli_fetch_array($result);
$i = 0;
while($data[$i]){
    echo $data[$i];
}
mysqli_close($con);
?>
