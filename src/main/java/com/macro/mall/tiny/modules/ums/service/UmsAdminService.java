package com.macro.mall.tiny.modules.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.dto.AdminPageDTO;
import com.macro.mall.tiny.modules.ums.dto.AuthRoleDTO;
import com.macro.mall.tiny.modules.ums.dto.UpdatePassDTO;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UmsAdminService extends IService<UmsAdmin> {
    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);


    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username,String password);


    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);


    /**
     * 分页查询
     * @param paramsDTO
     * @return
     */
    Page<UmsAdmin> pageList(AdminPageDTO paramsDTO);

    /**
     * 新增用户
     * @param umsAdmin
     * @return
     */
    Boolean create(UmsAdmin umsAdmin);

    /**
     * 更新用户
     * @param umsAdmin
     * @return
     */
    Boolean updateAdmin(UmsAdmin umsAdmin);

    /**
     * 删除用户
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 修改密码
     * @param updatePassDTO
     * @return
     */
    Boolean updatePass(UpdatePassDTO updatePassDTO);

    /**
     * 获取用户权限
     * @param id
     * @return
     */
    List<Long> getUserAuth(Long id);

    /**
     * 分配权限
     * @param authRoleDTO
     * @return
     */
    Boolean authRole(AuthRoleDTO authRoleDTO);
}
