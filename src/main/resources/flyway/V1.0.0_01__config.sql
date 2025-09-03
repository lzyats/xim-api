/*
 Navicat Premium Data Transfer

 Source Server         : 德讯测试
 Source Server Type    : MySQL
 Source Server Version : 50743 (5.7.43-log)
 Source Host           : localhost:3306
 Source Schema         : imback

 Target Server Type    : MySQL
 Target Server Version : 50743 (5.7.43-log)
 File Encoding         : 65001

 Date: 31/08/2025 20:34:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_banned
-- ----------------------------
DROP TABLE IF EXISTS `chat_banned`;
CREATE TABLE `chat_banned`  (
  `banned_id` bigint(20) NOT NULL COMMENT '封禁id',
  `banned_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封禁原因',
  `banned_time` datetime NULL DEFAULT NULL COMMENT '封禁时间',
  PRIMARY KEY (`banned_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '封禁状态' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_banned
-- ----------------------------

-- ----------------------------
-- Table structure for chat_config
-- ----------------------------
DROP TABLE IF EXISTS `chat_config`;
CREATE TABLE `chat_config`  (
  `config_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'key',
  `config_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'value',
  `remark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_key`) USING BTREE,
  UNIQUE INDEX `idx_key`(`config_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_config
-- ----------------------------
INSERT INTO `chat_config` VALUES ('apply_friend', '50', '申请好友单日次数');
INSERT INTO `chat_config` VALUES ('apply_group', '30', '申请群组单日次数');
INSERT INTO `chat_config` VALUES ('group_level_count', '2000', '群组成员默认数量');
INSERT INTO `chat_config` VALUES ('group_name_search', 'N', '群组名称搜索开关');
INSERT INTO `chat_config` VALUES ('notice_content', '<p>请不要发布任何反动、色情信息，否则系统将查封账号，并将相关信息上报公安机关。</p><p>近期诈骗犯罪案件时有发生，为防止您经济蒙受损失，收到以熟人名义要求转账、汇款时，务必提高警惕以防上当受骗！</p><p class=\"ql-align-center\"><img src=\"http://lxim.oss-cn-shenzhen.aliyuncs.com/alpaca/202508/23/12/68a93e1046d801752d24aef7.png\"></p>', '系统通告');
INSERT INTO `chat_config` VALUES ('notice_notype', '1', '系统公告类型');
INSERT INTO `chat_config` VALUES ('notice_status', 'Y', '系统通告开关');
INSERT INTO `chat_config` VALUES ('sys_audit', 'N', '审核开关');
INSERT INTO `chat_config` VALUES ('sys_beian', '我是备案信息', '备案信息');
INSERT INTO `chat_config` VALUES ('sys_captcha', '4321', '系统验证码');
INSERT INTO `chat_config` VALUES ('sys_cashname', '元', '货币单位');
INSERT INTO `chat_config` VALUES ('sys_cashstr', '￥', ' 货币单位');
INSERT INTO `chat_config` VALUES ('sys_friends', '25158309', ' 批量好友ID');
INSERT INTO `chat_config` VALUES ('sys_hook', '', 'WebHook地址');
INSERT INTO `chat_config` VALUES ('sys_invo', '10', '推荐单个用户奖励');
INSERT INTO `chat_config` VALUES ('sys_invoadus', 'Y', '推荐自动加推荐人为好友开关');
INSERT INTO `chat_config` VALUES ('sys_msgtodb', '1', ' 消息存储方式');
INSERT INTO `chat_config` VALUES ('sys_nickname', 'XIM', '注册昵称');
INSERT INTO `chat_config` VALUES ('sys_packet', '200', '红包金额');
INSERT INTO `chat_config` VALUES ('sys_phone', '13800000000', '审核账号');
INSERT INTO `chat_config` VALUES ('sys_project', 'XIM', '系统名称');
INSERT INTO `chat_config` VALUES ('sys_recall', '15', '撤回时间');
INSERT INTO `chat_config` VALUES ('sys_screenshot', 'Y', '系统截屏');
INSERT INTO `chat_config` VALUES ('sys_sendmoment', 'Y', '是否补发朋友圈');
INSERT INTO `chat_config` VALUES ('sys_share', 'https://www.baidu.com', '分享页面');
INSERT INTO `chat_config` VALUES ('sys_sign', '0.1', ' 签到日奖励');
INSERT INTO `chat_config` VALUES ('sys_signtoal', 'Y', ' 签到奖励入钱包');
INSERT INTO `chat_config` VALUES ('sys_watermark', '', '水印页面');
INSERT INTO `chat_config` VALUES ('user_deleted', '7', '用户注销间隔');
INSERT INTO `chat_config` VALUES ('user_hold', 'N', '用户手持开关');
INSERT INTO `chat_config` VALUES ('user_register', 'Y', '用户注册开关');
INSERT INTO `chat_config` VALUES ('user_sms', 'N', '用户短信开关');
INSERT INTO `chat_config` VALUES ('wallet_cash_auth', 'Y', '钱包提现认证开关');
INSERT INTO `chat_config` VALUES ('wallet_cash_cost', '0', '钱包提现加成金额');
INSERT INTO `chat_config` VALUES ('wallet_cash_count', '5', '钱包提现单日次数');
INSERT INTO `chat_config` VALUES ('wallet_cash_max', '5000', '钱包提现单日最大金额');
INSERT INTO `chat_config` VALUES ('wallet_cash_min', '50', '钱包提现单日最小金额');
INSERT INTO `chat_config` VALUES ('wallet_cash_rate', '0', '钱包提现手续费比率');
INSERT INTO `chat_config` VALUES ('wallet_cash_rates', '7.09', '美元汇率');
INSERT INTO `chat_config` VALUES ('wallet_cash_remark', '预计3个工作日内处理，有任何问题可随时咨询24小时在线客服！', '钱包提现提醒消息');
INSERT INTO `chat_config` VALUES ('wallet_recharge_amount', '200', '钱包充值单日总金额');
INSERT INTO `chat_config` VALUES ('wallet_recharge_android', '1,2', '钱包充值安卓开关');
INSERT INTO `chat_config` VALUES ('wallet_recharge_count', '5', '钱包充值单日次数');
INSERT INTO `chat_config` VALUES ('wallet_recharge_end', '00:00:00', '钱包充值结束时间');
INSERT INTO `chat_config` VALUES ('wallet_recharge_ios', '1,2', '钱包充值苹果开关');
INSERT INTO `chat_config` VALUES ('wallet_recharge_start', '00:00:00', '钱包充值开始时间');
INSERT INTO `chat_config` VALUES ('wallet_recharge_total', '9999', '钱包充值单日总笔数');

-- ----------------------------
-- Table structure for chat_feedback
-- ----------------------------
DROP TABLE IF EXISTS `chat_feedback`;
CREATE TABLE `chat_feedback`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交版本',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '处理状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '建议反馈' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_feedback
-- ----------------------------

-- ----------------------------
-- Table structure for chat_friend
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend`;
CREATE TABLE `chat_friend`  (
  `friend_id` bigint(20) NOT NULL COMMENT '主键',
  `current_id` bigint(20) NULL DEFAULT NULL COMMENT '当前id',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '群组id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '聊天号码',
  `remark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源',
  `black` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '黑名单',
  `top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '置顶',
  `disturb` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '静默',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '注销0正常null注销',
  PRIMARY KEY (`friend_id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `current_id`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_friend
-- ----------------------------

-- ----------------------------
-- Table structure for chat_friend_apply
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend_apply`;
CREATE TABLE `chat_friend_apply`  (
  `apply_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用户头像',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '聊天号码',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理由',
  `receive_id` bigint(20) NULL DEFAULT NULL COMMENT '接收id',
  `receive_remark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收备注',
  `source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请来源',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`apply_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友申请' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_friend_apply
-- ----------------------------

-- ----------------------------
-- Table structure for chat_friend_inform
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend_inform`;
CREATE TABLE `chat_friend_inform`  (
  `inform_id` bigint(20) NOT NULL COMMENT '主键',
  `inform_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '目标id',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '处理状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`inform_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '骚扰举报' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_friend_inform
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group
-- ----------------------------
DROP TABLE IF EXISTS `chat_group`;
CREATE TABLE `chat_group`  (
  `group_id` bigint(20) NOT NULL COMMENT '主键',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群名',
  `group_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群号',
  `banned` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '封禁群组',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '群组头像',
  `notice` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知公告',
  `notice_top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '悬浮开关',
  `config_member` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '成员保护',
  `config_invite` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '允许邀请',
  `config_speak` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '全员禁言',
  `config_title` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '群组头衔',
  `config_audit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '群组审核',
  `config_media` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '发送资源',
  `config_assign` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '专属可见',
  `config_nickname` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '昵称开关',
  `config_packet` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '红包开关',
  `config_amount` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '金额开关',
  `config_scan` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '二维码',
  `config_receive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '红包禁抢',
  `group_level` int(4) NULL DEFAULT 0 COMMENT '群组等级',
  `group_level_count` int(4) NULL DEFAULT 0 COMMENT '群组等级容量',
  `group_level_price` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '群组等级价格',
  `group_level_time` datetime NULL DEFAULT NULL COMMENT '群组容量时间',
  `privacy_no` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私开关',
  `privacy_scan` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私开关',
  `privacy_name` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私开关',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '注销0正常null注销',
  PRIMARY KEY (`group_id`) USING BTREE,
  UNIQUE INDEX `group_no`(`group_no`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天群组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group_apply
-- ----------------------------
DROP TABLE IF EXISTS `chat_group_apply`;
CREATE TABLE `chat_group_apply`  (
  `apply_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用户头像',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '群组id',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群组名称',
  `receive_id` bigint(20) NULL DEFAULT NULL COMMENT '接收人id',
  `apply_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请状态0无1同意2拒绝3忽略',
  `apply_source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请来源',
  `create_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`apply_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群组申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group_apply
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group_inform
-- ----------------------------
DROP TABLE IF EXISTS `chat_group_inform`;
CREATE TABLE `chat_group_inform`  (
  `inform_id` bigint(20) NOT NULL COMMENT '主键',
  `inform_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '目标id',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '处理状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`inform_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '骚扰举报' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group_inform
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group_log
-- ----------------------------
DROP TABLE IF EXISTS `chat_group_log`;
CREATE TABLE `chat_group_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '群组id',
  `log_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群组日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group_log
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group_member
-- ----------------------------
DROP TABLE IF EXISTS `chat_group_member`;
CREATE TABLE `chat_group_member`  (
  `member_id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '群组id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '聊天号码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像',
  `remark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `member_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'normal' COMMENT '成员类型',
  `top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否置顶',
  `disturb` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否免打扰',
  `member_source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '进群来源',
  `packet_white` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '禁抢白名单',
  `speak` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '禁言开关',
  `speak_time` datetime NULL DEFAULT NULL COMMENT '禁言时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '加入时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '注销0正常null注销',
  PRIMARY KEY (`member_id`) USING BTREE,
  UNIQUE INDEX `idx_group`(`user_id`, `group_id`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群组成员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group_member
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group_solitaire
-- ----------------------------
DROP TABLE IF EXISTS `chat_group_solitaire`;
CREATE TABLE `chat_group_solitaire`  (
  `solitaire_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '群组',
  `subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题',
  `example` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '例子',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`solitaire_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '成语接龙' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_group_solitaire
-- ----------------------------

-- ----------------------------
-- Table structure for chat_help
-- ----------------------------
DROP TABLE IF EXISTS `chat_help`;
CREATE TABLE `chat_help`  (
  `help_id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '状态',
  `sort` smallint(2) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`help_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天帮助' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_help
-- ----------------------------
INSERT INTO `chat_help` VALUES (1562386781612105731, '如何迁移/备份聊天记录', '目前版本不支持备份与恢复。', 'Y', 1);
INSERT INTO `chat_help` VALUES (1562386781612105732, '聊天记录清空后还能找回吗', '目前采用的是端对端加密传输，消息只记录在用户的终端设备上，一旦删除或撤回，就无法恢复。', 'Y', 2);
INSERT INTO `chat_help` VALUES (1562386781612105733, 'APP后台/锁屏后接收不到新消息通知', '进入手机“设置”“应用管理”找到《{}》进入权限管理赋予自启动、后台弹窗、悬浮窗、后台唤起权限/后台弹窗权限。', 'Y', 3);
INSERT INTO `chat_help` VALUES (1562386781612105734, 'APP后台总是被清理', '进入手机“设置”“应用自启动设置”找到《{}》设置“允许自启动”或者“允许后台运行”。', 'Y', 4);
INSERT INTO `chat_help` VALUES (1562386781612105735, 'APP后台/锁屏后接听不到语音/视频通话', '进入手机“设置”“应用管理”找到《{}》进入权限管理赋予自启动、悬浮窗、后台唤起权限/后台弹窗权限。', 'Y', 5);
INSERT INTO `chat_help` VALUES (1562386781612105736, '如何开启消息通知', '进入“我的”页面点击“软件设置”开启“消息声音”或“消息通知”。', 'Y', 6);
INSERT INTO `chat_help` VALUES (1562386781612105737, '怎么添加好友', '进入“消息”或“好友”页面点击右上角加号“添加好友”。', 'Y', 7);
INSERT INTO `chat_help` VALUES (1562386781612105738, '怎么同意/拒绝添加好友', '进入“好友”页面点击“验证信息”可以看到，好友申请列表，点击“忽略”或“同意”进行操作。', 'Y', 8);
INSERT INTO `chat_help` VALUES (1562386781612105739, '怎么切换账号', '进入“我的”页面点击“账号安全”点击“退出登录”进行操作。', 'Y', 9);
INSERT INTO `chat_help` VALUES (1562386781612105740, '怎么查看用户服务协议/隐私协议', '进入“我的”页面点击“软件设置”点击“服务协议”或“隐私协议”进行查看。', 'Y', 10);
INSERT INTO `chat_help` VALUES (1562386781612105741, '怎么查看我的个人信息收集情况', '进入“我的”页面点击“软件设置”点击“信息收集”进行查看。', 'Y', 11);


-- ----------------------------
-- Records of chat_msg
-- ----------------------------

-- ----------------------------
-- Table structure for chat_notice
-- ----------------------------
DROP TABLE IF EXISTS `chat_notice`;
CREATE TABLE `chat_notice`  (
  `notice_id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  `isindex` tinyint(1) NULL DEFAULT 0 COMMENT '系统首页公告',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_notice
-- ----------------------------
INSERT INTO `chat_notice` VALUES (1613477970402439169, '关于防诈骗的重要提醒', '<p>重要通知：近期诈骗犯罪案件时有发生，为防止您在经济上蒙受损失，请您接到陌生人或以熟人名义要求转账、汇款时，务必提高警惕，以防受骗！</p>', 'Y', '2025-08-31 17:01:19', 0, '2025-08-31 17:01:19');
INSERT INTO `chat_notice` VALUES (1949806025207205899, '秋季防疫通告', '<p>根据最新防疫要求，现做出以下通知：</p><p>进入公共场所需出示健康码</p><p>乘坐公共交通需佩戴口罩</p><p>积极配合社区核酸检测工作</p><p class=\"align-right ql-align-right\">防疫指挥部 宣</p>', 'Y', '2025-08-31 17:01:47', 1, '2025-08-31 17:01:47');

-- ----------------------------
-- Table structure for chat_number
-- ----------------------------
DROP TABLE IF EXISTS `chat_number`;
CREATE TABLE `chat_number`  (
  `chat_no` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '状态',
  PRIMARY KEY (`chat_no`) USING BTREE,
  UNIQUE INDEX `chat_no`(`chat_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统号码' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_number
-- ----------------------------

-- ----------------------------
-- Table structure for chat_portrait
-- ----------------------------
DROP TABLE IF EXISTS `chat_portrait`;
CREATE TABLE `chat_portrait`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像',
  `chat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天头像' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_portrait
-- ----------------------------
INSERT INTO `chat_portrait` VALUES (1793574396027731910, 'http://110.42.56.25:19000/xim/att/1.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731911, 'http://110.42.56.25:19000/xim/att/2.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731912, 'http://110.42.56.25:19000/xim/att/3.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731913, 'http://110.42.56.25:19000/xim/att/4.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731914, 'http://110.42.56.25:19000/xim/att/5.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731915, 'http://110.42.56.25:19000/xim/att/6.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731916, 'http://110.42.56.25:19000/xim/att/7.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731917, 'http://110.42.56.25:19000/xim/att/8.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731918, 'http://110.42.56.25:19000/xim/att/9.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731919, 'http://110.42.56.25:19000/xim/att/10.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731920, 'http://110.42.56.25:19000/xim/att/11.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731921, 'http://110.42.56.25:19000/xim/att/12.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731922, 'http://110.42.56.25:19000/xim/att/13.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731923, 'http://110.42.56.25:19000/xim/att/14.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731924, 'http://110.42.56.25:19000/xim/att/15.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731925, 'http://110.42.56.25:19000/xim/att/16.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731926, 'http://110.42.56.25:19000/xim/att/17.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731927, 'http://110.42.56.25:19000/xim/att/18.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731928, 'http://110.42.56.25:19000/xim/att/19.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731929, 'http://110.42.56.25:19000/xim/att/20.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731930, 'http://110.42.56.25:19000/xim/att/21.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731931, 'http://110.42.56.25:19000/xim/att/22.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731932, 'http://110.42.56.25:19000/xim/att/23.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731933, 'http://110.42.56.25:19000/xim/att/24.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731934, 'http://110.42.56.25:19000/xim/att/25.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731935, 'http://110.42.56.25:19000/xim/att/26.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731936, 'http://110.42.56.25:19000/xim/att/27.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731937, 'http://110.42.56.25:19000/xim/att/28.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731938, 'http://110.42.56.25:19000/xim/att/29.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731939, 'http://110.42.56.25:19000/xim/att/30.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731940, 'http://110.42.56.25:19000/xim/att/31.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731941, 'http://110.42.56.25:19000/xim/att/32.png', '1', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731970, 'http://110.42.56.25:19000/xim/btt/1.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731971, 'http://110.42.56.25:19000/xim/btt/2.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731972, 'http://110.42.56.25:19000/xim/btt/3.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731973, 'http://110.42.56.25:19000/xim/btt/4.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731974, 'http://110.42.56.25:19000/xim/btt/5.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731975, 'http://110.42.56.25:19000/xim/btt/6.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731976, 'http://110.42.56.25:19000/xim/btt/7.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731977, 'http://110.42.56.25:19000/xim/btt/8.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731978, 'http://110.42.56.25:19000/xim/btt/9.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731979, 'http://110.42.56.25:19000/xim/btt/10.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731980, 'http://110.42.56.25:19000/xim/btt/11.png', '2', 'Y');
INSERT INTO `chat_portrait` VALUES (1793574396027731981, 'http://110.42.56.25:19000/xim/btt/12.png', '2', 'Y');

-- ----------------------------
-- Table structure for chat_resource
-- ----------------------------
DROP TABLE IF EXISTS `chat_resource`;
CREATE TABLE `chat_resource`  (
  `resource_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '资源地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天资源' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_resource
-- ----------------------------

-- ----------------------------
-- Table structure for chat_robot
-- ----------------------------
DROP TABLE IF EXISTS `chat_robot`;
CREATE TABLE `chat_robot`  (
  `robot_id` bigint(20) NOT NULL COMMENT '主键',
  `secret` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '秘钥',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `portrait` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `menu` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '菜单',
  PRIMARY KEY (`robot_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务号' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_robot
-- ----------------------------
INSERT INTO `chat_robot` VALUES (10001, '8ykc55fcq1fc21agt11qtni60hujhrxf', '在线客服', 'http://110.42.56.25:19000/xim/root/1.png', '[]');
INSERT INTO `chat_robot` VALUES (10002, 'qry41hxsjg8l4kg242z5s1u91oxll8b', '支付助手', 'http://110.42.56.25:19000/xim/root/2.png', '[]');
INSERT INTO `chat_robot` VALUES (10003, 'zgs5ibsx565wn4ccbb3hqlnozwyiktm9', 'AI助理', 'http://110.42.56.25:19000/xim/root/3.png', '[]');

-- ----------------------------
-- Table structure for chat_robot_reply
-- ----------------------------
DROP TABLE IF EXISTS `chat_robot_reply`;
CREATE TABLE `chat_robot_reply`  (
  `reply_id` bigint(20) NOT NULL COMMENT '主键',
  `robot_id` bigint(20) NULL DEFAULT NULL COMMENT '机器人',
  `reply_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `reply_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键字',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  PRIMARY KEY (`reply_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务号' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_robot_reply
-- ----------------------------

-- ----------------------------
-- Table structure for chat_robot_sub
-- ----------------------------
DROP TABLE IF EXISTS `chat_robot_sub`;
CREATE TABLE `chat_robot_sub`  (
  `sub_id` bigint(20) NOT NULL COMMENT '主键',
  `robot_id` bigint(20) NULL DEFAULT NULL COMMENT '机器人',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '置顶',
  `disturb` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '静默',
  PRIMARY KEY (`sub_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务号' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_robot_sub
-- ----------------------------

-- ----------------------------
-- Table structure for chat_sms
-- ----------------------------
DROP TABLE IF EXISTS `chat_sms`;
CREATE TABLE `chat_sms`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '手机号',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '状态',
  `body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '结果',
  `create_time` datetime NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_sms
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user
-- ----------------------------
DROP TABLE IF EXISTS `chat_user`;
CREATE TABLE `chat_user`  (
  `user_id` bigint(20) NOT NULL COMMENT '主键',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '聊天号码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `portrait` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `gender` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '性别1男2女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `intro` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '介绍',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `salt` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `pass` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '密码标志',
  `auth` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '认证状态',
  `banned` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '封禁状态',
  `special` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '测试账号',
  `abnormal` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '异常账号',
  `payment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '支付密码',
  `privacy_no` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私no',
  `privacy_phone` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私手机',
  `privacy_scan` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私扫码',
  `privacy_card` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私名片',
  `privacy_group` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Y' COMMENT '隐私群组',
  `login_ios` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果openId',
  `login_qq` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '球球openId',
  `login_wx` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openId',
  `on_line` datetime NULL DEFAULT NULL COMMENT '在线时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `ip_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '注销0正常null注销',
  `safestr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '666666' COMMENT '安全码',
  `incode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邀请码',
  `user_dep` int(2) NULL DEFAULT 0 COMMENT '用户层级',
  `user_level` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级关系表',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父级ID',
  `isvip` tinyint(1) NULL DEFAULT 0 COMMENT '0普通1VIP2SVIP',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `incode`(`incode`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`, `deleted`) USING BTREE,
  UNIQUE INDEX `chat_no`(`user_no`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_appeal
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_appeal`;
CREATE TABLE `chat_user_appeal`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户申诉' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_appeal
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_collect
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_collect`;
CREATE TABLE `chat_user_collect`  (
  `collect_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `msg_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收藏类型',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_collect
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_deleted
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_deleted`;
CREATE TABLE `chat_user_deleted`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '注销时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '注销表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_deleted
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_info
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_info`;
CREATE TABLE `chat_user_info`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `identity1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正面',
  `identity2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '反面',
  `hold_card` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手持',
  `auth_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证原因',
  `auth_time` datetime NULL DEFAULT NULL COMMENT '认证时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_inv
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_inv`;
CREATE TABLE `chat_user_inv`  (
  `inid` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_inid` bigint(20) NOT NULL COMMENT '推荐人ID',
  `inv_usdt` double(10, 2) NULL DEFAULT 0.00 COMMENT '推荐奖励',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐人聊天号码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐人手机号',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐人昵称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '注销0正常null注销',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '0未处理1已处理2其他状态',
  PRIMARY KEY (`inid`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `user_inid`) USING BTREE COMMENT '同一个只能推荐一次'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员注册邀请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_inv
-- ----------------------------

-- ----------------------------
-- Records of chat_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_sign
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_sign`;
CREATE TABLE `chat_user_sign`  (
  `signid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID（关联用户表）',
  `trade_id` bigint(20) NULL DEFAULT NULL COMMENT '交易id',
  `sign_date` date NOT NULL COMMENT '签到日期（仅记录年月日，精确到天）',
  `reward_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '签到奖励（如USDT数量）',
  `sign_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '签到类型：1-正常签到，2-补签',
  `is_valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效：1-有效，0-无效（如取消签到）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（精确到秒）',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`signid`) USING BTREE,
  UNIQUE INDEX `uk_user_date`(`user_id`, `sign_date`) USING BTREE COMMENT '唯一索引：防止用户同一天重复签到',
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引：优化查询用户签到记录',
  INDEX `idx_sign_date`(`sign_date`) USING BTREE COMMENT '日期索引：优化查询某天的签到统计'
) ENGINE = InnoDB AUTO_INCREMENT = 1961464346844336130 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户按天签到记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_sign
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user_token
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_token`;
CREATE TABLE `chat_user_token`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'token',
  `device` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备',
  `device_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户token' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_token
-- ----------------------------

-- ----------------------------
-- Table structure for chat_version
-- ----------------------------
DROP TABLE IF EXISTS `chat_version`;
CREATE TABLE `chat_version`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本',
  `device` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备',
  `url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '地址',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `device`(`device`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统版本' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_version
-- ----------------------------
INSERT INTO `chat_version` VALUES (1561613225252913110, '1.0.0', 'android', 'https://www.baidu.com/demo.apk', '安卓最新版本1.2.0，更新了系统提现功能');
INSERT INTO `chat_version` VALUES (1561613225252913111, '1.0.0', 'ios', 'https://www.baidu.com/test', '我是苹果包');

-- ----------------------------
-- Table structure for chat_visit
-- ----------------------------
DROP TABLE IF EXISTS `chat_visit`;
CREATE TABLE `chat_visit`  (
  `visit_id` bigint(20) NOT NULL COMMENT '访问id',
  `visit_date` date NULL DEFAULT NULL COMMENT '访问时间',
  `visit_count` int(8) NULL DEFAULT 0 COMMENT '访问次数',
  PRIMARY KEY (`visit_id`) USING BTREE,
  UNIQUE INDEX `visit_date`(`visit_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户访问' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_visit
-- ----------------------------

-- ----------------------------
-- Table structure for chat_voice
-- ----------------------------
DROP TABLE IF EXISTS `chat_voice`;
CREATE TABLE `chat_voice`  (
  `msg_id` bigint(20) NOT NULL COMMENT '主键',
  `voice_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '地址',
  `voice_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文本',
  `user_id` bigint(20) NULL DEFAULT 0 COMMENT '用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '声音表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_voice
-- ----------------------------

-- ----------------------------
-- Table structure for friend_comments
-- ----------------------------
DROP TABLE IF EXISTS `friend_comments`;
CREATE TABLE `friend_comments`  (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `moment_id` bigint(20) NOT NULL COMMENT '关联动态ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论用户ID',
  `reply_to` bigint(20) NULL DEFAULT NULL COMMENT '回复的贴子用户ID（可为空）',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标记',
  `source` tinyint(1) NULL DEFAULT 1 COMMENT '是否为版主回复',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_moment_time`(`moment_id`, `create_time`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `reply_to`(`reply_to`) USING BTREE,
  CONSTRAINT `friend_comments_ibfk_1` FOREIGN KEY (`moment_id`) REFERENCES `friend_moments` (`moment_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `friend_comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `chat_user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1959833301481750530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of friend_comments
-- ----------------------------

-- ----------------------------
-- Table structure for friend_likes
-- ----------------------------
DROP TABLE IF EXISTS `friend_likes`;
CREATE TABLE `friend_likes`  (
  `like_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `moment_id` bigint(20) NOT NULL COMMENT '关联动态ID',
  `user_id` bigint(20) NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标记',
  PRIMARY KEY (`like_id`) USING BTREE,
  UNIQUE INDEX `uniq_moment_user`(`moment_id`, `user_id`) USING BTREE,
  INDEX `idx_moment_time`(`moment_id`, `create_time`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `friend_likes_ibfk_1` FOREIGN KEY (`moment_id`) REFERENCES `friend_moments` (`moment_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `friend_likes_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `chat_user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1961268367415316482 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of friend_likes
-- ----------------------------

-- ----------------------------
-- Table structure for friend_medias
-- ----------------------------
DROP TABLE IF EXISTS `friend_medias`;
CREATE TABLE `friend_medias`  (
  `media_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '媒体资源ID',
  `moment_id` bigint(20) NOT NULL COMMENT '关联动态ID',
  `momid` bigint(20) NULL DEFAULT NULL COMMENT '事件ID',
  `url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源URL',
  `thumbnail` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缩略图',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '类型：0-图片，1-视频，2-音频',
  `sort_order` tinyint(4) NULL DEFAULT 0 COMMENT '排序顺序',
  `width` smallint(6) NULL DEFAULT 0 COMMENT '宽度（图片/视频）',
  `height` smallint(6) NULL DEFAULT 0 COMMENT '高度（图片/视频）',
  `duration` int(11) NULL DEFAULT 0 COMMENT '时长（视频/音频，单位：秒）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`media_id`) USING BTREE,
  INDEX `idx_moment_sort`(`moment_id`, `sort_order`) USING BTREE,
  CONSTRAINT `friend_medias_ibfk_1` FOREIGN KEY (`moment_id`) REFERENCES `friend_moments` (`moment_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1960899049566486530 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈媒体资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of friend_medias
-- ----------------------------

-- ----------------------------
-- Table structure for friend_moments
-- ----------------------------
DROP TABLE IF EXISTS `friend_moments`;
CREATE TABLE `friend_moments`  (
  `moment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '动态ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户ID',
  `content` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文字内容',
  `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置信息',
  `visibility` tinyint(4) NULL DEFAULT 0 COMMENT '可见性：0-公开，1-好友可见，2-私密，3-部分可见，4-不给谁看',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标记',
  `visuser` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '可见用户ID',
  PRIMARY KEY (`moment_id`) USING BTREE,
  INDEX `idx_user_time`(`user_id`, `create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1960899049541320706 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈动态表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of friend_moments
-- ----------------------------


-- ----------------------------
-- Table structure for sys_column
-- ----------------------------
DROP TABLE IF EXISTS `sys_column`;
CREATE TABLE `sys_column`  (
  `column_id` bigint(20) NOT NULL COMMENT '表格ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `table_id` int(9) NULL DEFAULT NULL COMMENT '字段ID',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '字段内容',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '动态表格' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `dict_id` bigint(20) NOT NULL COMMENT '主键',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`, `dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_error`;
CREATE TABLE `sys_error`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `message` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文本内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统错误表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_error
-- ----------------------------

-- ----------------------------
-- Table structure for sys_html
-- ----------------------------
DROP TABLE IF EXISTS `sys_html`;
CREATE TABLE `sys_html`  (
  `id` bigint(20) NOT NULL COMMENT '网页ID',
  `html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '网页内容',
  `ctime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `roulekey` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标识符',
  `remake` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网页说明',
  `url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `roule`(`roulekey`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'APP网页定制' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_html
-- ----------------------------
INSERT INTO `sys_html` VALUES (1959183688632217601, '<p>许可协议</p>', '2025-08-23 17:19:26', 'sys-privacy', '隐私协议', 'https://work.weixin.qq.com/nl/eula');
INSERT INTO `sys_html` VALUES (1959186375268487169, '<p><strong>导言</strong></p><p><strong>欢迎你使用本软件及服务！</strong></p><p><br></p><p><strong>为使用软件及服务，你应当阅读并遵守《服务协议》（以下简称“本协议”）。请你务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款，以及开通或使用某项服务的单独协议，并选择接受或不接受。限制、免责条款可能以加粗等形式提示你注意。</strong></p><p><br></p><p><strong>除非你已阅读并接受本协议所有条款，否则你无权下载、安装或使用本软件及相关服务。你的下载、安装、使用、获取微信账号、登录等行为即视为你已阅读并同意上述协议的约束。</strong></p><p><br></p><p><strong>如果你未满18周岁，请在法定监护人的陪同下阅读本协议及上述其他协议，并特别注意未成年人使用条款。特别地，如果你是未满14周岁的儿童，则在完成账号注册前，还应请你的监护人仔细阅读系统公司专门制定的《儿童隐私保护声明》。只有在取得监护人对《儿童隐私保护声明》的同意后，未满14周岁的儿童方可使用微信服务。</strong></p><p><br></p><p>一、协议的范围</p><p>1.1 协议适用主体范围</p><p><br></p><p>本协议是你与系统之间关于你下载、安装、使用、复制本软件，以及使用系统相关服务所订立的协议。</p><p><br></p><p>1.2 协议关系及冲突条款</p><p><br></p><p>本协议被视为《系统服务协议》的补充协议，是其不可分割的组成部分，与其构成统一整体。本协议与上述内容存在冲突的，以本协议为准。</p><p><br></p><p>本协议内容同时包括系统可能不断发布的关于本服务的相关协议、业务规则等内容。上述内容一经正式发布，即为本协议不可分割的组成部分，你同样应当遵守。</p><p><br></p><p>二、关于本服务</p><p>2.1 本服务的内容</p><p><br></p><p>本服务内容是指系统向用户提供的跨平台的通讯工具（以下简称“微信”），支持单人、多人参与，在发送语音短信、视频、图片、表情和文字等即时通讯服务的基础上，同时为用户提供包括但不限于关系链拓展、便捷工具、微信公众账号、开放平台、与其他软件或硬件信息互通等功能或内容的软件许可及服务（以下简称“本服务”）。</p><p><br></p><p>2.2 本服务的形式</p><p><br></p><p>2.2.1 你使用本服务需要下载系统微信客户端软件，对于这些软件，系统给予你一项个人的、不可转让及非排他性的许可。微信网页版、Windows版、Mac版、车载版、手表版等需要通过二维码扫描登录。你仅可为访问或使用本服务的目的而使用这些软件及服务。</p><p><br></p><p>2.2.2 本服务中系统微信客户端软件可能提供包括但不限于iOS、Android、Windows Phone、Symbian、BlackBerry、Windows、Mac、Linux、HarmonyOS等多个应用版本，用户必须选择与所安装终端设备相匹配的软件版本。</p><p><br></p><p>2.3 本服务许可的范围</p><p><br></p><p>2.3.1 系统给予你一项个人的、不可转让及非排他性的许可，以使用本软件。你可以为非商业目的在单一台终端设备上安装、使用、显示、运行本软件。</p><p><br></p><p>2.3.2 你可以为使用本软件及服务的目的复制本软件的一个副本，仅用作备份。备份副本必须包含原软件中含有的所有著作权信息。</p><p><br></p><p>2.3.3 本条及本协议其他条款未明示授权的其他一切权利仍由系统保留，你在行使这些权利时须另外取得系统的书面许可。系统如果未行使前述任何权利，并不构成对该权利的放弃。</p><p><br></p><p>三、软件的获取</p><p>3.1 你可以直接从系统的网站上获取本软件，也可以从得到系统授权的第三方获取。</p><p><br></p><p>3.2 如果你从未经系统授权的第三方获取本软件或与本软件名称相同的安装程序，系统无法保证该软件能够正常使用，并对因此给你造成的损失不予负责。</p><p><br></p><p>四、软件的安装与卸载</p><p>4.1 系统可能为不同的终端设备开发了不同的软件版本，你应当根据实际情况选择下载合适的版本进行安装。</p><p><br></p><p>4.2 下载安装程序后，你需要按照该程序提示的步骤正确安装。</p><p><br></p><p>4.3 为提供更加优质、安全的服务，在本软件安装时系统可能推荐你安装其他软件，你可以选择安装或不安装。</p><p><br></p><p>4.4 如果你不再需要使用本软件或者需要安装新版软件，可以自行卸载。请注意，卸载本软件后，存储在用户终端设备的聊天记录将会被清除，重新安装后将无法继续查看原有聊天记录。如果你愿意帮助系统改进产品服务，请告知卸载的原因。</p><p><br></p><p>五、软件的更新</p><p>5.1 为了改善用户体验、完善服务内容，系统将不断努力开发新的服务，并为你不时提供软件更新（这些更新可能会采取软件替换、修改、功能强化、版本升级等形式）。</p><p><br></p><p>5.2 为了保证本软件及服务的安全性和功能的一致性，系统有权不经向你特别通知而对软件进行更新，或者对软件的部分功能效果进行改变或限制。</p><p><br></p><p>5.3 本软件新版本发布后，旧版本的软件可能无法使用。系统不保证旧版本软件继续可用及相应的客户服务，请你随时核对并下载最新版本。</p><p><br></p><p>六、用户个人信息保护</p><p>6.1 保护用户个人信息是系统的一项基本原则，系统将会采取合理的措施保护用户的个人信息。除法律法规规定的情形外，未经用户许可系统不会向第三方公开、透露用户个人信息。系统对相关信息采用专业加密存储与传输方式，保障用户个人信息的安全。</p><p><br></p><p>6.2 你在注册账号或使用本服务的过程中，需要提供一些必要的信息，例如：为向你提供账号注册服务或进行用户身份识别，需要你填写手机号码；附近的人功能需要你同意使用你所在的地理位置信息；手机通讯录匹配功能需要你授权访问手机通讯录等。若国家法律法规或政策有特殊规定的，你需要提供真实的身份信息。若你提供的信息不完整，则无法使用本服务或在使用过程中受到限制。</p><p><br></p><p>6.3 一般情况下，你可随时浏览、修改自己提交的信息，但出于安全性和身份识别（如号码申诉服务）的考虑，你可能无法修改注册时提供的初始注册信息及其他验证信息。若你通过已有微信账号辅助注册新微信账号，系统将视新微信账号注册过程中验证的手机号是该账号的初始手机号。</p><p><br></p><p>6.4 系统将运用各种安全技术和程序建立完善的管理制度来保护你的个人信息，以免遭受未经授权的访问、使用或披露。</p><p><br></p><p>6.5 系统非常重视对未成年人个人信息的保护。若你是18周岁以下的未成年人，在使用系统的服务前，应事先取得你家长或法定监护人的书面同意。</p><p><br></p><p>6.6 对于微信会如何收集、使用、存储和保护你的个人信息及你享有何种权利，你还可以阅读《微信隐私保护指引》予以进一步了解。</p><p><br></p><p>七、主权利义务条款</p><p>7.1 账号使用规范</p><p><br></p><p>7.1.1 你在使用本服务前需要注册一个微信账号。微信账号可通过手机号码进行注册，并绑定注册手机号码。或者你可以通过已有的微信账号辅助注册新微信账号。你可在注册成功后绑定QQ号码、邮箱账号。如你使用邮箱账号进行绑定，请你使用未与微信公众平台账号绑定的邮箱账号。在符合平台规则的前提下，你可以更换绑定的手机号码。系统有权约定通过已有的微信账号辅助注册新微信账号需满足的前提条件，用户利用同一身份不得注册超过合理数量的账号。</p><p><br></p><p>系统有权根据用户需求或产品需要对账号注册和绑定的方式进行变更。无论你通过本协议约定的何种方式注册微信账号，关于你使用账号的具体规则，请遵守《QQ号码规则》、《微信个人账号使用规范》、相关账号使用协议以及系统为此发布的专项规则。</p><p><br></p><p>7.1.2 微信账号的所有权归系统公司所有，用户完成申请注册手续后，仅获得微信账号的使用权，且该使用权仅属于初始申请注册人。同时，初始申请注册人不得赠与、借用、租用、转让或售卖微信账号或者以其他方式许可非初始申请注册人使用微信账号。非初始申请注册人不得通过受赠、继承、承租、受让或者其他任何方式使用微信账号。</p><p><br></p><p>7.1.3 用户有责任妥善保管注册账户信息及账户密码的安全，用户需要对注册账户以及密码下的行为承担法律责任。用户同意在任何情况下不向他人透露账户及密码信息。当在你怀疑他人在使用你的账号时，你应立即通知系统公司。</p><p><br></p><p>7.1.4 用户在使用手机号码注册微信账号时，系统将为你自动匹配微信号，你可以对微信号进行设置，也可根据相关功能页面的指引、在满足相应条件的前提下对微信号进行修改。</p><p><br></p><p>7.1.5 你在使用本软件客户端某一特定服务或功能时，该服务或功能可能会要求你基于微信账号创建或注册新的账号（以下简称“功能账号”），你需依据具体要求进行操作。如无系统书面说明或者许可，前述账号的所有权亦归系统公司所有，用户完成创建或注册操作后，仅获得该账号的使用权，且该使用权同样仅属于初始申请注册人，初始申请注册人不得赠与、借用、租用、转让或售卖该账号或者以其他方式许可非初始申请注册人使用该账号，非初始申请注册人不得通过受赠、继承、承租、受让或者其他任何方式使用该账号。</p><p><br></p><p>7.1.6 用户注册或创建微信账号或功能账号后如果长期不登录该账号，系统有权回收该账号，以免造成资源浪费，由此带来的任何损失均由用户自行承担。</p><p><br></p><p>7.2 用户注意事项</p><p><br></p><p>7.2.1 你理解并同意：为了向你提供有效的服务，本软件会利用你终端设备的处理器和带宽等资源。本软件使用过程中可能产生数据流量的费用，用户需自行向运营商了解相关资费信息，并自行承担相关费用。</p><p><br></p><p>7.2.2 你理解并同意：本软件的某些功能可能会让第三方知晓用户的信息，例如：用户的微信好友可以查询用户头像、名字、微信号或朋友圈内容等可公开的个人资料；视频号的头像、名字、简介以及发布的内容和互动信息等已公开的信息；使用手机号码注册微信账号或手机号码与微信账号关联的用户，微信账号可以被通讯录存有该号码的好友查询；用户关注部分类型公众号或服务号后将成为公众号或服务号关注用户，相关公众号或服务号运营者可以根据其功能权限获取关注用户头像、名字、消息评论内容、图片、视频等信息，并可通过微信公众平台发送消息与关注用户进行互动。关注用户的消息评论内容、向相关公众号或服务号上传的图片、视频都可以被具备相应功能权限的公众号或服务号运营者发布在公众号或服务号内，供第三方查阅。用户选择微信与其他软件或硬件信息互通后，其他软件或硬件的提供方可以获取用户在微信主动公开或传输的相关信息，用户在选择前应充分了解其他软件或硬件的产品功能及信息保护策略。</p><p><br></p><p>7.2.3 你在使用本软件某一特定服务时，该服务可能会另有单独的协议、相关业务规则等（以下统称为“单独协议”），你在使用该项服务前请阅读并同意相关的单独协议；你使用前述特定服务，即视为你接受相关单独协议。例如：如果你需要使用微信支付相关服务（包括但不限于零钱、微信红包、转账、刷卡、付款、收款等支付服务）时，需阅读并遵守《微信支付用户服务协议》；如果你需要使用视频号相关服务（包括不限于浏览、点赞、评论、转发、分享、创建或注册视频号等服务），需阅读并遵守《微信视频号运营规范》。</p><p><br></p><p>7.2.4 你理解并同意系统将会尽其商业上的合理努力保障你在本软件及服务中的数据存储安全，但是，系统并不能就此提供完全保证，包括但不限于以下情形：</p><p><br></p><p>7.2.4.1 系统不对你在本软件及服务中相关数据的删除或储存失败负责；</p><p><br></p><p>7.2.4.2 系统有权根据实际情况自行决定单个用户在本软件及服务中数据的最长储存期限，并在服务器上为其分配数据最大存储空间等。你可根据自己的需要自行备份本软件及服务中的相关数据；</p><p><br></p><p>7.2.4.3 如果你停止使用本软件及服务或服务被终止或取消，系统可以从服务器上永久地删除你的数据。服务停止、终止或取消后，系统没有义务向你返还任何数据。</p><p><br></p><p>7.2.5 用户在使用本软件及服务时，须自行承担如下来自系统不可掌控的风险内容，包括但不限于：</p><p><br></p><p>7.2.5.1 由于不可抗拒因素可能引起的个人信息丢失、泄漏等风险；</p><p><br></p><p>7.2.5.2 用户必须选择与所安装终端设备相匹配的软件版本，否则，由于软件与终端设备型号不相匹配所导致的任何问题或损害，均由用户自行承担；</p><p><br></p><p>7.2.5.3 用户在使用本软件访问第三方网站时，因第三方网站及相关内容所可能导致的风险，由用户自行承担；</p><p><br></p><p>7.2.5.4 用户发布的内容被他人转发、分享，因此等传播可能带来的风险和责任；</p><p><br></p><p>7.2.5.5 用户终端设备存储空间不足、故障等原因导致数据无法接收、丢失等风险；</p><p><br></p><p>7.2.5.6 由于无线网络信号不稳定、无线网络带宽小等原因，所引起的系统微信登录失败、资料同步不完整、页面打开速度慢等风险；</p><p><br></p><p>7.2.5.7 你理解并同意，在你进行账号注册或使用本服务时，如发现你账号可能存在涉诈等异常情形或风险的，系统有权根据相关法律法规规定重新核验你的账号，并可根据风险情况，采取限期改正、限制功能、暂停使用、关闭账号、禁止重新注册以及本协议规定的其他处置措施。在向你提供本服务过程中，如发现涉诈违法犯罪线索、风险信息的，系统有权依照国家有关规定，根据涉诈风险类型、程度情况移送公安、金融、电信、网信等有权部门。在你使用本服务时，请务必妥善保管个人账号及密码，不得非法买卖、出租、出借个人账号。</p><p><br></p><p>7.2.5.8 你理解并同意：在使用本服务的过程中，可能会遇到不可抗力等风险因素，使本服务发生中断。不可抗力是指不能预见、不能克服并不能避免且对一方或双方造成重大影响的客观事件，包括但不限于自然灾害如洪水、地震、风暴、瘟疫等以及社会事件如战争、动乱、政府行为等。</p><p><br></p><p>7.3 第三方产品和服务</p><p><br></p><p>7.3.1 你在使用本软件第三方提供的产品或服务时，除遵守本协议约定外，还应遵守第三方的用户协议。系统和第三方对可能出现的纠纷在法律规定和约定的范围内各自承担责任。</p><p><br></p><p>7.3.2 因用户使用本软件或要求系统提供特定服务时，本软件可能会调用第三方系统或者通过第三方支持用户的使用或访问，使用或访问的结果由该第三方提供（包括但不限于第三方通过微信公众账号提供的服务，或通过开放平台接入的内容等），系统不保证通过第三方提供服务及内容的安全性、准确性、有效性及其他不确定的风险，由此若引发的任何争议及损害，与系统无关，系统不承担任何责任。</p><p><br></p><p>7.3.3 第三方服务提供者可以通过本软件向你提供产品或者服务，比如你可以关注第三方注册的公众号或服务号、接受第三方通过小程序提供的服务及产品，或者通过微信授权登录能力登录并使用其他互联网服务。在此过程中，根据你所使用的服务，第三方服务提供者可能会访问、收集、使用和存储你的相关数据或信息，系统通过与第三方服务提供者签署的有关协议、微信相关规范以及合理的产品流程设计，严格要求第三方服务提供者获取你的任何数据均应遵守相关法律法规的规定，必须事先获得你的明确同意，采取必要的数据保护措施，且仅为产品或服务之目的合理使用你的相关数据，向你提供修改或删除自己数据的方式，并在停止服务时必须删除全部数据等，尽最大可能保障你的数据和隐私不受侵害。你在接受或使用第三方产品或服务前应充分了解第三方的主体信息及产品或服务的条款及政策。如果你发现有第三方服务提供者存有相关违法违规行为，可以向系统投诉，系统将查实后予以处理。</p><p><br></p><p>7.3.4 你理解并同意，系统有权决定将本软件作商业用途，包括但不限于开发、使用本软件的部分服务为第三方作推广等，系统承诺在推广过程中严格按照本协议约定保护你的个人信息，同时你亦可以根据系统设置选择屏蔽、拒绝接收相关推广信息。</p><p><br></p><p>八、用户行为规范</p><p>8.1 信息内容规范</p><p><br></p><p>8.1.1 本条所述信息内容是指用户使用本软件及服务过程中所制作、复制、发布、传播的任何内容，包括但不限于微信账号或功能账号的头像、名字、用户说明、简介等注册信息，或发送的文字、语音、图片、视频、表情等信息、朋友圈图文、视频动态、视频号内容和相关链接页面，以及其他使用微信账号或功能账号或本软件及服务所产生的内容。</p><p><br></p><p>8.1.2 你理解并同意，微信一直致力于为用户提供文明健康、规范有序的网络环境，你不得利用微信账号或功能账号或本软件及服务制作、复制、发布、传播如下干扰微信正常运营，以及侵犯其他用户或第三方合法权益的内容，包括但不限于：</p><p><br></p><p>8.1.2.1 发布、传送、传播、储存违反国家法律法规规定的内容：</p><p><br></p><p>（1）反对宪法所确定的基本原则的；</p><p><br></p><p>（2）危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p><p><br></p><p>（3）损害国家荣誉和利益的；</p><p><br></p><p>（4）歪曲、丑化、亵渎、否定英雄烈士事迹和精神，以侮辱、诽谤或者其他方式侵害英雄烈士的姓名、肖像、名誉、荣誉的；</p><p><br></p><p>（5）宣扬恐怖主义、极端主义或者煽动实施恐怖活动、极端主义活动的；</p><p><br></p><p>（6）煽动民族仇恨、民族歧视，破坏民族团结的；</p><p><br></p><p>（7）破坏国家宗教政策，宣扬邪教和封建迷信的；</p><p><br></p><p>（8）散布谣言，扰乱社会秩序，破坏社会稳定的；</p><p><br></p><p>（9）散布淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；</p><p><br></p><p>（10）侮辱或者诽谤他人，侵害他人名誉、隐私和其他合法权益的；</p><p><br></p><p>（11）使用夸张标题，内容与标题严重不符的；</p><p><br></p><p>（12）炒作绯闻、丑闻、劣迹等的；</p><p><br></p><p>（13）不当评述自然灾害、重大事故等灾难的；</p><p><br></p><p>（14）带有性暗示、性挑逗等易使人产生性联想的；</p><p><br></p><p>（15）展现血腥、惊悚、残忍等致人身心不适的；</p><p><br></p><p>（16）煽动人群歧视、地域歧视等的；</p><p><br></p><p>（17）宣扬低俗、庸俗、媚俗内容的；</p><p><br></p><p>（18）可能引发未成年人模仿不安全行为和违反社会公德行为、诱导未成年人不良嗜好等的；</p><p><br></p><p>（19）侵害未成年人合法权益或者损害未成年人身心健康的内容；</p><p><br></p><p>（20）其他对网络生态造成不良影响的内容；</p><p><br></p><p>（21）煽动非法集会、结社、游行、示威、聚众扰乱社会秩序；</p><p><br></p><p>（22）以非法民间组织名义活动的；</p><p><br></p><p>（23）不符合《即时通信工具公众信息服务发展管理暂行规定》及遵守法律法规、社会主义制度、国家利益、公民合法利益、公共秩序、社会道德风尚和信息真实性等“七条底线”要求的；</p><p><br></p><p>（24）法律、行政法规禁止的其他内容。</p><p><br></p><p>8.1.2.2 发布、传送、传播、储存侵害他人名誉权、肖像权、知识产权、商业秘密等合法权利的内容；</p><p><br></p><p>8.1.2.3 涉及他人隐私、个人信息或资料的；</p><p><br></p><p>8.1.2.4 发表、传送、传播骚扰、广告信息、过度营销信息及垃圾信息或含有任何性或性暗示的；</p><p><br></p><p>8.1.2.5 其他违反法律法规、政策及公序良俗、社会公德或干扰微信正常运营和侵犯其他用户或第三方合法权益内容的信息。</p><p><br></p><p>8.2 软件使用规范</p><p><br></p><p>8.2.1 除非法律允许或系统书面许可，你使用本软件过程中不得从事下列行为：</p><p><br></p><p>8.2.1.1 删除本软件及其副本上关于著作权的信息；</p><p><br></p><p>8.2.1.2 对本软件进行反向工程、反向汇编、反向编译，或者以其他方式尝试发现本软件的源代码；</p><p><br></p><p>8.2.1.3 对系统拥有知识产权的内容进行使用、出租、出借、复制、修改、链接、转载、汇编、发表、出版、建立镜像站点等；</p><p><br></p><p>8.2.1.4 对本软件或者本软件运行过程中释放到任何终端内存中的数据、软件运行过程中客户端与服务器端的交互数据，以及本软件运行所必需的系统数据，进行复制、修改、增加、删除、挂接运行或创作任何衍生作品，形式包括但不限于使用插件、外挂或非经系统授权的第三方工具/服务接入本软件和相关系统；</p><p><br></p><p>8.2.1.5 通过修改或伪造软件运行中的指令、数据，增加、删减、变动软件的功能或运行效果，或者将用于上述用途的软件、方法进行运营或向公众传播，无论这些行为是否为商业目的；</p><p><br></p><p>8.2.1.6 通过非系统开发、授权的第三方软件、插件、外挂、系统，登录或使用本软件及服务，或者进行自动化操作，或者制作、发布、传播上述工具、方法等；</p><p><br></p><p>8.2.1.7 自行或者授权他人、第三方软件或系统等对本软件及其组件、模块、数据进行控制、访问、读取或干扰，或者规避、破坏本软件设置的安全保护等技术措施；</p><p><br></p><p>8.2.1.8 其他未经系统明示授权的行为。</p><p><br></p><p>8.2.2 你理解并同意，基于用户体验、微信或相关服务平台运营安全、平台规则要求及健康发展等综合因素，系统有权选择提供服务的对象，有权决定功能设置，有权决定功能开放、数据接口和相关数据披露的对象和范围。针对以下情形，有权视具体情况中止或终止提供本服务，包括但不限于：</p><p><br></p><p>8.2.2.1 违反法律法规或本协议规定的；</p><p><br></p><p>8.2.2.2 影响服务体验的；</p><p><br></p><p>8.2.2.3 存在安全隐患的；</p><p><br></p><p>8.2.2.4 与微信或其服务平台已有主要功能或功能组件相似、相同，或可实现上述功能或功能组件的主要效用的；</p><p><br></p><p>8.2.2.5 界面、风格、功能、描述或使用者体验与微信或其服务平台类似，可能造成微信用户认为其所使用的功能或服务来源于系统或经系统授权的；</p><p><br></p><p>8.2.2.6 违背微信或其服务平台运营原则，或不符合系统其他管理要求的。</p><p><br></p><p>8.3 服务运营规范</p><p><br></p><p>除非法律允许或系统书面许可，你使用本服务过程中不得从事下列行为：</p><p><br></p><p>8.3.1 提交、发布虚假信息，或冒充、利用他人名义的；</p><p><br></p><p>8.3.2 诱导其他用户点击链接页面或分享信息的；</p><p><br></p><p>8.3.3 虚构事实、隐瞒真相以误导、欺骗他人的；</p><p><br></p><p>8.3.4 侵害他人名誉权、肖像权、知识产权、商业秘密等合法权利的；</p><p><br></p><p>8.3.5 未经系统书面许可利用微信账号、功能账号和任何功能，以及第三方运营平台进行推广或互相推广的；</p><p><br></p><p>8.3.6 利用微信账号或功能账号或本软件及服务从事任何违法犯罪活动的；</p><p><br></p><p>8.3.7 制作、发布与以上行为相关的方法、工具，或对此类方法、工具进行运营或传播，无论这些行为是否为商业目的；</p><p><br></p><p>8.3.8 其他违反法律法规规定、侵犯其他用户合法权益、干扰产品正常运营或系统未明示授权的行为。</p><p><br></p><p>为了更好地保障用户合法权益及良好的用户体验，系统针对微信账号制定了《微信个人账号使用规范》（以下简称《使用规范》），《使用规范》为本协议不可分割的组成部分，请你认真阅读并予以遵守。为了向用户提供一个绿色、安全、健康、便捷的网络生态环境，系统在必要时可能会修改《使用规范》，修改后的内容发布后即成为本协议不可分割的组成部分，你同样应当遵守。你可以在本软件的官方网站查阅《使用规范》的最新版本。</p><p><br></p><p>8.4 对自己行为负责</p><p><br></p><p>你充分了解并同意，你必须为自己注册账号下的一切行为负责，包括你所发表的任何内容、评论及点赞、推荐等行为，以及由此产生的任何后果。你应对本服务中的内容自行加以判断，并承担因使用内容而引起的所有风险，包括因对内容的正确性、完整性或实用性的依赖而产生的风险。系统无法且不会对因前述风险而导致的任何损失或损害承担责任。</p><p><br></p><p>8.5 违约处理</p><p><br></p><p>8.5.1 如果系统发现或收到他人举报或投诉用户违反本协议约定的，系统有权不经通知随时对相关内容进行删除、屏蔽，并视行为情节对违规主体相关账号处以包括但不限于警告、限制或禁止使用部分或全部功能、账号封禁直至注销、回收账号的处罚，并公告处理结果。</p><p><br></p><p>8.5.2 你理解并同意，系统有权依合理判断对违反有关法律法规或本协议规定的行为进行处罚，对违法违规的任何用户采取适当的法律行动，并依据法律法规保存有关信息向有关部门报告和配合调查等，用户应独自承担由此而产生的一切法律责任。</p><p><br></p><p>8.5.3 你理解并同意，因你违反本协议或相关服务条款的规定，导致或产生第三方主张的任何索赔、要求或损失，你应当独立承担责任；系统因此遭受损失的，你也应当一并赔偿。</p><p><br></p><p>九、知识产权声明</p><p>9.1 系统是本软件的知识产权权利人。本软件的一切著作权、商标权、专利权、商业秘密等知识产权，以及与本软件相关的所有信息内容（包括但不限于文字、图片、音频、视频、图表、界面设计、版面框架、有关数据或电子文档等）均受中华人民共和国法律法规和相应的国际条约保护，系统享有上述知识产权，但相关权利人依照法律规定应享有的权利除外。</p><p><br></p><p>9.2 未经系统或相关权利人书面同意，你不得为任何商业或非商业目的自行或许可任何第三方实施、利用、转让上述知识产权。</p><p><br></p><p>9.3 你使用本软件及服务过程中上传、发布的全部内容，均不会因为上传、发布行为发生知识产权、肖像权等权利的转移。与此同时，你理解并同意系统为实现产品目的，对你发布的特定公开非保密内容（如“视频动态”、“自拍表情”和“视频号内容”等）在法律允许的范围内进行使用，包括但不限于予以存储、向有关用户播放、供有关用户获取及再次使用等。</p><p><br></p><p>十、终端安全责任</p><p>10.1 你理解并同意，本软件同大多数互联网软件一样，可能会受多种因素影响，包括但不限于用户原因、网络服务质量、社会环境等；也可能会受各种安全问题的侵扰，包括但不限于他人非法利用用户资料，进行现实中的骚扰；用户下载安装的其他软件或访问的其他网站中可能含有病毒、木马程序或其他恶意程序，威胁你的终端设备信息和数据安全，继而影响本软件的正常使用等。因此，你应加强信息安全及个人信息的保护意识，注意密码保护，以免遭受损失。</p><p><br></p><p>10.2 你不得制作、发布、使用、传播用于窃取微信账号、功能账号及他人个人信息、财产的恶意程序。</p><p><br></p><p>10.3 维护软件安全与正常使用是系统和你的共同责任，系统将按照行业标准合理审慎地采取必要技术措施保护你的终端设备信息和数据安全，但是你承认和同意系统并不能就此提供完全保证。</p><p><br></p><p>10.4 在任何情况下，你不应轻信借款、索要密码或其他涉及财产的网络信息。涉及财产操作的，请一定先核实对方身份，并请经常留意系统有关防范诈骗犯罪的提示。</p><p><br></p><p>十一、第三方软件或技术</p><p>11.1 本软件可能会使用第三方软件或技术（包括本软件可能使用的开源代码和公共领域代码等，下同），这种使用已经获得合法授权。</p><p><br></p><p>11.2 本软件如果使用了第三方的软件或技术，系统将按照相关法规或约定，对相关的协议或其他文件，可能通过本协议附件、在本软件安装包特定文件夹中打包、或通过开源软件页面等形式进行展示，它们可能会以“软件使用许可协议”、“授权协议”、“开源代码许可证”或其他形式来表达。前述通过各种形式展现的相关协议、其他文件及网页，均是本协议不可分割的组成部分，与本协议具有同等的法律效力，你应当遵守这些要求。 如果你没有遵守这些要求，该第三方或者国家机关可能会对你提起诉讼、罚款或采取其他制裁措施，并要求系统给予协助，你应当自行承担法律责任。</p><p><br></p><p>11.3 如因本软件使用的第三方软件或技术引发的任何纠纷，应由该第三方负责解决，系统不承担任何责任。系统不对第三方软件或技术提供客服支持，若你需要获取支持，请与第三方联系。</p><p><br></p><p>十二、其他</p><p>12.1 你使用本软件即视为你已阅读并同意受本协议的约束。系统有权在必要时修改本协议条款，并以适当的方式提醒你，以便你及时了解本协议的最新版本，你可以在本软件客户端“我—设置—关于微信”或官方网站查阅本协议的最新版本。本协议条款变更后，如果你继续使用本软件，即视为你已接受修改后的协议。如果你不接受修改后的协议，应当停止使用本软件。</p><p><br></p><p>12.2 本协议签订地为中华人民共和国广东省深圳市南山区。</p><p><br></p><p>12.3 本协议的成立、生效、履行、解释及纠纷解决，适用中华人民共和国大陆地区法律（不包括冲突法）。</p><p><br></p><p>12.4 若你和系统之间发生任何纠纷或争议，首先应友好协商解决；协商不成的，你同意将纠纷或争议提交本协议签订地有管辖权的人民法院管辖。</p><p><br></p><p>12.5 本协议所有条款的标题仅为阅读方便，本身并无实际涵义，不能作为本协议涵义解释的依据。</p><p><br></p><p>12.6 本协议可能存在包括中文、英文在内的多种语言的版本，如果存在中文版本与其他语言的版本相冲突的地方，以中文版本为准。</p><p><br></p><p>12.7 本协议条款无论因何种原因部分无效或不可执行，其余条款仍有效，对双方具有约束力。</p><p><br></p><p>(完)</p><p><br></p><p>深圳市系统计算机系统有限公司</p>', '2025-08-23 17:30:05', 'sys-service', '服务协议', NULL);
INSERT INTO `sys_html` VALUES (1959186580432867329, '<p>政和科技股份有限公司是中国互联网+产业服务领军企业，通过全面实施以智慧产业互联网平台为主体，以产业服务和产业融合发展为两翼的“一体两翼”战略，打造最具潜力“产业赋能的中国引领者”。</p><p><br></p><p>公司坚持线上线下全场景服务，发挥“设计-建设-运营”一体化优势，致力于构建“平台+数据+应用+服务”的云生态产业圈，为政府部门、园区、企业及创业者等提供平台建设、平台运营、产业服务三位一体的产业赋能整体解决方案。</p><p><br></p><p class=\"ql-align-center\"><br></p><p>公司以北京为总部、以济南为本部，同时在天津、江苏、陕西、河南、安徽、贵州、江西、新疆等十余省市区设立分支机构，服务涉及国内20多个省（市、区），累计为50000多家企业、300多个政府管理部门和园区提供服务。被认定为国家中小企业公共服务示范平台、中国电子信息行业优秀企业、中国互联网行业安全认证示范企业、中国专精特新中小企业、创新创业服务领域最佳解决方案商、首批大数据重点骨干企业、首批隐形冠军企业、瞪羚企业，荣膺创新创业大赛全国总决赛十强。</p><p><br></p><p>公司现有员工近400人，本科以上学历人员占90%，硕士以上占30%，学科领域多元且搭配合理，高级工程师、系统分析师、高级项目管理工程师等高端技术研发团队和注册会计师、注册税务师、专利代理人、高级经济师等专业咨询团队均百余人；与浙江大学、山东大学、深圳先研院、中科院软件所等一流大学和科研机构建立了良好合作关系；公司持续引进和培育高层次人才，包括山东省蓝色汇智双百人才、泉城产业领军人才、泉城特聘人才等，面向全国提供系统全面的产业赋能服务。</p>', '2025-08-23 17:30:54', 'sys-aboutus', '关于我们', NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `log_id` bigint(20) NOT NULL COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `log_type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '业务类型',
  `request_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `request_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `ip_addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `message` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '操作状态（Y正常N异常）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_login`;
CREATE TABLE `sys_login`  (
  `log_id` bigint(20) NOT NULL COMMENT '访问ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ip_addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '登录状态（Y成功 N失败）',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL COMMENT '主ID',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '类型（M目录 C菜单 F按钮）',
  `icon` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '图标',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `perms` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `sort` int(4) NULL DEFAULT 0 COMMENT '顺序',
  `frame_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '外链',
  `frame_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'Y' COMMENT '菜单状态（Y正常N停用）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '显示',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1590993102, 0, '系统设置', '1', 'SettingOutlined', NULL, NULL, NULL, 902, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993118, 0, '系统监控', '1', 'CloudServerOutlined', NULL, NULL, NULL, 903, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993159, 1590993506, '用户管理', '2', 'AuditOutlined', '/sys/user', '/sys/user/index', 'sys:user:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993181, 1590993506, '角色管理', '2', 'SlidersOutlined', '/sys/role', '/sys/role/index', 'sys:role:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993195, 1590993102, '菜单管理', '2', 'CopyOutlined', '/sys/menu', '/sys/menu/index', 'sys:menu:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993303, 1590993102, '字典管理', '2', 'BarcodeOutlined', '/sys/dict', '/sys/dict/index', 'sys:dict:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993365, 1590993118, '在线用户', '2', 'ContactsOutlined', '/monitor/online', '/monitor/online/index', 'monitor:online:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993390, 1616398154, '定时任务', '2', 'FileExcelOutlined', '/quartz/job', '/quartz/job/index', 'quartz:job:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993460, 1725692158, '操作日志', '2', 'PicLeftOutlined', '/sys/log', '/sys/log/index', 'sys:log:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993481, 1725692158, '登录日志', '2', 'PicRightOutlined', '/sys/login', '/sys/login/index', 'sys:login:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993506, 0, '系统账户', '1', 'UserOutlined', NULL, NULL, NULL, 901, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993543, 1590993159, '用户新增', '3', '#', NULL, NULL, 'sys:user:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993546, 1590993159, '用户修改', '3', '#', NULL, NULL, 'sys:user:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993549, 1590993159, '用户删除', '3', '#', NULL, NULL, 'sys:user:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993567, 1590993181, '角色新增', '3', '#', NULL, NULL, 'sys:role:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993570, 1590993181, '角色修改', '3', '#', NULL, NULL, 'sys:role:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993579, 1590993181, '角色删除', '3', '#', NULL, NULL, 'sys:role:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993625, 1590993195, '菜单新增', '3', '#', '', '', 'sys:menu:add', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993629, 1590993195, '菜单修改', '3', '#', '', '', 'sys:menu:edit', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993633, 1590993195, '菜单删除', '3', '#', '', '', 'sys:menu:remove', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993734, 1590993303, '字典新增', '3', '#', NULL, NULL, 'sys:dict:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993737, 1590993303, '字典修改', '3', '#', NULL, NULL, 'sys:dict:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993750, 1590993303, '字典删除', '3', '#', NULL, NULL, 'sys:dict:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993793, 1590993460, '操作删除', '3', '#', NULL, NULL, 'sys:log:remove', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993804, 1590993481, '登录删除', '3', '#', NULL, NULL, 'sys:login:remove', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993816, 1590993365, '批量强退', '3', '#', NULL, NULL, 'monitor:online:logout', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993827, 1590993390, '任务新增', '3', '#', NULL, NULL, 'quartz:job:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993831, 1590993390, '任务修改', '3', '#', NULL, NULL, 'quartz:job:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1590993834, 1590993390, '任务删除', '3', '#', NULL, NULL, 'quartz:job:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1612438635, 1590993390, '执行一次', '3', '#', NULL, NULL, 'quartz:job:run', 4, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1616398154, 0, '系统任务', '1', 'AppstoreOutlined', NULL, NULL, NULL, 904, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1616407020, 1590993118, '缓存监控', '2', 'FundProjectionScreenOutlined', '/monitor/cache', '/monitor/cache/index', 'monitor:cache:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1616653528, 1590993195, '菜单复制', '3', '#', NULL, NULL, 'sys:menu:copy', 4, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1660205475, 1616398154, '定时日志', '2', 'FileExclamationOutlined', '/quartz/log', '/quartz/log/index', 'quartz:log:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1660205479, 1660205475, '任务删除', '3', '#', NULL, NULL, 'quartz:log:remove', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667963359, 1667963444, '用户管理', '2', 'UserAddOutlined', '/chat/user', '/chat/user/index', 'chat:user:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667963444, 0, '用户管理', '1', 'UserSwitchOutlined', NULL, NULL, NULL, 101, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667985163, 1673405797, '系统公告', '2', 'ScheduleOutlined', '/operate/notice', '/operate/notice/index', 'operate:notice:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667985164, 1667985163, '查询', '3', '#', '', '', 'operate:notice:query', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667985165, 1667985163, '新增', '3', '#', '', '', 'operate:notice:add', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667985166, 1667985163, '修改', '3', '#', '', '', 'operate:notice:edit', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667985167, 1667985163, '删除', '3', '#', '', '', 'operate:notice:remove', 4, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667987841, 1673405797, '帮助中心', '2', 'LineHeightOutlined', '/operate/help', '/operate/help/index', 'operate:help:list', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667987843, 1667987841, '新增', '3', '#', NULL, NULL, 'operate:help:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667987844, 1667987841, '修改', '3', '#', NULL, NULL, 'operate:help:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667987845, 1667987841, '删除', '3', '#', NULL, NULL, 'operate:help:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667988519, 1673405797, '建议反馈', '2', 'ScheduleOutlined', '/operate/feedback', '/operate/feedback/index', 'operate:feedback:list', 4, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667988523, 1667988519, '删除', '3', '#', NULL, NULL, 'operate:feedback:remove', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667991656, 1667963444, '用户举报', '2', 'UserDeleteOutlined', '/inform/user', '/inform/user/index', 'inform:user:list', 5, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667991660, 1667991656, '处理', '3', '#', NULL, NULL, 'inform:user:banned', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667994420, 1725012573, '系统升级', '2', 'RadiusBottomleftOutlined', '/operate/version', '/operate/version/index', 'operate:version:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667994423, 1667994420, '修改', '3', '#', NULL, NULL, 'operate:version:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1667994424, 1667994420, '删除', '3', '#', NULL, NULL, 'chat:version:remove', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1668075049, 1667963444, '群组管理', '2', 'UsergroupAddOutlined', '/chat/group', '/chat/group/index', 'chat:group:list', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1669114031, 1725704817, '封禁', '3', '#', NULL, NULL, 'chat:user:banned', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1669116129, 1673405797, '首页公告', '2', 'VerticalAlignTopOutlined', '/operate/notify', '/operate/notify/index', 'operate:notify:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1669116132, 1669116129, '修改', '3', '#', NULL, NULL, 'operate:notify:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1669779155, 1668075049, '消息', '3', '#', NULL, NULL, 'chat:group:msg', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1669780522, 1667963444, '群组举报', '2', 'UsergroupDeleteOutlined', '/inform/group', '/inform/group/index', 'inform:group:list', 6, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1669780524, 1669780522, '处理', '3', '#', NULL, NULL, 'inform:group:banned', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222559, 0, '账单统计', '1', 'BarChartOutlined', NULL, NULL, NULL, 103, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222894, 1670222559, '充值记录', '2', 'FilePdfOutlined', '/trade/recharge', '/trade/recharge/index', 'wallet:trade:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222898, 1670222559, '提现记录', '2', 'FileExclamationOutlined', '/trade/cash', '/trade/cash/index', 'wallet:trade:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222900, 1670222559, '转账记录', '2', 'FileImageOutlined', '/trade/transfer', '/trade/transfer/index', 'wallet:trade:list', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222904, 1670222559, '个人红包', '2', 'FilePdfOutlined', '/trade/packet', '/trade/packet/index', 'wallet:trade:list', 5, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222907, 1670222559, '专属红包', '2', 'FilePptOutlined', '/trade/assign', '/trade/assign/index', 'wallet:trade:list', 6, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222910, 1670222559, '手气红包', '2', 'FileExclamationOutlined', '/trade/group', '/trade/group/index', 'wallet:trade:list', 7, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670222913, 1670222559, '普通红包', '2', 'FileZipOutlined', '/trade/normal', '/trade/normal/index', 'wallet:trade:list', 8, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1670236462, 1667963359, '新增', '3', '#', NULL, NULL, 'chat:user:add', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1673405797, 0, '运营管理', '1', 'StrikethroughOutlined', NULL, NULL, NULL, 104, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1673406441, 1725704817, '修改', '3', '#', NULL, NULL, 'chat:user:edit', 4, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1673406506, 1725792755, '修改', '3', '#', NULL, NULL, 'chat:group:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1673424543, 0, '应用扩展', '1', 'GlobalOutlined', NULL, NULL, NULL, 107, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1673580640, 1725704817, '充值', '3', '#', NULL, NULL, 'chat:user:wallet', 5, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1680512107, 1670222559, '扫码转账', '2', 'FileJpgOutlined', '/trade/scan', '/trade/scan/index', 'wallet:trade:list', 4, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1680601640, 0, '审批管理', '1', 'ClearOutlined', NULL, NULL, NULL, 102, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1680601960, 1680601640, '提现审批', '2', 'BorderVerticleOutlined', '/approve/cash', '/approve/cash/index', 'approve:cash:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1680601962, 1680601960, '处理', '3', '#', NULL, NULL, 'approve:cash:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1680606806, 1680601640, '认证审批', '2', 'BorderTopOutlined', '/approve/auth', '/approve/auth/index', 'approve:auth:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1680606808, 1680606806, '处理', '3', '#', NULL, NULL, 'approve:auth:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1682396198, 0, '统计管理', '1', 'AlignLeftOutlined', NULL, NULL, NULL, 106, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1682396199, 1682396198, '用户日活', '2', 'UserOutlined', '/statistics/visit', '/statistics/visit/index', 'statistics:user:visit', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1682396202, 1682396198, '用户增长', '2', 'FileAddOutlined', '/statistics/trend', '/statistics/trend/index', 'statistics:user:trend', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1683788284, 1725704817, '实名', '3', '#', NULL, NULL, 'chat:user:auth', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1683806084, 1670222559, '消费记录', '2', 'FileAddOutlined', '/trade/shopping', '/trade/shopping/index', 'wallet:trade:list', 10, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1689595552, 1725704817, '注销', '3', '#', NULL, NULL, 'chat:user:deleted', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1690877515, 1680601640, '解封审批', '2', 'BorderBottomOutlined', '/approve/banned', '/approve/banned/index', 'approve:banned:list', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1690877517, 1690877515, '处理', '3', '#', NULL, NULL, 'approve:banned:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724569052, 1669116129, 'demo', '3', '#', NULL, NULL, 'operate:notify:query', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724577139, 1682396198, '用户充值', '2', 'NodeExpandOutlined', '/statistics/recharge', '/statistics/recharge/index', 'statistics:user:recharge', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724584046, 1682396198, '用户提现', '2', 'NodeCollapseOutlined', '/statistics/cash', '/statistics/cash/index', 'statistics:user:cash', 4, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724648552, 1673405797, '用户头像', '2', 'UserAddOutlined', '/operate/portrait/user', '/operate/portrait/user/index', 'operate:portrait:user', 5, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651302, 1724648552, '新增', '3', '#', NULL, NULL, 'operate:portrait:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651355, 1724648552, '修改', '3', '#', NULL, NULL, 'operate:portrait:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651378, 1724648552, '删除', '3', '#', NULL, NULL, 'operate:portrait:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651404, 1673405797, '群聊头像', '2', 'UsergroupAddOutlined', '/operate/portrait/group', '/operate/portrait/group/index', 'operate:portrait:group', 6, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651405, 1724651404, '新增', '3', '#', NULL, NULL, 'operate:portrait:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651407, 1724651404, '修改', '3', '#', NULL, NULL, 'operate:portrait:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724651408, 1724651404, '删除', '3', '#', NULL, NULL, 'operate:portrait:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724663909, 1725012573, '充值配置', '2', 'RadiusBottomrightOutlined', '/operate/recharge', '/operate/recharge/index', 'operate:recharge:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724663910, 1724663909, '修改', '3', '#', '', '', 'operate:recharge:edit', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724668523, 1725012573, '群组扩容', '2', 'RadiusUpleftOutlined', '/operate/group', '/operate/group/index', 'operate:group:list', 4, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724668524, 1724668523, '修改', '3', '#', '', '', 'operate:group:edit', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724742619, 1725012573, '提现配置', '2', 'RadiusSettingOutlined', '/operate/cash', '/operate/cash/index', 'operate:cash:list', 3, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724742620, 1724742619, '修改', '3', '#', '', '', 'operate:cash:edit', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724744484, 1725012573, '配置中心', '2', 'RadiusUprightOutlined', '/operate/config', '/operate/config/index', 'operate:config:list', 5, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1724744485, 1724744484, '修改', '3', '#', '', '', 'operate:config:edit', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725012573, 0, '配置管理', '1', 'AppstoreOutlined', NULL, NULL, NULL, 105, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725692158, 0, '系统日志', '1', 'TableOutlined', NULL, NULL, NULL, 905, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725704817, 1667963444, '用户详情', '2', 'AntDesignOutlined', '/chat/user/info', '/chat/user/index-info', 'chat:user:query', 2, 'N', '', 'Y', 'N');
INSERT INTO `sys_menu` VALUES (1725792755, 1667963444, '群组详情', '2', '#', '/chat/group/info', '/chat/group/index-info', 'chat:group:query', 4, 'N', '', 'Y', 'N');
INSERT INTO `sys_menu` VALUES (1725849590, 1725704817, '群组', '3', '#', NULL, NULL, 'chat:user:group', 8, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725849605, 1725704817, '日志', '3', '#', NULL, NULL, 'chat:user:log', 6, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725849899, 1725704817, '好友', '3', '#', NULL, NULL, 'chat:user:friend', 7, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725854289, 1725792755, '日志', '3', '#', NULL, NULL, 'chat:group:log', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1725960694, 1725704817, '消息', '3', '#', NULL, NULL, 'chat:user:msg', 9, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1726030934, 1725792755, '封禁', '3', '#', NULL, NULL, 'chat:group:banned', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1726122468, 1667988519, '查询', '3', '#', NULL, NULL, 'operate:feedback:query', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1728638324, 1673424543, '服务管理', '2', 'RadarChartOutlined', '/extend/robot', '/extend/robot/index', 'extend:robot:list', 1, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1728639762, 1728638324, '修改', '3', '#', NULL, NULL, 'extend:robot:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1728980776, 1728638324, '回复', '3', '#', NULL, NULL, 'extend:robot:reply', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1728990347, 1670222559, '群内转账', '2', 'FileUnknownOutlined', '/trade/interior', '/trade/interior/index', 'wallet:trade:list', 9, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1729051469, 1682396198, '收支汇总', '2', 'TransactionOutlined', '/statistics/report', '/statistics/report/index', 'statistics:user:report', 5, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1731916066, 1680601640, '异常账户', '2', 'BorderInnerOutlined', '/approve/special', '/approve/special/index', 'approve:special:list', 4, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1734420922, 1682396198, '平台汇总', '2', 'CrownOutlined', '/statistics/balance', '/statistics/balance/index', 'statistics:user:balance', 6, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1734695226, 1673424543, '应用管理', '2', 'AimOutlined', '/extend/uni', '/extend/uni/index', 'extend:uni:list', 2, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1734695227, 1734695226, '新增', '3', '#', NULL, NULL, 'extend:uni:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1734695228, 1734695226, '删除', '3', '#', NULL, NULL, 'extend:uni:remove', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1734695304, 1734695226, '修改', '3', '#', NULL, NULL, 'extend:uni:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1735786711, 1680601960, '导出', '3', '#', NULL, NULL, 'approve:cash:export', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1738810367, 1667963359, '详情', '3', '#', NULL, NULL, 'chat:user:list	', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1738810399, 1668075049, '详情', '3', '#', NULL, NULL, 'chat:group:list', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1745553948, 1725012573, '数据中心', '2', 'RadiusUpleftOutlined', '/operate/setting', '/operate/setting/index', 'operate:setting:list', 6, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1745553949, 1745553948, '修改', '3', '#', NULL, NULL, 'operate:setting:edit', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752214246, 1752215865, '评论表管理', '2', 'ArrowRightOutlined', '/friend/comments', '/friend/comments/index', 'friend:comments:list', 1, 'N', '朋友圈评论表管理菜单', 'Y', 'N');
INSERT INTO `sys_menu` VALUES (1752214247, 1752214246, '朋友圈评论表查询', '3', '#', NULL, NULL, 'friend:comments:query', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752214248, 1752214246, '朋友圈评论表新增', '3', '#', NULL, NULL, 'friend:comments:add', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752214249, 1752214246, '朋友圈评论表修改', '3', '#', NULL, NULL, 'friend:comments:edit', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752214250, 1752214246, '朋友圈评论表删除', '3', '#', NULL, NULL, 'friend:comments:remove', 4, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752215865, 1667963444, '朋友圈管理', '1', 'AliwangwangOutlined', NULL, NULL, NULL, 0, 'N', NULL, 'Y', 'N');
INSERT INTO `sys_menu` VALUES (1752225309, 1667963444, '圈子信息管理', '2', 'AliwangwangOutlined', '/friend/moments', '/friend/moments/index', 'friend:moments:list', 1, 'N', '朋友圈评论表管理菜单', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752225310, 1752225309, '信息表查询', '3', '#', NULL, NULL, 'friend:moments:query', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752225311, 1752225309, '信息新增', '3', '#', NULL, NULL, 'friend:moments:add', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752225312, 1752225309, '信息修改', '3', '#', NULL, NULL, 'friend:moments:edit', 3, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1752225313, 1752225309, '信息表删除', '3', '#', NULL, NULL, 'friend:moments:remove', 4, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1754493737, 1670222559, '用户签到', '2', 'FilePdfOutlined', '/trade/sign', '/trade/sign/index', 'wallet:trade:list', 11, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1755938828, 1673405797, '系统网页', '2', 'UsergroupAddOutlined', '/sys/html', '/sys/html/index', 'sys:html:list', 7, 'N', '', 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1755938829, 1755938828, '新增', '3', '#', NULL, NULL, 'sys:html:add', 1, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1755938830, 1755938828, '修改', '3', '#', NULL, NULL, 'sys:html:edit', 2, 'N', NULL, 'Y', 'Y');
INSERT INTO `sys_menu` VALUES (1755938831, 1755938828, '删除', '3', '#', NULL, NULL, 'sys:html:remove', 3, 'N', NULL, 'Y', 'Y');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Y' COMMENT '角色状态（Y正常N停用）',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name`) USING BTREE,
  UNIQUE INDEX `role_key`(`role_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1613012639384887298, '运营', 'admin', 1, 'Y', '运营');
INSERT INTO `sys_role` VALUES (1939279673435381761, '客服', 'kf', 300, 'Y', '客服');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1590993460);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1590993481);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1590993793);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1590993804);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1667963359);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1667963444);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1667991656);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1667991660);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1668075049);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1669114031);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1669779155);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1669780522);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1669780524);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1670236462);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1673406441);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1673406506);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1673580640);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1680606806);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1680606808);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1683788284);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1689595552);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1690877515);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1690877517);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725692158);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725704817);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725792755);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725849590);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725849605);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725849899);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725854289);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1725960694);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1726030934);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1731916066);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1738810367);
INSERT INTO `sys_role_menu` VALUES (1939279673435381761, 1738810399);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT 0 COMMENT '角色id',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `nickname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `salt` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '盐',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'Y' COMMENT '帐号状态（Y正常N停用）',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1613012757525848066, 1613012639384887298, 'admin', 'admin', 'u32t', '1f81a659afbc2f6275154cd50e70a136', 'Y', NULL);
INSERT INTO `sys_user` VALUES (1939279749394227201, 1939279673435381761, 'kf001', 'kf', 'h5oz', '55169619574fc49fafd4ac870e79203a', 'Y', '');

-- ----------------------------
-- Table structure for uni_item
-- ----------------------------
DROP TABLE IF EXISTS `uni_item`;
CREATE TABLE `uni_item`  (
  `uni_id` bigint(20) NOT NULL COMMENT '主键',
  `app_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'appId',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `version` bigint(20) NULL DEFAULT 100 COMMENT '版本',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '状态',
  PRIMARY KEY (`uni_id`) USING BTREE,
  UNIQUE INDEX `appId`(`app_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '小程序表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of uni_item
-- ----------------------------
INSERT INTO `uni_item` VALUES (10001, NULL, '百度一下', 'http://110.42.56.25:19000/xim/root/4.png', 100, 'https://www.baidu.com/', 'url', 'Y');
INSERT INTO `uni_item` VALUES (10002, '__UNI__E28E426', '天气预报', 'http://110.42.56.25:19000/xim/root/5.png', 100, 'https://baidu.com/alpaca/wgt/__UNI__E28E426.wgt', 'mini', 'Y');
INSERT INTO `uni_item` VALUES (10003, '__UNI__50FBB74', '授权示例', 'http://110.42.56.25:19000/xim/root/6.png', 100, 'https://baidu.com/alpaca/wgt/__UNI__50FBB74.wgt', 'mini', 'Y');

-- ----------------------------
-- Table structure for wallet_bank
-- ----------------------------
DROP TABLE IF EXISTS `wallet_bank`;
CREATE TABLE `wallet_bank`  (
  `bank_id` bigint(20) NOT NULL COMMENT '卡包id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `wallet` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户',
  PRIMARY KEY (`bank_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包卡包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_bank
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_cash
-- ----------------------------
DROP TABLE IF EXISTS `wallet_cash`;
CREATE TABLE `wallet_cash`  (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `wallet` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户',
  `amount` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '申请金额',
  `rate` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '交易利率',
  `cost` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '交易加成',
  `charge` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '交易手续',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `create_time` datetime NULL DEFAULT NULL COMMENT '交易时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`trade_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包提现' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_cash
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_info
-- ----------------------------
DROP TABLE IF EXISTS `wallet_info`;
CREATE TABLE `wallet_info`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `balance` decimal(65, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `salt` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐巴',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `version` int(8) NULL DEFAULT 0 COMMENT '版本',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户钱包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_info
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_packet
-- ----------------------------
DROP TABLE IF EXISTS `wallet_packet`;
CREATE TABLE `wallet_packet`  (
  `packet_id` bigint(20) NOT NULL COMMENT '主键',
  `trade_id` bigint(20) NULL DEFAULT NULL COMMENT '交易id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '接收id',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收no',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像',
  `amount` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '金额',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`packet_id`) USING BTREE,
  UNIQUE INDEX `trade_id`(`trade_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包红包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_packet
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_receive
-- ----------------------------
DROP TABLE IF EXISTS `wallet_receive`;
CREATE TABLE `wallet_receive`  (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '接收人',
  `amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '金额',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '状态',
  `version` int(8) NULL DEFAULT 0 COMMENT '执行版本',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`trade_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包余额' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_receive
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_recharge
-- ----------------------------
DROP TABLE IF EXISTS `wallet_recharge`;
CREATE TABLE `wallet_recharge`  (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户号码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `amount` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '支付金额',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易号码',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易号码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '交易时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `pay_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付类型',
  PRIMARY KEY (`trade_id`) USING BTREE,
  UNIQUE INDEX `trade_no`(`trade_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包充值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_recharge
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_shopping
-- ----------------------------
DROP TABLE IF EXISTS `wallet_shopping`;
CREATE TABLE `wallet_shopping`  (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户号码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `amount` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '支付金额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '交易时间',
  PRIMARY KEY (`trade_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包消费' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_shopping
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_task
-- ----------------------------
DROP TABLE IF EXISTS `wallet_task`;
CREATE TABLE `wallet_task`  (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `trade_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  `task_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '执行状态',
  `version` int(8) NULL DEFAULT 0 COMMENT '执行版本',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`trade_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_task
-- ----------------------------

-- ----------------------------
-- Table structure for wallet_trade
-- ----------------------------
DROP TABLE IF EXISTS `wallet_trade`;
CREATE TABLE `wallet_trade`  (
  `trade_id` bigint(20) NOT NULL COMMENT '主键',
  `trade_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  `trade_packet` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '是否红包',
  `trade_amount` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `trade_count` int(8) NULL DEFAULT 1 COMMENT '交易数量',
  `trade_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易备注',
  `trade_balance` decimal(65, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `trade_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易状态',
  `source_id` bigint(20) NULL DEFAULT 0 COMMENT '交易来源',
  `source_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易来源',
  `user_id` bigint(20) NULL DEFAULT 0 COMMENT '用户id',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户号码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用户头像',
  `group_id` bigint(20) NULL DEFAULT 0 COMMENT '群组',
  `group_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群号',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群名',
  `receive_id` bigint(20) NULL DEFAULT 0 COMMENT '接收id',
  `receive_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收号码',
  `receive_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收昵称',
  `receive_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收手机',
  `receive_portrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收头像',
  `create_time` datetime NULL DEFAULT NULL COMMENT '生成时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '注销0正常null注销',
  PRIMARY KEY (`trade_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `receive_id`(`receive_id`) USING BTREE,
  INDEX `group_id`(`group_id`) USING BTREE,
  INDEX `trade_type`(`trade_type`) USING BTREE,
  INDEX `trade_packet`(`trade_packet`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包交易总表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of wallet_trade
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
