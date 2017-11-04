/*
Navicat MySQL Data Transfer

Source Server         : localhost_3307
Source Server Version : 50505
Source Host           : localhost:3307
Source Database       : paycenter

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-06-14 06:51:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `app_id` varchar(64) NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `app_key` varchar(64) NOT NULL COMMENT '唯一标示，可重新生成',
  `app_create_time` datetime NOT NULL,
  `app_update_time` datetime NOT NULL,
  `app_state` tinyint(4) NOT NULL COMMENT '1可用 2不可用 3已删除',
  `app_return_url` varchar(2000) DEFAULT NULL,
  `app_notify_url` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `app_key` (`app_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for notify
-- ----------------------------
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify` (
  `notify_id` varchar(64) NOT NULL,
  `order_record_id` varchar(64) NOT NULL,
  `notify_create_time` datetime NOT NULL,
  `notify_expire_time` datetime NOT NULL,
  `notify_type` tinyint(4) NOT NULL COMMENT '1：return 2:notify',
  `app_id` varchar(64) NOT NULL,
  `notify_result` varchar(255) DEFAULT NULL COMMENT '类型2需要得到结果',
  PRIMARY KEY (`notify_id`),
  KEY `order_record_id` (`order_record_id`),
  KEY `app_id` (`app_id`),
  CONSTRAINT `notify_ibfk_1` FOREIGN KEY (`order_record_id`) REFERENCES `order_record` (`order_record_id`),
  CONSTRAINT `notify_ibfk_2` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_goods
-- ----------------------------
DROP TABLE IF EXISTS `order_goods`;
CREATE TABLE `order_goods` (
  `order_goods_id` varchar(64) NOT NULL,
  `order_record_id` varchar(64) NOT NULL,
  `order_goods_goods_id` varchar(255) NOT NULL,
  `order_goods_name` varchar(255) NOT NULL COMMENT '商品名',
  `order_goods_price` double(20,2) NOT NULL COMMENT '价格',
  `order_goods_number` int(11) NOT NULL COMMENT '数量',
  `order_goods_detail` varchar(255) DEFAULT NULL COMMENT '商品概述',
  `order_goods_url` varchar(2000) DEFAULT NULL COMMENT '商品url',
  PRIMARY KEY (`order_goods_id`),
  UNIQUE KEY `order_record_id` (`order_record_id`,`order_goods_goods_id`) USING BTREE,
  CONSTRAINT `order_goods_ibfk_1` FOREIGN KEY (`order_record_id`) REFERENCES `order_record` (`order_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_record
-- ----------------------------
DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record` (
  `order_record_id` varchar(64) NOT NULL,
  `app_id` varchar(64) NOT NULL COMMENT '应用id',
  `order_record_order_id` varchar(255) NOT NULL COMMENT '订单id与app_id联合唯一',
  `order_record_total_price` double(20,2) NOT NULL COMMENT '总价格',
  `order_record_create_time` datetime NOT NULL,
  `order_record_update_time` datetime NOT NULL,
  `order_record_pay_status` tinyint(4) NOT NULL COMMENT '支付状态1：未支付，2已支付 3支付钱数不对',
  `order_record_status` tinyint(4) NOT NULL COMMENT '状态1：存在 2已删除',
  `order_record_user_id` varchar(255) NOT NULL COMMENT '支付者',
  `order_record_pay_channel` tinyint(4) DEFAULT NULL COMMENT '1：支付宝',
  `order_record_order_detail` mediumtext COMMENT '订单详情 json字符串',
  `order_record_user_name` varchar(255) DEFAULT NULL COMMENT '购买者名字',
  `order_record_return_url` varchar(2000) DEFAULT NULL COMMENT '优先级高于app',
  `order_record_notify_url` varchar(2000) DEFAULT NULL COMMENT '优先级高于app',
  `order_record_notify_result` tinyint(4) NOT NULL COMMENT '1：未通知，2已通知',
  `order_record_notify_time` tinyint(4) NOT NULL COMMENT '通知次数',
  `trade_no` varchar(255) DEFAULT NULL COMMENT '支付平台订单号',
  `buyer_email` varchar(255) DEFAULT NULL COMMENT '支付平台购买者email',
  `notify_time` datetime DEFAULT NULL COMMENT '支付平台通知时间',
  `buyer_id` varchar(255) DEFAULT NULL COMMENT '支付平台购买者id',
  `total_fee` double(20,2) DEFAULT NULL COMMENT '支付平台支付的钱，可能支付的不够需要退钱的记录',
  `trade_status` varchar(255) DEFAULT NULL COMMENT '支付宝的交易状态',
  PRIMARY KEY (`order_record_id`),
  UNIQUE KEY `app_id` (`app_id`,`order_record_order_id`) USING BTREE,
  UNIQUE KEY `order_record_pay_channel` (`order_record_pay_channel`,`trade_no`) USING BTREE,
  CONSTRAINT `order_record_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_notify_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_notify_log`;
CREATE TABLE `pay_notify_log` (
  `pay_notifty_log_id` varchar(255) NOT NULL,
  `order_record_id` varchar(64) DEFAULT NULL COMMENT '订单记录id，可以为空，可能是外部发的假数据',
  `pay_notify_log_body` mediumtext NOT NULL COMMENT '所有内容',
  `pay_notify_create_time` datetime NOT NULL,
  PRIMARY KEY (`pay_notifty_log_id`),
  KEY `order_record_id` (`order_record_id`),
  CONSTRAINT `pay_notify_log_ibfk_1` FOREIGN KEY (`order_record_id`) REFERENCES `order_record` (`order_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
