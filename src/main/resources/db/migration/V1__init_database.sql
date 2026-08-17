-- MySQL dump 10.13  Distrib 9.5.0, for macos15.7 (arm64)
--
-- Host: localhost    Database: pkshop
-- ------------------------------------------------------
-- Server version	9.5.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '1136c6da-c45b-11f0-80ee-896a7612f69d:1-1689';

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `label` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recipient_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `line1` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `line2` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `district` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `province` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `postal_code` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Thailand',
  `is_default` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_addresses_user` (`user_id`),
  KEY `idx_addresses_default` (`user_id`,`is_default`),
  CONSTRAINT `fk_addresses_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_profiles`
--

DROP TABLE IF EXISTS `admin_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_profiles` (
  `user_id` bigint NOT NULL,
  `employee_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `employee_code` (`employee_code`),
  CONSTRAINT `fk_admin_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_profiles`
--

LOCK TABLES `admin_profiles` WRITE;
/*!40000 ALTER TABLE `admin_profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_brands`
--

DROP TABLE IF EXISTS `car_brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_brands`
--

LOCK TABLES `car_brands` WRITE;
/*!40000 ALTER TABLE `car_brands` DISABLE KEYS */;
INSERT INTO `car_brands` VALUES (1,'BMW','2026-02-27 17:59:05'),(2,'Mercedes Benz','2026-02-27 17:59:35'),(3,'Toyota','2026-02-27 17:59:45'),(4,'Honda','2026-02-27 17:59:51'),(5,'Mazada','2026-02-27 17:59:59'),(6,'Nissan','2026-02-27 18:00:05'),(7,'Ford','2026-02-27 18:00:20'),(8,'mitsubishi','2026-02-27 18:00:46'),(9,'MG','2026-02-27 18:01:08'),(10,'Audi','2026-02-27 18:02:00'),(11,'Chevrolet','2026-02-27 18:02:53'),(12,'Porsche','2026-02-27 18:03:26'),(13,'Isuzu','2026-03-03 07:00:41'),(14,'Suzuki','2026-03-03 07:13:38');
/*!40000 ALTER TABLE `car_brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_models`
--

DROP TABLE IF EXISTS `car_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_models` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `brand_id` bigint NOT NULL,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_brand_model` (`brand_id`,`name`),
  KEY `idx_car_models_brand` (`brand_id`),
  CONSTRAINT `fk_car_model_brand` FOREIGN KEY (`brand_id`) REFERENCES `car_brands` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_models`
--

LOCK TABLES `car_models` WRITE;
/*!40000 ALTER TABLE `car_models` DISABLE KEYS */;
INSERT INTO `car_models` VALUES (1,4,'Civic fe','2026-02-27 18:04:21'),(2,4,'Civic fd','2026-02-27 18:05:07'),(3,4,'Civic Type-r','2026-02-27 18:05:23'),(4,4,'Civic rs','2026-02-27 18:05:41'),(5,4,'Civic fk','2026-02-27 18:05:53'),(6,4,'Civic fb','2026-02-27 18:06:19'),(7,3,'Camry','2026-03-02 09:48:51'),(8,3,'Corolla altis 2.0 Turbo','2026-03-03 06:53:07'),(9,5,'Mazada 3','2026-03-03 06:53:30'),(10,6,'Almera','2026-03-03 06:54:09'),(11,3,'Yaris ATIV','2026-03-03 06:55:09'),(12,5,'CX-5','2026-03-03 06:55:24'),(13,4,'City 2.0','2026-03-03 06:55:42'),(14,4,'Accord 3.0 Turbo','2026-03-03 06:56:22'),(15,3,'Vios','2026-03-03 06:56:42'),(16,5,'Mazada 2 Skyactive','2026-03-03 06:56:57'),(17,6,'Teana','2026-03-03 06:57:12'),(18,4,'Jazz','2026-03-03 06:57:36'),(19,3,'Hilux Vigo','2026-03-03 06:58:12'),(20,3,'Hilux Revo','2026-03-03 06:58:27'),(21,3,'Travo','2026-03-03 06:58:42'),(22,6,'March','2026-03-03 06:59:02'),(23,3,'Fortuner','2026-03-03 06:59:38'),(24,7,'everest','2026-03-03 06:59:59'),(25,13,'Mu-x','2026-03-03 07:01:01'),(26,8,'pajero','2026-03-03 07:01:29'),(27,8,'Xpander','2026-03-03 07:02:08'),(28,8,'Xforce','2026-03-03 07:02:31'),(29,7,'Ranger','2026-03-03 07:02:55'),(30,11,'Camaro','2026-03-03 07:05:46'),(31,11,'Colorado','2026-03-03 07:06:12'),(32,11,'captiva','2026-03-03 07:06:29'),(33,1,'X7','2026-03-03 07:07:54'),(34,1,'X3','2026-03-03 07:08:03'),(35,1,'3 Series','2026-03-03 07:09:28'),(36,2,'C-Class','2026-03-03 07:10:04'),(37,2,'E-Class','2026-03-03 07:10:15'),(38,2,'GLC','2026-03-03 07:10:26'),(39,10,'Q5','2026-03-03 07:10:45'),(40,10,'A4 Sedan','2026-03-03 07:11:06'),(41,12,'911','2026-03-03 07:11:27'),(42,12,'Macan','2026-03-03 07:11:40'),(43,12,'Cayenne','2026-03-03 07:11:49'),(44,12,'Panamera','2026-03-03 07:12:00'),(45,9,'MG5','2026-03-03 07:13:02'),(46,9,'MG3','2026-03-03 07:13:16'),(47,14,'Swift','2026-03-03 07:13:52'),(48,5,'CX-30','2026-03-03 07:14:16'),(49,5,'CX-3','2026-03-03 07:14:23'),(50,5,'CX-8','2026-03-03 07:14:31');
/*!40000 ALTER TABLE `car_models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cart_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `qty` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cart_product` (`cart_id`,`product_id`),
  KEY `fk_cart_item_product` (`product_id`),
  CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_item_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (1,1,14,7,'2026-05-25 01:38:29','2026-05-25 02:04:17');
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_user_id` bigint NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_cart_customer_status` (`customer_user_id`,`status`),
  CONSTRAINT `fk_cart_customer` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,13,'ACTIVE','2026-05-25 01:38:29',NULL);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Demo Category'),(6,'ตัวถัง'),(13,'ระบบกรองและของเหลวสิ้นเปลือง'),(2,'ระบบช่วงล่าง'),(12,'ระบบปรับอากาศ'),(8,'ระบบพวงมาลัย'),(9,'ระบบระบายความร้อน'),(7,'ระบบส่งกำลัง'),(4,'ระบบเบรก'),(5,'ระบบไฟฟ้า'),(11,'ระบบไอเสีย'),(14,'ล้อและยาง'),(16,'อุปกรณ์ภายนอกตกแต่ง'),(15,'อุปกรณ์ภายในรถ'),(18,'อุปกรณ์ยึดและอะไหล่จุกจิก'),(17,'อุปกรณ์เซนเซอร์และ ECU'),(3,'เครื่องยนต์'),(10,'เชื้อเพลิง');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_claim_attachments`
--

DROP TABLE IF EXISTS `customer_claim_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_claim_attachments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_claim_id` bigint NOT NULL,
  `file_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_cclaim_attach` (`customer_claim_id`),
  CONSTRAINT `fk_cclaim_attach` FOREIGN KEY (`customer_claim_id`) REFERENCES `customer_claims` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_claim_attachments`
--

LOCK TABLES `customer_claim_attachments` WRITE;
/*!40000 ALTER TABLE `customer_claim_attachments` DISABLE KEYS */;
INSERT INTO `customer_claim_attachments` VALUES (1,1,'http://localhost:8080/upload/claims/0fbbc83f-3d73-4e02-a38f-b98d7db81926.JPG','2026-07-04 04:10:27'),(2,2,'http://localhost:8080/upload/claims/cb3a0121-292d-45e3-b944-f5d4000edf6c.JPG','2026-07-09 00:18:51'),(3,3,'http://localhost:8080/upload/claims/d5a38c26-9881-4122-844e-de5062761693.JPG','2026-07-09 09:50:07'),(4,4,'http://localhost:8080/upload/claims/bcfa27c7-960f-49b4-b8ba-4b4c7349cd20.JPG','2026-07-13 08:01:34'),(5,5,'http://localhost:8080/upload/claims/83386d35-3ca1-4e35-b02b-2ce4d74d891d.JPG','2026-07-13 08:35:44'),(6,6,'http://localhost:8080/upload/claims/cab6276f-a9a6-483e-9e72-900e24fb9484.JPG','2026-07-13 10:17:04'),(7,7,'http://localhost:8080/upload/claims/fa7c3feb-af55-484e-aa19-ffcb3e822a5f.jpg','2026-07-13 11:02:32'),(8,8,'http://localhost:8080/upload/claims/2b702d62-55e2-4e6f-bdd6-815f15691c4a.HEIC','2026-07-13 11:07:08'),(9,9,'http://localhost:8080/upload/claims/35974020-5890-447e-b067-e3d677611945.JPG','2026-07-13 11:09:48'),(10,10,'http://localhost:8080/upload/claims/f68a6d40-84b6-4d82-b058-367a04d65915.jpeg','2026-07-18 00:56:55'),(11,11,'http://localhost:8080/upload/claims/dfacd3b4-6944-4c3d-a7e3-97fe33ed098a.jpeg','2026-07-18 02:14:18'),(12,12,'http://localhost:8080/upload/claims/84ade109-2202-4fc9-9ee1-802df1a322cb.jpeg','2026-07-18 02:52:57'),(13,13,'http://localhost:8080/upload/claims/ee02835f-b224-4168-8ea9-353e58eed682.jpeg','2026-07-18 02:54:05');
/*!40000 ALTER TABLE `customer_claim_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_claims`
--

DROP TABLE IF EXISTS `customer_claims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_claims` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `customer_user_id` bigint NOT NULL,
  `claim_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `admin_comment` text COLLATE utf8mb4_unicode_ci,
  `refund_amount` decimal(12,2) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `product_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `replacement_order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cclaim_customer` (`customer_user_id`),
  KEY `idx_cclaim_status` (`status`),
  KEY `idx_cclaim_order` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `customer_claims_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_cclaim_customer` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_cclaim_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_claims`
--

LOCK TABLES `customer_claims` WRITE;
/*!40000 ALTER TABLE `customer_claims` DISABLE KEYS */;
INSERT INTO `customer_claims` VALUES (1,41,30,1,12,'RETURN_MONEY','PENDING','เสียหาย',NULL,NULL,'2026-07-04 04:10:27',NULL,NULL,NULL,NULL),(2,41,28,1,12,'RETURN_MONEY','APPROVED','เทส',NULL,NULL,'2026-07-09 00:18:51','2026-07-09 02:37:07','http://localhost:8080/upload/claims/cb3a0121-292d-45e3-b944-f5d4000edf6c.JPG','โช้คอัพหลัง Audi Q5',NULL),(3,20,29,1,12,'RETURN_MONEY','PENDING','ได้รับการกระแทก',NULL,NULL,'2026-07-09 09:50:07',NULL,'http://localhost:8080/upload/claims/d5a38c26-9881-4122-844e-de5062761693.JPG','ปั๊มน้ำ Porsche Cayenne',NULL),(4,55,24,1,12,'RETURN_MONEY','PENDING','พังยับเลยครับ',NULL,NULL,'2026-07-13 08:01:34',NULL,'http://localhost:8080/upload/claims/bcfa27c7-960f-49b4-b8ba-4b4c7349cd20.JPG','ไดสตาร์ท Toyota Hilux Vigo',NULL),(5,56,23,1,12,'RETURN_MONEY','APPROVED','fuck you shit!!!',NULL,8500.00,'2026-07-13 08:35:44','2026-07-13 08:36:39','http://localhost:8080/upload/claims/83386d35-3ca1-4e35-b02b-2ce4d74d891d.JPG','ไดชาร์จ Honda Jazz',NULL),(6,57,21,1,12,'RETURN_MONEY','APPROVED','มีการบิดงอครับ',NULL,2840.00,'2026-07-13 10:17:04','2026-07-13 10:23:55','http://localhost:8080/upload/claims/cab6276f-a9a6-483e-9e72-900e24fb9484.JPG','คันชักคันส่ง Nissan Teana',NULL),(7,58,24,1,12,'RETURN_MONEY','APPROVED','มี1ชิ้นได้รับความเสียหายครับ',NULL,3500.00,'2026-07-13 11:02:32','2026-07-13 11:03:31','http://localhost:8080/upload/claims/fa7c3feb-af55-484e-aa19-ffcb3e822a5f.jpg','ไดสตาร์ท Toyota Hilux Vigo',NULL),(8,59,25,3,12,'RETURN_MONEY','APPROVED','ผมไม่ชอบ',NULL,6000.00,'2026-07-13 11:07:08','2026-07-13 11:08:05','http://localhost:8080/upload/claims/2b702d62-55e2-4e6f-bdd6-815f15691c4a.HEIC','คอยล์จุดระเบิด Mazda 3',NULL),(9,55,24,1,12,'RETURN_MONEY','REJECTED','ขอเปลี่ยนเฉยๆครับ',NULL,NULL,'2026-07-13 11:09:48','2026-07-13 11:10:07','http://localhost:8080/upload/claims/35974020-5890-447e-b067-e3d677611945.JPG','ไดสตาร์ท Toyota Hilux Vigo',NULL),(10,60,27,1,12,'EXCHANGE','APPROVED','สินค้ามีรอยและชำรุดครับ',NULL,NULL,'2026-07-18 00:56:55','2026-07-18 00:58:59','http://localhost:8080/upload/claims/f68a6d40-84b6-4d82-b058-367a04d65915.jpeg','ไส้กรองน้ำมันเครื่อง Benz',NULL),(11,61,26,2,12,'EXCHANGE','APPROVED','สินค้าได้รับความเหสียหาย',NULL,NULL,'2026-07-18 02:14:18','2026-07-18 02:15:02','http://localhost:8080/upload/claims/dfacd3b4-6944-4c3d-a7e3-97fe33ed098a.jpeg','ผ้าเบรกหน้า BMW 3 Series',63),(12,64,25,2,12,'EXCHANGE','APPROVED','เสียหายได้รับการกระแทก',NULL,NULL,'2026-07-18 02:52:57','2026-07-18 02:54:48','http://localhost:8080/upload/claims/84ade109-2202-4fc9-9ee1-802df1a322cb.jpeg','คอยล์จุดระเบิด Mazda 3',65),(13,62,27,1,12,'RETURN_MONEY','APPROVED','สินค้าได้รับความเสียหายส่งช้าด้วย',NULL,260.00,'2026-07-18 02:54:05','2026-07-18 02:56:52','http://localhost:8080/upload/claims/ee02835f-b224-4168-8ea9-353e58eed682.jpeg','ไส้กรองน้ำมันเครื่อง Benz',NULL);
/*!40000 ALTER TABLE `customer_claims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_profiles`
--

DROP TABLE IF EXISTS `customer_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_profiles` (
  `user_id` bigint NOT NULL,
  `default_address_id` bigint DEFAULT NULL,
  `points` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_customer_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_profiles`
--

LOCK TABLES `customer_profiles` WRITE;
/*!40000 ALTER TABLE `customer_profiles` DISABLE KEYS */;
INSERT INTO `customer_profiles` VALUES (12,NULL,0,'2026-05-15 17:21:48'),(13,NULL,0,'2026-05-16 08:10:20'),(14,NULL,0,'2026-06-10 00:16:18'),(15,NULL,0,'2026-08-08 01:48:59'),(16,NULL,0,'2026-08-08 03:29:42'),(17,NULL,0,'2026-08-11 10:17:09');
/*!40000 ALTER TABLE `customer_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customs_profiles`
--

DROP TABLE IF EXISTS `customs_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customs_profiles` (
  `user_id` bigint NOT NULL,
  `department_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `officer_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `officer_code` (`officer_code`),
  CONSTRAINT `fk_customs_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customs_profiles`
--

LOCK TABLES `customs_profiles` WRITE;
/*!40000 ALTER TABLE `customs_profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `customs_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customs_reviews`
--

DROP TABLE IF EXISTS `customs_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customs_reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `import_document_id` bigint NOT NULL,
  `customs_user_id` bigint NOT NULL,
  `action` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment` text COLLATE utf8mb4_unicode_ci,
  `reviewed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_review_doc` (`import_document_id`),
  KEY `idx_review_customs` (`customs_user_id`),
  CONSTRAINT `fk_review_customs` FOREIGN KEY (`customs_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_review_doc` FOREIGN KEY (`import_document_id`) REFERENCES `import_documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customs_reviews`
--

LOCK TABLES `customs_reviews` WRITE;
/*!40000 ALTER TABLE `customs_reviews` DISABLE KEYS */;
INSERT INTO `customs_reviews` VALUES (1,1,7,'APPROVE','Approved','2026-02-24 11:24:42'),(2,2,7,'REJECT','Rejected','2026-03-01 10:34:09'),(3,3,7,'APPROVE','สินค้าผ่านหมด','2026-03-02 10:04:17'),(4,4,7,'APPROVE','Approved','2026-03-03 01:33:47'),(5,5,7,'APPROVE','Approved','2026-05-01 09:54:13'),(6,6,7,'APPROVE','Approved','2026-05-17 20:00:47'),(7,8,7,'APPROVE','สินค้าผ่านทั้งหมดไม่มีปัญหา','2026-06-07 01:58:44'),(8,9,7,'APPROVE','ผ่าน','2026-06-07 01:58:55'),(9,7,7,'REJECT','สินค้าไม่ผ่าน QA ไม่ได้มาตรฐานในการนำเข้า','2026-06-07 02:03:57'),(10,10,7,'APPROVE','Approved','2026-06-10 03:16:56'),(11,11,7,'APPROVE','Approved','2026-06-10 03:17:12'),(12,12,7,'APPROVE','Approved','2026-06-10 03:17:27'),(13,13,7,'APPROVE','Approved','2026-06-10 03:17:33'),(14,15,7,'APPROVE','Approved','2026-08-13 02:13:21');
/*!40000 ALTER TABLE `customs_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_document_files`
--

DROP TABLE IF EXISTS `import_document_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_document_files` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `import_document_id` bigint NOT NULL,
  `file_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_doc_file_doc` (`import_document_id`),
  CONSTRAINT `fk_doc_file_doc` FOREIGN KEY (`import_document_id`) REFERENCES `import_documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_document_files`
--

LOCK TABLES `import_document_files` WRITE;
/*!40000 ALTER TABLE `import_document_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `import_document_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_documents`
--

DROP TABLE IF EXISTS `import_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_documents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `import_lot_id` bigint NOT NULL,
  `doc_number` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `doc_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'E_IMPORT',
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SENT',
  `submitted_by_admin_id` bigint NOT NULL,
  `submitted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `doc_number` (`doc_number`),
  KEY `fk_doc_admin` (`submitted_by_admin_id`),
  KEY `idx_doc_status` (`status`),
  KEY `idx_doc_lot` (`import_lot_id`),
  CONSTRAINT `fk_doc_admin` FOREIGN KEY (`submitted_by_admin_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_doc_lot` FOREIGN KEY (`import_lot_id`) REFERENCES `import_lots` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_documents`
--

LOCK TABLES `import_documents` WRITE;
/*!40000 ALTER TABLE `import_documents` DISABLE KEYS */;
INSERT INTO `import_documents` VALUES (1,1,'DOC-1771954169082','E_IMPORT','APPROVED',5,'2026-02-24 10:29:29','2026-02-24 18:24:42',NULL),(2,2,'DOC-1771954295294','E_IMPORT','REJECTED',5,'2026-02-24 10:31:35','2026-03-01 17:34:08',NULL),(3,3,'DOC-1772470896322','E_IMPORT','APPROVED',5,'2026-03-02 10:01:36','2026-03-02 17:04:16',NULL),(4,4,'DOC-1772526716427','E_IMPORT','APPROVED',5,'2026-03-03 01:31:56','2026-03-03 08:33:46',NULL),(5,5,'DOC-1773674975190','E_IMPORT','APPROVED',5,'2026-03-16 08:29:35','2026-05-01 16:54:12',NULL),(6,6,'DOC-1779073100556','E_IMPORT','APPROVED',5,'2026-05-17 19:58:21','2026-05-18 03:00:47',NULL),(7,7,'DOC-1779073580648','E_IMPORT','REJECTED',5,'2026-05-17 20:06:21','2026-06-07 09:03:56',NULL),(8,8,'DOC-1780822456031','E_IMPORT','APPROVED',5,'2026-06-07 01:54:16','2026-06-07 08:58:43',NULL),(9,9,'DOC-1780822614566','E_IMPORT','APPROVED',5,'2026-06-07 01:56:55','2026-06-07 08:58:55',NULL),(10,10,'DOC-1781085802487','E_IMPORT','APPROVED',5,'2026-06-10 03:03:22','2026-06-10 10:16:55',NULL),(11,11,'DOC-1781085976235','E_IMPORT','APPROVED',5,'2026-06-10 03:06:16','2026-06-10 10:17:11',NULL),(12,12,'DOC-1781086139557','E_IMPORT','APPROVED',5,'2026-06-10 03:09:00','2026-06-10 10:17:27',NULL),(13,13,'DOC-1781086245301','E_IMPORT','APPROVED',5,'2026-06-10 03:10:45','2026-06-10 10:17:32',NULL),(14,14,'DOC-1786092661569','E_IMPORT','UNDER_REVIEW',5,'2026-08-07 01:51:02','2026-08-07 08:51:01',NULL),(15,15,'DOC-1786612360299','E_IMPORT','APPROVED',5,'2026-08-13 02:12:40','2026-08-13 09:13:21',NULL);
/*!40000 ALTER TABLE `import_documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_lot_items`
--

DROP TABLE IF EXISTS `import_lot_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_lot_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `import_lot_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `qty` int NOT NULL,
  `unit_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `line_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_lot_product` (`import_lot_id`,`product_id`),
  KEY `fk_loti_product` (`product_id`),
  CONSTRAINT `fk_loti_lot` FOREIGN KEY (`import_lot_id`) REFERENCES `import_lots` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_loti_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_lot_items`
--

LOCK TABLES `import_lot_items` WRITE;
/*!40000 ALTER TABLE `import_lot_items` DISABLE KEYS */;
INSERT INTO `import_lot_items` VALUES (1,1,2,100,95.00,9500.00,'2026-02-24 10:29:29'),(2,2,2,100,95.00,9500.00,'2026-02-24 10:31:35'),(3,3,11,30,950.00,28500.00,'2026-03-02 10:01:36'),(4,4,10,30,3000.00,90000.00,'2026-03-03 01:31:56'),(5,4,14,15,2700.00,40500.00,'2026-03-03 01:31:56'),(6,4,15,16,820.00,13120.00,'2026-03-03 01:31:56'),(7,4,18,15,520.00,7800.00,'2026-03-03 01:31:56'),(8,4,21,20,1100.00,22000.00,'2026-03-03 01:31:56'),(9,4,23,25,190.00,4750.00,'2026-03-03 01:31:56'),(10,5,11,30,950.00,28500.00,'2026-03-16 08:29:35'),(11,6,24,20,1000.00,20000.00,'2026-05-17 19:58:20'),(12,6,25,5,600.00,3000.00,'2026-05-17 19:58:20'),(13,6,26,3,1600.00,4800.00,'2026-05-17 19:58:21'),(14,6,27,10,60.00,600.00,'2026-05-17 19:58:21'),(15,6,28,3,4200.00,12600.00,'2026-05-17 19:58:21'),(16,6,29,4,6500.00,26000.00,'2026-05-17 19:58:21'),(17,6,30,7,500.01,3500.07,'2026-05-17 19:58:21'),(18,7,22,10,600.00,6000.00,'2026-05-17 20:06:21'),(19,8,16,10,700.00,7000.00,'2026-06-07 01:54:16'),(20,8,17,10,2500.00,25000.00,'2026-06-07 01:54:16'),(21,8,19,10,70.00,700.00,'2026-06-07 01:54:16'),(22,8,20,10,250.00,2500.00,'2026-06-07 01:54:16'),(23,8,22,10,500.00,5000.00,'2026-06-07 01:54:16'),(24,9,12,10,650.00,6500.00,'2026-06-07 01:56:55'),(25,9,13,10,50.00,500.00,'2026-06-07 01:56:55'),(26,10,25,13,400.00,5200.00,'2026-06-10 03:03:22'),(27,11,19,10,30.00,300.00,'2026-06-10 03:06:16'),(28,11,20,5,100.00,500.00,'2026-06-10 03:06:16'),(29,11,21,8,450.00,3600.00,'2026-06-10 03:06:16'),(30,11,22,10,450.00,4500.00,'2026-06-10 03:06:16'),(31,11,23,5,1000.00,5000.00,'2026-06-10 03:06:16'),(32,11,24,7,900.00,6300.00,'2026-06-10 03:06:16'),(33,12,26,5,1200.00,6000.00,'2026-06-10 03:09:00'),(34,12,27,11,20.00,220.00,'2026-06-10 03:09:00'),(35,12,28,3,3800.00,11400.00,'2026-06-10 03:09:00'),(36,12,29,3,6000.00,18000.00,'2026-06-10 03:09:00'),(37,12,30,8,300.00,2400.00,'2026-06-10 03:09:00'),(38,13,12,5,550.00,2750.00,'2026-06-10 03:10:45'),(39,13,13,5,20.00,100.00,'2026-06-10 03:10:45'),(40,13,15,7,400.00,2800.00,'2026-06-10 03:10:45'),(41,13,17,2,1900.00,3800.00,'2026-06-10 03:10:45'),(42,14,26,4,1300.00,5200.00,'2026-08-07 01:51:02'),(43,15,26,7,1700.00,11900.00,'2026-08-13 02:12:40');
/*!40000 ALTER TABLE `import_lot_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_lots`
--

DROP TABLE IF EXISTS `import_lots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_lots` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lot_number` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `purchase_order_id` bigint NOT NULL,
  `supplier_quotation_id` bigint DEFAULT NULL,
  `admin_user_id` bigint NOT NULL,
  `origin_country` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `incoterm` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping_method` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `freight_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `insurance_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `customs_duty_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `other_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `total_import_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT',
  `arrival_date` date DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lot_number` (`lot_number`),
  KEY `fk_lot_quote` (`supplier_quotation_id`),
  KEY `fk_lot_admin` (`admin_user_id`),
  KEY `idx_lot_status` (`status`),
  KEY `idx_lot_po` (`purchase_order_id`),
  CONSTRAINT `fk_lot_admin` FOREIGN KEY (`admin_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_lot_po` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_lot_quote` FOREIGN KEY (`supplier_quotation_id`) REFERENCES `supplier_quotations` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_lots`
--

LOCK TABLES `import_lots` WRITE;
/*!40000 ALTER TABLE `import_lots` DISABLE KEYS */;
INSERT INTO `import_lots` VALUES (1,'LOT-1771954169026',1,1,5,'Japan',NULL,'SEA',2000.00,0.00,0.00,1000.00,3000.00,'RECEIVED','2026-02-27','2026-02-24 10:29:29','2026-02-27 16:16:22'),(2,'LOT-1771954295259',1,1,5,'Japan',NULL,'SEA',2000.00,1000.00,3000.00,1000.00,7000.00,'CUSTOMS_REJECTED',NULL,'2026-02-24 10:31:35','2026-03-01 17:34:08'),(3,'LOT-1772470896269',16,4,5,'Thailand',NULL,'LAND',2000.00,2000.00,0.00,0.00,4000.00,'RECEIVED','2026-03-03','2026-03-02 10:01:36','2026-03-02 17:05:30'),(4,'LOT-1772526716336',18,5,5,'Thailand',NULL,'AIR',5000.00,2000.00,18517.00,14258.00,39775.00,'RECEIVED','2026-03-03','2026-03-03 01:31:56','2026-03-03 08:35:31'),(5,'LOT-1773674975156',16,4,5,'Thailand',NULL,'AIR',5000.00,2000.00,18000.00,0.00,25000.00,'RECEIVED','2026-05-01','2026-03-16 08:29:35','2026-05-01 16:55:01'),(6,'LOT-1779073100452',20,7,5,'Thailand',NULL,'AIR',4000.00,1000.00,3000.00,0.00,8000.00,'RECEIVED','2026-05-18','2026-05-17 19:58:20','2026-05-18 03:01:17'),(7,'LOT-1779073580605',21,8,5,'Thailand',NULL,'SEA',1500.00,500.00,1200.00,0.00,3200.00,'CUSTOMS_REJECTED',NULL,'2026-05-17 20:06:21','2026-06-07 09:03:56'),(8,'LOT-1780822455947',22,9,5,'Thailand',NULL,'AIR',5000.00,3500.00,3409.00,1500.00,13409.00,'RECEIVED','2026-06-07','2026-06-07 01:54:16','2026-06-07 09:04:57'),(9,'LOT-1780822614514',23,10,5,'Thailand',NULL,'AIR',2000.00,1500.00,2737.00,1000.00,7237.00,'RECEIVED','2026-06-07','2026-06-07 01:56:55','2026-06-07 09:05:20'),(10,'LOT-1781085802437',24,13,5,'Thailand',NULL,'LAND',500.00,500.00,2019.00,0.00,3019.00,'RECEIVED','2026-06-10','2026-06-10 03:03:22','2026-06-10 10:23:30'),(11,'LOT-1781085976135',25,14,5,'Thailand',NULL,'AIR',5000.00,3500.00,11221.00,0.00,19721.00,'RECEIVED','2026-06-10','2026-06-10 03:06:16','2026-06-10 10:23:22'),(12,'LOT-1781086139464',26,11,5,'Thailand',NULL,'AIR',7000.00,10000.00,22099.00,1500.00,40599.00,'RECEIVED','2026-06-10','2026-06-10 03:08:59','2026-06-10 10:23:14'),(13,'LOT-1781086245178',27,12,5,'Thailand',NULL,'SEA',2000.00,1000.00,4867.00,0.00,7867.00,'RECEIVED','2026-06-10','2026-06-10 03:10:45','2026-06-10 10:22:59'),(14,'LOT-1786092661513',28,15,5,'Thailand',NULL,'AIR',3000.00,1500.00,3800.00,0.00,8300.00,'CUSTOMS_PENDING',NULL,'2026-08-07 01:51:02','2026-08-07 08:51:01'),(15,'LOT-1786612360229',29,16,5,'Thailand',NULL,'AIR',3000.00,1500.00,6412.00,0.00,10912.00,'RECEIVED','2026-08-13','2026-08-13 02:12:40','2026-08-13 09:15:57');
/*!40000 ALTER TABLE `import_lots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transactions`
--

DROP TABLE IF EXISTS `inventory_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `txn_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_table` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_id` bigint DEFAULT NULL,
  `qty_change` int NOT NULL,
  `unit_cost` decimal(12,2) DEFAULT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_invtxn_user` (`created_by`),
  KEY `idx_invtxn_product_time` (`product_id`,`created_at`),
  KEY `idx_invtxn_ref` (`ref_table`,`ref_id`),
  CONSTRAINT `fk_invtxn_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_invtxn_user` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transactions`
--

LOCK TABLES `inventory_transactions` WRITE;
/*!40000 ALTER TABLE `inventory_transactions` DISABLE KEYS */;
INSERT INTO `inventory_transactions` VALUES (1,2,'IMPORT_IN','import_lots',1,100,95.00,'Receive import lot',5,'2026-02-27 09:16:23'),(2,11,'IMPORT_IN','import_lots',3,30,950.00,'Receive import lot',5,'2026-03-02 10:05:30'),(3,10,'IMPORT_IN','import_lots',4,30,3000.00,'Receive import lot',5,'2026-03-03 01:35:32'),(4,14,'IMPORT_IN','import_lots',4,15,2700.00,'Receive import lot',5,'2026-03-03 01:35:32'),(5,15,'IMPORT_IN','import_lots',4,16,820.00,'Receive import lot',5,'2026-03-03 01:35:32'),(6,18,'IMPORT_IN','import_lots',4,15,520.00,'Receive import lot',5,'2026-03-03 01:35:32'),(7,21,'IMPORT_IN','import_lots',4,20,1100.00,'Receive import lot',5,'2026-03-03 01:35:32'),(8,23,'IMPORT_IN','import_lots',4,25,190.00,'Receive import lot',5,'2026-03-03 01:35:32'),(9,11,'IMPORT_IN','import_lots',5,30,950.00,'Receive import lot',5,'2026-05-01 09:55:01'),(10,24,'IMPORT_IN','import_lots',6,20,1000.00,'Receive import lot',5,'2026-05-17 20:01:17'),(11,25,'IMPORT_IN','import_lots',6,5,600.00,'Receive import lot',5,'2026-05-17 20:01:17'),(12,26,'IMPORT_IN','import_lots',6,3,1600.00,'Receive import lot',5,'2026-05-17 20:01:17'),(13,27,'IMPORT_IN','import_lots',6,10,60.00,'Receive import lot',5,'2026-05-17 20:01:17'),(14,28,'IMPORT_IN','import_lots',6,3,4200.00,'Receive import lot',5,'2026-05-17 20:01:17'),(15,29,'IMPORT_IN','import_lots',6,4,6500.00,'Receive import lot',5,'2026-05-17 20:01:17'),(16,30,'IMPORT_IN','import_lots',6,7,500.01,'Receive import lot',5,'2026-05-17 20:01:17'),(17,16,'IMPORT_IN','import_lots',8,10,700.00,'Receive import lot',5,'2026-06-07 02:04:57'),(18,17,'IMPORT_IN','import_lots',8,10,2500.00,'Receive import lot',5,'2026-06-07 02:04:57'),(19,19,'IMPORT_IN','import_lots',8,10,70.00,'Receive import lot',5,'2026-06-07 02:04:57'),(20,20,'IMPORT_IN','import_lots',8,10,250.00,'Receive import lot',5,'2026-06-07 02:04:57'),(21,22,'IMPORT_IN','import_lots',8,10,500.00,'Receive import lot',5,'2026-06-07 02:04:57'),(22,12,'IMPORT_IN','import_lots',9,10,650.00,'Receive import lot',5,'2026-06-07 02:05:20'),(23,13,'IMPORT_IN','import_lots',9,10,50.00,'Receive import lot',5,'2026-06-07 02:05:20'),(24,12,'IMPORT_IN','import_lots',13,5,550.00,'Receive import lot',5,'2026-06-10 03:23:00'),(25,13,'IMPORT_IN','import_lots',13,5,20.00,'Receive import lot',5,'2026-06-10 03:23:00'),(26,15,'IMPORT_IN','import_lots',13,7,400.00,'Receive import lot',5,'2026-06-10 03:23:00'),(27,17,'IMPORT_IN','import_lots',13,2,1900.00,'Receive import lot',5,'2026-06-10 03:23:00'),(28,26,'IMPORT_IN','import_lots',12,5,1200.00,'Receive import lot',5,'2026-06-10 03:23:15'),(29,27,'IMPORT_IN','import_lots',12,11,20.00,'Receive import lot',5,'2026-06-10 03:23:15'),(30,28,'IMPORT_IN','import_lots',12,3,3800.00,'Receive import lot',5,'2026-06-10 03:23:15'),(31,29,'IMPORT_IN','import_lots',12,3,6000.00,'Receive import lot',5,'2026-06-10 03:23:15'),(32,30,'IMPORT_IN','import_lots',12,8,300.00,'Receive import lot',5,'2026-06-10 03:23:15'),(33,19,'IMPORT_IN','import_lots',11,10,30.00,'Receive import lot',5,'2026-06-10 03:23:23'),(34,20,'IMPORT_IN','import_lots',11,5,100.00,'Receive import lot',5,'2026-06-10 03:23:23'),(35,21,'IMPORT_IN','import_lots',11,8,450.00,'Receive import lot',5,'2026-06-10 03:23:23'),(36,22,'IMPORT_IN','import_lots',11,10,450.00,'Receive import lot',5,'2026-06-10 03:23:23'),(37,23,'IMPORT_IN','import_lots',11,5,1000.00,'Receive import lot',5,'2026-06-10 03:23:23'),(38,24,'IMPORT_IN','import_lots',11,7,900.00,'Receive import lot',5,'2026-06-10 03:23:23'),(39,25,'IMPORT_IN','import_lots',10,13,400.00,'Receive import lot',5,'2026-06-10 03:23:31'),(40,26,'IMPORT_IN','import_lots',15,7,1700.00,'Receive import lot',5,'2026-08-13 02:15:58'),(41,26,'SUPPLIER_CLAIM_OUT','supplier_claims',1,-2,1700.00,'ส่งคืนสินค้าให้ supplier (เคลม # 1)',5,'2026-08-13 02:19:09'),(42,26,'SUPPLIER_CLAIM_OUT','supplier_claims',2,-1,1700.00,'ส่งคืนสินค้าให้ supplier (เคลม # 2)',5,'2026-08-13 03:14:17'),(43,26,'SUPPLIER_CLAIM_OUT','supplier_claims',3,-1,1700.00,'ส่งคืนสินค้าให้ supplier (เคลม # 3)',5,'2026-08-13 10:16:46'),(44,26,'SUPPLIER_CLAIM_REPLACE_IN','supplier_claims',3,1,1700.00,'รับสินค้าเปลี่ยนจาก supplier (เคลม # 3)',5,'2026-08-13 10:18:43');
/*!40000 ALTER TABLE `inventory_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `product_name_snapshot` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `qty` int NOT NULL,
  `line_total` decimal(12,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `product_image_snapshot` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order_items_order` (`order_id`),
  KEY `idx_order_items_product` (`product_id`),
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (13,6,2,'Demo Product 1',100.00,6,600.00,NULL,NULL),(14,6,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,3,4500.00,NULL,NULL),(15,6,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,6,3540.00,NULL,NULL),(16,6,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,3,7500.00,NULL,NULL),(17,7,2,'Demo Product 1',100.00,6,600.00,NULL,NULL),(18,7,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,3,4500.00,NULL,NULL),(19,7,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,6,3540.00,NULL,NULL),(20,7,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,3,7500.00,NULL,NULL),(21,8,2,'Demo Product 1',100.00,6,600.00,NULL,NULL),(22,8,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,3,4500.00,NULL,NULL),(23,8,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,6,3540.00,NULL,NULL),(24,8,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,3,7500.00,NULL,NULL),(25,9,2,'Demo Product 1',100.00,6,600.00,NULL,NULL),(26,9,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,3,4500.00,NULL,NULL),(27,9,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,6,3540.00,NULL,NULL),(28,9,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,3,7500.00,NULL,NULL),(29,10,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,4,6000.00,NULL,NULL),(30,11,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,4,6000.00,NULL,NULL),(31,12,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,4,6000.00,NULL,NULL),(32,13,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,4,10000.00,NULL,NULL),(33,13,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,3,1770.00,NULL,NULL),(34,14,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,3,4500.00,NULL,NULL),(35,14,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,3,7500.00,NULL,NULL),(36,15,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,3,4500.00,NULL,NULL),(37,16,21,'คันชักคันส่ง Nissan Teana',2840.00,1,2840.00,NULL,NULL),(38,17,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,2,3000.00,NULL,NULL),(39,18,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,4,10000.00,NULL,NULL),(40,19,15,'ผ้าเบรกหน้า Honda City',1650.00,3,4950.00,NULL,NULL),(41,19,18,'โช้คอัพหน้า Honda Accord',4400.00,2,8800.00,NULL,NULL),(42,20,29,'ปั๊มน้ำ Porsche Cayenne',16400.00,1,16400.00,NULL,NULL),(45,22,21,'คันชักคันส่ง Nissan Teana',2840.00,4,11360.00,NULL,NULL),(46,22,30,'คอยล์จุดระเบิด BMW X3',1450.00,3,4350.00,NULL,NULL),(47,23,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,1,590.00,NULL,NULL),(48,24,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,1,590.00,NULL,NULL),(55,31,18,'โช้คอัพหน้า Honda Accord',4400.00,1,4400.00,NULL,'https://inwfile.com/s-cf/1ah0po.jpg'),(57,33,21,'คันชักคันส่ง Nissan Teana',2840.00,2,5680.00,NULL,'https://inwfile.com/s-gi/vlycu2.png'),(58,33,14,'สายพานหน้าเครื่อง Nissan Almera',590.00,1,590.00,NULL,'https://inwfile.com/s-cf/piuvsm.png'),(59,33,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,2,5000.00,NULL,'https://inwfile.com/s-fj/geeakk.jpg'),(60,34,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,2,7000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(61,34,18,'โช้คอัพหน้า Honda Accord',4400.00,1,4400.00,NULL,'https://inwfile.com/s-cf/1ah0po.jpg'),(62,34,15,'ผ้าเบรกหน้า Honda City',1650.00,5,8250.00,NULL,'https://inwfile.com/s-cf/kwku2a.png'),(63,34,29,'ปั๊มน้ำ Porsche Cayenne',16400.00,1,16400.00,NULL,'https://inwfile.com/s-ce/fogana.jpg'),(64,35,28,'โช้คอัพหลัง Audi Q5',10800.00,1,10800.00,NULL,'https://s.alicdn.com/@sc04/kf/Ha4e7281c1fdb48439552c84afb1841afg.png'),(65,35,23,'ไดชาร์จ Honda Jazz',8500.00,1,8500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg'),(66,36,28,'โช้คอัพหลัง Audi Q5',10800.00,1,10800.00,NULL,'https://s.alicdn.com/@sc04/kf/Ha4e7281c1fdb48439552c84afb1841afg.png'),(67,37,30,'คอยล์จุดระเบิด BMW X3',1450.00,4,5800.00,NULL,'https://image.made-in-china.com/202f0j00WnmqziVrrCcI/High-Quality-Ignition-Coil-for-BMW-1-3-5-6-7-Series-X1-X3-X5-M3-E46-2008-207-208-3008-308-508-Rcz-1-6-0221504100.webp'),(68,37,27,'ไส้กรองน้ำมันเครื่อง Benz',260.00,6,1560.00,NULL,'https://cache-igetweb-v2.mt108.info/uploads/images-cache/1007/product/b6c90600330738be10c04d3529cca747_full.jpg'),(69,37,26,'ผ้าเบรกหน้า BMW 3 Series',3450.00,2,6900.00,NULL,'https://inwfile.com/s-cf/ytov0e.jpg'),(70,37,25,'คอยล์จุดระเบิด Mazda 3',2000.00,3,6000.00,NULL,'https://inwfile.com/s-fj/6mp7uv.jpg'),(71,37,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,10,35000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(72,37,23,'ไดชาร์จ Honda Jazz',8500.00,15,127500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg'),(73,37,22,'แบตเตอรี่ 12V 45Ah',1680.00,7,11760.00,NULL,'https://inwfile.com/s-dk/fftzad.jpg'),(74,37,20,'บูชปีกนกล่าง Mazda 2',1000.00,7,7000.00,NULL,'https://down-th.img.susercontent.com/file/de44b45bd6d57e640e271a224378a36d'),(75,37,19,'ลูกหมากปีกนก Toyota Vios',400.00,5,2000.00,NULL,'https://inwfile.com/s-cf/hucvg9.png'),(76,37,17,'จานเบรกหน้า Mazda CX-5',6900.00,6,41400.00,NULL,'https://inwfile.com/s-fz/f8ku2t.jpg'),(77,37,16,'ผ้าเบรกหลัง Toyota Yaris',1900.00,6,11400.00,NULL,'https://inwfile.com/s-fa/bhx8id.jpg'),(78,37,15,'ผ้าเบรกหน้า Honda City',1650.00,4,6600.00,NULL,'https://inwfile.com/s-cf/kwku2a.png'),(79,37,13,'กรองน้ำมันเครื่อง Toyota Corolla',400.00,5,2000.00,NULL,'https://inwfile.com/s-cf/2ofjie.jpg'),(80,37,12,'หัวเทียน Mazda 3',2000.00,8,16000.00,NULL,'https://inwfile.com/s-fj/pomux0.jpg'),(81,37,11,'ผ้าเบรกหน้า Toyota Camry',2500.00,20,50000.00,NULL,'https://inwfile.com/s-fj/geeakk.jpg'),(82,37,10,'กรองอากาศเครื่องยนต์ Honda Civic',1500.00,10,15000.00,NULL,'https://inwfile.com/s-gf/stalxc.jpg'),(83,38,2,'Demo Product 1',100.00,1,100.00,NULL,NULL),(84,39,2,'Demo Product 1',100.00,1,100.00,NULL,NULL),(85,40,2,'Demo Product 1',100.00,1,100.00,NULL,NULL),(86,41,30,'คอยล์จุดระเบิด BMW X3',1450.00,2,2900.00,NULL,'https://image.made-in-china.com/202f0j00WnmqziVrrCcI/High-Quality-Ignition-Coil-for-BMW-1-3-5-6-7-Series-X1-X3-X5-M3-E46-2008-207-208-3008-308-508-Rcz-1-6-0221504100.webp'),(87,41,28,'โช้คอัพหลัง Audi Q5',10800.00,1,10800.00,NULL,'https://s.alicdn.com/@sc04/kf/Ha4e7281c1fdb48439552c84afb1841afg.png'),(88,41,29,'ปั๊มน้ำ Porsche Cayenne',16400.00,1,16400.00,NULL,'https://inwfile.com/s-ce/fogana.jpg'),(89,41,27,'ไส้กรองน้ำมันเครื่อง Benz',260.00,6,1560.00,NULL,'https://cache-igetweb-v2.mt108.info/uploads/images-cache/1007/product/b6c90600330738be10c04d3529cca747_full.jpg'),(90,41,26,'ผ้าเบรกหน้า BMW 3 Series',3450.00,2,6900.00,NULL,'https://inwfile.com/s-cf/ytov0e.jpg'),(91,41,25,'คอยล์จุดระเบิด Mazda 3',2000.00,3,6000.00,NULL,'https://inwfile.com/s-fj/6mp7uv.jpg'),(92,41,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,10,35000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(93,41,23,'ไดชาร์จ Honda Jazz',8500.00,10,85000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg'),(94,41,22,'แบตเตอรี่ 12V 45Ah',1680.00,5,8400.00,NULL,'https://inwfile.com/s-dk/fftzad.jpg'),(95,41,21,'คันชักคันส่ง Nissan Teana',2840.00,6,17040.00,NULL,'https://inwfile.com/s-gi/vlycu2.png'),(96,41,20,'บูชปีกนกล่าง Mazda 2',1000.00,5,5000.00,NULL,'https://down-th.img.susercontent.com/file/de44b45bd6d57e640e271a224378a36d'),(97,41,19,'ลูกหมากปีกนก Toyota Vios',400.00,5,2000.00,NULL,'https://inwfile.com/s-cf/hucvg9.png'),(98,41,18,'โช้คอัพหน้า Honda Accord',4400.00,2,8800.00,NULL,'https://inwfile.com/s-cf/1ah0po.jpg'),(99,41,17,'จานเบรกหน้า Mazda CX-5',6900.00,5,34500.00,NULL,'https://inwfile.com/s-fz/f8ku2t.jpg'),(100,41,15,'ผ้าเบรกหน้า Honda City',1650.00,5,8250.00,NULL,'https://inwfile.com/s-cf/kwku2a.png'),(101,41,13,'กรองน้ำมันเครื่อง Toyota Corolla',400.00,5,2000.00,NULL,'https://inwfile.com/s-cf/2ofjie.jpg'),(102,41,12,'หัวเทียน Mazda 3',2000.00,5,10000.00,NULL,'https://inwfile.com/s-fj/pomux0.jpg'),(103,42,2,'Demo Product 1',100.00,2,200.00,NULL,NULL),(104,42,28,'โช้คอัพหลัง Audi Q5',10800.00,1,10800.00,NULL,'https://s.alicdn.com/@sc04/kf/Ha4e7281c1fdb48439552c84afb1841afg.png'),(105,42,23,'ไดชาร์จ Honda Jazz',8500.00,10,85000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg'),(106,42,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,5,17500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(107,43,23,'ไดชาร์จ Honda Jazz',8500.00,10,85000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg'),(108,43,28,'โช้คอัพหลัง Audi Q5',10800.00,2,21600.00,NULL,'https://s.alicdn.com/@sc04/kf/Ha4e7281c1fdb48439552c84afb1841afg.png'),(109,44,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(110,44,17,'จานเบรกหน้า Mazda CX-5',6900.00,1,6900.00,NULL,'https://inwfile.com/s-fz/f8ku2t.jpg'),(111,45,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(112,45,22,'แบตเตอรี่ 12V 45Ah',1680.00,2,3360.00,NULL,'https://inwfile.com/s-dk/fftzad.jpg'),(113,45,21,'คันชักคันส่ง Nissan Teana',2840.00,2,5680.00,NULL,'https://inwfile.com/s-gi/vlycu2.png'),(114,46,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(115,46,22,'แบตเตอรี่ 12V 45Ah',1680.00,1,1680.00,NULL,'https://inwfile.com/s-dk/fftzad.jpg'),(116,47,30,'คอยล์จุดระเบิด BMW X3',1450.00,1,1450.00,NULL,'https://image.made-in-china.com/202f0j00WnmqziVrrCcI/High-Quality-Ignition-Coil-for-BMW-1-3-5-6-7-Series-X1-X3-X5-M3-E46-2008-207-208-3008-308-508-Rcz-1-6-0221504100.webp'),(117,48,30,'คอยล์จุดระเบิด BMW X3',1450.00,1,1450.00,NULL,'https://image.made-in-china.com/202f0j00WnmqziVrrCcI/High-Quality-Ignition-Coil-for-BMW-1-3-5-6-7-Series-X1-X3-X5-M3-E46-2008-207-208-3008-308-508-Rcz-1-6-0221504100.webp'),(118,49,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(119,50,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(120,51,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(121,52,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(123,54,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(124,55,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(125,56,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,1,3500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(126,56,23,'ไดชาร์จ Honda Jazz',8500.00,1,8500.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg'),(127,57,21,'คันชักคันส่ง Nissan Teana',2840.00,1,2840.00,NULL,'https://inwfile.com/s-gi/vlycu2.png'),(128,57,20,'บูชปีกนกล่าง Mazda 2',1000.00,1,1000.00,NULL,'https://down-th.img.susercontent.com/file/de44b45bd6d57e640e271a224378a36d'),(129,58,24,'ไดสตาร์ท Toyota Hilux Vigo',3500.00,2,7000.00,NULL,'https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg'),(130,59,25,'คอยล์จุดระเบิด Mazda 3',2000.00,3,6000.00,NULL,'https://inwfile.com/s-fj/6mp7uv.jpg'),(131,60,27,'ไส้กรองน้ำมันเครื่อง Benz',260.00,3,780.00,NULL,'https://cache-igetweb-v2.mt108.info/uploads/images-cache/1007/product/b6c90600330738be10c04d3529cca747_full.jpg'),(132,61,26,'ผ้าเบรกหน้า BMW 3 Series',3450.00,4,13800.00,NULL,'https://inwfile.com/s-cf/ytov0e.jpg'),(133,62,27,'ไส้กรองน้ำมันเครื่อง Benz',260.00,5,1300.00,NULL,'https://cache-igetweb-v2.mt108.info/uploads/images-cache/1007/product/b6c90600330738be10c04d3529cca747_full.jpg'),(134,64,25,'คอยล์จุดระเบิด Mazda 3',2000.00,3,6000.00,NULL,'https://inwfile.com/s-fj/6mp7uv.jpg'),(135,65,25,'คอยล์จุดระเบิด Mazda 3 (เคลมเปลี่ยนสินค้า)',0.00,2,0.00,NULL,'https://inwfile.com/s-fj/6mp7uv.jpg'),(136,66,30,'คอยล์จุดระเบิด BMW X3',1450.00,1,1450.00,NULL,'https://image.made-in-china.com/202f0j00WnmqziVrrCcI/High-Quality-Ignition-Coil-for-BMW-1-3-5-6-7-Series-X1-X3-X5-M3-E46-2008-207-208-3008-308-508-Rcz-1-6-0221504100.webp'),(137,66,29,'ปั๊มน้ำ Porsche Cayenne',16400.00,2,32800.00,NULL,'https://inwfile.com/s-ce/fogana.jpg'),(138,67,27,'ไส้กรองน้ำมันเครื่อง Benz',260.00,2,520.00,NULL,'https://cache-igetweb-v2.mt108.info/uploads/images-cache/1007/product/b6c90600330738be10c04d3529cca747_full.jpg');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_number` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_intent_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `customer_user_id` bigint NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING_PAYMENT',
  `subtotal` decimal(12,2) NOT NULL DEFAULT '0.00',
  `shipping_fee` decimal(12,2) NOT NULL DEFAULT '0.00',
  `discount_total` decimal(12,2) NOT NULL DEFAULT '0.00',
  `grand_total` decimal(12,2) NOT NULL DEFAULT '0.00',
  `currency` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'THB',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `promotion_id` bigint DEFAULT NULL,
  `promotion_code_snapshot` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `promotion_discount` decimal(12,2) NOT NULL DEFAULT '0.00',
  `shipping_address` text COLLATE utf8mb4_unicode_ci,
  `shipping_provider` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tracking_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_number` (`order_number`),
  KEY `idx_orders_customer_time` (`customer_user_id`,`created_at`),
  KEY `idx_orders_status` (`status`),
  KEY `idx_orders_promotion` (`promotion_id`),
  KEY `idx_orders_created_at` (`created_at`),
  CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_orders_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (6,'ORD-c3ad8486',NULL,12,'DELIVERED',16140.00,0.00,0.00,16140.00,'THB','2026-05-05 08:59:16','2026-05-31 09:15:43',NULL,NULL,0.00,'dqwdqw fwqdqwd fwqdqw wqfwqf wqfwq','Kerry','TH42343535FED'),(7,'ORD-23c697db',NULL,12,'DELIVERED',16140.00,0.00,0.00,16140.00,'THB','2026-05-05 08:59:17','2026-05-31 09:15:25',NULL,NULL,0.00,'dqwdqw fwqdqwd fwqdqw wqfwqf wqfwq','Kerry','THr324242FEDfe'),(8,'ORD-ea99ac90',NULL,12,'DELIVERED',16140.00,0.00,0.00,16140.00,'THB','2026-05-05 08:59:53','2026-05-31 09:15:09',NULL,NULL,0.00,'fqwfqwf fwqfqw fqwfqw fwqfqw fqwfqw','Kerry','TH24242FEVD3343'),(9,'ORD-45f7f1b9',NULL,12,'DELIVERED',16140.00,0.00,0.00,16140.00,'THB','2026-05-05 09:03:27','2026-05-31 09:14:41',NULL,NULL,0.00,'qwfqwf dw dwq fqwqwf fwqfqw','Flash','TH2425FEEVD343'),(10,'ORD-2f6f02e0',NULL,12,'DELIVERED',6000.00,0.00,0.00,6000.00,'THB','2026-05-06 08:17:06','2026-05-31 09:14:17',NULL,NULL,0.00,'fwqfwqf fqwfqw fqwfwqf fqwfwqf fwqfqwf','Kerry','TH43535FEFVD43'),(11,'ORD-484e1925',NULL,12,'DELIVERED',6000.00,0.00,0.00,3000.00,'THB','2026-05-06 08:46:23','2026-05-31 09:13:53',NULL,NULL,0.00,'ดๆได\"๐ ดๆไดๆไ ดๆไดไๆ ดๆไดๆไโๆ ดไๆด\"๐','Kerry','TH243535FEFED34'),(12,'ORD-c6e85d21',NULL,12,'DELIVERED',6000.00,0.00,0.00,3000.00,'THB','2026-05-06 09:00:38','2026-05-31 09:13:37',NULL,NULL,0.00,'qeqwdwqc ceqdqwcdwq dcqecqwdcqw cqqwcqw dqwdqwqw','Kerry','TH242435FERG353'),(13,'ORD-87853351',NULL,12,'DELIVERED',11770.00,0.00,0.00,11770.00,'THB','2026-05-06 09:01:47','2026-05-31 09:11:04',NULL,NULL,0.00,'feqfeqw fqefeqwf feqfeqwf feqfqew qefeqwfeqw','Kerry','TH33535EVDV4553'),(14,'ORD-23a932e5',NULL,12,'DELIVERED',12000.00,0.00,0.00,12000.00,'THB','2026-05-07 08:51:08','2026-05-31 09:10:16',NULL,NULL,0.00,'fqefqefq fewqfeqf fewfewf feqfeqf fewqfeqwf','Kerry','TH242535VVBR'),(15,'ORD-ee5f6645',NULL,12,'DELIVERED',4500.00,0.00,0.00,4500.00,'THB','2026-05-07 09:27:13','2026-05-31 09:09:50',NULL,NULL,0.00,'fqwfqw dfqwfdqw feqfqw feqwfqewf feqwfqwf','Kerry','TH242535FERVR'),(16,'ORD-9064ebf9',NULL,12,'DELIVERED',2840.00,0.00,0.00,2840.00,'THB','2026-05-10 09:19:27','2026-05-31 09:09:24',NULL,NULL,0.00,'fqefqefeq eqqeweq cqeceqc cqecqe cqecqec','Kerry','TH345543645RFBNG'),(17,'ORD-e6971b6d',NULL,12,'DELIVERED',3000.00,0.00,0.00,1500.00,'THB','2026-05-12 08:29:32','2026-05-31 09:08:44',NULL,NULL,0.00,'ดๆำดๆำ ดๆำดๆำ ดำๆดๆำ ดๆำดๆำ ำๆดำๆดๆ','Kerry','TH2325236RGGB'),(18,'ORD-1778651095875',NULL,12,'DELIVERED',10000.00,1500.00,0.00,6500.00,'THB','2026-05-12 22:44:56','2026-05-31 09:06:07',2,'RAIN',5000.00,'fqwfqwf fqfqw feqwfqw feqfq deqwfdqw','Kerry','TH1245RGFV456'),(19,'ORD-1779071087794',NULL,13,'DELIVERED',13750.00,1500.00,0.00,8375.00,'THB','2026-05-17 19:24:48','2026-05-31 09:08:01',2,'RAIN',6875.00,'สุกฤษฏ์ ตุดถีนนท์ 145/34 ยโสธร 40000','Kerry','TH353467575RGHY'),(20,'ORD-1779073853581','pi_3TgMgoLxTTh0GfjO1z3aq7NM',12,'DELIVERED',16400.00,1500.00,0.00,17900.00,'THB','2026-05-17 20:10:54','2026-07-09 17:02:36',NULL,NULL,0.00,'เสฎฐวุฒิ แก้วก่า 14/2 ม.4 ต.หนองขอนกว้าง อ.เมือง อุดรธานี 41000','Kerry','TH23454678BGRT'),(22,'ORD-1779183248948',NULL,13,'DELIVERED',15710.00,1500.00,0.00,17210.00,'THB','2026-05-19 02:34:09','2026-05-27 07:52:51',NULL,NULL,0.00,'gwegwe ewgweg gwegwe ewgweg ewgweg','Flash','TH112233445566BR'),(23,'ORD-1779184109236',NULL,13,'DELIVERED',590.00,1500.00,0.00,2090.00,'THB','2026-05-19 02:48:29','2026-05-31 09:07:22',NULL,NULL,0.00,'fqfwq fqwfqw fqwfqwf fqwfqw fqfqw','Kerry','TH434353RGGF'),(24,'ORD-1779185046012',NULL,13,'DELIVERED',590.00,1500.00,0.00,2090.00,'THB','2026-05-19 03:04:06','2026-05-25 00:24:25',NULL,NULL,0.00,'cqwcqw cqwcqw cqecqw qfwqcwq dqwefqwf','Kerry','TH123456789BA'),(31,'ORD-1780223541498',NULL,12,'CANCELLED',4400.00,1500.00,0.00,3700.00,'THB','2026-05-31 03:32:21','2026-06-02 05:26:25',2,'RAIN',2200.00,'เสฎฐวุฒิ แก้วก่า 14/2 ตำบลหนองขอนกว้าง บ้านค่ายเสนีย์ อำเภอเมือง จังหวัดอุดรธานี อุดรธานี 41000',NULL,NULL),(33,'ORD-1780377464957',NULL,12,'CANCELLED',11270.00,1500.00,0.00,12770.00,'THB','2026-06-01 22:17:45','2026-06-02 05:20:00',NULL,NULL,0.00,'เสฎฐวุฒิ แก้กว่า 14/2 หมู่4 บ้านค่าเสนีย์ ตำบลหนองขอนกว้าง อำเภอเมือง จังหวัดอุดรธานี อุดรธานี 41000',NULL,NULL),(34,'ORD-1780385566409','pi_3TdmpELxTTh0GfjO1oJBQcee',12,'CANCELLED',36050.00,1500.00,0.00,37550.00,'THB','2026-06-02 00:32:46','2026-06-02 07:34:40',NULL,NULL,0.00,'เก่ง แก้วก่า 14/2 บ้านค่ายเสนีย์ อุดรธานี อุดรธานี 41000',NULL,NULL),(35,'ORD-1780416317482','pi_3TduopLxTTh0GfjO1VCr71Ag',13,'CANCELLED',19300.00,1500.00,0.00,20800.00,'THB','2026-06-02 09:05:17','2026-06-02 16:07:51',NULL,NULL,0.00,'ปอนด์ ยโส 255/55 บ้านหนองใหญ่  จังหวัดยโสธร ยโสธร 42510',NULL,NULL),(36,'ORD-1780416931794','pi_3TduyjLxTTh0GfjO0lmP609m',13,'CANCELLED',10800.00,1500.00,0.00,12300.00,'THB','2026-06-02 09:15:32','2026-06-02 16:17:01',NULL,NULL,0.00,'ปอนด์ ยโส บ้าหนองใหญ่ ยโสธร 43211',NULL,NULL),(37,'ORD-1780825873867',NULL,12,'CANCELLED',345920.00,1500.00,0.00,342420.00,'THB','2026-06-07 02:51:14','2026-06-09 08:44:49',3,'MEGA100K',5000.00,'เสฎฐวุฒิ แก้วก่า 14/2 บ้านค่าเสนยี ตำบลหนองขอนกว้าง อำเภอเมือง จังหวัดอุดรธานี อุดธานี 41000',NULL,NULL),(38,'ORD-1780994255136',NULL,12,'CANCELLED',100.00,1500.00,0.00,1600.00,'THB','2026-06-09 01:37:35','2026-06-09 08:44:49',NULL,NULL,0.00,'ดไๆดๆด ดกๆไดๆได ดำๆดๆได ดๆไดๆได ดๆดๆไ',NULL,NULL),(39,'ORD-1780994869922',NULL,12,'CANCELLED',100.00,1500.00,0.00,1600.00,'THB','2026-06-09 01:47:50','2026-06-09 08:49:50',NULL,NULL,0.00,'fqwfqwfqwf fqwwfqwfqw qwfwqfwqf fqwfwqfqwf fqwfqwf',NULL,NULL),(40,'ORD-1780995059607',NULL,12,'CANCELLED',100.00,1500.00,0.00,1600.00,'THB','2026-06-09 01:51:00','2026-06-09 08:53:00',NULL,NULL,0.00,'fqwfqfq fqfqwf fqfqwf fqfqwf fdwqfwqf',NULL,NULL),(41,'ORD-1780996279290','pi_3TgLh0LxTTh0GfjO172o42iW',12,'REFUNDED',260550.00,1500.00,0.00,262050.00,'THB','2026-06-09 02:11:19','2026-07-09 09:37:06',NULL,NULL,0.00,'เสฎฐวุฒิ แก้วก่า 14/2 หมู่4 บ้านค่าเสนีย์ ตำบลหนองขอนกว้าง อำเภอเมือง จังหวัดอุดรธานี อุดรธานี 41000','Kerry','TH1356547TGH'),(42,'ORD-1780999775215',NULL,13,'CANCELLED',113500.00,1500.00,0.00,110000.00,'THB','2026-06-09 03:09:35','2026-06-09 10:12:31',3,'MEGA100K',5000.00,'qfwqfwq fqqwfqwf fqfqwfwqf fqefqwfqwf fqfqwfqwf',NULL,NULL),(43,'ORD-1781000111521','pi_3TgMgoLxTTh0GfjO1z3aq7NM',13,'DELIVERED',106600.00,1500.00,0.00,103100.00,'THB','2026-06-09 03:15:12','2026-06-09 10:22:08',3,'MEGA100K',5000.00,'สุกฤษฏ์ ตุดถีนนท์ 125/3 บ้านโคกสูง อำเภอเริงนกทา  ยโสธร 43200','Kerry','TH4657576RGB3'),(44,'ORD-1781016243009','pi_3TgQsyLxTTh0GfjO1Xh6x8hj',13,'CANCELLED',10400.00,1500.00,0.00,9900.00,'THB','2026-06-09 07:44:03','2026-06-10 07:18:29',4,'MEGA10K',2000.00,'สุกฤษฏ์  ตุดถีนนท์  125/3 บ้านโคกสูง อำเภอเริงนกทา  ยโสธร 43200',NULL,NULL),(45,'ORD-1781088038120','pi_3TgjYyLxTTh0GfjO0dKL3SvO',14,'PAID',12540.00,1500.00,0.00,12040.00,'THB','2026-06-10 03:40:38','2026-06-10 10:40:57',4,'MEGA10K',2000.00,'จันทราทิพย์ ขันขจร 125/5 บ้านดอนยางเดี่ยว ตำบลโพนงาม อำเภอหนองหาน  อุดรธานี 41500',NULL,NULL),(46,'ORD-1781146774530',NULL,14,'CANCELLED',5180.00,1500.00,0.00,6421.00,'THB','2026-06-10 19:59:35','2026-06-11 03:01:49',5,'PRO5K',259.00,'dwqdfqwf feqfeqf fefeqf fewfewqfq feqfqef',NULL,NULL),(47,'ORD-1782713761338','pi_3TnYWfLxTTh0GfjO0a8OLXaw',12,'SHIPPED',1450.00,1500.00,0.00,2950.00,'THB','2026-06-28 23:16:01','2026-06-29 06:20:49',NULL,NULL,0.00,'fqefqwfq feqwfqfqw fqwfqwqw fqfwqfdwqqw fqwfqwfqw','Flash','TH53532152135grwg'),(48,'ORD-1782718640910','pi_3TnZl1LxTTh0GfjO0srPWzLQ',12,'PAID',1450.00,1500.00,0.00,2950.00,'THB','2026-06-29 00:37:21','2026-06-29 07:37:38',NULL,NULL,0.00,'dqwfqwfw dfqefqwf fqefqwfdqw dfeqfqwfd fqefwq',NULL,NULL),(49,'ORD-1783938687862',NULL,12,'CANCELLED',3500.00,1500.00,0.00,5000.00,'THB','2026-07-13 03:31:28','2026-07-13 10:36:58',NULL,NULL,0.00,'fqfwqfwq fqefqwf fqwfwqfqw fwqfwqfqw feqfqwfqw',NULL,NULL),(50,'ORD-1783939085561',NULL,12,'CANCELLED',3500.00,1500.00,0.00,5000.00,'THB','2026-07-13 03:38:06','2026-07-13 10:58:00',NULL,NULL,0.00,'fqcqwcwqc fqefqwecqwc cqewceqwcqwe eqcqwcqwc dqwdwqcqwc',NULL,NULL),(51,'ORD-1783940348232',NULL,12,'CANCELLED',3500.00,1500.00,0.00,5000.00,'THB','2026-07-13 03:59:08','2026-07-13 11:18:50',NULL,NULL,0.00,'fvqeqwcqw feqweqc eqvqwecqw feqqwcqwc eqcwqecqwe',NULL,NULL),(52,'ORD-1783942038978',NULL,12,'CANCELLED',3500.00,1500.00,0.00,5000.00,'THB','2026-07-13 04:27:19','2026-07-13 14:50:00',NULL,NULL,0.00,'fqwfqwfqw feqfqwq feqfqcq feqceqceq fewceqceq',NULL,NULL),(54,'ORD-1783954249125',NULL,12,'CANCELLED',3500.00,1500.00,0.00,5000.00,'THB','2026-07-13 07:50:49','2026-07-13 14:54:00',NULL,NULL,0.00,'fqwqcqwxc w cqwcqw dwqcwqxc feqcqe',NULL,NULL),(55,'ORD-1783954509608','pi_3TslGNLxTTh0GfjO1EIIBfNl',12,'DELIVERED',3500.00,1500.00,0.00,5000.00,'THB','2026-07-13 07:55:10','2026-07-13 15:00:32',NULL,NULL,0.00,'ดๆไำดๆไดๆไ ดำๆอำไอแำไ ำไอำไอำไ ำไแำๆแำๆ อไำำไแำๆไแ','Flash','TH24242ECE224'),(56,'ORD-1783956783157','pi_3Tslr4LxTTh0GfjO1gz7aIpv',12,'PARTIALLY_REFUNDED',12000.00,1500.00,0.00,13500.00,'THB','2026-07-13 08:33:03','2026-07-13 15:36:39',NULL,NULL,0.00,'feqcqcqew fewqeqceq fewceqcqe feqewcew fewrwr','Kerry','TH2424FDECCW'),(57,'ORD-1783962827986','pi_3TsnQaLxTTh0GfjO15rrQ91D',12,'PARTIALLY_REFUNDED',3840.00,1500.00,0.00,5340.00,'THB','2026-07-13 10:13:48','2026-07-13 17:23:54',NULL,NULL,0.00,'fqefqwefqw feqfeqqwed feqfeqwfqwe qfeqfewfewqfe feewqfewqf','Flash','TH23124CECEWQ'),(58,'ORD-1783965589791','pi_3Tso94LxTTh0GfjO1De9yFdf',12,'PARTIALLY_REFUNDED',7000.00,1500.00,0.00,8500.00,'THB','2026-07-13 10:59:50','2026-07-13 18:03:30',NULL,NULL,0.00,'fevcewvwecwe feqcewcvwe vewvewcwecqc cveqcvewcewq qvewvwevwe','Kerry','TH343FECE233'),(59,'ORD-1783965890058','pi_3TsoE6LxTTh0GfjO1wfC3JAl',12,'PARTIALLY_REFUNDED',6000.00,1500.00,0.00,7500.00,'THB','2026-07-13 11:04:50','2026-07-13 18:08:04',NULL,NULL,0.00,'fqfqwfqwf feqfqwfqw fvrvrvrvv feqfeqfqe feqfeqfq','Flash','TH23423432FVEE'),(60,'ORD-1784361117381','pi_3TuT2gLxTTh0GfjO0P5PRNY7',12,'PRODUCT_EXCHANGE',780.00,1500.00,0.00,2280.00,'THB','2026-07-18 00:51:57','2026-07-18 07:58:59',NULL,NULL,0.00,'wf fqwfqwfqw feqfqwfqw fqefqwfqw feqfqwfqw','Kerry','TH2424FEF24'),(61,'ORD-1784365826804','pi_3TuUGVLxTTh0GfjO1q7tHlhN',12,'DELIVERED',13800.00,1500.00,0.00,15300.00,'THB','2026-07-18 02:10:27','2026-07-18 09:12:42',NULL,NULL,0.00,'fqwfqwfqwf fqefqwfqwf feqfeqvceq feqeqceq eqwfeqfq','Kerry','TH2424FEVEVC'),(62,'ORD-1784365890618','pi_3TuUHWLxTTh0GfjO1ATDU2fv',12,'PARTIALLY_REFUNDED',1300.00,1500.00,0.00,2800.00,'THB','2026-07-18 02:11:31','2026-07-18 09:56:52',NULL,NULL,0.00,'ceqkpmqpm vmeqpkmqwep feqkmqepkcm veqkpmqepom peqmfeqp','Flash','TH3r13FEWFW'),(63,'ORD-1784365826804-EX',NULL,12,'SHIPPED',0.00,0.00,0.00,0.00,'THB','2026-07-18 02:15:02','2026-07-18 09:17:50',NULL,NULL,0.00,'fqwfqwfqwf fqefqwfqwf feqfeqvceq feqeqceq eqwfeqfq','Kerry','THR1431341FE'),(64,'ORD-1784368248869','pi_3TuUtdLxTTh0GfjO0yYPZI44',12,'DELIVERED',6000.00,1500.00,0.00,7500.00,'THB','2026-07-18 02:50:49','2026-07-18 09:51:54',NULL,NULL,0.00,'fqefqwfqw fqfqwfwqf fqwfqwfqwf feqfwqfqw dwqfwqfqw','J&T','TH2442FEVE32'),(65,'ORD-1784368248869-EX',NULL,12,'SHIPPED',0.00,0.00,0.00,0.00,'THB','2026-07-18 02:54:48','2026-07-18 09:55:41',NULL,NULL,0.00,'fqefqwfqw fqfqwfwqf fqwfqwfqwf feqfwqfqw dwqfwqfqw','J&T','TH24242FCWWCW'),(66,'ORD-1786009047434',NULL,12,'CANCELLED',34250.00,1500.00,0.00,35750.00,'THB','2026-08-06 02:37:27','2026-08-06 09:39:30',NULL,NULL,0.00,'qe[omeqo[mecqo cemw[omewo[mcewo[ feoqmewo[cm vcewomcewocmo fceqmcewo[cmewo',NULL,NULL),(67,'ORD-1786883628326','pi_3U53GLLxTTh0GfjO07O31cG8',17,'PAID',520.00,1500.00,0.00,2020.00,'THB','2026-08-16 05:33:48','2026-08-16 12:34:14',NULL,NULL,0.00,'เสฎฐวฒิ แก้วก่า 14/2 บ้านค่ายเสนีย์ อุดรธานี 41000',NULL,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `out_of_stock_requests`
--

DROP TABLE IF EXISTS `out_of_stock_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `out_of_stock_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_user_id` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `requested_brand_id` bigint DEFAULT NULL,
  `requested_model_id` bigint DEFAULT NULL,
  `year` int DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPEN',
  `admin_note` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `part_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `car_brand` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `car_model` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_oos_product` (`product_id`),
  KEY `fk_oos_brand` (`requested_brand_id`),
  KEY `fk_oos_model` (`requested_model_id`),
  KEY `idx_oos_status` (`status`),
  KEY `idx_oos_customer` (`customer_user_id`),
  CONSTRAINT `fk_oos_brand` FOREIGN KEY (`requested_brand_id`) REFERENCES `car_brands` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_oos_customer` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_oos_model` FOREIGN KEY (`requested_model_id`) REFERENCES `car_models` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_oos_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `out_of_stock_requests`
--

LOCK TABLES `out_of_stock_requests` WRITE;
/*!40000 ALTER TABLE `out_of_stock_requests` DISABLE KEYS */;
INSERT INTO `out_of_stock_requests` VALUES (4,13,NULL,NULL,NULL,NULL,'test',NULL,'REJECTED',NULL,'2026-05-30 09:26:02','2026-05-30 09:28:47','test','test','test'),(5,13,NULL,NULL,NULL,NULL,'อยากได้ตัวที่เป็นลายตัดขวางครับ',NULL,'APPROVE',NULL,'2026-05-31 02:02:51','2026-05-31 02:03:27','ฝากระโปรง คาร์บอน','isuzu','D-Max/2020'),(6,12,NULL,NULL,NULL,NULL,'ต้องการของด่วนพร้อมขอโปรโมชั่น',NULL,'APPROVE',NULL,'2026-06-07 01:21:23','2026-06-07 01:22:03','ต้องการอะไหล่ทุกตัวไหนร้านที่หมดตอนนี้','ทุกยี่ห้อ','ทุกรุ่น');
/*!40000 ALTER TABLE `out_of_stock_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `provider` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STRIPE',
  `payment_intent_id` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `charge_id` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REQUIRES_PAYMENT',
  `amount` decimal(12,2) NOT NULL,
  `currency` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'THB',
  `paid_at` timestamp NULL DEFAULT NULL,
  `raw_payload` json DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `stripe_payment_intent_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_payment_intent` (`payment_intent_id`),
  KEY `idx_payments_order` (`order_id`),
  KEY `idx_payments_status` (`status`),
  CONSTRAINT `fk_payments_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_brands`
--

DROP TABLE IF EXISTS `product_brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_brands` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_brands`
--

LOCK TABLES `product_brands` WRITE;
/*!40000 ALTER TABLE `product_brands` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_categories`
--

DROP TABLE IF EXISTS `product_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(140) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `idx_category_parent` (`parent_id`),
  CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `product_categories` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_categories`
--

LOCK TABLES `product_categories` WRITE;
/*!40000 ALTER TABLE `product_categories` DISABLE KEYS */;
INSERT INTO `product_categories` VALUES (1,NULL,'Demo Category','demo-category-1771670063','2026-02-21 10:34:23');
/*!40000 ALTER TABLE `product_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_fitments`
--

DROP TABLE IF EXISTS `product_fitments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_fitments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `car_brand_id` bigint NOT NULL,
  `car_model_id` bigint NOT NULL,
  `year_from` int NOT NULL,
  `year_to` int DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_fitment_model` (`car_model_id`),
  KEY `idx_fitment_lookup` (`car_brand_id`,`car_model_id`,`year_from`,`year_to`),
  KEY `idx_fitment_product` (`product_id`),
  CONSTRAINT `fk_fitment_brand` FOREIGN KEY (`car_brand_id`) REFERENCES `car_brands` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_fitment_model` FOREIGN KEY (`car_model_id`) REFERENCES `car_models` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_fitment_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_fitments`
--

LOCK TABLES `product_fitments` WRITE;
/*!40000 ALTER TABLE `product_fitments` DISABLE KEYS */;
INSERT INTO `product_fitments` VALUES (1,10,4,4,2018,2024,NULL,'2026-03-02 07:30:38'),(2,10,4,6,2010,2014,NULL,'2026-03-02 07:31:31'),(3,10,4,2,2005,2012,NULL,'2026-03-02 07:32:16'),(4,10,4,1,2021,2026,NULL,'2026-03-02 07:32:56'),(5,10,4,5,2017,2025,NULL,'2026-03-02 07:33:33'),(6,10,4,3,2022,2025,NULL,'2026-03-02 07:34:33'),(7,11,3,7,2002,2006,NULL,'2026-03-02 09:49:30'),(8,12,5,9,2011,2015,NULL,'2026-03-03 07:16:14'),(9,13,3,8,2009,2011,NULL,'2026-03-03 07:17:20'),(10,14,6,10,2017,2017,NULL,'2026-03-03 07:18:40'),(11,15,4,13,2020,2026,NULL,'2026-03-03 07:20:19'),(12,16,3,11,2019,2021,NULL,'2026-03-03 07:22:03'),(13,17,5,12,2012,2017,NULL,'2026-03-03 07:23:47'),(14,18,4,14,2013,2019,NULL,'2026-03-03 07:26:14'),(15,19,3,15,2003,2018,NULL,'2026-03-03 07:27:50'),(16,20,5,16,2014,2022,NULL,'2026-03-03 07:29:45'),(17,21,6,17,2009,2013,NULL,'2026-03-03 07:31:58'),(18,22,3,11,2010,2026,NULL,'2026-03-03 07:34:14'),(19,22,3,7,2010,2026,NULL,'2026-03-03 07:34:21'),(20,22,3,8,2010,2026,NULL,'2026-03-03 07:34:32'),(21,22,3,15,2010,2026,NULL,'2026-03-03 07:34:43'),(22,22,14,47,2010,2026,NULL,'2026-03-03 07:34:51'),(23,22,4,14,2010,2026,NULL,'2026-03-03 07:35:00'),(24,22,4,13,2010,2026,NULL,'2026-03-03 07:35:13'),(25,22,4,18,2010,2026,NULL,'2026-03-03 07:35:27'),(26,22,6,10,2010,2026,NULL,'2026-03-03 07:35:48'),(27,23,4,18,2003,2014,NULL,'2026-03-03 07:37:55'),(28,24,3,19,2005,2015,NULL,'2026-03-03 07:39:22'),(29,25,5,9,2014,2018,NULL,'2026-03-03 07:41:06'),(30,26,1,35,2011,2026,NULL,'2026-03-03 07:43:30'),(31,27,2,36,2015,2026,NULL,'2026-03-03 07:46:48'),(32,27,2,37,2015,2026,NULL,'2026-03-03 07:47:13'),(33,28,10,39,2008,2015,NULL,'2026-03-03 07:49:02'),(34,29,12,43,2011,2017,NULL,'2026-03-03 07:51:07'),(35,30,1,34,2015,2026,NULL,'2026-03-03 07:52:55');
/*!40000 ALTER TABLE `product_fitments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_product_images_product` (`product_id`,`sort_order`),
  CONSTRAINT `fk_product_images_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_reviews`
--

DROP TABLE IF EXISTS `product_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `comment` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_review_product` (`product_id`),
  KEY `fk_review_user` (`user_id`),
  KEY `fk_review_order` (`order_id`),
  CONSTRAINT `fk_review_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_reviews`
--

LOCK TABLES `product_reviews` WRITE;
/*!40000 ALTER TABLE `product_reviews` DISABLE KEYS */;
INSERT INTO `product_reviews` VALUES (1,14,13,24,5,'สินค้าดีมีคุณภาพครับ','2026-05-25 02:30:48'),(2,29,12,20,5,'ของแท้จากยุโรปเลยครับ ยอดเยี่ยม','2026-05-27 07:59:01'),(3,21,13,22,3,'มีรอยเล็กน้อยครับช่วยปรับปรุงสินค้าด้วยนะครับ','2026-05-27 08:49:01'),(4,30,13,22,5,'สินค้าแท้จากยุโรปจริงๆครับมีคุณภาพ','2026-05-27 08:49:01');
/*!40000 ALTER TABLE `product_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sku` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `category_id` bigint NOT NULL,
  `product_brand_id` bigint DEFAULT NULL,
  `price` decimal(12,2) NOT NULL,
  `import_cost_avg` decimal(12,2) NOT NULL DEFAULT '0.00',
  `stock_qty` int NOT NULL DEFAULT '0',
  `reorder_level` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stock` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`),
  KEY `fk_products_brand` (`product_brand_id`),
  KEY `idx_products_category` (`category_id`),
  KEY `idx_products_active` (`is_active`),
  KEY `idx_products_name` (`name`),
  CONSTRAINT `fk_products_brand` FOREIGN KEY (`product_brand_id`) REFERENCES `product_brands` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (2,'SKU-DEMO-001','Demo Product 1','for PO item test',1,NULL,100.00,95.00,100,0,1,'2026-02-21 10:34:46','2026-06-09 10:12:31',NULL,0),(10,'ENG-AIRFLT-HON-CIV-STD-001','กรองอากาศเครื่องยนต์ Honda Civic',NULL,3,NULL,1500.00,700.00,18,0,1,'2026-03-02 06:53:36','2026-06-09 08:44:49','https://inwfile.com/s-gf/stalxc.jpg',0),(11,'BRK-PAD-TOY-CAM-FR-001','ผ้าเบรกหน้า Toyota Camry',NULL,4,NULL,2500.00,950.00,44,0,1,'2026-03-02 09:48:11','2026-06-09 08:44:49','https://inwfile.com/s-fj/geeakk.jpg',0),(12,'ENG-SPARK-MAZ-M3-STD-003','หัวเทียน Mazda 3',NULL,3,NULL,2000.00,600.00,10,0,1,'2026-03-03 06:48:33','2026-06-10 10:22:59','https://inwfile.com/s-fj/pomux0.jpg',0),(13,'ENG-OILFLT-TOY-COR-STD-002','กรองน้ำมันเครื่อง Toyota Corolla',NULL,3,NULL,400.00,35.00,10,0,1,'2026-03-03 06:49:35','2026-06-10 10:22:59','https://inwfile.com/s-cf/2ofjie.jpg',0),(14,'ENG-BELT-NSN-ALM-STD-004','สายพานหน้าเครื่อง Nissan Almera',NULL,3,NULL,590.00,400.00,9,0,1,'2026-03-03 06:52:08','2026-06-02 05:17:44','https://inwfile.com/s-cf/piuvsm.png',0),(15,'BRK-PAD-F-HON-CTY-STD-001','ผ้าเบรกหน้า Honda City',NULL,4,NULL,1650.00,430.00,10,0,1,'2026-03-03 07:20:06','2026-06-10 10:22:59','https://inwfile.com/s-cf/kwku2a.png',0),(16,'BRK-PAD-R-TOY-YAR-STD-002','ผ้าเบรกหลัง Toyota Yaris',NULL,4,NULL,1900.00,700.00,10,0,1,'2026-03-03 07:21:47','2026-06-09 08:44:49','https://inwfile.com/s-fa/bhx8id.jpg',0),(17,'BRK-DISC-F-MAZ-CX5-STD-003','จานเบรกหน้า Mazda CX-5',NULL,4,NULL,6900.00,2328.57,7,0,1,'2026-03-03 07:23:30','2026-06-10 10:22:59','https://inwfile.com/s-fz/f8ku2t.jpg',0),(18,'SUS-SHOCK-F-HON-ACD-STD-001','โช้คอัพหน้า Honda Accord',NULL,2,NULL,4400.00,300.00,9,0,1,'2026-03-03 07:25:52','2026-06-09 09:11:19','https://inwfile.com/s-cf/1ah0po.jpg',0),(19,'SUS-BALLJ-TOY-VIO-STD-002','ลูกหมากปีกนก Toyota Vios',NULL,2,NULL,400.00,43.33,15,0,1,'2026-03-03 07:27:36','2026-06-10 10:23:22','https://inwfile.com/s-cf/hucvg9.png',0),(20,'SUS-BUSH-L-MAZ-M2-STD-003','บูชปีกนกล่าง Mazda 2',NULL,2,NULL,1000.00,175.00,9,0,1,'2026-03-03 07:29:18','2026-07-13 17:13:48','https://down-th.img.susercontent.com/file/de44b45bd6d57e640e271a224378a36d',0),(21,'SUS-TIEROD-NSN-TEA-STD-004','คันชักคันส่ง Nissan Teana',NULL,2,NULL,2840.00,520.00,12,0,1,'2026-03-03 07:31:37','2026-07-13 17:13:48','https://inwfile.com/s-gi/vlycu2.png',0),(22,'ELE-BATT-12V-45AH-STD-001','แบตเตอรี่ 12V 45Ah',NULL,5,NULL,1680.00,466.67,13,0,1,'2026-03-03 07:33:24','2026-06-11 03:01:49','https://inwfile.com/s-dk/fftzad.jpg',0),(23,'ELE-ALT-HON-JAZ-STD-002','ไดชาร์จ Honda Jazz',NULL,5,NULL,8500.00,560.00,9,0,1,'2026-03-03 07:37:21','2026-07-13 15:33:04','https://mpdynamo.com/wp-content/uploads/2019/04/Honda-Jazz.jpg',0),(24,'ELE-START-TOY-HLX-STD-003','ไดสตาร์ท Toyota Hilux Vigo',NULL,5,NULL,3500.00,953.33,10,0,1,'2026-03-03 07:39:10','2026-07-13 17:59:50','https://mpdynamo.com/wp-content/uploads/2019/10/Toyota-Vigo.jpg',0),(25,'ELE-IGNC-MAZ-M3-STD-004','คอยล์จุดระเบิด Mazda 3',NULL,5,NULL,2000.00,426.67,7,0,1,'2026-03-03 07:40:12','2026-07-18 09:54:48','https://inwfile.com/s-fj/6mp7uv.jpg',0),(26,'BMW-BRAKEPAD-3SER-FR-001','ผ้าเบรกหน้า BMW 3 Series',NULL,4,NULL,3450.00,1700.00,4,0,1,'2026-03-03 07:43:22','2026-08-13 17:18:43','https://inwfile.com/s-cf/ytov0e.jpg',0),(27,'BENZ-OILFLT-CCLASS-001','ไส้กรองน้ำมันเครื่อง Benz',NULL,13,NULL,260.00,30.67,4,0,1,'2026-03-03 07:46:39','2026-08-16 12:33:49','https://cache-igetweb-v2.mt108.info/uploads/images-cache/1007/product/b6c90600330738be10c04d3529cca747_full.jpg',0),(28,'AUDI-SHOCK-Q5-RR-001','โช้คอัพหลัง Audi Q5',NULL,2,NULL,10800.00,3800.00,3,0,1,'2026-03-03 07:48:51','2026-06-10 10:23:14','https://s.alicdn.com/@sc04/kf/Ha4e7281c1fdb48439552c84afb1841afg.png',0),(29,'PORSCHE-WATERPUMP-CAY-001','ปั๊มน้ำ Porsche Cayenne',NULL,6,NULL,16400.00,6125.00,4,0,1,'2026-03-03 07:50:48','2026-08-06 09:39:30','https://inwfile.com/s-ce/fogana.jpg',0),(30,'BMW-IGNCOIL-X3-001','คอยล์จุดระเบิด BMW X3','-ทนทานต่อสภาพแวดล้อม: ทนต่อการใช้งานในสภาพอากาศทั่วไป.\n-ประสิทธิภาพสูงสุดในการจุดระเบิด: ช่วยเพิ่มแรงบันดาลใจในการเร่งเครื่อง.\n-ชุด 6 ชิ้น สำหรับการเปลี่ยนทั้งหมด: ช่วยประหยัดเวลาในการติดตั้งและใช้งานได้ทันที.\n-เหมาะกับรุ่น X1 X3 X4 X5 X6 Z3 Z4: ใช้ได้กับรุ่นที่มีความต้องการเฉพาะ.\n-การใช้งานได้กับรถ BMW หลายรุ่น: รองรับทุกรุ่นจาก 1 ถึง 7 ซีรีส์ 2004-2020.',5,NULL,1450.00,340.00,9,0,1,'2026-03-03 07:52:44','2026-08-06 09:39:30','https://image.made-in-china.com/202f0j00WnmqziVrrCcI/High-Quality-Ignition-Coil-for-BMW-1-3-5-6-7-Series-X1-X3-X5-M3-E46-2008-207-208-3008-308-508-Rcz-1-6-0221504100.webp',0);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion_categories`
--

DROP TABLE IF EXISTS `promotion_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion_categories` (
  `promotion_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`promotion_id`,`category_id`),
  UNIQUE KEY `uk_promo_cat` (`promotion_id`,`category_id`),
  KEY `fk_promo_categories_category` (`category_id`),
  CONSTRAINT `fk_promo_categories_category` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_promo_categories_promo` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion_categories`
--

LOCK TABLES `promotion_categories` WRITE;
/*!40000 ALTER TABLE `promotion_categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion_products`
--

DROP TABLE IF EXISTS `promotion_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion_products` (
  `promotion_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`promotion_id`,`product_id`),
  UNIQUE KEY `uq_promo_product` (`promotion_id`,`product_id`),
  KEY `fk_promo_products_product` (`product_id`),
  CONSTRAINT `fk_promo_products_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_promo_products_promo` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion_products`
--

LOCK TABLES `promotion_products` WRITE;
/*!40000 ALTER TABLE `promotion_products` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion_redemptions`
--

DROP TABLE IF EXISTS `promotion_redemptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion_redemptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `promotion_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `customer_user_id` bigint NOT NULL,
  `discount_amount` decimal(12,2) NOT NULL DEFAULT '0.00',
  `redeemed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `idx_redeem_promo` (`promotion_id`),
  KEY `idx_redeem_customer` (`customer_user_id`,`promotion_id`),
  CONSTRAINT `fk_redeem_customer` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_redeem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_redeem_promo` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion_redemptions`
--

LOCK TABLES `promotion_redemptions` WRITE;
/*!40000 ALTER TABLE `promotion_redemptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion_redemptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion_usages`
--

DROP TABLE IF EXISTS `promotion_usages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion_usages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `promotion_id` bigint NOT NULL,
  `customer_user_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `used_at` datetime NOT NULL,
  `use_at` datetime DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pu_order` (`order_id`),
  KEY `idx_pu_promo` (`promotion_id`),
  KEY `idx_pu_user` (`customer_user_id`),
  KEY `idx_user` (`customer_user_id`),
  KEY `idx_promo` (`promotion_id`),
  KEY `fk_user` (`user_id`),
  CONSTRAINT `fk_pu_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pu_promo` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pu_user` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion_usages`
--

LOCK TABLES `promotion_usages` WRITE;
/*!40000 ALTER TABLE `promotion_usages` DISABLE KEYS */;
INSERT INTO `promotion_usages` VALUES (1,2,12,18,'2026-05-13 05:44:56',NULL,NULL),(2,2,13,19,'2026-05-18 02:24:48',NULL,NULL),(10,2,12,31,'2026-05-31 10:32:22',NULL,NULL),(11,3,12,37,'2026-06-07 09:51:14',NULL,NULL),(13,3,13,43,'2026-06-09 10:15:12',NULL,NULL),(14,4,13,44,'2026-06-09 14:44:03',NULL,NULL),(15,4,14,45,'2026-06-10 10:40:38',NULL,NULL);
/*!40000 ALTER TABLE `promotion_usages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `promo_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` decimal(12,2) NOT NULL,
  `max_discount` decimal(12,2) DEFAULT NULL,
  `min_order_amount` decimal(12,2) DEFAULT NULL,
  `start_at` datetime NOT NULL,
  `end_at` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `usage_limit` int DEFAULT NULL,
  `per_user_limit` int DEFAULT NULL,
  `applies_to` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `fk_promotions_created_by` (`created_by`),
  KEY `idx_promotions_active_time` (`is_active`,`start_at`,`end_at`),
  KEY `idx_promotions_type` (`promo_type`),
  CONSTRAINT `fk_promotions_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
INSERT INTO `promotions` VALUES (1,'SUMMER','Summer Sales',NULL,'PERCENT',30.00,NULL,NULL,'2026-03-16 16:59:00','2026-04-16 16:59:00',1,NULL,1,'CATEGORY',5,'2026-03-16 15:19:13','2026-03-16 15:20:13'),(2,'RAIN','Sales!!!',NULL,'PERCENT',50.00,NULL,NULL,'2026-04-03 17:17:00','2026-06-04 17:17:00',1,NULL,NULL,'ORDER',5,'2026-04-02 17:18:09','2026-05-05 13:15:11'),(3,'MEGA100K','sales ช็อป 100,00 บาทขึ้นไป',NULL,'PERCENT',30.00,5000.00,100000.00,'2026-06-07 09:42:00','2026-06-30 09:42:00',1,20,1,'ORDER',5,'2026-06-07 09:43:03','2026-06-09 10:06:28'),(4,'MEGA10K','ลดกระหน่ำช็อปครบ 10,000 ลดสูง 2,000',NULL,'PERCENT',20.00,2000.00,10000.00,'2026-06-09 14:37:00','2026-06-30 14:37:00',1,20,1,'ORDER',5,'2026-06-09 14:37:40',NULL),(5,'PRO5K','โปรโมชั่นฤดูฝน',NULL,'PERCENT',5.00,300.00,5000.00,'2026-06-11 02:57:00','2026-06-30 02:57:00',1,20,1,'ORDER',5,'2026-06-11 02:57:30',NULL);
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_items`
--

DROP TABLE IF EXISTS `purchase_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `purchase_order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `qty` int NOT NULL,
  `target_unit_cost` decimal(12,2) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_po_product` (`purchase_order_id`,`product_id`),
  KEY `fk_poi_product` (`product_id`),
  KEY `idx_poi_po` (`purchase_order_id`),
  CONSTRAINT `fk_poi_po` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_poi_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_items`
--

LOCK TABLES `purchase_order_items` WRITE;
/*!40000 ALTER TABLE `purchase_order_items` DISABLE KEYS */;
INSERT INTO `purchase_order_items` VALUES (1,1,2,100,100.00,'2026-02-21 03:42:41'),(2,2,2,100,100.00,'2026-02-21 07:55:29'),(3,11,10,50,600.00,'2026-03-02 02:03:25'),(4,12,10,1,600.00,'2026-03-02 02:26:38'),(5,13,10,50,600.00,'2026-03-02 02:28:26'),(6,14,10,50,600.00,'2026-03-02 02:35:49'),(7,15,11,30,1000.00,'2026-03-02 02:50:00'),(8,16,11,30,1000.00,'2026-03-02 09:40:37'),(9,17,10,50,200.00,'2026-03-02 11:05:36'),(10,18,23,25,3500.00,'2026-03-03 01:09:19'),(11,18,18,15,2500.00,'2026-03-03 01:09:19'),(12,18,15,16,700.00,'2026-03-03 01:09:19'),(13,18,10,30,600.00,'2026-03-03 01:09:19'),(14,18,21,20,1300.00,'2026-03-03 01:09:19'),(15,18,14,15,200.00,'2026-03-03 01:09:19'),(16,19,30,20,650.00,'2026-03-17 00:41:21'),(17,19,28,8,4500.00,'2026-03-17 00:41:21'),(18,19,27,5,90.00,'2026-03-17 00:41:21'),(19,20,30,7,650.00,'2026-05-17 19:34:32'),(20,20,29,4,8000.00,'2026-05-17 19:34:32'),(21,20,28,3,4500.00,'2026-05-17 19:34:32'),(22,20,27,10,90.00,'2026-05-17 19:34:32'),(23,20,26,3,1700.00,'2026-05-17 19:34:32'),(24,20,25,5,800.00,'2026-05-17 19:34:32'),(25,20,24,20,1500.00,'2026-05-17 19:34:32'),(26,21,22,10,780.00,'2026-05-17 20:03:00'),(27,22,22,10,780.00,'2026-06-07 01:32:21'),(28,22,20,10,460.00,'2026-06-07 01:32:21'),(29,22,19,10,150.00,'2026-06-07 01:32:21'),(30,22,17,10,4000.00,'2026-06-07 01:32:21'),(31,22,16,10,870.00,'2026-06-07 01:32:21'),(32,23,13,10,150.00,'2026-06-07 01:35:11'),(33,23,12,10,900.00,'2026-06-07 01:35:11'),(34,24,25,13,600.00,'2026-06-10 00:43:12'),(35,25,24,7,1000.00,'2026-06-10 02:35:27'),(36,25,23,5,1200.00,'2026-06-10 02:35:27'),(37,25,22,10,500.00,'2026-06-10 02:35:27'),(38,25,21,8,600.00,'2026-06-10 02:35:27'),(39,25,20,5,250.00,'2026-06-10 02:35:27'),(40,25,19,10,70.00,'2026-06-10 02:35:27'),(41,26,30,8,500.00,'2026-06-10 02:37:52'),(42,26,29,3,6500.00,'2026-06-10 02:37:52'),(43,26,28,3,4200.00,'2026-06-10 02:37:52'),(44,26,27,11,60.00,'2026-06-10 02:37:52'),(45,26,26,5,1600.00,'2026-06-10 02:37:52'),(46,27,17,2,2500.00,'2026-06-10 02:40:35'),(47,27,15,7,500.00,'2026-06-10 02:40:35'),(48,27,13,5,50.00,'2026-06-10 02:40:35'),(49,27,12,5,650.00,'2026-06-10 02:40:35'),(50,28,26,4,1250.00,'2026-08-07 01:47:48'),(51,29,26,7,1640.00,'2026-08-13 02:05:27'),(52,30,26,1,1640.00,'2026-08-15 10:14:11'),(53,31,26,1,1640.00,'2026-08-15 10:15:15');
/*!40000 ALTER TABLE `purchase_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `po_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `admin_user_id` bigint NOT NULL,
  `supplier_user_id` bigint NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT',
  `currency` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USD',
  `expected_arrival_date` date DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `po_number` (`po_number`),
  KEY `idx_po_supplier_status` (`supplier_user_id`,`status`),
  KEY `idx_po_admin_created` (`admin_user_id`,`created_at`),
  CONSTRAINT `fk_po_admin` FOREIGN KEY (`admin_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_po_supplier` FOREIGN KEY (`supplier_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
INSERT INTO `purchase_orders` VALUES (1,'PO-1771669050756',5,4,'APPROVED','THB',NULL,NULL,'2026-02-21 03:17:31','2026-02-21 16:41:01'),(2,'PO-1771671332384',5,4,'APPROVED','THB',NULL,NULL,'2026-02-21 03:55:32','2026-02-21 16:21:47'),(3,'PO-1771842696262',5,4,'DRAFT','THB',NULL,NULL,'2026-02-23 03:31:36',NULL),(4,'PO-1772441451603',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:50:52',NULL),(5,'PO-1772441462066',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:51:02',NULL),(6,'PO-1772441482038',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:51:22',NULL),(7,'PO-1772441504856',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:51:45',NULL),(8,'PO-1772441548311',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:52:28',NULL),(9,'PO-1772441562320',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:52:42',NULL),(10,'PO-1772441581853',5,4,'DRAFT','THB',NULL,NULL,'2026-03-02 01:53:02',NULL),(11,'PO-1772442204832',5,4,'DRAFT','JPY',NULL,NULL,'2026-03-02 02:03:25',NULL),(12,'PO-1772443598209',5,4,'DRAFT','USD',NULL,NULL,'2026-03-02 02:26:38',NULL),(13,'PO-1772443705814',5,4,'DRAFT','JPY',NULL,NULL,'2026-03-02 02:28:26',NULL),(14,'PO-1772444148578',5,4,'DRAFT','JPY',NULL,NULL,'2026-03-02 02:35:49',NULL),(15,'PO-1772444999905',5,4,'QUOTED','JPY',NULL,NULL,'2026-03-02 02:50:00','2026-03-02 16:22:31'),(16,'PO-1772469636658',5,8,'CONFIRMED','THB',NULL,NULL,'2026-03-02 09:40:37','2026-03-02 16:43:51'),(17,'PO-1772474736456',5,8,'SENT','THB',NULL,NULL,'2026-03-02 11:05:36','2026-03-02 18:06:24'),(18,'PO-1772525358654',5,9,'CONFIRMED','JPY',NULL,NULL,'2026-03-03 01:09:19','2026-03-03 08:18:48'),(19,'PO-1773733281298',5,9,'QUOTED','THB',NULL,NULL,'2026-03-17 00:41:21','2026-03-17 09:31:18'),(20,'PO-1779071671695',5,4,'CONFIRMED','JPY',NULL,NULL,'2026-05-17 19:34:32','2026-05-18 02:50:48'),(21,'PO-1779073380034',5,4,'CONFIRMED','JPY',NULL,NULL,'2026-05-17 20:03:00','2026-05-18 03:04:33'),(22,'PO-1780821140955',5,6,'CONFIRMED','THB',NULL,NULL,'2026-06-07 01:32:21','2026-06-07 08:40:05'),(23,'PO-1780821310949',5,4,'CONFIRMED','THB',NULL,NULL,'2026-06-07 01:35:11','2026-06-07 08:40:14'),(24,'PO-1781077391563',5,4,'CONFIRMED','JPY',NULL,NULL,'2026-06-10 00:43:12','2026-06-10 09:53:54'),(25,'PO-1781084126947',5,4,'CONFIRMED','JPY',NULL,NULL,'2026-06-10 02:35:27','2026-06-10 09:53:32'),(26,'PO-1781084272217',5,8,'CONFIRMED','USD',NULL,NULL,'2026-06-10 02:37:52','2026-06-10 09:53:06'),(27,'PO-1781084434882',5,6,'CONFIRMED','JPY',NULL,NULL,'2026-06-10 02:40:35','2026-06-10 09:53:17'),(28,'PO-1786092468300',5,4,'CONFIRMED','THB',NULL,NULL,'2026-08-07 01:47:48','2026-08-07 08:50:06'),(29,'PO-1786611927356',5,4,'CONFIRMED','THB',NULL,NULL,'2026-08-13 02:05:27','2026-08-13 09:08:17'),(30,'PO-1786814051035',5,4,'DRAFT','THB',NULL,NULL,'2026-08-15 10:14:11',NULL),(31,'PO-1786814115449',5,4,'SENT','THB',NULL,NULL,'2026-08-15 10:15:15','2026-08-15 17:15:17');
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_item_id` bigint NOT NULL,
  `customer_user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `rating` tinyint NOT NULL,
  `comment` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_item_id` (`order_item_id`),
  KEY `fk_reviews_customer` (`customer_user_id`),
  KEY `idx_reviews_product_time` (`product_id`,`created_at`),
  CONSTRAINT `fk_reviews_customer` FOREIGN KEY (`customer_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_reviews_item` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reviews_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN','2026-02-07 05:06:55'),(2,'CUSTOMER','2026-02-07 05:06:55'),(3,'SUPPLIER','2026-02-07 05:06:55'),(4,'CUSTOMS','2026-02-07 05:06:55');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipments`
--

DROP TABLE IF EXISTS `shipments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `carrier` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tracking_number` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipped_at` timestamp NULL DEFAULT NULL,
  `delivered_at` timestamp NULL DEFAULT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NOT_SHIPPED',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `tracking_no` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `idx_shipments_tracking` (`tracking_number`),
  CONSTRAINT `fk_shipments_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipments`
--

LOCK TABLES `shipments` WRITE;
/*!40000 ALTER TABLE `shipments` DISABLE KEYS */;
INSERT INTO `shipments` VALUES (1,24,'Kerry',NULL,NULL,'2026-05-24 17:24:25','DELIVERED',NULL,'2026-05-19 15:22:44','2026-05-25 00:24:25','TH123456789BA'),(2,22,'Flash',NULL,'2026-05-27 00:51:19','2026-05-27 00:52:51','DELIVERED',NULL,'2026-05-27 07:51:19','2026-05-27 07:52:51','TH112233445566BR'),(3,20,'Kerry',NULL,'2026-05-27 00:56:08','2026-05-27 00:57:56','DELIVERED',NULL,'2026-05-27 07:56:07','2026-05-27 07:57:56','TH23454678BGRT'),(4,18,'Kerry',NULL,'2026-05-31 02:04:21','2026-05-31 02:06:07','DELIVERED',NULL,'2026-05-31 09:04:21','2026-05-31 09:06:07','TH1245RGFV456'),(5,23,'Kerry',NULL,'2026-05-31 02:07:04','2026-05-31 02:07:23','DELIVERED',NULL,'2026-05-31 09:07:03','2026-05-31 09:07:22','TH434353RGGF'),(6,19,'Kerry',NULL,'2026-05-31 02:07:46','2026-05-31 02:08:01','DELIVERED',NULL,'2026-05-31 09:07:45','2026-05-31 09:08:01','TH353467575RGHY'),(7,17,'Kerry',NULL,'2026-05-31 02:08:31','2026-05-31 02:08:44','DELIVERED',NULL,'2026-05-31 09:08:31','2026-05-31 09:08:44','TH2325236RGGB'),(8,16,'Kerry',NULL,'2026-05-31 02:09:11','2026-05-31 02:09:25','DELIVERED',NULL,'2026-05-31 09:09:10','2026-05-31 09:09:24','TH345543645RFBNG'),(9,15,'Kerry',NULL,'2026-05-31 02:09:39','2026-05-31 02:09:51','DELIVERED',NULL,'2026-05-31 09:09:39','2026-05-31 09:09:50','TH242535FERVR'),(10,14,'Kerry',NULL,'2026-05-31 02:10:03','2026-05-31 02:10:16','DELIVERED',NULL,'2026-05-31 09:10:03','2026-05-31 09:10:16','TH242535VVBR'),(11,13,'Kerry',NULL,'2026-05-31 02:10:50','2026-05-31 02:11:04','DELIVERED',NULL,'2026-05-31 09:10:50','2026-05-31 09:11:04','TH33535EVDV4553'),(12,12,'Kerry',NULL,'2026-05-31 02:13:25','2026-05-31 02:13:37','DELIVERED',NULL,'2026-05-31 09:13:24','2026-05-31 09:13:37','TH242435FERG353'),(13,11,'Kerry',NULL,NULL,'2026-05-31 02:13:54','DELIVERED',NULL,'2026-05-31 09:13:48','2026-05-31 09:13:53','TH243535FEFED34'),(14,10,'Kerry',NULL,'2026-05-31 02:14:04','2026-05-31 02:14:17','DELIVERED',NULL,'2026-05-31 09:14:04','2026-05-31 09:14:17','TH43535FEFVD43'),(15,9,'Flash',NULL,'2026-05-31 02:14:30','2026-05-31 02:14:41','DELIVERED',NULL,'2026-05-31 09:14:30','2026-05-31 09:14:41','TH2425FEEVD343'),(16,8,'Kerry',NULL,'2026-05-31 02:14:49','2026-05-31 02:15:10','DELIVERED',NULL,'2026-05-31 09:14:48','2026-05-31 09:15:09','TH24242FEVD3343'),(17,7,'Kerry',NULL,'2026-05-31 02:15:15','2026-05-31 02:15:26','DELIVERED',NULL,'2026-05-31 09:15:14','2026-05-31 09:15:25','THr324242FEDfe'),(18,6,'Kerry',NULL,'2026-05-31 02:15:33','2026-05-31 02:15:44','DELIVERED',NULL,'2026-05-31 09:15:32','2026-05-31 09:15:43','TH42343535FED'),(19,41,'Kerry',NULL,'2026-06-09 03:20:19','2026-06-09 03:22:02','DELIVERED',NULL,'2026-06-09 10:20:19','2026-06-09 10:22:01','TH1356547TGH'),(20,43,'Kerry',NULL,NULL,'2026-06-09 03:22:08','DELIVERED',NULL,'2026-06-09 10:20:46','2026-06-09 10:22:08','TH4657576RGB3'),(21,47,'Flash',NULL,'2026-06-28 23:20:41',NULL,'SHIPPED',NULL,'2026-06-29 06:20:40','2026-06-29 06:20:49','TH53532152135grwg'),(22,55,'Flash',NULL,'2026-07-13 07:59:45','2026-07-13 08:00:32','DELIVERED',NULL,'2026-07-13 14:59:44','2026-07-13 15:00:32','TH24242ECE224'),(23,56,'Kerry',NULL,'2026-07-13 08:34:16','2026-07-13 08:34:28','DELIVERED',NULL,'2026-07-13 15:34:15','2026-07-13 15:34:28','TH2424FDECCW'),(24,57,'Flash',NULL,'2026-07-13 10:14:43','2026-07-13 10:14:58','DELIVERED',NULL,'2026-07-13 17:14:42','2026-07-13 17:14:57','TH23124CECEWQ'),(25,58,'Kerry',NULL,'2026-07-13 11:01:08','2026-07-13 11:01:20','DELIVERED',NULL,'2026-07-13 18:01:08','2026-07-13 18:01:19','TH343FECE233'),(26,59,'Flash',NULL,'2026-07-13 11:05:48','2026-07-13 11:05:50','DELIVERED',NULL,'2026-07-13 18:05:47','2026-07-13 18:06:01','TH23423432FVEE'),(27,60,'Kerry',NULL,'2026-07-18 00:54:06','2026-07-18 00:54:22','DELIVERED',NULL,'2026-07-18 07:54:06','2026-07-18 07:54:21','TH2424FEF24'),(28,62,'Flash',NULL,'2026-07-18 02:12:19','2026-07-18 02:12:21','DELIVERED',NULL,'2026-07-18 09:12:19','2026-07-18 09:12:28','TH3r13FEWFW'),(29,61,'Kerry',NULL,'2026-07-18 02:12:33','2026-07-18 02:12:35','DELIVERED',NULL,'2026-07-18 09:12:32','2026-07-18 09:12:42','TH2424FEVEVC'),(30,63,'Kerry',NULL,NULL,NULL,'PENDING',NULL,'2026-07-18 09:16:53',NULL,'THR1431341FE'),(31,64,'J&T',NULL,'2026-07-18 02:51:37','2026-07-18 02:51:39','DELIVERED',NULL,'2026-07-18 09:51:37','2026-07-18 09:51:54','TH2442FEVE32'),(32,65,'J&T',NULL,NULL,NULL,'PENDING',NULL,'2026-07-18 09:55:32',NULL,'TH24242FCWWCW');
/*!40000 ALTER TABLE `shipments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_claim_attachments`
--

DROP TABLE IF EXISTS `supplier_claim_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_claim_attachments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_claim_id` bigint NOT NULL,
  `file_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_sclaim_attach` (`supplier_claim_id`),
  CONSTRAINT `fk_sclaim_attach` FOREIGN KEY (`supplier_claim_id`) REFERENCES `supplier_claims` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_claim_attachments`
--

LOCK TABLES `supplier_claim_attachments` WRITE;
/*!40000 ALTER TABLE `supplier_claim_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier_claim_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_claims`
--

DROP TABLE IF EXISTS `supplier_claims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_claims` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `purchase_order_id` bigint NOT NULL,
  `supplier_user_id` bigint NOT NULL,
  `admin_user_id` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `product_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `claim_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `supplier_response` text COLLATE utf8mb4_unicode_ci,
  `refund_amount` decimal(12,2) DEFAULT NULL,
  `resolved_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sclaim_po` (`purchase_order_id`),
  KEY `fk_sclaim_admin` (`admin_user_id`),
  KEY `idx_sclaim_status` (`status`),
  KEY `idx_sclaim_supplier` (`supplier_user_id`),
  CONSTRAINT `fk_sclaim_admin` FOREIGN KEY (`admin_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_sclaim_po` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_sclaim_supplier` FOREIGN KEY (`supplier_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_claims`
--

LOCK TABLES `supplier_claims` WRITE;
/*!40000 ALTER TABLE `supplier_claims` DISABLE KEYS */;
INSERT INTO `supplier_claims` VALUES (1,29,4,5,26,'ผ้าเบรกหน้า BMW 3 Series',2,'RETURN_REFUND','APPROVED','สินค้าได้รับความเสียหายมีรอยบุบ','เดี๋ยวจัดการให้ครับ',3280.00,'2026-08-13 03:44:17','2026-08-13 02:19:09','2026-08-13 10:44:16','2026-08-13 10:44:17'),(2,29,4,5,26,'ผ้าเบรกหน้า BMW 3 Series',1,'RETURN_REFUND','APPROVED','มีการกระแทกเสียหาย','เดี๋ยวจัดการให้เลยครับ',1640.00,'2026-08-13 03:28:41','2026-08-13 03:14:17','2026-08-13 10:28:41','2026-08-13 10:28:41'),(3,29,4,5,26,'ผ้าเบรกหน้า BMW 3 Series',1,'REPLACEMENT','COMPLETED','อยากขอเปลี่ยนสินค้าครับมีตำหนิเล็กน้อยครับ','ได้ครับเดี๋ยวจัดการส่งให้ครับ',1640.00,'2026-08-13 10:18:43','2026-08-13 10:16:46','2026-08-13 17:18:43','2026-08-13 17:18:43');
/*!40000 ALTER TABLE `supplier_claims` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_profiles`
--

DROP TABLE IF EXISTS `supplier_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_profiles` (
  `user_id` bigint NOT NULL,
  `company_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tax_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact_name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact_email` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact_phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_text` text COLLATE utf8mb4_unicode_ci,
  `bank_account_text` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  KEY `idx_supplier_company` (`company_name`),
  CONSTRAINT `fk_supplier_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_profiles`
--

LOCK TABLES `supplier_profiles` WRITE;
/*!40000 ALTER TABLE `supplier_profiles` DISABLE KEYS */;
INSERT INTO `supplier_profiles` VALUES (4,'PK Supplier',NULL,'Supplier One','supplier@pkshop.com','0997778888','JAPAN',NULL,NULL,'2026-05-15 17:01:44');
/*!40000 ALTER TABLE `supplier_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_quotation_items`
--

DROP TABLE IF EXISTS `supplier_quotation_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_quotation_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_quotation_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quoted_unit_cost` decimal(12,2) NOT NULL,
  `qty` int NOT NULL,
  `lead_time_days` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_quote_product` (`supplier_quotation_id`,`product_id`),
  KEY `fk_qi_product` (`product_id`),
  CONSTRAINT `fk_qi_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_qi_quote` FOREIGN KEY (`supplier_quotation_id`) REFERENCES `supplier_quotations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_quotation_items`
--

LOCK TABLES `supplier_quotation_items` WRITE;
/*!40000 ALTER TABLE `supplier_quotation_items` DISABLE KEYS */;
INSERT INTO `supplier_quotation_items` VALUES (1,1,2,95.00,100,NULL,'2026-02-21 04:21:51'),(2,2,2,80.00,150,NULL,'2026-02-21 08:00:16'),(3,3,11,1200.00,30,2,'2026-03-02 09:22:32'),(4,4,11,950.00,30,2,'2026-03-02 09:42:34'),(5,5,10,3000.00,30,3,'2026-03-03 01:17:07'),(6,5,14,2700.00,15,3,'2026-03-03 01:17:07'),(7,5,15,820.00,16,3,'2026-03-03 01:17:07'),(8,5,18,520.00,15,3,'2026-03-03 01:17:07'),(9,5,21,1100.00,20,3,'2026-03-03 01:17:07'),(10,5,23,190.00,25,3,'2026-03-03 01:17:07'),(11,6,27,100.00,5,4,'2026-03-17 02:31:19'),(12,6,28,5000.00,8,4,'2026-03-17 02:31:19'),(13,6,30,550.00,20,4,'2026-03-17 02:31:19'),(14,7,24,1000.00,20,3,'2026-05-17 19:49:33'),(15,7,25,600.00,5,3,'2026-05-17 19:49:33'),(16,7,26,1600.00,3,3,'2026-05-17 19:49:33'),(17,7,27,60.00,10,3,'2026-05-17 19:49:33'),(18,7,28,4200.00,3,3,'2026-05-17 19:49:33'),(19,7,29,6500.00,4,3,'2026-05-17 19:49:33'),(20,7,30,500.01,7,3,'2026-05-17 19:49:33'),(21,8,22,600.00,10,1,'2026-05-17 20:04:02'),(22,9,16,700.00,10,1,'2026-06-07 01:38:22'),(23,9,17,2500.00,10,1,'2026-06-07 01:38:22'),(24,9,19,70.00,10,1,'2026-06-07 01:38:22'),(25,9,20,250.00,10,1,'2026-06-07 01:38:22'),(26,9,22,500.00,10,1,'2026-06-07 01:38:22'),(27,10,12,650.00,10,1,'2026-06-07 01:39:24'),(28,10,13,50.00,10,1,'2026-06-07 01:39:24'),(29,11,26,1200.00,5,1,'2026-06-10 02:46:49'),(30,11,27,20.00,11,1,'2026-06-10 02:46:49'),(31,11,28,3800.00,3,1,'2026-06-10 02:46:49'),(32,11,29,6000.00,3,1,'2026-06-10 02:46:49'),(33,11,30,300.00,8,0,'2026-06-10 02:46:49'),(34,12,12,550.00,5,1,'2026-06-10 02:48:31'),(35,12,13,20.00,5,1,'2026-06-10 02:48:31'),(36,12,15,400.00,7,1,'2026-06-10 02:48:31'),(37,12,17,1900.00,2,1,'2026-06-10 02:48:31'),(38,13,25,400.00,13,1,'2026-06-10 02:49:48'),(39,14,19,30.00,10,1,'2026-06-10 02:50:56'),(40,14,20,100.00,5,1,'2026-06-10 02:50:56'),(41,14,21,450.00,8,1,'2026-06-10 02:50:56'),(42,14,22,450.00,10,1,'2026-06-10 02:50:56'),(43,14,23,1000.00,5,1,'2026-06-10 02:50:56'),(44,14,24,900.00,7,1,'2026-06-10 02:50:56'),(45,15,26,1300.00,4,3,'2026-08-07 01:49:15'),(46,16,26,1700.00,7,2,'2026-08-13 02:06:49');
/*!40000 ALTER TABLE `supplier_quotation_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_quotations`
--

DROP TABLE IF EXISTS `supplier_quotations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_quotations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `purchase_order_id` bigint NOT NULL,
  `quotation_number` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `supplier_user_id` bigint NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SUBMITTED',
  `valid_until` date DEFAULT NULL,
  `remarks` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `quotation_number` (`quotation_number`),
  KEY `fk_quote_supplier` (`supplier_user_id`),
  KEY `idx_quote_po` (`purchase_order_id`),
  KEY `idx_quote_status` (`status`),
  CONSTRAINT `fk_quote_po` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_quote_supplier` FOREIGN KEY (`supplier_user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_quotations`
--

LOCK TABLES `supplier_quotations` WRITE;
/*!40000 ALTER TABLE `supplier_quotations` DISABLE KEYS */;
INSERT INTO `supplier_quotations` VALUES (1,1,'Q-1771672910724',4,'ACCEPTED','2026-03-15',NULL,'2026-02-21 04:21:51','2026-02-21 16:41:01'),(2,2,'Q-1771686015500',4,'ACCEPTED','2026-03-20',NULL,'2026-02-21 08:00:16','2026-02-21 16:21:47'),(3,15,'Q-1772468551969',4,'REJECTED','2026-04-02',NULL,'2026-03-02 09:22:32','2026-03-02 16:44:15'),(4,16,'Q-1772469754247',8,'ACCEPTED','2026-04-02',NULL,'2026-03-02 09:42:34','2026-03-02 16:43:51'),(5,18,'Q-1772525827345',9,'ACCEPTED','2026-04-03',NULL,'2026-03-03 01:17:07','2026-03-03 08:18:48'),(6,19,'Q-1773739878593',9,'REJECTED',NULL,NULL,'2026-03-17 02:31:19','2026-06-10 09:52:04'),(7,20,'Q-1779072573332',4,'ACCEPTED',NULL,NULL,'2026-05-17 19:49:33','2026-05-18 02:50:48'),(8,21,'Q-1779073442291',4,'ACCEPTED','2026-05-18',NULL,'2026-05-17 20:04:02','2026-05-18 03:04:33'),(9,22,'Q-1780821502243',6,'ACCEPTED','2026-07-07',NULL,'2026-06-07 01:38:22','2026-06-07 08:40:05'),(10,23,'Q-1780821564233',4,'ACCEPTED','2026-07-07',NULL,'2026-06-07 01:39:24','2026-06-07 08:40:14'),(11,26,'Q-1781084809452',8,'ACCEPTED','2026-07-10',NULL,'2026-06-10 02:46:49','2026-06-10 09:53:06'),(12,27,'Q-1781084911189',6,'ACCEPTED',NULL,NULL,'2026-06-10 02:48:31','2026-06-10 09:53:17'),(13,24,'Q-1781084987990',4,'ACCEPTED',NULL,NULL,'2026-06-10 02:49:48','2026-06-10 09:53:54'),(14,25,'Q-1781085056089',4,'ACCEPTED','2026-07-10',NULL,'2026-06-10 02:50:56','2026-06-10 09:53:32'),(15,28,'Q-1786092554954',4,'ACCEPTED','2026-09-07',NULL,'2026-08-07 01:49:15','2026-08-07 08:50:06'),(16,29,'Q-1786612008988',4,'ACCEPTED','2026-09-13',NULL,'2026-08-13 02:06:49','2026-08-13 09:08:17');
/*!40000 ALTER TABLE `supplier_quotations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_roles_role` (`role_id`),
  CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (2,1),(3,1),(5,1),(1,2),(12,2),(13,2),(14,2),(15,2),(16,2),(17,2),(4,3),(6,3),(8,3),(9,3),(10,3),(11,3),(7,4);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth_provider` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'LOCAL',
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `last_login_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_users_status` (`status`),
  KEY `idx_users_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'customer1@pkshop.com','LOCAL','$2a$10$U3B3dKvurN2B.DikFwTFgOUdyMeyex0/LMcDTtQ4ux4rQbrPXIGra','Customer One','0999999999','ACTIVE','2026-02-21 02:09:43','2026-02-10 23:52:23','2026-02-21 09:09:43'),(2,'admin@pkshop.com','LOCAL','$2a$10$YGk48NFcNmvFP3/0y8rfSOQrL5hhvSnwv6uAlKkiRwiatzVB/2APK','Admin One','0999999999','ACTIVE','2026-02-16 11:57:33','2026-02-11 00:33:02','2026-02-16 18:57:32'),(3,'admin@example.com','LOCAL','$2a$10$2Yr.VRNPBnaOH0vMqaOAcurXrLbtmDUlA/edS2rPYW.dFUJQhiwEW','Admin','0967543401','ACTIVE','2026-02-16 11:48:12','2026-02-16 18:29:46','2026-02-16 18:48:12'),(4,'supplier@pkshop.com','LOCAL','$2a$10$H2WTDkxFknqZy4QsbdtufOpKnun.uC68BCFCZMoG7ZKui.ry4SMdC','Supplier One','0997778888','ACTIVE','2026-08-15 09:08:02','2026-02-16 18:51:09','2026-08-15 16:08:01'),(5,'cus@example.com','LOCAL','$2a$10$OFbmhNJLO1par9vpZWX1IOLWoQ1PKfPmLJTqIEiOP104xefr7Mvqq','Admin','0967543401','ACTIVE','2026-08-16 05:34:37','2026-02-21 09:32:21','2026-08-16 12:34:37'),(6,'supplier_test@pkshop.com','LOCAL','$2a$10$c.dpPUjExvWUfe7vaHJiJ.BUO6Lr3Ke5T9qlhcd74/eQ56Q47k9yS','Supplier Test','0888888888','ACTIVE','2026-08-06 04:10:23','2026-02-21 09:55:58','2026-08-06 11:10:22'),(7,'customs@example.com','LOCAL','$2a$10$nekoVf1qbu6n4J2Xl1Bpa.S5IFctwalCLh6EjMDgc16RJWp.WIMsW','Sedthawut Kaewka','0986471317','ACTIVE','2026-08-13 02:13:09','2026-02-24 18:05:29','2026-08-13 09:13:08'),(8,'autoprime@pkshop.com','LOCAL','$2a$10$5HIAS/lezFyjPIKaE2DDjuw8HAI24k3wK69KalN40bUil3bnwwwa6','AutoPrime','0986471317','ACTIVE','2026-06-10 02:40:56','2026-03-02 16:38:51','2026-06-10 09:40:55'),(9,'globaldrivesupply@phshop.com','LOCAL','$2a$10$ZNX/yV.Vvs6SiLAgixjg.eWRnWV.VJzbg6FOuSypdFftzeF.qAJWi','GlobalDriveSupply','0898562345','ACTIVE','2026-03-17 02:28:39','2026-03-03 07:57:29','2026-03-17 09:28:38'),(10,'proautoparts@phshop.com','LOCAL','$2a$10$BYlo2lVPqKrmCeWCVf1LMeAORUSwIm6Ya05/4FJDSGRNzc7oSOMFC','ProMotionAutoParts','0981234567','ACTIVE',NULL,'2026-03-03 07:59:01',NULL),(11,'nexaauto@pkshop.com','LOCAL','$2a$10$mUTPFZS/4jYNVHwAdqaZYeuoMlMYebz5RjmPKguLucw2CcT8MFpFu','NexaAuto','0874561234','ACTIVE',NULL,'2026-03-03 08:00:19',NULL),(12,'keng@gmail.com','LOCAL','$2a$10$LNSGEz2zNrfU73CYTeR7F.RxkbAwRX6asMDmWlqavt8vauqYDtDS6','Sedthawut Kaewka','0986471317','ACTIVE','2026-08-16 05:28:17','2026-03-03 09:55:53','2026-08-16 12:28:16'),(13,'pond@gmail.com','LOCAL','$2a$10$OcQP7vkPgJZB/YWODWyXFO/ONiMJvxTBmOpbmNyOVA90POxirxIYy','สุกฤษฏ์ ตุดถีนนท์','0824563456','ACTIVE','2026-08-08 02:19:52','2026-05-16 07:43:11','2026-08-08 09:20:05'),(14,'nicha@gmail.com','LOCAL','$2a$10$plTqKJ/DXmoXyeCoWQO9EuRNDnl/MwZ4306EWYQp/j36Fx3w6NrtS','จันทราทิพย์ ขันขจร','0934978880','ACTIVE','2026-08-08 02:19:27','2026-06-10 07:16:18','2026-08-08 09:19:39'),(15,'kareng@gmail.com','LOCAL','$2a$10$criy7CqiCDYTJo6.HY5Xe.5EVSydrsI1pBDeUOc2BbF3w9jIJBij2','สมรวย รวยมาก','0986471317','ACTIVE','2026-08-08 02:18:38','2026-08-08 08:48:59','2026-08-08 09:18:38'),(16,'test.google@gmail.com','GOOGLE',NULL,'Google Tester',NULL,'ACTIVE','2026-08-08 03:29:42','2026-08-08 10:29:41',NULL),(17,'sk.keng2547@gmail.com','GOOGLE',NULL,'Sedthawut Kodlor',NULL,'ACTIVE','2026-08-16 05:28:44','2026-08-11 17:17:08','2026-08-16 12:28:44');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-17 22:29:46
