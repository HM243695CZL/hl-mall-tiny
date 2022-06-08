package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.dto.ProductConditionDTO;

import java.util.List;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
public interface PmsProductService extends IService<PmsProduct> {

    Page list(ProductConditionDTO conditionDTO);

    /**
     * 更新单个字段的公共方法
     * @param ids
     * @param publishStatus
     * @param getPublishStatus
     * @return
     */
    boolean updateStatus(List<Long> ids, Integer publishStatus, SFunction<PmsProduct, ?> getPublishStatus);
}
