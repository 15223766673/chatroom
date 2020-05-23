/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : chatroom

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2020-05-23 01:54:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `friends`
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `friend_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('1', '761702168', '761702167');
INSERT INTO `friends` VALUES ('2', '761702167', '761702168');

-- ----------------------------
-- Table structure for `groupchat`
-- ----------------------------
DROP TABLE IF EXISTS `groupchat`;
CREATE TABLE `groupchat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `groupchat_builderid` int NOT NULL,
  `groupchat_id` int NOT NULL,
  `groupchat_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of groupchat
-- ----------------------------
INSERT INTO `groupchat` VALUES ('1', '761702167', '123', '654');

-- ----------------------------
-- Table structure for `groupchatusers`
-- ----------------------------
DROP TABLE IF EXISTS `groupchatusers`;
CREATE TABLE `groupchatusers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `groupchatid` int NOT NULL,
  `userid` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of groupchatusers
-- ----------------------------
INSERT INTO `groupchatusers` VALUES ('1', '123', '761702167');
INSERT INTO `groupchatusers` VALUES ('2', '123', '761702168');
INSERT INTO `groupchatusers` VALUES ('3', '123', '761702166');

-- ----------------------------
-- Table structure for `user_tbl`
-- ----------------------------
DROP TABLE IF EXISTS `user_tbl`;
CREATE TABLE `user_tbl` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '鏃犲悕姘?',
  `password` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=761702169 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user_tbl
-- ----------------------------
INSERT INTO `user_tbl` VALUES ('761702166', '田洲', '123');
INSERT INTO `user_tbl` VALUES ('761702167', '123', '123');
INSERT INTO `user_tbl` VALUES ('761702168', '123', '123');
