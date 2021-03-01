<?php

include('../database/AlisonConnection.php');

$consulta = $conn->prepare("CALL sp_solicitar_cabeceras_todos_pedidos();");

$consulta->execute();
$consulta->bind_result($id, $local, $direccion, $estado, $tipoPedido, $observacion, $referencia, $fecha, $hora);

$response = array();

while ($consulta->fetch()) {
    $temp = array();
    $temp['id'] = $id;
    $temp['local'] = $local;
    $temp['direccion'] = $direccion;
    $temp['estado'] = $estado;
    $temp['tipo_pedido'] = $tipoPedido;
    $temp['observacion'] = $observacion;
    $temp['referencia'] = $referencia;
    $temp['fecha'] = $fecha;
    $temp['hora'] = $hora;
    array_push($response, $temp);
}

$wrapper =new stdClass();
$wrapper->cabeceras = $response;

echo json_encode($wrapper);

mysqli_close($conn);
