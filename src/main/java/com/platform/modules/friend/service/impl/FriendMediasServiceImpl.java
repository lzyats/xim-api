package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.modules.friend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendMediasService;
import com.platform.modules.friend.dao.FriendMediasDao;
import com.platform.modules.friend.domain.FriendMedias;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 朋友圈媒体资源表 服务层实现
 * </p>
 */
@Service("friendMediasService")
public class FriendMediasServiceImpl extends BaseServiceImpl<FriendMedias> implements FriendMediasService {

    @Resource
    private FriendMediasDao friendMediasDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendMediasDao);
    }

    @Override
    public List<FriendMedias> queryList(FriendMedias t) {
        List<FriendMedias> dataList = friendMediasDao.queryList(t);
        return dataList;
    }

    /**
     * 获取某条信息的所有评论
     * @param momentId
     * @return
     */
    @Override
    public PageInfo queryListall(Long momentId) {
        // 查询
        List<FriendMedias> dataList = this.queryList(new FriendMedias(momentId));
        // 转换
        List<Dict> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            Dict dict = Dict.create().parseBean(y)
                    .filter(FriendMedias.LABEL_MOMENT_ID
                            , FriendMedias.LABEL_MEDIA_ID
                            , FriendMedias.LABEL_URL
                            , FriendMedias.LABEL_THUMBNAIL
                            , FriendMedias.LABEL_TYPE
                            , FriendMedias.LABEL_MOMID
                            , FriendMedias.LABEL_SORT_ORDER
                            , FriendMedias.LABEL_WIDTH
                            , FriendMedias.LABEL_HEIGHT
                            , FriendMedias.LABEL_DURATION
                            , FriendMedias.LABEL_CREATE_TIME);
            x.add(dict);
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

}
