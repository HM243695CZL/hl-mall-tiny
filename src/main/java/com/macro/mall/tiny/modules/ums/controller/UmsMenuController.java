package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.log.LogAnnotation;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;
import com.macro.mall.tiny.modules.ums.service.UmsMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags = "菜单管理")
@Tag(name = "菜单管理", description = "菜单管理")
@RequestMapping("/admin/menu")
public class UmsMenuController {

    @Autowired
    private UmsMenuService menuService;

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @LogAnnotation()
    @ApiOperation("新增菜单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@Valid @RequestBody UmsMenu menu) {
        return CommonResult.success(menuService.save(menu));
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @LogAnnotation()
    @ApiOperation("更新菜单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@Valid @RequestBody UmsMenu menu) {
        return CommonResult.success(menuService.updateById(menu));
    }


    /**
     * 获取全部菜单
     * @return
     */
    @LogAnnotation()
    @ApiOperation("获取全部菜单")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list() {
        return CommonResult.success(menuService.getMenuList());
    }

    /**
     * 查看
     * @param id
     * @return
     */
    @LogAnnotation()
    @ApiOperation("查看菜单")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public CommonResult view(@PathVariable Long id) {
        return CommonResult.success(menuService.view(id));
    }

    @LogAnnotation()
    @ApiOperation("根据ID删除后台菜单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public CommonResult delete(@PathVariable Long id) {
        return CommonResult.success(menuService.delete(id));
    }
}
