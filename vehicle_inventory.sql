-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: vehicle_inventory
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `title_history`
--

DROP TABLE IF EXISTS `title_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `title_history` (
  `vin` varchar(17) NOT NULL,
  `title_history` longtext,
  PRIMARY KEY (`vin`),
  UNIQUE KEY `VIN_UNIQUE` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `title_history`
--

LOCK TABLES `title_history` WRITE;
/*!40000 ALTER TABLE `title_history` DISABLE KEYS */;
INSERT INTO `title_history` VALUES ('19XFC1F34ME010455','New vehicle'),('1FM5K7F86DGB88960','3 owners, 1 accident'),('1FMJK1KTXMEA15394','1 owner'),('1FT7W2BT5GED26289','1 owner, 1 accident'),('1FTFW1CT7DKE33121','2 owners'),('1HGCV3F90KA008507','Not available'),('2HKRM4H77EH618303','2 owners'),('3FMTK1SS4MMA01775','1 owner'),('5FPYK2F16KB005627','3 owners, 2 accidents');
/*!40000 ALTER TABLE `title_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(500) DEFAULT NULL,
  `admin` int DEFAULT '0',
  `password` varchar(256) DEFAULT NULL,
  `active` int DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'defaultUser',0,'p@$$w0rd1',1),(2,'defaultAdmin',1,'p@$$w0rd2',1),(3,'0',0,'password',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `vin` varchar(17) NOT NULL,
  `price` int DEFAULT NULL,
  `model` varchar(100) DEFAULT NULL,
  `make` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `mileage` int DEFAULT NULL,
  `features` varchar(500) DEFAULT NULL,
  `size` varchar(45) DEFAULT NULL,
  `color` varchar(45) DEFAULT NULL,
  `engine` varchar(45) DEFAULT NULL,
  `fuel_economy` int DEFAULT NULL,
  `fuel_type` varchar(45) DEFAULT NULL,
  `location` int DEFAULT NULL,
  `sold` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`vin`),
  UNIQUE KEY `vin_UNIQUE` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES ('19XFC1F34ME010455',28998,'Civic EX-L','Honda','Japan','Sedan',2021,0,'Front Seat Heaters; Apple Carplay; Lane Departure Warning; Power Seats; Traction Control','Compact','Blue','4-cylinder',37,'Unleaded',77702,NULL),('1FM5K7F86DGB88960',20998,'Explorer Limited','Ford','USA','SUV',2013,106000,'Leather Seats; Satellite Radio; Automated Parking; Power Windows; Tow Hitch','Full Size','Grey','6-cylinder',21,'Unleaded',77701,NULL),('1FMJK1KTXMEA15394',70998,'Expedition Max Limited','Ford','USA','SUV',2021,7000,'Leather Seats; Apple Carplay; Navigation; Power Seats; Traction Control','Full Size','Silver','6-cylinder',20,'Premium',77702,NULL),('1FT7W2BT5GED26289',43998,'F250 XLT','Ford','USA','Truck',2016,120000,'Memory Seats; Android Auto; Navigation; Power Windows; Bed Liner','Full Size','White','8-cylinder',25,'Diesel',77701,NULL),('1FTFW1CT7DKE33121',28998,'F150 Lariat','Ford','USA','Truck',2013,118000,'Sunroof; Satellite Radio; Bluetooth; Power Seats; Tow Hitch','Full Size','Black','6-cylinder',18,'Premium',77704,NULL),('1HGCV3F90KA008507',38998,'Accord Hybrid Touring','Honda','Japan','Sedan',2019,1000,'Leather Seats; Bluetooth; Satellite Radio; Power Seats; ABS Brakes','Mid Size','Red','4-cylinder Hybrid',48,'Unleaded',77704,NULL),('2HKRM4H77EH618303',18998,'CR-V EX-L','Honda','Japan','SUV',2014,111000,'Sunroof; Satellite Radio; Bluetooth; Power Seats; Tow Hitch','Full Size','Silver','4-cylinder',26,'Unleaded',77703,NULL),('3FMTK1SS4MMA01775',55398,'Mustang Mach-E Select','Ford','USA','Coupe',2021,2000,'Memory Seats; Android Auto; Lane Departure Warning; Power Windows; Traction Control','Mid Size','Silver','Electric',93,'Electric',77703,NULL),('5FPYK2F16KB005627',28998,'Ridgeline Sport','Honda','Japan','Truck',2019,105000,'Leather Seats; Auxilliary Input; Bluetooth; Power Windows; Bed Liner','Full Size','White','6-cylinder',23,'Unleaded',77705,NULL);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-27 18:47:11
