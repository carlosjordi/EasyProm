-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 01-03-2021 a las 01:10:10
-- Versión del servidor: 10.4.17-MariaDB
-- Versión de PHP: 8.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `amemppcui5`
--
CREATE DATABASE IF NOT EXISTS `amemppcui5` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `amemppcui5`;

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_plato_imagen` (IN `nombre` VARCHAR(50), IN `precio` DECIMAL(11,2), IN `descripcion` VARCHAR(50), IN `url` TEXT, IN `id` INT)  BEGIN
	UPDATE PLATOS SET nombrePlato = nombre, precioPlato = precio, descripcionPlato = descripcion, imagenPlato = url WHERE idPlato = id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_plato_no_imagen` (IN `nombre` VARCHAR(50), IN `precio` DECIMAL(11,2), IN `descripcion` VARCHAR(50), IN `id` INT)  BEGIN
	UPDATE PLATOS SET nombrePlato = nombre, precioPlato = precio, descripcionPlato = descripcion WHERE idPlato = id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_agregar_plato_a_detalle_pedido` (`id_pedido` INT, `id_plato` INT, `cantidad` INT)  BEGIN
	INSERT INTO DETALLEPEDIDO (idPedido, idPlato, cantidadPlatoDetalle) VALUES (id_pedido, id_plato, cantidad);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminar_plato` (`id` INT)  BEGIN
	UPDATE PLATOS SET estadoPlato = 0 WHERE idPlato = id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_gestionar_pedido` (`id_pedido` INT, `observacion` VARCHAR(80), `estado` INT)  BEGIN
	UPDATE PEDIDO SET estadoPedido = estado, observacionPedido = observacion WHERE idPedido = id_pedido;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_listar_platos` ()  BEGIN
	SELECT idPlato, nombrePlato, precioPlato, estadoPlato, imagenPlato, descripcionPlato FROM `PLATOS` WHERE estadoPlato = 1;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_registrar_pedido_delivery` (`idSolicitante` VARCHAR(50), `direccion` VARCHAR(100), `referencia` VARCHAR(100))  BEGIN
	INSERT INTO PEDIDO (idLocal, id_solicitante, direccionEntrega, referenciaEntrega, tipoPedido) values (1, idSolicitante, direccion, referencia, 2);
    SELECT LAST_INSERT_ID() as id_pedido;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_registrar_pedido_recojo_en_tienda` (`idLocal` INT, `idSolicitante` VARCHAR(50), `fecha` VARCHAR(40), `hora` VARCHAR(40))  BEGIN
	INSERT INTO PEDIDO (idLocal, id_solicitante, fecha_recojo, hora_recojo, tipoPedido) values (idLocal, idSolicitante, fecha, hora, 1);
    SELECT LAST_INSERT_ID() as id_pedido;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_registrar_plato` (`nombre` VARCHAR(50), `precio` DECIMAL(11,2), `url` TEXT, `descripcion` VARCHAR(100))  BEGIN
	INSERT INTO PLATOS (nombrePlato, stockPlato, precioPlato, estadoPlato, imagenPlato, descripcionPlato) VALUES (nombre, 1, precio, 1, url, descripcion);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_solicitar_cabeceras_mis_pedidos` (`idSolicitante` VARCHAR(50))  BEGIN
	SELECT p.idPedido, l.nombreLocal, p.direccionEntrega, p.estadoPedido, p.tipoPedido, p.observacionPedido, p.referenciaEntrega, p.fecha_recojo, p.hora_recojo 
from PEDIDO p 
inner join LOCAL l 
on l.idLocal = p.idLocal 
WHERE p.id_solicitante = idSolicitante;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_solicitar_cabeceras_todos_pedidos` ()  BEGIN
	SELECT p.idPedido, l.nombreLocal, p.direccionEntrega, p.estadoPedido, p.tipoPedido, p.observacionPedido, p.referenciaEntrega, p.fecha_recojo, p.hora_recojo 
from PEDIDO p 
inner join LOCAL l 
on l.idLocal = p.idLocal 
where p.estadoPedido != 3;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_solicitar_detalle_pedido` (`idPedido` INT)  BEGIN
	SELECT dp.idPedido, pl.nombrePlato, pl.precioPlato, pl.imagenPlato, pl.descripcionPlato, dp.cantidadPlatoDetalle 
