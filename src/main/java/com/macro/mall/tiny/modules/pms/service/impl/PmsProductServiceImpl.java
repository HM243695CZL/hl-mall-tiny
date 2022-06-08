package com.macro.mall.tiny.modules.pms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductMapper;
import com.macro.mall.tiny.modules.pms.model.dto.ProductConditionDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    @Override
    public Page list(ProductConditionDTO conditionDTO) {
        Page page = new Page(conditionDTO.getPageNum(), conditionDTO.getPageSize());
        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmsProduct> lambda = queryWrapper.lambda();
        // 商品名称
        if (!StrUtil.isBlank(conditionDTO.getKeyword())) {
            lambda.like(PmsProduct::getName, conditionDTO.getKeyword());
        }
        // 商品货号
        if (!StrUtil.isBlank(conditionDTO.getProductSn())) {
            lambda.like(PmsProduct::getProductSn, conditionDTO.getProductSn());
        }
        // 商品分类
        if (conditionDTO.getProductCategoryId() != null && conditionDTO.getProductCategoryId() > 0) {
            lambda.eq(PmsProduct::getProductCategoryId, conditionDTO.getProductCategoryId());
        }
        // 商品品牌
        if (conditionDTO.getBrandId() != null && conditionDTO.getBrandId() > 0) {
            lambda.eq(PmsProduct::getBrandId, conditionDTO.getBrandId());
        }
        // 上架状态
        if (conditionDTO.getPublishStatus() != null) {
            lambda.eq(PmsProduct::getPublishStatus, conditionDTO.getPublishStatus());
        }
        // 审核状态
        if (conditionDTO.getVerifyStatus() != null) {
            lambda.eq(PmsProduct::getVerifyStatus, conditionDTO.getVerifyStatus());
        }

        return page(page, lambda);
    }

    /**
     * 更新单个字段的公共方法
     * @param ids
     * @param getPublishStatus
     * @return
     */
    @Override
    public boolean updateStatus(List<Long> ids, Integer status, SFunction<PmsProduct, ?> getPublishStatus) {
        UpdateWrapper<PmsProduct> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(getPublishStatus, status).in(PmsProduct::getId, ids);
        return update(updateWrapper);
    }
}
