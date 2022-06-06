package com.macro.mall.tiny.modules.pms.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@RestController
@RequestMapping("/product")
public class PmsProductController {

    @Autowired
    PmsProductService productService;

    @ApiOperation("商品列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public CommonResult<List<PmsProduct>> list() {
        List<PmsProduct> list = productService.list();
        return CommonResult.success(list);
    }

}

