package com.macro.mall.tiny.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsProductCategory;
import com.macro.mall.tiny.modules.pms.mapper.PmsProductCategoryMapper;
import com.macro.mall.tiny.modules.pms.service.PmsProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
