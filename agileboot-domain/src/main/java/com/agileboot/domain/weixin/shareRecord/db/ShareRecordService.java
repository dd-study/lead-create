package com.agileboot.domain.weixin.shareRecord.db;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 分享记录表 服务类
 */
public interface ShareRecordService extends IService<ShareRecord> {


    /**
     * 检测部门名称是否一致
     *
     * @param deptName 部门名称
     * @param deptId   部门id
     * @param parentId 父级部门id
     * @return 校验结果
     */
    boolean isDeptNameDuplicated(String deptName, Long deptId, Long parentId);


}
