CREATE TABLE `wallet_cash` (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `wallet` varchar(200) DEFAULT NULL COMMENT '账户',
  `amount` decimal(8,2) DEFAULT '0.00' COMMENT '申请金额',
  `rate` decimal(8,2) DEFAULT '0.00' COMMENT '交易利率',
  `cost` decimal(8,2) DEFAULT '0.00' COMMENT '交易加成',
  `charge` decimal(8,2) DEFAULT '0.00' COMMENT '交易手续',
  `reason` varchar(200) DEFAULT NULL COMMENT '拒绝原因',
  `create_time` datetime DEFAULT NULL COMMENT '交易时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`trade_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包提现';