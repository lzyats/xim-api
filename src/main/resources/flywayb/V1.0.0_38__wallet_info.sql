CREATE TABLE `wallet_info` (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `balance` decimal(65,2) DEFAULT '0.00' COMMENT '余额',
  `salt` varchar(4) DEFAULT NULL COMMENT '盐巴',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `version` int(8) DEFAULT '0' COMMENT '版本',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包';
