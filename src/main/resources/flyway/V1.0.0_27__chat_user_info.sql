CREATE TABLE `chat_user_info` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证',
  `identity1` varchar(200) DEFAULT NULL COMMENT '正面',
  `identity2` varchar(200) DEFAULT NULL COMMENT '反面',
  `hold_card` varchar(200) DEFAULT NULL COMMENT '手持',
  `auth_reason` varchar(200) DEFAULT NULL COMMENT '认证原因',
  `auth_time` datetime DEFAULT NULL COMMENT '认证时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详情';
