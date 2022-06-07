package com.macro.mall.tiny.modules.pms.model.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RelationAttrInfoDTO分类和筛选属性的关联数据", description="用户筛选属性的已保存数据初始化")
public class RelationAttrInfoDTO {

    // 商品类型id
    private Long attributeCategoryId;

    // 商品属性id
    private Long attributeId;
}
