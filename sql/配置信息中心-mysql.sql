CREATE DATABASE  IF NOT EXISTS `xdiamond` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `xdiamond`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 192.168.1.166    Database: xdiamond
-- ------------------------------------------------------
-- Server version	5.6.23-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profileId` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  `lastVersionValue` mediumtext,
  `description` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `createUser` varchar(255) DEFAULT NULL,
  `updateUser` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `profileId_key` (`profileId`,`key`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (43,14,'jdbc.driverClassName','com.mysql.jdbc.Driver','com.mysql.jdbc.Driver','mysql驱动类','2016-05-07 11:10:10','2016-05-07 11:10:24','admin','admin',1),(44,14,'jdbc.url','jdbc:mysql://192.168.147.128:3306/ggy?characterEncoding=UTF-8',NULL,'mysql的链接地址','2016-05-07 11:10:53',NULL,'admin',NULL,0),(45,14,'jdbc.username','root',NULL,'管理员名称','2016-05-07 11:11:09',NULL,'admin',NULL,0),(46,14,'jdbc.password','root',NULL,'管理员密码','2016-05-07 11:11:19',NULL,'admin',NULL,0),(53,17,'dubbo.service.version','1.7',NULL,'服务发布的版本号','2016-05-07 13:33:38',NULL,'admin',NULL,0),(54,21,'druid.pool.filters','stat*',NULL,'配置监控统计拦截的filters','2016-05-07 13:44:07',NULL,'admin',NULL,0),(55,21,'druid.pool.initialSize','3',NULL,'配置初始化大小','2016-05-07 13:44:29',NULL,'admin',NULL,0),(56,21,'druid.pool.maxActive','20',NULL,'配置链接最大数','2016-05-07 13:45:11',NULL,'admin',NULL,0),(57,21,'druid.pool.maxOpenPreparedStatements','20',NULL,'打开PSCache，并且指定每个连接上PSCache的大小（Oracle使用）','2016-05-07 13:45:38',NULL,'admin',NULL,0),(58,21,'druid.pool.maxWait','60000','60000','配置获取连接等待超时的时间，单位是毫秒','2016-05-07 13:46:00','2016-05-07 13:46:37','admin','admin',1),(59,21,'druid.pool.minEvictableIdleTimeMillis','300000',NULL,'配置一个连接在池中最小生存的时间，单位是毫秒','2016-05-07 13:46:24',NULL,'admin',NULL,0),(60,21,'druid.pool.minIdle','3','3','配置初始化最小链接数','2016-05-07 13:46:54','2016-05-07 13:47:16','admin','admin',1),(61,21,'druid.pool.poolPreparedStatements','true',NULL,'打开PSCache，并且指定每个连接上PSCache的大小（Oracle使用）','2016-05-07 13:47:36',NULL,'admin',NULL,0),(62,21,'druid.pool.testOnBorrow','false',NULL,NULL,'2016-05-07 13:47:54',NULL,'admin',NULL,0),(63,21,'druid.pool.testOnReturn','false',NULL,NULL,'2016-05-07 13:48:10',NULL,'admin',NULL,0),(64,21,'druid.pool.testWhileIdle','true',NULL,NULL,'2016-05-07 13:48:23',NULL,'admin',NULL,0),(65,21,'druid.pool.timeBetweenEvictionRunsMillis','60000',NULL,'配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒','2016-05-07 13:48:45',NULL,'admin',NULL,0),(66,21,'druid.pool.validationQuery','SELECT \'x\'',NULL,'验证','2016-05-07 13:49:04',NULL,'admin',NULL,0),(68,43,'ecm.domain','DCMTest',NULL,'ecm域名称','2016-05-07 14:26:30',NULL,'admin',NULL,0),(70,43,'ecm.server.password','123123',NULL,'ecm服务器管理员密码','2016-05-07 14:27:11',NULL,'admin',NULL,0),(71,43,'ecm.server.site','http://192.168.200.25\\:9080/wsi/FNCEWS40MTOM/',NULL,'ecm服务地址','2016-05-07 14:27:33',NULL,'admin',NULL,0),(72,43,'ecm.server.username','ceadmin',NULL,'ecm服务器管理员名称','2016-05-07 14:27:50',NULL,'admin',NULL,0),(74,43,'ecm.storageareaid','{B0174B54-0000-CA1A-9B08-F90500E6CDFB}',NULL,'数据存储域id','2016-05-07 14:28:28',NULL,'admin',NULL,0),(75,43,'ecm.targetobjectStore','NewOS',NULL,'ecm目标库','2016-05-07 14:28:45',NULL,'admin',NULL,0),(76,41,'mobile.fileStore.folderName','mobile',NULL,'移动设备在项目中发微作上传的文件存储文件夹名称','2016-05-07 14:29:36',NULL,'admin',NULL,0),(77,41,'ecm.jaas','FileNetP8WSI',NULL,'安全验证','2016-05-07 14:30:08',NULL,'admin',NULL,0),(79,41,'ecm.solution.prefix','GZPI',NULL,'解决方案前缀','2016-05-07 14:32:19',NULL,'admin',NULL,0),(81,41,'project.material.defaultDocument','GZPI_FILE_XMZL',NULL,'项目资料字段配置','2016-05-07 14:34:05',NULL,'admin',NULL,0),(82,41,'project.material.field.Business','GZPI_Business','GZPI_Business','业务类型字段配置','2016-05-07 14:34:24','2016-05-07 14:34:42','admin','admin',1),(83,41,'project.material.field.FileType','GZPI_FileType',NULL,'文种类型字段','2016-05-07 14:35:10',NULL,'admin',NULL,0),(84,41,'project.material.field.Organization','GZPI_Organization',NULL,'所属组织字段配置','2016-05-07 14:35:36',NULL,'admin',NULL,0),(85,41,'project.material.field.Region','GZPI_Region',NULL,'所属区域字段配置','2016-05-07 14:35:59',NULL,'admin',NULL,0),(86,41,'search.MaxRecords','500',NULL,'分页查询配置，查询最大记录数','2016-05-07 14:36:15',NULL,'admin',NULL,0),(87,41,'search.pageSize','20',NULL,'分页查询配置，分页查询每页大小','2016-05-07 14:36:31',NULL,'admin',NULL,0),(88,43,'wz.root.FolderId','{F9111E61-BC25-4996-8872-93D86FD867F7}',NULL,'微作根文件夹id','2016-05-07 14:37:03',NULL,'admin',NULL,0),(89,41,'wz.root.path','/微作',NULL,'微作根目录','2016-05-07 14:37:24',NULL,'admin',NULL,0),(90,47,'jdbc.driverClassName','oracle.jdbc.driver.OracleDriver',NULL,'驱动类名称','2016-05-07 14:42:21',NULL,'admin',NULL,0),(91,47,'jdbc.url','jdbc:oracle:thin:@192.168.200.25:1521:orcl','jdbc:oracle:thin:@192.168.200.25:1521:orcl','数据库连接地址','2016-05-07 14:42:38','2016-05-07 15:29:51','admin','admin',2),(92,47,'jdbc.username','ggy',NULL,'用户名称','2016-05-07 14:43:03',NULL,'admin',NULL,0),(93,47,'jdbc.password','pass',NULL,'用户密码','2016-05-07 14:43:13',NULL,'admin',NULL,0),(94,49,'ldap.bn','dc=dist',NULL,'basename','2016-05-07 14:46:20',NULL,'admin',NULL,0),(95,51,'ldap.bindDn','cn=root',NULL,'获取连接ldap的用户名','2016-05-07 14:47:28',NULL,'admin',NULL,0),(96,51,'ldap.password','tdsadmin',NULL,'登录ldap密码','2016-05-07 14:48:03',NULL,'admin',NULL,0),(97,51,'ldap.url','192.168.200.25',NULL,'ldap服务器地址','2016-05-07 14:50:22',NULL,'admin',NULL,0),(98,49,'ldap.port','389',NULL,'ldap默认端口','2016-05-07 14:50:47',NULL,'admin',NULL,0),(99,15,'mode','debug',NULL,'调试模式','2016-05-07 14:54:59',NULL,'admin',NULL,0),(100,19,'dubbo.monitor.address','192.168.1.166:6060',NULL,'服务监控中心地址','2016-05-07 15:38:03',NULL,'admin',NULL,0),(102,7,'dubbo.registry.address','192.168.1.166:2181,192.168.1.166:2182,192.168.1.166:2183',NULL,'dubbo服务注册中心（zookeeper集群地址）','2016-05-07 15:42:00',NULL,'admin',NULL,0);
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependency`
--

