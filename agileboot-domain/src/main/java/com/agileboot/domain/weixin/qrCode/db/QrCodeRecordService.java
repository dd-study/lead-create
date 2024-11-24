package com.agileboot.domain.weixin.qrCode.db;

import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordEditDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 二维码记录表 服务类
 */
public interface QrCodeRecordService extends IService<QrCodeRecord> {

    /**
     * 创建
     *
     * @Param:
     * @return:
     */
    ResponseDTO create(QrCodeRecordEditDto editDto);


}
