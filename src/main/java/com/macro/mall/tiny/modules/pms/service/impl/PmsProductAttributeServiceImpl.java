package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttribute;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductAttributeMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttributeCategory;
import com.macro.mall.tiny.modules.pms.model.dto.RelationAttrInfoDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductAttributeCategoryService;
import com.macro.mall.tiny.modules.pms.service.PmsProductAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@Service
@SuppressWarnings("all")
public class PmsProductAttributeServiceImpl extends ServiceImpl<PmsProductAttributeMapper, PmsProductAttribute> implements PmsProductAttributeService {

    @Autowired
    PmsProductAttributeMapper productAttributeMapper;

    @Autowired
    PmsProductAttributeCategoryService productAttrCategoryService;

    @Override
    public Page list(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        Page page = new Page();
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PmsProductAttribute::getProductAttributeCategoryId, cid)
                .eq(PmsProductAttribute::getType, type)
                .orderByAsc(PmsProductAttribute::getSort);
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean create(PmsProductAttribute productAttribute) {
        // 保存商品属性
        boolean save = save(productAttribute);
        // 更新对应属性、参数的数量
        if (save) {
            UpdateWrapper<PmsProductAttributeCategory> updateWrapper = new UpdateWrapper<>();
            // 属性
            if (productAttribute.getType() == 0) {
                updateWrapper.setSql("attribute_count = attribute_count + 1");
            }
            // 参数
            else if (productAttribute.getType() == 1) {
                updateWrapper.setSql("param_count = param_count + 1");
            }
            updateWrapper.lambda().eq(PmsProductAttributeCategory::getId, productAttribute.getProductAttributeCategoryId());
            productAttrCategoryService.update(updateWrapper);
        }
        return save;
    }

    @Override
    @Transactional
    public boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        // 获取当前属性的类别
        PmsProductAttribute productAttribute = null;
        for (Long id : ids) {
            productAttribute = getById(id);
            if (productAttribute != null) {
                break;
            }
        }

        int length = productAttributeMapper.deleteBatchIds(ids);

        if (length > 0 && productAttribute != null) {
            UpdateWrapper<PmsProductAttributeCategory> updateWrapper = new UpdateWrapper<>();
            // 属性
            if (productAttribute.getType() == 0) {
                updateWrapper.setSql("attribute_count = attribute_count -" + length);
            }
            // 参数
            else if (productAttribute.getType() == 1) {
                updateWrapper.setSql("param_count = param_count - " + length);
            }
            updateWrapper.lambda().eq(PmsProductAttributeCategory::getId, productAttribute.getProductAttributeCategoryId());
            productAttrCategoryService.update(updateWrapper);
        }
        return length > 0;
    }

    @Override
    public List<RelationAttrInfoDTO> getRelationAttrInfoByCid(Long cId) {
        return productAttributeMapper.getRelationAttrInfoByCid(cId);
    }
}
