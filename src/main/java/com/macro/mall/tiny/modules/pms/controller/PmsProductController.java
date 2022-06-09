package com.macro.mall.tiny.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.model.dto.ProductConditionDTO;
import com.macro.mall.tiny.modules.pms.model.dto.ProductSaveParamsDTO;
import com.macro.mall.tiny.modules.pms.model.dto.ProductUpdateInitDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list(ProductConditionDTO conditionDTO) {
        Page page = productService.list(conditionDTO);
        return CommonResult.success(CommonPage.restPage(page));
    }

    /**
     * 逻辑删除商品
     * @param ids
     * @return
     */
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        boolean result = productService.removeByIds(ids);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 更新是否新品的状态
     * @param ids
     * @return
     */
    @RequestMapping(value = "/update/newStatus", method = RequestMethod.POST)
    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("newStatus") Integer newStatus) {
        boolean result = productService.updateStatus(ids, newStatus, PmsProduct::getNewStatus);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 更新是否推荐
     * @param ids
     * @param recommendStatus
     * @return
     */
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("recommendStatus") Integer recommendStatus) {
        boolean result = productService.updateStatus(ids, recommendStatus, PmsProduct::getRecommandStatus);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 更新是否上架
     * @param ids
     * @param publishStatus
     * @return
     */
    @RequestMapping(value = "/update/publishStatus", method = RequestMethod.POST)
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("publishStatus") Integer publishStatus) {
        boolean result = productService.updateStatus(ids, publishStatus, PmsProduct::getPublishStatus);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 添加商品
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody ProductSaveParamsDTO productSaveParamsDTO) {
        boolean result = productService.create(productSaveParamsDTO);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }

    /**
     * 查看商品
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
    public CommonResult getUpdateInfo (@PathVariable Long id) {
        ProductUpdateInitDTO updateInitDTO = productService.getUpdateInfo(id);
        return CommonResult.success(updateInitDTO);
    }

    /**
     * 更新商品
     * @param productSaveParamsDTO
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@RequestBody @Valid ProductSaveParamsDTO productSaveParamsDTO) {
        boolean result = productService.update(productSaveParamsDTO);
        return result ? CommonResult.success(result) : CommonResult.failed();
    }
}

