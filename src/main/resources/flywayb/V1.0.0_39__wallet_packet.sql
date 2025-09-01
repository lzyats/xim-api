CREATE TABLE `wallet_packet` (
  `packet_id` bigint(20) NOT NULL COMMENT '主键',
  `trade_id` bigint(20) DEFAULT NULL COMMENT '交易id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '接收id',
  `user_no` varchar(32) DEFAULT NULL COMMENT '接收no',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `portrait` longtext COMMENT '头像',
  `amount` decimal(8,2) DEFAULT '0.00' COMMENT '金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`packet_id`),
  UNIQUE KEY `trade_id` (`trade_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包红包';
