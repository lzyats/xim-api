CREATE TABLE `chat_config` (
  `config_key` varchar(32) NOT NULL COMMENT 'key',
  `config_value` longtext COMMENT 'value',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_key`),
  UNIQUE KEY `idx_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设置表';

insert into `chat_config` (`config_key`, `config_value`, `remark`) values('apply_friend','50','申请好友单日次数');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('apply_group','30','申请群组单日次数');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('group_level_count','50','群组成员默认数量');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('notice_content','重要通知：近期诈骗犯罪案件时有发生，为防止您在经济上蒙受损失，请您接到陌生人或以熟人名义要求转账、汇款时，务必提高警惕，以防受骗','系统通告');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('notice_status','Y','系统通告开关');
insert INTO `chat_config` (`config_key`, `config_value`, `remark`) VALUES('sys_captcha',FLOOR(RAND() * 9000 + 1000),'系统验证码');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_packet','200','红包金额');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_phone','13800000000','审核账号');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_project','羊驼IM','系统名称');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_recall','15','撤回时间');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_share','https://www.baidu.com','分享页面');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_audit','N','审核开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_beian','我是备案信息','备案信息');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_nickname','羊驼IM用户','注册昵称');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_hook','','WebHook地址');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_watermark','','水印页面');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('sys_screenshot','Y','系统截屏');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('user_deleted','7','用户注销间隔');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('user_register','Y','用户注册开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('user_sms','N','用户短信开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_auth','N','钱包提现认证开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_cost','1','钱包提现加成金额');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_count','3','钱包提现单日次数');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_max','1000','钱包提现单日最大金额');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_min','1','钱包提现单日最小金额');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_rate','1','钱包提现手续费比率');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_cash_remark','预计3个工作日内处理','钱包提现提醒消息');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_recharge_android','1,2','钱包充值安卓开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_recharge_count','3','钱包充值单日次数');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('wallet_recharge_ios','1,2','钱包充值苹果开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('group_name_search','N','群组名称搜索开关');
insert into `chat_config` (`config_key`, `config_value`, `remark`) values('user_hold','Y','用户手持开关');
