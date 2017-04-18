/*
Navicat MySQL Data Transfer

Source Server         : 本地Mysql
Source Server Version : 50554
Source Host           : 127.0.0.1:3307
Source Database       : master

Target Server Type    : MYSQL
Target Server Version : 50554
File Encoding         : 65001

Date: 2017-04-18 16:38:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `city_id` varchar(6) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for country
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `country_id` char(6) NOT NULL,
  `cn` varchar(48) DEFAULT NULL,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ip
-- ----------------------------
DROP TABLE IF EXISTS `ip`;
CREATE TABLE `ip` (
  `id` varchar(15) NOT NULL,
  `country_id` char(6) DEFAULT NULL,
  `region_id` char(6) DEFAULT NULL,
  `city_id` char(6) DEFAULT NULL,
  `isp` int(7) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `status` tinyint(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region` (
  `region_id` char(6) NOT NULL,
  `region` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sp
-- ----------------------------
DROP TABLE IF EXISTS `sp`;
CREATE TABLE `sp` (
  `sp_id` int(7) NOT NULL,
  `sp_name` char(48) DEFAULT NULL,
  PRIMARY KEY (`sp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tao_bao_area
-- ----------------------------
DROP TABLE IF EXISTS `tao_bao_area`;
CREATE TABLE `tao_bao_area` (
  `id` varchar(32) NOT NULL,
  `country` varchar(50) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `region` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `county` varchar(50) DEFAULT NULL,
  `isp` varchar(10) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `country_id` varchar(50) DEFAULT NULL,
  `region_id` varchar(50) DEFAULT NULL,
  `city_id` varchar(50) DEFAULT NULL,
  `county_id` varchar(50) DEFAULT NULL,
  `isp_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
