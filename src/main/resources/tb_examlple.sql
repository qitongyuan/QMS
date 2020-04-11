/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : vblog

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2020-04-11 09:24:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `ancestors` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- ----------------------------
-- Records of tb_dept
-- ----------------------------
INSERT INTO `tb_dept` VALUES ('1', '0', '集团总部', '0', '0', '0');
INSERT INTO `tb_dept` VALUES ('2', '1', '互联网技术部', '1', '0', '0,1');
INSERT INTO `tb_dept` VALUES ('3', '1', '销售部', '2', '0', '0,1');
INSERT INTO `tb_dept` VALUES ('4', '3', '市场调研部', '0', '0', '0,1,3');
INSERT INTO `tb_dept` VALUES ('5', '3', '销售中心办公室', '1', '0', '0,1,3');
INSERT INTO `tb_dept` VALUES ('6', '2', '开发部', '0', '0', '0,1,2');
INSERT INTO `tb_dept` VALUES ('7', '2', '测试部', '1', '0', '0,1,2');
INSERT INTO `tb_dept` VALUES ('8', '2', '运维部', '2', '0', '0,1,2');
INSERT INTO `tb_dept` VALUES ('9', '2', '产品部', '3', '0', '0,1,2');
INSERT INTO `tb_dept` VALUES ('10', '1', '财务部', '0', '0', '0,1');
INSERT INTO `tb_dept` VALUES ('11', '10', '会计部', '0', '0', '0,1,10');
INSERT INTO `tb_dept` VALUES ('12', '10', '审计部', '1', '0', '0,1,10');
INSERT INTO `tb_dept` VALUES ('13', '10', '税务部', '2', '0', '0,1,10');
INSERT INTO `tb_dept` VALUES ('15', '14', '测试1', '0', '-1', null);
INSERT INTO `tb_dept` VALUES ('16', '14', '测试2', '1', '-1', null);
INSERT INTO `tb_dept` VALUES ('17', '11', '测试部门1', '1', '-1', null);
INSERT INTO `tb_dept` VALUES ('18', '15', '1', '0', '-1', null);
INSERT INTO `tb_dept` VALUES ('19', '10', '1', '0', '-1', null);
INSERT INTO `tb_dept` VALUES ('20', '2', '总监部2', '1', '-1', null);
INSERT INTO `tb_dept` VALUES ('21', '10', '总经理办公室', '3', '0', '0,1,10');
INSERT INTO `tb_dept` VALUES ('22', '10', '财务1部', '12', '-1', '');
INSERT INTO `tb_dept` VALUES ('23', '6', '开发一部', '1', '0', '0,1,2,6');
INSERT INTO `tb_dept` VALUES ('24', '6', '开发二部', '2', '0', '0,1,2,6');
INSERT INTO `tb_dept` VALUES ('25', '23', '前端部', '1', '0', '0,1,2,6,23');
INSERT INTO `tb_dept` VALUES ('26', '23', '后端部', '1', '0', '0,1,2,6,23');
INSERT INTO `tb_dept` VALUES ('27', '24', '前端部', '0', '0', '0,1,2,6,24');
INSERT INTO `tb_dept` VALUES ('28', '24', '测试部', '1', '0', '0,1,2,6,24');

-- ----------------------------
-- Table structure for tb_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict`;
CREATE TABLE `tb_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- ----------------------------
-- Records of tb_dict
-- ----------------------------
INSERT INTO `tb_dict` VALUES ('4', '博客类型', 'blogType', 'blogCode', '1', '1', '', '0');
INSERT INTO `tb_dict` VALUES ('5', '博客类型', 'blogType', 'blogCodeV2', '博客编码V2', '2', null, '0');
INSERT INTO `tb_dict` VALUES ('6', '商品类型', 'itemType', 'itemType', '婴儿', '1', '商品类型', '0');
INSERT INTO `tb_dict` VALUES ('7', '商品类型', 'itemType', 'itemType2', '洗发用品', '2', '商品类型', '0');

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES ('1', '0', '分类管理查询列表权限', null, 'sort:manage:list', null, '', null);
INSERT INTO `tb_menu` VALUES ('61', '0', '分类管理根据id查询权限', null, 'sort:manage:getone', null, null, null);

