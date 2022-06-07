package com.macro.mall.tiny.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttribute;
import com.macro.mall.tiny.modules.pms.model.dto.RelationAttrInfoDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {

    @Autowired
    private PmsProductAttributeService productAttributeService;

    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductAttribute>> getList(@PathVariable Long cid,
                                                                 @RequestParam(value = "type") Integer type,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page page = productAttributeService.list(cid, type, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    /**
     * 新增商品属性
     * @param productAttribute
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody PmsProductAttribute productAttribute) {
        boolean result = productAttributeService.create(productAttribute);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 修改商品属性
     * @param productAttribute
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@RequestBody PmsProductAttribute productAttribute) {
        boolean result = productAttributeService.updateById(productAttribute);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 查看商品属性详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<PmsProductAttribute> getById(@PathVariable Long id) {
        return CommonResult.success(productAttributeService.getById(id));
    }

    /**
     * 删除商品属性
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        boolean result = productAttributeService.delete(ids);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 获取商品分类id关联的筛选属性
     * @param cId
     * @return
     */
    @RequestMapping(value = "/attrInfo/{cId}")
    public CommonResult getRelationAttrInfoByCid(@PathVariable Long cId) {
        List<RelationAttrInfoDTO> list = productAttributeService.getRelationAttrInfoByCid(cId);
        return CommonResult.success(list);
    }
}

