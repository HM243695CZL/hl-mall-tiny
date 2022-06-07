package com.macro.mall.tiny.modules.pms.mapper;

import com.macro.mall.tiny.modules.pms.model.PmsProductAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.dto.ProductAttributeDTO;
import com.macro.mall.tiny.modules.pms.model.dto.RelationAttrInfoDTO;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttribute> {

    List<ProductAttributeDTO> getListWithAttr();

    List<RelationAttrInfoDTO> getRelationAttrInfoByCid(Long cId);
}
