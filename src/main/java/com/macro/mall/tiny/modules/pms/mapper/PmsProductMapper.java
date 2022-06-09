package com.macro.mall.tiny.modules.pms.mapper;

import com.macro.mall.tiny.modules.pms.model.PmsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.pms.model.dto.ProductUpdateInitDTO;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2022-06-06
 */
public interface PmsProductMapper extends BaseMapper<PmsProduct> {

    ProductUpdateInitDTO getUpdateInfo(Long id);
}
