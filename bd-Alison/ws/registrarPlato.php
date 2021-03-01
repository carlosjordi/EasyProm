<?php

include('../database/AlisonConnection.php');

$json = file_get_contents('php://input');
$data = json_decode($json);

$nombre = $data->nombre;
$precio = $data->precio;
$descripcion = $data->descripcion;
$foto = $data->foto;
$base = $data->base_foto;

$nombre_sin_espacios = str_replace(' ', '', $nombre);
$base_foto = $nombre_sin_espacios.$precio.".jpg";
$url_foto = $base.$base_foto;

$consulta = "CALL sp_registrar_plato('$nombre', $precio, '$url_foto', '$descripcion');";

$ruta_a_subir = "imagenes_platos/$base_foto";

$response = new stdClass();

if (mysqli_query($conn, $consulta)) {
    file_put_contents($ruta_a_subir, base64_decode($foto));
    $response->mensaje = "Plato Registrado";
    echo json_encode($response);
} else {
    $response->mensaje = "Hubo un problema";
    echo json_encode($response);
}

mysqli_close($conn);

?>