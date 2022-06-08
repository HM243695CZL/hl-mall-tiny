package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.macro.mall.tiny.modules.ums.model.UmsMemberLevel;
import com.macro.mall.tiny.modules.ums.mapper.UmsMemberLevelMapper;
import com.macro.mall.tiny.modules.ums.service.UmsMemberLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2022-06-08
 */
@Service
public class UmsMemberLevelServiceImpl extends ServiceImpl<UmsMemberLevelMapper, UmsMemberLevel> implements UmsMemberLevelService {

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        QueryWrapper<UmsMemberLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsMemberLevel::getDefaultStatus, defaultStatus);
        return list(queryWrapper);
    }
}
