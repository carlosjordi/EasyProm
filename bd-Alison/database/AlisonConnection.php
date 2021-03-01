<?php
$dbhost = 'localhost:3306';
$dbuser = 'root';
$dbpass = '';
$database = 'amEMpPCuI5';
$conn = new mysqli($dbhost, $dbuser, $dbpass, $database);
if(mysqli_connect_errno())
{
    die('no se puede conectar a la base de datos : '. mysqli_connect_error());
}
?>
