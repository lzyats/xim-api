CREATE TABLE `chat_group_solitaire` (
  `solitaire_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发起人',
  `group_id` bigint(20) DEFAULT NULL COMMENT '群组',
  `subject` varchar(50) DEFAULT NULL COMMENT '主题',
  `example` varchar(200) DEFAULT NULL COMMENT '例子',
  `content` longtext COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`solitaire_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成语接龙';
