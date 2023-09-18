package com.macro.mall.tiny.modules.ums.dto;

import com.macro.mall.tiny.common.vo.PageParamsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminPageDTO extends PageParamsDTO {

    @ApiModelProperty(value = "用户名")
    private String username;
}
