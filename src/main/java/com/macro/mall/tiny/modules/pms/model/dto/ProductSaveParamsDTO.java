package com.macro.mall.tiny.modules.pms.model.dto;

import com.macro.mall.tiny.modules.pms.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProductSaveParamsDTO保存商品数据传输对象", description = "用户商品的添加和修改")
public class ProductSaveParamsDTO  extends PmsProduct {

    // 会员价格
    @ApiModelProperty(value = "会员价格")
    private List<PmsMemberPrice> memberPriceList;
    // 商品满减
    @ApiModelProperty(value = "商品满减")
    private List<PmsProductFullReduction> productFullReductionList;
    // 商品阶梯价格
    @ApiModelProperty(value = "商品阶梯价格")
    private List<PmsProductLadder> productLadderList;
    // 商品属性相关
    @ApiModelProperty(value = "商品属性相关")
    private List<PmsProductAttributeValue> productAttributeValueList;
    // 商品sku库存信息
    @ApiModelProperty(value = "商品sku库存信息")
    @Size(min = 1, message = "SKU至少有一项")
    @Valid // 开启支持嵌套验证
    private List<PmsSkuStock> skuStockList;
}
