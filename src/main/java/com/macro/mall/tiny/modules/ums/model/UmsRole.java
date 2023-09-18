package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.macro.mall.tiny.common.vo.BaseModelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_role")
@ApiModel(value="UmsRole对象", description="后台用户角色表")
public class UmsRole extends BaseModelDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色key")
    @TableField("`key`")
    private String key;

    @ApiModelProperty(value = "描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty(value = "启用状态")
    private Boolean enabled;

}
