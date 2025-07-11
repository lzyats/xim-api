package com.platform.modules.chat.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDateDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo12 {

    @NotNull(message = "生日不能为空")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date birthday;

}
