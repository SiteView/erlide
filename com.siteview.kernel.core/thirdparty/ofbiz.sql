/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : ofbiz

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2011-12-01 17:18:31
*/

DROP DATABASE IF EXISTS OFBIZ;

CREATE DATABASE OFBIZ;

USE OFBIZ;

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `alert_log`
-- ----------------------------
DROP TABLE IF EXISTS `alert_log`;
CREATE TABLE `alert_log` (
  `ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ALERT_ID` varchar(255) collate latin1_general_cs NOT NULL,
  `ALERTTYPE` varchar(20) collate latin1_general_cs default NULL,
  `NAME` varchar(255) collate latin1_general_cs default NULL,
  `MONITOR` varchar(255) collate latin1_general_cs default NULL,
  `RECEIVER` varchar(255) collate latin1_general_cs default NULL,
  `TITLE` varchar(255) collate latin1_general_cs default NULL,
  `ALERTTIME` datetime default NULL,
  `RESULT` varchar(255) collate latin1_general_cs default NULL,
  `ALERT_LEVEL` varchar(255) collate latin1_general_cs default NULL,
  `CONTENT` longtext collate latin1_general_cs,
  `GROUPID` varchar(255) collate latin1_general_cs default NULL,
  `RESPONSETIME` datetime default NULL,
  `RESPONDER` varchar(255) collate latin1_general_cs default NULL,
  `RESPONSECONTENT` varchar(255) collate latin1_general_cs default NULL,
  `CLEARTIME` datetime default NULL,
  `TIMES` varchar(10) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ID`,`ALERT_ID`),
  KEY `ALERT_LOG_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ALERT_LOG_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of alert_log
-- ----------------------------

-- ----------------------------
-- Table structure for `browser_type`
-- ----------------------------
DROP TABLE IF EXISTS `browser_type`;
CREATE TABLE `browser_type` (
  `BROWSER_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `BROWSER_NAME` varchar(100) collate latin1_general_cs default NULL,
  `BROWSER_VERSION` varchar(10) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`BROWSER_TYPE_ID`),
  KEY `BRWSR_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `BRWSR_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of browser_type
-- ----------------------------

-- ----------------------------
-- Table structure for `c_i`
-- ----------------------------
DROP TABLE IF EXISTS `c_i`;
CREATE TABLE `c_i` (
  `CI_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `APP` varchar(255) collate latin1_general_cs default NULL,
  `CI_TYPE_ID` varchar(60) collate latin1_general_cs default NULL,
  `CI_NAME` varchar(100) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CI_ID`),
  KEY `CI_TYPE` (`CI_TYPE_ID`),
  KEY `C_I_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `C_I_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CI_TYPE` FOREIGN KEY (`CI_TYPE_ID`) REFERENCES `c_i_type` (`CI_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of c_i
-- ----------------------------

-- ----------------------------
-- Table structure for `c_i_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `c_i_attribute`;
CREATE TABLE `c_i_attribute` (
  `CI_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ATTR_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `ATTR_VALUE` varchar(255) collate latin1_general_cs default NULL,
  `ATTR_OBJ_VALUE` longblob,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CI_ID`,`ATTR_NAME`),
  KEY `CI_ATTR` (`CI_ID`),
  KEY `C_I_ATTRBT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `C_I_ATTRBT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CI_ATTR` FOREIGN KEY (`CI_ID`) REFERENCES `c_i` (`CI_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of c_i_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `c_i_type`
-- ----------------------------
DROP TABLE IF EXISTS `c_i_type`;
CREATE TABLE `c_i_type` (
  `CI_TYPE_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(60) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CI_TYPE_ID`),
  KEY `CI_TYPPAR` (`PARENT_TYPE_ID`),
  KEY `C_I_TYPE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `C_I_TYPE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CI_TYPPAR` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `c_i_type` (`CI_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of c_i_type
-- ----------------------------

-- ----------------------------
-- Table structure for `c_i_type_attr`
-- ----------------------------
DROP TABLE IF EXISTS `c_i_type_attr`;
CREATE TABLE `c_i_type_attr` (
  `CI_TYPE_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `ATTR_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CI_TYPE_ID`,`ATTR_NAME`),
  KEY `CI_TYPATTR` (`CI_TYPE_ID`),
  KEY `C_I_TP_ATTR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `C_I_TP_ATTR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CI_TYPATTR` FOREIGN KEY (`CI_TYPE_ID`) REFERENCES `c_i_type` (`CI_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of c_i_type_attr
-- ----------------------------

-- ----------------------------
-- Table structure for `catalina_session`
-- ----------------------------
DROP TABLE IF EXISTS `catalina_session`;
CREATE TABLE `catalina_session` (
  `SESSION_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `SESSION_SIZE` decimal(20,0) default NULL,
  `SESSION_INFO` longblob,
  `IS_VALID` char(1) collate latin1_general_cs default NULL,
  `MAX_IDLE` decimal(20,0) default NULL,
  `LAST_ACCESSED` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`SESSION_ID`),
  KEY `CTLN_SSSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CTLN_SSSN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of catalina_session
-- ----------------------------

-- ----------------------------
-- Table structure for `country_capital`
-- ----------------------------
DROP TABLE IF EXISTS `country_capital`;
CREATE TABLE `country_capital` (
  `COUNTRY_CODE` varchar(20) collate latin1_general_cs NOT NULL,
  `COUNTRY_CAPITAL` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`COUNTRY_CODE`),
  KEY `CNTRY_CAP_TO_CODE` (`COUNTRY_CODE`),
  KEY `CNTR_CPTL_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CNTR_CPTL_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CNTRY_CAP_TO_CODE` FOREIGN KEY (`COUNTRY_CODE`) REFERENCES `country_code` (`COUNTRY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of country_capital
-- ----------------------------
INSERT INTO `country_capital` VALUES ('AD', 'Andorra', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AE', 'Abu Dhabi', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AF', 'Kabul', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AG', 'St. Johns', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AI', 'The Valley', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AL', 'Tiran', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AM', 'Yerevan', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AN', 'Willemstad', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AO', 'Luanda', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AQ', 'None', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AR', 'Buenos Aires', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AS', 'Pago Pago', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AT', 'Vienna', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AU', 'Canberra', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AW', 'Oranjestad', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('AZ', 'Baku', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BA', 'Sarajevo', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BB', 'Bridgetown', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BD', 'Dhaka', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BE', 'Brussels', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BF', 'Ouagadougou', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BG', 'Sofia', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BH', 'Al-Manamah', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BI', 'Bujumbura', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BJ', 'Porto-Novo', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BM', 'Hamilton', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BN', 'Bandar Seri Begawan', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BO', 'La Paz', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BR', 'Brasilia', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BS', 'Nassau', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BT', 'Thimphu', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BV', 'None', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BW', 'Gaborone', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BY', 'Minsk', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('BZ', 'Belmopan', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CA', 'Ottawa', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CC', 'West Island', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CD', 'Kinshasa', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CF', 'Bangui', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CG', 'Brazzaville', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CH', 'Bern', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CI', 'Abidjan', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CK', 'Avarua', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CL', 'Santiago', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CM', 'Yaounde', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CN', 'Beijing', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CO', 'Bogota', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CR', 'San Jose', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CU', 'Havana', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CV', 'Praia', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CX', 'The Settlement', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CY', 'Nicosia', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('CZ', 'Prague', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('DE', 'Berlin', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('DJ', 'Djibouti', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('DK', 'Copenhagen', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('DM', 'Roseau', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('DO', 'Santo Domingo', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('DZ', 'Algiers', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('EC', 'Quito', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('EE', 'Tallinn', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('EG', 'Cairo', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('EH', 'El Aaiun', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ENG', 'London', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ER', 'Asmara', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ES', 'Madrid', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ET', 'Addis Ababa', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('FI', 'Helsinki', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('FJ', 'Suva', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('FK', 'Stanley', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('FM', 'Palikir', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('FO', 'Torshavn', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('FR', 'Paris', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GA', 'Libreville', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GB', 'London', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GD', 'St. George\'s', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GE', 'Tbilisi', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GF', 'Cayenne', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GH', 'Accra', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GI', 'Gibraltar', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GL', 'Godthab', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GM', 'Banjul', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GN', 'Conakry', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GP', 'Basse-Terre', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GQ', 'Malabo', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GR', 'Athens', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GS', 'None', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GT', 'Guatemala City', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GU', 'Agana', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GW', 'Bissau', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('GY', 'Georgetown', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('HK', 'Victoria', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('HM', 'None', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('HN', 'Tegucigalpa', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('HR', 'Zagreb', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('HT', 'Port-au-Prince', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('HU', 'Budapest', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ID', 'Jakarta', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IE', 'Dublin', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IL', 'Jerusalem', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IN', 'New Delhi', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IO', 'None', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IQ', 'Baghdad', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IR', 'Tehran', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IS', 'Reykjavik', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('IT', 'Rome', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('JM', 'Kingston', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('JO', 'Amman', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('JP', 'Tokyo', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KE', 'Nairobi', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KG', 'Bishkek', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KH', 'Phnom Penh', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KI', 'Tarawa', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KM', 'Moroni', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KN', 'Basseterre', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KP', 'Pyongyang', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KR', 'Seoul', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KW', 'Kuwait City', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KY', 'Georgetown', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('KZ', 'Astana', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LA', 'Vientiane', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LB', 'Beirut', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LC', 'Castries', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LI', 'Vaduz', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LK', 'Colombo', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LR', 'Monrovia', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LS', 'Maseru', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LT', 'Vilnius', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LU', 'Luxembourg', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LV', 'Riga', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('LY', 'Tripoli', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MA', 'Rabat', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MC', 'Monaco', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MD', 'Kishinev', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MG', 'Antananarivo', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MH', 'Majuro', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MK', 'Skopje', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ML', 'Bamako', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MM', 'Rangoon', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MN', 'Ulan Bator', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MO', 'Macau', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MP', 'Saipan', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MQ', 'Fort-de-France', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MR', 'Nouakchott', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MS', 'Plymouth', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MT', 'Valletta', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MU', 'Port Louis', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MV', 'Male', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MW', 'Lilongwe', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MX', 'Mexico City', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MY', 'Kuala Lumpur', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('MZ', 'Maputo', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NA', 'Windhoek', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NC', 'Noumea', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NE', 'Niamey', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NF', 'Kingston', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NG', 'Lagos', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NI', 'Managua', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NIR', 'Belfast', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NL', 'Amsterdam', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NO', 'Oslo', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NP', 'Kathmandu', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NR', 'Yaren', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NU', 'Alofi', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('NZ', 'Wellington', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('OM', 'Muscat', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PA', 'Panama City', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PE', 'Lima', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PF', 'Papeete', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PG', 'Port Moresby', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PH', 'Manila', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PK', 'Islamabad', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PL', 'Warsaw', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PM', 'St. Pierre', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PN', 'Adamstown', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PR', 'San Juan', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PT', 'Lisbon', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PW', 'Koror', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('PY', 'Asuncion', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('QA', 'Doha', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('RE', 'Saint-Denis', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('RO', 'Bucharest', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('RU', 'Moscow', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('RW', 'Kigali', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SA', 'Riyadh', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SB', 'Honiara', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SC', 'Victoria', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SCT', 'Edinburgh', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SD', 'Khartoum', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SE', 'Stockholm', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SG', 'Singapore', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SH', 'Jamestown', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SI', 'Ljubljana', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SJ', 'Longyearbyen', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SK', 'Bratislava', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SL', 'Freetown', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SM', 'San Marino', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SN', 'Dakar', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SO', 'Mogadishu', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SR', 'Paramaribo', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ST', 'Sao Tome', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SV', 'San Salvador', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SY', 'Damascus', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('SZ', 'Mbabane', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TC', 'Grand Turk', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TD', 'N\'Djamena', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TF', 'None', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TG', 'Lome', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TH', 'Bangkok', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TJ', 'Dushanbe', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TK', 'None', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TM', 'Ashgabat', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TN', 'Tunis', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TO', 'Nuku\'alofa', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TP', 'Dili', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TR', 'Ankara', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TT', 'Port of Spain', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TV', 'Funafuti', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TW', 'Taipei', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('TZ', 'Dodoma', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('UA', 'Kiev', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('UG', 'Kampala', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('UM', 'None', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('US', 'Washington', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('UY', 'Montevideo', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('UZ', 'Tashkent', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VA', 'Vatican City', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VC', 'Kingstown', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VE', 'Caracas', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VG', 'Road Town', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VI', 'Charlotte Amalie', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VN', 'Hanoi', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('VU', 'Port Vila', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('WF', 'Mata-Utu', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('WLS', 'Cardiff', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('WS', 'Apia', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('YE', 'San\'a', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('YT', 'Dzaoudzi', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('YU', 'Belgrade', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ZA', 'Pretoria', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ZM', 'Lusaka', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_capital` VALUES ('ZW', 'Harare', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `country_code`
-- ----------------------------
DROP TABLE IF EXISTS `country_code`;
CREATE TABLE `country_code` (
  `COUNTRY_CODE` varchar(20) collate latin1_general_cs NOT NULL,
  `COUNTRY_ABBR` varchar(60) collate latin1_general_cs default NULL,
  `COUNTRY_NUMBER` varchar(60) collate latin1_general_cs default NULL,
  `COUNTRY_NAME` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`COUNTRY_CODE`),
  KEY `CNTR_CD_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CNTR_CD_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of country_code
-- ----------------------------
INSERT INTO `country_code` VALUES ('AD', 'AND', '020', 'ANDORRA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AE', 'ARE', '784', 'UNITED ARAB EMIRATES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AF', 'AFG', '004', 'AFGHANISTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AG', 'ATG', '028', 'ANTIGUA AND BARBUDA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AI', 'AIA', '660', 'ANGUILLA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AL', 'ALB', '008', 'ALBANIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AM', 'ARM', '051', 'ARMENIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AN', 'ANT', '530', 'NETHERLANDS ANTILLES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AO', 'AGO', '024', 'ANGOLA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AQ', 'ATA', '010', 'ANTARCTICA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AR', 'ARG', '032', 'ARGENTINA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AS', 'ASM', '016', 'AMERICAN SAMOA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AT', 'AUT', '040', 'AUSTRIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AU', 'AUS', '036', 'AUSTRALIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AW', 'ABW', '533', 'ARUBA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('AZ', 'AZE', '031', 'AZERBAIJAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BA', 'BIH', '070', 'BOSNIA AND HERZEGOWINA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BB', 'BRB', '052', 'BARBADOS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BD', 'BGD', '050', 'BANGLADESH', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BE', 'BEL', '056', 'BELGIUM', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BF', 'BFA', '854', 'BURKINA FASO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BG', 'BGR', '100', 'BULGARIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BH', 'BHR', '048', 'BAHRAIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BI', 'BDI', '108', 'BURUNDI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BJ', 'BEN', '204', 'BENIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BM', 'BMU', '060', 'BERMUDA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BN', 'BRN', '096', 'BRUNEI DARUSSALAM', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BO', 'BOL', '068', 'BOLIVIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BR', 'BRA', '076', 'BRAZIL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BS', 'BHS', '044', 'BAHAMAS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BT', 'BTN', '064', 'BHUTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BV', 'BVT', '074', 'BOUVET ISLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BW', 'BWA', '072', 'BOTSWANA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BY', 'BLR', '112', 'BELARUS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('BZ', 'BLZ', '084', 'BELIZE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CA', 'CAN', '124', 'CANADA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CC', 'CCK', '166', 'COCOS (KEELING) ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CD', 'COD', '180', 'CONGO, THE DEMOCRATIC REPUBLIC OF THE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CF', 'CAF', '140', 'CENTRAL AFRICAN REPUBLIC', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CG', 'COG', '178', 'CONGO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CH', 'CHE', '756', 'SWITZERLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CI', 'CIV', '384', 'COTE D\'IVOIRE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CK', 'COK', '184', 'COOK ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CL', 'CHL', '152', 'CHILE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CM', 'CMR', '120', 'CAMEROON', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CN', 'CHN', '156', 'CHINA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CO', 'COL', '170', 'COLOMBIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CR', 'CRI', '188', 'COSTA RICA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CU', 'CUB', '192', 'CUBA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CV', 'CPV', '132', 'CAPE VERDE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CX', 'CXR', '162', 'CHRISTMAS ISLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CY', 'CYP', '196', 'CYPRUS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('CZ', 'CZE', '203', 'CZECH REPUBLIC', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('DE', 'DEU', '276', 'GERMANY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('DJ', 'DJI', '262', 'DJIBOUTI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('DK', 'DNK', '208', 'DENMARK', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('DM', 'DMA', '212', 'DOMINICA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('DO', 'DOM', '214', 'DOMINICAN REPUBLIC', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('DZ', 'DZA', '012', 'ALGERIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('EC', 'ECU', '218', 'ECUADOR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('EE', 'EST', '233', 'ESTONIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('EG', 'EGY', '818', 'EGYPT', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('EH', 'ESH', '732', 'WESTERN SAHARA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ENG', 'ENGL', '896', 'ENGLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ER', 'ERI', '232', 'ERITREA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ES', 'ESP', '724', 'SPAIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ET', 'ETH', '231', 'ETHIOPIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FI', 'FIN', '246', 'FINLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FJ', 'FJI', '242', 'FIJI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FK', 'FLK', '238', 'FALKLAND ISLANDS (MALVINAS)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FM', 'FSM', '583', 'MICRONESIA, FEDERATED STATES OF', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FO', 'FRO', '234', 'FAROE ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FR', 'FRA', '250', 'FRANCE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('FX', 'FXX', '249', 'FRANCE, METROPOLITAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GA', 'GAB', '266', 'GABON', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GB', 'GBR', '826', 'UNITED KINGDOM', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GD', 'GRD', '308', 'GRENADA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GE', 'GEO', '268', 'GEORGIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GF', 'GUF', '254', 'FRENCH GUIANA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GH', 'GHA', '288', 'GHANA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GI', 'GIB', '292', 'GIBRALTAR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GL', 'GRL', '304', 'GREENLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GM', 'GMB', '270', 'GAMBIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GN', 'GIN', '324', 'GUINEA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GP', 'GLP', '312', 'GUADELOUPE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GQ', 'GNQ', '226', 'EQUATORIAL GUINEA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GR', 'GRC', '300', 'GREECE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GS', 'SGS', '239', 'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GT', 'GTM', '320', 'GUATEMALA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GU', 'GUM', '316', 'GUAM', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GW', 'GNB', '624', 'GUINEA-BISSAU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('GY', 'GUY', '328', 'GUYANA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('HK', 'HKG', '344', 'HONG KONG', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('HM', 'HMD', '334', 'HEARD AND MC DONALD ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('HN', 'HND', '340', 'HONDURAS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('HR', 'HRV', '191', 'CROATIA (local name: Hrvatska)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('HT', 'HTI', '332', 'HAITI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('HU', 'HUN', '348', 'HUNGARY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ID', 'IDN', '360', 'INDONESIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IE', 'IRL', '372', 'IRELAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IL', 'ISR', '376', 'ISRAEL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IN', 'IND', '356', 'INDIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IO', 'IOT', '086', 'BRITISH INDIAN OCEAN TERRITORY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IQ', 'IRQ', '368', 'IRAQ', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IR', 'IRN', '364', 'IRAN (ISLAMIC REPUBLIC OF)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IS', 'ISL', '352', 'ICELAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('IT', 'ITA', '380', 'ITALY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('JM', 'JAM', '388', 'JAMAICA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('JO', 'JOR', '400', 'JORDAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('JP', 'JPN', '392', 'JAPAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KE', 'KEN', '404', 'KENYA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KG', 'KGZ', '417', 'KYRGYZSTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KH', 'KHM', '116', 'CAMBODIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KI', 'KIR', '296', 'KIRIBATI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KM', 'COM', '174', 'COMOROS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KN', 'KNA', '659', 'SAINT KITTS AND NEVIS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KP', 'PRK', '408', 'KOREA, DEMOCRATIC PEOPLE\'S REPUBLIC OF', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KR', 'KOR', '410', 'KOREA, REPUBLIC OF', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KW', 'KWT', '414', 'KUWAIT', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KY', 'CYM', '136', 'CAYMAN ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('KZ', 'KAZ', '398', 'KAZAKHSTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LA', 'LAO', '418', 'LAO PEOPLE\'S DEMOCRATIC REPUBLIC', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LB', 'LBN', '422', 'LEBANON', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LC', 'LCA', '662', 'SAINT LUCIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LI', 'LIE', '438', 'LIECHTENSTEIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LK', 'LKA', '144', 'SRI LANKA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LR', 'LBR', '430', 'LIBERIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LS', 'LSO', '426', 'LESOTHO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LT', 'LTU', '440', 'LITHUANIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LU', 'LUX', '442', 'LUXEMBOURG', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LV', 'LVA', '428', 'LATVIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('LY', 'LBY', '434', 'LIBYAN ARAB JAMAHIRIYA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MA', 'MAR', '504', 'MOROCCO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MC', 'MCO', '492', 'MONACO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MD', 'MDA', '498', 'MOLDOVA, REPUBLIC OF', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MG', 'MDG', '450', 'MADAGASCAR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MH', 'MHL', '584', 'MARSHALL ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MK', 'MKD', '807', 'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ML', 'MLI', '466', 'MALI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MM', 'MMR', '104', 'MYANMAR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MN', 'MNG', '496', 'MONGOLIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MO', 'MAC', '446', 'MACAU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MP', 'MNP', '580', 'NORTHERN MARIANA ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MQ', 'MTQ', '474', 'MARTINIQUE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MR', 'MRT', '478', 'MAURITANIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MS', 'MSR', '500', 'MONTSERRAT', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MT', 'MLT', '470', 'MALTA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MU', 'MUS', '480', 'MAURITIUS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MV', 'MDV', '462', 'MALDIVES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MW', 'MWI', '454', 'MALAWI', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MX', 'MEX', '484', 'MEXICO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MY', 'MYS', '458', 'MALAYSIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('MZ', 'MOZ', '508', 'MOZAMBIQUE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NA', 'NAM', '516', 'NAMIBIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NC', 'NCL', '540', 'NEW CALEDONIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NE', 'NER', '562', 'NIGER', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NF', 'NFK', '574', 'NORFOLK ISLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NG', 'NGA', '566', 'NIGERIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NI', 'NIC', '558', 'NICARAGUA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NIR', 'NIRL', '897', 'N.IRELAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NL', 'NLD', '528', 'NETHERLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NO', 'NOR', '578', 'NORWAY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NP', 'NPL', '524', 'NEPAL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NR', 'NRU', '520', 'NAURU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NU', 'NIU', '570', 'NIUE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('NZ', 'NZL', '554', 'NEW ZEALAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('OM', 'OMN', '512', 'OMAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PA', 'PAN', '591', 'PANAMA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PE', 'PER', '604', 'PERU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PF', 'PYF', '258', 'FRENCH POLYNESIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PG', 'PNG', '598', 'PAPUA NEW GUINEA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PH', 'PHL', '608', 'PHILIPPINES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PK', 'PAK', '586', 'PAKISTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PL', 'POL', '616', 'POLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PM', 'SPM', '666', 'ST. PIERRE AND MIQUELON', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PN', 'PCN', '612', 'PITCAIRN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PR', 'PRI', '630', 'PUERTO RICO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PS', 'PSE', '275', 'PALESTINIAN TERRITORY, OCCUPIED', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PT', 'PRT', '620', 'PORTUGAL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PW', 'PLW', '585', 'PALAU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('PY', 'PRY', '600', 'PARAGUAY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('QA', 'QAT', '634', 'QATAR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('RE', 'REU', '638', 'REUNION', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('RO', 'ROM', '642', 'ROMANIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('RU', 'RUS', '643', 'RUSSIAN FEDERATION', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('RW', 'RWA', '646', 'RWANDA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SA', 'SAU', '682', 'SAUDI ARABIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SB', 'SLB', '090', 'SOLOMON ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SC', 'SYC', '690', 'SEYCHELLES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SCT', 'SCOT', '895', 'SCOTLAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SD', 'SDN', '736', 'SUDAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SE', 'SWE', '752', 'SWEDEN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SG', 'SGP', '702', 'SINGAPORE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SH', 'SHN', '654', 'ST. HELENA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SI', 'SVN', '705', 'SLOVENIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SJ', 'SJM', '744', 'SVALBARD AND JAN MAYEN ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SK', 'SVK', '703', 'SLOVAKIA (Slovak Republic)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SL', 'SLE', '694', 'SIERRA LEONE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SM', 'SMR', '674', 'SAN MARINO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SN', 'SEN', '686', 'SENEGAL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SO', 'SOM', '706', 'SOMALIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SR', 'SUR', '740', 'SURINAME', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ST', 'STP', '678', 'SAO TOME AND PRINCIPE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SV', 'SLV', '222', 'EL SALVADOR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SY', 'SYR', '760', 'SYRIAN ARAB REPUBLIC', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('SZ', 'SWZ', '748', 'SWAZILAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TC', 'TCA', '796', 'TURKS AND CAICOS ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TD', 'TCD', '148', 'CHAD', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TF', 'ATF', '260', 'FRENCH SOUTHERN TERRITORIES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TG', 'TGO', '768', 'TOGO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TH', 'THA', '764', 'THAILAND', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TJ', 'TJK', '762', 'TAJIKISTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TK', 'TKL', '772', 'TOKELAU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TM', 'TKM', '795', 'TURKMENISTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TN', 'TUN', '788', 'TUNISIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TO', 'TON', '776', 'TONGA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TP', 'TMP', '626', 'EAST TIMOR', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TR', 'TUR', '792', 'TURKEY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TT', 'TTO', '780', 'TRINIDAD AND TOBAGO', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TV', 'TUV', '798', 'TUVALU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TW', 'TWN', '158', 'TAIWAN, PROVINCE OF CHINA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('TZ', 'TZA', '834', 'TANZANIA, UNITED REPUBLIC OF', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('UA', 'UKR', '804', 'UKRAINE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('UG', 'UGA', '800', 'UGANDA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('UM', 'UMI', '581', 'UNITED STATES MINOR OUTLYING ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('US', 'USA', '840', 'UNITED STATES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('UY', 'URY', '858', 'URUGUAY', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('UZ', 'UZB', '860', 'UZBEKISTAN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VA', 'VAT', '336', 'HOLY SEE (VATICAN CITY STATE)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VC', 'VCT', '670', 'SAINT VINCENT AND THE GRENADINES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VE', 'VEN', '862', 'VENEZUELA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VG', 'VGB', '092', 'VIRGIN ISLANDS (BRITISH)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VI', 'VIR', '850', 'VIRGIN ISLANDS (U.S.)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VN', 'VNM', '704', 'VIET NAM', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('VU', 'VUT', '548', 'VANUATU', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('WF', 'WLF', '876', 'WALLIS AND FUTUNA ISLANDS', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('WLS', 'WALS', '898', 'WALES', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('WS', 'WSM', '882', 'SAMOA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('YE', 'YEM', '887', 'YEMEN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('YT', 'MYT', '175', 'MAYOTTE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('YU', 'YUG', '891', 'YUGOSLAVIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ZA', 'ZAF', '710', 'SOUTH AFRICA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ZM', 'ZMB', '894', 'ZAMBIA', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `country_code` VALUES ('ZW', 'ZWE', '716', 'ZIMBABWE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `country_tele_code`
-- ----------------------------
DROP TABLE IF EXISTS `country_tele_code`;
CREATE TABLE `country_tele_code` (
  `COUNTRY_CODE` varchar(20) collate latin1_general_cs NOT NULL,
  `TELE_CODE` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`COUNTRY_CODE`),
  KEY `CNTRY_TELE_TO_CODE` (`COUNTRY_CODE`),
  KEY `CNTR_TL_CD_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CNTR_TL_CD_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CNTRY_TELE_TO_CODE` FOREIGN KEY (`COUNTRY_CODE`) REFERENCES `country_code` (`COUNTRY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of country_tele_code
-- ----------------------------
INSERT INTO `country_tele_code` VALUES ('AD', '376', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AE', '971', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AF', '93', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AG', '1-268', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AI', '1-264', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AL', '355', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AM', '374', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AN', '599', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AO', '244', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AQ', '672', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AR', '54', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AS', '684', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AT', '43', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AU', '61', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AW', '297', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('AZ', '994', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BA', '387', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BB', '1-246', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BD', '880', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BE', '32', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BF', '226', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BG', '359', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BH', '973', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BI', '257', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BJ', '229', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BM', '1-441', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BN', '673', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BO', '591', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BR', '55', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BS', '1-242', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BT', '975', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BW', '267', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BY', '375', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('BZ', '501', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CA', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CC', '61', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CD', '243', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CF', '236', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CG', '242', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CH', '41', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CI', '225', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CK', '682', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CL', '56', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CM', '237', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CN', '86', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CO', '57', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CR', '506', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CU', '53', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CV', '238', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CX', '61', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CY', '357', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('CZ', '420', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('DE', '49', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('DJ', '253', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('DK', '45', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('DM', '1-767', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('DO', '809', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('DZ', '213', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('EC', '593', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('EE', '372', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('EG', '20', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('EH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ER', '291', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ES', '34', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ET', '251', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('FI', '358', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('FJ', '679', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('FK', '500', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('FM', '691', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('FO', '298', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('FR', '33', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GA', '241', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GB', '44', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GD', '1-473', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GE', '995', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GF', '594', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GH', '233', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GI', '350', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GL', '299', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GM', '220', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GN', '224', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GP', '590', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GQ', '240', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GR', '30', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GT', '502', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GU', '1-671', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GW', '245', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('GY', '592', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('HK', '852', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('HM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('HN', '504', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('HR', '385', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('HT', '509', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('HU', '36', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ID', '62', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IE', '353', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IL', '972', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IN', '91', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IQ', '964', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IR', '98', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IS', '354', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('IT', '39', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('JM', '1-876', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('JO', '962', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('JP', '81', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KE', '254', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KG', '996', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KH', '855', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KI', '686', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KM', '269', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KN', '1-869', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KP', '850', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KR', '82', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KW', '965', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KY', '1-345', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('KZ', '7', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LA', '856', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LB', '961', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LC', '1-758', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LI', '423', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LK', '94', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LR', '231', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LS', '266', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LT', '370', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LU', '352', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LV', '371', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('LY', '218', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MA', '212', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MC', '377', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MD', '373', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MG', '261', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MH', '692', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MK', '389', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ML', '223', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MM', '95', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MN', '976', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MO', '853', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MP', '670', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MQ', '596', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MR', '222', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MS', '1-664', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MT', '356', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MU', '230', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MV', '960', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MW', '265', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MX', '52', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MY', '60', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('MZ', '258', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NA', '264', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NC', '687', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NE', '227', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NF', '672', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NG', '234', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NI', '505', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NL', '31', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NO', '47', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NP', '977', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NR', '674', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NU', '683', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('NZ', '64', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('OM', '968', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PA', '507', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PE', '51', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PF', '689', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PG', '675', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PH', '63', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PK', '92', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PL', '48', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PM', '508', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PR', '1-787', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PT', '351', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PW', '680', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('PY', '595', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('QA', '974', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('RE', '262', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('RO', '40', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('RU', '7', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('RW', '250', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SA', '966', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SB', '677', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SC', '248', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SD', '249', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SE', '46', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SG', '65', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SH', '290', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SI', '386', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SJ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SK', '421', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SL', '232', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SM', '378', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SN', '221', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SO', '252', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SR', '597', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ST', '239', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SV', '503', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SY', '963', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('SZ', '268', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TC', '1-649', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TD', '235', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TG', '228', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TH', '66', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TJ', '992', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TK', '690', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TM', '993', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TN', '216', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TO', '676', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TP', '670', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TR', '90', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TT', '1-868', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TV', '688', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TW', '886', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('TZ', '255', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('UA', '380', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('UG', '256', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('UM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('US', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('UY', '598', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('UZ', '998', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VA', '39', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VC', '1-784', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VE', '58', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VG', '1-284', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VI', '1-340', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VN', '84', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('VU', '678', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('WF', '681', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('WS', '684', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('YE', '967', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('YT', '269', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('YU', '381', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ZA', '27', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ZM', '260', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');
INSERT INTO `country_tele_code` VALUES ('ZW', '263', '2011-12-01 17:17:56', '2011-12-01 17:17:55', '2011-12-01 17:17:56', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `custom_method`
-- ----------------------------
DROP TABLE IF EXISTS `custom_method`;
CREATE TABLE `custom_method` (
  `CUSTOM_METHOD_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `CUSTOM_METHOD_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `CUSTOM_METHOD_NAME` varchar(255) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CUSTOM_METHOD_ID`),
  KEY `CME_TO_TYPE` (`CUSTOM_METHOD_TYPE_ID`),
  KEY `CSTM_MTHD_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CSTM_MTHD_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CME_TO_TYPE` FOREIGN KEY (`CUSTOM_METHOD_TYPE_ID`) REFERENCES `custom_method_type` (`CUSTOM_METHOD_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of custom_method
-- ----------------------------

-- ----------------------------
-- Table structure for `custom_method_type`
-- ----------------------------
DROP TABLE IF EXISTS `custom_method_type`;
CREATE TABLE `custom_method_type` (
  `CUSTOM_METHOD_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `HAS_TABLE` char(1) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CUSTOM_METHOD_TYPE_ID`),
  KEY `CME_TYPE_PARENT` (`PARENT_TYPE_ID`),
  KEY `CSTM_MTD_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CSTM_MTD_TP_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `CME_TYPE_PARENT` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `custom_method_type` (`CUSTOM_METHOD_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of custom_method_type
-- ----------------------------

-- ----------------------------
-- Table structure for `custom_time_period`
-- ----------------------------
DROP TABLE IF EXISTS `custom_time_period`;
CREATE TABLE `custom_time_period` (
  `CUSTOM_TIME_PERIOD_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_PERIOD_ID` varchar(20) collate latin1_general_cs default NULL,
  `PERIOD_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `PERIOD_NUM` decimal(20,0) default NULL,
  `PERIOD_NAME` varchar(100) collate latin1_general_cs default NULL,
  `FROM_DATE` date default NULL,
  `THRU_DATE` date default NULL,
  `IS_CLOSED` char(1) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CUSTOM_TIME_PERIOD_ID`),
  KEY `ORG_PRD_PARPER` (`PARENT_PERIOD_ID`),
  KEY `ORG_PRD_PERTYP` (`PERIOD_TYPE_ID`),
  KEY `CSTM_TM_PRD_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CSTM_TM_PRD_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ORG_PRD_PARPER` FOREIGN KEY (`PARENT_PERIOD_ID`) REFERENCES `custom_time_period` (`CUSTOM_TIME_PERIOD_ID`),
  CONSTRAINT `ORG_PRD_PERTYP` FOREIGN KEY (`PERIOD_TYPE_ID`) REFERENCES `period_type` (`PERIOD_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of custom_time_period
-- ----------------------------

-- ----------------------------
-- Table structure for `data_source`
-- ----------------------------
DROP TABLE IF EXISTS `data_source`;
CREATE TABLE `data_source` (
  `DATA_SOURCE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DATA_SOURCE_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`DATA_SOURCE_ID`),
  KEY `DATA_SRC_TYP` (`DATA_SOURCE_TYPE_ID`),
  KEY `DATA_SOURCE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `DATA_SOURCE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `DATA_SRC_TYP` FOREIGN KEY (`DATA_SOURCE_TYPE_ID`) REFERENCES `data_source_type` (`DATA_SOURCE_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of data_source
-- ----------------------------
INSERT INTO `data_source` VALUES ('CONTEXT_INDUCTION', 'CONTENT_CREATION', 'Context Induction', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('CSR_ENTRY', 'ADMIN_ENTRY', 'Customer Service Rep Data Entry', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('ECOMMERCE_SITE', 'WEBSITE_ENTRY', 'eCommerce Site Profile Maintenance', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('GENERAL_MAILING', 'MAILING_LIST_SIGNUP', 'General Interest Mailing List Signup', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('GEN_ADMIN', 'ADMIN_ENTRY', 'General Administrative Data Entry', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('GEOPT_GOOGLE', 'GEOPOINT_SUPPLIER', 'Google as GeoPoint supplier', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('GEOPT_MAPTP', 'GEOPOINT_SUPPLIER', 'MapTP a GeoPoint supplier', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('GEOPT_MICROSOFT', 'GEOPOINT_SUPPLIER', 'Microsoft as GeoPoint supplier', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('GEOPT_YAHOO', 'GEOPOINT_SUPPLIER', 'Yahoo as GeoPoint supplier', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('MY_PORTAL', 'MY_PORTAL', 'My Portal Registration', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source` VALUES ('USER_ENTRY', 'CONTENT_CREATION', 'User Entry', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `data_source_type`
-- ----------------------------
DROP TABLE IF EXISTS `data_source_type`;
CREATE TABLE `data_source_type` (
  `DATA_SOURCE_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`DATA_SOURCE_TYPE_ID`),
  KEY `DT_SRC_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `DT_SRC_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of data_source_type
-- ----------------------------
INSERT INTO `data_source_type` VALUES ('ADMIN_ENTRY', 'Administrative Data Entry', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('ADVERTISEMENT', 'Advertisement', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('CONTENT_CREATION', 'Content and Data Resource Creation', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('CONTEST_SIGNUP', 'Contest Signup', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('GEOPOINT_SUPPLIER', 'Name of GeoPoints publisher', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('MAILING_LIST_SIGNUP', 'Mailing List Signup', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('MY_PORTAL', 'My Portal', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('PARTNER', 'Partner', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('PURCHASED_DATA', 'Purchased Data', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('WEB', 'Web', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `data_source_type` VALUES ('WEBSITE_ENTRY', 'Website Data Entry', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `email_template_setting`
-- ----------------------------
DROP TABLE IF EXISTS `email_template_setting`;
CREATE TABLE `email_template_setting` (
  `EMAIL_TEMPLATE_SETTING_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `BODY_SCREEN_LOCATION` varchar(255) collate latin1_general_cs default NULL,
  `XSLFO_ATTACH_SCREEN_LOCATION` varchar(255) collate latin1_general_cs default NULL,
  `FROM_ADDRESS` varchar(255) collate latin1_general_cs default NULL,
  `CC_ADDRESS` varchar(255) collate latin1_general_cs default NULL,
  `BCC_ADDRESS` varchar(255) collate latin1_general_cs default NULL,
  `SUBJECT` varchar(255) collate latin1_general_cs default NULL,
  `CONTENT_TYPE` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EMAIL_TEMPLATE_SETTING_ID`),
  KEY `EML_TMPT_STG_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EML_TMPT_STG_TXCRS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of email_template_setting
-- ----------------------------

-- ----------------------------
-- Table structure for `entity_audit_log`
-- ----------------------------
DROP TABLE IF EXISTS `entity_audit_log`;
CREATE TABLE `entity_audit_log` (
  `AUDIT_HISTORY_SEQ_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `CHANGED_ENTITY_NAME` varchar(255) collate latin1_general_cs default NULL,
  `CHANGED_FIELD_NAME` varchar(255) collate latin1_general_cs default NULL,
  `PK_COMBINED_VALUE_TEXT` varchar(255) collate latin1_general_cs default NULL,
  `OLD_VALUE_TEXT` varchar(255) collate latin1_general_cs default NULL,
  `NEW_VALUE_TEXT` varchar(255) collate latin1_general_cs default NULL,
  `CHANGED_DATE` datetime default NULL,
  `CHANGED_BY_INFO` varchar(255) collate latin1_general_cs default NULL,
  `CHANGED_SESSION_INFO` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`AUDIT_HISTORY_SEQ_ID`),
  KEY `ENTT_ADT_LG_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENTT_ADT_LG_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_audit_log
-- ----------------------------

-- ----------------------------
-- Table structure for `entity_group`
-- ----------------------------
DROP TABLE IF EXISTS `entity_group`;
CREATE TABLE `entity_group` (
  `ENTITY_GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ENTITY_GROUP_NAME` varchar(100) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_GROUP_ID`),
  KEY `ENTT_GRP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENTT_GRP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_group
-- ----------------------------

-- ----------------------------
-- Table structure for `entity_group_entry`
-- ----------------------------
DROP TABLE IF EXISTS `entity_group_entry`;
CREATE TABLE `entity_group_entry` (
  `ENTITY_GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ENTITY_OR_PACKAGE` varchar(255) collate latin1_general_cs NOT NULL,
  `APPL_ENUM_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_GROUP_ID`,`ENTITY_OR_PACKAGE`),
  KEY `ENTGRP_GRP` (`ENTITY_GROUP_ID`),
  KEY `ENT_GRP_ENR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENT_GRP_ENR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENTGRP_GRP` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `entity_group` (`ENTITY_GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_group_entry
-- ----------------------------

-- ----------------------------
-- Table structure for `entity_key_store`
-- ----------------------------
DROP TABLE IF EXISTS `entity_key_store`;
CREATE TABLE `entity_key_store` (
  `KEY_NAME` varchar(250) collate latin1_general_cs NOT NULL,
  `KEY_TEXT` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`KEY_NAME`),
  KEY `ENTT_K_STR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENTT_K_STR_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_key_store
-- ----------------------------
INSERT INTO `entity_key_store` VALUES ('{SHA}0b3f6c337846ce8b5e3b81831a84ee2a5739e802', '230db6da0dc1b9e0f4a7cdcde38694e0d07a80730ddf2f08', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}1dd7cda2489fc66811f84101c20ef765dc2cd4a9', '7acdb91551a16dd0649defc4f7a18607c7ab62105e4afb9d', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}224728c1f5fbfdda336f28669fae27f041490d7d', '6880c4b5f1e0ab5d2691ae9b0da4702cf2e6ad2f4af4a4b6', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}30a1e1fe63ee4354b5b80fbdc7c0ab8e46940521', 'bf5da7c1681951c819fb2c79c1cd834a3e64d69d23a461ea', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}5bdf9dfba43d5837400f28a478f102dfa2058546', '577f38106475518567c1a27f234ce658376ed35d387f8cf2', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}6335542570da02c9f707ab52001eb814bd10c86b', 'a2b616bf5d4598f8b01926d6d5fdad491f94f43d9bf280a4', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}6880606f417f73ccf4be9e3982cd9f3012f3938a', '5867ea01eac4b675cece8c07b34676fb8004eadc86ec6885', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}831e5540c2caec44436577a2e4f3fe13d7813837', '5861cd3dc2d5cd8c19ef6b0e402989cb195837d326ec5764', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}87efbe445939468dd12eacbbac34874c55d00e6a', '75d0feda2068c8ea519794ce5815b00880fe38bace92733d', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}881651fcec8e8e0946278e3cf8f43e892a68a5f9', '2c58e93bf276da32fd674aa7260e5d4ae0c88c4989bc75ce', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}88ce371bf57b20e3d3f637c511ebcd60d404b418', 'fd83a43775b6153de3cdfe8f5db358fbcd2ce54f6b627cce', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}94d8c5b7fd7276006ab26490ef454bfaca59422d', 'f85e3292548a57b62531df45f749c2e6628f04c4e5cbd62c', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}97bf2c2b46e07e5c7f84e1b198284680244828cc', 'f10dd6bc8a54b910d5084cfe64252f0da24cb5687a15ad45', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}b221493ef45991fcb21aec2a333beff15e5bc1ae', 'f13168732937cbf7796bd3e026389dfdbc34fe4fe562d007', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}b8d9f6781e37df7330973e646fe5cd9800af5ab1', '4c614ac1ef6bab68ab9e401f13f8d0e558a4a175e9ecb908', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}b9a37880d27bceb967e45dfe437fcc228e6caad3', 'df6d750b317fe354e62cea236de397d632d5a79dab3d6bb6', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}d490255760e9a2120de01e6628d9de54c8c969e5', 'e54f04fbd662c8cba27cad0e31926e07868c52df94642cd0', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `entity_key_store` VALUES ('{SHA}f339007717c0f052beb5c76482dd7dc5ac35393f', 'ba3157205768f7f234914c68b5aea28c1957ce768686235e', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}f722a90d46bbbb570f9389d023ebe7dc500f71ca', '52577c4a6bbcf8156b3780cd32f1a2fb899eae946ed07552', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54', '2011-12-01 17:17:54');
INSERT INTO `entity_key_store` VALUES ('{SHA}fbfce697ca1ce81d712d30edf3d4e8f4ee2694f1', 'f7c44fc10b75c15e8f5e378ff29db0a89dfead7af2fb5edf', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `entity_sync`
-- ----------------------------
DROP TABLE IF EXISTS `entity_sync`;
CREATE TABLE `entity_sync` (
  `ENTITY_SYNC_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `RUN_STATUS_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_SUCCESSFUL_SYNCH_TIME` datetime default NULL,
  `LAST_HISTORY_START_DATE` datetime default NULL,
  `PRE_OFFLINE_SYNCH_TIME` datetime default NULL,
  `OFFLINE_SYNC_SPLIT_MILLIS` decimal(20,0) default NULL,
  `SYNC_SPLIT_MILLIS` decimal(20,0) default NULL,
  `SYNC_END_BUFFER_MILLIS` decimal(20,0) default NULL,
  `MAX_RUNNING_NO_UPDATE_MILLIS` decimal(20,0) default NULL,
  `TARGET_SERVICE_NAME` varchar(255) collate latin1_general_cs default NULL,
  `TARGET_DELEGATOR_NAME` varchar(255) collate latin1_general_cs default NULL,
  `KEEP_REMOVE_INFO_HOURS` double default NULL,
  `FOR_PULL_ONLY` char(1) collate latin1_general_cs default NULL,
  `FOR_PUSH_ONLY` char(1) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_SYNC_ID`),
  KEY `ENTITY_SYNC_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENTITY_SYNC_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_sync
-- ----------------------------
INSERT INTO `entity_sync` VALUES ('1500', 'ESR_NOT_STARTED', null, null, null, null, '600000', null, null, 'remoteStoreEntitySyncDataRmi', null, '24', null, 'Y', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `entity_sync_history`
-- ----------------------------
DROP TABLE IF EXISTS `entity_sync_history`;
CREATE TABLE `entity_sync_history` (
  `ENTITY_SYNC_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `START_DATE` datetime NOT NULL,
  `RUN_STATUS_ID` varchar(20) collate latin1_general_cs default NULL,
  `BEGINNING_SYNCH_TIME` datetime default NULL,
  `LAST_SUCCESSFUL_SYNCH_TIME` datetime default NULL,
  `LAST_CANDIDATE_END_TIME` datetime default NULL,
  `LAST_SPLIT_START_TIME` decimal(20,0) default NULL,
  `TO_CREATE_INSERTED` decimal(20,0) default NULL,
  `TO_CREATE_UPDATED` decimal(20,0) default NULL,
  `TO_CREATE_NOT_UPDATED` decimal(20,0) default NULL,
  `TO_STORE_INSERTED` decimal(20,0) default NULL,
  `TO_STORE_UPDATED` decimal(20,0) default NULL,
  `TO_STORE_NOT_UPDATED` decimal(20,0) default NULL,
  `TO_REMOVE_DELETED` decimal(20,0) default NULL,
  `TO_REMOVE_ALREADY_DELETED` decimal(20,0) default NULL,
  `TOTAL_ROWS_EXPORTED` decimal(20,0) default NULL,
  `TOTAL_ROWS_TO_CREATE` decimal(20,0) default NULL,
  `TOTAL_ROWS_TO_STORE` decimal(20,0) default NULL,
  `TOTAL_ROWS_TO_REMOVE` decimal(20,0) default NULL,
  `TOTAL_SPLITS` decimal(20,0) default NULL,
  `TOTAL_STORE_CALLS` decimal(20,0) default NULL,
  `RUNNING_TIME_MILLIS` decimal(20,0) default NULL,
  `PER_SPLIT_MIN_MILLIS` decimal(20,0) default NULL,
  `PER_SPLIT_MAX_MILLIS` decimal(20,0) default NULL,
  `PER_SPLIT_MIN_ITEMS` decimal(20,0) default NULL,
  `PER_SPLIT_MAX_ITEMS` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_SYNC_ID`,`START_DATE`),
  KEY `ENTSYNC_HSTSNC` (`ENTITY_SYNC_ID`),
  KEY `ENT_SNC_HSR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENT_SNC_HSR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENTSYNC_HSTSNC` FOREIGN KEY (`ENTITY_SYNC_ID`) REFERENCES `entity_sync` (`ENTITY_SYNC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_sync_history
-- ----------------------------

-- ----------------------------
-- Table structure for `entity_sync_include`
-- ----------------------------
DROP TABLE IF EXISTS `entity_sync_include`;
CREATE TABLE `entity_sync_include` (
  `ENTITY_SYNC_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ENTITY_OR_PACKAGE` varchar(255) collate latin1_general_cs NOT NULL,
  `APPL_ENUM_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_SYNC_ID`,`ENTITY_OR_PACKAGE`),
  KEY `ENTSYNC_INCSNC` (`ENTITY_SYNC_ID`),
  KEY `ENT_SNC_INCD_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENT_SNC_INCD_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENTSYNC_INCSNC` FOREIGN KEY (`ENTITY_SYNC_ID`) REFERENCES `entity_sync` (`ENTITY_SYNC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_sync_include
-- ----------------------------
INSERT INTO `entity_sync_include` VALUES ('1500', 'org.ofbiz.', 'ESIA_INCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `entity_sync_include` VALUES ('1500', 'org.ofbiz.entity', 'ESIA_EXCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `entity_sync_include` VALUES ('1500', 'org.ofbiz.service', 'ESIA_EXCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `entity_sync_include` VALUES ('1500', 'ServerHit', 'ESIA_EXCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `entity_sync_include` VALUES ('1500', 'ServerHitBin', 'ESIA_EXCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `entity_sync_include_group`
-- ----------------------------
DROP TABLE IF EXISTS `entity_sync_include_group`;
CREATE TABLE `entity_sync_include_group` (
  `ENTITY_SYNC_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ENTITY_GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_SYNC_ID`,`ENTITY_GROUP_ID`),
  KEY `ENTSNCGU_GRP` (`ENTITY_GROUP_ID`),
  KEY `ENTSNCGU_SNC` (`ENTITY_SYNC_ID`),
  KEY `ENT_SNC_IND_GRP_TP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENT_SNC_IND_GRP_TS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENTSNCGU_GRP` FOREIGN KEY (`ENTITY_GROUP_ID`) REFERENCES `entity_group` (`ENTITY_GROUP_ID`),
  CONSTRAINT `ENTSNCGU_SNC` FOREIGN KEY (`ENTITY_SYNC_ID`) REFERENCES `entity_sync` (`ENTITY_SYNC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_sync_include_group
-- ----------------------------

-- ----------------------------
-- Table structure for `entity_sync_remove`
-- ----------------------------
DROP TABLE IF EXISTS `entity_sync_remove`;
CREATE TABLE `entity_sync_remove` (
  `ENTITY_SYNC_REMOVE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PRIMARY_KEY_REMOVED` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTITY_SYNC_REMOVE_ID`),
  KEY `ENT_SNC_RMV_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENT_SNC_RMV_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of entity_sync_remove
-- ----------------------------

-- ----------------------------
-- Table structure for `enumeration`
-- ----------------------------
DROP TABLE IF EXISTS `enumeration`;
CREATE TABLE `enumeration` (
  `ENUM_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ENUM_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `ENUM_CODE` varchar(60) collate latin1_general_cs default NULL,
  `SEQUENCE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENUM_ID`),
  KEY `ENUM_TO_TYPE` (`ENUM_TYPE_ID`),
  KEY `ENUMERATION_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENUMERATION_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENUM_TO_TYPE` FOREIGN KEY (`ENUM_TYPE_ID`) REFERENCES `enumeration_type` (`ENUM_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of enumeration
-- ----------------------------
INSERT INTO `enumeration` VALUES ('ESIA_ALWAYS', 'ENTSYNC_INC_APPL', 'ALWAYS', '03', 'Always Include', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `enumeration` VALUES ('ESIA_EXCLUDE', 'ENTSYNC_INC_APPL', 'EXCLUDE', '02', 'Exclude', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `enumeration` VALUES ('ESIA_INCLUDE', 'ENTSYNC_INC_APPL', 'INCLUDE', '01', 'Include', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `enumeration` VALUES ('EXFTSRC_CUSTOMER', 'EXMPL_FEAT_SOURCE', 'CUSTOMER', '01', 'Customer', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `enumeration` VALUES ('EXFTSRC_EMPLOYEE', 'EXMPL_FEAT_SOURCE', 'EMPLOYEE', '03', 'Employee', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `enumeration` VALUES ('EXFTSRC_PARTNER', 'EXMPL_FEAT_SOURCE', 'PARTNER', '02', 'Partner', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `enumeration` VALUES ('EXTERNAL_CONVERSION', 'CONVERSION_PURPOSE', 'EXTERNAL', '20', 'External Conversion', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('INTERNAL_CONVERSION', 'CONVERSION_PURPOSE', 'INTERNAL', '10', 'Internal Conversion', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_BT', 'KW_THES_REL', 'BT', '05', 'Broader Term', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_CS', 'KW_THES_REL', 'CS', '03', 'Correct Spelling', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_LANG_FR', 'KW_THES_REL', 'LANG_FR', '22', 'Lang: French', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_LANG_SP', 'KW_THES_REL', 'LANG_SP', '21', 'Lang: Spanish', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_MT', 'KW_THES_REL', 'MT', '06', 'Micro-thesaurus Term', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_NT', 'KW_THES_REL', 'NT', '04', 'Narrower Term', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_RT', 'KW_THES_REL', 'RT', '07', 'Related Term', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_SN', 'KW_THES_REL', 'SN', '08', 'Scope Notes', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_UF', 'KW_THES_REL', 'UF', '01', 'Used For', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('KWTR_USE', 'KW_THES_REL', 'USE', '02', 'Use Instead', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_DOCBOOKSTYLESHEET', 'VT_RES_TYPE', null, '03', 'Docbook Style Sheet URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_EXTRA_HEAD', 'VT_RES_TYPE', null, '06', 'Additional <tt><head></tt> Element Markup', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_FTR_JAVASCRIPT', 'VT_RES_TYPE', null, '11', 'Footer JavaScript File URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_FTR_TMPLT_LOC', 'VT_RES_TYPE', null, '10', 'Footer Template Location', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_HDR_IMAGE_URL', 'VT_RES_TYPE', null, '07', 'Masthead/Branding Logo Image URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_HDR_JAVASCRIPT', 'VT_RES_TYPE', null, '09', 'Header JavaScript File URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_HDR_TMPLT_LOC', 'VT_RES_TYPE', null, '08', 'Header Template Location', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_HELPSTYLESHEET', 'VT_RES_TYPE', null, '02', 'Help Style Sheet URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_MSG_TMPLT_LOC', 'VT_RES_TYPE', null, '15', 'Main Messages Template Location', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_NAME', 'VT_RES_TYPE', null, '00', 'Visual Theme name', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_NAV_CLOSE_TMPLT', 'VT_RES_TYPE', null, '14', 'Closing Navigation Template Location', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_NAV_OPEN_TMPLT', 'VT_RES_TYPE', null, '13', 'Opening Navigation Template Location', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_NAV_TMPLT_LOC', 'VT_RES_TYPE', null, '12', 'Main Navigation Template Location', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_RTL_STYLESHEET', 'VT_RES_TYPE', null, '04', 'Right-to-Left (RTL) Style Sheet URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_SCREENSHOT', 'VT_RES_TYPE', null, '16', 'Theme Preview Screenshot', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_SHORTCUT_ICON', 'VT_RES_TYPE', null, '05', 'Shortcut Icon URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('VT_STYLESHEET', 'VT_RES_TYPE', null, '01', 'Style Sheet URL', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('_00_', '_NA_', '_00_', '0', 'Not Applicable', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration` VALUES ('_NA_', '_NA_', '_NA_', '0', 'Not Applicable', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `enumeration_type`
-- ----------------------------
DROP TABLE IF EXISTS `enumeration_type`;
CREATE TABLE `enumeration_type` (
  `ENUM_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `HAS_TABLE` char(1) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENUM_TYPE_ID`),
  KEY `ENUM_TYPE_PARENT` (`PARENT_TYPE_ID`),
  KEY `ENMRTN_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `ENMRTN_TP_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENUM_TYPE_PARENT` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `enumeration_type` (`ENUM_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of enumeration_type
-- ----------------------------
INSERT INTO `enumeration_type` VALUES ('CONVERSION_PURPOSE', null, null, 'Conversion Purpose', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration_type` VALUES ('ENTSYNC_INC_APPL', null, 'N', 'Entity Sync Include Application Type', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `enumeration_type` VALUES ('EXMPL_FEAT_SOURCE', null, 'N', 'Example Feature Source', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `enumeration_type` VALUES ('KW_THES_REL', null, 'N', 'Thesaurus Relationship Type', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration_type` VALUES ('VT_RES_TYPE', null, 'N', 'Visual Theme Resource Type (stylesheet, javascript file, etc)', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `enumeration_type` VALUES ('_NA_', null, null, 'Not Applicable', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `example`
-- ----------------------------
DROP TABLE IF EXISTS `example`;
CREATE TABLE `example` (
  `EXAMPLE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `EXAMPLE_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `STATUS_ID` varchar(20) collate latin1_general_cs default NULL,
  `EXAMPLE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LONG_DESCRIPTION` longtext collate latin1_general_cs,
  `COMMENTS` varchar(255) collate latin1_general_cs default NULL,
  `EXAMPLE_SIZE` decimal(20,0) default NULL,
  `EXAMPLE_DATE` datetime default NULL,
  `ANOTHER_DATE` datetime default NULL,
  `ANOTHER_TEXT` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_ID`),
  KEY `EXMPL_TYP` (`EXAMPLE_TYPE_ID`),
  KEY `EXMPL_STTS` (`STATUS_ID`),
  KEY `EXAMPLE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXAMPLE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXMPL_STTS` FOREIGN KEY (`STATUS_ID`) REFERENCES `status_item` (`STATUS_ID`),
  CONSTRAINT `EXMPL_TYP` FOREIGN KEY (`EXAMPLE_TYPE_ID`) REFERENCES `example_type` (`EXAMPLE_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example
-- ----------------------------
INSERT INTO `example` VALUES ('EX01', 'CONTRIVED', 'EXST_IN_DESIGN', 'Example 1', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX02', 'CONTRIVED', 'EXST_IN_DESIGN', 'Example 2', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX03', 'CONTRIVED', 'EXST_IN_DESIGN', 'Example 3', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX04', 'REAL_WORLD', 'EXST_IN_DESIGN', 'Example 4', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX05', 'REAL_WORLD', 'EXST_IN_DESIGN', 'Example 5', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX06', 'MADE_UP', 'EXST_IN_DESIGN', 'Example 6', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX07', 'MADE_UP', 'EXST_IN_DESIGN', 'Example 7', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX08', 'MADE_UP', 'EXST_IN_DESIGN', 'Example 8', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX09', 'MADE_UP', 'EXST_IN_DESIGN', 'Example 9', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX10', 'MADE_UP', 'EXST_IN_DESIGN', 'Example 10', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX11', 'INSPIRED', 'EXST_IN_DESIGN', 'Example 11', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example` VALUES ('EX12', 'INSPIRED', 'EXST_IN_DESIGN', 'Example 12', null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `example_feature`
-- ----------------------------
DROP TABLE IF EXISTS `example_feature`;
CREATE TABLE `example_feature` (
  `EXAMPLE_FEATURE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `FEATURE_SOURCE_ENUM_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_FEATURE_ID`),
  KEY `EXFT_ENUM` (`FEATURE_SOURCE_ENUM_ID`),
  KEY `EXMPL_FTR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXMPL_FTR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXFT_ENUM` FOREIGN KEY (`FEATURE_SOURCE_ENUM_ID`) REFERENCES `enumeration` (`ENUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example_feature
-- ----------------------------

-- ----------------------------
-- Table structure for `example_feature_appl`
-- ----------------------------
DROP TABLE IF EXISTS `example_feature_appl`;
CREATE TABLE `example_feature_appl` (
  `EXAMPLE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `EXAMPLE_FEATURE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `FROM_DATE` datetime NOT NULL,
  `THRU_DATE` datetime default NULL,
  `EXAMPLE_FEATURE_APPL_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `SEQUENCE_NUM` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_ID`,`EXAMPLE_FEATURE_ID`,`FROM_DATE`),
  KEY `EXFTAP_EXPL` (`EXAMPLE_ID`),
  KEY `EXFTAP_EXFT` (`EXAMPLE_FEATURE_ID`),
  KEY `EXFTAP_TYP` (`EXAMPLE_FEATURE_APPL_TYPE_ID`),
  KEY `EXML_FTR_APL_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXML_FTR_APL_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXFTAP_EXFT` FOREIGN KEY (`EXAMPLE_FEATURE_ID`) REFERENCES `example_feature` (`EXAMPLE_FEATURE_ID`),
  CONSTRAINT `EXFTAP_EXPL` FOREIGN KEY (`EXAMPLE_ID`) REFERENCES `example` (`EXAMPLE_ID`),
  CONSTRAINT `EXFTAP_TYP` FOREIGN KEY (`EXAMPLE_FEATURE_APPL_TYPE_ID`) REFERENCES `example_feature_appl_type` (`EXAMPLE_FEATURE_APPL_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example_feature_appl
-- ----------------------------

-- ----------------------------
-- Table structure for `example_feature_appl_type`
-- ----------------------------
DROP TABLE IF EXISTS `example_feature_appl_type`;
CREATE TABLE `example_feature_appl_type` (
  `EXAMPLE_FEATURE_APPL_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_FEATURE_APPL_TYPE_ID`),
  KEY `EXFTAPTP_PAR` (`PARENT_TYPE_ID`),
  KEY `EXL_FTR_APL_TP_TXP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXL_FTR_APL_TP_TXS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXFTAPTP_PAR` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `example_feature_appl_type` (`EXAMPLE_FEATURE_APPL_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example_feature_appl_type
-- ----------------------------
INSERT INTO `example_feature_appl_type` VALUES ('DESIRED', null, 'Desired', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example_feature_appl_type` VALUES ('NOT_ALLOWED', null, 'Not Allowed', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example_feature_appl_type` VALUES ('REQUIRED', null, 'Required', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `example_item`
-- ----------------------------
DROP TABLE IF EXISTS `example_item`;
CREATE TABLE `example_item` (
  `EXAMPLE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `EXAMPLE_ITEM_SEQ_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `AMOUNT` double default NULL,
  `AMOUNT_UOM_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_ID`,`EXAMPLE_ITEM_SEQ_ID`),
  KEY `EXMPLIT_UOM` (`AMOUNT_UOM_ID`),
  KEY `EXMPLIT_EXMP` (`EXAMPLE_ID`),
  KEY `EXMPL_ITM_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXMPL_ITM_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXMPLIT_EXMP` FOREIGN KEY (`EXAMPLE_ID`) REFERENCES `example` (`EXAMPLE_ID`),
  CONSTRAINT `EXMPLIT_UOM` FOREIGN KEY (`AMOUNT_UOM_ID`) REFERENCES `uom` (`UOM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example_item
-- ----------------------------

-- ----------------------------
-- Table structure for `example_status`
-- ----------------------------
DROP TABLE IF EXISTS `example_status`;
CREATE TABLE `example_status` (
  `EXAMPLE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `STATUS_DATE` datetime NOT NULL,
  `STATUS_END_DATE` datetime default NULL,
  `STATUS_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_ID`,`STATUS_DATE`),
  KEY `EXMPLST_EXMPL` (`EXAMPLE_ID`),
  KEY `EXMPLST_STTS` (`STATUS_ID`),
  KEY `EXMPL_STTS_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXMPL_STTS_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXMPLST_EXMPL` FOREIGN KEY (`EXAMPLE_ID`) REFERENCES `example` (`EXAMPLE_ID`),
  CONSTRAINT `EXMPLST_STTS` FOREIGN KEY (`STATUS_ID`) REFERENCES `status_item` (`STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example_status
-- ----------------------------

-- ----------------------------
-- Table structure for `example_type`
-- ----------------------------
DROP TABLE IF EXISTS `example_type`;
CREATE TABLE `example_type` (
  `EXAMPLE_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`EXAMPLE_TYPE_ID`),
  KEY `EXMPLTP_PAR` (`PARENT_TYPE_ID`),
  KEY `EXMPL_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `EXMPL_TP_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `EXMPLTP_PAR` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `example_type` (`EXAMPLE_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of example_type
-- ----------------------------
INSERT INTO `example_type` VALUES ('CONTRIVED', 'MADE_UP', 'Contrived', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example_type` VALUES ('INSPIRED', 'MADE_UP', 'Inspired', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example_type` VALUES ('MADE_UP', null, 'Made Up', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `example_type` VALUES ('REAL_WORLD', null, 'Real World', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `geo`
-- ----------------------------
DROP TABLE IF EXISTS `geo`;
CREATE TABLE `geo` (
  `GEO_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `GEO_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `GEO_NAME` varchar(100) collate latin1_general_cs default NULL,
  `GEO_CODE` varchar(60) collate latin1_general_cs default NULL,
  `GEO_SEC_CODE` varchar(60) collate latin1_general_cs default NULL,
  `ABBREVIATION` varchar(60) collate latin1_general_cs default NULL,
  `WELL_KNOWN_TEXT` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GEO_ID`),
  KEY `GEO_TO_TYPE` (`GEO_TYPE_ID`),
  KEY `GEO_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `GEO_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `GEO_TO_TYPE` FOREIGN KEY (`GEO_TYPE_ID`) REFERENCES `geo_type` (`GEO_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of geo
-- ----------------------------
INSERT INTO `geo` VALUES ('AA', 'STATE', 'Armed Forces Americas', 'AA', null, 'AA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AB', 'PROVINCE', 'Alberta', 'AB', null, 'AB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('ABW', 'COUNTRY', 'Aruba', 'AW', '533', 'ABW', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AE', 'STATE', 'Armed Forces Europe', 'AE', null, 'AE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AFG', 'COUNTRY', 'Afghanistan', 'AF', '004', 'AFG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AGO', 'COUNTRY', 'Angola', 'AO', '024', 'AGO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AHUST', 'GROUP', 'Alaska/Hawaii/Territories', 'AHUST', null, 'AHUST', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AIA', 'COUNTRY', 'Anguilla', 'AI', '660', 'AIA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AK', 'STATE', 'Alaska', 'AK', null, 'AK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AKHI', 'GROUP', 'Alaska/Hawaii', 'AKHI', null, 'AKHI', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AL', 'STATE', 'Alabama', 'AL', null, 'AL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('ALB', 'COUNTRY', 'Albania', 'AL', '008', 'ALB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AND', 'COUNTRY', 'Andorra', 'AD', '020', 'AND', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ANT', 'COUNTRY', 'Netherlands Antilles', 'AN', '530', 'ANT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AP', 'STATE', 'Armed Forces Pacific', 'AP', null, 'AP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AR', 'STATE', 'Arkansas', 'AR', null, 'AR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('ARE', 'COUNTRY', 'United Arab Emirates', 'AE', '784', 'ARE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ARG', 'COUNTRY', 'Argentina', 'AR', '032', 'ARG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ARM', 'COUNTRY', 'Armenia', 'AM', '051', 'ARM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AS', 'STATE', 'American Samoa', 'AS', null, 'AS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('ASM', 'COUNTRY', 'American Samoa', 'AS', '016', 'ASM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ATA', 'COUNTRY', 'Antarctica', 'AQ', '010', 'ATA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ATF', 'COUNTRY', 'French Southern Territories', 'TF', '260', 'ATF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ATG', 'COUNTRY', 'Antigua And Barbuda', 'AG', '028', 'ATG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-ACT', 'TERRITORY', 'Australian Capital Territory', 'ACT', null, 'ACT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-NSW', 'STATE', 'New South Wales', 'NSW', null, 'NSW', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-NT', 'TERRITORY', 'Northern Territory', 'NT', null, 'NT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-QLD', 'STATE', 'Queensland', 'QLD', null, 'QLD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-SA', 'STATE', 'South Australia', 'SA', null, 'SA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-TAS', 'STATE', 'Tasmania', 'TAS', null, 'TAS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-VIC', 'STATE', 'Victoria', 'VIC', null, 'VIC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AU-WA', 'STATE', 'Western Australia', 'WA', null, 'WA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AUS', 'COUNTRY', 'Australia', 'AU', '036', 'AUS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AUT', 'COUNTRY', 'Austria', 'AT', '040', 'AUT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('AZ', 'STATE', 'Arizona', 'AZ', null, 'AZ', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('AZE', 'COUNTRY', 'Azerbaijan', 'AZ', '031', 'AZE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BC', 'PROVINCE', 'British Columbia', 'BC', null, 'BC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('BDI', 'COUNTRY', 'Burundi', 'BI', '108', 'BDI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BEL', 'COUNTRY', 'Belgium', 'BE', '056', 'BEL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BEN', 'COUNTRY', 'Benin', 'BJ', '204', 'BEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BFA', 'COUNTRY', 'Burkina Faso', 'BF', '854', 'BFA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-01', 'PROVINCE', 'Blagoevgrad', '01', null, 'E', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-02', 'PROVINCE', 'Burgas', '02', null, 'A', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-03', 'PROVINCE', 'Varna', '03', null, 'B', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-04', 'PROVINCE', 'Veliko Tarnovo', '04', null, 'BT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-05', 'PROVINCE', 'Vidin', '05', null, 'BH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-06', 'PROVINCE', 'Vratsa', '06', null, 'BP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-07', 'PROVINCE', 'Gabrovo', '07', null, 'EB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-08', 'PROVINCE', 'Dobrich', '08', null, 'TX', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-09', 'PROVINCE', 'Kardzhali', '09', null, 'K', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-10', 'PROVINCE', 'Kyustendil', '10', null, 'KH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-11', 'PROVINCE', 'Lovech', '11', null, 'OB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-12', 'PROVINCE', 'Montana', '12', null, 'M', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-13', 'PROVINCE', 'Pazardzhik', '13', null, 'PA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-14', 'PROVINCE', 'Pernik', '14', null, 'PK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-15', 'PROVINCE', 'Pleven', '15', null, 'EH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-16', 'PROVINCE', 'Plovdiv', '16', null, 'PB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-17', 'PROVINCE', 'Razgrad', '17', null, 'PP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-18', 'PROVINCE', 'Ruse', '18', null, 'P', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-19', 'PROVINCE', 'Silistra', '19', null, 'CC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-20', 'PROVINCE', 'Sliven', '20', null, 'CH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-21', 'PROVINCE', 'Smolyan', '21', null, 'CM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-22', 'PROVINCE', 'Sofia', '22', null, 'C', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-23', 'PROVINCE', 'Sofia Province', '23', null, 'CO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-24', 'PROVINCE', 'Stara Zagora', '24', null, 'CT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-25', 'PROVINCE', 'Targovishte', '25', null, 'T', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-26', 'PROVINCE', 'Haskovo', '26', null, 'X', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-27', 'PROVINCE', 'Shumen', '27', null, 'H', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BG-28', 'PROVINCE', 'Yambol', '28', null, 'Y', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BGD', 'COUNTRY', 'Bangladesh', 'BD', '050', 'BGD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BGR', 'COUNTRY', 'Bulgaria', 'BG', '100', 'BGR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BHR', 'COUNTRY', 'Bahrain', 'BH', '048', 'BHR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BHS', 'COUNTRY', 'Bahamas', 'BS', '044', 'BHS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BIH', 'COUNTRY', 'Bosnia And Herzegowina', 'BA', '070', 'BIH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BLR', 'COUNTRY', 'Belarus', 'BY', '112', 'BLR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BLZ', 'COUNTRY', 'Belize', 'BZ', '084', 'BLZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BMU', 'COUNTRY', 'Bermuda', 'BM', '060', 'BMU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BOL', 'COUNTRY', 'Bolivia', 'BO', '068', 'BOL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-AC', 'STATE', 'Acre', 'AC', null, 'AC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-AL', 'STATE', 'Alagoas', 'AL', null, 'AL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-AM', 'STATE', 'Amazonas', 'AM', null, 'AM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-AP', 'STATE', 'Amapá', 'AP', null, 'AP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-BA', 'STATE', 'Bahia', 'BA', null, 'BA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-CE', 'STATE', 'Ceará', 'CE', null, 'CE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-DF', 'STATE', 'Distrito Federal', 'DF', null, 'DF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-ES', 'STATE', 'Espírito Santo', 'ES', null, 'ES', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-GO', 'STATE', 'Goiás', 'GO', null, 'GO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-MA', 'STATE', 'Maranhão', 'MA', null, 'MA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-MG', 'STATE', 'Minas Gerais', 'MG', null, 'MG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-MS', 'STATE', 'Mato Grosso do Sul', 'MS', null, 'MS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-MT', 'STATE', 'Mato Grosso', 'MT', null, 'MT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-PA', 'STATE', 'Pará', 'PA', null, 'PA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-PB', 'STATE', 'Paraíba', 'PB', null, 'PB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-PE', 'STATE', 'Pernambuco', 'PE', null, 'PE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-PI', 'STATE', 'Piauí', 'PI', null, 'PI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-PR', 'STATE', 'Paraná', 'PR', null, 'PR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-RJ', 'STATE', 'Rio de Janeiro', 'RJ', null, 'RJ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-RN', 'STATE', 'Rio Grande do Norte', 'RN', null, 'RN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-RO', 'STATE', 'Rondônia', 'RO', null, 'RO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-RR', 'STATE', 'Roraima', 'RR', null, 'RR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-RS', 'STATE', 'Rio Grande do Sul', 'RS', null, 'RS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-SC', 'STATE', 'Santa Catarina', 'SC', null, 'SC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-SE', 'STATE', 'Sergipe', 'SE', null, 'SE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-SP', 'STATE', 'São Paulo', 'SP', null, 'SP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BR-TO', 'STATE', 'Tocantins', 'TO', null, 'TO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BRA', 'COUNTRY', 'Brazil', 'BR', '076', 'BRA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BRB', 'COUNTRY', 'Barbados', 'BB', '052', 'BRB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BRN', 'COUNTRY', 'Brunei Darussalam', 'BN', '096', 'BRN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BTN', 'COUNTRY', 'Bhutan', 'BT', '064', 'BTN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BVT', 'COUNTRY', 'Bouvet Island', 'BV', '074', 'BVT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('BWA', 'COUNTRY', 'Botswana', 'BW', '072', 'BWA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CA', 'STATE', 'California', 'CA', null, 'CA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('CAF', 'COUNTRY', 'Central African Republic', 'CF', '140', 'CAF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CAN', 'COUNTRY', 'Canada', 'CA', '124', 'CAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CCK', 'COUNTRY', 'Cocos (keeling) Islands', 'CC', '166', 'CCK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CHE', 'COUNTRY', 'Switzerland', 'CH', '756', 'CHE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CHL', 'COUNTRY', 'Chile', 'CL', '152', 'CHL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CHN', 'COUNTRY', 'China', 'CN', '156', 'CHN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CIV', 'COUNTRY', 'Cote D\'ivoire', 'CI', '384', 'CIV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CMR', 'COUNTRY', 'Cameroon', 'CM', '120', 'CMR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO', 'STATE', 'Colorado', 'CO', null, 'CO', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('CO-AMA', 'STATE', 'Amazonas', 'AMA', null, 'AMA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-ANT', 'STATE', 'Antioquia', 'ANT', null, 'ANT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-ARA', 'STATE', 'Arauca', 'ARA', null, 'ARA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-ATL', 'STATE', 'Atlántico', 'ATL', null, 'ATL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-BOL', 'STATE', 'Bolívar', 'BOL', null, 'BOL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-BOY', 'STATE', 'Boyacá', 'BOY', null, 'BOY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CAL', 'STATE', 'Caldas', 'CAL', null, 'CAL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CAQ', 'STATE', 'Caquetá', 'CAQ', null, 'CAQ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CAS', 'STATE', 'Casanare', 'CAS', null, 'CAS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CAU', 'STATE', 'Cauca', 'CAU', null, 'CAU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CES', 'STATE', 'Cesar', 'CES', null, 'CES', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CHO', 'STATE', 'Chocó', 'CHO', null, 'CHO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-COR', 'STATE', 'Córdoba', 'COR', null, 'COR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-CUN', 'STATE', 'Cundinamarca', 'CUN', null, 'CUN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-DC', 'STATE', 'Bogotá D.C.', 'DC', null, 'DC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-GUA', 'STATE', 'Guainía', 'GUA', null, 'GUA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-GUV', 'STATE', 'Guaviare', 'GUV', null, 'GUV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-HUI', 'STATE', 'Huila', 'HUI', null, 'HUI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-LAG', 'STATE', 'La Guajira', 'LAG', null, 'LAG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-MAG', 'STATE', 'Magdalena', 'MAG', null, 'MAG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-MET', 'STATE', 'Meta', 'MET', null, 'MET', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-NAR', 'STATE', 'Nariño', 'NAR', null, 'NAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-NSA', 'STATE', 'Norte de Santander', 'NSA', null, 'NSA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-PUT', 'STATE', 'Putumayo', 'PUT', null, 'PUT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-QUI', 'STATE', 'Quindío', 'QUI', null, 'QUI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-RIS', 'STATE', 'Risaralda', 'RIS', null, 'RIS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-SAN', 'STATE', 'Santander', 'SAN', null, 'SAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-SAP', 'STATE', 'San Andrés y Providencia', 'SAP', null, 'SAP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-SUC', 'STATE', 'Sucre', 'SUC', null, 'SUC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-TOL', 'STATE', 'Tolima', 'TOL', null, 'TOL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-VAC', 'STATE', 'Valle del Cauca', 'VAC', null, 'VAC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-VAU', 'STATE', 'Vaupés', 'VAU', null, 'VAU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CO-VID', 'STATE', 'Vichada', 'VID', null, 'VID', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('COD', 'COUNTRY', 'Congo, The Democratic Republic Of The', 'CD', '180', 'COD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('COG', 'COUNTRY', 'Congo', 'CG', '178', 'COG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('COK', 'COUNTRY', 'Cook Islands', 'CK', '184', 'COK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('COL', 'COUNTRY', 'Colombia', 'CO', '170', 'COL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('COM', 'COUNTRY', 'Comoros', 'KM', '174', 'COM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CPV', 'COUNTRY', 'Cape Verde', 'CV', '132', 'CPV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CRI', 'COUNTRY', 'Costa Rica', 'CR', '188', 'CRI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CT', 'STATE', 'Connecticut', 'CT', null, 'CT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('CUB', 'COUNTRY', 'Cuba', 'CU', '192', 'CUB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CXR', 'COUNTRY', 'Christmas Island', 'CX', '162', 'CXR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CYM', 'COUNTRY', 'Cayman Islands', 'KY', '136', 'CYM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CYP', 'COUNTRY', 'Cyprus', 'CY', '196', 'CYP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('CZE', 'COUNTRY', 'Czech Republic', 'CZ', '203', 'CZE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DC', 'STATE', 'District of Columbia', 'DC', null, 'DC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('DE', 'STATE', 'Delaware', 'DE', null, 'DE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('DE-BE', 'STATE', 'Berlin', 'BE', null, 'BE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-BR', 'STATE', 'Brandenburg', 'BR', null, 'BR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-BW', 'STATE', 'Baden-Württemberg', 'BW', null, 'BW', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-BY', 'STATE', 'Bayern', 'BY', null, 'BY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-HB', 'STATE', 'Bremen', 'HB', null, 'HB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-HE', 'STATE', 'Hessen', 'HE', null, 'HE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-HH', 'STATE', 'Hamburg', 'HH', null, 'HH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-MV', 'STATE', 'Mecklenburg-Vorpommern', 'MV', null, 'MV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-NI', 'STATE', 'Niedersachsen', 'NI', null, 'NI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-NW', 'STATE', 'Nordrhein-Westfalen', 'NW', null, 'NW', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-RP', 'STATE', 'Rheinland-Pfalz', 'RP', null, 'RP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-SA', 'STATE', 'Sachsen-Anhalt', 'SA', null, 'SA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-SH', 'STATE', 'Schleswig-Holstein', 'SH', null, 'SH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-SL', 'STATE', 'Saarland', 'SL', null, 'SL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-SN', 'STATE', 'Sachsen', 'SN', null, 'SN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DE-TH', 'STATE', 'Thüringen', 'TH', null, 'TH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DEU', 'COUNTRY', 'Germany', 'DE', '276', 'DEU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DJI', 'COUNTRY', 'Djibouti', 'DJ', '262', 'DJI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DMA', 'COUNTRY', 'Dominica', 'DM', '212', 'DMA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DNK', 'COUNTRY', 'Denmark', 'DK', '208', 'DNK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DOM', 'COUNTRY', 'Dominican Republic', 'DO', '214', 'DOM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('DZA', 'COUNTRY', 'Algeria', 'DZ', '012', 'DZA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ECU', 'COUNTRY', 'Ecuador', 'EC', '218', 'ECU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('EGY', 'COUNTRY', 'Egypt', 'EG', '818', 'EGY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ENGL', 'COUNTRY', 'England', 'ENG', '896', 'ENGL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ERI', 'COUNTRY', 'Eritrea', 'ER', '232', 'ERI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-A', 'PROVINCE', 'Alicante', 'A', null, 'A', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-AB', 'PROVINCE', 'Albacete', 'AB', null, 'AB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-AL', 'PROVINCE', 'Almería', 'AL', null, 'AL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-AN', 'REGION', 'Andalucía', 'AN', null, 'AN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-AR', 'REGION', 'Aragón', 'AR', null, 'AR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-AS', 'REGION', 'Oviedo', 'O', null, 'O', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-AV', 'PROVINCE', 'Ávila', 'AV', null, 'AV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-B', 'PROVINCE', 'Barcelona', 'B', null, 'B', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-BA', 'PROVINCE', 'Badajoz', 'BA', null, 'BA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-BI', 'PROVINCE', 'Vizcaya', 'BI', null, 'BI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-BU', 'PROVINCE', 'Burgos', 'BU', null, 'BU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-C', 'PROVINCE', 'La Coruña', 'C', null, 'C', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CA', 'PROVINCE', 'Cádiz', 'CA', null, 'CA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CAN', 'REGION', 'Cantabria', 'S', null, 'S', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CC', 'PROVINCE', 'Cáceres', 'CC', null, 'CC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CE', 'PROVINCE', 'Ceuta', 'CE', null, 'CE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CL', 'REGION', 'Castilla y León', 'CL', null, 'CL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CM', 'REGION', 'Castilla-La Mancha', 'CM', null, 'CM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CN', 'REGION', 'Canarias', 'CN', null, 'CN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CO', 'PROVINCE', 'Córdoba', 'CO', null, 'CO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CR', 'PROVINCE', 'Ciudad Real', 'CR', null, 'CR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CS', 'PROVINCE', 'Castellón', 'CS', null, 'CS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CT', 'REGION', 'Cataluña', 'CT', null, 'CT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-CU', 'PROVINCE', 'Cuenca', 'CU', null, 'CU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-EX', 'REGION', 'Extremadura', 'EX', null, 'EX', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-GA', 'REGION', 'Galicia', 'GA', null, 'GA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-GC', 'PROVINCE', 'Las Palmas', 'GC', null, 'GC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-GI', 'PROVINCE', 'Gerona', 'GI', null, 'GI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-GR', 'PROVINCE', 'Granada', 'GR', null, 'GR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-GU', 'PROVINCE', 'Guadalajara', 'GU', null, 'GU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-H', 'PROVINCE', 'Huelva', 'H', null, 'H', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-HU', 'PROVINCE', 'Huesca', 'HU', null, 'HU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-IB', 'REGION', 'Islas Baleares', 'IB', null, 'IB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-J', 'PROVINCE', 'Jaén', 'J', null, 'J', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-L', 'PROVINCE', 'Lérida', 'L', null, 'L', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-LE', 'PROVINCE', 'León', 'LE', null, 'LE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-LO', 'PROVINCE', 'Logroño', 'LO', null, 'LO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-LR', 'REGION', 'La Rioja', 'LO', null, 'LO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-LU', 'PROVINCE', 'Lugo', 'LU', null, 'LU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-M', 'PROVINCE', 'Madrid', 'M', null, 'M', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-MA', 'PROVINCE', 'Málaga', 'MA', null, 'MA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-MAD', 'REGION', 'Madrid', 'M', null, 'M', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-ML', 'PROVINCE', 'Melilla', 'ML', null, 'ML', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-MU', 'PROVINCE', 'Murcia', 'MU', null, 'MU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-MUR', 'REGION', 'Región de Murcia', 'MU', null, 'MU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-NA', 'PROVINCE', 'Navarra', 'NA', null, 'NA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-NAV', 'REGION', 'Navarra', 'NA', null, 'NA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-O', 'PROVINCE', 'Asturias', 'O', null, 'O', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-OR', 'PROVINCE', 'Orense', 'OR', null, 'OR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-P', 'PROVINCE', 'Palencia', 'P', null, 'P', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-PM', 'PROVINCE', 'Islas Baleares', 'PM', null, 'PM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-PO', 'PROVINCE', 'Pontevedra', 'PO', null, 'PO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-PV', 'REGION', 'País Vasco', 'PV', null, 'PV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-S', 'PROVINCE', 'Cantabria', 'S', null, 'S', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-SA', 'PROVINCE', 'Salamanca', 'SA', null, 'SA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-SE', 'PROVINCE', 'Sevilla', 'SE', null, 'SE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-SG', 'PROVINCE', 'Segovia', 'SG', null, 'SG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-SO', 'PROVINCE', 'Soria', 'SO', null, 'SO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-SS', 'PROVINCE', 'Guipúzcoa', 'SS', null, 'SS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-T', 'PROVINCE', 'Tarragona', 'T', null, 'T', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-TE', 'PROVINCE', 'Teruel', 'TE', null, 'TE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-TF', 'PROVINCE', 'Santa Cruz de Tenerife', 'TF', null, 'TF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-TO', 'PROVINCE', 'Toledo', 'TO', null, 'TO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-V', 'PROVINCE', 'Valencia', 'V', null, 'V', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-VA', 'PROVINCE', 'Valladolid', 'VA', null, 'VA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-VC', 'REGION', 'Comunidad Valenciana', 'VC', null, 'VC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-VI', 'PROVINCE', 'Álava', 'VI', null, 'VI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-Z', 'PROVINCE', 'Zaragoza', 'Z', null, 'Z', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ES-ZA', 'PROVINCE', 'Zamora', 'ZA', null, 'ZA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ESH', 'COUNTRY', 'Western Sahara', 'EH', '732', 'ESH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ESP', 'COUNTRY', 'Spain', 'ES', '724', 'ESP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('EST', 'COUNTRY', 'Estonia', 'EE', '233', 'EST', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ETH', 'COUNTRY', 'Ethiopia', 'ET', '231', 'ETH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('EU', 'GROUP', 'European Union', 'EU', null, 'EU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FIN', 'COUNTRY', 'Finland', 'FI', '246', 'FIN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FJI', 'COUNTRY', 'Fiji', 'FJ', '242', 'FJI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FL', 'STATE', 'Florida', 'FL', null, 'FL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('FLK', 'COUNTRY', 'Falkland Islands (malvinas)', 'FK', '238', 'FLK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FM', 'STATE', 'Federated States of Micronesia', 'FM', null, 'FM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('FR-01', 'COUNTY', 'Ain', '01', null, 'AIN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-02', 'COUNTY', 'Aisne', '02', null, 'AIS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-03', 'COUNTY', 'Allier', '03', null, 'ALL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-04', 'COUNTY', 'Alpes de Hautes-Provence', '04', null, 'AHP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-05', 'COUNTY', 'Hautes-Alpes', '05', null, 'HAL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-06', 'COUNTY', 'Alpes-Maritimes', '06', null, 'ALM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-07', 'COUNTY', 'Ardèche', '07', null, 'ARD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-08', 'COUNTY', 'Ardennes', '08', null, 'ARE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-09', 'COUNTY', 'Ariège', '09', null, 'ARI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-10', 'COUNTY', 'Aube', '10', null, 'AUB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-11', 'COUNTY', 'Aude', '11', null, 'AUD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-12', 'COUNTY', 'Aveyron', '12', null, 'AVE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-13', 'COUNTY', 'Bouches-du-Rhône', '13', null, 'BDR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-14', 'COUNTY', 'Calvados', '14', null, 'CAL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-15', 'COUNTY', 'Cantal', '15', null, 'CAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-16', 'COUNTY', 'Charente', '16', null, 'CHA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-17', 'COUNTY', 'Charente-Maritime', '17', null, 'CHM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-18', 'COUNTY', 'Cher', '18', null, 'CHE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-19', 'COUNTY', 'Corrèze', '19', null, 'COR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-20', 'COUNTY', 'Corse', '20', null, 'COS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-21', 'COUNTY', 'Côte-d\'Or', '21', null, 'COO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-22', 'COUNTY', 'Côtes d\'Armor', '22', null, 'COA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-23', 'COUNTY', 'Creuse', '23', null, 'CRE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-24', 'COUNTY', 'Dordogne', '24', null, 'DOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-25', 'COUNTY', 'Doubs', '25', null, 'DOU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-26', 'COUNTY', 'Drôme', '26', null, 'DRO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-27', 'COUNTY', 'Eure', '27', null, 'EUR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-28', 'COUNTY', 'Eure-et-Loir', '28', null, 'EUL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-29', 'COUNTY', 'Finistère', '29', null, 'FIN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-2A', 'COUNTY', 'Corse-du-Sud', '2A', null, 'CDS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-2B', 'COUNTY', 'Haute-Corse', '2B', null, 'HCO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-30', 'COUNTY', 'Gard', '30', null, 'GAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-31', 'COUNTY', 'Haute-Garonne', '31', null, 'HAG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-32', 'COUNTY', 'Gers', '32', null, 'GER', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-33', 'COUNTY', 'Gironde', '33', null, 'GIR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-34', 'COUNTY', 'Hérault', '34', null, 'HER', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-35', 'COUNTY', 'Ille-et-Vilaine', '35', null, 'ILL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-36', 'COUNTY', 'Indre', '36', null, 'IND', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-37', 'COUNTY', 'Indre-et-Loire', '37', null, 'IEL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-38', 'COUNTY', 'Isère', '38', null, 'ISE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-39', 'COUNTY', 'Jura', '39', null, 'JUR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-40', 'COUNTY', 'Landes', '40', null, 'LAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-41', 'COUNTY', 'Loir-et-Cher', '41', null, 'LEC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-42', 'COUNTY', 'Loire', '42', null, 'LOI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-43', 'COUNTY', 'Haute-Loire', '43', null, 'HLO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-44', 'COUNTY', 'Loire-Atlantique', '44', null, 'LOA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-45', 'COUNTY', 'Loiret', '45', null, 'LOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-46', 'COUNTY', 'Lot', '46', null, 'LOT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-47', 'COUNTY', 'Lot-et-Garonne', '47', null, 'LOG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-48', 'COUNTY', 'Lozère', '48', null, 'LOZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-49', 'COUNTY', 'Maine-et-Loire', '49', null, 'MEL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-50', 'COUNTY', 'Manche', '50', null, 'MAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-51', 'COUNTY', 'Marne', '51', null, 'MAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-52', 'COUNTY', 'Haute-Marne', '52', null, 'HMA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-53', 'COUNTY', 'Mayenne', '53', null, 'MAY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-54', 'COUNTY', 'Meurthe-et-Moselle', '54', null, 'MEM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-55', 'COUNTY', 'Meuse', '55', null, 'MEU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-56', 'COUNTY', 'Morbihan', '56', null, 'MOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-57', 'COUNTY', 'Moselle', '57', null, 'MOS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-58', 'COUNTY', 'Nièvre', '58', null, 'NIE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-59', 'COUNTY', 'Nord', '59', null, 'NOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-60', 'COUNTY', 'Oise', '60', null, 'OIS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-61', 'COUNTY', 'Orne', '61', null, 'ORN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-62', 'COUNTY', 'Pas-de-Calais', '62', null, 'PDC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-63', 'COUNTY', 'Puy-de-Dôme', '63', null, 'PUY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-64', 'COUNTY', 'Pyrénées-Atlantiques', '64', null, 'PYA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-65', 'COUNTY', 'Hautes-Pyrénées', '65', null, 'HPY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-66', 'COUNTY', 'Pyrénées-Orientales', '66', null, 'PYO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-67', 'COUNTY', 'Bas-Rhin', '67', null, 'BRH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-68', 'COUNTY', 'Haut-Rhin', '68', null, 'HRH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-69', 'COUNTY', 'Rhône', '69', null, 'RHO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-70', 'COUNTY', 'Haute-Saône', '70', null, 'HAS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-71', 'COUNTY', 'Saône-et-Loire', '71', null, 'SEL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-72', 'COUNTY', 'Sarthe', '72', null, 'SAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-73', 'COUNTY', 'Savoie', '73', null, 'SAV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-74', 'COUNTY', 'Haute-Savoie', '74', null, 'HAS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-75', 'COUNTY', 'Paris', '75', null, 'PAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-76', 'COUNTY', 'Seine-Maritime', '76', null, 'SMA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-77', 'COUNTY', 'Seine-et-Marne', '77', null, 'SEM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-78', 'COUNTY', 'Yvelines', '78', null, 'YVE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-79', 'COUNTY', 'Deux-Sèvres', '79', null, 'DSE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-80', 'COUNTY', 'Somme', '80', null, 'SOM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-81', 'COUNTY', 'Tarn', '81', null, 'TAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-82', 'COUNTY', 'Tarn-et-Garonne', '82', null, 'TAG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-83', 'COUNTY', 'Var', '83', null, 'VAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-84', 'COUNTY', 'Vaucluse', '84', null, 'VAU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-85', 'COUNTY', 'Vendée', '85', null, 'VEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-86', 'COUNTY', 'Vienne', '86', null, 'VIE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-87', 'COUNTY', 'Haute-Vienne', '87', null, 'HVI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-88', 'COUNTY', 'Vosges', '88', null, 'VOS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-89', 'COUNTY', 'Yonne', '89', null, 'YON', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-90', 'COUNTY', 'Territoire-de-Belfort', '90', null, 'TDB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-91', 'COUNTY', 'Essonne', '91', null, 'ESS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-92', 'COUNTY', 'Hauts-de-Seine', '92', null, 'HDS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-93', 'COUNTY', 'Seine-Saint-Denis', '93', null, 'SSD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-94', 'COUNTY', 'Val-de-Marne', '94', null, 'VDM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-95', 'COUNTY', 'Val-d\'Oise', '95', null, 'VDO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-971', 'COUNTY', 'Guadeloupe', '971', null, 'GUA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-972', 'COUNTY', 'Guyane', '972', null, 'GUY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-973', 'COUNTY', 'Martinique', '973', null, 'MAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-974', 'COUNTY', 'La Réunion', '974', null, 'REU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-975', 'COUNTY', 'St Pierre et Miquelon', '975', null, 'SPM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-976', 'COUNTY', 'Mayotte', '976', null, 'MAY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-98', 'COUNTY', 'Monaco', '98', null, 'MON', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-986', 'COUNTY', 'Wallis et Futuna', '986', null, 'WeF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-987', 'COUNTY', 'Polynésie Française', '987', null, 'POF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-988', 'COUNTY', 'Nouvelle Calédonie', '988', null, 'NOC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ACY74', 'COUNTY_CITY', 'Annecy', 'ACY', null, 'ACY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AGE47', 'COUNTY_CITY', 'Agen', 'AGE', null, 'AGE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AJA2A', 'COUNTY_CITY', 'Ajaccio', 'AJA', null, 'AJA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ALB81', 'COUNTY_CITY', 'Albi', 'ALB', null, 'ALB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ALE61', 'COUNTY_CITY', 'Alençon', 'ALE', null, 'ALE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ALS', 'REGION', 'Alsace', 'ALS', null, 'ALS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AMI80', 'COUNTY_CITY', 'Amiens', 'AMI', null, 'AMI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ANG16', 'COUNTY_CITY', 'Angoulême', 'ANG', null, 'ANG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ANG49', 'COUNTY_CITY', 'Angers', 'ANG', null, 'ANG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AQU', 'REGION', 'Aquitaine', 'AQU', null, 'AQU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ARR62', 'COUNTY_CITY', 'Arras', 'ARR', null, 'ARR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AUC32', 'COUNTY_CITY', 'Auch', 'AUC', null, 'AUC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AUR15', 'COUNTY_CITY', 'Aurillac', 'AUR', null, 'AUR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AUV', 'REGION', 'Auvergne', 'AUV', null, 'AUV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AVG84', 'COUNTY_CITY', 'Avignon', 'AVG', null, 'AVG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-AXR89', 'COUNTY_CITY', 'Auxerre', 'AXR', null, 'AXR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BAN', 'REGION', 'Basse-Normandie', 'BAN', null, 'BAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BAS2B', 'COUNTY_CITY', 'Bastia', 'BAS', null, 'BAS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BBY93', 'COUNTY_CITY', 'Bobigny', 'BBY', null, 'BBY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BEB01', 'COUNTY_CITY', 'Bourg-en-Bresse', 'BEB', null, 'BEB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BES25', 'COUNTY_CITY', 'Besançon', 'BES', null, 'BES', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BLD55', 'COUNTY_CITY', 'Bar-le-Duc', 'BLD', null, 'BLD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BLF90', 'COUNTY_CITY', 'Belfort', 'BLF', null, 'BLF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BLO41', 'COUNTY_CITY', 'Blois', 'BLO', null, 'BLO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BOR33', 'COUNTY_CITY', 'Bordeaux', 'BOR', null, 'BOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BOU', 'REGION', 'Bourgogne', 'BOU', null, 'BOU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BOU18', 'COUNTY_CITY', 'Bourges', 'BOU', null, 'BOU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BRE', 'REGION', 'Bretagne', 'BRE', null, 'BRE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-BVA60', 'COUNTY_CITY', 'Beauvais', 'BVA', null, 'BVA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CAE14', 'COUNTY_CITY', 'Caen', 'CAE', null, 'CAE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CAH46', 'COUNTY_CITY', 'Cahors', 'CAH', null, 'CAH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CAR11', 'COUNTY_CITY', 'Carcassonne', 'CAR', null, 'CAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CBY73', 'COUNTY_CITY', 'Chambéry', 'CBY', null, 'CBY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CEC51', 'COUNTY_CITY', 'Châlons-en-Champagne', 'CEC', null, 'CEC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CEN', 'REGION', 'Centre', 'CEN', null, 'CEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CHA', 'REGION', 'Champagne-Ardenne', 'CHA', null, 'CHA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CHA28', 'COUNTY_CITY', 'Chartres', 'CHA', null, 'CHA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CHM08', 'COUNTY_CITY', 'Charleville-Mézières', 'CHM', null, 'CHM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CHM52', 'COUNTY_CITY', 'Chaumont', 'CHM', null, 'CHM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CHT36', 'COUNTY_CITY', 'Châteauroux', 'CHT', null, 'CHT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CLF63', 'COUNTY_CITY', 'Clermont-Ferrand', 'CLF', null, 'CLF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CLM68', 'COUNTY_CITY', 'Colmar', 'CLM', null, 'CLM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-COR', 'REGION', 'Corse', 'COR', null, 'COR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-CTL94', 'COUNTY_CITY', 'Créteil', 'CTL', null, 'CTL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-DIG04', 'COUNTY_CITY', 'Digne', 'DIG', null, 'DIG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-DIJ21', 'COUNTY_CITY', 'Dijon', 'DIJ', null, 'DIJ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-DOM', 'REGION', 'Departement d\'outre Mer', 'DOM', null, 'DOM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-EPI88', 'COUNTY_CITY', 'Épinal', 'EPI', null, 'EPI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-EVR27', 'COUNTY_CITY', 'Évreux', 'EVR', null, 'EVR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-EVR91', 'COUNTY_CITY', 'Évry', 'EVR', null, 'EVR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-FCO', 'REGION', 'Franche-Comté', 'FCO', null, 'FCO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-FOI09', 'COUNTY_CITY', 'Foix', 'FOI', null, 'FOI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-GAP05', 'COUNTY_CITY', 'Gap', 'GAP', null, 'GAP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-GRE38', 'COUNTY_CITY', 'Grenoble', 'GRE', null, 'GRE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-GUE23', 'COUNTY_CITY', 'Guéret', 'GUE', null, 'GUE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-HNO', 'REGION', 'Haute-Normandie', 'HNO', null, 'HNO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-IDF', 'REGION', 'Ile-de-France', 'IDF', null, 'IDF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LAO02', 'COUNTY_CITY', 'Laon', 'LAO', null, 'LAO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LAR', 'REGION', 'Languedoc-Roussillon', 'LAR', null, 'LAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LIL59', 'COUNTY_CITY', 'Lille', 'LIL', null, 'LIL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LIM', 'REGION', 'Limousin', 'LIM', null, 'LIM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LMA72', 'COUNTY_CITY', 'Le Mans', 'LMA', null, 'LMA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LMG87', 'COUNTY_CITY', 'Limoges', 'LMG', null, 'LMG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LON39', 'COUNTY_CITY', 'Lons-le-Saunier', 'LON', null, 'LON', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LOR', 'REGION', 'Lorraine', 'LOR', null, 'LOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LRO17', 'COUNTY_CITY', 'La Rochelle', 'LRO', null, 'LRO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LVL53', 'COUNTY_CITY', 'Laval', 'LVL', null, 'LVL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-LYO69', 'COUNTY_CITY', 'Lyon', 'LYO', null, 'LYO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MAR13', 'COUNTY_CITY', 'Marseille', 'MAR', null, 'MAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MCN71', 'COUNTY_CITY', 'Mâcon', 'MCN', null, 'MCN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MDM40', 'COUNTY_CITY', 'Mont-de-Marsan', 'MDM', null, 'MDM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MEN48', 'COUNTY_CITY', 'Mende', 'MEN', null, 'MEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MET57', 'COUNTY_CITY', 'Metz', 'MET', null, 'MET', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MIP', 'REGION', 'Midi-Pyrénées', 'MIP', null, 'MIP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MLN77', 'COUNTY_CITY', 'Melun', 'MLN', null, 'MLN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MON34', 'COUNTY_CITY', 'Montpellier', 'MON', null, 'MON', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MOU03', 'COUNTY_CITY', 'Moulins', 'MOU', null, 'MOU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-MTB82', 'COUNTY_CITY', 'Montauban', 'MTB', null, 'MTB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NAN44', 'COUNTY_CITY', 'Nantes', 'NAN', null, 'NAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NCY54', 'COUNTY_CITY', 'Nancy', 'NCY', null, 'NCY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NEV58', 'COUNTY_CITY', 'Nevers', 'NEV', null, 'NEV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NIC06', 'COUNTY_CITY', 'Nice', 'NIC', null, 'NIC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NIM30', 'COUNTY_CITY', 'Nîmes', 'NIM', null, 'NIM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NIO79', 'COUNTY_CITY', 'Niort', 'NIO', null, 'NIO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NPC', 'REGION', 'Nord-Pas-de-Calais', 'NPC', null, 'NPC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-NTR92', 'COUNTY_CITY', 'Nanterre', 'NTR', null, 'NTR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ORL45', 'COUNTY_CITY', 'Orléans', 'ORL', null, 'ORL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PAC', 'REGION', 'Provence-Alpes-Côte d\'Azur', 'PAC', null, 'PAC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PAR75', 'COUNTY_CITY', 'Paris', 'PAR', null, 'PAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PAU64', 'COUNTY_CITY', 'Pau', 'PAU', null, 'PAU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PDL', 'REGION', 'Pays de la Loire', 'PDL', null, 'PDL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PER24', 'COUNTY_CITY', 'Périgueux', 'PER', null, 'PER', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PEV43', 'COUNTY_CITY', 'Le Puy-en-Velay', 'PEV', null, 'PEV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PIC', 'REGION', 'Picardie', 'PIC', null, 'PIC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-POC', 'REGION', 'Poitou-Charentes', 'POC', null, 'POC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-POI86', 'COUNTY_CITY', 'Poitiers', 'POI', null, 'POI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PPG66', 'COUNTY_CITY', 'Perpignan', 'PPG', null, 'PPG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PRI07', 'COUNTY_CITY', 'Privas', 'PRI', null, 'PRI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-PTO95', 'COUNTY_CITY', 'Pontoise', 'PTO', null, 'PTO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-QUI29', 'COUNTY_CITY', 'Quimper', 'QUI', null, 'QUI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-RAL', 'REGION', 'Rhône-Alpes', 'RAL', null, 'RAL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-REN35', 'COUNTY_CITY', 'Rennes', 'REN', null, 'REN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ROD12', 'COUNTY_CITY', 'Rodez', 'ROD', null, 'ROD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-ROU76', 'COUNTY_CITY', 'Rouen', 'ROU', null, 'ROU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-RSY85', 'COUNTY_CITY', 'La Roche-sur-Yon', 'RSY', null, 'RSY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-SBG67', 'COUNTY_CITY', 'Strasbourg', 'SBG', null, 'SBG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-SBR22', 'COUNTY_CITY', 'Saint-Brieuc', 'SBR', null, 'SBR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-SET42', 'COUNTY_CITY', 'Saint-Étienne', 'SET', null, 'SET', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-SLO50', 'COUNTY_CITY', 'Saint-Lô', 'SLO', null, 'SLO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TBS65', 'COUNTY_CITY', 'Tarbes', 'TBS', null, 'TBS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TLN83', 'COUNTY_CITY', 'Toulon', 'TLN', null, 'TLN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TLS31', 'COUNTY_CITY', 'Toulouse', 'TLS', null, 'TLS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TOM', 'REGION', 'Territoire d\'outre Mer', 'TOM', null, 'TOM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TOU37', 'COUNTY_CITY', 'Tours', 'TOU', null, 'TOU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TRO10', 'COUNTY_CITY', 'Troyes', 'TRO', null, 'TRO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-TUL19', 'COUNTY_CITY', 'Tulle', 'TUL', null, 'TUL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-VAL26', 'COUNTY_CITY', 'Valence', 'VAL', null, 'VAL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-VAN56', 'COUNTY_CITY', 'Vannes', 'VAN', null, 'VAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-VSL70', 'COUNTY_CITY', 'Vesoul', 'VSL', null, 'VSL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FR-VSS78', 'COUNTY_CITY', 'Versailles', 'VSS', null, 'VSS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FRA', 'COUNTRY', 'France', 'FR', '250', 'FRA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FRO', 'COUNTRY', 'Faroe Islands', 'FO', '234', 'FRO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FSM', 'COUNTRY', 'Micronesia, Federated States Of', 'FM', '583', 'FSM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('FXX', 'COUNTRY', 'France, Metropolitan', 'FX', '249', 'FXX', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GA', 'STATE', 'Georgia', 'GA', null, 'GA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GAB', 'COUNTRY', 'Gabon', 'GA', '266', 'GAB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GB-ABDN', 'COUNTY', 'Aberdeenshire', 'ABDN', null, 'ABDN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-AGSY', 'COUNTY', 'Anglesey/Sir Fon', 'AGSY', null, 'AGSY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ANGS', 'COUNTY', 'Angus/Forfarshire', 'ANGS', null, 'ANGS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ARGL', 'COUNTY', 'Argyllshire', 'ARGL', null, 'ARGL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-AYRS', 'COUNTY', 'Ayrshire', 'AYRS', null, 'AYRS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BANF', 'COUNTY', 'Banffshire', 'BANF', null, 'BANF', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BDFD', 'COUNTY', 'Bedfordshire', 'BDFD', null, 'BDFD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BREK', 'COUNTY', 'Brecknockshire/Sir Frycheiniog', 'BREK', null, 'BREK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BRKS', 'COUNTY', 'Berkshire', 'BRKS', null, 'BRKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BUCKS', 'COUNTY', 'Buckinghamshire', 'BUCKS', null, 'BUCKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BUTE', 'COUNTY', 'Buteshire', 'BUTE', null, 'BUTE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-BWKS', 'COUNTY', 'Berwickshire', 'BWKS', null, 'BWKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CAMBS', 'COUNTY', 'Cambridgeshire', 'CAMBS', null, 'CAMBS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CDGN', 'COUNTY', 'Cardiganshire/Ceredigion', 'CDGN', null, 'CDGN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CHES', 'COUNTY', 'Cheshire', 'CHES', null, 'CHES', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CLAK', 'COUNTY', 'Clackmannanshire', 'CLAK', null, 'CLAK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CMRN', 'COUNTY', 'Carmarthenshire/Sir Gaerfyrddin', 'CMRN', null, 'CMRN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CMTY', 'COUNTY', 'Cromartyshire', 'CMTY', null, 'CMTY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CNFN', 'COUNTY', 'Caernarfonshire/Sir Gaernarfon', 'CNFN', null, 'CNFN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CNWL', 'COUNTY', 'Cornwall', 'CNWL', null, 'CNWL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CTHN', 'COUNTY', 'Caithness', 'CTHN', null, 'CTHN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-CUMB', 'COUNTY', 'Cumberland', 'CUMB', null, 'CUMB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DENB', 'COUNTY', 'Denbighshire/Sir Ddinbych', 'DENB', null, 'DENB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DEVON', 'COUNTY', 'Devon', 'DEVON', null, 'DEVON', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DMBR', 'COUNTY', 'Dunbartonshire/Dumbartonshire', 'DMBR', null, 'DMBR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DMFS', 'COUNTY', 'Dumfriesshire', 'DMFS', null, 'DMFS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DRBYS', 'COUNTY', 'Derbyshire', 'DRBYS', null, 'DRBYS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DRHM', 'COUNTY', 'Durham', 'DRHM', null, 'DRHM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-DRST', 'COUNTY', 'Dorset', 'DRST', null, 'DRST', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ELOTH', 'COUNTY', 'East Lothian/Haddingtonshire', 'ELOTH', null, 'ELOTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ESSX', 'COUNTY', 'Essex', 'ESSX', null, 'ESSX', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-FIFE', 'COUNTY', 'Fife', 'FIFE', null, 'FIFE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-FTSH', 'COUNTY', 'Flintshire/Sir Fflint', 'FTSH', null, 'FTSH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-GLAM', 'COUNTY', 'Glamorgan/Morgannwg', 'GLAM', null, 'GLAM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-GLOU', 'COUNTY', 'Gloucestershire', 'GLOU', null, 'GLOU', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-HAMPS', 'COUNTY', 'Hampshire', 'HAMPS', null, 'HAMPS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-HERTS', 'COUNTY', 'Hertfordshire', 'HERTS', null, 'HERTS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-HRFDS', 'COUNTY', 'Herefordshire', 'HRFDS', null, 'HRFDS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-HUNTS', 'COUNTY', 'Huntingdonshire', 'HUNTS', null, 'HUNTS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-INVER', 'COUNTY', 'Inverness-shire', 'INVER', null, 'INVER', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-KENT', 'COUNTY', 'Kent', 'KENT', null, 'KENT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-KNDN', 'COUNTY', 'Kincardineshire', 'KNDN', null, 'KNDN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-KRCUD', 'COUNTY', 'Kirkcudbrightshire', 'KRCUD', null, 'KRCUD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-KRSS', 'COUNTY', 'Kinross-shire', 'KRSS', null, 'KRSS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-LANCS', 'COUNTY', 'Lancashire', 'LANCS', null, 'LANCS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-LIECS', 'COUNTY', 'Leicestershire', 'LIECS', null, 'LIECS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-LINCS', 'COUNTY', 'Lincolnshire', 'LINCS', null, 'LINCS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-LRKS', 'COUNTY', 'Lanarkshire', 'LRKS', null, 'LRKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-MIENT', 'COUNTY', 'Merioneth/Meirionnydd', 'MIENT', null, 'MIENT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-MLOTH', 'COUNTY', 'Midlothian/Edinburghshire', 'MLOTH', null, 'MLOTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-MMTH', 'COUNTY', 'Monmouthshire/Sir Fynwy', 'MMTH', null, 'MMTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-MNTGS', 'COUNTY', 'Montgomeryshire/Sir Drefaldwyn', 'MNTGS', null, 'MNTGS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-MORAY', 'COUNTY', 'Morayshire', 'MORAY', null, 'MORAY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-MSEX', 'COUNTY', 'Middlesex', 'MSEX', null, 'MSEX', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-NAIRN', 'COUNTY', 'Nairnshire', 'NAIRN', null, 'NAIRN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-NFLK', 'COUNTY', 'Norfolk', 'NFLK', null, 'NFLK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-NHANTS', 'COUNTY', 'Northamptonshire', 'NHANTS', null, 'NHANTS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-NHUMB', 'COUNTY', 'Northumberland', 'NHUMB', null, 'NHUMB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-NOTTS', 'COUNTY', 'Nottinghamshire', 'NOTTS', null, 'NOTTS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ORK', 'COUNTY', 'Orkney', 'ORK', null, 'ORK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-OXFD', 'COUNTY', 'Oxfordshire', 'OXFD', null, 'OXFD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-PEEBS', 'COUNTY', 'Peeblesshire', 'PEEBS', null, 'PEEBS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-PERTH', 'COUNTY', 'Perthshire', 'PERTH', null, 'PERTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-PMBRK', 'COUNTY', 'Pembrokeshire/Sir Benfro', 'PMBRK', null, 'PMBRK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-RNFR', 'COUNTY', 'Renfrewshire', 'RNFR', null, 'RNFR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-RNRS', 'COUNTY', 'Radnorshire/Sir Faesyfed', 'RNRS', null, 'RNRS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ROSS', 'COUNTY', 'Ross-shire', 'ROSS', null, 'ROSS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-ROXB', 'COUNTY', 'Roxburghshire', 'ROXB', null, 'ROXB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-RUTL', 'COUNTY', 'Rutland', 'RUTL', null, 'RUTL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SELKS', 'COUNTY', 'Selkirkshire', 'SELKS', null, 'SELKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SFFK', 'COUNTY', 'Suffolk', 'SFFK', null, 'SFFK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SHET', 'COUNTY', 'Shetland', 'SHET', null, 'SHET', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SHROPS', 'COUNTY', 'Shropshire', 'SHROPS', null, 'SHROPS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SOMST', 'COUNTY', 'Somerset', 'SOMST', null, 'SOMST', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-STAFFS', 'COUNTY', 'Staffordshire', 'STAFFS', null, 'STAFFS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-STLNG', 'COUNTY', 'Stirlingshire', 'STLNG', null, 'STLNG', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SURR', 'COUNTY', 'Surrey', 'SURR', null, 'SURR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SUSX', 'COUNTY', 'Sussex', 'SUSX', null, 'SUSX', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-SUTH', 'COUNTY', 'Sutherland', 'SUTH', null, 'SUTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-WGNSH', 'COUNTY', 'Wigtownshire', 'WGNSH', null, 'WGNSH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-WILTS', 'COUNTY', 'Wiltshire', 'WILTS', null, 'WILTS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-WLOTH', 'COUNTY', 'West Lothian/Linlithgowshire', 'WLOTH', null, 'WLOTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-WMLD', 'COUNTY', 'Westmorland', 'WMLD', null, 'WMLD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-WORCS', 'COUNTY', 'Worcestershire', 'WORCS', null, 'WORCS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-WWKS', 'COUNTY', 'Warwickshire', 'WWKS', null, 'WWKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GB-YORKS', 'COUNTY', 'Yorkshire', 'YORKS', null, 'YORKS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GBR', 'COUNTRY', 'United Kingdom', 'GB', '826', 'GBR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GEO', 'COUNTRY', 'Georgia', 'GE', '268', 'GEO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GHA', 'COUNTRY', 'Ghana', 'GH', '288', 'GHA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GIB', 'COUNTRY', 'Gibraltar', 'GI', '292', 'GIB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GIN', 'COUNTRY', 'Guinea', 'GN', '324', 'GIN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GLP', 'COUNTRY', 'Guadeloupe', 'GP', '312', 'GLP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GMB', 'COUNTRY', 'Gambia', 'GM', '270', 'GMB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GNB', 'COUNTRY', 'Guinea-bissau', 'GW', '624', 'GNB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GNQ', 'COUNTRY', 'Equatorial Guinea', 'GQ', '226', 'GNQ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GRC', 'COUNTRY', 'Greece', 'GR', '300', 'GRC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GRD', 'COUNTRY', 'Grenada', 'GD', '308', 'GRD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GRL', 'COUNTRY', 'Greenland', 'GL', '304', 'GRL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GTM', 'COUNTRY', 'Guatemala', 'GT', '320', 'GTM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GU', 'STATE', 'Guam', 'GU', null, 'GU', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('GUF', 'COUNTRY', 'French Guiana', 'GF', '254', 'GUF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GUM', 'COUNTRY', 'Guam', 'GU', '316', 'GUM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('GUY', 'COUNTRY', 'Guyana', 'GY', '328', 'GUY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('HI', 'STATE', 'Hawaii', 'HI', null, 'HI', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('HKG', 'COUNTRY', 'Hong Kong', 'HK', '344', 'HKG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('HMD', 'COUNTRY', 'Heard And Mc Donald Islands', 'HM', '334', 'HMD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('HND', 'COUNTRY', 'Honduras', 'HN', '340', 'HND', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('HRV', 'COUNTRY', 'Croatia (local Name: Hrvatska)', 'HR', '191', 'HRV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('HTI', 'COUNTRY', 'Haiti', 'HT', '332', 'HTI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('HUN', 'COUNTRY', 'Hungary', 'HU', '348', 'HUN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IA', 'STATE', 'Iowa', 'IA', null, 'IA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('ID', 'STATE', 'Idaho', 'ID', null, 'ID', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IDN', 'COUNTRY', 'Indonesia', 'ID', '360', 'IDN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IL', 'STATE', 'Illinois', 'IL', null, 'IL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IN', 'STATE', 'Indiana', 'IN', null, 'IN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IN-AN', 'STATE', 'ANDAMAN AND NICOBAR', 'AN', null, 'AN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-AP', 'STATE', 'ANDHRA PRADESH', 'AP', null, 'AP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-AR', 'STATE', 'ARUNACHAL PRADESH', 'AR', null, 'AR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-AS', 'STATE', 'ASSAM', 'AS', null, 'AS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-BR', 'STATE', 'BIHAR', 'BR', null, 'BR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-CH', 'STATE', 'CHANDIGARH', 'CH', null, 'CH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-CT', 'STATE', 'CHHATTISGARH', 'CT', null, 'CT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-DD', 'STATE', 'DAMAN AND DIU', 'DD', null, 'DD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-DN', 'STATE', 'DADRA AND NAGER HAVELI', 'DN', null, 'DN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-GA', 'STATE', 'GOA', 'GA', null, 'GA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-GJ', 'STATE', 'GUJARAT', 'GJ', null, 'GJ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-HP', 'STATE', 'HIMACHAL PRADESH', 'HP', null, 'HP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-HR', 'STATE', 'HARYANA', 'HR', null, 'HR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-JH', 'STATE', 'JHARKHAND', 'JH', null, 'JH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-JK', 'STATE', 'JAMMU AND KASHMIR', 'JK', null, 'JK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-KA', 'STATE', 'KARNATAKA', 'KA', null, 'KA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-KL', 'STATE', 'KERALA', 'KL', null, 'KL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-LD', 'STATE', 'LAKSHADWEEP', 'LD', null, 'LD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-MH', 'STATE', 'MAHARASHTRA', 'MH', null, 'MH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-ML', 'STATE', 'MEGHALAYA', 'ML', null, 'ML', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-MN', 'STATE', 'MANIPUR', 'MN', null, 'MN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-MP', 'STATE', 'MADHYA PRADESH', 'MP', null, 'MP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-MZ', 'STATE', 'MIZORAM', 'MZ', null, 'MZ', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-ND', 'STATE', 'NEW DELHI', 'ND', null, 'ND', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-NL', 'STATE', 'NAGALAND', 'NL', null, 'NL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-OR', 'STATE', 'ORISSA', 'OR', null, 'OR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-PB', 'STATE', 'PUNJAB', 'PB', null, 'PB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-PY', 'STATE', 'PONDICHERRY', 'PY', null, 'PY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-RJ', 'STATE', 'RAJASTHAN', 'RJ', null, 'RJ', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-SK', 'STATE', 'SIKKIM', 'SK', null, 'SK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-TN', 'STATE', 'TAMILNADU', 'TN', null, 'TN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-TR', 'STATE', 'TRIPURA', 'TR', null, 'TR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-UP', 'STATE', 'UTTAR PRADESH', 'UP', null, 'UP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-UT', 'STATE', 'UTTARANCHAL', 'UT', null, 'UT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IN-WB', 'STATE', 'WEST BENGAL', 'WB', null, 'WB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IND', 'COUNTRY', 'India', 'IN', '356', 'IND', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IOT', 'COUNTRY', 'British Indian Ocean Territory', 'IO', '086', 'IOT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IRL', 'COUNTRY', 'Ireland', 'IE', '372', 'IRL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IRL-CAVN', 'COUNTY', 'Cavan', 'CAVN', null, 'CAVN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-CLARE', 'COUNTY', 'Clare', 'CLARE', null, 'CLARE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-CORK', 'COUNTY', 'Cork', 'CORK', null, 'CORK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-CRLW', 'COUNTY', 'Carlow', 'CRLW', null, 'CRLW', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-DBLN', 'COUNTY', 'Dublin', 'DBLN', null, 'DBLN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-DNGL', 'COUNTY', 'Donegal', 'DNGL', null, 'DNGL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-GALW', 'COUNTY', 'Galway', 'GALW', null, 'GALW', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-KERRY', 'COUNTY', 'Kerry', 'KERRY', null, 'KERRY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-KLDR', 'COUNTY', 'Kildare', 'KLDR', null, 'KLDR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-KLKNY', 'COUNTY', 'Kilkenny', 'KLKNY', null, 'KLKNY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-LAOIS', 'COUNTY', 'Laois', 'LAOIS', null, 'LAOIS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-LGFD', 'COUNTY', 'Longford', 'LGFD', null, 'LGFD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-LMRK', 'COUNTY', 'Limerick', 'LMRK', null, 'LMRK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-LOUTH', 'COUNTY', 'Louth', 'LOUTH', null, 'LOUTH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-LTRM', 'COUNTY', 'Leitrim', 'LTRM', null, 'LTRM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-MAYO', 'COUNTY', 'Mayo', 'MAYO', null, 'MAYO', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-MEATH', 'COUNTY', 'Meath', 'MEATH', null, 'MEATH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-MNGHN', 'COUNTY', 'Monaghan', 'MNGHN', null, 'MNGHN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-OFLY', 'COUNTY', 'Offaly', 'OFLY', null, 'OFLY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-RSCMN', 'COUNTY', 'Roscommon', 'RSCMN', null, 'RSCMN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-SLIGO', 'COUNTY', 'Sligo', 'SLIGO', null, 'SLIGO', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-TPRY', 'COUNTY', 'Tipperary', 'TPRY', null, 'TPRY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-WKLW', 'COUNTY', 'Wicklow', 'WKLW', null, 'WKLW', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-WMETH', 'COUNTY', 'West Meath', 'WMETH', null, 'WMETH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-WTFD', 'COUNTY', 'Waterford', 'WTFD', null, 'WTFD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRL-WXFD', 'COUNTY', 'Wexford', 'WXFD', null, 'WXFD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('IRN', 'COUNTRY', 'Iran (islamic Republic Of)', 'IR', '364', 'IRN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IRQ', 'COUNTRY', 'Iraq', 'IQ', '368', 'IRQ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ISL', 'COUNTRY', 'Iceland', 'IS', '352', 'ISL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ISR', 'COUNTRY', 'Israel', 'IL', '376', 'ISR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AG', 'PROVINCE', 'Agrigento', 'AG', null, 'AG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AL', 'PROVINCE', 'Alessandria', 'AL', null, 'AL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AN', 'PROVINCE', 'Ancona', 'AN', null, 'AN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AO', 'PROVINCE', 'Aosta', 'AO', null, 'AO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AP', 'PROVINCE', 'Ascoli Piceno', 'AP', null, 'AP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AQ', 'PROVINCE', 'L\'Aquila', 'AQ', null, 'AQ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AR', 'PROVINCE', 'Arezzo', 'AR', null, 'AR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AT', 'PROVINCE', 'Asti', 'AT', null, 'AT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-AV', 'PROVINCE', 'Avellino', 'AV', null, 'AV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BA', 'PROVINCE', 'Bari', 'BA', null, 'BA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BG', 'PROVINCE', 'Bergamo', 'BG', null, 'BG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BI', 'PROVINCE', 'Biella', 'BI', null, 'BI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BL', 'PROVINCE', 'Belluno', 'BL', null, 'BL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BN', 'PROVINCE', 'Benevento', 'BN', null, 'BN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BO', 'PROVINCE', 'Bologna', 'BO', null, 'BO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BR', 'PROVINCE', 'Brindisi', 'BR', null, 'BR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BS', 'PROVINCE', 'Brescia', 'BS', null, 'BS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BT', 'PROVINCE', 'Barletta-Andria-Trani', 'BT', null, 'BT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-BZ', 'PROVINCE', 'Bolzano', 'BZ', null, 'BZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CA', 'PROVINCE', 'Cagliari', 'CA', null, 'CA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CB', 'PROVINCE', 'Campobasso', 'CB', null, 'CB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CE', 'PROVINCE', 'Caserta', 'CE', null, 'CE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CH', 'PROVINCE', 'Chieti', 'CH', null, 'CH', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CI', 'PROVINCE', 'Carbonia-Iglesias', 'CI', null, 'CI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CL', 'PROVINCE', 'Caltanissetta', 'CL', null, 'CL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CN', 'PROVINCE', 'Cuneo', 'CN', null, 'CN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CO', 'PROVINCE', 'Como', 'CO', null, 'CO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CR', 'PROVINCE', 'Cremona', 'CR', null, 'CR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CS', 'PROVINCE', 'Cosenza', 'CS', null, 'CS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CT', 'PROVINCE', 'Catania', 'CT', null, 'CT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-CZ', 'PROVINCE', 'Catanzaro', 'CZ', null, 'CZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-EN', 'PROVINCE', 'Enna', 'EN', null, 'EN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-FC', 'PROVINCE', 'Forli\'-Cesena', 'FC', null, 'FC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-FE', 'PROVINCE', 'Ferrara', 'FE', null, 'FE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-FG', 'PROVINCE', 'Foggia', 'FG', null, 'FG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-FI', 'PROVINCE', 'Firenze', 'FI', null, 'FI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-FM', 'PROVINCE', 'Fermo', 'FM', null, 'FM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-FR', 'PROVINCE', 'Frosinone', 'FR', null, 'FR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-GE', 'PROVINCE', 'Genova', 'GE', null, 'GE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-GO', 'PROVINCE', 'Gorizia', 'GO', null, 'GO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-GR', 'PROVINCE', 'Grosseto', 'GR', null, 'GR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-IM', 'PROVINCE', 'Imperia', 'IM', null, 'IM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-IS', 'PROVINCE', 'Isernia', 'IS', null, 'IS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-KR', 'PROVINCE', 'Crotone', 'KR', null, 'KR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-LC', 'PROVINCE', 'Lecco', 'LC', null, 'LC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-LE', 'PROVINCE', 'Lecce', 'LE', null, 'LE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-LI', 'PROVINCE', 'Livorno', 'LI', null, 'LI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-LO', 'PROVINCE', 'Lodi', 'LO', null, 'LO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-LT', 'PROVINCE', 'Latina', 'LT', null, 'LT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-LU', 'PROVINCE', 'Lucca', 'LU', null, 'LU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MB', 'PROVINCE', 'Monza e Brianza', 'MB', null, 'MB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MC', 'PROVINCE', 'Macerata', 'MC', null, 'MC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-ME', 'PROVINCE', 'Messina', 'ME', null, 'ME', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MI', 'PROVINCE', 'Milano', 'MI', null, 'MI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MN', 'PROVINCE', 'Mantova', 'MN', null, 'MN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MO', 'PROVINCE', 'Modena', 'MO', null, 'MO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MS', 'PROVINCE', 'Massa-Carrara', 'MS', null, 'MS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-MT', 'PROVINCE', 'Matera', 'MT', null, 'MT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-NA', 'PROVINCE', 'Napoli', 'NA', null, 'NA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-NO', 'PROVINCE', 'Novara', 'NO', null, 'NO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-NU', 'PROVINCE', 'Nuoro', 'NU', null, 'NU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-OG', 'PROVINCE', 'Ogliastra', 'OG', null, 'OG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-OR', 'PROVINCE', 'Oristano', 'OR', null, 'OR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-OT', 'PROVINCE', 'Olbia-Tempio', 'OT', null, 'OT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PA', 'PROVINCE', 'Palermo', 'PA', null, 'PA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PC', 'PROVINCE', 'Piacenza', 'PC', null, 'PC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PD', 'PROVINCE', 'Padova', 'PD', null, 'PD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PE', 'PROVINCE', 'Pescara', 'PE', null, 'PE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PG', 'PROVINCE', 'Perugia', 'PG', null, 'PG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PI', 'PROVINCE', 'Pisa', 'PI', null, 'PI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PN', 'PROVINCE', 'Pordenone', 'PN', null, 'PN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PO', 'PROVINCE', 'Prato', 'PO', null, 'PO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PR', 'PROVINCE', 'Parma', 'PR', null, 'PR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PT', 'PROVINCE', 'Pistoia', 'PT', null, 'PT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PU', 'PROVINCE', 'Pesaro e Urbino', 'PU', null, 'PU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PV', 'PROVINCE', 'Pavia', 'PV', null, 'PV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-PZ', 'PROVINCE', 'Potenza', 'PZ', null, 'PZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RA', 'PROVINCE', 'Ravenna', 'RA', null, 'RA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RC', 'PROVINCE', 'Reggio Calabria', 'RC', null, 'RC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RE', 'PROVINCE', 'Reggio Emilia', 'RE', null, 'RE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RG', 'PROVINCE', 'Ragusa', 'RG', null, 'RG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RI', 'PROVINCE', 'Rieti', 'RI', null, 'RI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RM', 'PROVINCE', 'Roma', 'RM', null, 'RM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RN', 'PROVINCE', 'Rimini', 'RN', null, 'RN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-RO', 'PROVINCE', 'Rovigo', 'RO', null, 'RO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SA', 'PROVINCE', 'Salerno', 'SA', null, 'SA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SI', 'PROVINCE', 'Siena', 'SI', null, 'SI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SO', 'PROVINCE', 'Sondrio', 'SO', null, 'SO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SP', 'PROVINCE', 'La Spezia', 'SP', null, 'SP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SR', 'PROVINCE', 'Siracusa', 'SR', null, 'SR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SS', 'PROVINCE', 'Sassari', 'SS', null, 'SS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-SV', 'PROVINCE', 'Savona', 'SV', null, 'SV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TA', 'PROVINCE', 'Taranto', 'TA', null, 'TA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TE', 'PROVINCE', 'Teramo', 'TE', null, 'TE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TN', 'PROVINCE', 'Trento', 'TN', null, 'TN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TO', 'PROVINCE', 'Torino', 'TO', null, 'TO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TP', 'PROVINCE', 'Trapani', 'TP', null, 'TP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TR', 'PROVINCE', 'Terni', 'TR', null, 'TR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TS', 'PROVINCE', 'Trieste', 'TS', null, 'TS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-TV', 'PROVINCE', 'Treviso', 'TV', null, 'TV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-UD', 'PROVINCE', 'Udine', 'UD', null, 'UD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VA', 'PROVINCE', 'Varese', 'VA', null, 'VA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VB', 'PROVINCE', 'Verbano-Cusio-Ossola', 'VB', null, 'VB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VC', 'PROVINCE', 'Vercelli', 'VC', null, 'VC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VE', 'PROVINCE', 'Venezia', 'VE', null, 'VE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VI', 'PROVINCE', 'Vicenza', 'VI', null, 'VI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VR', 'PROVINCE', 'Verona', 'VR', null, 'VR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VS', 'PROVINCE', 'Medio Campidano', 'VS', null, 'VS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VT', 'PROVINCE', 'Viterbo', 'VT', null, 'VT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('IT-VV', 'PROVINCE', 'Vibo Valentia', 'VV', null, 'VV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ITA', 'COUNTRY', 'Italy', 'IT', '380', 'ITA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('JAM', 'COUNTRY', 'Jamaica', 'JM', '388', 'JAM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('JOR', 'COUNTRY', 'Jordan', 'JO', '400', 'JOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('JPN', 'COUNTRY', 'Japan', 'JP', '392', 'JPN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KAZ', 'COUNTRY', 'Kazakhstan', 'KZ', '398', 'KAZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KEN', 'COUNTRY', 'Kenya', 'KE', '404', 'KEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KGZ', 'COUNTRY', 'Kyrgyzstan', 'KG', '417', 'KGZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KHM', 'COUNTRY', 'Cambodia', 'KH', '116', 'KHM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KIR', 'COUNTRY', 'Kiribati', 'KI', '296', 'KIR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KNA', 'COUNTRY', 'Saint Kitts And Nevis', 'KN', '659', 'KNA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KOR', 'COUNTRY', 'Korea, Republic Of', 'KR', '410', 'KOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KS', 'STATE', 'Kansas', 'KS', null, 'KS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('KWT', 'COUNTRY', 'Kuwait', 'KW', '414', 'KWT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('KY', 'STATE', 'Kentucky', 'KY', null, 'KY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('LA', 'STATE', 'Louisiana', 'LA', null, 'LA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('LAO', 'COUNTRY', 'Lao People\'s Democratic Republic', 'LA', '418', 'LAO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LBN', 'COUNTRY', 'Lebanon', 'LB', '422', 'LBN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LBR', 'COUNTRY', 'Liberia', 'LR', '430', 'LBR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LBY', 'COUNTRY', 'Libyan Arab Jamahiriya', 'LY', '434', 'LBY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LCA', 'COUNTRY', 'Saint Lucia', 'LC', '662', 'LCA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LIE', 'COUNTRY', 'Liechtenstein', 'LI', '438', 'LIE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LKA', 'COUNTRY', 'Sri Lanka', 'LK', '144', 'LKA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LSO', 'COUNTRY', 'Lesotho', 'LS', '426', 'LSO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LTU', 'COUNTRY', 'Lithuania', 'LT', '440', 'LTU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LUX', 'COUNTRY', 'Luxembourg', 'LU', '442', 'LUX', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('LVA', 'COUNTRY', 'Latvia', 'LV', '428', 'LVA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MA', 'STATE', 'Massachusetts', 'MA', null, 'MA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MAC', 'COUNTRY', 'Macau', 'MO', '446', 'MAC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MAR', 'COUNTRY', 'Morocco', 'MA', '504', 'MAR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MB', 'PROVINCE', 'Manitoba', 'MB', null, 'MB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MCO', 'COUNTRY', 'Monaco', 'MC', '492', 'MCO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MD', 'STATE', 'Maryland', 'MD', null, 'MD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MDA', 'COUNTRY', 'Moldova, Republic Of', 'MD', '498', 'MDA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MDG', 'COUNTRY', 'Madagascar', 'MG', '450', 'MDG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MDV', 'COUNTRY', 'Maldives', 'MV', '462', 'MDV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ME', 'STATE', 'Maine', 'ME', null, 'ME', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MEX', 'COUNTRY', 'Mexico', 'MX', '484', 'MEX', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MEXCAN', 'GROUP', 'Mexico/Canada', 'MEXCAN', null, 'MEXCAN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MH', 'STATE', 'Marshall Islands', 'MH', null, 'MH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MHL', 'COUNTRY', 'Marshall Islands', 'MH', '584', 'MHL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MI', 'STATE', 'Michigan', 'MI', null, 'MI', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MKD', 'COUNTRY', 'Macedonia, The Former Yugoslav Republic Of', 'MK', '807', 'MKD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MLI', 'COUNTRY', 'Mali', 'ML', '466', 'MLI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MLT', 'COUNTRY', 'Malta', 'MT', '470', 'MLT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MMR', 'COUNTRY', 'Myanmar', 'MM', '104', 'MMR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MN', 'STATE', 'Minnesota', 'MN', null, 'MN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MNE', 'COUNTRY', 'Montenegro', 'ME', '499', 'MNE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MNG', 'COUNTRY', 'Mongolia', 'MN', '496', 'MNG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MNP', 'COUNTRY', 'Northern Mariana Islands', 'MP', '580', 'MNP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MO', 'STATE', 'Missouri', 'MO', null, 'MO', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MOZ', 'COUNTRY', 'Mozambique', 'MZ', '508', 'MOZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MP', 'STATE', 'Northern Mariana Islands', 'MP', null, 'MP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MRT', 'COUNTRY', 'Mauritania', 'MR', '478', 'MRT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MS', 'STATE', 'Mississippi', 'MS', null, 'MS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MSR', 'COUNTRY', 'Montserrat', 'MS', '500', 'MSR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MT', 'STATE', 'Montana', 'MT', null, 'MT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MTQ', 'COUNTRY', 'Martinique', 'MQ', '474', 'MTQ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MUS', 'COUNTRY', 'Mauritius', 'MU', '480', 'MUS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MWI', 'COUNTRY', 'Malawi', 'MW', '454', 'MWI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MX-AG', 'STATE', 'Aguascalientes', 'AGU', null, 'AGU', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-BN', 'STATE', 'Baja California', 'BCN', null, 'BCN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-BS', 'STATE', 'Baja California Sur', 'BCS', null, 'BCS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-CA', 'STATE', 'Coahuila', 'COA', null, 'COA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-CH', 'STATE', 'Chihuahua', 'CHH', null, 'CHH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-CL', 'STATE', 'Colima', 'COL', null, 'COL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-CM', 'STATE', 'Campeche', 'CAM', null, 'CAM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-CP', 'STATE', 'Chiapas', 'CHP', null, 'CHP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-DF', 'STATE', 'Distrito Federal', 'DIF', null, 'DIF', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-DU', 'STATE', 'Durango', 'DUR', null, 'DUR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-GJ', 'STATE', 'Guanajuato', 'GUA', null, 'GUA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-GR', 'STATE', 'Guerrero', 'GRO', null, 'GRO', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-HI', 'STATE', 'Hidalgo', 'HID', null, 'HID', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-JA', 'STATE', 'Jalisco', 'JAL', null, 'JAL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-MC', 'STATE', 'Michoacán', 'MIC', null, 'MIC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-MR', 'STATE', 'Morelos', 'MOR', null, 'MOR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-MX', 'STATE', 'México', 'MEX', null, 'MEX', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-NA', 'STATE', 'Nayarit', 'NAY', null, 'NAY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-NL', 'STATE', 'Nuevo León', 'NLE', null, 'NLE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-OA', 'STATE', 'Oaxaca', 'OAX', null, 'OAX', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-PU', 'STATE', 'Puebla', 'PUE', null, 'PUE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-QE', 'STATE', 'Querétaro', 'QUE', null, 'QUE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-QR', 'STATE', 'Quintana Roo', 'ROO', null, 'ROO', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-SI', 'STATE', 'Sinaloa', 'SIN', null, 'SIN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-SL', 'STATE', 'San Luis Potosí', 'SLP', null, 'SLP', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-SO', 'STATE', 'Sonora', 'SON', null, 'SON', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-TB', 'STATE', 'Tabasco', 'TAB', null, 'TAB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-TL', 'STATE', 'Tlaxcala', 'TLA', null, 'TLA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-TM', 'STATE', 'Tamaulipas', 'TAM', null, 'TAM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-VE', 'STATE', 'Veracruz', 'VER', null, 'VER', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-YU', 'STATE', 'Yucatán', 'YUC', null, 'YUC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MX-ZA', 'STATE', 'Zacatecas', 'ZAC', null, 'ZAC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('MYS', 'COUNTRY', 'Malaysia', 'MY', '458', 'MYS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('MYT', 'COUNTRY', 'Mayotte', 'YT', '175', 'MYT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NAM', 'COUNTRY', 'Namibia', 'NA', '516', 'NAM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NB', 'PROVINCE', 'New Brunswick', 'NB', null, 'NB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NC', 'STATE', 'North Carolina', 'NC', null, 'NC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NCL', 'COUNTRY', 'New Caledonia', 'NC', '540', 'NCL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ND', 'STATE', 'North Dakota', 'ND', null, 'ND', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NE', 'STATE', 'Nebraska', 'NE', null, 'NE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NER', 'COUNTRY', 'Niger', 'NE', '562', 'NER', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NFK', 'COUNTRY', 'Norfolk Island', 'NF', '574', 'NFK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NGA', 'COUNTRY', 'Nigeria', 'NG', '566', 'NGA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NH', 'STATE', 'New Hampshire', 'NH', null, 'NH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIC', 'COUNTRY', 'Nicaragua', 'NI', '558', 'NIC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NIRL', 'COUNTRY', 'N.Ireland', 'NIR', '897', 'NIRL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NIRL-ARMG', 'COUNTY', 'Armagh', 'ARMG', null, 'ARMG', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIRL-ATRM', 'COUNTY', 'Antrim', 'ATRM', null, 'ATRM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIRL-DOWN', 'COUNTY', 'Down', 'DOWN', null, 'DOWN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIRL-FMNH', 'COUNTY', 'Fermanagh', 'FMNH', null, 'FMNH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIRL-LDRY', 'COUNTY', 'Londonderry', 'LDRY', null, 'LDRY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIRL-TYR', 'COUNTY', 'Tyrone', 'TYR', null, 'TYR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NIU', 'COUNTRY', 'Niue', 'NU', '570', 'NIU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NJ', 'STATE', 'New Jersey', 'NJ', null, 'NJ', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL', 'PROVINCE', 'Newfoundland and Labrador', 'NL', null, 'NL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-DR', 'PROVINCE', 'Drenthe', 'DR', null, 'DR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-FL', 'PROVINCE', 'Flevoland', 'FL', null, 'FL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-FR', 'PROVINCE', 'Friesland', 'FR', null, 'FR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-GL', 'PROVINCE', 'Gelderland', 'GL', null, 'GL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-GR', 'PROVINCE', 'Groningen', 'GR', null, 'GR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-LB', 'PROVINCE', 'Limburg', 'LB', null, 'LB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-NB', 'PROVINCE', 'Noord-Brabant', 'NB', null, 'NB', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-NH', 'PROVINCE', 'Noord-Holland', 'NH', null, 'NH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-OV', 'PROVINCE', 'Overijssel', 'OV', null, 'OV', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-UT', 'PROVINCE', 'Utrecht', 'UT', null, 'UT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-ZE', 'PROVINCE', 'Zeeland', 'ZE', null, 'ZE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-ZH', 'PROVINCE', 'Zuid-Holland', 'ZH', null, 'ZH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NL-ZL', 'PROVINCE', 'Zeeland', 'ZL', null, 'ZL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NLD', 'COUNTRY', 'Netherlands', 'NL', '528', 'NLD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NM', 'STATE', 'New Mexico', 'NM', null, 'NM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NOR', 'COUNTRY', 'Norway', 'NO', '578', 'NOR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NPL', 'COUNTRY', 'Nepal', 'NP', '524', 'NPL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NRU', 'COUNTRY', 'Nauru', 'NR', '520', 'NRU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('NS', 'PROVINCE', 'Nova Scotia', 'NS', null, 'NS', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NT', 'PROVINCE', 'Northwest Territories', 'NT', null, 'NT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NU', 'PROVINCE', 'Nunavut', 'NU', null, 'NU', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NV', 'STATE', 'Nevada', 'NV', null, 'NV', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NY', 'STATE', 'New York', 'NY', null, 'NY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('NZL', 'COUNTRY', 'New Zealand', 'NZ', '554', 'NZL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('OH', 'STATE', 'Ohio', 'OH', null, 'OH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('OK', 'STATE', 'Oklahoma', 'OK', null, 'OK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('OMN', 'COUNTRY', 'Oman', 'OM', '512', 'OMN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ON', 'PROVINCE', 'Ontario', 'ON', null, 'ON', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('OR', 'STATE', 'Oregon', 'OR', null, 'OR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('PA', 'STATE', 'Pennsylvania', 'PA', null, 'PA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('PAK', 'COUNTRY', 'Pakistan', 'PK', '586', 'PAK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PAN', 'COUNTRY', 'Panama', 'PA', '591', 'PAN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PCN', 'COUNTRY', 'Pitcairn', 'PN', '612', 'PCN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PE', 'PROVINCE', 'Prince Edward Island', 'PE', null, 'PE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('PER', 'COUNTRY', 'Peru', 'PE', '604', 'PER', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PHL', 'COUNTRY', 'Philippines', 'PH', '608', 'PHL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PLW', 'COUNTRY', 'Palau', 'PW', '585', 'PLW', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PNG', 'COUNTRY', 'Papua New Guinea', 'PG', '598', 'PNG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('POL', 'COUNTRY', 'Poland', 'PL', '616', 'POL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PR', 'STATE', 'Puerto Rico', 'PR', null, 'PR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('PRI', 'COUNTRY', 'Puerto Rico', 'PR', '630', 'PRI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PRK', 'COUNTRY', 'Korea, Democratic People\'s Republic Of', 'KP', '408', 'PRK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PRT', 'COUNTRY', 'Portugal', 'PT', '620', 'PRT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PRY', 'COUNTRY', 'Paraguay', 'PY', '600', 'PRY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PSE', 'COUNTRY', 'Palestinian Territory, Occupied', 'PS', '275', 'PSE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('PW', 'STATE', 'Palau', 'PW', null, 'PW', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('PYF', 'COUNTRY', 'French Polynesia', 'PF', '258', 'PYF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('QAT', 'COUNTRY', 'Qatar', 'QA', '634', 'QAT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('QC', 'PROVINCE', 'Quebec', 'QC', null, 'QC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('REU', 'COUNTRY', 'Reunion', 'RE', '638', 'REU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('RI', 'STATE', 'Rhode Island', 'RI', null, 'RI', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('ROU', 'COUNTRY', 'Romania', 'RO', '642', 'ROU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('RUS', 'COUNTRY', 'Russian Federation', 'RU', '643', 'RUS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('RWA', 'COUNTRY', 'Rwanda', 'RW', '646', 'RWA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SAU', 'COUNTRY', 'Saudi Arabia', 'SA', '682', 'SAU', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SC', 'STATE', 'South Carolina', 'SC', null, 'SC', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('SCOT', 'COUNTRY', 'Scotland', 'SCT', '895', 'SCOT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SD', 'STATE', 'South Dakota', 'SD', null, 'SD', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('SDN', 'COUNTRY', 'Sudan', 'SD', '736', 'SDN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SEN', 'COUNTRY', 'Senegal', 'SN', '686', 'SEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SGP', 'COUNTRY', 'Singapore', 'SG', '702', 'SGP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SGS', 'COUNTRY', 'South Georgia And The South Sandwich Islands', 'GS', '239', 'SGS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SHN', 'COUNTRY', 'St. Helena', 'SH', '654', 'SHN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SJM', 'COUNTRY', 'Svalbard And Jan Mayen Islands', 'SJ', '744', 'SJM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SK', 'PROVINCE', 'Saskatchewan', 'SK', null, 'SK', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('SLB', 'COUNTRY', 'Solomon Islands', 'SB', '090', 'SLB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SLE', 'COUNTRY', 'Sierra Leone', 'SL', '694', 'SLE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SLV', 'COUNTRY', 'El Salvador', 'SV', '222', 'SLV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SMR', 'COUNTRY', 'San Marino', 'SM', '674', 'SMR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SOM', 'COUNTRY', 'Somalia', 'SO', '706', 'SOM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SPM', 'COUNTRY', 'St. Pierre And Miquelon', 'PM', '666', 'SPM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SRB', 'COUNTRY', 'Serbia', 'RS', '688', 'SRB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('STP', 'COUNTRY', 'Sao Tome And Principe', 'ST', '678', 'STP', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SUR', 'COUNTRY', 'Suriname', 'SR', '740', 'SUR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SVK', 'COUNTRY', 'Slovakia (slovak Republic)', 'SK', '703', 'SVK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SVN', 'COUNTRY', 'Slovenia', 'SI', '705', 'SVN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SWE', 'COUNTRY', 'Sweden', 'SE', '752', 'SWE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SWZ', 'COUNTRY', 'Swaziland', 'SZ', '748', 'SWZ', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SYC', 'COUNTRY', 'Seychelles', 'SC', '690', 'SYC', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('SYR', 'COUNTRY', 'Syrian Arab Republic', 'SY', '760', 'SYR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TCA', 'COUNTRY', 'Turks And Caicos Islands', 'TC', '796', 'TCA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TCD', 'COUNTRY', 'Chad', 'TD', '148', 'TCD', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TGO', 'COUNTRY', 'Togo', 'TG', '768', 'TGO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('THA', 'COUNTRY', 'Thailand', 'TH', '764', 'THA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TJK', 'COUNTRY', 'Tajikistan', 'TJ', '762', 'TJK', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TKL', 'COUNTRY', 'Tokelau', 'TK', '772', 'TKL', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TKM', 'COUNTRY', 'Turkmenistan', 'TM', '795', 'TKM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TLS', 'COUNTRY', 'East Timor', 'TL', '626', 'TLS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TN', 'STATE', 'Tennessee', 'TN', null, 'TN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('TON', 'COUNTRY', 'Tonga', 'TO', '776', 'TON', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TTO', 'COUNTRY', 'Trinidad And Tobago', 'TT', '780', 'TTO', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TUN', 'COUNTRY', 'Tunisia', 'TN', '788', 'TUN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TUR', 'COUNTRY', 'Turkey', 'TR', '792', 'TUR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TUV', 'COUNTRY', 'Tuvalu', 'TV', '798', 'TUV', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TWN', 'COUNTRY', 'Taiwan', 'TW', '158', 'TWN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('TX', 'STATE', 'Texas', 'TX', null, 'TX', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('TZA', 'COUNTRY', 'Tanzania, United Republic Of', 'TZ', '834', 'TZA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('UGA', 'COUNTRY', 'Uganda', 'UG', '800', 'UGA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('UKR', 'COUNTRY', 'Ukraine', 'UA', '804', 'UKR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('UM', 'STATE', 'U.S. Minor Outlying Islands', 'UM', null, 'UM', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('UMI', 'COUNTRY', 'United States Minor Outlying Islands', 'UM', '581', 'UMI', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('URY', 'COUNTRY', 'Uruguay', 'UY', '858', 'URY', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('US50', 'GROUP', 'US 50 (no APO/FPO)', 'US50', null, 'US50', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USA', 'COUNTRY', 'United States', 'US', '840', 'USA', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('USA-84057', 'POSTAL_CODE', '84057', '84057', null, '84057', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USAF', 'GROUP', 'US Armed Forces', 'USAF', null, 'USAF', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USALL', 'GROUP', 'US All (w/ APO/FPO)', 'USALL', null, 'USALL', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USCAN', 'GROUP', 'US/Canada', 'USCAN', null, 'USCAN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USCN', 'GROUP', 'US Continental', 'USCN', null, 'USCN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USMEXCAN', 'GROUP', 'US/Mexico/Canada', 'USMEXCAN', null, 'USMEXCAN', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('USTR', 'GROUP', 'US Territories', 'USTR', null, 'USTR', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('UT', 'STATE', 'Utah', 'UT', null, 'UT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('UT-SANPETE', 'COUNTY', 'Sanpete', 'SANPETE', null, 'SANPETE', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('UT-UTAH', 'COUNTY', 'Utah County', 'UTAH', null, 'UTAH', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('UZB', 'COUNTRY', 'Uzbekistan', 'UZ', '860', 'UZB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VA', 'STATE', 'Virginia', 'VA', null, 'VA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('VAT', 'COUNTRY', 'Holy See (vatican City State)', 'VA', '336', 'VAT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VCT', 'COUNTRY', 'Saint Vincent And The Grenadines', 'VC', '670', 'VCT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VEN', 'COUNTRY', 'Venezuela', 'VE', '862', 'VEN', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VGB', 'COUNTRY', 'Virgin Islands (british)', 'VG', '092', 'VGB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VI', 'STATE', 'Virgin Islands', 'VI', null, 'VI', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('VIR', 'COUNTRY', 'Virgin Islands (u.s.)', 'VI', '850', 'VIR', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VNM', 'COUNTRY', 'Viet Nam', 'VN', '704', 'VNM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('VT', 'STATE', 'Vermont', 'VT', null, 'VT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('VUT', 'COUNTRY', 'Vanuatu', 'VU', '548', 'VUT', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('WA', 'STATE', 'Washington', 'WA', null, 'WA', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('WALS', 'COUNTRY', 'Wales', 'WLS', '898', 'WALS', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('WI', 'STATE', 'Wisconsin', 'WI', null, 'WI', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('WLF', 'COUNTRY', 'Wallis And Futuna Islands', 'WF', '876', 'WLF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('WSM', 'COUNTRY', 'Samoa', 'WS', '882', 'WSM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('WV', 'STATE', 'West Virginia', 'WV', null, 'WV', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('WY', 'STATE', 'Wyoming', 'WY', null, 'WY', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('YEM', 'COUNTRY', 'Yemen', 'YE', '887', 'YEM', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('YT', 'PROVINCE', 'Yukon', 'YT', null, 'YT', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo` VALUES ('YUG', 'COUNTRY', 'Yugoslavia', 'YU', '891', 'YUG', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ZAF', 'COUNTRY', 'South Africa', 'ZA', '710', 'ZAF', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ZMB', 'COUNTRY', 'Zambia', 'ZM', '894', 'ZMB', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('ZWE', 'COUNTRY', 'Zimbabwe', 'ZW', '716', 'ZWE', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo` VALUES ('_NA_', null, 'Not Applicable', '_NA_', null, '_NA_', null, '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');

-- ----------------------------
-- Table structure for `geo_assoc`
-- ----------------------------
DROP TABLE IF EXISTS `geo_assoc`;
CREATE TABLE `geo_assoc` (
  `GEO_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `GEO_ID_TO` varchar(20) collate latin1_general_cs NOT NULL,
  `GEO_ASSOC_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GEO_ID`,`GEO_ID_TO`),
  KEY `GEO_ASSC_TO_MAIN` (`GEO_ID`),
  KEY `GEO_ASSC_TO_ASSC` (`GEO_ID_TO`),
  KEY `GEO_ASSC_TO_TYPE` (`GEO_ASSOC_TYPE_ID`),
  KEY `GEO_ASSOC_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `GEO_ASSOC_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `GEO_ASSC_TO_ASSC` FOREIGN KEY (`GEO_ID_TO`) REFERENCES `geo` (`GEO_ID`),
  CONSTRAINT `GEO_ASSC_TO_MAIN` FOREIGN KEY (`GEO_ID`) REFERENCES `geo` (`GEO_ID`),
  CONSTRAINT `GEO_ASSC_TO_TYPE` FOREIGN KEY (`GEO_ASSOC_TYPE_ID`) REFERENCES `geo_assoc_type` (`GEO_ASSOC_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of geo_assoc
-- ----------------------------
INSERT INTO `geo_assoc` VALUES ('AA', 'USAF', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AE', 'USAF', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AHUST', 'US50', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AHUST', 'USALL', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AK', 'AKHI', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AKHI', 'AHUST', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AL', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AP', 'USAF', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AR', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AS', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-ACT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-NSW', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-NT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-QLD', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-SA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-TAS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-VIC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUS', 'AU-WA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AUT', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('AZ', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('BEL', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-01', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-02', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-03', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-04', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-05', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-06', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-07', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-08', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-09', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-10', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-11', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-12', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-13', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-14', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-15', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-16', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-17', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-18', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-19', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-20', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-21', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-22', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-23', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-24', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-25', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-26', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-27', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'BG-28', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BGR', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-AL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-AM', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-AP', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-BA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-CE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-DF', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-ES', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-GO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-MA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-MG', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-MS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-MT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-PA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-PB', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-PE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-PI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-PR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-RJ', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-RN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-RO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-RR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-RS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-SC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-SE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-SP', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('BRA', 'BR-TO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('CA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'AB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'BC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'MB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'MEXCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'NB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'NL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'NS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'NT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'NU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'ON', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'PE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'QC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'SK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'USCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'USMEXCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CAN', 'YT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CO', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-AMA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-ANT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-ARA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-ATL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-BOL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-BOY', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CAL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CAQ', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CAS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CAU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CES', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CHO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-COR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-CUN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-DC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-GUA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-GUV', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-HUI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-LAG', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-MAG', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-MET', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-NAR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-NSA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-PUT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-QUI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-RIS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-SAN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-SAP', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-SUC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-TOL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-VAC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-VAU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('COL', 'CO-VID', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('CT', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('CYP', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('CZE', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DC', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('DE', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-BE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-BR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-BW', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-BY', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-HB', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-HE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-HH', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-MV', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-NI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-NW', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-RP', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-SA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-SH', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-SL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-SN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'DE-TH', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DEU', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('DNK', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-BDFD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-BRKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-BUCKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-BWKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-CAMBS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-CHES', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-CNWL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-CUMB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-DEVON', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-DRBYS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-DRHM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-DRST', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-ESSX', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-GLOU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-HAMPS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-HERTS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-HRFDS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-HUNTS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-KENT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-LANCS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-LIECS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-LINCS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-MSEX', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-NFLK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-NHANTS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-NHUMB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-NOTTS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-OXFD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-RUTL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-SFFK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-SHROPS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-SOMST', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-STAFFS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-SURR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-SUSX', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-WILTS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-WMLD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-WORCS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-WWKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GB-YORKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ENGL', 'GBR', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-AL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-CA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-CO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-GR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-H', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-J', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-MA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AN', 'ES-SE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AR', 'ES-HU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AR', 'ES-TE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AR', 'ES-Z', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-AS', 'ES-O', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CAN', 'ES-S', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-AV', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-BU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-CR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-LE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-P', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-SA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-SG', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-SO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-VA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CL', 'ES-ZA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CM', 'ES-AB', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CM', 'ES-CU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CM', 'ES-GU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CM', 'ES-TO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CN', 'ES-GC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CN', 'ES-TF', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CT', 'ES-B', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CT', 'ES-GI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CT', 'ES-L', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-CT', 'ES-T', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-EX', 'ES-BA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-EX', 'ES-CC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-GA', 'ES-C', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-GA', 'ES-LU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-GA', 'ES-OR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-GA', 'ES-PO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-IB', 'ES-PM', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-LR', 'ES-LO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-MAD', 'ES-M', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-MUR', 'ES-MU', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-NAV', 'ES-NA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-PV', 'ES-BI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-PV', 'ES-SS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-PV', 'ES-VI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-VC', 'ES-A', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-VC', 'ES-CS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ES-VC', 'ES-V', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-AN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-AR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-AS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-CAN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-CE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-CL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-CM', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-CN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-CT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-EX', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-GA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-IB', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-LR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-MAD', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-ML', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-MUR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-NAV', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-PV', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'ES-VC', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ESP', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('EST', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FIN', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FL', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('FM', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('FR-ACY74', 'FR-74', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AGE47', 'FR-47', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AJA2A', 'FR-2A', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ALB81', 'FR-81', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ALE61', 'FR-61', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ALS', 'FR-67', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ALS', 'FR-68', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AMI80', 'FR-80', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ANG16', 'FR-16', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ANG49', 'FR-49', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AQU', 'FR-24', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AQU', 'FR-33', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AQU', 'FR-40', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AQU', 'FR-47', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AQU', 'FR-64', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ARR62', 'FR-62', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AUC32', 'FR-32', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AUR15', 'FR-15', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AUV', 'FR-03', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AUV', 'FR-15', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AUV', 'FR-43', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AUV', 'FR-63', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AVG84', 'FR-84', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-AXR89', 'FR-89', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BAN', 'FR-14', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BAN', 'FR-50', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BAN', 'FR-61', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BAS2B', 'FR-2B', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BBY93', 'FR-93', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BEB01', 'FR-01', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BES25', 'FR-25', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BLD55', 'FR-55', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BLF90', 'FR-90', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BLO41', 'FR-41', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BOR33', 'FR-33', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BOU', 'FR-21', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BOU', 'FR-58', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BOU', 'FR-71', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BOU', 'FR-89', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BOU18', 'FR-18', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BRE', 'FR-22', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BRE', 'FR-29', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BRE', 'FR-35', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BRE', 'FR-56', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-BVA60', 'FR-60', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CAE14', 'FR-14', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CAH46', 'FR-46', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CAR11', 'FR-11', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CBY73', 'FR-73', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEC51', 'FR-51', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEN', 'FR-18', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEN', 'FR-28', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEN', 'FR-36', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEN', 'FR-37', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEN', 'FR-41', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CEN', 'FR-45', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHA', 'FR-08', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHA', 'FR-10', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHA', 'FR-51', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHA', 'FR-52', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHA28', 'FR-28', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHM08', 'FR-08', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHM52', 'FR-52', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CHT36', 'FR-36', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CLF63', 'FR-63', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CLM68', 'FR-68', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-COR', 'FR-20', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-COR', 'FR-2A', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-COR', 'FR-2B', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-CTL94', 'FR-94', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-DIG04', 'FR-04', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-DIJ21', 'FR-21', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-DOM', 'FR-971', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-DOM', 'FR-972', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-DOM', 'FR-973', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-DOM', 'FR-974', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-EPI88', 'FR-88', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-EVR27', 'FR-27', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-EVR91', 'FR-91', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-FCO', 'FR-25', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-FCO', 'FR-39', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-FCO', 'FR-70', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-FCO', 'FR-90', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-FOI09', 'FR-09', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-GAP05', 'FR-05', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-GRE38', 'FR-38', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-GUE23', 'FR-23', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-HNO', 'FR-27', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-HNO', 'FR-76', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-75', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-77', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-78', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-91', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-92', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-93', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-94', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-IDF', 'FR-95', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LAO02', 'FR-02', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LAR', 'FR-11', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LAR', 'FR-30', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LAR', 'FR-34', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LAR', 'FR-48', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LAR', 'FR-66', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LIL59', 'FR-59', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LIM', 'FR-19', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LIM', 'FR-23', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LIM', 'FR-87', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LMA72', 'FR-72', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LMG87', 'FR-87', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LON39', 'FR-39', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LOR', 'FR-54', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LOR', 'FR-55', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LOR', 'FR-57', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LOR', 'FR-88', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LRO17', 'FR-17', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LVL53', 'FR-53', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-LYO69', 'FR-69', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MAR13', 'FR-13', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MCN71', 'FR-71', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MDM40', 'FR-40', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MEN48', 'FR-48', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MET57', 'FR-57', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-09', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-12', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-31', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-32', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-46', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-65', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-81', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MIP', 'FR-82', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MLN77', 'FR-77', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MON34', 'FR-34', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MOU03', 'FR-03', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-MTB82', 'FR-82', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NAN44', 'FR-44', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NCY54', 'FR-54', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NEV58', 'FR-58', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NIC06', 'FR-06', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NIM30', 'FR-30', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NIO79', 'FR-79', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NPC', 'FR-59', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NPC', 'FR-62', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-NTR92', 'FR-92', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ORL45', 'FR-45', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAC', 'FR-04', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAC', 'FR-05', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAC', 'FR-06', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAC', 'FR-13', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAC', 'FR-83', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAC', 'FR-84', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAR75', 'FR-75', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PAU64', 'FR-64', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PDL', 'FR-44', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PDL', 'FR-49', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PDL', 'FR-53', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PDL', 'FR-72', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PDL', 'FR-85', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PER24', 'FR-24', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PEV43', 'FR-43', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PIC', 'FR-02', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PIC', 'FR-60', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PIC', 'FR-80', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-POC', 'FR-16', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-POC', 'FR-17', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-POC', 'FR-79', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-POC', 'FR-86', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-POI86', 'FR-86', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PPG66', 'FR-66', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PRI07', 'FR-07', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-PTO95', 'FR-95', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-QUI29', 'FR-29', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-01', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-07', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-26', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-38', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-42', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-69', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-73', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RAL', 'FR-74', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-REN35', 'FR-35', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ROD12', 'FR-12', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-ROU76', 'FR-76', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-RSY85', 'FR-85', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-SBG67', 'FR-67', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-SBR22', 'FR-22', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-SET42', 'FR-42', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-SLO50', 'FR-50', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TBS65', 'FR-65', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TLN83', 'FR-83', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TLS31', 'FR-31', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TOM', 'FR-975', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TOM', 'FR-976', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TOM', 'FR-986', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TOM', 'FR-987', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TOM', 'FR-988', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TOU37', 'FR-37', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TRO10', 'FR-10', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-TUL19', 'FR-19', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-VAL26', 'FR-26', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-VAN56', 'FR-56', 'COUNTY_SEAT', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-VSL70', 'FR-70', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FR-VSS78', 'FR-78', 'COUNTY_SEAT', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-ALS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-AQU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-AUV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-BAN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-BOU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-BRE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-CEN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-CHA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-COR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-FCO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-HNO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-IDF', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-LAR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-LIM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-LOR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-MIP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-NPC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-PAC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-PDL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-PIC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-POC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('FRA', 'FR-RAL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('GA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('GBR', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('GRC', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('GU', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('HI', 'AKHI', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('HUN', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ID', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IL', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IN', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-AN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-AP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-AR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-AS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-BR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-CH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-CT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-DD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-DN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-GA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-GJ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-HP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-HR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-JH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-JK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-KA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-KL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-LD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-MH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-ML', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-MN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-MP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-MZ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-ND', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-NL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-OR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-PB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-PY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-RJ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-SK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-TN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-TR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-UP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-UT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IND', 'IN-WB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IRL', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-CAVN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-CLARE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-CORK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-CRLW', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-DBLN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-DNGL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-GALW', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-KERRY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-KLDR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-KLKNY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-LAOIS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-LGFD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-LMRK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-LOUTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-LTRM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-MAYO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-MEATH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-MNGHN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-OFLY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-RSCMN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-SLIGO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-TPRY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-WKLW', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-WMETH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-WTFD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('IRL', 'IRL-WXFD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ITA', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AG', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AP', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AQ', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-AV', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BG', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-BZ', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CA', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CB', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CE', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CH', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CI', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CL', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CO', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CS', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CT', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-CZ', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-EN', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-FC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-FE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-FG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-FI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-FM', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-FR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-GE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-GO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-GR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-IM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-IS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-KR', 'REGIONS', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-LC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-LE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-LI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-LO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-LT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-LU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-ME', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-MT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-NA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-NO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-NU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-OG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-OR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-OT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-PZ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-RO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-SV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-TV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-UD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('ITA', 'IT-VV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:56', '2011-12-01 17:17:57', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('KS', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('KY', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('LA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('LTU', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('LUX', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('LVA', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('MA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MD', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ME', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MEXCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-AG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-BN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-BS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-CA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-CH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-CL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-CM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-CP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-DF', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-DU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-GJ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-GR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-HI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-JA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-MC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-MR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-MX', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-NA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-NL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-OA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-PU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-QE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-QR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-SI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-SL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-SO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-TB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-TL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-TM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-VE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-YU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'MX-ZA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MEX', 'USMEXCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MH', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MI', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MLT', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('MN', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MO', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MP', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MS', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('MT', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NC', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ND', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NE', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NH', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'GBR', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'NIRL-ARMG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'NIRL-ATRM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'NIRL-DOWN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'NIRL-FMNH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'NIRL-LDRY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NIRL', 'NIRL-TYR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NJ', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-DR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-FL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-FR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-GL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-GR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-LB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-NB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-NH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-OV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-UT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-ZE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-ZH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NLD', 'NL-ZL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NM', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NV', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('NY', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('OH', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('OK', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('OR', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('PA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('POL', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('PR', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('PRT', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('PW', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('RI', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('ROU', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('SC', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ABDN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ANGS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ARGL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-AYRS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-BANF', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-BUTE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-CLAK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-CMTY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-DMBR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-DMFS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ELOTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-FIFE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-INVER', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-KNDN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-KRCUD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-KRSS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-LRKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-MLOTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-MORAY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-NAIRN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ORK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-PEEBS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-PERTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-RNFR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ROSS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-ROXB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-SELKS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-SHET', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-STLNG', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-SUTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-WGNSH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GB-WLOTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SCOT', 'GBR', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('SD', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('SVK', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('SVN', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('SWE', 'EU', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('TN', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('TX', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('UM', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AP', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'AZ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'CA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'CO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'CT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'DC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'DE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'FL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'GA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'GU', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'HI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'IA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'ID', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'IL', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'IN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'KS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'KY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'LA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'ME', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MO', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'MT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'ND', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NJ', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'NY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'OH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'OK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'OR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'PA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'PR', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'RI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'SC', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'SD', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'TN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'TX', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'USA-84057', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'USCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'USMEXCAN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'UT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'VA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'VI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'VT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'WA', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'WI', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'WV', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USA', 'WY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USAF', 'USALL', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USCN', 'US50', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USCN', 'USALL', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('USTR', 'AHUST', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('UT', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('UT', 'UT-SANPETE', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('UT', 'UT-UTAH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('UT-UTAH', 'USA-84057', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('VA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('VI', 'USTR', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('VT', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WA', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-AGSY', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-BREK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-CDGN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-CMRN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-CNFN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-CTHN', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-DENB', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-FTSH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-GLAM', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-MIENT', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-MMTH', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-MNTGS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-PMBRK', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GB-RNRS', 'REGIONS', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WALS', 'GBR', 'GROUP_MEMBER', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_assoc` VALUES ('WI', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WV', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `geo_assoc` VALUES ('WY', 'USCN', 'GROUP_MEMBER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `geo_assoc_type`
-- ----------------------------
DROP TABLE IF EXISTS `geo_assoc_type`;
CREATE TABLE `geo_assoc_type` (
  `GEO_ASSOC_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GEO_ASSOC_TYPE_ID`),
  KEY `G_ASSC_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `G_ASSC_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of geo_assoc_type
-- ----------------------------
INSERT INTO `geo_assoc_type` VALUES ('COUNTY_CITY', 'City in a county', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_assoc_type` VALUES ('COUNTY_SEAT', 'Administrative Main City of a County', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_assoc_type` VALUES ('GROUP_MEMBER', 'Geo Group Member', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_assoc_type` VALUES ('POSTAL_CODE', 'Postal code associated to a GeoType', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_assoc_type` VALUES ('REGIONS', 'For a region of a larger Geo, i.e. states, counties, provinces...', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `geo_point`
-- ----------------------------
DROP TABLE IF EXISTS `geo_point`;
CREATE TABLE `geo_point` (
  `GEO_POINT_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DATA_SOURCE_ID` varchar(20) collate latin1_general_cs default NULL,
  `LATITUDE` double NOT NULL,
  `LONGITUDE` double NOT NULL,
  `ELEVATION` double default NULL,
  `ELEVATION_UOM_ID` varchar(20) collate latin1_general_cs default NULL,
  `INFORMATION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GEO_POINT_ID`),
  KEY `GEOPOINT_DTSRC` (`DATA_SOURCE_ID`),
  KEY `GPT_TYPE_UOM` (`ELEVATION_UOM_ID`),
  KEY `GEO_POINT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `GEO_POINT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `GEOPOINT_DTSRC` FOREIGN KEY (`DATA_SOURCE_ID`) REFERENCES `data_source` (`DATA_SOURCE_ID`),
  CONSTRAINT `GPT_TYPE_UOM` FOREIGN KEY (`ELEVATION_UOM_ID`) REFERENCES `uom` (`UOM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of geo_point
-- ----------------------------

-- ----------------------------
-- Table structure for `geo_type`
-- ----------------------------
DROP TABLE IF EXISTS `geo_type`;
CREATE TABLE `geo_type` (
  `GEO_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `HAS_TABLE` char(1) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GEO_TYPE_ID`),
  KEY `GEO_TYPE_PARENT` (`PARENT_TYPE_ID`),
  KEY `GEO_TYPE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `GEO_TYPE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `GEO_TYPE_PARENT` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `geo_type` (`GEO_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of geo_type
-- ----------------------------
INSERT INTO `geo_type` VALUES ('CITY', null, 'N', 'City', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('COUNTRY', null, 'N', 'Country', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('COUNTY', null, 'N', 'County', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('COUNTY_CITY', null, 'N', 'County-City', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('GROUP', null, 'N', 'Group', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('MUNICIPALITY', null, 'N', 'Municipality', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('POSTAL_CODE', null, 'N', 'Postal Code', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('PROVINCE', null, 'N', 'Province', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('REGION', null, 'N', 'Region', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('SALES_TERRITORY', 'TERRITORY', 'N', 'Sales Territory', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('SERVICE_TERRITORY', 'TERRITORY', 'N', 'Service Territory', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('STATE', null, 'N', 'State', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `geo_type` VALUES ('TERRITORY', null, 'N', 'Territory', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `geo_with_state`
-- ----------------------------
DROP TABLE IF EXISTS `geo_with_state`;
CREATE TABLE `geo_with_state` (
  `GEO_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `SHOW_STATE_LIST` char(1) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GEO_ID`),
  KEY `GEO` (`GEO_ID`),
  KEY `G_WTH_STT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `G_WTH_STT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `GEO` FOREIGN KEY (`GEO_ID`) REFERENCES `geo` (`GEO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of geo_with_state
-- ----------------------------
INSERT INTO `geo_with_state` VALUES ('AUS', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('BGR', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('BRA', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('CAN', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('CHN', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('COL', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('DEU', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('ENGL', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('GBR', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('IND', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('ITA', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('MEX', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('NIRL', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('NLD', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('POL', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('SCOT', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('USA', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `geo_with_state` VALUES ('WALS', '1', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');

-- ----------------------------
-- Table structure for `job_sandbox`
-- ----------------------------
DROP TABLE IF EXISTS `job_sandbox`;
CREATE TABLE `job_sandbox` (
  `JOB_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `JOB_NAME` varchar(100) collate latin1_general_cs default NULL,
  `RUN_TIME` datetime default NULL,
  `POOL_ID` varchar(100) collate latin1_general_cs default NULL,
  `STATUS_ID` varchar(20) collate latin1_general_cs default NULL,
  `PARENT_JOB_ID` varchar(20) collate latin1_general_cs default NULL,
  `PREVIOUS_JOB_ID` varchar(20) collate latin1_general_cs default NULL,
  `SERVICE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `LOADER_NAME` varchar(100) collate latin1_general_cs default NULL,
  `MAX_RETRY` decimal(20,0) default NULL,
  `AUTH_USER_LOGIN_ID` varchar(250) collate latin1_general_cs default NULL,
  `RUN_AS_USER` varchar(250) collate latin1_general_cs default NULL,
  `RUNTIME_DATA_ID` varchar(20) collate latin1_general_cs default NULL,
  `RECURRENCE_INFO_ID` varchar(20) collate latin1_general_cs default NULL,
  `TEMP_EXPR_ID` varchar(20) collate latin1_general_cs default NULL,
  `CURRENT_RECURRENCE_COUNT` decimal(20,0) default NULL,
  `MAX_RECURRENCE_COUNT` decimal(20,0) default NULL,
  `RUN_BY_INSTANCE_ID` varchar(20) collate latin1_general_cs default NULL,
  `START_DATE_TIME` datetime default NULL,
  `FINISH_DATE_TIME` datetime default NULL,
  `CANCEL_DATE_TIME` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`JOB_ID`),
  KEY `JOB_SNDBX_RECINFO` (`RECURRENCE_INFO_ID`),
  KEY `JOB_SNDBX_TEMPEXPR` (`TEMP_EXPR_ID`),
  KEY `JOB_SNDBX_RNTMDTA` (`RUNTIME_DATA_ID`),
  KEY `JOB_SNDBX_AUSRLGN` (`AUTH_USER_LOGIN_ID`),
  KEY `JOB_SNDBX_USRLGN` (`RUN_AS_USER`),
  KEY `JOB_SNDBX_STTS` (`STATUS_ID`),
  KEY `JOB_SANDBOX_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `JOB_SANDBOX_TXCRTS` (`CREATED_TX_STAMP`),
  KEY `JOB_SNDBX_RUNSTAT` (`RUN_BY_INSTANCE_ID`,`STATUS_ID`),
  CONSTRAINT `JOB_SNDBX_AUSRLGN` FOREIGN KEY (`AUTH_USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`),
  CONSTRAINT `JOB_SNDBX_RECINFO` FOREIGN KEY (`RECURRENCE_INFO_ID`) REFERENCES `recurrence_info` (`RECURRENCE_INFO_ID`),
  CONSTRAINT `JOB_SNDBX_RNTMDTA` FOREIGN KEY (`RUNTIME_DATA_ID`) REFERENCES `runtime_data` (`RUNTIME_DATA_ID`),
  CONSTRAINT `JOB_SNDBX_STTS` FOREIGN KEY (`STATUS_ID`) REFERENCES `status_item` (`STATUS_ID`),
  CONSTRAINT `JOB_SNDBX_TEMPEXPR` FOREIGN KEY (`TEMP_EXPR_ID`) REFERENCES `temporal_expression` (`TEMP_EXPR_ID`),
  CONSTRAINT `JOB_SNDBX_USRLGN` FOREIGN KEY (`RUN_AS_USER`) REFERENCES `user_login` (`USER_LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of job_sandbox
-- ----------------------------
INSERT INTO `job_sandbox` VALUES ('8200', 'Clear EntitySyncRemove Info', '2000-01-01 00:00:00', 'pool', null, null, null, 'cleanSyncRemoveInfo', null, null, null, 'system', null, null, 'MIDNIGHT_DAILY', null, '-1', null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `job_sandbox` VALUES ('PURGE_OLD_JOBS', 'Purge Old Jobs', '2000-01-01 00:00:00', 'pool', null, null, null, 'purgeOldJobs', null, null, null, 'system', null, null, 'MIDNIGHT_DAILY', null, '-1', null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `keyword_thesaurus`
-- ----------------------------
DROP TABLE IF EXISTS `keyword_thesaurus`;
CREATE TABLE `keyword_thesaurus` (
  `ENTERED_KEYWORD` varchar(255) collate latin1_general_cs NOT NULL,
  `ALTERNATE_KEYWORD` varchar(255) collate latin1_general_cs NOT NULL,
  `RELATIONSHIP_ENUM_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`ENTERED_KEYWORD`,`ALTERNATE_KEYWORD`),
  KEY `KW_THRS_RLENM` (`RELATIONSHIP_ENUM_ID`),
  KEY `KWRD_THSRS_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `KWRD_THSRS_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `KW_THRS_RLENM` FOREIGN KEY (`RELATIONSHIP_ENUM_ID`) REFERENCES `enumeration` (`ENUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of keyword_thesaurus
-- ----------------------------

-- ----------------------------
-- Table structure for `note_data`
-- ----------------------------
DROP TABLE IF EXISTS `note_data`;
CREATE TABLE `note_data` (
  `NOTE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `NOTE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `NOTE_INFO` longtext collate latin1_general_cs,
  `NOTE_DATE_TIME` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`NOTE_ID`),
  KEY `NOTE_DATA_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `NOTE_DATA_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of note_data
-- ----------------------------

-- ----------------------------
-- Table structure for `operation`
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation` (
  `OPERATION_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `APP` varchar(255) collate latin1_general_cs default NULL,
  `OPERATION_TYPE_ID` varchar(60) collate latin1_general_cs default NULL,
  `OPERATION_NAME` varchar(100) collate latin1_general_cs default NULL,
  `GROUP_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`OPERATION_ID`),
  KEY `MONITOR_TYPE` (`OPERATION_TYPE_ID`),
  KEY `OPERATION_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPERATION_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `MONITOR_TYPE` FOREIGN KEY (`OPERATION_TYPE_ID`) REFERENCES `operation_type` (`OPERATION_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `operation_attribute`;
CREATE TABLE `operation_attribute` (
  `OPERATION_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ATTR_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `ATTR_VALUE` varchar(255) collate latin1_general_cs default NULL,
  `ATTR_OBJ_VALUE` longblob,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`OPERATION_ID`,`ATTR_NAME`),
  KEY `MONITOR_ATTR` (`OPERATION_ID`),
  KEY `OPRTN_ATTRT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRTN_ATTRT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `MONITOR_ATTR` FOREIGN KEY (`OPERATION_ID`) REFERENCES `operation` (`OPERATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_attribute_log`
-- ----------------------------
DROP TABLE IF EXISTS `operation_attribute_log`;
CREATE TABLE `operation_attribute_log` (
  `LOG_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `OPERATION_ID` varchar(20) collate latin1_general_cs default NULL,
  `NAME` longblob,
  `LOG_TIME` datetime default NULL,
  `CATEGORY` varchar(10) collate latin1_general_cs default NULL,
  `DESCRIPTION` longblob,
  `MEASUREMENT` longblob,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`LOG_ID`),
  KEY `OPRN_ATTT_LG_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRN_ATTT_LG_TXCRS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_attribute_log
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_attribute_log_values`
-- ----------------------------
DROP TABLE IF EXISTS `operation_attribute_log_values`;
CREATE TABLE `operation_attribute_log_values` (
  `LOG_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ATTR_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `ATTR_VALUE` varchar(255) collate latin1_general_cs default NULL,
  `STYPE` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`LOG_ID`,`ATTR_NAME`),
  KEY `OPN_ATT_LG_VLS_TXP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPN_ATT_LG_VLS_TXS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_attribute_log_values
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_group`
-- ----------------------------
DROP TABLE IF EXISTS `operation_group`;
CREATE TABLE `operation_group` (
  `GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GROUP_ID`),
  KEY `MONITOR_GROUP` (`GROUP_ID`),
  KEY `OPRTN_GRP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRTN_GRP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_group
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_machine`
-- ----------------------------
DROP TABLE IF EXISTS `operation_machine`;
CREATE TABLE `operation_machine` (
  `OPERATION_ID` varchar(20) collate latin1_general_cs default NULL,
  `MACHINE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`MACHINE_ID`),
  KEY `OPRTN_MCHN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRTN_MCHN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_machine
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_proxy`
-- ----------------------------
DROP TABLE IF EXISTS `operation_proxy`;
CREATE TABLE `operation_proxy` (
  `OPERATIONPROXY_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PROXY_ID` varchar(20) collate latin1_general_cs default NULL,
  `CI_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`OPERATIONPROXY_ID`),
  KEY `OPRTN_PRX_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRTN_PRX_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_proxy
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_tag`
-- ----------------------------
DROP TABLE IF EXISTS `operation_tag`;
CREATE TABLE `operation_tag` (
  `TAG_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `OPERATION_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TAG_ID`,`OPERATION_ID`),
  KEY `OPRTN_TG_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRTN_TG_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_tag
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_type`
-- ----------------------------
DROP TABLE IF EXISTS `operation_type`;
CREATE TABLE `operation_type` (
  `OPERATION_TYPE_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(60) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`OPERATION_TYPE_ID`),
  KEY `MONITOR_TYPPAR` (`PARENT_TYPE_ID`),
  KEY `OPRTN_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRTN_TP_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `MONITOR_TYPPAR` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `operation_type` (`OPERATION_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_type
-- ----------------------------

-- ----------------------------
-- Table structure for `operation_type_attr`
-- ----------------------------
DROP TABLE IF EXISTS `operation_type_attr`;
CREATE TABLE `operation_type_attr` (
  `OPERATION_TYPE_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `ATTR_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `DATA_TYPE` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`OPERATION_TYPE_ID`,`ATTR_NAME`),
  KEY `MONITOR_TYPATTR` (`OPERATION_TYPE_ID`),
  KEY `OPRN_TP_ATR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `OPRN_TP_ATR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `MONITOR_TYPATTR` FOREIGN KEY (`OPERATION_TYPE_ID`) REFERENCES `operation_type` (`OPERATION_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of operation_type_attr
-- ----------------------------

-- ----------------------------
-- Table structure for `period_type`
-- ----------------------------
DROP TABLE IF EXISTS `period_type`;
CREATE TABLE `period_type` (
  `PERIOD_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `PERIOD_LENGTH` decimal(20,0) default NULL,
  `UOM_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PERIOD_TYPE_ID`),
  KEY `PER_TYPE_UOM` (`UOM_ID`),
  KEY `PERIOD_TYPE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PERIOD_TYPE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `PER_TYPE_UOM` FOREIGN KEY (`UOM_ID`) REFERENCES `uom` (`UOM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of period_type
-- ----------------------------
INSERT INTO `period_type` VALUES ('FISCAL_BIWEEK', 'Fiscal Bi-Week', '2', 'TF_wk', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `period_type` VALUES ('FISCAL_MONTH', 'Fiscal Month', '1', 'TF_mon', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `period_type` VALUES ('FISCAL_QUARTER', 'Fiscal Quarter', '3', 'TF_mon', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `period_type` VALUES ('FISCAL_WEEK', 'Fiscal Week', '1', 'TF_wk', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `period_type` VALUES ('FISCAL_YEAR', 'Fiscal Year', '1', 'TF_yr', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `period_type` VALUES ('SALES_MONTH', 'Sales Month', '1', 'TF_mon', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `period_type` VALUES ('SALES_QUARTER', 'Sales Quarter', '3', 'TF_mon', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `platform_type`
-- ----------------------------
DROP TABLE IF EXISTS `platform_type`;
CREATE TABLE `platform_type` (
  `PLATFORM_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PLATFORM_NAME` varchar(100) collate latin1_general_cs default NULL,
  `PLATFORM_VERSION` varchar(10) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PLATFORM_TYPE_ID`),
  KEY `PLTFRM_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PLTFRM_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of platform_type
-- ----------------------------

-- ----------------------------
-- Table structure for `portal_page`
-- ----------------------------
DROP TABLE IF EXISTS `portal_page`;
CREATE TABLE `portal_page` (
  `PORTAL_PAGE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTAL_PAGE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `OWNER_USER_LOGIN_ID` varchar(20) collate latin1_general_cs default NULL,
  `ORIGINAL_PORTAL_PAGE_ID` varchar(20) collate latin1_general_cs default NULL,
  `PARENT_PORTAL_PAGE_ID` varchar(20) collate latin1_general_cs default NULL,
  `SEQUENCE_NUM` decimal(20,0) default NULL,
  `SECURITY_GROUP_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTAL_PAGE_ID`),
  KEY `PortPage_PARENT` (`PARENT_PORTAL_PAGE_ID`),
  KEY `PORTPAGE_SECGRP` (`SECURITY_GROUP_ID`),
  KEY `PORTAL_PAGE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PORTAL_PAGE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `PortPage_PARENT` FOREIGN KEY (`PARENT_PORTAL_PAGE_ID`) REFERENCES `portal_page` (`PORTAL_PAGE_ID`),
  CONSTRAINT `PORTPAGE_SECGRP` FOREIGN KEY (`SECURITY_GROUP_ID`) REFERENCES `security_group` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portal_page
-- ----------------------------
INSERT INTO `portal_page` VALUES ('EXAMPLE', 'Example Portal Page', 'The default example OFBiz portal page', '_NA_', null, null, '0', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page` VALUES ('EXAMPLE1', 'Example Portal Page 1', 'Portal page 1', '_NA_', null, 'EXAMPLE', '100', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page` VALUES ('EXAMPLE2', 'Example Portal Page 2', 'Portal page 2', '_NA_', null, 'EXAMPLE', '200', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page` VALUES ('_NA_', 'For using genericPortletScreen', 'To be able to have parameters at the portlet level, not PortletPage level', '_NA_', null, null, '0', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `portal_page_column`
-- ----------------------------
DROP TABLE IF EXISTS `portal_page_column`;
CREATE TABLE `portal_page_column` (
  `PORTAL_PAGE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `COLUMN_SEQ_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `COLUMN_WIDTH_PIXELS` decimal(20,0) default NULL,
  `COLUMN_WIDTH_PERCENTAGE` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTAL_PAGE_ID`,`COLUMN_SEQ_ID`),
  KEY `PRTL_PGCOL_PAGE` (`PORTAL_PAGE_ID`),
  KEY `PRTL_PG_CLN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTL_PG_CLN_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `PRTL_PGCOL_PAGE` FOREIGN KEY (`PORTAL_PAGE_ID`) REFERENCES `portal_page` (`PORTAL_PAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portal_page_column
-- ----------------------------
INSERT INTO `portal_page_column` VALUES ('EXAMPLE', '00001', '400', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page_column` VALUES ('EXAMPLE', '00002', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page_column` VALUES ('EXAMPLE1', '00001', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page_column` VALUES ('EXAMPLE2', '00001', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `portal_page_portlet`
-- ----------------------------
DROP TABLE IF EXISTS `portal_page_portlet`;
CREATE TABLE `portal_page_portlet` (
  `PORTAL_PAGE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTAL_PORTLET_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTLET_SEQ_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `COLUMN_SEQ_ID` varchar(20) collate latin1_general_cs default NULL,
  `SEQUENCE_NUM` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTAL_PAGE_ID`,`PORTAL_PORTLET_ID`,`PORTLET_SEQ_ID`),
  KEY `PRTL_PGPTLT_PAGE` (`PORTAL_PAGE_ID`),
  KEY `PRTL_PGPTLT_PTLT` (`PORTAL_PORTLET_ID`),
  KEY `PRL_PG_PRTT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRL_PG_PRTT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `PRTL_PGPTLT_PAGE` FOREIGN KEY (`PORTAL_PAGE_ID`) REFERENCES `portal_page` (`PORTAL_PAGE_ID`),
  CONSTRAINT `PRTL_PGPTLT_PTLT` FOREIGN KEY (`PORTAL_PORTLET_ID`) REFERENCES `portal_portlet` (`PORTAL_PORTLET_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portal_page_portlet
-- ----------------------------
INSERT INTO `portal_page_portlet` VALUES ('EXAMPLE', 'EXAMPLE_1', '00001', '00001', '0', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page_portlet` VALUES ('EXAMPLE', 'EXAMPLE_3', '00001', '00002', '1', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page_portlet` VALUES ('EXAMPLE1', 'EXAMPLE_1', '00001', '00001', '1', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_page_portlet` VALUES ('EXAMPLE2', 'EXAMPLE_1', '00001', '00001', '1', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `portal_portlet`
-- ----------------------------
DROP TABLE IF EXISTS `portal_portlet`;
CREATE TABLE `portal_portlet` (
  `PORTAL_PORTLET_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTLET_NAME` varchar(100) collate latin1_general_cs default NULL,
  `SCREEN_NAME` varchar(255) collate latin1_general_cs default NULL,
  `SCREEN_LOCATION` varchar(255) collate latin1_general_cs default NULL,
  `EDIT_FORM_NAME` varchar(255) collate latin1_general_cs default NULL,
  `EDIT_FORM_LOCATION` varchar(255) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `SCREENSHOT` varchar(255) collate latin1_general_cs default NULL,
  `SECURITY_SERVICE_NAME` varchar(255) collate latin1_general_cs default NULL,
  `SECURITY_MAIN_ACTION` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTAL_PORTLET_ID`),
  KEY `PRTL_PRTLT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTL_PRTLT_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portal_portlet
-- ----------------------------
INSERT INTO `portal_portlet` VALUES ('EXAMPLE_1', 'Example 1', 'ExamplePortlet1', 'component://example/widget/example/PortletScreens.xml', 'ExamplePortlet1Edit', 'component://example/widget/example/PortletEditForms.xml', 'Example portlet n. 1', '/images/ofbiz_logo.gif', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_portlet` VALUES ('EXAMPLE_2', 'Example 2', 'ExamplePortlet2', 'component://example/widget/example/PortletScreens.xml', 'ExamplePortlet2Edit', 'component://example/widget/example/PortletEditForms.xml', 'Example portlet n. 2', '/images/opentravelsystem_logo.jpg', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_portlet` VALUES ('EXAMPLE_3', 'Example 3', 'ExamplePortlet3', 'component://example/widget/example/PortletScreens.xml', 'ExamplePortlet3Edit', 'component://example/widget/example/PortletEditForms.xml', 'Example portlet n. 3', null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portal_portlet` VALUES ('FindGenericEntity', 'Entity List simple search criteria', 'FindGenericEntity', 'component://common/widget/PortalPageScreens.xml', 'FindGenericEntityParam', 'component://common/widget/PortletEditForms.xml', 'Simple search criteria, only Id (param idName) and description for entityList, result is sent to List${Entity}Area (FindGenericEntityPrtl define this area). This portel has 3 mandatory parameters.', null, null, 'VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portal_portlet` VALUES ('GenericPortalPage', 'Sub PortalPage', 'GenericPortalPage', 'component://common/widget/PortalPageScreens.xml', 'GenericPortalPageParam', 'component://common/widget/PortletEditForms.xml', 'a portlet which included a portalPage to be able to have multiple line, each line being a sub page. This portlet has 1 mandatory parameters.', null, null, 'VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portal_portlet` VALUES ('GenericScreenlet', 'Screenlet with one screen', 'GenericScreenlet', 'component://common/widget/PortalPageScreens.xml', 'GenericScreenletParam', 'component://common/widget/PortletEditForms.xml', 'Simple portlet using screenlet, with one screen. This portlet has 3 mandatory parameters.', null, null, 'VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portal_portlet` VALUES ('GenericScreenletAjax', 'Screenlet with one screen called by ajax', 'GenericScreenletAjax', 'component://common/widget/PortalPageScreens.xml', 'GenericScreenletAjaxParam', 'component://common/widget/PortletEditForms.xml', 'Simple portlet using screenlet, with one screen which was update by ajax process, so with a div. This portlet has 4 mandatory parameters.', null, null, 'VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portal_portlet` VALUES ('GricScltAjaxWithMenu', 'Screenlet with one screen, and one menu called by ajax', 'GenericScreenletAjaxWithMenu', 'component://common/widget/PortalPageScreens.xml', 'GenericScreenletAjaxWithMenuParam', 'component://common/widget/PortletEditForms.xml', 'Simple portlet using screenlet with a menu, with one screen which was update by ajax process, so with a div. This portlet has 6 mandatory parameters.', null, null, 'VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `portlet_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `portlet_attribute`;
CREATE TABLE `portlet_attribute` (
  `PORTAL_PAGE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTAL_PORTLET_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTLET_SEQ_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `ATTR_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `ATTR_VALUE` varchar(255) collate latin1_general_cs default NULL,
  `ATTR_TYPE` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTAL_PAGE_ID`,`PORTAL_PORTLET_ID`,`PORTLET_SEQ_ID`,`ATTR_NAME`),
  KEY `PTLT_ATTR_PTLT` (`PORTAL_PORTLET_ID`),
  KEY `PRTLT_ATTRT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTLT_ATTRT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `PTLT_ATTR_PTLT` FOREIGN KEY (`PORTAL_PORTLET_ID`) REFERENCES `portal_portlet` (`PORTAL_PORTLET_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portlet_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `portlet_category`
-- ----------------------------
DROP TABLE IF EXISTS `portlet_category`;
CREATE TABLE `portlet_category` (
  `PORTLET_CATEGORY_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTLET_CATEGORY_ID`),
  KEY `PRTLT_CTGR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTLT_CTGR_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portlet_category
-- ----------------------------
INSERT INTO `portlet_category` VALUES ('EXAMPLE', 'Example Portlet(s)', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portlet_category` VALUES ('GENERIC_PORTLET', 'Generics portlets usable for easily parametrize portalPages', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `portlet_portlet_category`
-- ----------------------------
DROP TABLE IF EXISTS `portlet_portlet_category`;
CREATE TABLE `portlet_portlet_category` (
  `PORTAL_PORTLET_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PORTLET_CATEGORY_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PORTAL_PORTLET_ID`,`PORTLET_CATEGORY_ID`),
  KEY `PPTLTCAT_PTPL` (`PORTAL_PORTLET_ID`),
  KEY `PPTLTCAT_PTLTCAT` (`PORTLET_CATEGORY_ID`),
  KEY `PRTT_PRT_CTR_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTT_PRT_CTR_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `PPTLTCAT_PTLTCAT` FOREIGN KEY (`PORTLET_CATEGORY_ID`) REFERENCES `portlet_category` (`PORTLET_CATEGORY_ID`),
  CONSTRAINT `PPTLTCAT_PTPL` FOREIGN KEY (`PORTAL_PORTLET_ID`) REFERENCES `portal_portlet` (`PORTAL_PORTLET_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of portlet_portlet_category
-- ----------------------------
INSERT INTO `portlet_portlet_category` VALUES ('EXAMPLE_1', 'EXAMPLE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portlet_portlet_category` VALUES ('EXAMPLE_2', 'EXAMPLE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portlet_portlet_category` VALUES ('EXAMPLE_3', 'EXAMPLE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `portlet_portlet_category` VALUES ('FindGenericEntity', 'GENERIC_PORTLET', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portlet_portlet_category` VALUES ('GenericPortalPage', 'GENERIC_PORTLET', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portlet_portlet_category` VALUES ('GenericScreenlet', 'GENERIC_PORTLET', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portlet_portlet_category` VALUES ('GenericScreenletAjax', 'GENERIC_PORTLET', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `portlet_portlet_category` VALUES ('GricScltAjaxWithMenu', 'GENERIC_PORTLET', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `protected_view`
-- ----------------------------
DROP TABLE IF EXISTS `protected_view`;
CREATE TABLE `protected_view` (
  `GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `VIEW_NAME_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `MAX_HITS` decimal(20,0) default NULL,
  `MAX_HITS_DURATION` decimal(20,0) default NULL,
  `TARPIT_DURATION` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GROUP_ID`,`VIEW_NAME_ID`),
  KEY `VIEW_SECGRP_GRP` (`GROUP_ID`),
  KEY `PRTCTD_VW_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTCTD_VW_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `VIEW_SECGRP_GRP` FOREIGN KEY (`GROUP_ID`) REFERENCES `security_group` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of protected_view
-- ----------------------------

-- ----------------------------
-- Table structure for `protocol_type`
-- ----------------------------
DROP TABLE IF EXISTS `protocol_type`;
CREATE TABLE `protocol_type` (
  `PROTOCOL_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PROTOCOL_NAME` varchar(100) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PROTOCOL_TYPE_ID`),
  KEY `PRTCL_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PRTCL_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of protocol_type
-- ----------------------------

-- ----------------------------
-- Table structure for `proxy`
-- ----------------------------
DROP TABLE IF EXISTS `proxy`;
CREATE TABLE `proxy` (
  `PROXY_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `HOST` varchar(100) collate latin1_general_cs default NULL,
  `ERLANGNODE` varchar(100) collate latin1_general_cs default NULL,
  `COOKIE` varchar(60) collate latin1_general_cs default NULL,
  `PLATFORM` varchar(100) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PROXY_ID`),
  KEY `PROXY_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `PROXY_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of proxy
-- ----------------------------

-- ----------------------------
-- Table structure for `recurrence_info`
-- ----------------------------
DROP TABLE IF EXISTS `recurrence_info`;
CREATE TABLE `recurrence_info` (
  `RECURRENCE_INFO_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `START_DATE_TIME` datetime default NULL,
  `EXCEPTION_DATE_TIMES` longtext collate latin1_general_cs,
  `RECURRENCE_DATE_TIMES` longtext collate latin1_general_cs,
  `EXCEPTION_RULE_ID` varchar(20) collate latin1_general_cs default NULL,
  `RECURRENCE_RULE_ID` varchar(20) collate latin1_general_cs default NULL,
  `RECURRENCE_COUNT` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`RECURRENCE_INFO_ID`),
  KEY `REC_INFO_RCRLE` (`RECURRENCE_RULE_ID`),
  KEY `REC_INFO_EX_RCRLE` (`EXCEPTION_RULE_ID`),
  KEY `RCRRNC_INF_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `RCRRNC_INF_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `REC_INFO_EX_RCRLE` FOREIGN KEY (`EXCEPTION_RULE_ID`) REFERENCES `recurrence_rule` (`RECURRENCE_RULE_ID`),
  CONSTRAINT `REC_INFO_RCRLE` FOREIGN KEY (`RECURRENCE_RULE_ID`) REFERENCES `recurrence_rule` (`RECURRENCE_RULE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of recurrence_info
-- ----------------------------

-- ----------------------------
-- Table structure for `recurrence_rule`
-- ----------------------------
DROP TABLE IF EXISTS `recurrence_rule`;
CREATE TABLE `recurrence_rule` (
  `RECURRENCE_RULE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `FREQUENCY` varchar(60) collate latin1_general_cs default NULL,
  `UNTIL_DATE_TIME` datetime default NULL,
  `COUNT_NUMBER` decimal(20,0) default NULL,
  `INTERVAL_NUMBER` decimal(20,0) default NULL,
  `BY_SECOND_LIST` longtext collate latin1_general_cs,
  `BY_MINUTE_LIST` longtext collate latin1_general_cs,
  `BY_HOUR_LIST` longtext collate latin1_general_cs,
  `BY_DAY_LIST` longtext collate latin1_general_cs,
  `BY_MONTH_DAY_LIST` longtext collate latin1_general_cs,
  `BY_YEAR_DAY_LIST` longtext collate latin1_general_cs,
  `BY_WEEK_NO_LIST` longtext collate latin1_general_cs,
  `BY_MONTH_LIST` longtext collate latin1_general_cs,
  `BY_SET_POS_LIST` longtext collate latin1_general_cs,
  `WEEK_START` varchar(60) collate latin1_general_cs default NULL,
  `X_NAME` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`RECURRENCE_RULE_ID`),
  KEY `RCRRNC_RL_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `RCRRNC_RL_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of recurrence_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `runtime_data`
-- ----------------------------
DROP TABLE IF EXISTS `runtime_data`;
CREATE TABLE `runtime_data` (
  `RUNTIME_DATA_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `RUNTIME_INFO` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`RUNTIME_DATA_ID`),
  KEY `RNTM_DT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `RNTM_DT_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of runtime_data
-- ----------------------------

-- ----------------------------
-- Table structure for `security_group`
-- ----------------------------
DROP TABLE IF EXISTS `security_group`;
CREATE TABLE `security_group` (
  `GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GROUP_ID`),
  KEY `SCRT_GRP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SCRT_GRP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of security_group
-- ----------------------------
INSERT INTO `security_group` VALUES ('BIZADMIN', 'Full Business Applications permission group, has all business app admin permissions, not technical permissions.', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group` VALUES ('FLEXADMIN', 'Flexible Admin group, has all granular permissions.', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group` VALUES ('FULLADMIN', 'Full Admin group, has all general permissions.', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group` VALUES ('VIEWADMIN', 'Demo Admin group, has all view permissions.', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `security_group_permission`
-- ----------------------------
DROP TABLE IF EXISTS `security_group_permission`;
CREATE TABLE `security_group_permission` (
  `GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PERMISSION_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`GROUP_ID`,`PERMISSION_ID`),
  KEY `SEC_GRP_PERM_GRP` (`GROUP_ID`),
  KEY `SCT_GRP_PRMN_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SCT_GRP_PRMN_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `SEC_GRP_PERM_GRP` FOREIGN KEY (`GROUP_ID`) REFERENCES `security_group` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of security_group_permission
-- ----------------------------
INSERT INTO `security_group_permission` VALUES ('BIZADMIN', 'EXAMPLE_ADMIN', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('BIZADMIN', 'OFBTOOLS_VIEW', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('BIZADMIN', 'PERIOD_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ARTIFACT_INFO_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'COMMON_CREATE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'COMMON_DELETE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'COMMON_UPDATE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'COMMON_VIEW', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'DATAFILE_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENTITY_DATA_CREATE', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENTITY_DATA_DELETE', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENTITY_DATA_UPDATE', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENTITY_DATA_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENTITY_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENTITY_SYNC_ADMIN', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'ENUM_STATUS_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'EXAMPLE_CREATE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'EXAMPLE_DELETE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'EXAMPLE_UPDATE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'EXAMPLE_VIEW', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'LABEL_MANAGER_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'OFBTOOLS_VIEW', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'PERIOD_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'PORTALPAGE_ADMIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'SERVER_STATS_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'SERVICE_INVOKE_ANY', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'SERVICE_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'UTIL_CACHE_EDIT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'UTIL_CACHE_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'UTIL_DEBUG_EDIT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'UTIL_DEBUG_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'VISUALTHEME_CREATE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'VISUALTHEME_DELETE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'VISUALTHEME_UPDATE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FLEXADMIN', 'WEBTOOLS_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'ARTIFACT_INFO_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'access', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'BI_ADMIN', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'COMMON_ADMIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'create', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'DATAFILE_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'delete', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'ENTITY_DATA_ADMIN', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'ENTITY_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'ENTITY_SYNC_ADMIN', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'ENUM_STATUS_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'EXAMPLE_ADMIN', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'LABEL_MANAGER_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'OFBTOOLS_VIEW', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'PERIOD_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'PORTALPAGE_ADMIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'read', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'SERVER_STATS_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'SERVICE_INVOKE_ANY', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'SERVICE_MAINT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'TEMPEXPR_ADMIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'USERPREF_ADMIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'UTIL_CACHE_EDIT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'UTIL_CACHE_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'UTIL_DEBUG_EDIT', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'UTIL_DEBUG_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'update', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'VISUALTHEME_ADMIN', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('FULLADMIN', 'WEBTOOLS_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'ARTIFACT_INFO_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'access', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'COMMON_VIEW', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'ENTITY_DATA_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'EXAMPLE_VIEW', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'LABEL_MANAGER_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'OFBTOOLS_VIEW', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'read', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'SERVER_STATS_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'UTIL_CACHE_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'UTIL_DEBUG_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_group_permission` VALUES ('VIEWADMIN', 'WEBTOOLS_VIEW', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `security_permission`
-- ----------------------------
DROP TABLE IF EXISTS `security_permission`;
CREATE TABLE `security_permission` (
  `PERMISSION_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `DYNAMIC_ACCESS` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PERMISSION_ID`),
  KEY `SCRT_PRMSSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SCRT_PRMSSN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of security_permission
-- ----------------------------
INSERT INTO `security_permission` VALUES ('ARTIFACT_INFO_VIEW', 'View the Artifact Info pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('access', 'Base ACCESS permission', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('BI_ADMIN', 'ALL Business Intelligence operations.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('BI_VIEW', 'Business Intelligence View permission.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('COMMON_ADMIN', 'Admin operations in the Common Component.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('COMMON_CREATE', 'Create operations in the Common Component.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('COMMON_DELETE', 'Delete operations in the Common Component.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('COMMON_UPDATE', 'Update operations in the Common Component.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('COMMON_VIEW', 'View operations in the Common Component.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('create', 'Base CREATE permission', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('DATAFILE_MAINT', 'Use the Data File Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('delete', 'Base DELETE permission', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('ENTITY_DATA_ADMIN', 'ALL with the Entity Data Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENTITY_DATA_CREATE', 'Create with the Entity Data Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENTITY_DATA_DELETE', 'Delete with the Entity Data Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENTITY_DATA_UPDATE', 'Update with the Entity Data Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENTITY_DATA_VIEW', 'View with the Entity Data Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENTITY_MAINT', 'Use the Entity Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENTITY_SYNC_ADMIN', 'Use the Entity Sync Admin pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('ENUM_STATUS_MAINT', 'Use the Enum and Status Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('EXAMPLE_ADMIN', 'ALL operations in the Example Management Screens.', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_permission` VALUES ('EXAMPLE_CREATE', 'Create operations in the Example Management Screens.', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_permission` VALUES ('EXAMPLE_DELETE', 'Delete operations in the Example Management Screens.', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_permission` VALUES ('EXAMPLE_UPDATE', 'Update operations in the Example Management Screens.', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_permission` VALUES ('EXAMPLE_VIEW', 'View operations in the Example Management Screens.', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `security_permission` VALUES ('LABEL_MANAGER_VIEW', 'View the Labels Info pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('OFBTOOLS_VIEW', 'Permission to access the Stock OFBiz Manager Applications.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('PERIOD_MAINT', 'Use the Period Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('PORTALPAGE_ADMIN', 'Admin operations on Portal Pages.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('read', 'Base READ permission', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('SERVER_STATS_VIEW', 'View the Server Statistics pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('SERVICE_INVOKE_ANY', 'Permission to invoke any service remotely.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('SERVICE_MAINT', 'Use the Service Maintenance pages.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('TEMPEXPR_ADMIN', 'Temporal expression admin', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('USERPREF_ADMIN', 'User preferences admin', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('UTIL_CACHE_EDIT', 'Edit a UtilCache instance.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('UTIL_CACHE_VIEW', 'View a UtilCache instance.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('UTIL_DEBUG_EDIT', 'Edit a UtilDebug instance.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('UTIL_DEBUG_VIEW', 'View a UtilDebug instance.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `security_permission` VALUES ('update', 'Base UPDATE permission', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('VISUALTHEME_ADMIN', 'ALL operations on Visual Themes and Visual Theme Resources.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('VISUALTHEME_CREATE', 'Create Visual Themes and Visual Theme Resources.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('VISUALTHEME_DELETE', 'Delete Visual Themes and Visual Theme Resources.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('VISUALTHEME_UPDATE', 'Update Visual Themes and Visual Theme Resources.', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `security_permission` VALUES ('WEBTOOLS_VIEW', 'Permission to access the WebTools Menu.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `security_permission_auto_grant`
-- ----------------------------
DROP TABLE IF EXISTS `security_permission_auto_grant`;
CREATE TABLE `security_permission_auto_grant` (
  `PERMISSION_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `GRANT_PERMISSION` varchar(250) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`PERMISSION_ID`,`GRANT_PERMISSION`),
  KEY `SEC_PERM_AUTO_GRNT` (`PERMISSION_ID`),
  KEY `SCT_PRN_AT_GRT_TXP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SCT_PRN_AT_GRT_TXS` (`CREATED_TX_STAMP`),
  CONSTRAINT `SEC_PERM_AUTO_GRNT` FOREIGN KEY (`PERMISSION_ID`) REFERENCES `security_permission` (`PERMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of security_permission_auto_grant
-- ----------------------------

-- ----------------------------
-- Table structure for `selenium_test_suite_path`
-- ----------------------------
DROP TABLE IF EXISTS `selenium_test_suite_path`;
CREATE TABLE `selenium_test_suite_path` (
  `TEST_SUITE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TEST_SUITE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `TEST_SUITE_TYPE` varchar(20) collate latin1_general_cs default NULL,
  `TEST_SUITE_PATH` varchar(255) collate latin1_general_cs default NULL,
  `DESCRIPTION` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TEST_SUITE_ID`),
  KEY `SLM_TST_ST_PTH_TXP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SLM_TST_ST_PTH_TXS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of selenium_test_suite_path
-- ----------------------------
INSERT INTO `selenium_test_suite_path` VALUES ('EXAMPLE_TESTSUITE', 'example_testsuite', 'XML', 'framework/testtools/testdef/seleniumxml/example/example_testsuite.xml', 'test suite for example component', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `selenium_test_suite_path` VALUES ('EX_HTML_TESTSUITE', 'example_html_testsuite', 'HTML', 'framework/example/testdef/selenium/testSuite.html', 'HTML test suite for example component', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `sequence_value_item`
-- ----------------------------
DROP TABLE IF EXISTS `sequence_value_item`;
CREATE TABLE `sequence_value_item` (
  `SEQ_NAME` varchar(60) collate latin1_general_cs NOT NULL,
  `SEQ_ID` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`SEQ_NAME`),
  KEY `SQNC_VL_ITM_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SQNC_VL_ITM_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of sequence_value_item
-- ----------------------------

-- ----------------------------
-- Table structure for `server_hit`
-- ----------------------------
DROP TABLE IF EXISTS `server_hit`;
CREATE TABLE `server_hit` (
  `VISIT_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `CONTENT_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `HIT_START_DATE_TIME` datetime NOT NULL,
  `HIT_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `NUM_OF_BYTES` decimal(20,0) default NULL,
  `RUNNING_TIME_MILLIS` decimal(20,0) default NULL,
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs default NULL,
  `STATUS_ID` varchar(20) collate latin1_general_cs default NULL,
  `REQUEST_URL` varchar(255) collate latin1_general_cs default NULL,
  `REFERRER_URL` varchar(255) collate latin1_general_cs default NULL,
  `SERVER_IP_ADDRESS` varchar(20) collate latin1_general_cs default NULL,
  `SERVER_HOST_NAME` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VISIT_ID`,`CONTENT_ID`,`HIT_START_DATE_TIME`,`HIT_TYPE_ID`),
  KEY `SERVER_HIT_SHTYP` (`HIT_TYPE_ID`),
  KEY `SERVER_HIT_VISIT` (`VISIT_ID`),
  KEY `SERVER_HIT_STATUS` (`STATUS_ID`),
  KEY `SERVER_HIT_USER` (`USER_LOGIN_ID`),
  KEY `SERVER_HIT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SERVER_HIT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `SERVER_HIT_SHTYP` FOREIGN KEY (`HIT_TYPE_ID`) REFERENCES `server_hit_type` (`HIT_TYPE_ID`),
  CONSTRAINT `SERVER_HIT_STATUS` FOREIGN KEY (`STATUS_ID`) REFERENCES `status_item` (`STATUS_ID`),
  CONSTRAINT `SERVER_HIT_USER` FOREIGN KEY (`USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`),
  CONSTRAINT `SERVER_HIT_VISIT` FOREIGN KEY (`VISIT_ID`) REFERENCES `visit` (`VISIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of server_hit
-- ----------------------------

-- ----------------------------
-- Table structure for `server_hit_bin`
-- ----------------------------
DROP TABLE IF EXISTS `server_hit_bin`;
CREATE TABLE `server_hit_bin` (
  `SERVER_HIT_BIN_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `CONTENT_ID` varchar(250) collate latin1_general_cs default NULL,
  `HIT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `SERVER_IP_ADDRESS` varchar(20) collate latin1_general_cs default NULL,
  `SERVER_HOST_NAME` varchar(255) collate latin1_general_cs default NULL,
  `BIN_START_DATE_TIME` datetime default NULL,
  `BIN_END_DATE_TIME` datetime default NULL,
  `NUMBER_HITS` decimal(20,0) default NULL,
  `TOTAL_TIME_MILLIS` decimal(20,0) default NULL,
  `MIN_TIME_MILLIS` decimal(20,0) default NULL,
  `MAX_TIME_MILLIS` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`SERVER_HIT_BIN_ID`),
  KEY `SERVER_HBIN_TYPE` (`HIT_TYPE_ID`),
  KEY `SRVR_HT_BN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SRVR_HT_BN_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `SERVER_HBIN_TYPE` FOREIGN KEY (`HIT_TYPE_ID`) REFERENCES `server_hit_type` (`HIT_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of server_hit_bin
-- ----------------------------

-- ----------------------------
-- Table structure for `server_hit_type`
-- ----------------------------
DROP TABLE IF EXISTS `server_hit_type`;
CREATE TABLE `server_hit_type` (
  `HIT_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`HIT_TYPE_ID`),
  KEY `SRVR_HT_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SRVR_HT_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of server_hit_type
-- ----------------------------
INSERT INTO `server_hit_type` VALUES ('ENTITY', 'Entity', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `server_hit_type` VALUES ('EVENT', 'Event', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `server_hit_type` VALUES ('REQUEST', 'Request', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `server_hit_type` VALUES ('SERVICE', 'Service', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `server_hit_type` VALUES ('VIEW', 'View', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `service_semaphore`
-- ----------------------------
DROP TABLE IF EXISTS `service_semaphore`;
CREATE TABLE `service_semaphore` (
  `SERVICE_NAME` varchar(100) collate latin1_general_cs NOT NULL,
  `LOCK_THREAD` varchar(100) collate latin1_general_cs default NULL,
  `LOCK_TIME` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`SERVICE_NAME`),
  KEY `SRVC_SMPHR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `SRVC_SMPHR_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of service_semaphore
-- ----------------------------

-- ----------------------------
-- Table structure for `standard_language`
-- ----------------------------
DROP TABLE IF EXISTS `standard_language`;
CREATE TABLE `standard_language` (
  `STANDARD_LANGUAGE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `LANG_CODE3T` varchar(10) collate latin1_general_cs default NULL,
  `LANG_CODE3B` varchar(10) collate latin1_general_cs default NULL,
  `LANG_CODE2` varchar(10) collate latin1_general_cs default NULL,
  `LANG_NAME` varchar(60) collate latin1_general_cs default NULL,
  `LANG_FAMILY` varchar(60) collate latin1_general_cs default NULL,
  `LANG_CHARSET` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`STANDARD_LANGUAGE_ID`),
  KEY `STNDRD_LNGG_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `STNDRD_LNGG_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of standard_language
-- ----------------------------
INSERT INTO `standard_language` VALUES ('6301', 'aar', 'aar', 'aa', 'Afar', 'Hamitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6302', 'abk', 'abk', 'ab', 'Abkhazian', 'Ibero-caucasian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6303', 'ace', 'ace', null, 'Achinese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6304', 'ach', 'ach', null, 'Acoli', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6305', 'ada', 'ada', null, 'Adangme', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6306', 'afa', 'afa', null, 'Afro-Asiatic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6307', 'afh', 'afh', null, 'Afrihili', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6308', 'afr', 'afr', 'af', 'Afrikaans', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6309', 'aka', 'aka', null, 'Akan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6310', 'akk', 'akk', null, 'Akkadian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6311', 'ale', 'ale', null, 'Aleut', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6312', 'alg', 'alg', null, 'Algonquian languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6313', 'amh', 'amh', 'am', 'Amharic', 'Semitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6314', 'ang', 'ang', null, 'English, Old (ca. 450-1100)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6315', 'apa', 'apa', null, 'Apache languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6316', 'ara', 'ara', 'ar', 'Arabic', 'Semitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6317', 'arc', 'arc', null, 'Aramaic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6318', 'arn', 'arn', null, 'Araucanian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6319', 'arp', 'arp', null, 'Arapaho', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6320', 'art', 'art', null, 'Artificial (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6321', 'arw', 'arw', null, 'Arawak', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6322', 'asm', 'asm', 'as', 'Assamese', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6323', 'ath', 'ath', null, 'Athapascan languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6324', 'aus', 'aus', null, 'Australian languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6325', 'ava', 'ava', null, 'Avaric', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6326', 'ave', 'ave', 'ae', 'Avestan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6327', 'awa', 'awa', null, 'Awadhi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6328', 'aym', 'aym', 'ay', 'Aymara', 'Amerindian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6329', 'aze', 'aze', 'az', 'Azerbaijani', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6330', 'bad', 'bad', null, 'Banda', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6331', 'bai', 'bai', null, 'Bamileke languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6332', 'bak', 'bak', 'ba', 'Bashkir', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6333', 'bal', 'bal', null, 'Baluchi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6334', 'bam', 'bam', null, 'Bambara', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6335', 'ban', 'ban', null, 'Balinese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6336', 'bas', 'bas', null, 'Basa', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6337', 'bat', 'bat', null, 'Baltic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6338', 'bej', 'bej', null, 'Beja', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6339', 'bel', 'bel', 'be', 'Belarusian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6340', 'bem', 'bem', null, 'Bemba', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6341', 'ben', 'ben', 'bn', 'Bengali', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6342', 'ber', 'ber', null, 'Berber (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6343', 'bho', 'bho', null, 'Bhojpuri', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6344', 'bih', 'bih', 'bh', 'Bihari', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6345', 'bik', 'bik', null, 'Bikol', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6346', 'bin', 'bin', null, 'Bini', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6347', 'bis', 'bis', 'bi', 'Bislama', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6348', 'bla', 'bla', null, 'Siksika', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6349', 'bnt', 'bnt', null, 'Bantu (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6350', 'bod', 'tib', 'bo', 'Tibetan', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6351', 'bos', 'bos', 'bs', 'Bosnian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6352', 'bra', 'bra', null, 'Braj', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6353', 'bre', 'bre', 'br', 'Breton', 'Celtic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6354', 'btk', 'btk', null, 'Batak (Indonesia)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6355', 'bua', 'bua', null, 'Buriat', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6356', 'bug', 'bug', null, 'Buginese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6357', 'bul', 'bul', 'bg', 'Bulgarian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6358', 'cad', 'cad', null, 'Caddo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6359', 'cai', 'cai', null, 'Central American Indian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6360', 'car', 'car', null, 'Carib', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6361', 'cat', 'cat', 'ca', 'Catalan', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6362', 'cau', 'cau', null, 'Caucasian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6363', 'ceb', 'ceb', null, 'Cebuano', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6364', 'cel', 'cel', null, 'Celtic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6365', 'ces', 'cze', 'cs', 'Czech', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6366', 'cha', 'cha', 'ch', 'Chamorro', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6367', 'chb', 'chb', null, 'Chibcha', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6368', 'che', 'che', 'ce', 'Chechen', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6369', 'chg', 'chg', null, 'Chagatai', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6370', 'chk', 'chk', null, 'Chuukese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6371', 'chm', 'chm', null, 'Mari', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6372', 'chn', 'chn', null, 'Chinook jargon', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6373', 'cho', 'cho', null, 'Choctaw', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6374', 'chp', 'chp', null, 'Chipewyan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6375', 'chr', 'chr', null, 'Cherokee', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6376', 'chu', 'chu', 'cu', 'Church Slavic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6377', 'chv', 'chv', 'cv', 'Chuvash', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6378', 'chy', 'chy', null, 'Cheyenne', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6379', 'cmc', 'cmc', null, 'Chamic languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6380', 'cop', 'cop', null, 'Coptic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6381', 'cor', 'cor', 'kw', 'Cornish', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6382', 'cos', 'cos', 'co', 'Corsican', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6383', 'cpe', 'cpe', null, 'Creoles and pidgins, English based (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6384', 'cpf', 'cpf', null, 'Creoles and pidgins, French-based (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6385', 'cpp', 'cpp', null, 'Creoles and pidgins, Portuguese-based (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6386', 'cre', 'cre', null, 'Cree', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6387', 'crp', 'crp', null, 'Creoles and pidgins (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6388', 'cus', 'cus', null, 'Cushitic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6389', 'cym', 'wel', 'cy', 'Welsh', 'Celtic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6390', 'dak', 'dak', null, 'Dakota', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6391', 'dan', 'dan', 'da', 'Danish', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6392', 'day', 'day', null, 'Dayak', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6393', 'del', 'del', null, 'Delaware', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6394', 'den', 'den', null, 'Slave (Athapascan)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6395', 'deu', 'ger', 'de', 'German', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6396', 'dgr', 'dgr', null, 'Dogrib', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6397', 'din', 'din', null, 'Dinka', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6398', 'div', 'div', null, 'Divehi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6399', 'doi', 'doi', null, 'Dogri', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6400', 'dra', 'dra', null, 'Dravidian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6401', 'dua', 'dua', null, 'Duala', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6402', 'dum', 'dum', null, 'Dutch, Middle (ca. 1050-1350)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6403', 'dyu', 'dyu', null, 'Dyula', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6404', 'dzo', 'dzo', 'dz', 'Dzongkha', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6405', 'efi', 'efi', null, 'Efik', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6406', 'egy', 'egy', null, 'Egyptian (Ancient)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6407', 'eka', 'eka', null, 'Ekajuk', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6408', 'ell', 'gre', 'el', 'Greek, Modern (1453-)', 'Latin/greek', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6409', 'elx', 'elx', null, 'Elamite', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6410', 'eng', 'eng', 'en', 'English', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6411', 'enm', 'enm', null, 'English, Middle (1100-1500)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6412', 'epo', 'epo', 'eo', 'Esperanto', 'International aux.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6413', 'est', 'est', 'et', 'Estonian', 'Finno-ugric', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6414', 'eus', 'baq', 'eu', 'Basque', 'Basque', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6415', 'ewe', 'ewe', null, 'Ewe', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6416', 'ewo', 'ewo', null, 'Ewondo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6417', 'fan', 'fan', null, 'Fang', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6418', 'fao', 'fao', 'fo', 'Faroese', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6419', 'fas', 'per', 'fa', 'Persian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6420', 'fat', 'fat', null, 'Fanti', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6421', 'fij', 'fij', 'fj', 'Fijian', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6422', 'fin', 'fin', 'fi', 'Finnish', 'Finno-ugric', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6423', 'fiu', 'fiu', null, 'Finno-Ugrian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6424', 'fon', 'fon', null, 'Fon', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6425', 'fra', 'fre', 'fr', 'French', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6426', 'frm', 'frm', null, 'French, Middle (ca. 1400-1600)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6427', 'fro', 'fro', null, 'French, Old (842-ca. 1400)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6428', 'fry', 'fry', 'fy', 'Frisian', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6429', 'ful', 'ful', null, 'Fulah', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6430', 'fur', 'fur', null, 'Friulian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6431', 'gaa', 'gaa', null, 'Ga', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6432', 'gay', 'gay', null, 'Gayo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6433', 'gba', 'gba', null, 'Gbaya', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6434', 'gem', 'gem', null, 'Germanic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6435', 'gez', 'gez', null, 'Geez', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6436', 'gil', 'gil', null, 'Gilbertese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6437', 'gla', 'gla', 'gd', 'Gaelic (Scots)', 'Celtic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6438', 'gle', 'gle', 'ga', 'Irish', 'Celtic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6439', 'glg', 'glg', 'gl', 'Gallegan', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6440', 'glv', 'glv', 'gv', 'Manx', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6441', 'gmh', 'gmh', null, 'German, Middle High (ca. 1050-1500)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6442', 'goh', 'goh', null, 'German, Old High (ca. 750-1050)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6443', 'gon', 'gon', null, 'Gondi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6444', 'gor', 'gor', null, 'Gorontalo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6445', 'got', 'got', null, 'Gothic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6446', 'grb', 'grb', null, 'Grebo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6447', 'grc', 'grc', null, 'Greek, Ancient (to 1453)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6448', 'grn', 'grn', 'gn', 'Guarani', 'Amerindian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6449', 'guj', 'guj', 'gu', 'Gujarati', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6450', 'gwi', 'gwi', null, 'Gwich´in', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6451', 'hai', 'hai', null, 'Haida', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6452', 'hau', 'hau', 'ha', 'Hausa', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6453', 'haw', 'haw', null, 'Hawaiian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6454', 'heb', 'heb', 'he', 'Hebrew', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6455', 'her', 'her', 'hz', 'Herero', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6456', 'hil', 'hil', null, 'Hiligaynon', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6457', 'him', 'him', null, 'Himachali', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6458', 'hin', 'hin', 'hi', 'Hindi', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6459', 'hit', 'hit', null, 'Hittite', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6460', 'hmn', 'hmn', null, 'Hmong', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6461', 'hmo', 'hmo', 'ho', 'Hiri Motu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6462', 'hrv', 'scr', 'hr', 'Croatian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6463', 'hun', 'hun', 'hu', 'Hungarian', 'Finno-ugric', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6464', 'hup', 'hup', null, 'Hupa', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6465', 'hye', 'arm', 'hy', 'Armenian', 'Indo-european (other)', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6466', 'iba', 'iba', null, 'Iban', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6467', 'ibo', 'ibo', null, 'Igbo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6468', 'ijo', 'ijo', null, 'Ijo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6469', 'iku', 'iku', 'iu', 'Inuktitut', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6470', 'ile', 'ile', 'ie', 'Interlingue', 'International aux.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6471', 'ilo', 'ilo', null, 'Iloko', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6472', 'ina', 'ina', 'ia', 'Interlingua', 'International aux.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6473', 'inc', 'inc', null, 'Indic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6474', 'ind', 'ind', 'id', 'Indonesian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6475', 'ine', 'ine', null, 'Indo-European (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6476', 'ipk', 'ipk', 'ik', 'Inupiaq', 'Eskimo', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6477', 'ira', 'ira', null, 'Iranian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6478', 'iro', 'iro', null, 'Iroquoian languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6479', 'isl', 'ice', 'is', 'Icelandic', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6480', 'ita', 'ita', 'it', 'Italian', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6481', 'jaw', 'jav', 'jw', 'Javanese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6482', 'jpn', 'jpn', 'ja', 'Japanese', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6483', 'jpr', 'jpr', null, 'Judeo-Persian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6484', 'kaa', 'kaa', null, 'Kara-Kalpak', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6485', 'kab', 'kab', null, 'Kabyle', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6486', 'kac', 'kac', null, 'Kachin', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6487', 'kal', 'kal', 'kl', 'Kalaallisut', 'Eskimo', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6488', 'kam', 'kam', null, 'Kamba', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6489', 'kan', 'kan', 'kn', 'Kannada', 'Dravidian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6490', 'kar', 'kar', null, 'Karen', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6491', 'kas', 'kas', 'ks', 'Kashmiri', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6492', 'kat', 'geo', 'ka', 'Georgian', 'Ibero-caucasian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6493', 'kau', 'kau', null, 'Kanuri', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6494', 'kaw', 'kaw', null, 'Kawi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6495', 'kaz', 'kaz', 'kk', 'Kazakh', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6496', 'kha', 'kha', null, 'Khasi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6497', 'khi', 'khi', null, 'Khoisan (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6498', 'khm', 'khm', 'km', 'Khmer', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6499', 'kho', 'kho', null, 'Khotanese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6500', 'kik', 'kik', 'ki', 'Kikuyu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6501', 'kin', 'kin', 'rw', 'Kinyarwanda', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6502', 'kir', 'kir', 'ky', 'Kirghiz', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6503', 'kmb', 'kmb', null, 'Kimbundu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6504', 'kok', 'kok', null, 'Konkani', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6505', 'kom', 'kom', 'kv', 'Komi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6506', 'kon', 'kon', null, 'Kongo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6507', 'kor', 'kor', 'ko', 'Korean', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6508', 'kos', 'kos', null, 'Kosraean', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6509', 'kpe', 'kpe', null, 'Kpelle', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6510', 'kro', 'kro', null, 'Kru', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6511', 'kru', 'kru', null, 'Kurukh', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6512', 'kum', 'kum', null, 'Kumyk', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6513', 'kur', 'kur', 'ku', 'Kurdish', 'Iranian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6514', 'kut', 'kut', null, 'Kutenai', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6515', 'lad', 'lad', null, 'Ladino', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6516', 'lah', 'lah', null, 'Lahnda', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6517', 'lam', 'lam', null, 'Lamba', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6518', 'lao', 'lao', 'lo', 'Lao', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6519', 'lat', 'lat', 'la', 'Latin', 'Latin/greek', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6520', 'lav', 'lav', 'lv', 'Latvian', 'Baltic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6521', 'lez', 'lez', null, 'Lezghian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6522', 'lin', 'lin', 'ln', 'Lingala', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6523', 'lit', 'lit', 'lt', 'Lithuanian', 'Baltic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6524', 'lol', 'lol', null, 'Mongo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6525', 'loz', 'loz', null, 'Lozi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6526', 'ltz', 'ltz', 'lb', 'Letzeburgesch', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6527', 'lua', 'lua', null, 'Luba-Lulua', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6528', 'lub', 'lub', null, 'Luba-Katanga', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6529', 'lug', 'lug', null, 'Ganda', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6530', 'lui', 'lui', null, 'Luiseno', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6531', 'lun', 'lun', null, 'Lunda', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6532', 'luo', 'luo', null, 'Luo (Kenya and Tanzania)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6533', 'lus', 'lus', null, 'lushai', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6534', 'mad', 'mad', null, 'Madurese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6535', 'mag', 'mag', null, 'Magahi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6536', 'mah', 'mah', 'mh', 'Marshall', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6537', 'mai', 'mai', null, 'Maithili', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6538', 'mak', 'mak', null, 'Makasar', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6539', 'mal', 'mal', 'ml', 'Malayalam', 'Dravidian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6540', 'man', 'man', null, 'Mandingo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6541', 'map', 'map', null, 'Austronesian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6542', 'mar', 'mar', 'mr', 'Marathi', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6543', 'mas', 'mas', null, 'Masai', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6544', 'mdr', 'mdr', null, 'Mandar', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6545', 'men', 'men', null, 'Mende', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6546', 'mga', 'mga', null, 'Irish, Middle (900-1200)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6547', 'mic', 'mic', null, 'Micmac', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6548', 'min', 'min', null, 'Minangkabau', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6549', 'mis', 'mis', null, 'Miscellaneous languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6550', 'mkd', 'mac', 'mk', 'Macedonian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6551', 'mkh', 'mkh', null, 'Mon-Khmer (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6552', 'mlg', 'mlg', 'mg', 'Malagasy', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6553', 'mlt', 'mlt', 'mt', 'Maltese', 'Semitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6554', 'mnc', 'mnc', null, 'Manchu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6555', 'mni', 'mni', null, 'Manipuri', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6556', 'mno', 'mno', null, 'Manobo languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6557', 'moh', 'moh', null, 'Mohawk', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6558', 'mol', 'mol', 'mo', 'Moldavian', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6559', 'mon', 'mon', 'mn', 'Mongolian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6560', 'mos', 'mos', null, 'Mossi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6561', 'mri', 'mao', 'mi', 'Maori', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6562', 'msa', 'may', 'ms', 'Malay', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6563', 'mul', 'mul', null, 'Multiple languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6564', 'mun', 'mun', null, 'Munda languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6565', 'mus', 'mus', null, 'Creek', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6566', 'mwr', 'mwr', null, 'Marwari', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6567', 'mya', 'bur', 'my', 'Burmese', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6568', 'myn', 'myn', null, 'Mayan languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6569', 'nah', 'nah', null, 'Nahuatl', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6570', 'nai', 'nai', null, 'North American Indian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6571', 'nau', 'nau', 'na', 'Nauru', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6572', 'nav', 'nav', 'nv', 'Navajo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6573', 'nbl', 'nbl', 'nr', 'Ndebele, South', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6574', 'nde', 'nde', 'nd', 'Ndebele, North', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6575', 'ndo', 'ndo', 'ng', 'Ndonga', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6576', 'nds', 'nds', null, 'Low German; Low Saxon; German, Low; Saxon, Low', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6577', 'nep', 'nep', 'ne', 'Nepali', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6578', 'new', 'new', null, 'Newari', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6579', 'nia', 'nia', null, 'Nias', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6580', 'nic', 'nic', null, 'Niger-Kordofanian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6581', 'niu', 'niu', null, 'Niuean', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6582', 'nld', 'dut', 'nl', 'Dutch', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6583', 'nno', 'nno', 'nn', 'Norwegian Nynorsk', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6584', 'nob', 'nob', 'nb', 'Norwegian Bokmål', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6585', 'non', 'non', null, 'Norse, Old', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6586', 'nor', 'nor', 'no', 'Norwegian', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6587', 'nso', 'nso', null, 'Sotho, Northern', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6588', 'nub', 'nub', null, 'Nubian languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6589', 'nya', 'nya', 'ny', 'Chichewa; Nyanja', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6590', 'nym', 'nym', null, 'Nyamwezi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6591', 'nyn', 'nyn', null, 'Nyankole', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6592', 'nyo', 'nyo', null, 'Nyoro', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6593', 'nzi', 'nzi', null, 'Nzima', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6594', 'oci', 'oci', 'oc', 'Occitan (post 1500); Provençal', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6595', 'oji', 'oji', null, 'Ojibwa', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6596', 'ori', 'ori', 'or', 'Oriya', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6597', 'orm', 'orm', 'om', 'Oromo', 'Hamitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6598', 'osa', 'osa', null, 'Osage', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6599', 'oss', 'oss', 'os', 'Ossetian; Ossetic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6600', 'ota', 'ota', null, 'Turkish, Ottoman (1500-1928)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6601', 'oto', 'oto', null, 'Otomian languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6602', 'paa', 'paa', null, 'Papuan (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6603', 'pag', 'pag', null, 'Pangasinan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6604', 'pal', 'pal', null, 'Pahlavi', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6605', 'pam', 'pam', null, 'Pampanga', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6606', 'pan', 'pan', 'pa', 'Panjabi', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6607', 'pap', 'pap', null, 'Papiamento', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6608', 'pau', 'pau', null, 'Palauan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6609', 'peo', 'peo', null, 'Persian, Old (ca. 600-400 b.c.)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6610', 'phi', 'phi', null, 'Philippine (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6611', 'pli', 'pli', 'pi', 'Pali', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6612', 'pol', 'pol', 'pl', 'Polish', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6613', 'pon', 'pon', null, 'Pohnpeian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6614', 'por', 'por', 'pt', 'Portuguese', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6615', 'pra', 'pra', null, 'Prakrit languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6616', 'pro', 'pro', null, 'Provençal, Old (to 1500)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6617', 'pus', 'pus', 'ps', 'Pushto', 'Iranian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6618', 'que', 'que', 'qu', 'Quechua', 'Amerindian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6619', 'raj', 'raj', null, 'Rajasthani', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6620', 'rap', 'rap', null, 'Rapanui', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6621', 'rar', 'rar', null, 'Rarotongan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6622', 'roa', 'roa', null, 'Romance (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6623', 'rom', 'rom', null, 'Romany', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6624', 'ron', 'rum', 'ro', 'Romanian', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6625', 'run', 'run', 'rn', 'Rundi', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6626', 'rus', 'rus', 'ru', 'Russian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6627', 'sad', 'sad', null, 'Sandawe', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6628', 'sag', 'sag', 'sg', 'Sango', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6629', 'sah', 'sah', null, 'Yakut', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6630', 'sai', 'sai', null, 'South American Indian (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6631', 'sal', 'sal', null, 'Salishan languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6632', 'sam', 'sam', null, 'Samaritan Aramaic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6633', 'san', 'san', 'sa', 'Sanskrit', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6634', 'sas', 'sas', null, 'Sasak', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6635', 'sat', 'sat', null, 'Santali', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6636', 'sco', 'sco', null, 'Scots', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6637', 'sel', 'sel', null, 'Selkup', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6638', 'sem', 'sem', null, 'Semitic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6639', 'sga', 'sga', null, 'Irish, Old (to 900)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6640', 'sgn', 'sgn', null, 'Sign Languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6641', 'shn', 'shn', null, 'Shan', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6642', 'sid', 'sid', null, 'Sidamo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6643', 'sin', 'sin', 'si', 'Sinhalese', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6644', 'sio', 'sio', null, 'Siouan languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6645', 'sit', 'sit', null, 'Sino-Tibetan (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6646', 'sla', 'sla', null, 'Slavic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6647', 'slk', 'slo', 'sk', 'Slovak', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6648', 'slv', 'slv', 'sl', 'Slovenian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6649', 'sme', 'sme', 'se', 'Northern Sami', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6650', 'smi', 'smi', null, 'Sami languages (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6651', 'smo', 'smo', 'sm', 'Samoan', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6652', 'sna', 'sna', 'sn', 'Shona', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6653', 'snd', 'snd', 'sd', 'Sindhi', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6654', 'snk', 'snk', null, 'Soninke', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6655', 'sog', 'sog', null, 'Sogdian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6656', 'som', 'som', 'so', 'Somali', 'Hamitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6657', 'son', 'son', null, 'Songhai', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6658', 'sot', 'sot', 'st', 'Sotho, Southern', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6659', 'spa', 'spa', 'es', 'Spanish', 'Romance', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6660', 'sqi', 'alb', 'sq', 'Albanian', 'Indo-european (other)', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6661', 'srd', 'srd', 'sc', 'Sardinian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6662', 'srp', 'scc', 'sr', 'Serbian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6663', 'srr', 'srr', null, 'Serer', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6664', 'ssa', 'ssa', null, 'Nilo-Saharan (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6665', 'ssw', 'ssw', 'ss', 'Swati', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6666', 'suk', 'suk', null, 'Sukuma', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6667', 'sun', 'sun', 'su', 'Sundanese', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6668', 'sus', 'sus', null, 'Susu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6669', 'sux', 'sux', null, 'Sumerian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6670', 'swa', 'swa', 'sw', 'Swahili', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6671', 'swe', 'swe', 'sv', 'Swedish', 'Germanic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6672', 'syr', 'syr', null, 'Syriac', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6673', 'tah', 'tah', 'ty', 'Tahitian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6674', 'tai', 'tai', null, 'Tai (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6675', 'tam', 'tam', 'ta', 'Tamil', 'Dravidian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6676', 'tat', 'tat', 'tt', 'Tatar', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6677', 'tel', 'tel', 'te', 'Telugu', 'Dravidian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6678', 'tem', 'tem', null, 'Timne', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6679', 'ter', 'ter', null, 'Tereno', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6680', 'tet', 'tet', null, 'Tetum', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6681', 'tgk', 'tgk', 'tg', 'Tajik', 'Iranian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6682', 'tgl', 'tgl', 'tl', 'Tagalog', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6683', 'tha', 'tha', 'th', 'Thai', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6684', 'tig', 'tig', null, 'Tigre', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6685', 'tir', 'tir', 'ti', 'Tigrinya', 'Semitic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6686', 'tiv', 'tiv', null, 'Tiv', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6687', 'tkl', 'tkl', null, 'Tokelau', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6688', 'tli', 'tli', null, 'Tlingit', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6689', 'tmh', 'tmh', null, 'Tamashek', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6690', 'tog', 'tog', null, 'Tonga (Nyasa)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6691', 'ton', 'ton', 'to', 'Tonga (Tonga Islands)', 'Oceanic/indonesian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6692', 'tpi', 'tpi', null, 'Tok Pisin', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6693', 'tsi', 'tsi', null, 'Tsimshian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6694', 'tsn', 'tsn', 'tn', 'Tswana', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6695', 'tso', 'tso', 'ts', 'Tsonga', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6696', 'tuk', 'tuk', 'tk', 'Turkmen', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6697', 'tum', 'tum', null, 'Tumbuka', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6698', 'tur', 'tur', 'tr', 'Turkish', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6699', 'tut', 'tut', null, 'Altaic (Other)', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6700', 'tvl', 'tvl', null, 'Tuvalu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6701', 'twi', 'twi', 'tw', 'Twi', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6702', 'tyv', 'tyv', null, 'Tuvinian', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6703', 'uga', 'uga', null, 'Ugaritic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6704', 'uig', 'uig', 'ug', 'Uighur', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6705', 'ukr', 'ukr', 'uk', 'Ukrainian', 'Slavic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6706', 'umb', 'umb', null, 'Umbundu', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6707', 'und', 'und', null, 'Undetermined', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6708', 'urd', 'urd', 'ur', 'Urdu', 'Indian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6709', 'uzb', 'uzb', 'uz', 'Uzbek', 'Turkic/altaic', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6710', 'vai', 'vai', null, 'Vai', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6711', 'ven', 'ven', null, 'Venda', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6712', 'vie', 'vie', 'vi', 'Vietnamese', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6713', 'vol', 'vol', 'vo', 'Volapük', 'International aux.', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6714', 'vot', 'vot', null, 'Votic', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6715', 'wak', 'wak', null, 'Wakashan languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6716', 'wal', 'wal', null, 'Walamo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6717', 'war', 'war', null, 'Waray', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6718', 'was', 'was', null, 'Washo', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6719', 'wen', 'wen', null, 'Sorbian languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6720', 'wol', 'wol', 'wo', 'Wolof', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6721', 'xho', 'xho', 'xh', 'Xhosa', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6722', 'yao', 'yao', null, 'Yao', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6723', 'yap', 'yap', null, 'Yapese', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6724', 'yid', 'yid', 'yi', 'Yiddish', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6725', 'yor', 'yor', 'yo', 'Yoruba', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6726', 'ypk', 'ypk', null, 'Yupik languages', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6727', 'zap', 'zap', null, 'Zapotec', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6728', 'zen', 'zen', null, 'Zenaga', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6729', 'zha', 'zha', 'za', 'Zhuang', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6730', 'zho', 'chi', 'zh', 'Chinese', 'Asian', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6731', 'znd', 'znd', null, 'Zande', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6732', 'zul', 'zul', 'zu', 'Zulu', 'Negro-african', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `standard_language` VALUES ('6733', 'zun', 'zun', null, 'Zuni', null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `standard_time_period`
-- ----------------------------
DROP TABLE IF EXISTS `standard_time_period`;
CREATE TABLE `standard_time_period` (
  `STANDARD_TIME_PERIOD_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PERIOD_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `FROM_DATE` datetime default NULL,
  `THRU_DATE` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`STANDARD_TIME_PERIOD_ID`),
  KEY `STD_TM_PER_TYPE` (`PERIOD_TYPE_ID`),
  KEY `STNDD_TM_PRD_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `STNDD_TM_PRD_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `STD_TM_PER_TYPE` FOREIGN KEY (`PERIOD_TYPE_ID`) REFERENCES `period_type` (`PERIOD_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of standard_time_period
-- ----------------------------

-- ----------------------------
-- Table structure for `status_item`
-- ----------------------------
DROP TABLE IF EXISTS `status_item`;
CREATE TABLE `status_item` (
  `STATUS_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `STATUS_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `STATUS_CODE` varchar(60) collate latin1_general_cs default NULL,
  `SEQUENCE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`STATUS_ID`),
  KEY `STATUS_TO_TYPE` (`STATUS_TYPE_ID`),
  KEY `STATUS_ITEM_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `STATUS_ITEM_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `STATUS_TO_TYPE` FOREIGN KEY (`STATUS_TYPE_ID`) REFERENCES `status_type` (`STATUS_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of status_item
-- ----------------------------
INSERT INTO `status_item` VALUES ('ESR_COMPLETE', 'ENTSYNC_RUN', 'COMPLETE', '04', 'Complete', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('ESR_DATA_ERROR', 'ENTSYNC_RUN', 'DATA_ERROR', '99', 'Data Error', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('ESR_NOT_STARTED', 'ENTSYNC_RUN', 'NOT_STARTED', '01', 'Not Started', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('ESR_OTHER_ERROR', 'ENTSYNC_RUN', 'OTHER_ERROR', '97', 'Other Error', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('ESR_PENDING', 'ENTSYNC_RUN', 'PENDING', '03', 'Offline Pending', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('ESR_RUNNING', 'ENTSYNC_RUN', 'RUNNING', '02', 'Running', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('ESR_SERVICE_ERROR', 'ENTSYNC_RUN', 'SERVICE_ERROR', '98', 'Service Call Error', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('EXST_APPROVED', 'EXAMPLE_STATUS', 'APPROVED', '03', 'Approved', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('EXST_CANCELLED', 'EXAMPLE_STATUS', 'CANCELLED', '99', 'Cancelled', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('EXST_COMPLETE', 'EXAMPLE_STATUS', 'COMPLETE', '06', 'Complete', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('EXST_DEFINED', 'EXAMPLE_STATUS', 'DEFINED', '02', 'Defined', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('EXST_IMPLEMENTED', 'EXAMPLE_STATUS', 'IMPLEMENTED', '04', 'Implemented', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('EXST_IN_DESIGN', 'EXAMPLE_STATUS', 'IN_DESIGN', '01', 'In Design', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('EXST_TESTED', 'EXAMPLE_STATUS', 'TESTED', '05', 'Tested', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_item` VALUES ('NOT_SYNCHRONIZED', 'SYNCHRONIZE_STATUS', 'NOT_SYNCHRONIZED', '01', 'Not-Synchronized', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `status_item` VALUES ('SERVICE_CANCELLED', 'SERVICE_STATUS', 'CANCELLED', '99', 'Cancelled', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SERVICE_CRASHED', 'SERVICE_STATUS', 'CRASHED', '40', 'Crashed', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SERVICE_FAILED', 'SERVICE_STATUS', 'FAILED', '20', 'Failed', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SERVICE_FINISHED', 'SERVICE_STATUS', 'FINISHED', '10', 'Finished', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SERVICE_PENDING', 'SERVICE_STATUS', 'PENDING', '01', 'Pending', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SERVICE_QUEUED', 'SERVICE_STATUS', 'QUEUED', '02', 'Queued', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SERVICE_RUNNING', 'SERVICE_STATUS', 'RUNNING', '05', 'Running', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_item` VALUES ('SYNCHRONIZED', 'SYNCHRONIZE_STATUS', 'SYNCHRONIZED', '02', 'Synchronized', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `status_item` VALUES ('_NA_', '_NA_', '_NA_', '0', 'Not Applicable', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `status_type`
-- ----------------------------
DROP TABLE IF EXISTS `status_type`;
CREATE TABLE `status_type` (
  `STATUS_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `HAS_TABLE` char(1) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`STATUS_TYPE_ID`),
  KEY `STATUS_TYPE_PARENT` (`PARENT_TYPE_ID`),
  KEY `STATUS_TYPE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `STATUS_TYPE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `STATUS_TYPE_PARENT` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `status_type` (`STATUS_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of status_type
-- ----------------------------
INSERT INTO `status_type` VALUES ('ENTSYNC_RUN', null, 'N', 'Entity Sync Run', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_type` VALUES ('EXAMPLE_STATUS', null, 'N', 'Example', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_type` VALUES ('SERVICE_STATUS', null, 'N', 'Scheduled Service', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `status_type` VALUES ('SYNCHRONIZE_STATUS', null, 'N', 'Synchronize', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `status_type` VALUES ('_NA_', null, null, 'Not Applicable', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `status_valid_change`
-- ----------------------------
DROP TABLE IF EXISTS `status_valid_change`;
CREATE TABLE `status_valid_change` (
  `STATUS_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `STATUS_ID_TO` varchar(20) collate latin1_general_cs NOT NULL,
  `CONDITION_EXPRESSION` varchar(255) collate latin1_general_cs default NULL,
  `TRANSITION_NAME` varchar(100) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`STATUS_ID`,`STATUS_ID_TO`),
  KEY `STATUS_CHG_MAIN` (`STATUS_ID`),
  KEY `STATUS_CHG_TO` (`STATUS_ID_TO`),
  KEY `STS_VLD_CHG_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `STS_VLD_CHG_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `STATUS_CHG_MAIN` FOREIGN KEY (`STATUS_ID`) REFERENCES `status_item` (`STATUS_ID`),
  CONSTRAINT `STATUS_CHG_TO` FOREIGN KEY (`STATUS_ID_TO`) REFERENCES `status_item` (`STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of status_valid_change
-- ----------------------------
INSERT INTO `status_valid_change` VALUES ('EXST_APPROVED', 'EXST_CANCELLED', null, 'Cancel Example', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_APPROVED', 'EXST_IMPLEMENTED', null, 'Implementation Complete', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_DEFINED', 'EXST_APPROVED', null, 'Approve', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_DEFINED', 'EXST_CANCELLED', null, 'Cancel Example', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_IMPLEMENTED', 'EXST_CANCELLED', null, 'Cancel Example', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_IMPLEMENTED', 'EXST_TESTED', null, 'Testing Complete', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_IN_DESIGN', 'EXST_CANCELLED', null, 'Cancel Example', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_IN_DESIGN', 'EXST_DEFINED', null, 'Definition Complete', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_TESTED', 'EXST_CANCELLED', null, 'Cancel Example', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `status_valid_change` VALUES ('EXST_TESTED', 'EXST_COMPLETE', null, 'Example Completed', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `TAG_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TAG_NAME` varchar(100) collate latin1_general_cs default NULL,
  `PARENT_TAG_ID` varchar(20) collate latin1_general_cs default NULL,
  `CATEGORY` varchar(100) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TAG_ID`),
  KEY `TAG_PAR` (`PARENT_TAG_ID`),
  KEY `TAG_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TAG_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `TAG_PAR` FOREIGN KEY (`PARENT_TAG_ID`) REFERENCES `tag` (`TAG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for `tarpitted_login_view`
-- ----------------------------
DROP TABLE IF EXISTS `tarpitted_login_view`;
CREATE TABLE `tarpitted_login_view` (
  `VIEW_NAME_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `USER_LOGIN_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TARPIT_RELEASE_DATE_TIME` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VIEW_NAME_ID`,`USER_LOGIN_ID`),
  KEY `TRPTD_LGN_VW_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TRPTD_LGN_VW_TXCRS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of tarpitted_login_view
-- ----------------------------

-- ----------------------------
-- Table structure for `temporal_expression`
-- ----------------------------
DROP TABLE IF EXISTS `temporal_expression`;
CREATE TABLE `temporal_expression` (
  `TEMP_EXPR_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TEMP_EXPR_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `DATE1` datetime default NULL,
  `DATE2` datetime default NULL,
  `INTEGER1` decimal(20,0) default NULL,
  `INTEGER2` decimal(20,0) default NULL,
  `STRING1` varchar(20) collate latin1_general_cs default NULL,
  `STRING2` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TEMP_EXPR_ID`),
  KEY `TMPL_EXPRSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TMPL_EXPRSN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of temporal_expression
-- ----------------------------
INSERT INTO `temporal_expression` VALUES ('1ST_AND_15TH_MONTH', 'UNION', 'First and Fifteenth of the month', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('1ST_MONDAY_IN_MONTH', 'DAY_IN_MONTH', 'First Monday in Month', null, null, '2', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('2ND_MONDAY_IN_MONTH', 'DAY_IN_MONTH', 'Second Monday in Month', null, null, '2', '2', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('3RD_MONDAY_IN_MONTH', 'DAY_IN_MONTH', 'Third Monday in Month', null, null, '2', '3', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('4TH_MONDAY_IN_MONTH', 'DAY_IN_MONTH', 'Fourth Monday in Month', null, null, '2', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('4TH_THURS_IN_MONTH', 'DAY_IN_MONTH', 'Fourth Thursday in Month', null, null, '5', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('5PM_BIMONDAY', 'FREQUENCY', 'Every Other Monday at 5pm', '2000-01-03 17:00:00', null, '5', '14', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('CHRISTMAS_DAY', 'INTERSECTION', 'Christmas Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('COLUMBUS_DAY', 'INTERSECTION', 'Columbus Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAILY_GRIND', 'INTERSECTION', 'Monday to Friday at 8am without US Federal Holidays', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_01', 'DAY_OF_MONTH_RANGE', 'Day 1 of Month', null, null, '1', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_02', 'DAY_OF_MONTH_RANGE', 'Day 2 of Month', null, null, '2', '2', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_03', 'DAY_OF_MONTH_RANGE', 'Day 3 of Month', null, null, '3', '3', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_04', 'DAY_OF_MONTH_RANGE', 'Day 4 of Month', null, null, '4', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_05', 'DAY_OF_MONTH_RANGE', 'Day 5 of Month', null, null, '5', '5', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_06', 'DAY_OF_MONTH_RANGE', 'Day 6 of Month', null, null, '6', '6', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_07', 'DAY_OF_MONTH_RANGE', 'Day 7 of Month', null, null, '7', '7', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_08', 'DAY_OF_MONTH_RANGE', 'Day 8 of Month', null, null, '8', '8', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_09', 'DAY_OF_MONTH_RANGE', 'Day 9 of Month', null, null, '9', '9', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_10', 'DAY_OF_MONTH_RANGE', 'Day 10 of Month', null, null, '10', '10', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_11', 'DAY_OF_MONTH_RANGE', 'Day 11 of Month', null, null, '11', '11', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_12', 'DAY_OF_MONTH_RANGE', 'Day 12 of Month', null, null, '12', '12', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_13', 'DAY_OF_MONTH_RANGE', 'Day 13 of Month', null, null, '13', '13', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_14', 'DAY_OF_MONTH_RANGE', 'Day 14 of Month', null, null, '14', '14', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_15', 'DAY_OF_MONTH_RANGE', 'Day 15 of Month', null, null, '15', '15', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_16', 'DAY_OF_MONTH_RANGE', 'Day 16 of Month', null, null, '16', '16', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_17', 'DAY_OF_MONTH_RANGE', 'Day 17 of Month', null, null, '17', '17', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_18', 'DAY_OF_MONTH_RANGE', 'Day 18 of Month', null, null, '18', '18', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_19', 'DAY_OF_MONTH_RANGE', 'Day 19 of Month', null, null, '19', '19', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_20', 'DAY_OF_MONTH_RANGE', 'Day 20 of Month', null, null, '20', '20', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_21', 'DAY_OF_MONTH_RANGE', 'Day 21 of Month', null, null, '21', '21', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_22', 'DAY_OF_MONTH_RANGE', 'Day 22 of Month', null, null, '22', '22', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_23', 'DAY_OF_MONTH_RANGE', 'Day 23 of Month', null, null, '23', '23', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_24', 'DAY_OF_MONTH_RANGE', 'Day 24 of Month', null, null, '24', '24', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_25', 'DAY_OF_MONTH_RANGE', 'Day 25 of Month', null, null, '25', '25', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_26', 'DAY_OF_MONTH_RANGE', 'Day 26 of Month', null, null, '26', '26', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_27', 'DAY_OF_MONTH_RANGE', 'Day 27 of Month', null, null, '27', '27', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_28', 'DAY_OF_MONTH_RANGE', 'Day 28 of Month', null, null, '28', '28', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_29', 'DAY_OF_MONTH_RANGE', 'Day 29 of Month', null, null, '29', '29', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_30', 'DAY_OF_MONTH_RANGE', 'Day 30 of Month', null, null, '30', '30', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFMONTH_31', 'DAY_OF_MONTH_RANGE', 'Day 31 of Month', null, null, '31', '31', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_01', 'DAY_OF_WEEK_RANGE', 'Sunday', null, null, '1', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_02', 'DAY_OF_WEEK_RANGE', 'Monday', null, null, '2', '2', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_03', 'DAY_OF_WEEK_RANGE', 'Tuesday', null, null, '3', '3', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_04', 'DAY_OF_WEEK_RANGE', 'Wednesday', null, null, '4', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_05', 'DAY_OF_WEEK_RANGE', 'Thursday', null, null, '5', '5', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_06', 'DAY_OF_WEEK_RANGE', 'Friday', null, null, '6', '6', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('DAYOFWEEK_07', 'DAY_OF_WEEK_RANGE', 'Saturday', null, null, '7', '7', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('GOVT_WORK_SCHED', 'DIFFERENCE', 'Monday to Friday without US Federal Holidays', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_00', 'HOUR_RANGE', 'Hour 0', null, null, '0', '0', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_01', 'HOUR_RANGE', 'Hour 1', null, null, '1', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_02', 'HOUR_RANGE', 'Hour 2', null, null, '2', '2', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_03', 'HOUR_RANGE', 'Hour 3', null, null, '3', '3', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_04', 'HOUR_RANGE', 'Hour 4', null, null, '4', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_05', 'HOUR_RANGE', 'Hour 5', null, null, '5', '5', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_06', 'HOUR_RANGE', 'Hour 6', null, null, '6', '6', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_07', 'HOUR_RANGE', 'Hour 7', null, null, '7', '7', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_08', 'HOUR_RANGE', 'Hour 8', null, null, '8', '8', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_09', 'HOUR_RANGE', 'Hour 9', null, null, '9', '9', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_10', 'HOUR_RANGE', 'Hour 10', null, null, '10', '10', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_11', 'HOUR_RANGE', 'Hour 11', null, null, '11', '11', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_12', 'HOUR_RANGE', 'Hour 12', null, null, '12', '12', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_13', 'HOUR_RANGE', 'Hour 13', null, null, '13', '13', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_14', 'HOUR_RANGE', 'Hour 14', null, null, '14', '14', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_15', 'HOUR_RANGE', 'Hour 15', null, null, '15', '15', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_16', 'HOUR_RANGE', 'Hour 16', null, null, '16', '16', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_17', 'HOUR_RANGE', 'Hour 17', null, null, '17', '17', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_18', 'HOUR_RANGE', 'Hour 18', null, null, '18', '18', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_19', 'HOUR_RANGE', 'Hour 19', null, null, '19', '19', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_20', 'HOUR_RANGE', 'Hour 20', null, null, '20', '20', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_21', 'HOUR_RANGE', 'Hour 21', null, null, '21', '21', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_22', 'HOUR_RANGE', 'Hour 22', null, null, '22', '22', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('HOUR_23', 'HOUR_RANGE', 'Hour 23', null, null, '23', '23', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('INDEPENDENCE_DAY', 'INTERSECTION', 'Independence Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('JANUARY_FIRST', 'INTERSECTION', 'January First', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('LABOR_DAY', 'INTERSECTION', 'Labor Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('LAST_MONDAY_IN_MONTH', 'DAY_IN_MONTH', 'Last Monday in Month', null, null, '2', '-1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MEMORIAL_DAY', 'INTERSECTION', 'Memorial Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MIDNIGHT_DAILY', 'FREQUENCY', 'Daily Midnight', '2000-01-01 00:00:00', null, '5', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_00', 'MINUTE_RANGE', 'Minute 0', null, null, '0', '0', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_01', 'MINUTE_RANGE', 'Minute 1', null, null, '1', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_02', 'MINUTE_RANGE', 'Minute 2', null, null, '2', '2', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_03', 'MINUTE_RANGE', 'Minute 3', null, null, '3', '3', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_04', 'MINUTE_RANGE', 'Minute 4', null, null, '4', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_05', 'MINUTE_RANGE', 'Minute 5', null, null, '5', '5', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_06', 'MINUTE_RANGE', 'Minute 6', null, null, '6', '6', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_07', 'MINUTE_RANGE', 'Minute 7', null, null, '7', '7', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_08', 'MINUTE_RANGE', 'Minute 8', null, null, '8', '8', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_09', 'MINUTE_RANGE', 'Minute 9', null, null, '9', '9', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_10', 'MINUTE_RANGE', 'Minute 10', null, null, '10', '10', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_11', 'MINUTE_RANGE', 'Minute 11', null, null, '11', '11', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_12', 'MINUTE_RANGE', 'Minute 12', null, null, '12', '12', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_13', 'MINUTE_RANGE', 'Minute 13', null, null, '13', '13', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_14', 'MINUTE_RANGE', 'Minute 14', null, null, '14', '14', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_15', 'MINUTE_RANGE', 'Minute 15', null, null, '15', '15', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_16', 'MINUTE_RANGE', 'Minute 16', null, null, '16', '16', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_17', 'MINUTE_RANGE', 'Minute 17', null, null, '17', '17', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_18', 'MINUTE_RANGE', 'Minute 18', null, null, '18', '18', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_19', 'MINUTE_RANGE', 'Minute 19', null, null, '19', '19', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_20', 'MINUTE_RANGE', 'Minute 20', null, null, '20', '20', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_21', 'MINUTE_RANGE', 'Minute 21', null, null, '21', '21', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_22', 'MINUTE_RANGE', 'Minute 22', null, null, '22', '22', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_23', 'MINUTE_RANGE', 'Minute 23', null, null, '23', '23', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_24', 'MINUTE_RANGE', 'Minute 24', null, null, '24', '24', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_25', 'MINUTE_RANGE', 'Minute 25', null, null, '25', '25', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_26', 'MINUTE_RANGE', 'Minute 26', null, null, '26', '26', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_27', 'MINUTE_RANGE', 'Minute 27', null, null, '27', '27', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_28', 'MINUTE_RANGE', 'Minute 28', null, null, '28', '28', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_29', 'MINUTE_RANGE', 'Minute 29', null, null, '29', '29', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_30', 'MINUTE_RANGE', 'Minute 30', null, null, '30', '30', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_31', 'MINUTE_RANGE', 'Minute 31', null, null, '31', '31', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_32', 'MINUTE_RANGE', 'Minute 32', null, null, '32', '32', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_33', 'MINUTE_RANGE', 'Minute 33', null, null, '33', '33', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_34', 'MINUTE_RANGE', 'Minute 34', null, null, '34', '34', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_35', 'MINUTE_RANGE', 'Minute 35', null, null, '35', '35', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_36', 'MINUTE_RANGE', 'Minute 36', null, null, '36', '36', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_37', 'MINUTE_RANGE', 'Minute 37', null, null, '37', '37', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_38', 'MINUTE_RANGE', 'Minute 38', null, null, '38', '38', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_39', 'MINUTE_RANGE', 'Minute 39', null, null, '39', '39', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_40', 'MINUTE_RANGE', 'Minute 40', null, null, '40', '40', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_41', 'MINUTE_RANGE', 'Minute 41', null, null, '41', '41', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_42', 'MINUTE_RANGE', 'Minute 42', null, null, '42', '42', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_43', 'MINUTE_RANGE', 'Minute 43', null, null, '43', '43', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_44', 'MINUTE_RANGE', 'Minute 44', null, null, '44', '44', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_45', 'MINUTE_RANGE', 'Minute 45', null, null, '45', '45', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_46', 'MINUTE_RANGE', 'Minute 46', null, null, '46', '46', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_47', 'MINUTE_RANGE', 'Minute 47', null, null, '47', '47', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_48', 'MINUTE_RANGE', 'Minute 48', null, null, '48', '48', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_49', 'MINUTE_RANGE', 'Minute 49', null, null, '49', '49', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_50', 'MINUTE_RANGE', 'Minute 50', null, null, '50', '50', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_51', 'MINUTE_RANGE', 'Minute 51', null, null, '51', '51', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_52', 'MINUTE_RANGE', 'Minute 52', null, null, '52', '52', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_53', 'MINUTE_RANGE', 'Minute 53', null, null, '53', '53', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_54', 'MINUTE_RANGE', 'Minute 54', null, null, '54', '54', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_55', 'MINUTE_RANGE', 'Minute 55', null, null, '55', '55', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_56', 'MINUTE_RANGE', 'Minute 56', null, null, '56', '56', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_57', 'MINUTE_RANGE', 'Minute 57', null, null, '57', '57', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_58', 'MINUTE_RANGE', 'Minute 58', null, null, '58', '58', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MINUTE_59', 'MINUTE_RANGE', 'Minute 59', null, null, '59', '59', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MLK_DAY', 'INTERSECTION', 'Martin Luther King Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_01', 'MONTH_RANGE', 'January', null, null, '0', '0', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_02', 'MONTH_RANGE', 'February', null, null, '1', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_03', 'MONTH_RANGE', 'March', null, null, '2', '2', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_04', 'MONTH_RANGE', 'April', null, null, '3', '3', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_05', 'MONTH_RANGE', 'May', null, null, '4', '4', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_06', 'MONTH_RANGE', 'June', null, null, '5', '5', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_07', 'MONTH_RANGE', 'July', null, null, '6', '6', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_08', 'MONTH_RANGE', 'August', null, null, '7', '7', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_09', 'MONTH_RANGE', 'September', null, null, '8', '8', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_10', 'MONTH_RANGE', 'October', null, null, '9', '9', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_11', 'MONTH_RANGE', 'November', null, null, '10', '10', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_12', 'MONTH_RANGE', 'December', null, null, '11', '11', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MONTH_RANGE_13', 'MONTH_RANGE', 'Undecimber', null, null, '12', '12', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('MON_TO_FRI', 'DAY_OF_WEEK_RANGE', 'Monday to Friday', null, null, '2', '6', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('PRESIDENTS_DAY', 'INTERSECTION', 'Presidents Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('SAT_TO_SUN', 'DAY_OF_WEEK_RANGE', 'Saturday to Sunday', null, null, '7', '1', null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('THANKSGIVING_DAY', 'INTERSECTION', 'Thanksgiving Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('US_FED_HOLIDAYS', 'UNION', 'US Federal Holidays', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression` VALUES ('VETERANS_DAY', 'INTERSECTION', 'Veterans Day', null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `temporal_expression_assoc`
-- ----------------------------
DROP TABLE IF EXISTS `temporal_expression_assoc`;
CREATE TABLE `temporal_expression_assoc` (
  `FROM_TEMP_EXPR_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TO_TEMP_EXPR_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `EXPR_ASSOC_TYPE` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`FROM_TEMP_EXPR_ID`,`TO_TEMP_EXPR_ID`),
  KEY `TEMP_EXPR_FROM` (`FROM_TEMP_EXPR_ID`),
  KEY `TEMP_EXPR_TO` (`TO_TEMP_EXPR_ID`),
  KEY `TML_EXPRN_ASC_TXSP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TML_EXPRN_ASC_TXCS` (`CREATED_TX_STAMP`),
  CONSTRAINT `TEMP_EXPR_FROM` FOREIGN KEY (`FROM_TEMP_EXPR_ID`) REFERENCES `temporal_expression` (`TEMP_EXPR_ID`),
  CONSTRAINT `TEMP_EXPR_TO` FOREIGN KEY (`TO_TEMP_EXPR_ID`) REFERENCES `temporal_expression` (`TEMP_EXPR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of temporal_expression_assoc
-- ----------------------------
INSERT INTO `temporal_expression_assoc` VALUES ('1ST_AND_15TH_MONTH', 'DAYOFMONTH_01', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('1ST_AND_15TH_MONTH', 'DAYOFMONTH_15', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('CHRISTMAS_DAY', 'DAYOFMONTH_25', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('CHRISTMAS_DAY', 'MONTH_RANGE_12', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('COLUMBUS_DAY', '2ND_MONDAY_IN_MONTH', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('COLUMBUS_DAY', 'MONTH_RANGE_10', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('DAILY_GRIND', 'GOVT_WORK_SCHED', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('DAILY_GRIND', 'HOUR_08', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('GOVT_WORK_SCHED', 'MON_TO_FRI', 'INCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('GOVT_WORK_SCHED', 'US_FED_HOLIDAYS', 'EXCLUDE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('INDEPENDENCE_DAY', 'DAYOFMONTH_04', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('INDEPENDENCE_DAY', 'MONTH_RANGE_07', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('JANUARY_FIRST', 'DAYOFMONTH_01', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('JANUARY_FIRST', 'MONTH_RANGE_01', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('LABOR_DAY', '1ST_MONDAY_IN_MONTH', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('LABOR_DAY', 'MONTH_RANGE_09', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('MEMORIAL_DAY', 'LAST_MONDAY_IN_MONTH', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('MEMORIAL_DAY', 'MONTH_RANGE_05', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('MLK_DAY', '3RD_MONDAY_IN_MONTH', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('MLK_DAY', 'MONTH_RANGE_01', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('PRESIDENTS_DAY', '3RD_MONDAY_IN_MONTH', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('PRESIDENTS_DAY', 'MONTH_RANGE_02', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('THANKSGIVING_DAY', '4TH_THURS_IN_MONTH', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('THANKSGIVING_DAY', 'MONTH_RANGE_11', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'CHRISTMAS_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'COLUMBUS_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'INDEPENDENCE_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'JANUARY_FIRST', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'LABOR_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'MEMORIAL_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'MLK_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'PRESIDENTS_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'THANKSGIVING_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('US_FED_HOLIDAYS', 'VETERANS_DAY', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('VETERANS_DAY', 'DAYOFMONTH_11', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `temporal_expression_assoc` VALUES ('VETERANS_DAY', 'MONTH_RANGE_11', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `test_blob`
-- ----------------------------
DROP TABLE IF EXISTS `test_blob`;
CREATE TABLE `test_blob` (
  `TEST_BLOB_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TEST_BLOB_FIELD` longblob,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TEST_BLOB_ID`),
  KEY `TEST_BLOB_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TEST_BLOB_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of test_blob
-- ----------------------------

-- ----------------------------
-- Table structure for `test_field_type`
-- ----------------------------
DROP TABLE IF EXISTS `test_field_type`;
CREATE TABLE `test_field_type` (
  `TEST_FIELD_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `BLOB_FIELD` longblob,
  `BYTE_ARRAY_FIELD` longblob,
  `OBJECT_FIELD` longblob,
  `DATE_FIELD` date default NULL,
  `TIME_FIELD` time default NULL,
  `DATE_TIME_FIELD` datetime default NULL,
  `FIXED_POINT_FIELD` decimal(18,6) default NULL,
  `FLOATING_POINT_FIELD` double default NULL,
  `NUMERIC_FIELD` decimal(20,0) default NULL,
  `CLOB_FIELD` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TEST_FIELD_TYPE_ID`),
  KEY `TST_FLD_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TST_FLD_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of test_field_type
-- ----------------------------

-- ----------------------------
-- Table structure for `testing`
-- ----------------------------
DROP TABLE IF EXISTS `testing`;
CREATE TABLE `testing` (
  `TESTING_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TESTING_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `TESTING_NAME` varchar(100) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `COMMENTS` varchar(255) collate latin1_general_cs default NULL,
  `TESTING_SIZE` decimal(20,0) default NULL,
  `TESTING_DATE` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TESTING_ID`),
  KEY `ENTITY_ENTY_TYP` (`TESTING_TYPE_ID`),
  KEY `TESTING_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TESTING_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `ENTITY_ENTY_TYP` FOREIGN KEY (`TESTING_TYPE_ID`) REFERENCES `testing_type` (`TESTING_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of testing
-- ----------------------------

-- ----------------------------
-- Table structure for `testing_node`
-- ----------------------------
DROP TABLE IF EXISTS `testing_node`;
CREATE TABLE `testing_node` (
  `TESTING_NODE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PRIMARY_PARENT_NODE_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TESTING_NODE_ID`),
  KEY `TESTNG_NDE_PARNT` (`PRIMARY_PARENT_NODE_ID`),
  KEY `TSTNG_ND_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TSTNG_ND_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `TESTNG_NDE_PARNT` FOREIGN KEY (`PRIMARY_PARENT_NODE_ID`) REFERENCES `testing_node` (`TESTING_NODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of testing_node
-- ----------------------------

-- ----------------------------
-- Table structure for `testing_node_member`
-- ----------------------------
DROP TABLE IF EXISTS `testing_node_member`;
CREATE TABLE `testing_node_member` (
  `TESTING_NODE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `TESTING_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `FROM_DATE` datetime NOT NULL,
  `THRU_DATE` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TESTING_NODE_ID`,`TESTING_ID`,`FROM_DATE`),
  KEY `TESTING_NMBR_TEST` (`TESTING_ID`),
  KEY `TEST_NMBR_NODE` (`TESTING_NODE_ID`),
  KEY `TSTG_ND_MMR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TSTG_ND_MMR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `TESTING_NMBR_TEST` FOREIGN KEY (`TESTING_ID`) REFERENCES `testing` (`TESTING_ID`),
  CONSTRAINT `TEST_NMBR_NODE` FOREIGN KEY (`TESTING_NODE_ID`) REFERENCES `testing_node` (`TESTING_NODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of testing_node_member
-- ----------------------------

-- ----------------------------
-- Table structure for `testing_type`
-- ----------------------------
DROP TABLE IF EXISTS `testing_type`;
CREATE TABLE `testing_type` (
  `TESTING_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`TESTING_TYPE_ID`),
  KEY `TSTNG_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `TSTNG_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of testing_type
-- ----------------------------

-- ----------------------------
-- Table structure for `uom`
-- ----------------------------
DROP TABLE IF EXISTS `uom`;
CREATE TABLE `uom` (
  `UOM_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `UOM_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `ABBREVIATION` varchar(60) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`UOM_ID`),
  KEY `UOM_TO_TYPE` (`UOM_TYPE_ID`),
  KEY `UOM_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `UOM_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `UOM_TO_TYPE` FOREIGN KEY (`UOM_TYPE_ID`) REFERENCES `uom_type` (`UOM_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of uom
-- ----------------------------
INSERT INTO `uom` VALUES ('ADP', 'CURRENCY_MEASURE', 'ADP', 'Andoran peseta', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AED', 'CURRENCY_MEASURE', 'AED', 'United Arab Emirates Dirham', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AFA', 'CURRENCY_MEASURE', 'AFA', 'Afghani', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ALL', 'CURRENCY_MEASURE', 'ALL', 'Albanian Lek', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AMD', 'CURRENCY_MEASURE', 'AMD', 'Armenian Dram', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ANG', 'CURRENCY_MEASURE', 'ANG', 'West Indian Guilder', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AOK', 'CURRENCY_MEASURE', 'AOK', 'Angolan Kwanza', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ARA', 'CURRENCY_MEASURE', 'ARA', 'Argentinian Austral', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AREA_A', 'AREA_MEASURE', 'A', 'Acre', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_a', 'AREA_MEASURE', 'a', 'Are', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_cm2', 'AREA_MEASURE', 'cm2', 'Square Centimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_ft2', 'AREA_MEASURE', 'ft2', 'Square Foot', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_ha', 'AREA_MEASURE', 'ha', 'Hectare', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_in2', 'AREA_MEASURE', 'in2', 'Square Inch', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_km2', 'AREA_MEASURE', 'km2', 'Square Kilometer', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_m2', 'AREA_MEASURE', 'm2', 'Square Meter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_mi2', 'AREA_MEASURE', 'mi2', 'Square Mile', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_mm2', 'AREA_MEASURE', 'mm2', 'Square Millimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_rd2', 'AREA_MEASURE', 'rd2', 'Square Rod', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('AREA_yd2', 'AREA_MEASURE', 'yd2', 'Square Yard', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('ARS', 'CURRENCY_MEASURE', 'ARS', 'Argentina Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AUD', 'CURRENCY_MEASURE', 'AUD', 'Australian Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AWG', 'CURRENCY_MEASURE', 'AWG', 'Aruban Guilder', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('AZM', 'CURRENCY_MEASURE', 'AZM', 'Azerbaijan Manat', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BAD', 'CURRENCY_MEASURE', 'BAD', 'Bosnia-Herzogovinian Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BBD', 'CURRENCY_MEASURE', 'BBD', 'Barbados Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BDT', 'CURRENCY_MEASURE', 'BDT', 'Bangladesh Taka', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BGN', 'CURRENCY_MEASURE', 'BGN', 'Bulgarian Lev', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BHD', 'CURRENCY_MEASURE', 'BHD', 'Bahrain Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BIF', 'CURRENCY_MEASURE', 'BIF', 'Burundi Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BMD', 'CURRENCY_MEASURE', 'BMD', 'Bermudan Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BND', 'CURRENCY_MEASURE', 'BND', 'Brunei Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BOB', 'CURRENCY_MEASURE', 'BOB', 'Bolivian Boliviano', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BRL', 'CURRENCY_MEASURE', 'BRL', 'Brazilian Real', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BRR', 'CURRENCY_MEASURE', 'BRR', 'Brazil', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BSD', 'CURRENCY_MEASURE', 'BSD', 'Bahaman Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BWP', 'CURRENCY_MEASURE', 'BWP', 'Botswana Pula', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BYR', 'CURRENCY_MEASURE', 'BYR', 'Belorussian Ruble', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('BZD', 'CURRENCY_MEASURE', 'BZD', 'Belize Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CAD', 'CURRENCY_MEASURE', 'CAD', 'Canadian Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CDP', 'CURRENCY_MEASURE', 'CDP', 'Santo Domiongo', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CHF', 'CURRENCY_MEASURE', 'CHF', 'Swiss Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CLP', 'CURRENCY_MEASURE', 'CLP', 'Chilean Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CNY', 'CURRENCY_MEASURE', 'CNY', 'China', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('COP', 'CURRENCY_MEASURE', 'COP', 'Colombian Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CRC', 'CURRENCY_MEASURE', 'CRC', 'Costa Rica Colon', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CUP', 'CURRENCY_MEASURE', 'CUP', 'Cuban Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CVE', 'CURRENCY_MEASURE', 'CVE', 'Cape Verde Escudo', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CYP', 'CURRENCY_MEASURE', 'CYP', 'Cyprus Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('CZK', 'CURRENCY_MEASURE', 'CZK', 'Czech Krona', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('DATASPD_bps', 'DATASPD_MEASURE', 'bps', 'Bit-per-second of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATASPD_Gbps', 'DATASPD_MEASURE', 'Gbps', 'Gigabit-per-second of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATASPD_Kbps', 'DATASPD_MEASURE', 'Kbps', 'Kilobit-per-second of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATASPD_Mbps', 'DATASPD_MEASURE', 'Mbps', 'Megabit-per-second of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATASPD_Tbps', 'DATASPD_MEASURE', 'Tbps', 'Terabit-per-second of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_B', 'DATA_MEASURE', 'B', 'Byte of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_b', 'DATA_MEASURE', 'b', 'Bit of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_GB', 'DATA_MEASURE', 'GB', 'Gigabyte of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_Gb', 'DATA_MEASURE', 'Gb', 'Gigabit of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_KB', 'DATA_MEASURE', 'KB', 'Kilobyte of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_Kb', 'DATA_MEASURE', 'Kb', 'Kilobit of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_MB', 'DATA_MEASURE', 'MB', 'Megabyte of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_Mb', 'DATA_MEASURE', 'Mb', 'Megabit of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_PB', 'DATA_MEASURE', 'PB', 'Petabyte of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_TB', 'DATA_MEASURE', 'TB', 'Terabyte of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DATA_Tb', 'DATA_MEASURE', 'Tb', 'Terabit of Data', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('DJF', 'CURRENCY_MEASURE', 'DJF', 'Djibouti Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('DKK', 'CURRENCY_MEASURE', 'DKK', 'Danish Krone', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('DOP', 'CURRENCY_MEASURE', 'DOP', 'Dominican Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('DRP', 'CURRENCY_MEASURE', 'DRP', 'Dominican Republic Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('DZD', 'CURRENCY_MEASURE', 'DZD', 'Algerian Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ECS', 'CURRENCY_MEASURE', 'ECS', 'Ecuador Sucre', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('EEK', 'CURRENCY_MEASURE', 'EEK', 'Estonian Krone', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('EGP', 'CURRENCY_MEASURE', 'EGP', 'Egyptian Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('EN_Btu', 'ENERGY_MEASURE', 'Btu', 'British Thermal Unit', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('EN_cal15', 'ENERGY_MEASURE', 'cal15', 'Calorie (@15.5c)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('EN_HP', 'ENERGY_MEASURE', 'HP', 'Horsepower (mechanical)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('EN_J', 'ENERGY_MEASURE', 'J', 'Joule (absolute)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('EN_Kw', 'ENERGY_MEASURE', 'Kw', 'Kilowatt', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('EN_w', 'ENERGY_MEASURE', 'w', 'Watt', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('ETB', 'CURRENCY_MEASURE', 'ETB', 'Ethiopian Birr', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('EUR', 'CURRENCY_MEASURE', 'EUR', 'Euro', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('FJD', 'CURRENCY_MEASURE', 'FJD', 'Fiji Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('FKP', 'CURRENCY_MEASURE', 'FKP', 'Falkland Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GBP', 'CURRENCY_MEASURE', 'GBP', 'British Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GEK', 'CURRENCY_MEASURE', 'GEK', 'Georgian Kupon', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GHC', 'CURRENCY_MEASURE', 'GHC', 'Ghanian Cedi', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GIP', 'CURRENCY_MEASURE', 'GIP', 'Gibraltar Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GMD', 'CURRENCY_MEASURE', 'GMD', 'Gambian Dalasi', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GNF', 'CURRENCY_MEASURE', 'GNF', 'Guinea Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GTQ', 'CURRENCY_MEASURE', 'GTQ', 'Guatemalan Quedzal', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GWP', 'CURRENCY_MEASURE', 'GWP', 'Guinea Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('GYD', 'CURRENCY_MEASURE', 'GYD', 'Guyanese Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('HKD', 'CURRENCY_MEASURE', 'HKD', 'Hong Kong Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('HNL', 'CURRENCY_MEASURE', 'HNL', 'Honduran Lempira', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('HRD', 'CURRENCY_MEASURE', 'HRD', 'Croatian Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('HTG', 'CURRENCY_MEASURE', 'HTG', 'Haitian Gourde', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('HUF', 'CURRENCY_MEASURE', 'HUF', 'Hungarian forint', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('IDR', 'CURRENCY_MEASURE', 'IDR', 'Indeonesian Rupiah', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ILS', 'CURRENCY_MEASURE', 'ILS', 'Israeli Scheckel', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('INR', 'CURRENCY_MEASURE', 'INR', 'Indian Rupee', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('IQD', 'CURRENCY_MEASURE', 'IQD', 'Iraqui Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('IRR', 'CURRENCY_MEASURE', 'IRR', 'Iranian Rial', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ISK', 'CURRENCY_MEASURE', 'ISK', 'Iceland Krona', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('JMD', 'CURRENCY_MEASURE', 'JMD', 'Jamaican Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('JOD', 'CURRENCY_MEASURE', 'JOD', 'Jordanian Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('JPY', 'CURRENCY_MEASURE', 'JPY', 'Japanese Yen', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KES', 'CURRENCY_MEASURE', 'KES', 'Kenyan Shilling', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KHR', 'CURRENCY_MEASURE', 'KHR', 'Cambodian Riel', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KIS', 'CURRENCY_MEASURE', 'KIS', 'Kirghizstan Som', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KMF', 'CURRENCY_MEASURE', 'KMF', 'Comoros Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KPW', 'CURRENCY_MEASURE', 'KPW', 'North Korean Won', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KRW', 'CURRENCY_MEASURE', 'KRW', 'South Korean Won', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KWD', 'CURRENCY_MEASURE', 'KWD', 'Kuwaiti Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KYD', 'CURRENCY_MEASURE', 'KYD', 'Cayman Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('KZT', 'CURRENCY_MEASURE', 'KZT', 'Kazakhstani Tenge', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LAK', 'CURRENCY_MEASURE', 'LAK', 'Laotian Kip', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LBP', 'CURRENCY_MEASURE', 'LBP', 'Lebanese Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LEN_A', 'LENGTH_MEASURE', 'A', 'Angstrom', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_cb', 'LENGTH_MEASURE', 'cb', 'Cable', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_chG', 'LENGTH_MEASURE', 'chG', 'Chain (Gunter\'s/surveyor\'s)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_chR', 'LENGTH_MEASURE', 'chR', 'Chain (Ramden\'s/engineer\'s)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_cm', 'LENGTH_MEASURE', 'cm', 'Centimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_dam', 'LENGTH_MEASURE', 'dam', 'Dekameter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_dm', 'LENGTH_MEASURE', 'dm', 'Decimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_fm', 'LENGTH_MEASURE', 'fm', 'Fathom', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_ft', 'LENGTH_MEASURE', 'ft', 'Foot', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_fur', 'LENGTH_MEASURE', 'fur', 'Furlong', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_hand', 'LENGTH_MEASURE', 'hand', 'Hand (horse\'s height)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_in', 'LENGTH_MEASURE', 'in', 'Inch', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_km', 'LENGTH_MEASURE', 'km', 'Kilometer', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_league', 'LENGTH_MEASURE', 'league', 'League', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_lnG', 'LENGTH_MEASURE', 'lnG', 'Link (Gunter\'s)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_lnR', 'LENGTH_MEASURE', 'lnR', 'Link (Ramden\'s)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_m', 'LENGTH_MEASURE', 'm', 'Meter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_mi', 'LENGTH_MEASURE', 'mi', 'Mile (statute/land)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_mil', 'LENGTH_MEASURE', 'mil', 'Mil (Milli-inch)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_mm', 'LENGTH_MEASURE', 'mm', 'Millimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_nmi', 'LENGTH_MEASURE', 'nmi', 'Mile (nautical/sea)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_pica', 'LENGTH_MEASURE', 'pica', 'Pica (type size)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_point', 'LENGTH_MEASURE', 'point', 'Point (type size)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_rd', 'LENGTH_MEASURE', 'rd', 'Rod', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_u', 'LENGTH_MEASURE', 'u', 'Micrometer (Micron)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LEN_yd', 'LENGTH_MEASURE', 'yd', 'Yard', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('LKR', 'CURRENCY_MEASURE', 'LKR', 'Sri Lankan Rupee', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LRD', 'CURRENCY_MEASURE', 'LRD', 'Liberian Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LSL', 'CURRENCY_MEASURE', 'LSL', 'Lesotho Loti', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LTL', 'CURRENCY_MEASURE', 'LTL', 'Lithuanian Lita', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LVL', 'CURRENCY_MEASURE', 'LVL', 'Latvian Lat', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('LYD', 'CURRENCY_MEASURE', 'LYD', 'Libyan Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MAD', 'CURRENCY_MEASURE', 'MAD', 'Moroccan Dirham', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MDL', 'CURRENCY_MEASURE', 'MDL', 'Moldavian Lei', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MGF', 'CURRENCY_MEASURE', 'MGF', 'Madagascan Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MNT', 'CURRENCY_MEASURE', 'MNT', 'Mongolian Tugrik', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MOP', 'CURRENCY_MEASURE', 'MOP', 'Macao Pataca', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MRO', 'CURRENCY_MEASURE', 'MRO', 'Mauritanian Ouguiya', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MTL', 'CURRENCY_MEASURE', 'MTL', 'Maltese Lira', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MUR', 'CURRENCY_MEASURE', 'MUR', 'Mauritius Rupee', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MVR', 'CURRENCY_MEASURE', 'MVR', 'Maldive Rufiyaa', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MWK', 'CURRENCY_MEASURE', 'MWK', 'Malawi Kwacha', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MXN', 'CURRENCY_MEASURE', 'MXN', 'Mexican Peso (new)', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MXP', 'CURRENCY_MEASURE', 'MXP', 'Mexican Peso (old)', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MYR', 'CURRENCY_MEASURE', 'MYR', 'Malaysian Ringgit', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('MZM', 'CURRENCY_MEASURE', 'MZM', 'Mozambique Metical', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NGN', 'CURRENCY_MEASURE', 'NGN', 'Nigerian Naira', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NIC', 'CURRENCY_MEASURE', 'NIC', 'Nicaragua', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NIO', 'CURRENCY_MEASURE', 'NIO', 'Nicaraguan Cordoba', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NIS', 'CURRENCY_MEASURE', 'NIS', 'New Israeli Shekel', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NOK', 'CURRENCY_MEASURE', 'NOK', 'Norwegian Krone', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NPR', 'CURRENCY_MEASURE', 'NPR', 'Nepalese Rupee', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('NZD', 'CURRENCY_MEASURE', 'NZD', 'New Zealand Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('OMR', 'CURRENCY_MEASURE', 'OMR', 'Omani Rial', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('OTH_A', 'OTHER_MEASURE', 'A', 'Amphere - Electric current', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('OTH_box', 'OTHER_MEASURE', 'bx', 'Box', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('OTH_cd', 'OTHER_MEASURE', 'cd', 'Candela - Luminosity (intensity of light)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('OTH_ea', 'OTHER_MEASURE', 'ea', 'Each', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('OTH_mol', 'OTHER_MEASURE', 'mol', 'Mole - Substance (molecule)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('OTH_pk', 'OTHER_MEASURE', 'pk', 'Package', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('OTH_pp', 'OTHER_MEASURE', 'pp', 'Per Person', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('PAB', 'CURRENCY_MEASURE', 'PAB', 'Panamanian Balboa', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PEI', 'CURRENCY_MEASURE', 'PEI', 'Peruvian Inti', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PEN', 'CURRENCY_MEASURE', 'PEN', 'Peruvian Sol - New', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PES', 'CURRENCY_MEASURE', 'PES', 'Peruvian Sol', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PGK', 'CURRENCY_MEASURE', 'PGK', 'Papua New Guinea Kina', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PHP', 'CURRENCY_MEASURE', 'PHP', 'Philippino Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PKR', 'CURRENCY_MEASURE', 'PKR', 'Pakistan Rupee', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PLN', 'CURRENCY_MEASURE', 'PLN', 'Polish Zloty', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PLZ', 'CURRENCY_MEASURE', 'PLZ', 'Poland', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('PYG', 'CURRENCY_MEASURE', 'PYG', 'Paraguayan Guarani', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('QAR', 'CURRENCY_MEASURE', 'QAR', 'Qatar Riyal', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ROL', 'CURRENCY_MEASURE', 'ROL', 'Romanian Leu', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('RUR', 'CURRENCY_MEASURE', 'RUR', 'Russian Rouble', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('RWF', 'CURRENCY_MEASURE', 'RWF', 'Rwanda Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SAR', 'CURRENCY_MEASURE', 'SAR', 'Saudi Riyal', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SBD', 'CURRENCY_MEASURE', 'SBD', 'Solomon Islands Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SCR', 'CURRENCY_MEASURE', 'SCR', 'Seychelles Rupee', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SDP', 'CURRENCY_MEASURE', 'SDP', 'Sudanese Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SEK', 'CURRENCY_MEASURE', 'SEK', 'Swedish Krona', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SGD', 'CURRENCY_MEASURE', 'SGD', 'Singapore Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SHP', 'CURRENCY_MEASURE', 'SHP', 'St.Helena Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SLL', 'CURRENCY_MEASURE', 'SLL', 'Leone', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SOL', 'CURRENCY_MEASURE', 'SOL', 'Peru', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SOS', 'CURRENCY_MEASURE', 'SOS', 'Somalian Shilling', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SRG', 'CURRENCY_MEASURE', 'SRG', 'Surinam Guilder', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('STD', 'CURRENCY_MEASURE', 'STD', 'Sao Tome / Principe Dobra', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SUR', 'CURRENCY_MEASURE', 'SUR', 'Russian Ruble (old)', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SVC', 'CURRENCY_MEASURE', 'SVC', 'El Salvador Colon', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SYP', 'CURRENCY_MEASURE', 'SYP', 'Syrian Pound', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('SZL', 'CURRENCY_MEASURE', 'SZL', 'Swaziland Lilangeni', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TEMP_C', 'TEMP_MEASURE', 'C', 'Degrees Celsius', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TEMP_F', 'TEMP_MEASURE', 'F', 'Degrees Fahrenheit', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TEMP_K', 'TEMP_MEASURE', 'K', 'Kelvin', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_century', 'TIME_FREQ_MEASURE', 'century', 'Time in Centuries', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_day', 'TIME_FREQ_MEASURE', 'day', 'Time in Days', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_decade', 'TIME_FREQ_MEASURE', 'decade', 'Time in Decades', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_hr', 'TIME_FREQ_MEASURE', 'hr', 'Time in Hours', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_millenium', 'TIME_FREQ_MEASURE', 'millenium', 'Time in Millenia', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_min', 'TIME_FREQ_MEASURE', 'min', 'Time in Minutes', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_mon', 'TIME_FREQ_MEASURE', 'mon', 'Time in Months', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_ms', 'TIME_FREQ_MEASURE', 'ms', 'Time in Milli-Seconds', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_s', 'TIME_FREQ_MEASURE', 's', 'Time in Seconds', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_score', 'TIME_FREQ_MEASURE', 'score', 'Time in Scores', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_wk', 'TIME_FREQ_MEASURE', 'wk', 'Time in Weeks', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('TF_yr', 'TIME_FREQ_MEASURE', 'yr', 'Time in Years', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('THB', 'CURRENCY_MEASURE', 'THB', 'Thailand Baht', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TJR', 'CURRENCY_MEASURE', 'TJR', 'Tadzhikistani Ruble', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TMM', 'CURRENCY_MEASURE', 'TMM', 'Turkmenistani Manat', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TND', 'CURRENCY_MEASURE', 'TND', 'Tunisian Dinar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TOP', 'CURRENCY_MEASURE', 'TOP', 'Tongan Pa\'anga', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TPE', 'CURRENCY_MEASURE', 'TPE', 'Timor Escudo', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TRY', 'CURRENCY_MEASURE', 'TRY', 'Turkish Lira', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TTD', 'CURRENCY_MEASURE', 'TTD', 'Trinidad and Tobago Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TWD', 'CURRENCY_MEASURE', 'TWD', 'New Taiwan Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('TZS', 'CURRENCY_MEASURE', 'TZS', 'Tanzanian Shilling', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('UAH', 'CURRENCY_MEASURE', 'UAH', 'Ukrainian Hryvnia', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('UGS', 'CURRENCY_MEASURE', 'UGS', 'Ugandan Shilling', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('USD', 'CURRENCY_MEASURE', 'USD', 'United States Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('UYP', 'CURRENCY_MEASURE', 'UYP', 'Uruguayan New Peso', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('UYU', 'CURRENCY_MEASURE', 'UYU', 'Uruguay', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('VDRY_cm3', 'VOLUME_DRY_MEASURE', 'cm3', 'Cubic centimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VDRY_ft3', 'VOLUME_DRY_MEASURE', 'ft3', 'Cubic foot', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VDRY_in3', 'VOLUME_DRY_MEASURE', 'in3', 'Cubic inch', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VDRY_m3', 'VOLUME_DRY_MEASURE', 'm3', 'Cubic meter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VDRY_mm3', 'VOLUME_DRY_MEASURE', 'mm3', 'Cubic millimeter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VDRY_ST', 'VOLUME_DRY_MEASURE', 'ST', 'Stere (cubic meter)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VDRY_yd3', 'VOLUME_DRY_MEASURE', 'yd3', 'Cubic yard', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VEB', 'CURRENCY_MEASURE', 'VEB', 'Venezuelan Bolivar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('VLIQ_bbl', 'VOLUME_LIQ_MEASURE', 'bbl', 'Barrel (US)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_cup', 'VOLUME_LIQ_MEASURE', 'cup', 'Cup', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_dr', 'VOLUME_LIQ_MEASURE', 'dr', 'Dram (US)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_galUK', 'VOLUME_LIQ_MEASURE', 'gal', 'Gallon (UK)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_galUS', 'VOLUME_LIQ_MEASURE', 'gal', 'Gallon (US)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_gi', 'VOLUME_LIQ_MEASURE', 'gi', 'Gill (1/4 UK pint)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_L', 'VOLUME_LIQ_MEASURE', 'L', 'Liter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_ml', 'VOLUME_LIQ_MEASURE', 'ml', 'Milliliter', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_ozUK', 'VOLUME_LIQ_MEASURE', 'fl. oz (UK)', 'Ounce, fluid (UK)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_ozUS', 'VOLUME_LIQ_MEASURE', 'fl. oz (US)', 'Ounce, fluid (US)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_ptUK', 'VOLUME_LIQ_MEASURE', 'pt (UK)', 'Pint (UK)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_ptUS', 'VOLUME_LIQ_MEASURE', 'pt (US)', 'Pint (US)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_qt', 'VOLUME_LIQ_MEASURE', 'qt', 'Quart', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_Tbs', 'VOLUME_LIQ_MEASURE', 'Tbs', 'Tablespoon', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VLIQ_tsp', 'VOLUME_LIQ_MEASURE', 'tsp', 'Teaspoon', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('VND', 'CURRENCY_MEASURE', 'VND', 'Vietnamese Dong', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('VUV', 'CURRENCY_MEASURE', 'VUV', 'Vanuatu Vatu', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('WST', 'CURRENCY_MEASURE', 'WST', 'Samoan Tala', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('WT_dr_avdp', 'WEIGHT_MEASURE', 'dr avdp', 'Dram (avdp)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_dwt', 'WEIGHT_MEASURE', 'dwt', 'Pennyweight', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_g', 'WEIGHT_MEASURE', 'g', 'Gram', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_gr', 'WEIGHT_MEASURE', 'gr', 'Grain', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_kg', 'WEIGHT_MEASURE', 'kg', 'Kilogram', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_lb', 'WEIGHT_MEASURE', 'lb', 'Pound (avdp)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_lt', 'WEIGHT_MEASURE', 'lt', 'Ton (long or British)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_mg', 'WEIGHT_MEASURE', 'mg', 'Milligram', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_mt', 'WEIGHT_MEASURE', 'mt', 'Ton (metric)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_oz', 'WEIGHT_MEASURE', 'oz', 'Ounce (avdp)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_oz_tr', 'WEIGHT_MEASURE', 'oz tr', 'Ounce (troy)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_sh_t', 'WEIGHT_MEASURE', 'sh t', 'Ton (short or US)', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('WT_st', 'WEIGHT_MEASURE', 'st', 'Stone', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom` VALUES ('XAF', 'CURRENCY_MEASURE', 'XAF', 'Gabon C.f.A Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('XCD', 'CURRENCY_MEASURE', 'XCD', 'East Carribean Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('XOF', 'CURRENCY_MEASURE', 'XOF', 'Benin C.f.A. Franc', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('YER', 'CURRENCY_MEASURE', 'YER', 'Yemeni Ryal', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ZAR', 'CURRENCY_MEASURE', 'ZAR', 'South African Rand', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ZMK', 'CURRENCY_MEASURE', 'ZMK', 'Zambian Kwacha', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ZRZ', 'CURRENCY_MEASURE', 'ZRZ', 'Zaire', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');
INSERT INTO `uom` VALUES ('ZWD', 'CURRENCY_MEASURE', 'ZWD', 'Zimbabwean Dollar', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56', '2011-12-01 17:17:56');

-- ----------------------------
-- Table structure for `uom_conversion`
-- ----------------------------
DROP TABLE IF EXISTS `uom_conversion`;
CREATE TABLE `uom_conversion` (
  `UOM_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `UOM_ID_TO` varchar(20) collate latin1_general_cs NOT NULL,
  `CONVERSION_FACTOR` double default NULL,
  `CUSTOM_METHOD_ID` varchar(20) collate latin1_general_cs default NULL,
  `DECIMAL_SCALE` decimal(20,0) default NULL,
  `ROUNDING_MODE` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`UOM_ID`,`UOM_ID_TO`),
  KEY `UOM_CONV_MAIN` (`UOM_ID`),
  KEY `UOM_CONV_TO` (`UOM_ID_TO`),
  KEY `UOM_CUSTOM_METHOD` (`CUSTOM_METHOD_ID`),
  KEY `UM_CNVRSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `UM_CNVRSN_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `UOM_CONV_MAIN` FOREIGN KEY (`UOM_ID`) REFERENCES `uom` (`UOM_ID`),
  CONSTRAINT `UOM_CONV_TO` FOREIGN KEY (`UOM_ID_TO`) REFERENCES `uom` (`UOM_ID`),
  CONSTRAINT `UOM_CUSTOM_METHOD` FOREIGN KEY (`CUSTOM_METHOD_ID`) REFERENCES `custom_method` (`CUSTOM_METHOD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of uom_conversion
-- ----------------------------
INSERT INTO `uom_conversion` VALUES ('AREA_A', 'AREA_ft2', '43560', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_a', 'AREA_m2', '100', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_cm2', 'AREA_in2', '0.155', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_cm2', 'AREA_mm2', '100', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_ft2', 'AREA_in2', '144', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_ha', 'AREA_A', '2.471', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_ha', 'AREA_m2', '10000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_km2', 'AREA_m2', '1000000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_m2', 'AREA_cm2', '10000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_mi2', 'AREA_A', '639.8', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_rd2', 'AREA_ft2', '272.25', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('AREA_yd2', 'AREA_ft2', '9', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATASPD_Gbps', 'DATASPD_Mbps', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATASPD_Kbps', 'DATASPD_bps', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATASPD_Mbps', 'DATASPD_Kbps', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATASPD_Tbps', 'DATASPD_Gbps', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_B', 'DATA_b', '8', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_GB', 'DATA_MB', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_Gb', 'DATA_Mb', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_KB', 'DATA_B', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_Kb', 'DATA_b', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_MB', 'DATA_KB', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_Mb', 'DATA_Kb', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_PB', 'DATA_TB', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_TB', 'DATA_GB', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('DATA_Tb', 'DATA_Gb', '1024', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('EN_cal15', 'EN_J', '4.1855', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('EN_HP', 'EN_w', '746', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('EN_Kw', 'EN_HP', '1.341', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('EN_Kw', 'EN_w', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_A', 'LEN_in', '4e-009', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_A', 'LEN_m', '1e-010', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_cb', 'LEN_fm', '120', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_cb', 'LEN_ft', '720', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_chG', 'LEN_ft', '66', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_chG', 'LEN_rd', '4', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_chR', 'LEN_ft', '100', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_cm', 'LEN_m', '0.01', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_dam', 'LEN_m', '10', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_dm', 'LEN_m', '0.1', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_fm', 'LEN_ft', '6', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_ft', 'LEN_in', '12', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_ft', 'LEN_m', '0.3048', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_fur', 'LEN_mi', '0.125', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_hand', 'LEN_in', '4', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_in', 'LEN_cm', '2.54', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_in', 'LEN_mm', '25.4', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_in', 'LEN_u', '25400', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_km', 'LEN_m', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_league', 'LEN_mi', '3', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_lnG', 'LEN_in', '7.92', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_lnR', 'LEN_in', '12', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_m', 'LEN_in', '39.37', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_mi', 'LEN_ft', '5280', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_mi', 'LEN_fur', '8', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_mi', 'LEN_km', '1.609', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_mil', 'LEN_in', '0.001', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_mm', 'LEN_m', '0.001', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_mm', 'LEN_u', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_nmi', 'LEN_ft', '6076.11549', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_nmi', 'LEN_km', '1.85', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_pica', 'LEN_point', '12', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_point', 'LEN_in', '0.0138', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_point', 'LEN_mm', '0.351', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_rd', 'LEN_ft', '16.5', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_u', 'LEN_mm', '0.001', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('LEN_yd', 'LEN_ft', '3', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_century', 'TF_yr', '100', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_day', 'TF_hr', '24', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_decade', 'TF_yr', '10', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_hr', 'TF_min', '60', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_millenium', 'TF_yr', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_min', 'TF_s', '60', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_s', 'TF_ms', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_score', 'TF_yr', '20', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_wk', 'TF_day', '7', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('TF_yr', 'TF_day', '365', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VDRY_cm3', 'VDRY_in3', '0.061', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VDRY_cm3', 'VDRY_mm3', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VDRY_ft3', 'VDRY_in3', '1728', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VDRY_m3', 'VDRY_yd3', '1.3', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VDRY_ST', 'VDRY_m3', '1', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VDRY_yd3', 'VDRY_ft3', '27', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_bbl', 'VLIQ_galUS', '31.5', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_cup', 'VLIQ_Tbs', '16', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_galUK', 'VDRY_m3', '0.4546', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_galUK', 'VLIQ_galUS', '1.2009', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_galUS', 'VLIQ_L', '3.785', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_galUS', 'VLIQ_qt', '4', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_L', 'VDRY_m3', '0.001', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_L', 'VLIQ_ml', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_L', 'VLIQ_qt', '1.056', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ml', 'VLIQ_L', '0.001', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ozUK', 'VLIQ_L', '0.029', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ozUK', 'VLIQ_ozUS', '0.96', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ozUS', 'VLIQ_dr', '8', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ozUS', 'VLIQ_Tbs', '2', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ptUK', 'VLIQ_gi', '4', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ptUK', 'VLIQ_ptUS', '1.2', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_ptUS', 'VLIQ_ozUS', '16', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_qt', 'VLIQ_ptUS', '2', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('VLIQ_Tbs', 'VLIQ_tsp', '3', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_dr_avdp', 'WT_oz', '0.0625', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_dwt', 'WT_g', '1.555', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_g', 'WT_kg', '0.001', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_g', 'WT_lb', '0.00220462247604', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_g', 'WT_mg', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_g', 'WT_oz', '0.03527', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_gr', 'WT_g', '0.0648', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_gr', 'WT_oz', '0.00229', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_kg', 'WT_g', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_kg', 'WT_lb', '2.2', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_lb', 'WT_oz', '16', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_lt', 'WT_lb', '2240', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_lt', 'WT_mt', '1.02', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_mt', 'WT_kg', '1000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_oz', 'WT_g', '28', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_oz', 'WT_lb', '0.0625', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_oz_tr', 'WT_g', '30', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_sh_t', 'WT_lb', '2000', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `uom_conversion` VALUES ('WT_st', 'WT_lb', '14', null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `uom_conversion_dated`
-- ----------------------------
DROP TABLE IF EXISTS `uom_conversion_dated`;
CREATE TABLE `uom_conversion_dated` (
  `UOM_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `UOM_ID_TO` varchar(20) collate latin1_general_cs NOT NULL,
  `FROM_DATE` datetime NOT NULL,
  `THRU_DATE` datetime default NULL,
  `CONVERSION_FACTOR` double default NULL,
  `CUSTOM_METHOD_ID` varchar(20) collate latin1_general_cs default NULL,
  `DECIMAL_SCALE` decimal(20,0) default NULL,
  `ROUNDING_MODE` varchar(20) collate latin1_general_cs default NULL,
  `PURPOSE_ENUM_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`UOM_ID`,`UOM_ID_TO`,`FROM_DATE`),
  KEY `DATE_UOM_CONV_MAIN` (`UOM_ID`),
  KEY `DATE_UOM_CONV_TO` (`UOM_ID_TO`),
  KEY `UOMD_CUSTOM_METHOD` (`CUSTOM_METHOD_ID`),
  KEY `UOMD_PURPOSE_ENUM` (`PURPOSE_ENUM_ID`),
  KEY `UM_CNVRN_DTD_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `UM_CNVRN_DTD_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `DATE_UOM_CONV_MAIN` FOREIGN KEY (`UOM_ID`) REFERENCES `uom` (`UOM_ID`),
  CONSTRAINT `DATE_UOM_CONV_TO` FOREIGN KEY (`UOM_ID_TO`) REFERENCES `uom` (`UOM_ID`),
  CONSTRAINT `UOMD_CUSTOM_METHOD` FOREIGN KEY (`CUSTOM_METHOD_ID`) REFERENCES `custom_method` (`CUSTOM_METHOD_ID`),
  CONSTRAINT `UOMD_PURPOSE_ENUM` FOREIGN KEY (`PURPOSE_ENUM_ID`) REFERENCES `enumeration` (`ENUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of uom_conversion_dated
-- ----------------------------

-- ----------------------------
-- Table structure for `uom_group`
-- ----------------------------
DROP TABLE IF EXISTS `uom_group`;
CREATE TABLE `uom_group` (
  `UOM_GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `UOM_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`UOM_GROUP_ID`,`UOM_ID`),
  KEY `UOM_GROUP_UOM` (`UOM_ID`),
  KEY `UOM_GROUP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `UOM_GROUP_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `UOM_GROUP_UOM` FOREIGN KEY (`UOM_ID`) REFERENCES `uom` (`UOM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of uom_group
-- ----------------------------

-- ----------------------------
-- Table structure for `uom_type`
-- ----------------------------
DROP TABLE IF EXISTS `uom_type`;
CREATE TABLE `uom_type` (
  `UOM_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PARENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `HAS_TABLE` char(1) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`UOM_TYPE_ID`),
  KEY `UOM_TYPE_PARENT` (`PARENT_TYPE_ID`),
  KEY `UOM_TYPE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `UOM_TYPE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `UOM_TYPE_PARENT` FOREIGN KEY (`PARENT_TYPE_ID`) REFERENCES `uom_type` (`UOM_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of uom_type
-- ----------------------------
INSERT INTO `uom_type` VALUES ('AREA_MEASURE', null, 'N', 'Area', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('CURRENCY_MEASURE', null, 'N', 'Currency', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('DATASPD_MEASURE', null, 'N', 'Data Speed', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('DATA_MEASURE', null, 'N', 'Data Size', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('ENERGY_MEASURE', null, 'N', 'Energy', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('LENGTH_MEASURE', null, 'N', 'Length', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('OTHER_MEASURE', null, 'N', 'Other', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('TEMP_MEASURE', null, 'N', 'Temperature', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('TIME_FREQ_MEASURE', null, 'N', 'Time/Frequency', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('VOLUME_DRY_MEASURE', null, 'N', 'Dry Volume', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('VOLUME_LIQ_MEASURE', null, 'N', 'Liquid Volume', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `uom_type` VALUES ('WEIGHT_MEASURE', null, 'N', 'Weight', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `user_agent`
-- ----------------------------
DROP TABLE IF EXISTS `user_agent`;
CREATE TABLE `user_agent` (
  `USER_AGENT_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `BROWSER_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `PLATFORM_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `PROTOCOL_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `USER_AGENT_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `USER_AGENT_METHOD_TYPE_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_AGENT_ID`),
  KEY `UAGENT_BROWSER` (`BROWSER_TYPE_ID`),
  KEY `UAGENT_PLATFORM` (`PLATFORM_TYPE_ID`),
  KEY `UAGENT_PROTOCOL` (`PROTOCOL_TYPE_ID`),
  KEY `UAGENT_TYPE` (`USER_AGENT_TYPE_ID`),
  KEY `UAGENT_METHOD` (`USER_AGENT_METHOD_TYPE_ID`),
  KEY `USER_AGENT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USER_AGENT_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `UAGENT_BROWSER` FOREIGN KEY (`BROWSER_TYPE_ID`) REFERENCES `browser_type` (`BROWSER_TYPE_ID`),
  CONSTRAINT `UAGENT_METHOD` FOREIGN KEY (`USER_AGENT_METHOD_TYPE_ID`) REFERENCES `user_agent_method_type` (`USER_AGENT_METHOD_TYPE_ID`),
  CONSTRAINT `UAGENT_PLATFORM` FOREIGN KEY (`PLATFORM_TYPE_ID`) REFERENCES `platform_type` (`PLATFORM_TYPE_ID`),
  CONSTRAINT `UAGENT_PROTOCOL` FOREIGN KEY (`PROTOCOL_TYPE_ID`) REFERENCES `protocol_type` (`PROTOCOL_TYPE_ID`),
  CONSTRAINT `UAGENT_TYPE` FOREIGN KEY (`USER_AGENT_TYPE_ID`) REFERENCES `user_agent_type` (`USER_AGENT_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_agent
-- ----------------------------

-- ----------------------------
-- Table structure for `user_agent_method_type`
-- ----------------------------
DROP TABLE IF EXISTS `user_agent_method_type`;
CREATE TABLE `user_agent_method_type` (
  `USER_AGENT_METHOD_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_AGENT_METHOD_TYPE_ID`),
  KEY `USR_AGT_MTD_TP_TXP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_AGT_MTD_TP_TXS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_agent_method_type
-- ----------------------------

-- ----------------------------
-- Table structure for `user_agent_type`
-- ----------------------------
DROP TABLE IF EXISTS `user_agent_type`;
CREATE TABLE `user_agent_type` (
  `USER_AGENT_TYPE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_AGENT_TYPE_ID`),
  KEY `USR_AGNT_TP_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_AGNT_TP_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_agent_type
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `CURRENT_PASSWORD` varchar(60) collate latin1_general_cs default NULL,
  `PASSWORD_HINT` varchar(255) collate latin1_general_cs default NULL,
  `IS_SYSTEM` char(1) collate latin1_general_cs default NULL,
  `ENABLED` char(1) collate latin1_general_cs default NULL,
  `HAS_LOGGED_OUT` char(1) collate latin1_general_cs default NULL,
  `REQUIRE_PASSWORD_CHANGE` char(1) collate latin1_general_cs default NULL,
  `LAST_CURRENCY_UOM` varchar(20) collate latin1_general_cs default NULL,
  `LAST_LOCALE` varchar(10) collate latin1_general_cs default NULL,
  `LAST_TIME_ZONE` varchar(60) collate latin1_general_cs default NULL,
  `DISABLED_DATE_TIME` datetime default NULL,
  `SUCCESSIVE_FAILED_LOGINS` decimal(20,0) default NULL,
  `EXTERNAL_AUTH_ID` varchar(250) collate latin1_general_cs default NULL,
  `USER_LDAP_DN` varchar(250) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_LOGIN_ID`),
  KEY `USER_LOGIN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USER_LOGIN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_login
-- ----------------------------
INSERT INTO `user_login` VALUES ('admin', '{SHA}47ca69ebb4bdc9ae0adec130880165d2cc05db1a', null, null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login` VALUES ('anonymous', null, null, null, 'N', null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `user_login` VALUES ('bizadmin', '{SHA}47ca69ebb4bdc9ae0adec130880165d2cc05db1a', null, null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login` VALUES ('demoadmin', '{SHA}47ca69ebb4bdc9ae0adec130880165d2cc05db1a', null, null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login` VALUES ('flexadmin', '{SHA}47ca69ebb4bdc9ae0adec130880165d2cc05db1a', null, null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login` VALUES ('ltdadmin', '{SHA}47ca69ebb4bdc9ae0adec130880165d2cc05db1a', null, null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login` VALUES ('ltdadmin1', '{SHA}47ca69ebb4bdc9ae0adec130880165d2cc05db1a', null, null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login` VALUES ('system', null, null, 'Y', 'N', null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `user_login_history`
-- ----------------------------
DROP TABLE IF EXISTS `user_login_history`;
CREATE TABLE `user_login_history` (
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `VISIT_ID` varchar(20) collate latin1_general_cs default NULL,
  `FROM_DATE` datetime NOT NULL,
  `THRU_DATE` datetime default NULL,
  `PASSWORD_USED` varchar(60) collate latin1_general_cs default NULL,
  `SUCCESSFUL_LOGIN` char(1) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_LOGIN_ID`,`FROM_DATE`),
  KEY `USER_LH_USER` (`USER_LOGIN_ID`),
  KEY `USR_LGN_HSR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_LGN_HSR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `USER_LH_USER` FOREIGN KEY (`USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_login_history
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login_password_history`
-- ----------------------------
DROP TABLE IF EXISTS `user_login_password_history`;
CREATE TABLE `user_login_password_history` (
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `FROM_DATE` datetime NOT NULL,
  `THRU_DATE` datetime default NULL,
  `CURRENT_PASSWORD` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_LOGIN_ID`,`FROM_DATE`),
  KEY `USER_LPH_USER` (`USER_LOGIN_ID`),
  KEY `USR_LGN_PSD_HSR_TP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_LGN_PSD_HSR_TS` (`CREATED_TX_STAMP`),
  CONSTRAINT `USER_LPH_USER` FOREIGN KEY (`USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_login_password_history
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login_security_group`
-- ----------------------------
DROP TABLE IF EXISTS `user_login_security_group`;
CREATE TABLE `user_login_security_group` (
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `GROUP_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `FROM_DATE` datetime NOT NULL,
  `THRU_DATE` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_LOGIN_ID`,`GROUP_ID`,`FROM_DATE`),
  KEY `USER_SECGRP_USER` (`USER_LOGIN_ID`),
  KEY `USER_SECGRP_GRP` (`GROUP_ID`),
  KEY `USR_LGN_SCT_GRP_TP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_LGN_SCT_GRP_TS` (`CREATED_TX_STAMP`),
  CONSTRAINT `USER_SECGRP_GRP` FOREIGN KEY (`GROUP_ID`) REFERENCES `security_group` (`GROUP_ID`),
  CONSTRAINT `USER_SECGRP_USER` FOREIGN KEY (`USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_login_security_group
-- ----------------------------
INSERT INTO `user_login_security_group` VALUES ('admin', 'FULLADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login_security_group` VALUES ('bizadmin', 'BIZADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login_security_group` VALUES ('demoadmin', 'VIEWADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login_security_group` VALUES ('flexadmin', 'FLEXADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login_security_group` VALUES ('ltdadmin', 'VIEWADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login_security_group` VALUES ('ltdadmin1', 'VIEWADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `user_login_security_group` VALUES ('system', 'FULLADMIN', '2001-01-01 12:00:00', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `user_login_session`
-- ----------------------------
DROP TABLE IF EXISTS `user_login_session`;
CREATE TABLE `user_login_session` (
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `SAVED_DATE` datetime default NULL,
  `SESSION_DATA` longtext collate latin1_general_cs,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_LOGIN_ID`),
  KEY `USER_SESSION_USER` (`USER_LOGIN_ID`),
  KEY `USR_LGN_SSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_LGN_SSN_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `USER_SESSION_USER` FOREIGN KEY (`USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_login_session
-- ----------------------------

-- ----------------------------
-- Table structure for `user_pref_group_type`
-- ----------------------------
DROP TABLE IF EXISTS `user_pref_group_type`;
CREATE TABLE `user_pref_group_type` (
  `USER_PREF_GROUP_TYPE_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_PREF_GROUP_TYPE_ID`),
  KEY `USR_PRF_GRP_TP_TXP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_PRF_GRP_TP_TXS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_pref_group_type
-- ----------------------------
INSERT INTO `user_pref_group_type` VALUES ('GLOBAL_PREFERENCES', 'Global preferences', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `user_preference`
-- ----------------------------
DROP TABLE IF EXISTS `user_preference`;
CREATE TABLE `user_preference` (
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs NOT NULL,
  `USER_PREF_TYPE_ID` varchar(60) collate latin1_general_cs NOT NULL,
  `USER_PREF_GROUP_TYPE_ID` varchar(60) collate latin1_general_cs default NULL,
  `USER_PREF_VALUE` varchar(255) collate latin1_general_cs default NULL,
  `USER_PREF_DATA_TYPE` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`USER_LOGIN_ID`,`USER_PREF_TYPE_ID`),
  KEY `USR_PRFRNC_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `USR_PRFRNC_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of user_preference
-- ----------------------------
INSERT INTO `user_preference` VALUES ('_NA_', 'VISUAL_THEME', 'GLOBAL_PREFERENCES', 'DEFAULT', null, '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `visit`
-- ----------------------------
DROP TABLE IF EXISTS `visit`;
CREATE TABLE `visit` (
  `VISIT_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `VISITOR_ID` varchar(20) collate latin1_general_cs default NULL,
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs default NULL,
  `USER_CREATED` char(1) collate latin1_general_cs default NULL,
  `SESSION_ID` varchar(250) collate latin1_general_cs default NULL,
  `SERVER_IP_ADDRESS` varchar(20) collate latin1_general_cs default NULL,
  `SERVER_HOST_NAME` varchar(255) collate latin1_general_cs default NULL,
  `WEBAPP_NAME` varchar(60) collate latin1_general_cs default NULL,
  `INITIAL_LOCALE` varchar(60) collate latin1_general_cs default NULL,
  `INITIAL_REQUEST` varchar(255) collate latin1_general_cs default NULL,
  `INITIAL_REFERRER` varchar(255) collate latin1_general_cs default NULL,
  `INITIAL_USER_AGENT` varchar(255) collate latin1_general_cs default NULL,
  `USER_AGENT_ID` varchar(20) collate latin1_general_cs default NULL,
  `CLIENT_IP_ADDRESS` varchar(60) collate latin1_general_cs default NULL,
  `CLIENT_HOST_NAME` varchar(255) collate latin1_general_cs default NULL,
  `CLIENT_USER` varchar(60) collate latin1_general_cs default NULL,
  `CLIENT_IP_ISP_NAME` varchar(60) collate latin1_general_cs default NULL,
  `CLIENT_IP_POSTAL_CODE` varchar(60) collate latin1_general_cs default NULL,
  `CLIENT_IP_STATE_PROV_GEO_ID` varchar(20) collate latin1_general_cs default NULL,
  `CLIENT_IP_COUNTRY_GEO_ID` varchar(20) collate latin1_general_cs default NULL,
  `COOKIE` varchar(60) collate latin1_general_cs default NULL,
  `FROM_DATE` datetime default NULL,
  `THRU_DATE` datetime default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VISIT_ID`),
  KEY `VISIT_VISITOR` (`VISITOR_ID`),
  KEY `VISIT_USER_AGNT` (`USER_AGENT_ID`),
  KEY `VISIT_CIP_STPRV` (`CLIENT_IP_STATE_PROV_GEO_ID`),
  KEY `VISIT_CIP_CNTRY` (`CLIENT_IP_COUNTRY_GEO_ID`),
  KEY `VISIT_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `VISIT_TXCRTS` (`CREATED_TX_STAMP`),
  KEY `VISIT_THRU_IDX` (`THRU_DATE`),
  CONSTRAINT `VISIT_CIP_CNTRY` FOREIGN KEY (`CLIENT_IP_COUNTRY_GEO_ID`) REFERENCES `geo` (`GEO_ID`),
  CONSTRAINT `VISIT_CIP_STPRV` FOREIGN KEY (`CLIENT_IP_STATE_PROV_GEO_ID`) REFERENCES `geo` (`GEO_ID`),
  CONSTRAINT `VISIT_USER_AGNT` FOREIGN KEY (`USER_AGENT_ID`) REFERENCES `user_agent` (`USER_AGENT_ID`),
  CONSTRAINT `VISIT_VISITOR` FOREIGN KEY (`VISITOR_ID`) REFERENCES `visitor` (`VISITOR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of visit
-- ----------------------------

-- ----------------------------
-- Table structure for `visitor`
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor` (
  `VISITOR_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `USER_LOGIN_ID` varchar(250) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VISITOR_ID`),
  KEY `VISITOR_USRLGN` (`USER_LOGIN_ID`),
  KEY `VISITOR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `VISITOR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `VISITOR_USRLGN` FOREIGN KEY (`USER_LOGIN_ID`) REFERENCES `user_login` (`USER_LOGIN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of visitor
-- ----------------------------

-- ----------------------------
-- Table structure for `visual_theme`
-- ----------------------------
DROP TABLE IF EXISTS `visual_theme`;
CREATE TABLE `visual_theme` (
  `VISUAL_THEME_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `VISUAL_THEME_SET_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VISUAL_THEME_ID`),
  KEY `VT_THEME_SET` (`VISUAL_THEME_SET_ID`),
  KEY `VSL_THM_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `VSL_THM_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `VT_THEME_SET` FOREIGN KEY (`VISUAL_THEME_SET_ID`) REFERENCES `visual_theme_set` (`VISUAL_THEME_SET_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of visual_theme
-- ----------------------------
INSERT INTO `visual_theme` VALUES ('BIZZNESS_TIME', 'BACKOFFICE', 'It\'s bizzness, it\'s bizzness time.  I couldn\'t have said it better myself. This theme gets down', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme` VALUES ('BLUELIGHT', 'BACKOFFICE', 'BlueLight Theme: Breadcrumbs, drop-down menus and rounded corners', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme` VALUES ('DROPPINGCRUMBS', 'BACKOFFICE', 'Dropping Crumbs: Evolution of Blue-Light, includes a drop down menu embedded in the breadcrumbs bar', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme` VALUES ('FLAT_GREY', 'BACKOFFICE', 'Flat Grey - Floating Layout, Sight-Impaired Accessible, Bidirectional', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme` VALUES ('MULTIFLEX', 'ECOMMERCE', 'Alternative VisualTheme for Ecommerce', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme` VALUES ('TOMAHAWK', 'BACKOFFICE', 'Tomahawk: the evolution of the Dropping Crumbs theme', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `visual_theme_resource`
-- ----------------------------
DROP TABLE IF EXISTS `visual_theme_resource`;
CREATE TABLE `visual_theme_resource` (
  `VISUAL_THEME_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `RESOURCE_TYPE_ENUM_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `SEQUENCE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `RESOURCE_VALUE` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VISUAL_THEME_ID`,`RESOURCE_TYPE_ENUM_ID`,`SEQUENCE_ID`),
  KEY `VT_RES_THEME` (`VISUAL_THEME_ID`),
  KEY `VT_RES_TYPE_ENUM` (`RESOURCE_TYPE_ENUM_ID`),
  KEY `VSL_THM_RSC_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `VSL_THM_RSC_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `VT_RES_THEME` FOREIGN KEY (`VISUAL_THEME_ID`) REFERENCES `visual_theme` (`VISUAL_THEME_ID`),
  CONSTRAINT `VT_RES_TYPE_ENUM` FOREIGN KEY (`RESOURCE_TYPE_ENUM_ID`) REFERENCES `enumeration` (`ENUM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of visual_theme_resource
-- ----------------------------
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_DOCBOOKSTYLESHEET', '01', '/bizznesstime/webapp/bizznesstime/css/docbook.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_FTR_TMPLT_LOC', '01', 'component://bizznesstime/includes/footer.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_HDR_IMAGE_URL', '01', '/images/ofbiz_logo.gif', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_HDR_JAVASCRIPT', '01', '/bizznesstime/js/application.js', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_HDR_TMPLT_LOC', '01', 'component://bizznesstime/includes/header.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_HELPSTYLESHEET', '01', '/bizznesstime/css/help.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_MSG_TMPLT_LOC', '01', 'component://bizznesstime/includes/messages.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_NAME', '01', 'BIZZNESS_TIME', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_NAV_TMPLT_LOC', '01', 'component://bizznesstime/includes/appbar.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_SCREENSHOT', '01', '/bizznesstime/screenshot.jpg', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_SHORTCUT_ICON', '01', '/images/ofbiz.ico', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BIZZNESS_TIME', 'VT_STYLESHEET', '01', '/bizznesstime/css/style.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_DOCBOOKSTYLESHEET', '01', '/bluelight/webapp/bluelight/docbook.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_FTR_TMPLT_LOC', '01', 'component://bluelight/includes/footer.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_HDR_IMAGE_URL', '01', '/images/ofbiz_logo.gif', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_HDR_JAVASCRIPT', '01', '/bluelight/dropdown.js', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_HDR_TMPLT_LOC', '01', 'component://bluelight/includes/header.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_HELPSTYLESHEET', '01', '/bluelight/help.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_MSG_TMPLT_LOC', '01', 'component://bluelight/includes/messages.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_NAME', '01', 'BLUELIGHT', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_NAV_CLOSE_TMPLT', '01', 'component://bluelight/includes/appbarClose.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_NAV_OPEN_TMPLT', '01', 'component://bluelight/includes/appbarOpen.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_SCREENSHOT', '01', '/bluelight/screenshot.jpg', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_SHORTCUT_ICON', '01', '/images/ofbiz.ico', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('BLUELIGHT', 'VT_STYLESHEET', '01', '/bluelight/style.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_DOCBOOKSTYLESHEET', '01', '/droppingcrumbs/webapp/droppingcrumbs/css/docbook.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_FTR_TMPLT_LOC', '01', 'component://droppingcrumbs/includes/footer.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_HDR_IMAGE_URL', '01', '/images/ofbiz_logo.gif', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_HDR_JAVASCRIPT', '01', '/droppingcrumbs/js/dropdown.js', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_HDR_TMPLT_LOC', '01', 'component://droppingcrumbs/includes/header.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_HELPSTYLESHEET', '01', '/droppingcrumbs/css/help.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_MSG_TMPLT_LOC', '01', 'component://common/webcommon/includes/messages.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_NAME', '01', 'DROPPINGCRUMBS', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_NAV_CLOSE_TMPLT', '01', 'component://droppingcrumbs/includes/appbarClose.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_NAV_OPEN_TMPLT', '01', 'component://droppingcrumbs/includes/appbarOpen.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_SCREENSHOT', '01', '/droppingcrumbs/screenshot.jpg', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_SHORTCUT_ICON', '01', '/images/ofbiz.ico', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('DROPPINGCRUMBS', 'VT_STYLESHEET', '01', '/droppingcrumbs/css/style.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_DOCBOOKSTYLESHEET', '01', '/flatgrey/webapp/flatgrey/docbook.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_FTR_TMPLT_LOC', '01', 'component://flatgrey/includes/footer.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_HDR_IMAGE_URL', '01', '/flatgrey/images/ofbiz_logo.gif', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_HDR_JAVASCRIPT', '01', '/flatgrey/js/application.js', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_HDR_TMPLT_LOC', '01', 'component://flatgrey/includes/header.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_HELPSTYLESHEET', '01', '/flatgrey/help.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_MSG_TMPLT_LOC', '01', 'component://common/webcommon/includes/messages.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_NAME', '01', 'FLAT_GREY', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_NAV_TMPLT_LOC', '01', 'component://flatgrey/includes/appbar.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_RTL_STYLESHEET', '01', '/flatgrey/mainrtl.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_SCREENSHOT', '01', '/flatgrey/screenshot.jpg', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_SHORTCUT_ICON', '01', '/images/ofbiz.ico', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_STYLESHEET', '01', '/flatgrey/maincss.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('FLAT_GREY', 'VT_STYLESHEET', '02', '/flatgrey/javascript.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_FTR_TMPLT_LOC', '01', 'component://multiflex/includes/footer.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_HDR_IMAGE_URL', '01', '/images/ofbiz_logo.gif', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_HDR_TMPLT_LOC', '01', 'component://multiflex/includes/header.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_SCREENSHOT', '01', '/multiflex/screenshot.jpg', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_SHORTCUT_ICON', '01', '/images/ofbiz.ico', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_STYLESHEET', '01', '/ecommerce/images/blog.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_STYLESHEET', '02', '/multiflex/style.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('MULTIFLEX', 'VT_STYLESHEET', '04', '/content/images/contentForum.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_DOCBOOKSTYLESHEET', '01', '/tomahawk/webapp/tomahawk/css/docbook.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_FTR_TMPLT_LOC', '01', 'component://tomahawk/includes/footer.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_HDR_IMAGE_URL', '01', '/images/ofbiz_logo.gif', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_HDR_JAVASCRIPT', '01', '/tomahawk/js/dropdown.js', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_HDR_TMPLT_LOC', '01', 'component://tomahawk/includes/header.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_HELPSTYLESHEET', '01', '/tomahawk/css/help.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_MSG_TMPLT_LOC', '01', 'component://common/webcommon/includes/messages.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_NAME', '01', 'TOMAHAWK', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_NAV_CLOSE_TMPLT', '01', 'component://tomahawk/includes/appbarClose.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_NAV_OPEN_TMPLT', '01', 'component://tomahawk/includes/appbarOpen.ftl', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_SCREENSHOT', '01', '/tomahawk/screenshot.jpg', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_SHORTCUT_ICON', '01', '/images/ofbiz.ico', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `visual_theme_resource` VALUES ('TOMAHAWK', 'VT_STYLESHEET', '01', '/tomahawk/css/style.css', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');

-- ----------------------------
-- Table structure for `visual_theme_set`
-- ----------------------------
DROP TABLE IF EXISTS `visual_theme_set`;
CREATE TABLE `visual_theme_set` (
  `VISUAL_THEME_SET_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`VISUAL_THEME_SET_ID`),
  KEY `VSL_THM_ST_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `VSL_THM_ST_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of visual_theme_set
-- ----------------------------
INSERT INTO `visual_theme_set` VALUES ('BACKOFFICE', 'Themes to be used for backoffice applications', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');
INSERT INTO `visual_theme_set` VALUES ('ECOMMERCE', 'Themes to be used for ECommerce applications', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `web_page`
-- ----------------------------
DROP TABLE IF EXISTS `web_page`;
CREATE TABLE `web_page` (
  `WEB_PAGE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `PAGE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `WEB_SITE_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`WEB_PAGE_ID`),
  KEY `WEB_PAGE_SITE` (`WEB_SITE_ID`),
  KEY `WEB_PAGE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `WEB_PAGE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `WEB_PAGE_SITE` FOREIGN KEY (`WEB_SITE_ID`) REFERENCES `web_site` (`WEB_SITE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of web_page
-- ----------------------------

-- ----------------------------
-- Table structure for `web_site`
-- ----------------------------
DROP TABLE IF EXISTS `web_site`;
CREATE TABLE `web_site` (
  `WEB_SITE_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `SITE_NAME` varchar(100) collate latin1_general_cs default NULL,
  `HTTP_HOST` varchar(255) collate latin1_general_cs default NULL,
  `HTTP_PORT` varchar(10) collate latin1_general_cs default NULL,
  `HTTPS_HOST` varchar(255) collate latin1_general_cs default NULL,
  `HTTPS_PORT` varchar(10) collate latin1_general_cs default NULL,
  `ENABLE_HTTPS` char(1) collate latin1_general_cs default NULL,
  `STANDARD_CONTENT_PREFIX` varchar(255) collate latin1_general_cs default NULL,
  `SECURE_CONTENT_PREFIX` varchar(255) collate latin1_general_cs default NULL,
  `COOKIE_DOMAIN` varchar(255) collate latin1_general_cs default NULL,
  `VISUAL_THEME_SET_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`WEB_SITE_ID`),
  KEY `WEB_SITE_THEME_SET` (`VISUAL_THEME_SET_ID`),
  KEY `WEB_SITE_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `WEB_SITE_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `WEB_SITE_THEME_SET` FOREIGN KEY (`VISUAL_THEME_SET_ID`) REFERENCES `visual_theme_set` (`VISUAL_THEME_SET_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of web_site
-- ----------------------------
INSERT INTO `web_site` VALUES ('BI', 'Business Intelligence Application', null, null, null, null, null, null, null, null, 'BACKOFFICE', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `web_site` VALUES ('EXAMPLE', 'Example Application', null, null, null, null, null, null, null, null, 'BACKOFFICE', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58', '2011-12-01 17:17:58');
INSERT INTO `web_site` VALUES ('WEBTOOLS', 'Web Tools', null, null, null, null, null, null, null, null, 'BACKOFFICE', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55', '2011-12-01 17:17:55');

-- ----------------------------
-- Table structure for `webslinger_host_mapping`
-- ----------------------------
DROP TABLE IF EXISTS `webslinger_host_mapping`;
CREATE TABLE `webslinger_host_mapping` (
  `HOST_NAME` varchar(100) collate latin1_general_cs NOT NULL,
  `CONTEXT_PATH` varchar(255) collate latin1_general_cs NOT NULL,
  `WEBSLINGER_SERVER_ID` varchar(20) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`HOST_NAME`,`CONTEXT_PATH`),
  KEY `WHM_WS` (`WEBSLINGER_SERVER_ID`),
  KEY `WBSLR_HST_MPG_TXSP` (`LAST_UPDATED_TX_STAMP`),
  KEY `WBSLR_HST_MPG_TXCS` (`CREATED_TX_STAMP`),
  CONSTRAINT `WHM_WS` FOREIGN KEY (`WEBSLINGER_SERVER_ID`) REFERENCES `webslinger_server` (`WEBSLINGER_SERVER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of webslinger_host_mapping
-- ----------------------------
INSERT INTO `webslinger_host_mapping` VALUES ('*', '/webslinger', 'WEBSLINGER', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `webslinger_host_suffix`
-- ----------------------------
DROP TABLE IF EXISTS `webslinger_host_suffix`;
CREATE TABLE `webslinger_host_suffix` (
  `HOST_SUFFIX_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `HOST_SUFFIX` varchar(100) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`HOST_SUFFIX_ID`),
  KEY `WBSLR_HST_SFX_TXSP` (`LAST_UPDATED_TX_STAMP`),
  KEY `WBSLR_HST_SFX_TXCS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of webslinger_host_suffix
-- ----------------------------
INSERT INTO `webslinger_host_suffix` VALUES ('LOCALHOST', '.localhost', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `webslinger_host_suffix` VALUES ('PREVIEW', '.preview', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `webslinger_server`
-- ----------------------------
DROP TABLE IF EXISTS `webslinger_server`;
CREATE TABLE `webslinger_server` (
  `WEBSLINGER_SERVER_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DELEGATOR_NAME` varchar(100) collate latin1_general_cs default NULL,
  `DISPATCHER_NAME` varchar(100) collate latin1_general_cs default NULL,
  `SERVER_NAME` varchar(100) collate latin1_general_cs default NULL,
  `WEB_SITE_ID` varchar(20) collate latin1_general_cs default NULL,
  `TARGET` varchar(100) collate latin1_general_cs default NULL,
  `LOAD_AT_START` char(1) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`WEBSLINGER_SERVER_ID`),
  KEY `WSS_WS` (`WEB_SITE_ID`),
  KEY `WBSLNGR_SRR_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `WBSLNGR_SRR_TXCRTS` (`CREATED_TX_STAMP`),
  CONSTRAINT `WSS_WS` FOREIGN KEY (`WEB_SITE_ID`) REFERENCES `web_site` (`WEB_SITE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of webslinger_server
-- ----------------------------
INSERT INTO `webslinger_server` VALUES ('WEBSLINGER', 'default', 'webslinger', 'webslinger', null, 'ofbiz-component://webslinger/websites/webslinger', null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `webslinger_server_base`
-- ----------------------------
DROP TABLE IF EXISTS `webslinger_server_base`;
CREATE TABLE `webslinger_server_base` (
  `WEBSLINGER_SERVER_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `BASE_NAME` varchar(100) collate latin1_general_cs NOT NULL,
  `SEQ_NUM` decimal(20,0) default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`WEBSLINGER_SERVER_ID`,`BASE_NAME`),
  KEY `WSB_WS` (`WEBSLINGER_SERVER_ID`),
  KEY `WBSLR_SRR_BS_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `WBSLR_SRR_BS_TXCRS` (`CREATED_TX_STAMP`),
  CONSTRAINT `WSB_WS` FOREIGN KEY (`WEBSLINGER_SERVER_ID`) REFERENCES `webslinger_server` (`WEBSLINGER_SERVER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of webslinger_server_base
-- ----------------------------

-- ----------------------------
-- Table structure for `x509_issuer_provision`
-- ----------------------------
DROP TABLE IF EXISTS `x509_issuer_provision`;
CREATE TABLE `x509_issuer_provision` (
  `CERT_PROVISION_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `COMMON_NAME` varchar(255) collate latin1_general_cs default NULL,
  `ORGANIZATIONAL_UNIT` varchar(255) collate latin1_general_cs default NULL,
  `ORGANIZATION_NAME` varchar(255) collate latin1_general_cs default NULL,
  `CITY_LOCALITY` varchar(255) collate latin1_general_cs default NULL,
  `STATE_PROVINCE` varchar(255) collate latin1_general_cs default NULL,
  `COUNTRY` varchar(255) collate latin1_general_cs default NULL,
  `SERIAL_NUMBER` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`CERT_PROVISION_ID`),
  KEY `X59_ISR_PRVN_TXSTP` (`LAST_UPDATED_TX_STAMP`),
  KEY `X59_ISR_PRVN_TXCRS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of x509_issuer_provision
-- ----------------------------
