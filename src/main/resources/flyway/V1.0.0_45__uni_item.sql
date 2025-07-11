CREATE TABLE `uni_item` (
  `uni_id` bigint(20) NOT NULL COMMENT '主键',
  `app_id` varchar(20) DEFAULT NULL COMMENT 'appId',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `version` bigint(20) DEFAULT '100' COMMENT '版本',
  `path` varchar(200) DEFAULT NULL COMMENT '地址',
  `type` varchar(20) DEFAULT NULL COMMENT '类型',
  `status` char(1) DEFAULT 'N' COMMENT '状态',
  PRIMARY KEY (`uni_id`),
  UNIQUE KEY `appId` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序表';

insert into `uni_item` (`uni_id`, `app_id`, `name`, `icon`, `version`, `path`, `type`, `status`) values('10001',NULL,'百度一下','https://img.alicdn.com/imgextra/i2/87413133/O1CN01sm9ayS1Z0xsyy6UG8_!!87413133.jpg','100','https://www.baidu.com/','url','Y');
insert into `uni_item` (`uni_id`, `app_id`, `name`, `icon`, `version`, `path`, `type`, `status`) values('10002','__UNI__E28E426','天气预报','https://img.alicdn.com/imgextra/i3/87413133/O1CN011GqQfO1Z0xszOTtqa_!!87413133.jpg','100','https://baidu.com/alpaca/wgt/__UNI__E28E426.wgt','mini','Y');
insert into `uni_item` (`uni_id`, `app_id`, `name`, `icon`, `version`, `path`, `type`, `status`) values('10003','__UNI__50FBB74','授权示例','https://img.alicdn.com/imgextra/i4/87413133/O1CN01JHvuTb1Z0xswBOXWk_!!87413133.jpg','100','https://baidu.com/alpaca/wgt/__UNI__50FBB74.wgt','mini','Y');
