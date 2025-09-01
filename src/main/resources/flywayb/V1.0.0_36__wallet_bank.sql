CREATE TABLE `wallet_bank` (
  `bank_id` bigint(20) NOT NULL COMMENT '卡包id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `wallet` varchar(200) DEFAULT NULL COMMENT '账户',
  PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包卡包';
