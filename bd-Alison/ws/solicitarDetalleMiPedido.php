<?php

include('../database/AlisonConnection.php');
/*
$data = json_decode(file_get_contents('php://input'));
$idPedido = $data->id_pedido;
*/
$idPedido = $_GET['id_pedido'];

$consulta = $conn->prepare("CALL sp_solicitar_detalle_pedido($idPedido);");

$consulta->execute();
$consulta->bind_result($id, $nombre, $precio, $urlImagen, $descripcion, $cantidad);

$response = array();

while ($consulta->fetch()) {
    $temp = array();
    $temp['id'] = $id;
    $temp['nombre'] = $nombre;
    $temp['precio'] = $precio;
    $temp['url_imagen'] = $urlImagen;
    $temp['descripcion'] = $descripcion;
    $temp['cantidad'] = $cantidad;
    array_push($response, $temp);
}

$wrapper =new stdClass();
$wrapper->detalle = $response;

echo json_encode($wrapper);

mysqli_close($conn);
