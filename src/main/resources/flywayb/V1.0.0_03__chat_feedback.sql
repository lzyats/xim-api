CREATE TABLE `chat_feedback` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `images` longtext COMMENT '图片',
  `content` longtext COMMENT '内容',
  `version` varchar(20) DEFAULT NULL COMMENT '提交版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建议反馈';
