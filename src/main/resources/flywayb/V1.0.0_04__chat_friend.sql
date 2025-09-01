CREATE TABLE `chat_friend` (
  `friend_id` bigint(20) NOT NULL COMMENT '主键',
  `current_id` bigint(20) DEFAULT NULL COMMENT '当前id',
  `group_id` bigint(20) DEFAULT NULL COMMENT '群组id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `portrait` longtext COMMENT '头像',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `user_no` varchar(32) DEFAULT NULL COMMENT '聊天号码',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  `source` char(1) DEFAULT NULL COMMENT '来源',
  `black` char(1) DEFAULT 'N' COMMENT '黑名单',
  `top` char(1) DEFAULT 'N' COMMENT '置顶',
  `disturb` char(1) DEFAULT 'N' COMMENT '静默',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '注销0正常null注销',
  PRIMARY KEY (`friend_id`),
  UNIQUE KEY `user_id` (`user_id`,`current_id`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友表';
