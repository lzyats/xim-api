CREATE TABLE `wallet_task` (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `trade_type` varchar(4) DEFAULT NULL COMMENT '交易类型',
  `task_time` datetime DEFAULT NULL COMMENT '执行时间',
  `status` char(1) DEFAULT 'N' COMMENT '执行状态',
  `version` int(8) DEFAULT '0' COMMENT '执行版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`trade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包任务';
