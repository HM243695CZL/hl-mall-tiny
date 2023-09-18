package com.macro.mall.tiny.modules.ums.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.exception.ApiException;
import com.macro.mall.tiny.common.exception.Asserts;
import com.macro.mall.tiny.constants.Constants;
import com.macro.mall.tiny.domain.AdminUserDetails;
import com.macro.mall.tiny.modules.ums.dto.AdminPageDTO;
import com.macro.mall.tiny.modules.ums.dto.AuthRoleDTO;
import com.macro.mall.tiny.modules.ums.dto.UpdatePassDTO;
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminMapper;
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleMenuRelationService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import com.macro.mall.tiny.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper,UmsAdmin> implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private UmsRoleMapper roleMapper;
    @Autowired
    private UmsRoleService roleService;
    @Autowired
    private UmsRoleMenuRelationService roleMenuRelationService;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdmin admin = null;
        QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsAdmin::getUsername,username);
        List<UmsAdmin> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            setUserRoles(admin);
            return admin;
        }
        throw new ApiException("用户名不存在");
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }


    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            return new AdminUserDetails(admin);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    /**
     * 分页查询
     * @param paramsDTO
     * @return
     */
    @Override
    public Page<UmsAdmin> pageList(AdminPageDTO paramsDTO) {
        Page<UmsAdmin> page = new Page<>(paramsDTO.getPageIndex(), paramsDTO.getPageSize());
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(paramsDTO.getUsername())) {
            queryWrapper.lambda().like(UmsAdmin::getUsername, paramsDTO.getUsername());
        }
        Page<UmsAdmin> pageList = page(page, queryWrapper);
        // 根据用户id获取角色id
        page.getRecords().stream().forEach(this::setUserRoles);
        return pageList;
    }

    /**
     * 设置用户的roles字段
     * @param umsAdmin
     */
    private void setUserRoles(UmsAdmin umsAdmin) {
        List<Long> roleIds = adminRoleRelationService.list(new QueryWrapper<UmsAdminRoleRelation>().eq("admin_id", umsAdmin.getId())
                .select("role_id")).stream().map(UmsAdminRoleRelation::getRoleId).collect(Collectors.toList());
        if (!ObjectUtil.isEmpty(roleIds)) {
            // 根据角色id获取角色名称
            List<String> roleName = roleService.listByIds(roleIds).stream().map(UmsRole::getKey).collect(Collectors.toList());
            umsAdmin.setRoles(roleName);
        }

    }

    /**
     * 新增用户
     * @param umsAdmin
     * @return
     */
    @Override
    public Boolean create(UmsAdmin umsAdmin) {
        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(Constants.INIT_PASSWORD);
        umsAdmin.setPassword(encodePassword);
        return save(umsAdmin);
    }

    /**
     * 更新用户
     * @param umsAdmin
     * @return
     */
    @Override
    public Boolean updateAdmin(UmsAdmin umsAdmin) {
        UmsAdmin admin = getById(umsAdmin.getId());
        umsAdmin.setPassword(admin.getPassword());
        return updateById(umsAdmin);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Boolean delete(Long id) {
        // 删除用户角色关系
        QueryWrapper<UmsAdminRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsAdminRoleRelation::getAdminId, id);
        adminRoleRelationService.remove(queryWrapper);
        return removeById(id);
    }

    /**
     * 修改密码
     * @param updatePassDTO
     * @return
     */
    @Override
    public Boolean updatePass(UpdatePassDTO updatePassDTO) {
        UmsAdmin admin = getById(updatePassDTO.getId());
        admin.setPassword(passwordEncoder.encode(updatePassDTO.getPassword()));
        return updateById(admin);
    }

    /**
     * 获取用户权限
     * @param id
     * @return
     */
    @Override
    public List<Long> getUserAuth(Long id) {
        // 查询用户所有的角色id
        List<Long> roleIds = adminRoleRelationService.list(new QueryWrapper<UmsAdminRoleRelation>()
                .eq("admin_id", id)).stream().map(UmsAdminRoleRelation::getRoleId).collect(Collectors.toList());
        return roleIds;
    }

    /**
     * 分配权限
     * @param authRoleDTO
     * @return
     */
    @Transactional
    @Override
    public Boolean authRole(AuthRoleDTO authRoleDTO) {
        // 先清空之前的授权
        QueryWrapper<UmsAdminRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsAdminRoleRelation::getAdminId, authRoleDTO.getId());
        adminRoleRelationService.remove(queryWrapper);
        
        // 再重新授权
        List<UmsAdminRoleRelation> adminRoleList = setAdminAndRole(authRoleDTO.getRoles(), authRoleDTO.getId());
        return adminRoleRelationService.saveBatch(adminRoleList);
    }

    /**
     * 设置用户和角色的关系
     * @param roles
     * @param id
     * @return
     */
    private List<UmsAdminRoleRelation> setAdminAndRole(List<Long> roles, Long id) {
        List<UmsAdminRoleRelation> adminRoleList = new ArrayList<>();
        for (Long roleId : roles) {
            UmsAdminRoleRelation adminRoleRelation = new UmsAdminRoleRelation();
            adminRoleRelation.setAdminId(id);
            adminRoleRelation.setRoleId(roleId);
            adminRoleList.add(adminRoleRelation);
        }
        return adminRoleList;
    }
}
