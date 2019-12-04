-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Temps de generació: 04-12-2019 a les 11:37:22
-- Versió del servidor: 10.4.8-MariaDB
-- Versió de PHP: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de dades: `futbol_db`
--

DELIMITER $$
--
-- Procediments
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `CrearTablas` ()  NO SQL
BEGIN
  CREATE TABLE IF NOT EXISTS Equipos (ID_Equipo int NOT NULL, Nombre_Equipo varchar(50));
  ALTER TABLE equipos ADD COLUMN calidad int;
  CREATE TABLE IF NOT EXISTS Jugadores(ID_Jugador int NOT NULL, Nombre_Jugador varchar (50), Posicion varchar(50), ID_Equipo int NOT NULL, Nombre_Equipo varchar(50));
  CREATE TABLE IF NOT EXISTS Clasificacion(Posicion int, Nombre_Equipo varchar(50) NOT NULL, Victorias int , Derrotas int , Empates int , Puntos int);
  CREATE TABLE IF NOT EXISTS Partidos(EquipoA varchar(50), GolesA int, EquipoB varchar(50), GolesB int);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertarClasificacion` (IN `Posicion` INT(11), IN `Nombre_Equipo` VARCHAR(50), IN `Victorias` INT(11), IN `Derrotas` INT(11), IN `Empates` INT(11), IN `Puntos` INT(11))  NO SQL
BEGIN
	insert into clasificacion (Posicion,Nombre_Equipo,Victorias,Derrotas,Empates,Puntos) values 	(Posicion,Nombre_Equipo,Victorias,Derrotas,Empates,Puntos);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertarEquipos` (IN `ID_Equipo` INT(11), IN `Nombre_Equipo` VARCHAR(50), IN `Calidad` INT(11))  NO SQL
BEGIN
	insert into equipos (ID_Equipo,Nombre_Equipo,Calidad) values 	(ID_Equipo,Nombre_Equipo,Calidad);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertarJugadores` (IN `ID_Jugador` INT(11), IN `Nombre_Jugador` VARCHAR(50), IN `Posicion` VARCHAR(50), IN `ID_Equipo` INT(11), IN `Nombre_Equipo` VARCHAR(50))  NO SQL
BEGIN
	insert into jugadores (ID_Jugador,Nombre_Jugador,Posicion,ID_Equipo,Nombre_Equipo) values 	(ID_Jugador,Nombre_Jugador,Posicion,ID_Equipo,Nombre_Equipo);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertarPartidos` (IN `EquipoA` VARCHAR(50), IN `GolesA` INT(11), IN `EquipoB` VARCHAR(50), IN `GolesB` INT(11))  NO SQL
BEGIN
	insert into partidos (EquipoA,GolesA,EquipoB,GolesB) values 	(EquipoA,GolesA,EquipoB,GolesB);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de la taula `clasificacion`
--

