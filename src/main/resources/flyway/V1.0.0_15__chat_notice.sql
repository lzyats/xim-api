CREATE TABLE `chat_notice` (
  `notice_id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(20) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `status` char(1) DEFAULT 'N' COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告';

insert into `chat_notice` (`notice_id`, `title`, `content`, `status`, `create_time`) values('1613477970402439169','测试公告','重要通知：近期诈骗犯罪案件时有发生，为防止您在经济上蒙受损失，请您接到陌生人或以熟人名义要求转账、汇款时，务必提高警惕，以防受骗','Y','2023-01-01 00:00:00');
