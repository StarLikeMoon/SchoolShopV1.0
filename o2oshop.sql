/*
Navicat MySQL Data Transfer

Source Server         : gcpo2o
Source Server Version : 50730
Source Host           : 34.96.211.30:3306
Source Database       : o2oshop

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-05-27 23:29:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_area
-- ----------------------------
DROP TABLE IF EXISTS `tb_area`;
CREATE TABLE `tb_area` (
  `area_id` int(2) NOT NULL AUTO_INCREMENT,
  `area_name` varchar(200) NOT NULL,
  `priority` int(2) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`area_id`),
  UNIQUE KEY `UK_AREA` (`area_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_area
-- ----------------------------
INSERT INTO `tb_area` VALUES ('1', '航空港北区', '1', null, null);
INSERT INTO `tb_area` VALUES ('2', '航空港南区', '2', null, null);

-- ----------------------------
-- Table structure for tb_head_line
-- ----------------------------
DROP TABLE IF EXISTS `tb_head_line`;
CREATE TABLE `tb_head_line` (
  `line_id` int(100) NOT NULL AUTO_INCREMENT,
  `line_name` varchar(1000) DEFAULT NULL,
  `line_link` varchar(2000) NOT NULL,
  `line_img` varchar(2000) NOT NULL,
  `priority` int(2) DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0' COMMENT '0：禁止使用，1：允许使用',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`line_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_head_line
-- ----------------------------
INSERT INTO `tb_head_line` VALUES ('1', 'test', 'test', '\\upload\\item\\headline\\1.jpg', '20', '1', '2020-05-18 01:09:31', null);
INSERT INTO `tb_head_line` VALUES ('2', '头条2', '头条2', '\\upload\\item\\headline\\2.jpg', '10', '1', '2020-05-18 01:37:31', null);
INSERT INTO `tb_head_line` VALUES ('3', '头条3', '头条3', '\\upload\\item\\headline\\3.jpg', '0', '0', '2020-05-18 01:37:45', null);

-- ----------------------------
-- Table structure for tb_local_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_local_auth`;
CREATE TABLE `tb_local_auth` (
  `local_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`local_auth_id`),
  UNIQUE KEY `uk_local_profile` (`username`),
  KEY `fk_localauth_profile` (`user_id`),
  CONSTRAINT `fk_localauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_local_auth
-- ----------------------------
INSERT INTO `tb_local_auth` VALUES ('1', '1', 'testusername', 's2e5q2l5y2b5yq5sl2qe6262q566s6sq', '2020-05-24 00:38:13', '2020-05-24 21:17:57');
INSERT INTO `tb_local_auth` VALUES ('2', '2', 'newAuth', '5526l6l5sl5252sl550b9e266s5b5q6b', null, '2020-05-24 18:09:17');

-- ----------------------------
-- Table structure for tb_person_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_person_info`;
CREATE TABLE `tb_person_info` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `profile_img` varchar(1024) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `gender` varchar(2) DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0' COMMENT '0：禁止使用，1：允许使用',
  `user_type` int(2) NOT NULL DEFAULT '1' COMMENT '1:顾客，2:商家，3:超级管理员',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_person_info
-- ----------------------------
INSERT INTO `tb_person_info` VALUES ('1', '测试', 'test', 'test', '1', '1', '2', null, null, null);
INSERT INTO `tb_person_info` VALUES ('2', 'test', 'test', 'test', '1', '1', '1', null, null, null);
INSERT INTO `tb_person_info` VALUES ('3', '张三', null, 'test', '男', '1', '2', '2020-05-24 18:51:38', '2020-05-24 18:51:38', null);

-- ----------------------------
-- Table structure for tb_product
-- ----------------------------
DROP TABLE IF EXISTS `tb_product`;
CREATE TABLE `tb_product` (
  `product_id` int(100) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(100) NOT NULL,
  `product_desc` varchar(2000) DEFAULT NULL,
  `img_addr` varchar(2000) DEFAULT '',
  `normal_price` varchar(100) DEFAULT NULL,
  `promotion_price` varchar(100) DEFAULT NULL,
  `priority` int(2) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0',
  `product_category_id` int(11) DEFAULT NULL,
  `shop_id` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  KEY `fk_product_procate` (`product_category_id`),
  KEY `fk_product_shop` (`shop_id`),
  CONSTRAINT `fk_product_procate` FOREIGN KEY (`product_category_id`) REFERENCES `tb_product_category` (`product_category_id`),
  CONSTRAINT `fk_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_product
-- ----------------------------
INSERT INTO `tb_product` VALUES ('1', '珍珠奶茶', '珍珠奶茶正在打折，期待到店消费', '\\upload\\item\\shop\\1\\2020051821324930266.jpg', '10', '5', '55', '2020-05-18 21:17:14', '2020-05-18 21:32:49', '1', '1', '1');
INSERT INTO `tb_product` VALUES ('2', '雀巢咖啡', '雀巢咖啡正在打折，欢迎到店消费', '\\upload\\item\\shop\\1\\2020051821333274722.jpg', '20', '5', '50', '2020-05-17 19:07:52', '2020-05-18 21:33:32', '1', '24', '1');

-- ----------------------------
-- Table structure for tb_product_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_product_category`;
CREATE TABLE `tb_product_category` (
  `product_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_category_name` varchar(100) NOT NULL,
  `priority` int(2) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `shop_id` int(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_category_id`),
  KEY `fk_procate_shop` (`shop_id`),
  CONSTRAINT `fk_procate_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_product_category
-- ----------------------------
INSERT INTO `tb_product_category` VALUES ('1', '奶茶', '0', null, '1');
INSERT INTO `tb_product_category` VALUES ('24', '咖啡', '10', '2020-05-17 16:33:10', '1');

-- ----------------------------
-- Table structure for tb_product_img
-- ----------------------------
DROP TABLE IF EXISTS `tb_product_img`;
CREATE TABLE `tb_product_img` (
  `product_img_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_img_addr` varchar(2000) NOT NULL,
  `product_img_desc` varchar(2000) DEFAULT NULL,
  `priority` int(2) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `product_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`product_img_id`),
  KEY `fk_proimg_product` (`product_id`),
  CONSTRAINT `fk_proimg_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_product_img
-- ----------------------------
INSERT INTO `tb_product_img` VALUES ('7', '\\upload\\item\\shop\\1\\20200518213249557980.jpg', null, null, '2020-05-18 21:32:49', '1');
INSERT INTO `tb_product_img` VALUES ('8', '\\upload\\item\\shop\\1\\20200518213249528371.jpg', null, null, '2020-05-18 21:32:49', '1');
INSERT INTO `tb_product_img` VALUES ('9', '\\upload\\item\\shop\\1\\20200518213332802140.jpg', null, null, '2020-05-18 21:33:32', '2');
INSERT INTO `tb_product_img` VALUES ('10', '\\upload\\item\\shop\\1\\20200518213332196661.jpg', null, null, '2020-05-18 21:33:32', '2');

-- ----------------------------
-- Table structure for tb_shop
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop`;
CREATE TABLE `tb_shop` (
  `shop_id` int(10) NOT NULL AUTO_INCREMENT,
  `owner_id` int(10) NOT NULL COMMENT '店铺创建人',
  `area_id` int(5) DEFAULT NULL,
  `shop_category_id` int(11) DEFAULT NULL,
  `shop_name` varchar(256) NOT NULL,
  `shop_desc` varchar(1024) DEFAULT NULL,
  `shop_addr` varchar(200) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `shop_img` varchar(1024) DEFAULT NULL,
  `priority` int(3) DEFAULT '0',
  `enable_status` int(2) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `advice` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shop_id`),
  KEY `fk_shop_area` (`area_id`),
  KEY `fk_shop_profile` (`owner_id`),
  KEY `fk_shop_shopcate` (`shop_category_id`),
  CONSTRAINT `fk_shop_area` FOREIGN KEY (`area_id`) REFERENCES `tb_area` (`area_id`),
  CONSTRAINT `fk_shop_profile` FOREIGN KEY (`owner_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_shop_shopcate` FOREIGN KEY (`shop_category_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_shop
-- ----------------------------
INSERT INTO `tb_shop` VALUES ('1', '1', '1', '1', '二哥奶茶店', '本店主营珍珠奶茶', '航空港北区2栋楼下', '123456', '\\upload\\item\\shop\\1\\2020051421402373831.jpg', null, '1', '2020-05-12 21:58:02', '2020-05-14 21:40:23', '');
INSERT INTO `tb_shop` VALUES ('2', '1', '2', '2', '二哥咖啡馆', '本店主营咖啡', '航空港南区1栋楼下', '123456', '\\upload\\item\\shop\\1\\2020051421402373831.jpg', '0', '1', '2020-05-18 15:34:29', '2020-05-18 15:34:32', null);
INSERT INTO `tb_shop` VALUES ('7', '1', '1', '9', '二哥网咖', 'i7 9900K+RTX2080Ti', '北区大门口右转50米', '123456789', '\\upload\\item\\shop\\7\\2020051817454324345.jpg', null, '1', '2020-05-18 17:45:43', '2020-05-18 17:45:43', null);
INSERT INTO `tb_shop` VALUES ('8', '1', '2', '10', '二哥电影院', '二哥电影院', '二哥电影院', '123456789', '\\upload\\item\\shop\\8\\2020051817464813044.jpg', null, '1', '2020-05-18 17:46:48', '2020-05-18 17:46:48', null);
INSERT INTO `tb_shop` VALUES ('9', '1', '2', '11', '二哥四六级辅导班', '二哥四六级辅导班', '二哥四六级辅导班', '123456789', '\\upload\\item\\shop\\9\\2020051817471938444.jpg', null, '1', '2020-05-18 17:47:19', '2020-05-18 17:47:19', null);
INSERT INTO `tb_shop` VALUES ('10', '1', '2', '12', '二哥自习室', '二哥自习室', '二哥自习室', '123456789', '\\upload\\item\\shop\\10\\2020051817474226609.jpg', null, '1', '2020-05-18 17:47:42', '2020-05-18 17:47:42', null);
INSERT INTO `tb_shop` VALUES ('11', '1', '2', '13', '二哥在线高数辅导', '二哥在线高数辅导', '二哥在线高数辅导', '123456798', '\\upload\\item\\shop\\11\\2020051817480863562.jpg', null, '1', '2020-05-18 17:48:08', '2020-05-18 17:48:08', null);
INSERT INTO `tb_shop` VALUES ('12', '1', '2', '14', '二哥书店', '二哥书店', '二哥书店', '123456789', '\\upload\\item\\shop\\12\\2020051817482860289.jpg', null, '1', '2020-05-18 17:48:28', '2020-05-18 17:48:28', null);
INSERT INTO `tb_shop` VALUES ('13', '1', '2', '15', '二哥二手服饰', '二哥二手服饰', '二哥二手服饰', '123456798', '\\upload\\item\\shop\\13\\2020051817484784060.jpg', null, '1', '2020-05-18 17:48:47', '2020-05-18 17:48:47', null);
INSERT INTO `tb_shop` VALUES ('14', '1', '2', '16', '二哥二手手机', '二哥二手手机', '二哥二手手机', '123465789', '\\upload\\item\\shop\\14\\2020051817490570897.jpg', null, '1', '2020-05-18 17:49:05', '2020-05-18 17:49:05', null);
INSERT INTO `tb_shop` VALUES ('15', '1', '2', '17', '二哥租车行', '二哥租车行', '二哥租车行', '123456798', '\\upload\\item\\shop\\15\\2020051817493126765.jpg', null, '1', '2020-05-18 17:49:31', '2020-05-18 17:49:31', null);
INSERT INTO `tb_shop` VALUES ('16', '1', '2', '18', '二哥空调租赁', '二哥空调租赁', '二哥空调租赁', '123465798', '\\upload\\item\\shop\\16\\2020051817495861732.jpg', null, '1', '2020-05-18 17:49:58', '2020-05-18 17:49:58', null);

-- ----------------------------
-- Table structure for tb_shop_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop_category`;
CREATE TABLE `tb_shop_category` (
  `shop_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_category_name` varchar(1000) NOT NULL DEFAULT '',
  `shop_category_desc` varchar(1000) DEFAULT '',
  `shop_category_img` varchar(2000) DEFAULT NULL,
  `priority` int(2) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`shop_category_id`),
  KEY `fk_shop_category_self` (`parent_id`),
  CONSTRAINT `fk_shop_category_self` FOREIGN KEY (`parent_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_shop_category
-- ----------------------------
INSERT INTO `tb_shop_category` VALUES ('1', '咖啡奶茶', '咖啡奶茶', '\\upload\\item\\shopcategory\\20200517231446354266.png', '0', '2020-05-18 14:26:20', '2020-05-18 14:26:25', '3');
INSERT INTO `tb_shop_category` VALUES ('2', '蛋糕甜点', '蛋糕甜点', '\\upload\\item\\shopcategory\\20200517231446354266.png', '0', '2020-05-18 14:27:04', '2020-05-18 14:27:06', '3');
INSERT INTO `tb_shop_category` VALUES ('3', '美食饮品', '美食饮品', '\\upload\\item\\shopcategory\\20200517231446354266.png', '0', '2020-05-18 14:26:00', '2020-05-18 14:27:09', null);
INSERT INTO `tb_shop_category` VALUES ('4', '休闲娱乐', '休闲娱乐', '\\upload\\item\\shopcategory\\20200517231446354299.png', '0', '2020-05-18 14:26:58', '2020-05-18 14:27:01', null);
INSERT INTO `tb_shop_category` VALUES ('5', '培训教育', '培训教育', '\\upload\\item\\shopcategory\\20200517231446354288.png', '0', '2020-05-18 14:27:26', '2020-05-18 14:27:29', null);
INSERT INTO `tb_shop_category` VALUES ('6', '二手市场', '二手市场', '\\upload\\item\\shopcategory\\20200517231446354277.png', '0', '2020-05-18 14:27:45', '2020-05-18 14:27:47', null);
INSERT INTO `tb_shop_category` VALUES ('7', '书籍资料', '书籍资料', '\\upload\\item\\shopcategory\\20200517231446354300.png', '0', '2020-05-18 14:28:02', '2020-05-18 14:28:04', null);
INSERT INTO `tb_shop_category` VALUES ('8', '租赁市场', '租赁市场', '\\upload\\item\\shopcategory\\20200518231446354255.png', '0', '2020-05-18 14:28:17', '2020-05-18 14:28:20', null);
INSERT INTO `tb_shop_category` VALUES ('9', '网吧游戏厅', '网吧游戏厅', '\\upload\\item\\shopcategory\\20200517231446354299.png', '0', '2020-05-18 17:22:53', '2020-05-18 17:22:58', '4');
INSERT INTO `tb_shop_category` VALUES ('10', '电影院', '电影院', '\\upload\\item\\shopcategory\\20200517231446354299.png', '0', '2020-05-18 17:40:22', '2020-05-18 17:40:24', '4');
INSERT INTO `tb_shop_category` VALUES ('11', '四六级辅导', '四六级辅导', '\\upload\\item\\shopcategory\\20200517231446354288.png', '0', '2020-05-18 17:41:21', '2020-05-18 17:41:24', '5');
INSERT INTO `tb_shop_category` VALUES ('12', '期末自习室', '期末自习室', '\\upload\\item\\shopcategory\\20200517231446354288.png', '0', '2020-05-18 17:41:43', '2020-05-18 17:41:45', '5');
INSERT INTO `tb_shop_category` VALUES ('13', '网课视频', '网课视频', '\\upload\\item\\shopcategory\\20200517231446354300.png', '0', '2020-05-18 17:42:27', '2020-05-18 17:42:29', '7');
INSERT INTO `tb_shop_category` VALUES ('14', '书本练习册', '书本练习册', '\\upload\\item\\shopcategory\\20200517231446354300.png', '0', '2020-05-18 17:42:52', '2020-05-18 17:42:55', '7');
INSERT INTO `tb_shop_category` VALUES ('15', '二手服饰箱包', '二手服饰箱包', '\\upload\\item\\shopcategory\\20200517231446354277.png', '0', '2020-05-18 17:43:16', '2020-05-18 17:43:18', '6');
INSERT INTO `tb_shop_category` VALUES ('16', '二手数码', '二手数码', '\\upload\\item\\shopcategory\\20200517231446354277.png', '0', '2020-05-18 17:43:36', '2020-05-18 17:43:39', '6');
INSERT INTO `tb_shop_category` VALUES ('17', '车辆租赁', '车辆租赁', '\\upload\\item\\shopcategory\\20200518231446354255.png', '0', '2020-05-18 17:44:05', '2020-05-18 17:44:08', '8');
INSERT INTO `tb_shop_category` VALUES ('18', '物品租赁', '物品租赁', '\\upload\\item\\shopcategory\\20200518231446354255.png', '0', '2020-05-18 17:44:36', '2020-05-18 17:44:38', '8');

-- ----------------------------
-- Table structure for tb_wechat_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_wechat_auth`;
CREATE TABLE `tb_wechat_auth` (
  `wechat_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `open_id` varchar(1024) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`wechat_auth_id`),
  UNIQUE KEY `uk_wechatauth_profile` (`open_id`) USING BTREE,
  KEY `fk_wechatauth_profile` (`user_id`),
  CONSTRAINT `fk_wechatauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_wechat_auth
-- ----------------------------
