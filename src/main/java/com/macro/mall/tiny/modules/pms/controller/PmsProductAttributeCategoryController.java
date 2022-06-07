package com.macro.mall.tiny.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttributeCategory;
import com.macro.mall.tiny.modules.pms.model.dto.ProductAttributeDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductAttributeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 产品属性分类表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@RestController
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {

    @Autowired
    PmsProductAttributeCategoryService attributeCategoryService;

    /**
     * 获取商品属性分类列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        Page page = attributeCategoryService.list(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    /**
     * 添加商品分类
     * @param productAttributeCategory
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(PmsProductAttributeCategory productAttributeCategory) {
        boolean result = attributeCategoryService.create(productAttributeCategory);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 修改商品分类
     * @param productAttributeCategory
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(PmsProductAttributeCategory productAttributeCategory) {
        boolean result = attributeCategoryService.updateById(productAttributeCategory);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 删除商品分类
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public CommonResult delete(@PathVariable Long id) {
        boolean result = attributeCategoryService.removeById(id);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 筛选属性下拉级联数据
     * @return
     */
    @RequestMapping(value = "/list/withAttr", method = RequestMethod.GET)
    public CommonResult getListWithAttr() {
        List<ProductAttributeDTO> list = attributeCategoryService.getListWithAttr();
        return CommonResult.success(list);
    }

}

