package com.agileboot.domain.weixin.shareRecord.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 分享记录表 服务实现类
 */
@Service
@RequiredArgsConstructor
public class ShareRecordServiceImpl extends ServiceImpl<ShareRecordMapper, ShareRecord> implements ShareRecordService {


    @Override
    public boolean isDeptNameDuplicated(String deptName, Long deptId, Long parentId) {
        QueryWrapper<ShareRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_name", deptName)
                .ne(deptId != null, "dept_id", deptId)
                .eq(parentId != null, "parent_id", parentId);

        return this.baseMapper.exists(queryWrapper);
    }

}
