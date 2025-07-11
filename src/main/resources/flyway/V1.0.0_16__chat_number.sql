CREATE TABLE `chat_number` (
  `chat_no` varchar(8) NOT NULL COMMENT '编号',
  `status` char(1) DEFAULT 'N' COMMENT '状态',
  PRIMARY KEY (`chat_no`),
  UNIQUE KEY `chat_no` (`chat_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统号码';
