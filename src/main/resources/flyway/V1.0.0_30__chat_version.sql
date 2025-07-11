CREATE TABLE `chat_version` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `version` varchar(20) DEFAULT NULL COMMENT '版本',
  `device` varchar(20) DEFAULT NULL COMMENT '设备',
  `url` longtext COMMENT '地址',
  `content` longtext COMMENT '内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `device` (`device`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统版本';

insert into `chat_version` (`id`, `version`, `device`, `url`, `content`) values('1561613225252913110','1.0.0','android','https://www.baidu.com/demo.apk','我是安卓包');
insert into `chat_version` (`id`, `version`, `device`, `url`, `content`) values('1561613225252913111','1.0.0','ios','https://www.baidu.com/test','我是苹果包');

