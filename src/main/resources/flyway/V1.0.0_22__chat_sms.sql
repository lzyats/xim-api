CREATE TABLE `chat_sms` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `mobile` varchar(50) DEFAULT '0' COMMENT '手机号',
  `status` char(1) DEFAULT 'Y' COMMENT '状态',
  `body` longtext COMMENT '结果',
  `create_time` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信记录';
