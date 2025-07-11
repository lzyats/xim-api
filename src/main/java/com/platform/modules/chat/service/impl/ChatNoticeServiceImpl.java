package com.platform.modules.chat.service.impl;

import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatNoticeDao;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.chat.service.ChatNoticeService;
import com.platform.modules.common.vo.CommonVo04;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 通知公告 服务层实现
 * </p>
 */
@Service("chatNoticeService")
public class ChatNoticeServiceImpl extends BaseServiceImpl<ChatNotice> implements ChatNoticeService {

    @Resource
    private ChatNoticeDao chatNoticeDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatNoticeDao);
    }

    @Override
    public List<ChatNotice> queryList(ChatNotice t) {
        List<ChatNotice> dataList = chatNoticeDao.queryList(t);
        return dataList;
    }

    @Override
    public PageInfo queryDataList() {
        ChatNotice query = new ChatNotice().setStatus(YesOrNoEnum.YES);
        List<ChatNotice> dataList = queryList(query);
        // list转Obj
        List<CommonVo04> resultList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new CommonVo04(y));
        }, ArrayList::addAll);
        return getPageInfo(resultList, dataList);
    }

}
