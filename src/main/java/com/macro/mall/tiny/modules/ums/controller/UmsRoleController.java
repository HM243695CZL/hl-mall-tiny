package com.macro.mall.tiny.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.log.LogAnnotation;
import com.macro.mall.tiny.modules.ums.dto.AuthMenuDTO;
import com.macro.mall.tiny.modules.ums.dto.PageRoleDTO;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 后台用户角色管理
 */
@RestController
@Api(tags = "角色管理")
@Tag(name = "角色管理",description = "后台用户角色管理")
@RequestMapping("/admin/role")
public class UmsRoleController {
    @Autowired
    private UmsRoleService roleService;

    /**
     * 分页
     * @param paramsDTO
     * @return
     */
    @LogAnnotation()
    @ApiOperation("分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public CommonResult page(@RequestBody PageRoleDTO paramsDTO) {
        Page<UmsRole> roleList = roleService.pageList(paramsDTO);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    /**
     * 新增角色
     */
    @LogAnnotation()
    @ApiOperation("新增角色")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@Valid @RequestBody UmsRole umsRole) {
        return CommonResult.success(roleService.save(umsRole));
    }

    /**
     * 查看角色
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("查看角色")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public CommonResult view(@PathVariable Long id) {
        return CommonResult.success(roleService.getById(id));
    }

    /**
     * 更新角色
     * @param umsRole
     * @return
     */
    @LogAnnotation()
    @ApiOperation("更新角色")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody UmsRole umsRole) {
        return CommonResult.success(roleService.updateById(umsRole));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("删除角色")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public CommonResult delete(@PathVariable Long id) {
        return CommonResult.success(roleService.delete(id));
    }

    /**
     * 获取全部角色
     * @return
     */
    @LogAnnotation()
    @ApiOperation("获取全部角色")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list() {
        return CommonResult.success(roleService.list());
    }

    /**
     * 获取角色已分配的权限
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("获取已分配的权限")
    @RequestMapping(value = "/viewAuth/{id}", method = RequestMethod.GET)
    public CommonResult viewAuth(@PathVariable Long id) {
        List<Long> menuIds = roleService.viewAuth(id);
        return CommonResult.success(menuIds);
    }

    /**
     * 分配权限
     * @param authMenuDTO
     * @return
     */
    @LogAnnotation()
    @ApiOperation("分配权限")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public CommonResult authMenu(@RequestBody AuthMenuDTO authMenuDTO) {
        return CommonResult.success(roleService.authMenu(authMenuDTO));
    }

}