-- ----------------------------
-- Table structure for tb_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_post`;
CREATE TABLE `tb_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `order_num` int(4) NOT NULL DEFAULT '0' COMMENT '排序号',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态（1正常 0停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`),
  UNIQUE KEY `idx_post_code` (`post_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='岗位信息表';

-- ----------------------------
-- Records of tb_post
-- ----------------------------
INSERT INTO `tb_post` VALUES ('1', 'Rank1001', '董事长', '0', '1', '2019-07-22 23:16:28', null, '董事长');
INSERT INTO `tb_post` VALUES ('2', 'Rank1002', '总经理', '1', '1', '2019-07-22 23:17:30', null, '总经理');
INSERT INTO `tb_post` VALUES ('3', 'Rank1003', '互联网技术总监', '2', '1', '2019-07-23 09:25:38', null, '互联网技术总监');
INSERT INTO `tb_post` VALUES ('4', 'Rank1004', '财务总监', '3', '1', '2019-07-23 09:25:55', null, '财务总监');
INSERT INTO `tb_post` VALUES ('5', 'Rank1005', '技术主管', '4', '1', '2019-07-23 09:26:17', null, '技术主管');
INSERT INTO `tb_post` VALUES ('6', 'Rank1006', '产品经理', '5', '1', '2019-07-23 09:27:02', null, '产品经理');
INSERT INTO `tb_post` VALUES ('7', 'Rank1007', '开发部经理', '6', '1', '2019-07-23 09:27:14', null, '开发部经理');
INSERT INTO `tb_post` VALUES ('8', 'Rank1008', '运维主管', '7', '1', '2019-07-23 09:27:23', null, '运维主管');
INSERT INTO `tb_post` VALUES ('9', 'Rank1009', '高级开发工程师', '8', '1', '2019-07-23 09:27:35', null, '高级开发工程师');
INSERT INTO `tb_post` VALUES ('10', 'R1010', 'HRBP', '9', '1', '2019-07-23 09:27:52', '2019-07-23 10:01:10', 'HRBP');

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `data_scope` char(255) DEFAULT NULL COMMENT '数据权限',
  `role_key` varchar(100) DEFAULT NULL COMMENT '权限标识',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1246330568637468677 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', '管理员', '管理员', '2019-02-22 14:29:45', '1', 'admin');
INSERT INTO `tb_role` VALUES ('2', '部门经理', '部门经理', '2020-04-01 13:57:00', '2', 'manage');
INSERT INTO `tb_role` VALUES ('1246330568637468674', '测试角色', '测试角色', '2020-04-04 06:55:37', '3', 'test');
INSERT INTO `tb_role` VALUES ('1246330568637468675', '角色代表', '角色代表', '2020-04-04 07:00:04', '3', 'roleone');
INSERT INTO `tb_role` VALUES ('1246330568637468676', '角色代表2', '角色代表2', '2020-04-04 07:04:56', '3', 'roleone2');

-- ----------------------------
-- Table structure for tb_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_dept`;
CREATE TABLE `tb_role_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=285 DEFAULT CHARSET=utf8 COMMENT='角色与部门对应关系';

-- ----------------------------
-- Records of tb_role_dept
-- ----------------------------
INSERT INTO `tb_role_dept` VALUES ('283', '2', '6');
INSERT INTO `tb_role_dept` VALUES ('284', '2', '7');

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=657 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of tb_role_menu
-- ----------------------------
INSERT INTO `tb_role_menu` VALUES ('650', '1', '1');
INSERT INTO `tb_role_menu` VALUES ('651', '1246330568637468674', '1');
INSERT INTO `tb_role_menu` VALUES ('652', '1246330568637468674', '2');
INSERT INTO `tb_role_menu` VALUES ('653', '1246330568637468675', '1');
INSERT INTO `tb_role_menu` VALUES ('654', '1246330568637468675', '2');
INSERT INTO `tb_role_menu` VALUES ('655', '1246330568637468676', '1');
INSERT INTO `tb_role_menu` VALUES ('656', '1246330568637468676', '2');

