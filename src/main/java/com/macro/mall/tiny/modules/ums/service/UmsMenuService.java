package com.macro.mall.tiny.modules.ums.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.InitMenuDTO;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;

import java.util.List;

public interface UmsMenuService extends IService<UmsMenu> {
    /**
     * 根据用户id获取用户菜单
     * @param id 用户id
     * @return
     */
    List<InitMenuDTO> getMenuListByUserId(Long id);

    /**
     * 获取全部菜单
     * @return
     */
    List<UmsMenu> getMenuList();

    /**
     * 查看菜单
     * @param id 菜单id
     * @return
     */
    UmsMenu view(Long id);

    /**
     * 根据ID删除后台菜单
     * @param id 菜单id
     * @return
     */
    Boolean delete(Long id);
}