from DETALLEPEDIDO dp 
inner join PLATOS pl 
on pl.idPlato = dp.idPlato 
WHERE dp.idPedido = idPedido;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `idCategoria` int(11) NOT NULL,
  `nombreCategoria` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallepedido`
--

CREATE TABLE `detallepedido` (
  `idDetallePedido` int(11) NOT NULL,
  `idPedido` int(11) NOT NULL,
  `idPlato` int(11) NOT NULL,
  `cantidadPlatoDetalle` int(3) NOT NULL,
  `totalPagarDetalle` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `detallepedido`
--

INSERT INTO `detallepedido` (`idDetallePedido`, `idPedido`, `idPlato`, `cantidadPlatoDetalle`, `totalPagarDetalle`) VALUES
(12, 39, 7, 1, NULL),
(15, 41, 9, 2, NULL),
(1716, 3443, 7, 2, NULL),
(1717, 3444, 7, 2, NULL),
(1718, 3445, 7, 1, NULL),
(1719, 3445, 9, 1, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_plato_categoria`
--

CREATE TABLE `detalle_plato_categoria` (
  `idDetallePlato_Categoria` int(11) NOT NULL,
  `idPlato` int(11) NOT NULL,
  `idCategoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `local`
--

CREATE TABLE `local` (
  `idLocal` int(11) NOT NULL,
  `nombreLocal` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `local`
--

INSERT INTO `local` (`idLocal`, `nombreLocal`) VALUES
(1, 'Salaverry'),
(2, 'Chacarilla');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido`
--

CREATE TABLE `pedido` (
  `idPedido` int(11) NOT NULL,
  `idPersonal` int(11) DEFAULT NULL,
  `idLocal` int(11) NOT NULL,
  `id_solicitante` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  `creacionPedido` datetime NOT NULL DEFAULT current_timestamp(),
  `cierrePedido` datetime DEFAULT NULL,
  `recojoPedido` datetime DEFAULT NULL,
  `direccionEntrega` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'no es delivery',
  `estadoPedido` int(1) NOT NULL DEFAULT 0,
  `tipoPedido` int(1) NOT NULL,
  `observacionPedido` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nombrePedido` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `referenciaEntrega` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'no es delivery / no se especifico',
  `fecha_recojo` varchar(24) COLLATE utf8_unicode_ci DEFAULT 'no recojo en tienda',
  `hora_recojo` varchar(24) COLLATE utf8_unicode_ci DEFAULT 'no recojo en tienda',
  `telefonoCliente` int(9) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `pedido`
--

INSERT INTO `pedido` (`idPedido`, `idPersonal`, `idLocal`, `id_solicitante`, `creacionPedido`, `cierrePedido`, `recojoPedido`, `direccionEntrega`, `estadoPedido`, `tipoPedido`, `observacionPedido`, `nombrePedido`, `referenciaEntrega`, `fecha_recojo`, `hora_recojo`, `telefonoCliente`) VALUES
(39, NULL, 1, 'ffffffff-fc42-bc69-ffff-ffffef05ac4a', '2021-02-23 13:09:34', NULL, NULL, 'mi casita', 0, 2, NULL, NULL, 'al costado de un mercado chiquito', 'no recojo en tienda', 'no recojo en tienda', NULL),
(41, NULL, 2, 'ffffffff-fde8-90aa-ffff-ffffef05ac4a', '2021-02-23 21:30:10', NULL, NULL, 'no es delivery', 1, 1, 'jmeter test', NULL, 'no es delivery / no se especifico', '25 de Febrero del 2021', '6:29 pm', NULL),
(3443, NULL, 2, 'ffffffff-fde8-90aa-ffff-ffffef05ac4a', '2021-02-28 18:17:30', NULL, NULL, 'no es delivery', 0, 1, NULL, NULL, 'no es delivery / no se especifico', '1 de Marzo del 2021', '6:15 pm', NULL),
(3444, NULL, 1, 'ffffffff-fde8-90aa-ffff-ffffef05ac4a', '2021-02-28 18:21:05', NULL, NULL, 'doreccion', 2, 2, 'comentario', NULL, 'referenci', 'no recojo en tienda', 'no recojo en tienda', NULL),
(3445, NULL, 1, 'ffffffff-fde8-90aa-ffff-ffffef05ac4a', '2021-02-28 18:38:36', NULL, NULL, 'no es delivery', 1, 1, 'aceptado', NULL, 'no es delivery / no se especifico', '28 de Febrero del 2021', '11:38 pm', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personal`
--

CREATE TABLE `personal` (
  `idPersonal` int(11) NOT NULL,
  `nombrePersonal` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `apellidoPersonal` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `usuario` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contrasena` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `telefonoPeronal` int(9) DEFAULT NULL,
  `estadoPersonal` tinyint(1) NOT NULL,
  `puestoPersonal` int(1) NOT NULL,
  `fechaIngreso` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `personal`
--

INSERT INTO `personal` (`idPersonal`, `nombrePersonal`, `apellidoPersonal`, `usuario`, `contrasena`, `telefonoPeronal`, `estadoPersonal`, `puestoPersonal`, `fechaIngreso`) VALUES
(1, 'Juan Carlos', 'Castillo Aycachi', 'testing01', 'testing01', 979666355, 1, 1, '2021-02-22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `platos`
--

CREATE TABLE `platos` (
  `idPlato` int(11) NOT NULL,
  `nombrePlato` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `stockPlato` int(11) DEFAULT NULL,
  `precioPlato` decimal(11,2) NOT NULL,
  `estadoPlato` tinyint(1) NOT NULL,
  `imagenPlato` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `descripcionPlato` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `platos`
--

INSERT INTO `platos` (`idPlato`, `nombrePlato`, `stockPlato`, `precioPlato`, `estadoPlato`, `imagenPlato`, `descripcionPlato`) VALUES
(7, 'mandarina verde', 1, '6.00', 1, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/mandarinaverde5.99.jpg', 'la ultima del dia'),
(8, 'ole ole', 1, '2.50', 0, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/oleole2.35.jpg', 'recien salido del horno'),
(9, 'chin chin', 1, '4.20', 1, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/chinchin3.33.jpg', 'ricos'),
(10, 'caramelo', 1, '2.96', 0, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/caramelo2.96.jpg', 'no me quites mi caramelo'),
(12, 'Test', 1, '11.11', 0, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/Perrito10.2.jpg', 'test jmeter'),
(1715, 'plato 15', 1, '12.25', 0, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/plato12.2.jpg', 'plato'),
(1716, 'gato', 1, '11.11', 0, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/gato11.11.jpg', 'gatito'),
(1717, 'pizarra', 1, '15.15', 1, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/pizarra15.15.jpg', 'pizarra'),
(1718, 'test', 1, '11.00', 1, 'http://192.168.1.7/bd-Alison/ws/imagenes_platos/test11.jpg', 'test 1');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL,
  `nombreUsuario` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `direccionUsuario` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `telefonoUsuario` int(9) NOT NULL,
  `emailUsuario` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`idCategoria`);

--
-- Indices de la tabla `detallepedido`
--
ALTER TABLE `detallepedido`
  ADD PRIMARY KEY (`idDetallePedido`),
  ADD KEY `fk_PlatoDetallePedido` (`idPlato`),
  ADD KEY `fk_PedidoDetallePedido` (`idPedido`);

--
-- Indices de la tabla `detalle_plato_categoria`
--
ALTER TABLE `detalle_plato_categoria`
  ADD PRIMARY KEY (`idDetallePlato_Categoria`),
  ADD KEY `fk_plato` (`idPlato`),
  ADD KEY `fk_categoria` (`idCategoria`);

--
-- Indices de la tabla `local`
--
ALTER TABLE `local`
  ADD PRIMARY KEY (`idLocal`);

--
-- Indices de la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD PRIMARY KEY (`idPedido`),
  ADD KEY `fk_Personal` (`idPersonal`),
  ADD KEY `fk_Local` (`idLocal`);

--
-- Indices de la tabla `personal`
--
ALTER TABLE `personal`
  ADD PRIMARY KEY (`idPersonal`);

--
-- Indices de la tabla `platos`
--
ALTER TABLE `platos`
  ADD PRIMARY KEY (`idPlato`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `idCategoria` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detallepedido`
--
ALTER TABLE `detallepedido`
  MODIFY `idDetallePedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1720;

--
-- AUTO_INCREMENT de la tabla `local`
--
ALTER TABLE `local`
  MODIFY `idLocal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `pedido`
--
ALTER TABLE `pedido`
  MODIFY `idPedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3446;

--
-- AUTO_INCREMENT de la tabla `personal`
--
ALTER TABLE `personal`
  MODIFY `idPersonal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `platos`
--
ALTER TABLE `platos`
  MODIFY `idPlato` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1719;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detallepedido`
--
ALTER TABLE `detallepedido`
  ADD CONSTRAINT `fk_PedidoDetallePedido` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`idPedido`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_PlatoDetallePedido` FOREIGN KEY (`idPlato`) REFERENCES `platos` (`idPlato`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `detalle_plato_categoria`
--
ALTER TABLE `detalle_plato_categoria`
  ADD CONSTRAINT `fk_categoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_plato` FOREIGN KEY (`idPlato`) REFERENCES `platos` (`idPlato`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `pedido`
--
ALTER TABLE `pedido`
  ADD CONSTRAINT `fk_Local` FOREIGN KEY (`idLocal`) REFERENCES `local` (`idLocal`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
