CREATE TABLE `chat_user_log` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `log_type` varchar(4) DEFAULT NULL COMMENT '类型',
  `content` varchar(200) DEFAULT NULL COMMENT '操作内容',
  `ip` varchar(128) DEFAULT NULL COMMENT 'ip',
  `ip_addr` varchar(128) DEFAULT NULL COMMENT 'ip地址',
  `device_type` varchar(32) DEFAULT NULL COMMENT '设备类型',
  `device_version` varchar(32) DEFAULT NULL COMMENT '设备版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户日志';