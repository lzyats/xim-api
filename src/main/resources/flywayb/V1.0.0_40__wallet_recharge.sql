CREATE TABLE `wallet_recharge` (
  `trade_id` bigint(20) NOT NULL COMMENT '交易id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_no` varchar(50) DEFAULT NULL COMMENT '用户号码',
  `phone` varchar(50) DEFAULT NULL COMMENT '用户手机',
  `nickname` varchar(20) DEFAULT NULL COMMENT '用户昵称',
  `amount` decimal(8,2) DEFAULT '0.00' COMMENT '支付金额',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '交易号码',
  `order_no` varchar(50) DEFAULT NULL COMMENT '交易号码',
  `create_time` datetime DEFAULT NULL COMMENT '交易时间',
  `update_time` datetime DEFAULT NULL COMMENT '处理时间',
  `pay_type` char(1) DEFAULT NULL COMMENT '支付类型',
  PRIMARY KEY (`trade_id`),
  UNIQUE KEY `trade_no` (`trade_no`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包充值';