CREATE TABLE `sys_log` (
  `log_id` bigint(20) NOT NULL COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `log_type` char(2) DEFAULT '0' COMMENT '业务类型',
  `request_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `request_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `username` varchar(50) DEFAULT '' COMMENT '操作人员',
  `ip_addr` varchar(50) DEFAULT '' COMMENT '主机地址',
  `location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `message` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` char(1) DEFAULT 'N' COMMENT '操作状态（Y正常N异常）',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志记录';

CREATE TABLE `sys_login` (
  `log_id` bigint(20) NOT NULL COMMENT '访问ID',
  `username` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ip_addr` varchar(50) DEFAULT '' COMMENT '登录IP地址',
  `location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT 'N' COMMENT '登录状态（Y成功 N失败）',
  `message` varchar(255) DEFAULT '' COMMENT '提示消息',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统访问记录';

CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL DEFAULT 'Y' COMMENT '角色状态（Y正常N停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`),
  UNIQUE KEY `role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`status`,`remark`) values (1613012639384887298,'运营','admin',1,'Y','运营');

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色和菜单关联表';

CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT '0' COMMENT '角色id',
  `username` varchar(30) NOT NULL COMMENT '用户账号',
  `nickname` varchar(30) NOT NULL COMMENT '用户昵称',
  `salt` varchar(4) DEFAULT '' COMMENT '盐',
  `password` varchar(32) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT 'Y' COMMENT '帐号状态（Y正常N停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

insert  into `sys_user`(`user_id`,`role_id`,`username`,`nickname`,`salt`,`password`,`status`,`remark`) values (1613012757525848066,1613012639384887298,'admin','admin','u32t','1f81a659afbc2f6275154cd50e70a136','Y',NULL);

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '主ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父ID',
  `menu_name` varchar(50) NOT NULL COMMENT '名称',
  `menu_type` char(1) DEFAULT '' COMMENT '类型（M目录 C菜单 F按钮）',
  `icon` varchar(200) DEFAULT '#' COMMENT '图标',
  `path` varchar(200) DEFAULT '' COMMENT '路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `perms` varchar(200) DEFAULT NULL COMMENT '权限',
  `sort` int(4) DEFAULT '0' COMMENT '顺序',
  `frame_flag` char(1) DEFAULT 'N' COMMENT '外链',
  `frame_url` varchar(500) DEFAULT '' COMMENT '地址',
  `status` char(1) DEFAULT 'Y' COMMENT '菜单状态（Y正常N停用）',
  `visible` char(1) DEFAULT 'N' COMMENT '显示',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限表';

CREATE TABLE `sys_dict` (
  `dict_id` bigint(20) NOT NULL COMMENT '主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `dict_code` varchar(100) DEFAULT '' COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`,`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典数据';

CREATE TABLE `sys_column` (
  `column_id` bigint(20) NOT NULL COMMENT '表格ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `table_id` int(9) DEFAULT NULL COMMENT '字段ID',
  `content` longtext COMMENT '字段内容',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表格';