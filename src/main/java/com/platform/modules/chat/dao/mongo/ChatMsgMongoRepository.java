package com.platform.modules.chat.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.platform.modules.chat.domain.mongo.ChatMsgMongo;
import java.util.List;

/**
 * MongoDB 消息备份操作接口
 */
public interface ChatMsgMongoRepository extends MongoRepository<ChatMsgMongo, String> {

    // 根据消息ID查询备份记录
    ChatMsgMongo findByMsgId(Long msgId);


}