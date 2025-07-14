package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;


import com.platform.modules.friend.domain.*;


@Data
@Accessors(chain = true) // 链式调用
public class MomentVo01 {

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
    private String createTime;

    /** 图片列表，改为可选类型 */
    private List<MediasVo01> images;

    /** 评论内容，改为可选类型 */
    private List<CommentsVo01> comments;

    /** 点赞列表，改为可选类型 */
    private List<String> likes;
}
