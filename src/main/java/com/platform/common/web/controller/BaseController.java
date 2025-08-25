package com.platform.common.web.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.core.EnumUtils;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.JsonDateDeserializer;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.page.TableSupport;
import com.platform.modules.chat.enums.BannedTypeEnum;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 */
@Slf4j
public class BaseController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(JsonDateDeserializer.parseDate(text));
            }
        });
        // YesOrNoEnum 类型转换
        binder.registerCustomEditor(YesOrNoEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(YesOrNoEnum.class, text));
            }
        });
        // GenderTypeEnum 类型转换
        binder.registerCustomEditor(GenderEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(GenderEnum.class, text));
            }
        });
        // PushMsgTypeEnum 类型转换
        binder.registerCustomEditor(PushMsgTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(PushMsgTypeEnum.class, text));
            }
        });
        // ChatTalkEnum 类型转换
        binder.registerCustomEditor(ChatTalkEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(ChatTalkEnum.class, text));
            }
        });
        // BannedTypeEnum 类型转换
        binder.registerCustomEditor(BannedTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(BannedTypeEnum.class, text));
            }
        });
        // TradePayEnum 类型转换
        binder.registerCustomEditor(TradePayEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(TradePayEnum.class, text));
            }
        });
        // TradeTypeEnum 类型转换
        binder.registerCustomEditor(TradeTypeEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(EnumUtils.toEnum(TradeTypeEnum.class, text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.getPageDomain();
        startPage(PageDomain.escapeOrderBySql(pageDomain.getOrderBy()));
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(String orderBy) {
        PageDomain pageDomain = TableSupport.getPageDomain();
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), StrUtil.toUnderlineCase(orderBy));
    }

    /**
     * 设置排序分页数据
     */
    protected void orderBy(String orderBy) {
        PageHelper.orderBy(StrUtil.toUnderlineCase(orderBy));
    }

    /**
     * 响应请求分页数据
     */
    protected TableDataInfo getDataTable(PageInfo<?> list) {
        return new TableDataInfo(list.getList(), list.getTotal());
    }

    /**
     * 响应请求分页数据加密
     */
    protected TableDataInfo getDataTable(PageInfo<?> list,String key) {
        return new TableDataInfo(list.getList(), list.getTotal(),key);
    }

    /**
     * 响应请求分页数据
     */
    protected TableDataInfo getDataTable(List<?> list) {
        return new TableDataInfo(list, new PageInfo(list).getTotal());
    }

    /**
     * 响应请求分页数据加密
     */
    protected TableDataInfo getDataTable(List<?> list,String key) {
        return new TableDataInfo(list, new PageInfo(list).getTotal(),key);
    }

}
