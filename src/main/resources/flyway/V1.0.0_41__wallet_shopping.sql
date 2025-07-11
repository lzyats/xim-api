CREATE TABLE `wallet_shopping` (
  `trade_id` BIGINT(20) NOT NULL COMMENT '交易id',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
  `user_no` VARCHAR(50) DEFAULT NULL COMMENT '用户号码',
  `phone` VARCHAR(50) DEFAULT NULL COMMENT '用户手机',
  `nickname` VARCHAR(20) DEFAULT NULL COMMENT '用户昵称',
  `amount` DECIMAL(8,2) DEFAULT '0.00' COMMENT '支付金额',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '交易备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '交易时间',
  PRIMARY KEY (`trade_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='钱包消费';