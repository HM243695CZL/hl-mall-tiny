package com.macro.mall.tiny.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 产品分类 前端控制器
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@RestController
@RequestMapping("/productCategory")
public class PmsProductCategoryController {

    @Autowired
    PmsProductCategoryService productCategoryService;

    /**
     * 获取分类列表
     * @param parentId 父级id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductCategory>> getList(@PathVariable Long parentId,
                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page page = productCategoryService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    /**
     * 更新导航栏状态显示隐藏状态
     * @param ids 更新的id数组
     * @param navStatus  状态
     * @return
     */
    @RequestMapping(value = "/update/navStatus", method = RequestMethod.POST)
    public CommonResult updateNavStatus(@RequestParam(value = "ids") List<Long> ids,
                                        @RequestParam(value = "navStatus") Integer navStatus) {
        boolean result = productCategoryService.updateNavStatus(ids, navStatus);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }


    /**
     * 删除商品分类
     * @param id 需要删除的分类id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        boolean result = productCategoryService.removeById(id);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 新增商品分类
     * @param productCategory
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create (@RequestBody PmsProductCategory productCategory) {
        boolean result = productCategoryService.save(productCategory);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 查看商品分类详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult getById(@PathVariable Long id) {
        return CommonResult.success(productCategoryService.getById(id));
    }

    /**
     * 更新商品分类
     * @param productCategory
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@RequestBody PmsProductCategory productCategory) {
        boolean result = productCategoryService.updateById(productCategory);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }
}

