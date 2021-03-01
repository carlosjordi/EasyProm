<?php

include('../database/AlisonConnection.php');

$data = json_decode(file_get_contents('php://input'));
$idSolicitante = $data->id_solicitante;
$direccionEntrega = $data->direccion_entrega;
$referenciaEntrega = $data->referencia_entrega;

$consulta = "CALL sp_registrar_pedido_delivery('$idSolicitante', '$direccionEntrega', '$referenciaEntrega');";

$response = new stdClass();

if ($id_pedido = mysqli_query($conn, $consulta)) {
    $array = mysqli_fetch_array($id_pedido);
    $response->mensaje = "Pedido Registrado";
    $response->id_pedido = $array['id_pedido'];
    echo json_encode($response);
} else {
    $response->mensaje = "Hubo un problema";
    $response->id_pedido = -1;
    echo json_encode($response);
}

mysqli_close($conn);
