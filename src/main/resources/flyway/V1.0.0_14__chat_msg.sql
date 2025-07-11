CREATE TABLE `chat_msg` (
  `msg_id` bigint(20) NOT NULL COMMENT '消息主键',
  `sync_id` bigint(20) DEFAULT NULL COMMENT '同步id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发送人',
  `receive_id` bigint(20) DEFAULT NULL COMMENT '接收人',
  `group_id` bigint(20) DEFAULT NULL COMMENT '群id',
  `talk_type` char(1) DEFAULT NULL COMMENT '聊天类型',
  `msg_type` varchar(20) DEFAULT NULL COMMENT '消息类型',
  `content` longtext COMMENT '消息内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息';
