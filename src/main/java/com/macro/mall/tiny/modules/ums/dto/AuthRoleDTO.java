package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AuthRoleDTO {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "角色id数组")
    private List<Long> roles;
}
