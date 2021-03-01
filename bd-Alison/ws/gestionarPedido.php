<?php

include('../database/AlisonConnection.php');

$data = json_decode(file_get_contents('php://input'));
$idPedido = $data->id_pedido;
$observacion = $data->observacion;
$estado = $data->estado;

$consulta = "CALL sp_gestionar_pedido($idPedido, '$observacion', $estado);";

$response = new stdClass();

if (mysqli_query($conn, $consulta)) {
    if ($estado == 1) {
        $response->mensaje = "Pedido Aceptado";
    } else if ($estado == 2) {
        $response->mensaje = "Pedido Rechazado";
    }
    echo json_encode($response);
} else {
    $response->mensaje = "Hubo un problema";
    echo json_encode($response);
}

mysqli_close($conn);
