package com.platform.modules.friend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



/**
 * <p>
 * 朋友圈媒体资源表实体类
 * </p>
 */
@Data
@TableName("friend_medias")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true) // 链式调用
public class FriendMedias extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 媒体资源ID
     */
    @TableId
    private Long mediaId;
    /**
     * 关联动态ID
     */
    @NotNull(message = "momentId不能为空")
    private Long momentId;
    /**
     * 事件ID
     */
    private Long momid;
    /**
     * 资源URL
     */
    @NotBlank(message = "图像不能为空")
    @Size(max = 500, message = "图像URL不能超过500个字符")
    private String url;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 类型：0-图片，1-视频，2-音频
     */
    private Integer type;
    /**
     * 排序顺序
     */
    private Integer sortOrder;
    /**
     * 宽度（图片/视频）
     */
    private Integer width;
    /**
     * 高度（图片/视频）
     */
    private Integer height;
    /**
     * 时长（视频/音频，单位：秒）
     */
    private Integer duration;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public FriendMedias(Long momentId) {
        this.momentId = momentId;
    }

    /**
     * 字段
     */
    public static final String LABEL_MEDIA_ID = "mediaId";
    public static final String LABEL_MOMENT_ID = "momentId";
    public static final String LABEL_MOMID = "momid";
    public static final String LABEL_URL = "url";
    public static final String LABEL_THUMBNAIL = "thumbnail";
    public static final String LABEL_TYPE = "type";
    public static final String LABEL_SORT_ORDER = "sortOrder";
    public static final String LABEL_WIDTH = "width";
    public static final String LABEL_HEIGHT = "height";
    public static final String LABEL_DURATION = "duration";
    public static final String LABEL_CREATE_TIME = "createTime";

}
