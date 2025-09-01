CREATE TABLE `chat_visit` (
  `visit_id` bigint(20) NOT NULL COMMENT '访问id',
  `visit_date` date DEFAULT NULL COMMENT '访问时间',
  `visit_count` int(8) DEFAULT '0' COMMENT '访问次数',
  PRIMARY KEY (`visit_id`),
  UNIQUE KEY `visit_date` (`visit_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户访问';
