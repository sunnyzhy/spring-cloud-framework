/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 80027
Source Host           : 127.0.0.1:3306
Source Database       : z_base

Target Server Type    : MYSQL
Target Server Version : 80027
File Encoding         : 65001

Date: 2021-11-30 14:11:24
*/

SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `z_base`;

CREATE DATABASE `z_base` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `z_base`;

-- ----------------------------
-- Table structure for base_data_group
-- ----------------------------
DROP TABLE IF EXISTS `base_data_group`;
CREATE TABLE `base_data_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sort` bigint DEFAULT '0' COMMENT '分组排序的序号',
  `name` varchar(255) DEFAULT NULL,
  `parent_id` int NOT NULL DEFAULT '-1',
  `type` char(1) DEFAULT NULL COMMENT '保留字段，暂无使用',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `description` varchar(255) DEFAULT NULL,
  `tenant_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `upd_user` varchar(255) DEFAULT NULL,
  `upd_name` varchar(255) DEFAULT NULL,
  `upd_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_tid_type` (`tenant_id`) USING BTREE,
  KEY `i_tid_pid` (`tenant_id`,`parent_id`,`name`) USING BTREE,
  KEY `i_tid_name` (`tenant_id`,`name`),
  KEY `i_tid` (`tenant_id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_data_group
-- ----------------------------

-- ----------------------------
-- Table structure for base_data_group_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_data_group_authority`;
CREATE TABLE `base_data_group_authority` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `data_group_id` int NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_tid_rid_gid` (`role_id`,`data_group_id`) USING BTREE COMMENT 'desc:用于把授权节点设置为selected的记录'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_data_group_authority
-- ----------------------------

-- ----------------------------
-- Table structure for base_data_group_type
-- ----------------------------
DROP TABLE IF EXISTS `base_data_group_type`;
CREATE TABLE `base_data_group_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_data_group_type
-- ----------------------------

-- ----------------------------
-- Table structure for base_data_group_user
-- ----------------------------
DROP TABLE IF EXISTS `base_data_group_user`;
CREATE TABLE `base_data_group_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data_group_id` int NOT NULL,
  `user_id` int NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_tid_gid_uid` (`data_group_id`,`user_id`) USING BTREE COMMENT '查询用户，内层获取分页最小用户ID',
  KEY `i_tid_uid_gid` (`user_id`,`data_group_id`) USING BTREE COMMENT '查询用户，外层分页跳转使用',
  KEY `i_uid` (`user_id`) USING BTREE COMMENT '通过uid快速查找匹配的分组数据,通过角色查找用户时使用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_data_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for base_element
-- ----------------------------
DROP TABLE IF EXISTS `base_element`;
CREATE TABLE `base_element` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '资源编码',
  `type` varchar(255) DEFAULT NULL COMMENT '资源类型',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `uri` varchar(255) DEFAULT NULL,
  `menu_id` int NOT NULL COMMENT '资源关联菜单',
  `method` varchar(10) DEFAULT NULL COMMENT '资源请求类型',
  `description` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `menu_id` (`menu_id`),
  KEY `i_method_uri` (`method`,`uri`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_element
-- ----------------------------

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '路径编码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题',
  `parent_id` int NOT NULL COMMENT '父级节点ID',
  `uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源路径',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `sort` bigint NOT NULL DEFAULT '0' COMMENT '排序',
  `path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单上下级关系',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `upd_user` varchar(255) DEFAULT NULL,
  `upd_name` varchar(255) DEFAULT NULL,
  `upd_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_menu
-- ----------------------------

-- ----------------------------
-- Table structure for base_resource_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_authority`;
CREATE TABLE `base_resource_authority` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `resource_id` int NOT NULL,
  `resource_type` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_resid_rid` (`resource_id`,`role_id`,`resource_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_resource_authority
-- ----------------------------

-- ----------------------------
-- Table structure for base_role_member
-- ----------------------------
DROP TABLE IF EXISTS `base_role_member`;
CREATE TABLE `base_role_member` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `user_id` int NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_uid` (`user_id`) COMMENT '用户登录的时候，不知道租户id，所以user_id单独作为索引',
  KEY `i_tid_rid_uid` (`role_id`,`user_id`) USING BTREE COMMENT '查找角色下的用户,租户ID放最后面，因为有些查询语句没有传递租户id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_role_member
-- ----------------------------

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `tel_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `type` int DEFAULT '0' COMMENT '用户类型\r\n1:平台管理员（唯一） \r\n2:租户超级管理员（各租户只有一个） \r\n3:租户创建的子管理员',
  `status` int NOT NULL DEFAULT '0' COMMENT '0:正常；1:注销',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `tenant_id` varchar(64) NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `upd_user` varchar(255) DEFAULT NULL,
  `upd_name` varchar(255) DEFAULT NULL,
  `upd_host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `username` (`user_name`) USING BTREE COMMENT '用户登录的时候，不知道租户id，所以username单独作为索引',
  KEY `i_tid_type` (`tenant_id`,`type`),
  KEY `i_tid_name` (`tenant_id`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user` VALUES ('1', 'admin', '$2a$10$qVF14zh7oxM6TT8blY6tN.G0zGG4MOHCNzhYAoEaGt/ZNYTydBWhi', 'admin', null, null, null, null, null, '0', '0', null, '53de62d232d5489dbb20975c56b1c20a', null, null, null, null, null, null, null, null);
