CREATE TABLE `chat_user_token` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `token` varchar(256) DEFAULT NULL COMMENT 'token',
  `device` varchar(32) DEFAULT NULL COMMENT '设备',
  `device_type` varchar(32) DEFAULT NULL COMMENT '设备',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户token';
