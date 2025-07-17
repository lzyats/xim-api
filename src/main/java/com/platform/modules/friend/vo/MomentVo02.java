package com.platform.modules.friend.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 确保 null 值字段不被序列化
@Accessors(chain = true) // 链式调用
public class MomentVo02 {

    /** 用户ID，改为可选类型 */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /** 动态正文，改为可选类型 */
    @NotBlank(message = "朋友圈内容不能为空")
    @Size(max = 500, message = "朋友圈内容长度不能大于500")
    private String content;


    /** 位置信息，字符类型，可为空 */
    private String location;

    /** 发布时间，改为可选类型 */
    private String createTime;

    private Integer visibility;

    /** 图片列表，改为可选类型 */
    private List<MediasVo02> images;

}
