CREATE TABLE `chat_friend_apply` (
  `apply_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `portrait` longtext COMMENT '用户头像',
  `nickname` varchar(32) DEFAULT NULL COMMENT '用户昵称',
  `user_no` varchar(32) DEFAULT NULL COMMENT '聊天号码',
  `reason` varchar(200) DEFAULT NULL COMMENT '理由',
  `receive_id` bigint(20) DEFAULT NULL COMMENT '接收id',
  `receive_remark` varchar(20) DEFAULT NULL COMMENT '接收备注',
  `source` char(1) DEFAULT NULL COMMENT '申请来源',
  `status` char(1) DEFAULT NULL COMMENT '申请状态',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友申请';
