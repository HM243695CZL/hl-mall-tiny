package com.macro.mall.tiny.modules.ums.service;

import com.macro.mall.tiny.modules.ums.model.UmsMemberLevel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 会员等级表 服务类
 * </p>
 *
 * @author macro
 * @since 2022-06-08
 */
public interface UmsMemberLevelService extends IService<UmsMemberLevel> {

    List<UmsMemberLevel> list(Integer defaultStatus);
}
