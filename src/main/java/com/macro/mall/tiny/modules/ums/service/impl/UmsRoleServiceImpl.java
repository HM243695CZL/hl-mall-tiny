package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.dto.AuthMenuDTO;
import com.macro.mall.tiny.modules.ums.dto.PageRoleDTO;
import com.macro.mall.tiny.modules.ums.mapper.UmsMenuMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.model.UmsRoleMenuRelation;
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleMenuRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper,UmsRole>implements UmsRoleService {
    @Autowired
    private UmsMenuMapper menuMapper;
    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private UmsRoleMenuRelationService roleMenuRelationService;
    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return menuMapper.getMenuList(adminId);
    }

    /**
     * 分页查询
     * @param paramsDTO
     * @return
     */
    @Override
    public Page<UmsRole> pageList(PageRoleDTO paramsDTO) {
        Page<UmsRole> page = new Page<>(paramsDTO.getPageIndex(), paramsDTO.getPageSize());
        QueryWrapper<UmsRole> queryWrapper = new QueryWrapper<>();
        return page(page, queryWrapper);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Boolean delete(Long id) {
        // 删除用户角色表的角色
        QueryWrapper<UmsAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdminRoleRelation::getRoleId, id);
        adminRoleRelationService.remove(wrapper);
        // 删除角色菜单表对应的角色
        QueryWrapper<UmsRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsRoleMenuRelation::getRoleId, id);
        roleMenuRelationService.remove(queryWrapper);
        return removeById(id);
    }

    /**
     * 获取角色已分配的权限
     * @param id
     * @return
     */
    @Override
    public List<Long> viewAuth(Long id) {
        QueryWrapper<UmsRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsRoleMenuRelation::getRoleId, id);
        List<Long> menuIds = roleMenuRelationService.list(queryWrapper.select("menu_id"))
                .stream().map(UmsRoleMenuRelation::getMenuId).collect(Collectors.toList());
        return menuIds;
    }

    /**
     * 授权
     * @param authMenuDTO
     * @return
     */
    @Transactional
    @Override
    public Boolean authMenu(AuthMenuDTO authMenuDTO) {
        // 先清空之前的授权
        QueryWrapper<UmsRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsRoleMenuRelation::getRoleId, authMenuDTO.getId());
        roleMenuRelationService.remove(queryWrapper);

        List<UmsRoleMenuRelation> roleMenuRelationList = setRoleAndMenuRelation(authMenuDTO.getMenuIds(), authMenuDTO.getId());
        return roleMenuRelationService.saveBatch(roleMenuRelationList);
    }

    /**
     * 设置角色和菜单的关联关系
     * @param menuIds
     * @param id
     * @return
     */
    private List<UmsRoleMenuRelation> setRoleAndMenuRelation(List<Long> menuIds, Long id) {
        List<UmsRoleMenuRelation> roleMenuRelationList = new ArrayList<>();
        for (Long menuId : menuIds) {
            UmsRoleMenuRelation roleMenuRelation = new UmsRoleMenuRelation();
            roleMenuRelation.setRoleId(id);
            roleMenuRelation.setMenuId(menuId);
            roleMenuRelationList.add(roleMenuRelation);
        }
        return roleMenuRelationList;
    }


}
