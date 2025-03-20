-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 192.168.31.167    Database: access_control
-- ------------------------------------------------------
-- Server version	5.6.51

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
-- Table structure for table `access_record`
--

DROP TABLE IF EXISTS `access_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `join_time` datetime DEFAULT NULL COMMENT '进入时间',
  `out_time` datetime DEFAULT NULL COMMENT '出去时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `access_record`
--

LOCK TABLES `access_record` WRITE;
/*!40000 ALTER TABLE `access_record` DISABLE KEYS */;
INSERT INTO `access_record` VALUES (11,32,'2024-01-11 20:40:35','2024-01-11 20:41:06'),(12,32,'2024-01-11 21:12:55','2024-01-11 21:27:44');
/*!40000 ALTER TABLE `access_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_info`
--

DROP TABLE IF EXISTS `admin_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_info`
--

LOCK TABLES `admin_info` WRITE;
/*!40000 ALTER TABLE `admin_info` DISABLE KEYS */;
INSERT INTO `admin_info` VALUES (1,'admin','admin');
/*!40000 ALTER TABLE `admin_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学号',
  `sex` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `college` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学院',
  `phone` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `major` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专业',
  `face_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (32,'20240609001','男','张三','信息工程学院','18212001244','软件工程','/img/f9fe15e3-1902-4bc1-afa3-ad5c43849b07.jpg');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitor`
--

DROP TABLE IF EXISTS `visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `sex` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `id_card` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `visitor_time` datetime DEFAULT NULL COMMENT '来访时间',
  `out_time` datetime DEFAULT NULL COMMENT '离开时间',
  `visitor_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '受访人名字',
  `visitor_college` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '受访人学院',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitor`
--

LOCK TABLES `visitor` WRITE;
/*!40000 ALTER TABLE `visitor` DISABLE KEYS */;
INSERT INTO `visitor` VALUES (5,'李四','男','520121199912042815','18212001245','2024-01-11 08:00:00','2024-01-13 04:00:00','王二','信息工程学院');
/*!40000 ALTER TABLE `visitor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'access_control'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-11 21:29:03
