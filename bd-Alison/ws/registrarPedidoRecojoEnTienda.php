<?php

include('../database/AlisonConnection.php');

$data = json_decode(file_get_contents('php://input'));
$idLocal = $data->id_local;
$idSolicitante = $data->id_solicitante;
$fechaRecojo = $data->fecha_recojo;
$horaRecojo = $data->hora_recojo;

$consulta = "CALL sp_registrar_pedido_recojo_en_tienda($idLocal, '$idSolicitante', '$fechaRecojo', '$horaRecojo');";

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
