package com.platform.modules.friend.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 确保 null 值字段不被序列化
@Accessors(chain = true) // 链式调用
public class MomentVo03 {

    /** 动态ID，改为可选类型 */
    private Long momentId;

    /** 用户ID，改为可选类型 */
    private Long userId;

    /** 用户头像，改为可选类型 */
    private String portrait;

    /** 用户昵称，改为可选类型 */
    private String nickname;

    /** 动态正文，改为可选类型 */
    private String content;

    /** 位置信息，字符类型，可为空 */
    private String location;

    /** 发布时间，改为可选类型 */
    private Date createTime;

    /** 可见人群 */
    private int visibility;

    private int isdelete;

    /** 可见人列表 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> visuser;

    /** 图片列表，改为可选类型 */
    private List<MediasVo01> images;

    /** 评论内容，改为可选类型 */
    private List<CommentsVo01> comments;

    /** 点赞列表，改为可选类型 */
    private List<String> likes;
}
