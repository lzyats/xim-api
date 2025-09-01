CREATE TABLE `chat_friend_inform` (
  `inform_id` bigint(20) NOT NULL COMMENT '主键',
  `inform_type` char(1) DEFAULT NULL COMMENT '类型',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `group_id` bigint(20) DEFAULT NULL COMMENT '目标id',
  `images` longtext COMMENT '图片',
  `content` longtext COMMENT '内容',
  `status` char(1) DEFAULT 'N' COMMENT '处理状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`inform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骚扰举报';