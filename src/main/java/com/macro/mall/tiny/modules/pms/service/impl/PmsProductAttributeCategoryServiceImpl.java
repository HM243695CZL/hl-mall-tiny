package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductAttributeMapper;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttributeCategory;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductAttributeCategoryMapper;
import com.macro.mall.tiny.modules.pms.model.dto.ProductAttributeDTO;
import com.macro.mall.tiny.modules.pms.service.PmsProductAttributeCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
@Service
public class PmsProductAttributeCategoryServiceImpl extends ServiceImpl<PmsProductAttributeCategoryMapper, PmsProductAttributeCategory> implements PmsProductAttributeCategoryService {

    @Autowired
    PmsProductAttributeMapper productAttributeMapper;

    @Override
    public Page list(Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        return page(page);
    }

    @Override
    public boolean create(PmsProductAttributeCategory productAttributeCategory) {
        productAttributeCategory.setAttributeCount(0);
        productAttributeCategory.setParamCount(0);
        return save(productAttributeCategory);
    }

    /**
     * 筛选属性的级联数据
     * @return
     */
    @Override
    public List<ProductAttributeDTO> getListWithAttr() {
        return productAttributeMapper.getListWithAttr();
    }
}
