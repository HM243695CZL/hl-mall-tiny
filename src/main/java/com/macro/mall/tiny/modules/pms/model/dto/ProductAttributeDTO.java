package com.macro.mall.tiny.modules.pms.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macro.mall.tiny.modules.pms.model.PmsProductAttribute;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProductAttributeDTO筛选属性的数据传输对象", description="用于商品分类--筛选属性下拉级联数据")
public class ProductAttributeDTO {

    // 商品类型id
    private Long id;

    // 商品类型名称
    private String name;

    // 商品属性二级级联
    private List<PmsProductAttribute> productAttributeList;
}
