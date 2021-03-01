<?php

include('../database/AlisonConnection.php');

$data = json_decode(file_get_contents('php://input'));
$id = $data->id;

$consulta = "CALL sp_eliminar_plato($id)";

$response = new stdClass();

if (mysqli_query($conn, $consulta)) {
    $response->mensaje = "Plato Eliminado";
    echo json_encode($response);
} else {
    $response->mensaje = "Hubo un problema";
    echo json_encode($response);
}

mysqli_close($conn);
