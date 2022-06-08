package com.macro.mall.tiny.modules.ums.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsMemberLevel;
import com.macro.mall.tiny.modules.ums.service.UmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 会员等级表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2022-06-08
 */
@RestController
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {

    @Autowired
    UmsMemberLevelService memberLevelService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list (@RequestParam(value = "defaultStatus", defaultValue = "0") Integer defaultStatus) {
        List<UmsMemberLevel> list = memberLevelService.list(defaultStatus);
        return CommonResult.success(list);
    }
}

