/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : ofbiztenant

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2011-12-01 17:18:43
*/

drop database IF EXISTS ofbiztenant;

CREATE DATABASE IF NOT EXISTS ofbiztenant;

USE ofbiztenant;

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `tenant`
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant` (
  `TENANT_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TENANT_NAME` varchar(100) collate latin1_general_cs default NULL,
  `DOMAIN_NAME` varchar(255) collate latin1_general_cs default NULL,
  `INITIAL_PATH` varchar(255) collate latin1_general_cs default NULL,
  `DISABLED` char(1) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TENANT_ID`),
  KEY `TENANT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TENANT_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of tenant
-- ----------------------------
INSERT INTO `tenant` VALUES ('DEMO1', 'Demo Tenant One', null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `tenant` VALUES ('DEMO2', 'Demo Tenant Two', null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `tenant_data_source`
-- ----------------------------
DROP TABLE IF EXISTS `tenant_data_source`;
CREATE TABLE `tenant_data_source` (
  `TENANT_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ENTITY_GROUP_NAME` varchar(100) collate latin1_general_cs NOT NULL,
  `JDBC_URI` varchar(255) collate latin1_general_cs default NULL,
  `JDBC_USERNAME` varchar(255) collate latin1_general_cs default NULL,
  `JDBC_PASSWORD` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TENANT_ID`,`ENTITY_GROUP_NAME`),
  KEY `TNTDTSRC_TNT` (`TENANT_ID`),
  KEY `TNNT_DT_SRC_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TNNT_DT_SRC_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `TNTDTSRC_TNT` FOREIGN KEY (`TENANT_ID`) REFERENCES `tenant` (`TENANT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of tenant_data_source
-- ----------------------------
INSERT INTO `tenant_data_source` VALUES ('DEMO1', 'org.ofbiz', 'jdbc:derby:ofbiz_DEMO1;create=true', 'ofbiz', 'ofbiz', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `tenant_data_source` VALUES ('DEMO1', 'org.ofbiz.olap', 'jdbc:derby:ofbizolap_DEMO1;create=true', 'ofbiz', 'ofbiz', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `tenant_data_source` VALUES ('DEMO2', 'org.ofbiz', 'jdbc:derby:ofbiz_DEMO2;create=true', 'ofbiz', 'ofbiz', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `tenant_data_source` VALUES ('DEMO2', 'org.ofbiz.olap', 'jdbc:derby:ofbizolap_DEMO2;create=true', 'ofbiz', 'ofbiz', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