-- ----------------------------
-- Table structure for tb_sorts
-- ----------------------------
DROP TABLE IF EXISTS `tb_sorts`;
CREATE TABLE `tb_sorts` (
  `sort_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `sort_name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort_alias` varchar(15) DEFAULT NULL COMMENT '分类别名',
  `sort_description` text NOT NULL COMMENT '分类描述',
  `parent_sort_id` bigint(20) NOT NULL COMMENT '父分类ID',
  `tenant_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sort_id`),
  KEY `sort_name` (`sort_name`),
  KEY `sort_alias` (`sort_alias`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_sorts
-- ----------------------------
INSERT INTO `tb_sorts` VALUES ('6', '分类1', '分类1', '分类1', '0', '000000');
INSERT INTO `tb_sorts` VALUES ('7', '分类2', '分类2', '分类2', '0', null);
INSERT INTO `tb_sorts` VALUES ('8', '分类3', '分类3', '分类3', '0', null);
INSERT INTO `tb_sorts` VALUES ('9', '分类4', '分类4', '分类4', '0', '000000');
INSERT INTO `tb_sorts` VALUES ('10', '分类5', '分类5', '分类5', '0', null);
INSERT INTO `tb_sorts` VALUES ('11', 'test', 'test', 'test', '0', null);

-- ----------------------------
-- Table structure for tb_users
-- ----------------------------
DROP TABLE IF EXISTS `tb_users`;
CREATE TABLE `tb_users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_ip` varchar(20) NOT NULL COMMENT '用户IP',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `user_password` varchar(100) NOT NULL COMMENT '用户密码',
  `salt` varchar(50) DEFAULT NULL,
  `user_email` varchar(30) NOT NULL COMMENT '用户邮箱',
  `user_profile_photo` varchar(255) NOT NULL COMMENT '用户头像',
  `user_registration_time` datetime DEFAULT NULL COMMENT '注册时间',
  `user_birthday` date DEFAULT NULL COMMENT '用户生日',
  `user_age` tinyint(4) DEFAULT NULL COMMENT '用户年龄',
  `user_telephone_number` varchar(11) NOT NULL COMMENT '用户手机号',
  `user_nickname` varchar(20) NOT NULL COMMENT '用户昵称',
  `dept_id` bigint(20) DEFAULT NULL,
  `del_flag` char(255) DEFAULT NULL COMMENT '是否删除（0代表未删除 1代表已删除）',
  `tenant_id` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`user_id`),
  KEY `user_name` (`user_name`),
  KEY `user_nickname` (`user_nickname`),
  KEY `user_email` (`user_email`),
  KEY `user_telephone_number` (`user_telephone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_users
-- ----------------------------
INSERT INTO `tb_users` VALUES ('2', '127.0.0.1', 'admin', '5ccbc953700336b3e53608fc05eb78ff97471d58918ae2b663e8ed1d86e00797', '1TsRU8nItmj4HvujFlxY', 'admin@qq.com', '123', '2020-02-01 16:45:21', '1995-03-10', '25', '1500000000', 'qty', '9', '0', '000000');

-- ----------------------------
-- Table structure for tb_user_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_post`;
CREATE TABLE `tb_user_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户Id',
  `post_id` bigint(20) NOT NULL COMMENT '岗位Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of tb_user_post
-- ----------------------------
INSERT INTO `tb_user_post` VALUES ('1', '2', '1');
INSERT INTO `tb_user_post` VALUES ('2', '2', '2');
INSERT INTO `tb_user_post` VALUES ('13', '26', '10');
INSERT INTO `tb_user_post` VALUES ('42', '6', '4');

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES ('69', '1', '1');
INSERT INTO `tb_user_role` VALUES ('70', '2', '2');
