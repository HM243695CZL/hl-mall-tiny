package com.macro.mall.tiny.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.log.LogAnnotation;
import com.macro.mall.tiny.modules.ums.dto.*;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import com.macro.mall.tiny.modules.ums.service.UmsMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户管理
 */
@RestController
@Api(tags = "用户管理")
@Tag(name = "用户管理",description = "后台用户管理")
@RequestMapping("/admin/admin")
public class UmsAdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private UmsMenuService menuService;

    /**
     * 分页
     * @param paramsDTO
     * @return
     */
    @LogAnnotation()
    @ApiOperation("分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public CommonResult page(@RequestBody AdminPageDTO paramsDTO) {
        Page<UmsAdmin> adminList = adminService.pageList(paramsDTO);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    /**
     * 新增用户
     * @param umsAdmin
     * @return
     */
    @LogAnnotation()
    @ApiOperation("新增用户")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@Valid @RequestBody UmsAdmin umsAdmin) {
        return CommonResult.success(adminService.create(umsAdmin));
    }

    /**
     * 查看用户
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("查看用户")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public CommonResult view(@PathVariable Long id) {
        return CommonResult.success(adminService.getById(id));
    }

    /**
     * 更新用户
     * @param umsAdmin
     * @return
     */
    @LogAnnotation()
    @ApiOperation("更新用户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@Valid @RequestBody UmsAdmin umsAdmin) {
        return CommonResult.success(adminService.updateAdmin(umsAdmin));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public CommonResult delete(@PathVariable Long id) {
        return CommonResult.success(adminService.delete(id));
    }

    /**
     * 修改密码
     * @param updatePassDTO
     * @return
     */
    @LogAnnotation()
    @ApiOperation("修改密码")
    @RequestMapping(value = "/updatePass", method = RequestMethod.POST)
    public CommonResult updatePass(@RequestBody UpdatePassDTO updatePassDTO) {
        return CommonResult.success(adminService.updatePass(updatePassDTO));
    }

    /**
     * 获取用户权限
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("获取用户权限")
    @RequestMapping(value = "/getUserAuth/{id}", method = RequestMethod.GET)
    public CommonResult getUserAuth(@PathVariable Long id) {
        return CommonResult.success(adminService.getUserAuth(id));
    }

    /**
     * 分配权限
     * @param authRoleDTO
     * @return
     */
    @LogAnnotation()
    @ApiOperation("分配权限")
    @RequestMapping(value = "/authRole", method = RequestMethod.POST)
    public CommonResult authRole(@RequestBody AuthRoleDTO authRoleDTO) {
        return CommonResult.success(adminService.authRole(authRoleDTO));
    }

    @LogAnnotation()
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        UmsAdmin admin = adminService.getAdminByUsername(umsAdminLoginParam.getUsername());
        List<InitMenuDTO> menuList = menuService.getMenuListByUserId(admin.getId());
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("menuList", menuList);
        tokenMap.put("userInfo", admin);
        return CommonResult.success(tokenMap);
    }
}
