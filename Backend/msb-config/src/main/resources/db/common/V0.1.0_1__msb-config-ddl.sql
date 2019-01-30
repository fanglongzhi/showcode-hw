--
-- Table structure for table `t_config`
--

-- use msb;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `application` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `profile` varchar(20) CHARACTER SET utf8mb4  DEFAULT NULL,
  `label` varchar(20) CHARACTER SET utf8mb4  DEFAULT NULL,
  `property_key` varchar(1024) CHARACTER SET utf8mb4  DEFAULT NULL,
  `property_value` varchar(2048) CHARACTER SET utf8mb4  DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `t_service_info`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_service_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(50) NOT NULL COMMENT 'ID',
  `service_name` varchar(50) DEFAULT NULL,
  `service_secret` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_service_service_id_uindex` (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4;