package com.macro.mall.tiny.modules.pms.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProductSaveParamsDTO商品初始化数据", description = "用于商品的初始化")
public class ProductUpdateInitDTO extends ProductSaveParamsDTO {
    // 一级分类id
    private Long cateParentId;
}
