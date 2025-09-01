CREATE TABLE `chat_group_log` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) DEFAULT NULL COMMENT '群组id',
  `log_type` varchar(4) DEFAULT NULL COMMENT '日志类型',
  `content` varchar(200) DEFAULT NULL COMMENT '操作内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群组日志';
