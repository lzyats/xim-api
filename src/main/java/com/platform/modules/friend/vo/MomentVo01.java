package com.platform.modules.friend.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 图片列表，改为可选类型 */
    private List<FriendMedias> images;

    /** 评论内容，改为可选类型 */
    private List<FriendComments> comments;

    /** 点赞列表，改为可选类型 */
    private List<String> likes;
}
