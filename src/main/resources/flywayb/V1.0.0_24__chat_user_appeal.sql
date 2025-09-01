CREATE TABLE `chat_user_appeal` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `images` longtext COMMENT '图片',
  `content` longtext COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户申诉';