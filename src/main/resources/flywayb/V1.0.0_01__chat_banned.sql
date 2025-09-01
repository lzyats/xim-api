CREATE TABLE `chat_banned` (
  `banned_id` bigint(20) NOT NULL COMMENT '封禁id',
  `banned_reason` varchar(200) DEFAULT NULL COMMENT '封禁原因',
  `banned_time` datetime DEFAULT NULL COMMENT '封禁时间',
  PRIMARY KEY (`banned_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='封禁状态';
