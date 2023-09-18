package com.macro.mall.tiny.modules.ums.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthMenuDTO {

    // 菜单id数组
    private List<Long> menuIds;

    // 角色id
    private Long id;
}
