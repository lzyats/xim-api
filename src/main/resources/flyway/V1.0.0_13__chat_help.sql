CREATE TABLE `chat_help` (
  `help_id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `status` char(1) DEFAULT 'Y' COMMENT '状态',
  `sort` smallint(2) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`help_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天帮助';

insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105731','如何迁移/备份聊天记录','目前版本不支持备份与恢复。','Y','1');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105732','聊天记录清空后还能找回吗','目前采用的是端对端加密传输，消息只记录在用户的终端设备上，一旦删除或撤回，就无法恢复。','Y','2');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105733','APP后台/锁屏后接收不到新消息通知','进入手机“设置”“应用管理”找到《{}》进入权限管理赋予自启动、后台弹窗、悬浮窗、后台唤起权限/后台弹窗权限。','Y','3');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105734','APP后台总是被清理','进入手机“设置”“应用自启动设置”找到《{}》设置“允许自启动”或者“允许后台运行”。','Y','4');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105735','APP后台/锁屏后接听不到语音/视频通话','进入手机“设置”“应用管理”找到《{}》进入权限管理赋予自启动、悬浮窗、后台唤起权限/后台弹窗权限。','Y','5');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105736','如何开启消息通知','进入“我的”页面点击“软件设置”开启“消息声音”或“消息通知”。','Y','6');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105737','怎么添加好友','进入“消息”或“好友”页面点击右上角加号“添加好友”。','Y','7');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105738','怎么同意/拒绝添加好友','进入“好友”页面点击“验证信息”可以看到，好友申请列表，点击“忽略”或“同意”进行操作。','Y','8');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105739','怎么切换账号','进入“我的”页面点击“账号安全”点击“退出登录”进行操作。','Y','9');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105740','怎么查看用户服务协议/隐私协议','进入“我的”页面点击“软件设置”点击“服务协议”或“隐私协议”进行查看。','Y','10');
insert into `chat_help` (`help_id`, `title`, `content`, `status`, `sort`) values('1562386781612105741','怎么查看我的个人信息收集情况','进入“我的”页面点击“软件设置”点击“信息收集”进行查看。','Y','11');
