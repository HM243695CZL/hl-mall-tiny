package com.macro.mall.tiny.modules.pms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductMapper;
import com.macro.mall.tiny.modules.pms.model.dto.ProductConditionDTO;
import com.macro.mall.tiny.modules.pms.model.dto.ProductSaveParamsDTO;
import com.macro.mall.tiny.modules.pms.model.dto.ProductUpdateInitDTO;
import com.macro.mall.tiny.modules.pms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
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
@SuppressWarnings("all")
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    @Autowired
    PmsMemberPriceService memberPriceService;

    @Autowired
    PmsProductLadderService productLadderService;

    @Autowired
    PmsProductFullReductionService productFullReductionService;

    @Autowired
    PmsSkuStockService skuStockService;

    @Autowired
    PmsProductAttributeValueService productAttributeValueService;

    @Autowired
    PmsProductMapper productMapper;

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

    @Override
    @Transactional
    public boolean create(ProductSaveParamsDTO productSaveParamsDTO) {
        // 保存商品基本信息 -- 主表
        PmsProduct product = productSaveParamsDTO;
        product.setId(null);
        boolean result = save(product);
        if (result) {
            switch (product.getPromotionType()) {
                case 2:
                    // 会员价格
                    saveManyList(productSaveParamsDTO.getMemberPriceList(), product.getId(), memberPriceService);
                    break;
                case 3:
                    // 阶梯价格
                    saveManyList(productSaveParamsDTO.getProductLadderList(), product.getId(), productLadderService);
                    break;
                case 4:
                    // 满减价格
                    saveManyList(productSaveParamsDTO.getProductFullReductionList(), product.getId(), productFullReductionService);
                    break;
            }
            // sku
            saveManyList(productSaveParamsDTO.getSkuStockList(), product.getId(), skuStockService);
            // spu
            saveManyList(productSaveParamsDTO.getProductAttributeValueList(), product.getId(), productAttributeValueService);
        }
        return true;
    }

    @Override
    public ProductUpdateInitDTO getUpdateInfo(Long id) {
        return productMapper.getUpdateInfo(id);
    }

    @Override
    public boolean update(ProductSaveParamsDTO productSaveParamsDTO) {
        // 保存商品基本信息 -- 主表
        PmsProduct product = productSaveParamsDTO;
        boolean result = updateById(product);
        if (result) {
            switch (product.getPromotionType()) {
                case 2:
                    // 根据商品id删除
                    deleteManyListByProductId(product.getId(), memberPriceService);
                    // 会员价格
                    saveManyList(productSaveParamsDTO.getMemberPriceList(), product.getId(), memberPriceService);
                    break;
                case 3:
                    deleteManyListByProductId(product.getId(), productLadderService);
                    // 阶梯价格
                    saveManyList(productSaveParamsDTO.getProductLadderList(), product.getId(), productLadderService);
                    break;
                case 4:
                    deleteManyListByProductId(product.getId(), productFullReductionService);
                    // 满减价格
                    saveManyList(productSaveParamsDTO.getProductFullReductionList(), product.getId(), productFullReductionService);
                    break;
            }
            // sku
            deleteManyListByProductId(product.getId(), skuStockService);
            saveManyList(productSaveParamsDTO.getSkuStockList(), product.getId(), skuStockService);
            // spu
            deleteManyListByProductId(product.getId(), productAttributeValueService);
            saveManyList(productSaveParamsDTO.getProductAttributeValueList(), product.getId(), productAttributeValueService);
        }
        return true;
    }

    /**
     * 根据商品id删除关联数据
     */
    public void deleteManyListByProductId(Long productId, IService service) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        service.remove(queryWrapper);
    }

    /**
     * 公共方法：保存会员价格、阶梯价格、满减价格、sku、spu 商品的关联数据
     */
    public void saveManyList(List<?> list, Long productId, IService service) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        try {
            // 通过反射 赋值商品id
            for (Object obj : list) {
                Method setProductIdMethod = obj.getClass().getMethod("setProductId", Long.class);
                // 修改时清除主键id
                Method setId = obj.getClass().getMethod("setId", Long.class);
                setId.invoke(obj, (Long)null);
                // 调用setProductId
                setProductIdMethod.invoke(obj, productId);
            }
            service.saveBatch(list);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
