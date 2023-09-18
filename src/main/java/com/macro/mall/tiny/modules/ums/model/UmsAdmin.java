package com.macro.mall.tiny.modules.ums.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_admin")
@ApiModel(value="UmsAdmin对象", description="后台用户表")
public class UmsAdmin implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    @ApiModelProperty(value = "1： 男  2： 女")
    private Integer sex;

    @ApiModelProperty(value = "微信的openID")
    private String wxOpenId;

    @ApiModelProperty(value = "最近一次登录IP")
    private String lastLoginIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最近一次登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "角色id列表")
    @TableField(exist = false)
    private List<Integer> roleIds;

    @ApiModelProperty(value = "角色名称")
    @TableField(exist = false)
    private List<String> roles;

}
