CREATE TABLE `chat_robot_sub` (
  `sub_id` bigint(20) NOT NULL COMMENT '主键',
  `robot_id` bigint(20) DEFAULT NULL COMMENT '机器人',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `top` char(1) DEFAULT 'N' COMMENT '置顶',
  `disturb` char(1) DEFAULT 'N' COMMENT '静默',
  PRIMARY KEY (`sub_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务号';
