package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.exception.ApiException;
import com.macro.mall.tiny.modules.ums.dto.InitMenuDTO;
import com.macro.mall.tiny.modules.ums.dto.MenuMataDTO;
import com.macro.mall.tiny.modules.ums.mapper.UmsMenuMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.model.UmsRoleMenuRelation;
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsMenuService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleMenuRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UmsMenuServiceImpl extends ServiceImpl<UmsMenuMapper,UmsMenu>implements UmsMenuService {

    @Autowired
    private UmsRoleMenuRelationService roleMenuService;

    @Autowired
    private UmsAdminRoleRelationService adminRoleService;

    @Autowired
    private UmsRoleService roleService;

    @Override
    public List<InitMenuDTO> getMenuListByUserId(Long userId) {
        ArrayList<InitMenuDTO> dataList = new ArrayList<>();
        List<InitMenuDTO> menus = new ArrayList<>();
        // 获取用户对应的角色id
        List<Long> roleIds = adminRoleService.list(new QueryWrapper<UmsAdminRoleRelation>().eq("admin_id", userId).select("role_id"))
                .stream().map(UmsAdminRoleRelation::getRoleId).collect(Collectors.toList());
        // 根据角色id查询对应的菜单id
        List<Long> menuIds = roleMenuService.list(new QueryWrapper<UmsRoleMenuRelation>().in("role_id", roleIds).select("menu_id"))
                .stream().map(UmsRoleMenuRelation::getMenuId).collect(Collectors.toList());
        // 根据菜单id获取对应的菜单父id (需要对数据进行空值判断【Objects::nonNull】)
        List<Long> pIds = list(new QueryWrapper<UmsMenu>().in("id", menuIds).select("pid"))
                .stream().filter(Objects::nonNull).map(UmsMenu::getPid).collect(Collectors.toList());
        // 将菜单父id添加到menuIds中，以便查出父级菜单信息
        menuIds.addAll(pIds);
        // 根据菜单id查询出菜单
        list(new QueryWrapper<UmsMenu>().in("id", menuIds)
                .lambda().orderBy(true, true, UmsMenu::getSort))
                .stream().forEach(menuItem -> {
                    // 根据角色表查询对应的角色key
                    List<String> roleKey = roleService.listByIds(roleIds).stream().map(UmsRole::getKey).collect(Collectors.toList());
                    InitMenuDTO initMenuDTO = new InitMenuDTO();
                    MenuMataDTO menuMataDTO = new MenuMataDTO();
                    // 设置meta
                    menuMataDTO.setTitle(menuItem.getTitle());
                    menuMataDTO.setIsLink(menuItem.getIsLink());
                    menuMataDTO.setIsHide(menuItem.getIsHide());
                    menuMataDTO.setIsKeepAlive(menuItem.getIsKeepAlive());
                    menuMataDTO.setIsAffix(menuItem.getIsAffix());
                    menuMataDTO.setIsIframe(menuItem.getIsIframe());
                    menuMataDTO.setRoles(roleKey);
                    menuMataDTO.setIcon(menuItem.getIcon());

                    initMenuDTO.setId(menuItem.getId());
                    initMenuDTO.setPid(menuItem.getPid());
                    initMenuDTO.setPath(menuItem.getPath());
                    initMenuDTO.setName(menuItem.getName());
                    initMenuDTO.setComponent(menuItem.getComponent());
                    initMenuDTO.setMeta(menuMataDTO);
                    menus.add(initMenuDTO);
                });
        // 找到父节点
        for (InitMenuDTO menu : menus) {
            if (ObjectUtil.isEmpty(menu.getPid())) {
                menu.setChildren(new ArrayList<InitMenuDTO>());
                dataList.add(menu);
            }
        }
        // 根据父节点找到子节点
        for (InitMenuDTO menu : dataList) {
            menu.getChildren().add(findInitMenuChildren(menu, menus));
        }
        return dataList;
    }

    /**
     * 获取全部菜单
     * @return
     */
    @Override
    public List<UmsMenu> getMenuList() {
        QueryWrapper<UmsMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderBy(true, true, UmsMenu::getSort);
        // 获取所有菜单
        List<UmsMenu> menuLst = list(wrapper);
        // 给菜单设置关联的角色
        menuLst.stream().forEach(menu -> {
            // 根据菜单id获取角色id
            List<Long> roleIds = roleMenuService.list(new QueryWrapper<UmsRoleMenuRelation>().eq("menu_id", menu.getId()).select("role_id"))
                    .stream().map(UmsRoleMenuRelation::getRoleId).collect(Collectors.toList());
            if (!ObjectUtil.isEmpty(roleIds)) {
                // 根据角色表查询对应的角色key和角色id
                List<String> roleKey = roleService.listByIds(roleIds).stream().map(UmsRole::getKey).collect(Collectors.toList());
                menu.setRoles(roleKey);
                menu.setRoleIds(roleIds);
            }
        });
        return buildMenuTree(menuLst);
    }

    /**
     * 查看菜单
     * @param id 菜单id
     * @return
     */
    @Override
    public UmsMenu view(Long id) {
        UmsMenu menu = getById(id);
        // 获取菜单对应的角色id
        List<Long> roleIds = roleMenuService.list(new QueryWrapper<UmsRoleMenuRelation>().eq("menu_id", id).select("role_id"))
                .stream().map(UmsRoleMenuRelation::getRoleId).collect(Collectors.toList());
        menu.setRoleIds(roleIds);
        return menu;
    }

    /**
     * 删除菜单
     * @param id 菜单id
     * @return
     */
    @Override
    public Boolean delete(Long id) {
        // 如果存在子菜单，则不能删除
        QueryWrapper<UmsMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsMenu::getPid, id);
        UmsMenu menu = getOne(wrapper);
        if (ObjectUtil.isNotNull(menu)) {
            throw new ApiException("当前菜单存在子菜单，不能删除");
        }
        // 清空菜单对应的所有角色
        QueryWrapper<UmsRoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsRoleMenuRelation::getMenuId, id);
        roleMenuService.remove(queryWrapper);
        return removeById(id);
    }

    /**
     * 构建菜单树结构
     * @param menus 菜单列表
     * @return
     */
    private List<UmsMenu> buildMenuTree(List<UmsMenu> menus) {
        ArrayList<UmsMenu> dataList = new ArrayList<>();
        // 找到父节点
        for (UmsMenu menu : menus) {
            if (ObjectUtil.isEmpty(menu.getPid())) {
                menu.setChildren(new ArrayList<UmsMenu>());
                dataList.add(menu);
            }
        }
        // 根据父节点找到子节点
        for (UmsMenu menu : dataList) {
            menu.getChildren().add(findMenuChildren(menu, menus));
        }
        return dataList;
    }

    /**
     * 递归菜单
     * @param menu
     * @param menuLst
     * @return
     */
    private UmsMenu findMenuChildren(UmsMenu menu, List<UmsMenu> menuLst) {
        menu.setChildren(new ArrayList<>());
        for (UmsMenu item : menuLst) {
            if (menu.getId().equals(item.getPid())) {
                menu.getChildren().add(findMenuChildren(item, menuLst));
            }
        }
        return menu;
    }

    /**
     * 递归菜单
     * @param menu
     * @param menus
     * @return
     */
    private InitMenuDTO findInitMenuChildren(InitMenuDTO menu, List<InitMenuDTO> menus) {
        menu.setChildren(new ArrayList<>());
        for (InitMenuDTO item : menus) {
            if (menu.getId().equals(item.getPid())) {
                menu.getChildren().add(findInitMenuChildren(item, menus));
            }
        }
        return menu;
    }
}
