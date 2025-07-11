CREATE TABLE `wallet_receive` (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '接收人',
  `amount` decimal(8,2) DEFAULT NULL COMMENT '金额',
  `status` char(1) DEFAULT 'N' COMMENT '状态',
  `version` int(8) DEFAULT '0' COMMENT '执行版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`trade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包余额';