DROP TABLE IF EXISTS `dependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dependency` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `dependencyProjectId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectId_dependencyProjectId` (`projectId`,`dependencyProjectId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependency`
--

LOCK TABLES `dependency` WRITE;
/*!40000 ALTER TABLE `dependency` DISABLE KEYS */;
INSERT INTO `dependency` VALUES (7,5,2),(12,12,6),(16,14,4),(9,14,5),(10,14,11),(11,14,12),(13,14,13),(14,15,2),(15,15,4),(18,15,5);
/*!40000 ALTER TABLE `dependency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupName` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,'admin','admin');
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_roles`
--

DROP TABLE IF EXISTS `group_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_roles` (
  `groupId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`groupId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_roles`
--

LOCK TABLES `group_roles` WRITE;
/*!40000 ALTER TABLE `group_roles` DISABLE KEYS */;
INSERT INTO `group_roles` VALUES (1,1);
/*!40000 ALTER TABLE `group_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permissionStr` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission` (`permissionStr`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'admin','admin'),(2,'*','admin');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `access` int(11) NOT NULL DEFAULT '10',
  `secretKey` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectId_profileName` (`projectId`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile`
--

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` VALUES (5,2,'base',40,NULL,NULL,NULL,NULL),(6,2,'test',30,NULL,NULL,NULL,NULL),(7,2,'dev',30,NULL,NULL,NULL,NULL),(8,2,'product',40,'Cu9TdQ8qO1Kyjfyb',NULL,NULL,NULL),(13,4,'base',40,NULL,NULL,NULL,NULL),(14,4,'test',30,NULL,NULL,NULL,NULL),(15,4,'dev',30,NULL,NULL,NULL,NULL),(16,4,'product',40,'5ZpcwhsLbQsv4Nds',NULL,NULL,NULL),(17,5,'base',40,NULL,NULL,NULL,NULL),(18,5,'test',30,NULL,NULL,NULL,NULL),(19,5,'dev',30,NULL,NULL,NULL,NULL),(20,5,'product',40,'vzJrhZGfdeC7YyKV',NULL,NULL,NULL),(21,6,'base',40,NULL,NULL,NULL,NULL),(22,6,'test',30,NULL,NULL,NULL,NULL),(23,6,'dev',30,NULL,NULL,NULL,NULL),(24,6,'product',40,'UcljzIhct3U72z1B',NULL,NULL,NULL),(41,11,'base',40,NULL,NULL,NULL,NULL),(42,11,'test',30,NULL,NULL,NULL,NULL),(43,11,'dev',30,NULL,NULL,NULL,NULL),(44,11,'product',40,'Ychc2US0XzNgb1fm',NULL,NULL,NULL),(45,12,'base',40,NULL,NULL,NULL,NULL),(46,12,'test',30,NULL,NULL,NULL,NULL),(47,12,'dev',30,NULL,NULL,NULL,NULL),(48,12,'product',40,'T00zWkAeee6NepJI',NULL,NULL,NULL),(49,13,'base',40,NULL,NULL,NULL,NULL),(50,13,'test',30,NULL,NULL,NULL,NULL),(51,13,'dev',30,NULL,NULL,NULL,NULL),(52,13,'product',40,'AvH2h2JQFy7l6vlN',NULL,NULL,NULL),(53,14,'base',40,NULL,NULL,NULL,NULL),(54,14,'test',30,NULL,NULL,NULL,NULL),(55,14,'dev',30,NULL,NULL,NULL,NULL),(56,14,'product',40,'DYoYwavi0XpTj4Zx',NULL,NULL,NULL),(57,15,'base',40,NULL,NULL,NULL,NULL),(58,15,'test',30,NULL,NULL,NULL,NULL),(59,15,'dev',30,NULL,NULL,NULL,NULL),(60,15,'product',40,'YgzxyE7t1u0AUtqf',NULL,NULL,NULL);
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` varchar(255) NOT NULL,
  `artifactId` varchar(255) NOT NULL,
  `version` varchar(255) NOT NULL,
  `ownerGroup` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `bPublic` int(11) DEFAULT '1',
  `bAllowDependency` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupId_artifactId_version` (`groupId`,`artifactId`,`version`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (2,'com.dist','distconf-zookeeper','1.0.0',1,'zookeeper配置',1,1),(4,'com.dist','distconf-common','1.0.0',1,'通用配置',1,1),(5,'com.dist','distconf-dubbo','1.0.0',1,'dubbo服务的配置信息',1,1),(6,'com.dist','distconf-druid','1.0.0',1,'druid链接池配置',1,1),(11,'com.dist','distconf-ecm','1.0.0',1,'ecm配置',1,1),(12,'com.dist','distconf-datasource','1.0.0',1,'数据源配置',1,1),(13,'com.dist','distconf-ldap','1.0.0',1,'LDAP配置',1,1),(14,'com.dist','distconf-provider','1.0.0',1,'服务的提供者配置',1,0),(15,'com.dist','distconf-consumer','1.0.0',1,'服务消费者配置',1,0);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin','admin，管理员');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permissions` (
  `roleId` int(11) NOT NULL,
  `permissionId` int(11) NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `passwordSalt` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `loginCount` int(11) DEFAULT '0',
  `provider` varchar(255) DEFAULT NULL COMMENT 'ldap, oauth2...',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userName` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin','8659c1f36193d2f8f062a4afe44c0d71f2a28d6997fe3207247f9485a8c5b5efa3c34f329e787be310cb31f4f1adc370057b538c4692838d3126cc544f53e369','djWHvtVYFI5pA5OviK1278joLYBXqnxM','admin@dist.com','2015-06-05 16:25:17',NULL,NULL,NULL,'standard');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_groups`
--

DROP TABLE IF EXISTS `user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_groups` (
  `userId` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  `access` int(11) NOT NULL DEFAULT '10',
  PRIMARY KEY (`userId`,`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups`
--

LOCK TABLES `user_groups` WRITE;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
INSERT INTO `user_groups` VALUES (1,1,50);
/*!40000 ALTER TABLE `user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-07 16:39:21
