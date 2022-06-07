package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductCategoryAttributeRelationMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductCategoryMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategoryAttributeRelation;
import com.macro.mall.tiny.modules.pms.model.dto.PmsProductCategoryDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryAttributeRelationService;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@Service
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryService {

    @Autowired
    PmsProductCategoryAttributeRelationService relationService;

    /**
     * 获取商品分类列表
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page list(Long parentId, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        QueryWrapper<PmsProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PmsProductCategory::getParentId, parentId);
        return page(page, queryWrapper);
    }

    /**
     * 修改导航栏显示状态
     * @param ids
     * @param navStatus
     * @return
     */
    @Override
    public boolean updateNavStatus(List<Long> ids, Integer navStatus) {
        UpdateWrapper<PmsProductCategory> updateWrapper = new UpdateWrapper<>();
        // 需要更新的列数据
        updateWrapper.lambda().set(PmsProductCategory::getNavStatus, navStatus)
                // 条件
                .in(PmsProductCategory::getId, ids);
        return update(updateWrapper);
    }

    @Override
    @Transactional
    public boolean create(PmsProductCategoryDTO productCategoryDTO) {
        // 保存商品分类
        PmsProductCategory productCategory = new PmsProductCategory();
        // 拷贝数据
        BeanUtils.copyProperties(productCategoryDTO, productCategory);
         // 设置商品数量和级别的默认值
        productCategory.setProductCount(0);
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            // 只有二级分类，可以写死
            productCategory.setLevel(1);
        }
        save(productCategory);
        saveAttrRelation(productCategoryDTO, productCategory);
        return true;
    }

    /**
     * 添加关联属性
     * @param productCategoryDTO
     * @param productCategory
     * @return
     */
    private boolean saveAttrRelation(PmsProductCategoryDTO productCategoryDTO, PmsProductCategory productCategory) {
        List<Long> productAttributeIdList = productCategoryDTO.getProductAttributeIdList();
        List<PmsProductCategoryAttributeRelation> list = new ArrayList<>();
        for (Long attrId : productAttributeIdList) {
            // 获取保存的商品分类id，保存商品分类筛选属性关系
            PmsProductCategoryAttributeRelation productCategoryAttributeRelation = new PmsProductCategoryAttributeRelation();
            productCategoryAttributeRelation.setProductCategoryId(productCategory.getId());
            productCategoryAttributeRelation.setProductAttributeId(attrId);
            list.add(productCategoryAttributeRelation);
        }
        return relationService.saveBatch(list);
    }

    @Override
    public boolean update(PmsProductCategoryDTO productCategoryDTO) {
        // 保存商品分类
        PmsProductCategory productCategory = new PmsProductCategory();
        // 拷贝数据
        BeanUtils.copyProperties(productCategoryDTO, productCategory);
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            // 只有二级分类，可以写死
            productCategory.setLevel(1);
        }
        updateById(productCategory);

        // 删除已保存的关联属性
        QueryWrapper<PmsProductCategoryAttributeRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PmsProductCategoryAttributeRelation::getProductCategoryId, productCategory.getId());
        relationService.remove(queryWrapper);

        saveAttrRelation(productCategoryDTO, productCategory);
        return true;
    }
}
