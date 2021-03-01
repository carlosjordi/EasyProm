<?php
$server ="localhost";
$user ="root";
$password ="";
$database ="amEMpPCuI5";
$port ="3306";

//utf-8
function ConvertirUTF8($array){

    array_walk_recursive($array, function(&$item,$key){
        if (!mb_detect_encoding($item,'utf-8', true)){
            $item = utf8_encode($item);
        } 
    });
    return $array;
}

//String de conexion

$conexion = new mysqli($server,$user,$password,$database,$port);
if ($conexion -> connect_errno) {
    die($conexion -> connect_error);
}

//guardar, modificar, eliminar
function NonQuery($sqlstr, &$conexion = null){

    if (!$conexion)global $conexion;
    $result = $conexion->query($sqlstr);
    return $conexion -> affected_rows;

}


//select
function ObtenerRegistros($sqlstr, &$conexion = null){

    if (!$conexion)global $conexion;
    $result = $conexion->query($sqlstr);
    $resultArray = array();
    foreach($result as $registros){
        $resultArray[] = $registros;
    }

    return $resultArray;
}

?>