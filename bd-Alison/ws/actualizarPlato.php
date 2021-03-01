<?php

include('../database/AlisonConnection.php');

$data = json_decode(file_get_contents('php://input'));
$id = $data->id;
$nombre = $data->nombre;
$precio = $data->precio;
$descripcion = $data->descripcion;
$foto = $data->foto;
$base = $data->base_foto;

$nombre_sin_espacios = str_replace(' ', '', $nombre);
$base_foto = $nombre_sin_espacios . $precio . ".jpg";

$url_foto = $base . $base_foto;

$consulta = "";

if ($foto == "Imagen no fue actualizada") {
    $consulta = "CALL sp_actualizar_plato_no_imagen('$nombre', $precio, '$descripcion', $id)";
} else {
    $consulta = "CALL sp_actualizar_plato_imagen('$nombre', $precio, '$descripcion','$url_foto', $id)";
}

$ruta_a_subir = "imagenes_platos/$base_foto";

$response = new stdClass();

if (mysqli_query($conn, $consulta)) {
    if ($foto != "Imagen no fue actualizada") {
        file_put_contents($ruta_a_subir, base64_decode($foto));
    }
    $response->mensaje = "Plato Actualizado";
    echo json_encode($response);
} else {
    $response->mensaje = "Hubo un problema";
    echo json_encode($response);
}

mysqli_close($conn);
