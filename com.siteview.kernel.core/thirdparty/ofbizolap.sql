/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : ofbizolap

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2011-12-01 17:18:38
*/

drop database IF EXISTS OFBIZOLAP;

CREATE DATABASE IF NOT EXISTS OFBIZOLAP;

USE OFBIZOLAP;

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `currency_dimension`
-- ----------------------------
DROP TABLE IF EXISTS `currency_dimension`;
CREATE TABLE `currency_dimension` (
  `DIMENSION_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `CURRENCY_ID` varchar(20) collate latin1_general_cs default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`DIMENSION_ID`),
  KEY `CRRNC_DMNSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `CRRNC_DMNSN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of currency_dimension
-- ----------------------------
INSERT INTO `currency_dimension` VALUES ('_NA_', null, 'Currency Not Set.', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `currency_dimension` VALUES ('_NF_', null, 'Currency Not Found.', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');

-- ----------------------------
-- Table structure for `date_dimension`
-- ----------------------------
DROP TABLE IF EXISTS `date_dimension`;
CREATE TABLE `date_dimension` (
  `DIMENSION_ID` varchar(20) collate latin1_general_cs NOT NULL,
  `DATE_VALUE` date default NULL,
  `DESCRIPTION` varchar(255) collate latin1_general_cs default NULL,
  `DAY_NAME` varchar(60) collate latin1_general_cs default NULL,
  `DAY_OF_MONTH` decimal(20,0) default NULL,
  `DAY_OF_YEAR` decimal(20,0) default NULL,
  `MONTH_NAME` varchar(60) collate latin1_general_cs default NULL,
  `MONTH_OF_YEAR` decimal(20,0) default NULL,
  `YEAR_NAME` decimal(20,0) default NULL,
  `WEEK_OF_MONTH` decimal(20,0) default NULL,
  `WEEK_OF_YEAR` decimal(20,0) default NULL,
  `YEAR_MONTH_DAY` varchar(60) collate latin1_general_cs default NULL,
  `YEAR_AND_MONTH` varchar(60) collate latin1_general_cs default NULL,
  `WEEKDAY_TYPE` varchar(60) collate latin1_general_cs default NULL,
  `LAST_UPDATED_STAMP` datetime default NULL,
  `LAST_UPDATED_TX_STAMP` datetime default NULL,
  `CREATED_STAMP` datetime default NULL,
  `CREATED_TX_STAMP` datetime default NULL,
  PRIMARY KEY  (`DIMENSION_ID`),
  KEY `DT_DMNSN_TXSTMP` (`LAST_UPDATED_TX_STAMP`),
  KEY `DT_DMNSN_TXCRTS` (`CREATED_TX_STAMP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

-- ----------------------------
-- Records of date_dimension
-- ----------------------------
INSERT INTO `date_dimension` VALUES ('_NA_', null, 'Date Not Set.', null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
INSERT INTO `date_dimension` VALUES ('_NF_', null, 'Date Not Found.', null, null, null, null, null, null, null, null, null, null, null, '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57', '2011-12-01 17:17:57');