CREATE TABLE `clasificacion` (
  `Posicion` int(11) DEFAULT NULL,
  `Nombre_Equipo` varchar(50) NOT NULL,
  `Victorias` int(11) DEFAULT NULL,
  `Derrotas` int(11) DEFAULT NULL,
  `Empates` int(11) DEFAULT NULL,
  `Puntos` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Bolcament de dades per a la taula `clasificacion`
--

INSERT INTO `clasificacion` (`Posicion`, `Nombre_Equipo`, `Victorias`, `Derrotas`, `Empates`, `Puntos`) VALUES
(1, 'MovistarInter', 1, 0, 0, 3),
(2, 'Barsa', 1, 0, 0, 3),
(3, 'ElPozoMurcia', 0, 1, 0, 0),
(4, 'PalmaFutsal', 0, 1, 0, 0),
(5, 'OsasunaMagna', 1, 0, 0, 3),
(6, 'Valdepenas', 0, 1, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de la taula `equipos`
--

CREATE TABLE `equipos` (
  `ID_Equipo` int(11) NOT NULL,
  `Nombre_Equipo` varchar(50) DEFAULT NULL,
  `calidad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Bolcament de dades per a la taula `equipos`
--

INSERT INTO `equipos` (`ID_Equipo`, `Nombre_Equipo`, `calidad`) VALUES
(1, 'MovistarInter', 63),
(2, 'Barsa', 57),
(3, 'ElPozoMurcia', 69),
(4, 'PalmaFutsal', 84),
(5, 'OsasunaMagna', 85),
(6, 'Valdepenas', 95);

-- --------------------------------------------------------

--
-- Estructura de la taula `jugadores`
--

CREATE TABLE `jugadores` (
  `ID_Jugador` int(11) NOT NULL,
  `Nombre_Jugador` varchar(50) DEFAULT NULL,
  `Posicion` varchar(50) DEFAULT NULL,
  `ID_Equipo` int(11) NOT NULL,
  `Nombre_Equipo` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Bolcament de dades per a la taula `jugadores`
--

INSERT INTO `jugadores` (`ID_Jugador`, `Nombre_Jugador`, `Posicion`, `ID_Equipo`, `Nombre_Equipo`) VALUES
(100, 'Alejandro Gonzalez Perez', 'Portero', 1, 'MovistarInter'),
(101, 'Marlon Oliveira Araujo', 'Cierre', 1, 'MovistarInter'),
(102, 'Carlos Ortiz Jimenez', 'Cierre', 1, 'MovistarInter'),
(103, 'Ricardo Filipe Da Silva Braga', 'Ala', 1, 'MovistarInter'),
(104, 'Jean Pierre Guisel Costa', 'Pivot', 1, 'MovistarInter'),
(110, 'Alex Lluch Romeu', 'Portero', 2, 'Barsa'),
(111, 'Jesus Nazaret Aicardo Collantes', 'Cierre', 2, 'Barsa'),
(112, 'Alejandro Ceron Albert', 'Cierre', 2, 'Barsa'),
(113, 'Roger Serrano Bernat', 'Ala', 2, 'Barsa'),
(114, 'Carlos Vagner Gularte Filho', 'Pivot', 2, 'Barsa'),
(120, 'Federico Perez Garrigos', 'Portero', 3, 'ElPozoMurcia'),
(121, 'Felipe Valerio Pascual', 'Cierre', 3, 'ElPozoMurcia'),
(122, 'Leonardo Santana Da Silva', 'Cierre', 3, 'ElPozoMurcia'),
(123, 'Antonio Fernando Aguilera Sancho', 'Ala', 3, 'ElPozoMurcia'),
(124, 'Shimizu Kazuya', 'Pivot', 3, 'ElPozoMurcia'),
(130, 'Carlos Barron Redondo', 'Portero', 4, 'PalmaFutsal'),
(131, 'Manuel Urbano Cañete', 'Cierre', 4, 'PalmaFutsal'),
(132, 'Rafael Lopez Expósito', 'Cierre', 4, 'PalmaFutsal'),
(133, 'Diego Nunes De Souza Costa', 'Ala', 4, 'PalmaFutsal'),
(134, 'Diego Quintela Carril', 'Pivot', 4, 'PalmaFutsal'),
(140, 'Asier Llamas Echeverria', 'Portero', 5, 'OsasunaMagna'),
(141, 'Roberto Martil Fernandez', 'Cierre', 5, 'OsasunaMagna'),
(142, 'Oihan Sanchez San Martin', 'Cierre', 5, 'OsasunaMagna'),
(143, 'Alberto Ferraz Barboza', 'Ala', 5, 'OsasunaMagna'),
(144, 'Diego Brandao Martins', 'Pivot', 5, 'OsasunaMagna'),
(150, 'Pedro Coronado Quintana', 'Portero', 6, 'Valdepenas'),
(151, 'Jose Miguel Luis Cortes', 'Cierre', 6, 'Valdepenas'),
(152, 'Rafael França Bezerra', 'Cierre', 6, 'Valdepenas'),
(153, 'Juan Cozar', 'Ala', 6, 'Valdepenas'),
(154, 'Pablo Ibarra Fuertes', 'Pivot', 6, 'Valdepenas');

-- --------------------------------------------------------

--
-- Estructura de la taula `partidos`
--

CREATE TABLE `partidos` (
  `EquipoA` varchar(50) DEFAULT NULL,
  `GolesA` int(11) DEFAULT NULL,
  `EquipoB` varchar(50) DEFAULT NULL,
  `GolesB` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Bolcament de dades per a la taula `partidos`
--

INSERT INTO `partidos` (`EquipoA`, `GolesA`, `EquipoB`, `GolesB`) VALUES
('ElPozoMurcia', 5, 'MovistarInter', 7),
('Valdepenas', 2, 'Barsa', 6),
('OsasunaMagna', 3, 'PalmaFutsal', 2);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
