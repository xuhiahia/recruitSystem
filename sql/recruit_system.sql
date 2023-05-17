/*
 Navicat Premium Data Transfer

 Source Server         : andy
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : recruit_system

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 25/04/2023 21:04:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` tinyint NOT NULL DEFAULT 2,
  `admin_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `admin_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `admin_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `admins_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES (1, 2, 'admin', '1180c8eb9fad62ed1d16bb74958a732a', '2023-04-23 01:47:07', '2023-04-23 01:47:07', NULL);

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `blog_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `blog_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `blog_images` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `blog_comments` int NULL DEFAULT 0,
  `blog_likes` int NULL DEFAULT 0,
  `blog_collections` int NULL DEFAULT 0,
  `blog_status` tinyint NULL DEFAULT 0 COMMENT '默认0-审核，1-正常，2被举报，3禁用',
  `blog_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章类型',
  `blog_shares` int NULL DEFAULT 0 COMMENT '分享数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `blog_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (2, 1649340592213209091, '我超帅的', '我帅吗', 'https://th.bing.com/th/id/OIP.36dTOhRnplOgjBMcdP4ixQHaHa?w=195&h=196&c=7&r=0&o=5&dpr=1.5&pid=1.7', 0, 1, 1, 1, '经历', 0, '2023-04-23 00:14:44', '2023-04-25 20:20:15');
INSERT INTO `blog` VALUES (3, 1649340592213209091, '测试1', '测试标题1', 'https://th.bing.com/th/id/OIP.SULrrdddaM5PvHI1iymehgHaHa?w=196&h=196&c=7&r=0&o=5&dpr=1.5&pid=1.7', 0, 0, 0, 1, '职场动态', 0, '2023-04-23 17:24:33', '2023-04-25 04:29:45');
INSERT INTO `blog` VALUES (4, 1649340592213209091, '测试2', '测试2', 'https://th.bing.com/th/id/OIP.odQern4p-X4FQNJxGTrNDQHaLq?w=115&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7', 0, 0, 0, 1, '职场动态', 0, '2023-04-23 17:28:33', '2023-04-25 04:30:36');
INSERT INTO `blog` VALUES (5, 1649340592213209091, '测试3', '测试3', 'https://th.bing.com/th/id/OIP.uVVvxDcTegpIVHNMKZdTeAHaEo?w=256&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7', 0, 0, 0, 1, '企业职位', 0, '2023-04-23 17:34:12', '2023-04-25 04:30:37');

-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `blog_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT 0 COMMENT '默认0，一级评论',
  `blog_comment_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `answer_id` bigint NULL DEFAULT NULL COMMENT '回答的用户id',
  `blog_comment_status` tinyint NULL DEFAULT 0 COMMENT '默认0 1-被举报 2-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `blog_comment_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------
INSERT INTO `blog_comment` VALUES (1, 1, 1649340592213209091, 0, '真的吗', NULL, 0, '2023-04-22 21:03:22', '2023-04-22 21:03:22');
INSERT INTO `blog_comment` VALUES (2, 1, 1649340592213209091, 0, '真的', NULL, 0, '2023-04-22 21:05:05', '2023-04-22 21:05:05');
INSERT INTO `blog_comment` VALUES (3, 1, 1649340592213209091, 0, '假的', NULL, 0, '2023-04-22 21:15:12', '2023-04-22 21:15:12');
INSERT INTO `blog_comment` VALUES (4, 1, 1649340592213209091, 0, '哈哈哈', NULL, 0, '2023-04-22 21:21:01', '2023-04-22 21:21:01');
INSERT INTO `blog_comment` VALUES (5, 1, 1649340592213209091, 0, '我是哈哈小王', 1, 0, '2023-04-22 21:29:22', '2023-04-22 21:29:22');
INSERT INTO `blog_comment` VALUES (6, 1, 1649340592213209091, 0, '你好啊', NULL, 0, '2023-04-22 21:37:49', '2023-04-22 21:37:49');
INSERT INTO `blog_comment` VALUES (7, 1, 1649340592213209091, 0, '最后一次', NULL, 0, '2023-04-22 21:49:51', '2023-04-22 21:49:51');
INSERT INTO `blog_comment` VALUES (8, 2, 1649340592213209091, 0, '个人详情页发评论测试1', NULL, 0, '2023-04-23 00:43:17', '2023-04-23 00:43:17');

-- ----------------------------
-- Table structure for chat_list
-- ----------------------------
DROP TABLE IF EXISTS `chat_list`;
CREATE TABLE `chat_list`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_chat_id` bigint NULL DEFAULT NULL,
  `send_user` bigint NULL DEFAULT NULL,
  `receive_user` bigint NULL DEFAULT NULL,
  `send_window` tinyint NULL DEFAULT 0 COMMENT '发送方是否还在窗口  默认0-不在 1-在',
  `receive_window` tinyint NULL DEFAULT 0 COMMENT '接收方是否还在窗口 默认0-不在 1-在',
  `unread` int NULL DEFAULT 0,
  `status` tinyint NULL DEFAULT 0 COMMENT '列表状态， 0不删除 1删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `chat_list_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_list
-- ----------------------------
INSERT INTO `chat_list` VALUES (1, 1, 1649340592213209091, 1649340592213209093, 0, 0, 2, 0, '2023-04-25 00:10:16', '2023-04-25 05:21:40');
INSERT INTO `chat_list` VALUES (2, 2, 1649340592213209093, 1649340592213209091, 0, 0, 1, 0, '2023-04-25 00:10:27', '2023-04-25 05:19:24');
INSERT INTO `chat_list` VALUES (3, 3, 1649340592213209093, 1649832106647412738, 0, 0, 6, 0, '2023-04-25 02:25:48', '2023-04-25 06:35:20');
INSERT INTO `chat_list` VALUES (4, 4, 1649832106647412738, 1649340592213209093, 0, 0, 0, 0, '2023-04-25 02:25:48', '2023-04-25 02:25:48');
INSERT INTO `chat_list` VALUES (5, 5, 1649340592213209091, 1649832106647412738, 0, 0, 0, 0, '2023-04-25 16:05:28', '2023-04-25 16:05:28');
INSERT INTO `chat_list` VALUES (6, 6, 1649832106647412738, 1649340592213209091, 0, 0, 0, 0, '2023-04-25 16:05:28', '2023-04-25 16:05:28');

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_chat_id` bigint NULL DEFAULT NULL,
  `send_user` bigint NULL DEFAULT NULL COMMENT '发送用户的id',
  `receive_user` bigint NULL DEFAULT NULL,
  `chat_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_latest` tinyint NULL DEFAULT NULL COMMENT '是否是最后一条消息 0是 1不是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `chat_message_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '聊天内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_message
-- ----------------------------
INSERT INTO `chat_message` VALUES (1, 2, 1649340592213209091, 1649340592213209093, 'aaaaaaaaaa', NULL, '2023-04-25 00:59:59', '2023-04-25 00:59:59');
INSERT INTO `chat_message` VALUES (2, 2, 1649340592213209091, 1649340592213209093, 'aaaaaaaaaa', NULL, '2023-04-25 01:02:22', '2023-04-25 01:02:22');
INSERT INTO `chat_message` VALUES (3, 2, 1649340592213209091, 1649340592213209093, '你好', NULL, '2023-04-25 01:06:38', '2023-04-25 01:06:38');
INSERT INTO `chat_message` VALUES (4, 2, 1649340592213209091, 1649340592213209093, '你好', NULL, '2023-04-25 01:08:45', '2023-04-25 01:08:45');
INSERT INTO `chat_message` VALUES (5, 2, 1649340592213209091, 1649340592213209093, '你好', NULL, '2023-04-25 01:09:29', '2023-04-25 01:09:29');
INSERT INTO `chat_message` VALUES (6, 2, 1649340592213209091, 1649340592213209093, '你好', NULL, '2023-04-25 01:10:33', '2023-04-25 01:10:33');
INSERT INTO `chat_message` VALUES (7, 1, 1649340592213209091, 1649340592213209093, '呼叫大王', NULL, '2023-04-25 05:19:24', '2023-04-25 05:21:09');
INSERT INTO `chat_message` VALUES (8, 2, 1649340592213209093, 1649340592213209091, '呼叫大王 我是小王', NULL, '2023-04-25 05:21:40', '2023-04-25 05:21:40');
INSERT INTO `chat_message` VALUES (9, 4, 1649832106647412738, 1649340592213209093, '你好\n', NULL, '2023-04-25 06:27:10', '2023-04-25 06:27:10');
INSERT INTO `chat_message` VALUES (10, 4, 1649832106647412738, 1649340592213209093, '你好', NULL, '2023-04-25 06:31:17', '2023-04-25 06:31:17');
INSERT INTO `chat_message` VALUES (11, 4, 1649832106647412738, 1649340592213209093, '你是谁', NULL, '2023-04-25 06:31:57', '2023-04-25 06:31:57');
INSERT INTO `chat_message` VALUES (12, 4, 1649832106647412738, 1649340592213209093, '你是', NULL, '2023-04-25 06:32:48', '2023-04-25 06:32:48');
INSERT INTO `chat_message` VALUES (13, 4, 1649832106647412738, 1649340592213209093, '你是', NULL, '2023-04-25 06:33:34', '2023-04-25 06:33:34');
INSERT INTO `chat_message` VALUES (14, 4, 1649832106647412738, 1649340592213209093, '测试', NULL, '2023-04-25 06:35:20', '2023-04-25 06:35:20');
INSERT INTO `chat_message` VALUES (15, 4, 1649832106647412738, 1649340592213209093, '你去汇总\n', NULL, '2023-04-25 06:35:44', '2023-04-25 06:35:44');
INSERT INTO `chat_message` VALUES (16, 3, 1649340592213209093, 1649832106647412738, '好的', NULL, '2023-04-25 06:35:55', '2023-04-25 06:35:55');
INSERT INTO `chat_message` VALUES (17, 6, 1649832106647412738, 1649340592213209091, '呼叫小王', NULL, '2023-04-25 20:18:23', '2023-04-25 20:18:23');
INSERT INTO `chat_message` VALUES (18, 5, 1649340592213209091, 1649832106647412738, '呼叫腾讯', NULL, '2023-04-25 20:18:50', '2023-04-25 20:18:50');

-- ----------------------------
-- Table structure for chief
-- ----------------------------
DROP TABLE IF EXISTS `chief`;
CREATE TABLE `chief`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `chief_salary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `chief_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `chief_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `chief_command` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `chief_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `chief_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `chief_hc` int NULL DEFAULT NULL COMMENT '招聘人数',
  `chief_real_hc` int NULL DEFAULT NULL,
  `chief_status` int NULL DEFAULT 0 COMMENT '0-审核 1-通过',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `chief_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '招聘岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chief
-- ----------------------------
INSERT INTO `chief` VALUES (1, 1649832106647412738, '10K', '杭州', '敲代码', 'Java', '后端开发', '00:04:00-00:17:00', 10, 0, 1, '2023-04-24 00:41:40', '2023-04-24 00:43:54');

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` tinyint NOT NULL DEFAULT 1,
  `company_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_industry` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scale` tinyint NULL DEFAULT NULL COMMENT '0-小，1-中。2-大',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_email` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_phone` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_status` tinyint NOT NULL DEFAULT 0 COMMENT '企业状态0-启用 1-停用',
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `company_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1649832106647412739 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公司' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES (1649832106647412738, 1, 'https://th.bing.com/th/id/OIP.1dz-HDAXsPXpuQuBy7aDMQHaEo?w=305&h=190&c=7&r=0&o=5&dpr=1.5&pid=1.7', '12345678912', '1180c8eb9fad62ed1d16bb74958a732a', '腾讯', 'IT', 2, '深圳', '12345678912@qq.com', '12345678912', 0, 'good', '2023-04-23 01:46:41', '2023-04-23 01:46:41', 0);

-- ----------------------------
-- Table structure for company_task
-- ----------------------------
DROP TABLE IF EXISTS `company_task`;
CREATE TABLE `company_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NULL DEFAULT NULL COMMENT '企业Id',
  `task_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目标题',
  `task_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_command` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_status` tinyint NULL DEFAULT 0 COMMENT '默认0-发布 1-停用',
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目类型',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `company_task_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业项目' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of company_task
-- ----------------------------
INSERT INTO `company_task` VALUES (1, 1649832106647412738, '偶像练习生', '唱跳rap篮球', '力争第一', 0, '实践型', '2023-04-23 02:38:11', '2023-04-23 02:38:11');

-- ----------------------------
-- Table structure for information
-- ----------------------------
DROP TABLE IF EXISTS `information`;
CREATE TABLE `information`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `information_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `information_title` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `information_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of information
-- ----------------------------
INSERT INTO `information` VALUES (1, 1649340592213209091, '你好', '你好', '2023-04-23 17:12:05', '2023-04-23 17:12:05');
INSERT INTO `information` VALUES (2, 1649340592213209091, '审核通过', '您发表的文章测试3已经通过审核', '2023-04-23 17:34:28', '2023-04-23 17:34:28');

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `note_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人描述',
  `user_id` bigint NULL DEFAULT NULL,
  `academic_degree` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `job_status` tinyint NOT NULL COMMENT '0-离校随时到岗  1-在校月内到岗  2-在校考虑机会 3-在校暂不考虑',
  `graduate_year` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `graduate_school` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note_education` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教育经历',
  `note_item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目经历',
  `note_expect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '求职期望',
  `note_work` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工作经历',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `note_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简历地址（如果有提交附件的话）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `note_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简历内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of note
-- ----------------------------
INSERT INTO `note` VALUES (1, '我很帅', 1649340592213209091, '博士', 1, '2024', '清华大学', '我很帅', '我很帅', '我很帅', '我很帅', '2023-04-21 21:30:11', '2023-04-21 21:30:11', NULL);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint NULL DEFAULT NULL,
  `notice_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `notice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `notice_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, 1, NULL, '2023-04-23 04:55:57', '2023-04-23 04:55:57', '偶像练习生招新啦');
INSERT INTO `notice` VALUES (3, 1, NULL, '2023-04-23 05:01:54', '2023-04-23 05:01:54', '测试1');
INSERT INTO `notice` VALUES (4, 1, '22222222', '2023-04-23 05:01:54', '2023-04-23 05:01:54', '测试2');
INSERT INTO `notice` VALUES (5, 1, '22222222222', '2023-04-23 05:02:13', '2023-04-23 05:02:13', '测试2');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` tinyint NULL DEFAULT 0,
  `gender` tinyint NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `avatar_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note_id` bigint NULL DEFAULT NULL,
  `is_delete` int NULL DEFAULT 0,
  `user_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1649340592213209094 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2023-04-21 17:16:52', '2023-04-21 17:16:52');
INSERT INTO `user` VALUES (1649340592213209089, 0, NULL, NULL, 'a4256a46044b1ac0b3d686ba29ed508c', '1234567896', NULL, NULL, NULL, 0, NULL, '2023-04-21 17:13:35', '2023-04-21 17:19:19');
INSERT INTO `user` VALUES (1649340592213209090, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2023-04-21 17:17:13', '2023-04-21 17:17:13');
INSERT INTO `user` VALUES (1649340592213209091, 0, 1, '哈哈小王', '1180c8eb9fad62ed1d16bb74958a732a', '12345678911', 18, 'https://th.bing.com/th/id/OIP.96i01WOj6yxp9usLYra7cAHaH1?w=175&h=186&c=7&r=0&o=5&dpr=1.5&pid=1.7', 1, 0, '20230421@qq.com', '2023-04-21 17:20:05', '2023-04-25 16:29:20');
INSERT INTO `user` VALUES (1649340592213209092, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2023-04-21 17:20:24', '2023-04-21 17:20:24');
INSERT INTO `user` VALUES (1649340592213209093, 0, 1, '哈哈大王', '1180c8eb9fad62ed1d16bb74958a732a', '1234567', 12, 'https://th.bing.com/th/id/OIP.uFWNR0wnhzObnw-E0ISFqQHaHa?w=196&h=196&c=7&r=0&o=5&dpr=1.5&pid=1.7', NULL, 0, NULL, '2023-04-25 00:02:34', '2023-04-25 04:19:26');

-- ----------------------------
-- Table structure for user_chat
-- ----------------------------
DROP TABLE IF EXISTS `user_chat`;
CREATE TABLE `user_chat`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `send_user` bigint NULL DEFAULT NULL COMMENT '发送消息的用户',
  `receive_user` bigint NULL DEFAULT NULL COMMENT '接收消息的用户',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_chat_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户聊天关系主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_chat
-- ----------------------------
INSERT INTO `user_chat` VALUES (1, 1649340592213209091, 1649340592213209093, '2023-04-25 00:03:07', '2023-04-25 00:03:07');
INSERT INTO `user_chat` VALUES (2, 1649340592213209093, 1649340592213209091, '2023-04-25 00:03:18', '2023-04-25 00:03:18');
INSERT INTO `user_chat` VALUES (3, 1649340592213209093, 1649832106647412738, '2023-04-25 02:25:48', '2023-04-25 02:25:48');
INSERT INTO `user_chat` VALUES (4, 1649832106647412738, 1649340592213209093, '2023-04-25 02:25:48', '2023-04-25 02:25:48');
INSERT INTO `user_chat` VALUES (5, 1649340592213209091, 1649832106647412738, '2023-04-25 16:05:28', '2023-04-25 16:05:28');
INSERT INTO `user_chat` VALUES (6, 1649832106647412738, 1649340592213209091, '2023-04-25 16:05:28', '2023-04-25 16:05:28');

-- ----------------------------
-- Table structure for user_chief
-- ----------------------------
DROP TABLE IF EXISTS `user_chief`;
CREATE TABLE `user_chief`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NULL DEFAULT NULL,
  `chief_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `user_chief_status` tinyint NULL DEFAULT 0 COMMENT '应聘状态0-待定 1-通过',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_chief_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-岗位对应表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_chief
-- ----------------------------
INSERT INTO `user_chief` VALUES (1, 1649832106647412738, 1, 1649340592213209091, 0, '2023-04-24 01:43:45', '2023-04-24 01:43:45');
INSERT INTO `user_chief` VALUES (2, 1649832106647412738, 1, 1649340592213209093, 0, '2023-04-25 16:27:01', '2023-04-25 16:27:01');

-- ----------------------------
-- Table structure for user_report
-- ----------------------------
DROP TABLE IF EXISTS `user_report`;
CREATE TABLE `user_report`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `blog_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_report_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户举报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_report
-- ----------------------------

-- ----------------------------
-- Table structure for user_share
-- ----------------------------
DROP TABLE IF EXISTS `user_share`;
CREATE TABLE `user_share`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `blog_id` bigint NULL DEFAULT NULL,
  `blog_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_share_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_share
-- ----------------------------
INSERT INTO `user_share` VALUES (14, 1649340592213209091, 2, '我帅吗', '2023-04-25 20:20:15', '2023-04-25 20:20:15');

-- ----------------------------
-- Table structure for user_task
-- ----------------------------
DROP TABLE IF EXISTS `user_task`;
CREATE TABLE `user_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `task_id` bigint NULL DEFAULT NULL,
  `user_submit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户传的附件',
  `user_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户评论',
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务标题',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_task_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业项目用户对应表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_task
-- ----------------------------
INSERT INTO `user_task` VALUES (1, 1649340592213209091, 1649832106647412738, 1, '你好', '你好', '实践型', '偶像练习生', '2023-04-23 22:54:27', '2023-04-23 22:54:27');

-- ----------------------------
-- Table structure for user_task_comment
-- ----------------------------
DROP TABLE IF EXISTS `user_task_comment`;
CREATE TABLE `user_task_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_comment` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `company_task_id` bigint NULL DEFAULT NULL COMMENT '企业评价',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_task_comment_id_uindex`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业项目用户评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_task_comment
-- ----------------------------
INSERT INTO `user_task_comment` VALUES (1, 1649340592213209091, '哈哈小王', NULL, NULL, NULL, '2023-04-23 22:54:52', '2023-04-23 22:54:52');
INSERT INTO `user_task_comment` VALUES (2, 1649340592213209091, '哈哈小王', NULL, NULL, NULL, '2023-04-23 22:58:14', '2023-04-23 22:58:14');
INSERT INTO `user_task_comment` VALUES (3, 1649340592213209091, '哈哈小王', NULL, '测试1', 1, '2023-04-23 22:58:36', '2023-04-23 22:58:36');

SET FOREIGN_KEY_CHECKS = 1;
