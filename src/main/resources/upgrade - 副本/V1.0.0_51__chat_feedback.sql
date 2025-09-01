ALTER TABLE `chat_feedback`
	ADD COLUMN `status` char(1)  COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '处理状态' after `version` ,
	CHANGE `create_time` `create_time` datetime   NULL COMMENT '创建时间' after `status` ;