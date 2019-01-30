-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: 188.0.96.27    Database: msb
-- ------------------------------------------------------
-- Server version	8.0.11

 SET NAMES utf8mb4 ;

--
-- Table structure for table `shedlock`
--
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `shedlock` (
  `name` varchar(64) NOT NULL,
  `lock_until` timestamp(3) NULL DEFAULT NULL,
  `locked_at` timestamp(3) NULL DEFAULT NULL,
  `locked_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_api_group`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_api_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='api';


--
-- Table structure for table `t_api_group_item`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_api_group_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `api_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ;



--
-- Table structure for table `t_app`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(100) DEFAULT NULL,
  `app_secret` varchar(200) DEFAULT NULL COMMENT 'app秘钥',
  `app_name` varchar(100) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `link_email` varchar(100) DEFAULT NULL,
  `link_tel` varchar(20) DEFAULT NULL,
  `link_man` varchar(50) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL COMMENT '开发商',
  `status` tinyint(4) NOT NULL COMMENT '0:新增，1：生效，2：审批中，3：审批不通过',
  `create_by` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `is_created_in_sso` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_app_app_id_uindex` (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_app_audit`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_app_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(100) DEFAULT NULL,
  `app_name` varchar(100) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `link_email` varchar(100) DEFAULT NULL,
  `link_tel` varchar(20) DEFAULT NULL,
  `link_man` varchar(50) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL COMMENT '开发商',
  `status` tinyint(4) DEFAULT NULL COMMENT '0:新增，1：生效，2：审批中，3：审批不通过',
  `audit_result` varchar(10) DEFAULT NULL,
  `reason` varchar(200) DEFAULT NULL,
  `apply_date` datetime NOT NULL,
  `apply_by` varchar(50) DEFAULT NULL,
  `audit_date` datetime DEFAULT NULL,
  `audit_by` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_app_order_api`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_app_order_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `api_id` int(11) NOT NULL COMMENT 'type:0 apiId, type:1 api组id',
  `status` int(1) NOT NULL COMMENT ' 0:订阅申请中、1:订阅申请不通过、2:已订阅、3:已退订',
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30000 DEFAULT CHARSET=utf8 COMMENT='应用订阅api';


--
-- Table structure for table `t_app_order_api_audit`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_app_order_api_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `app_id` int(11) NOT NULL,
  `api_id` int(11) NOT NULL COMMENT 'type:0 apiId, type:1 api组id',
  `apply_type` tinyint(4) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `apply_date` datetime DEFAULT NULL,
  `apply_by` varchar(50) DEFAULT NULL,
  `audit_date` datetime DEFAULT NULL,
  `audit_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `reason` varchar(200) DEFAULT NULL,
  `audit_result` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='应用订阅api';


--
-- Table structure for table `t_breaker_strategy`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_breaker_strategy` (
  `enable_breaker` int(11) DEFAULT NULL,
  `request_volume` int(11) DEFAULT NULL,
  `sleep` int(11) DEFAULT NULL,
  `fail_rate` int(11) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `is_default` int(11) DEFAULT NULL,
  `strategy_name` varchar(100) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_breaker_strategy_api`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_breaker_strategy_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `strategy_id` int(11) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_data_org`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_data_org` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_name` varchar(50) DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `parent_list` varchar(500) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='数据权限组';


--
-- Table structure for table `t_data_org_user`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_data_org_user` (
  `org_id` int(11) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `main_flag` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据组与用户关系表';


--
-- Table structure for table `t_error_code`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_error_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(100) DEFAULT NULL,
  `code` varchar(11) NOT NULL,
  `message` varchar(500) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_error_code_code_uindex` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_gateway_monitor`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_gateway_monitor` (
  `id` int(11) NOT NULL,
  `count` int(11) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `route_time` int(11) DEFAULT NULL,
  `route_count` int(11) DEFAULT NULL,
  `requst_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_module`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_module` (
  `id` int(11) unsigned NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `module_name` varchar(50) NOT NULL,
  `m_key` varchar(100) NOT NULL,
  `apis` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `status` varchar(10) NOT NULL DEFAULT '0',
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='权限模块';


--
-- Table structure for table `t_org`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_org` (
  `ORGID` int(11) NOT NULL AUTO_INCREMENT,
  `ORGCODE` varchar(100) DEFAULT NULL,
  `ORGNAME` varchar(200) NOT NULL,
  `ORGLEVEL` int(11) DEFAULT NULL,
  `PARENTORGID` int(11) DEFAULT NULL,
  `ORGSEQ` varchar(100) DEFAULT NULL,
  `ORGADDR` varchar(200) DEFAULT NULL,
  `LINKMAN` varchar(50) DEFAULT NULL,
  `LINKTEL` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `AREA` varchar(20) DEFAULT NULL,
  `ORGSELTYPE` varchar(10) DEFAULT NULL,
  `ISDW` varchar(10) DEFAULT NULL,
  `DWORGTYPE` varchar(20) DEFAULT NULL,
  `DWORGCODE` varchar(20) DEFAULT NULL,
  `SPECIALTY` varchar(30) DEFAULT NULL,
  `DWORGSUBTYPE` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ORGID`)
) ENGINE=InnoDB AUTO_INCREMENT=100000000 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_rate_limit_strategy`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_rate_limit_strategy` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NULL,
  `object_id` int(11) DEFAULT NULL,
  `object_path` varchar(100) DEFAULT NULL,
  `object_name` varchar(100) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `second_limit` int(11) DEFAULT NULL,
  `minute_limit` int(11) DEFAULT NULL,
  `hour_limit` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_role`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_role_auth`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_role_auth` (
  `role_id` int(11) NOT NULL,
  `auth_id` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_service`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(50) NOT NULL COMMENT 'ID',
  `service_name` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `service_secret` varchar(100) DEFAULT NULL,
  `service_code` varchar(4) DEFAULT NULL,
  `refresh_date` datetime DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_service_service_id_uindex` (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_service_api`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_service_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(100) NOT NULL,
  `api_name` varchar(100) NOT NULL,
  `path` varchar(100) NOT NULL,
  `method` varchar(10) NOT NULL COMMENT 'GET/POST/PUT/DELETE/HEAD',
  `status` tinyint(4) NOT NULL COMMENT '0:1:2:34:56',
  `is_annoymous_access` tinyint(1) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(45) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueKey` (`service_id`,`path`,`method`)
) ENGINE=InnoDB AUTO_INCREMENT=30000 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_service_api_audit`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_service_api_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `api_id` int(11) DEFAULT NULL,
  `service_id` varchar(100) NOT NULL,
  `api_name` varchar(100) DEFAULT NULL,
  `path` varchar(100) NOT NULL,
  `method` varchar(10) DEFAULT NULL COMMENT 'GET/POST/PUT/DELETE/HEAD',
  `status` tinyint(4) NOT NULL COMMENT '0:新增，1:申请上线，2:已上线，3：上线审批不通过，4:申请下线，5：下线，6：下线审批不通过',
  `audit_result` varchar(10) DEFAULT NULL COMMENT '0: 通过，1：不通过',
  `reason` varchar(200) DEFAULT NULL COMMENT '审批不通过的原因',
  `apply_type` tinyint(4) DEFAULT NULL COMMENT '0: 申请上线 1：申请下线',
  `apply_date` datetime DEFAULT NULL,
  `audit_date` datetime DEFAULT NULL,
  `audit_by` varchar(50) DEFAULT NULL,
  `apply_by` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='api审批';

--
-- Table structure for table `t_service_breaker`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_service_breaker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_key` varchar(200) DEFAULT NULL,
  `property_value` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `strategy_id` int(11) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3000 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_service_log`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_service_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL COMMENT '0：注册，1：取消',
  `service_id` varchar(100) NOT NULL,
  `content` varchar(300) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_service_route`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_service_route` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(100) NOT NULL,
  `path` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_service_route_path_uindex` (`path`)
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_user`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL,
  `is_lock` tinyint(1) NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `login_id` varchar(64) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `person_card_no` varchar(18) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `mobile` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 ;


--
-- Table structure for table `t_user_role`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
-- Dump completed on 2018-11-12 11:21:22
