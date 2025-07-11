CREATE TABLE `chat_user_deleted` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '注销时间',
  PRIMARY KEY (`user_id`),
  KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='注销表';
