package com.agileboot.domain.weixin.qrCode.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class QrCodeRecordEditDto implements Serializable {

    /**
     * id
     */
    private String id;
    /**
     * 关联员工id
     */
    private String userId;
    /**
     * 渠道id
     */
    private String channelId;
    /**
     * 备注
     */
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;
}
