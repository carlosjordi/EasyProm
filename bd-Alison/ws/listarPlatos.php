<?php

include('../database/AlisonConnection.php');

$rs = mysqli_query($conn,"CALL sp_listar_platos()");

$objPlatos = new stdClass();

$listarPlatos=[];
while($row = mysqli_fetch_array($rs))
{
    $plato = new stdClass();

    $plato->idPlato=$row['idPlato'];
    $plato->nombrePlato=$row['nombrePlato'];
    $plato->precioPlato=$row['precioPlato'];
    $plato->estadoPlato=$row['estadoPlato'];
    $plato->imagenPlato=$row['imagenPlato'];
    $plato->descripcionPlato=$row['descripcionPlato'];

    $listarPlatos[] = $plato;
}

$objPlatos->PLATOS = $listarPlatos;

mysqli_close($conn);
echo json_encode($objPlatos);