package com.macro.mall.tiny.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductCategory>> getList(
            @PathVariable Long parentId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        Page page = productCategoryService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }
}

