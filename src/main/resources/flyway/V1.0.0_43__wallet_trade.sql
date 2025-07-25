CREATE TABLE `wallet_trade` (
  `trade_id` bigint(20) NOT NULL COMMENT '主键',
  `trade_type` varchar(4) DEFAULT NULL COMMENT '交易类型',
  `trade_amount` decimal(8,2) DEFAULT '0.00' COMMENT '交易金额',
  `trade_count` int(8) DEFAULT '1' COMMENT '交易数量',
  `trade_remark` varchar(200) DEFAULT NULL COMMENT '交易备注',
  `trade_balance` decimal(65,2) DEFAULT '0.00' COMMENT '余额',
  `trade_status` char(1) DEFAULT NULL COMMENT '交易状态',
  `source_id` bigint(20) DEFAULT '0' COMMENT '交易来源',
  `source_type` varchar(4) DEFAULT NULL COMMENT '交易来源',
  `user_id` bigint(20) DEFAULT '0' COMMENT '用户id',
  `user_no` varchar(32) DEFAULT NULL COMMENT '用户号码',
  `nickname` varchar(20) DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(50) DEFAULT NULL COMMENT '用户手机',
  `portrait` longtext COMMENT '用户头像',
  `group_id` bigint(20) DEFAULT '0' COMMENT '群组',
  `group_no` varchar(32) DEFAULT NULL COMMENT '群号',
  `group_name` varchar(50) DEFAULT NULL COMMENT '群名',
  `receive_id` bigint(20) DEFAULT '0' COMMENT '接收id',
  `receive_no` varchar(32) DEFAULT NULL COMMENT '接收号码',
  `receive_name` varchar(20) DEFAULT NULL COMMENT '接收昵称',
  `receive_phone` varchar(50) DEFAULT NULL COMMENT '接收手机',
  `receive_portrait` longtext COMMENT '接收头像',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '注销0正常null注销',
  PRIMARY KEY (`trade_id`),
  KEY `user_id` (`user_id`),
  KEY `receive_id` (`receive_id`),
  KEY `group_id` (`group_id`),
  KEY `trade_type` (`trade_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包交易';