package com.macro.mall.tiny.modules.ums.dto;

import com.macro.mall.tiny.common.vo.PageParamsDTO;
import io.swagger.annotations.ApiModelProperty;

public class PageRoleDTO extends PageParamsDTO {

    @ApiModelProperty("角色名称")
    private String name;
}
