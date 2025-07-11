CREATE TABLE `chat_group_apply` (
  `apply_id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(32) DEFAULT NULL COMMENT '用户昵称',
  `portrait` longtext COMMENT '用户头像',
  `group_id` bigint(20) DEFAULT NULL COMMENT '群组id',
  `group_name` varchar(50) DEFAULT NULL COMMENT '群组名称',
  `receive_id` bigint(20) DEFAULT NULL COMMENT '接收人id',
  `apply_status` char(1) DEFAULT NULL COMMENT '申请状态0无1同意2拒绝3忽略',
  `apply_source` char(1) DEFAULT NULL COMMENT '申请来源',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`apply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群组申请表';
