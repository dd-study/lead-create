package com.agileboot.domain.weixin.qrCode.db;

import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordDto;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordEditDto;
import com.agileboot.domain.weixin.qrCode.query.QrCodeRecordQuery;
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
    ResponseDTO add(QrCodeRecordEditDto editDto);

    /**
     * 修改
     *
     * @return
     * @Param
     */
    ResponseDTO edit(QrCodeRecordEditDto editDto);

    PageDTO<QrCodeRecordDto> listRecord(QrCodeRecordQuery query);


}
