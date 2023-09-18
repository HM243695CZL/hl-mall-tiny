package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.AuthMenuDTO;
import com.macro.mall.tiny.modules.ums.dto.PageRoleDTO;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;
import com.macro.mall.tiny.modules.ums.model.UmsRole;

import java.util.List;

public interface UmsRoleService extends IService<UmsRole> {
    /**
     * 根据管理员ID获取对应菜单
     */
    List<UmsMenu> getMenuList(Long adminId);

    /**
     * 分页查询
     * @param paramsDTO
     * @return
     */
    Page<UmsRole> pageList(PageRoleDTO paramsDTO);

    Boolean delete(Long id);

    /**
     * 获取角色已分配的权限
     * @param id
     * @return
     */
    List<Long> viewAuth(Long id);

    /**
     * 授权
     * @param authMenuDTO
     * @return
     */
    Boolean authMenu(AuthMenuDTO authMenuDTO);
}
