<?php

include('../database/AlisonConnection.php');

$data = json_decode(file_get_contents('php://input'));
$idPedido = $data->id_pedido;
$idPlato = $data->id_plato;
$cantidad = $data->cantidad;

$consulta = "CALL sp_agregar_plato_a_detalle_pedido($idPedido, $idPlato, $cantidad);";

$response = new stdClass();

if (mysqli_query($conn, $consulta)) {
    $response->mensaje = "Plato agregado a detalle";
    echo json_encode($response);
} else {
    $response->mensaje = "Hubo un problema";
    echo json_encode($response);
}

mysqli_close($conn);
