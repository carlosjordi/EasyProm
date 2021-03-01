<?php

include('../database/AlisonConnection.php');

$usuario = $_GET["usuario"];
$contrasena = $_GET["contrasena"];

$consulta = $conn->prepare("SELECT idPersonal, nombrePersonal, apellidoPersonal FROM PERSONAL WHERE usuario = '$usuario' AND contrasena = '$contrasena' AND estadoPersonal = 1;");

$consulta->execute();

$consulta->bind_result($id, $nombre, $apellido);

$personal = new stdClass();

if($consulta->fetch())
{
    $personal->id = $id;
    $personal->nombre = $nombre;
    $personal->apellido = $apellido;
    $personal->puesto = "Puesto temporal"; // lo harcodeo temporalmente por que no hay tabla para los cargos y quería ir un poco rápido
}

echo json_encode($personal);

mysqli_close($conn);
