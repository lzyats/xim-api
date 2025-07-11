CREATE TABLE `chat_robot_reply` (
  `reply_id` bigint(20) NOT NULL COMMENT '主键',
  `robot_id` bigint(20) DEFAULT NULL COMMENT '机器人',
  `reply_type` varchar(20) DEFAULT NULL COMMENT '类型',
  `reply_key` varchar(200) DEFAULT NULL COMMENT '关键字',
  `content` longtext COMMENT '内容',
  PRIMARY KEY (`reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务号';
