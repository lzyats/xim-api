CREATE TABLE `chat_robot` (
  `robot_id` bigint(20) NOT NULL COMMENT '主键',
  `secret` varchar(32) DEFAULT NULL COMMENT '秘钥',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `portrait` varchar(200) DEFAULT NULL COMMENT '头像',
  `menu` longtext COMMENT '菜单',
  PRIMARY KEY (`robot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务号';

insert into `chat_robot` (`robot_id`, `secret`, `nickname`, `portrait`, `menu`) values('10001','8ykc55fcq1fc21agt11qtni60hujhrxf','在线客服','https://img.alicdn.com/imgextra/i4/87413133/O1CN01NxWkgo1Z0xqvPDYbs_!!87413133.png','[]');
insert into `chat_robot` (`robot_id`, `secret`, `nickname`, `portrait`, `menu`) values('10002','qry41hxsjg8l4kg242z5s1u91oxll8b','支付助手','https://img.alicdn.com/imgextra/i2/87413133/O1CN01sVp8VY1Z0xsCzOdWE_!!87413133.png','[]');
insert into `chat_robot` (`robot_id`, `secret`, `nickname`, `portrait`, `menu`) values('10003','zgs5ibsx565wn4ccbb3hqlnozwyiktm9','羊驼助手','https://img.alicdn.com/imgextra/i4/87413133/O1CN01V9Um9U1Z0xs1UA7iE_!!87413133.png','[]');
