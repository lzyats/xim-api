CREATE TABLE `chat_voice` (
  `msg_id` bigint(20) NOT NULL COMMENT '主键',
  `voice_url` longtext COMMENT '地址',
  `voice_text` longtext COMMENT '文本',
  `user_id` bigint(20) DEFAULT '0' COMMENT '用户',
  `create_time` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='声音表';
