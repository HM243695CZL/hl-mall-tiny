package com.macro.mall.tiny.modules.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.pms.model.dto.ProductAttributeDTO;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
public interface PmsProductAttributeCategoryService extends IService<PmsProductAttributeCategory> {

    Page list(Integer pageNum, Integer pageSize);

    boolean create(PmsProductAttributeCategory productAttributeCategory);

    List<ProductAttributeDTO> getListWithAttr();
}
