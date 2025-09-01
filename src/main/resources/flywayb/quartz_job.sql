/*
 Navicat Premium Data Transfer

 Source Server         : 德讯测试
 Source Server Type    : MySQL
 Source Server Version : 50743 (5.7.43-log)
 Source Host           : localhost:3306
 Source Schema         : kaolaim

 Target Server Type    : MySQL
 Target Server Version : 50743 (5.7.43-log)
 File Encoding         : 65001

 Date: 01/09/2025 21:34:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `quartz_job`;
CREATE TABLE `quartz_job`  (
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `invoke_target` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '状态（Y正常N暂停）',
  PRIMARY KEY (`job_id`, `job_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quartz_job
-- ----------------------------
INSERT INTO `quartz_job` VALUES (1793574396027799001, '钱包任务', 'walletTaskService.task()', '0 0/5 * * * ? *', 'Y');
INSERT INTO `quartz_job` VALUES (1793574396027799002, '用户日活', 'chatTaskService.visit()', '0 0 3 * * ? *', 'Y');
INSERT INTO `quartz_job` VALUES (1793574396027799003, '用户解封', 'chatTaskService.banned()', '0 0/10 * * * ? *', 'Y');
INSERT INTO `quartz_job` VALUES (1793574396027799004, '群组降级', 'chatTaskService.level()', '0 0 5 * * ? *', 'Y');
INSERT INTO `quartz_job` VALUES (1793574396027799005, '钱包补偿', 'walletReceiveService.task()', '0 0/5 * * * ? *', 'Y');
INSERT INTO `quartz_job` VALUES (1793574396027799006, '删除日志', 'chatTaskService.dellogs()', '0 30 3 * * ?', 'Y');

SET FOREIGN_KEY_CHECKS = 1;
