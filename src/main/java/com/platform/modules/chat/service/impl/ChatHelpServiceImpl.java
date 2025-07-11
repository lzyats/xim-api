package com.platform.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.dao.ChatHelpDao;
import com.platform.modules.chat.domain.ChatHelp;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 聊天帮助 服务层实现
 * </p>
 */
@Service("chatHelpService")
public class ChatHelpServiceImpl extends BaseServiceImpl<ChatHelp> implements ChatHelpService {

    @Resource
    private ChatHelpDao chatHelpDao;

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatHelpDao);
    }

    @Override
    public List<ChatHelp> queryList(ChatHelp t) {
        List<ChatHelp> dataList = chatHelpDao.queryList(t);
        return dataList;
    }

    @Override
    public List<LabelVo> queryDataList() {
        List<ChatHelp> dataList = queryList(new ChatHelp());
        // 查询项目名称
        String project = chatConfigService.queryConfig(ChatConfigEnum.SYS_PROJECT).getStr();
        // list转Obj
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            LabelVo data = new LabelVo()
                    .setLabel(y.getTitle())
                    .setValue(StrUtil.format(y.getContent(), project));
            x.add(data);
        }, ArrayList::addAll);
    }

}
