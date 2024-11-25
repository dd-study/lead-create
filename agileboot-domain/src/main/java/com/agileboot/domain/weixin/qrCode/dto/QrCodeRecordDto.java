package com.agileboot.domain.weixin.qrCode.dto;

import com.agileboot.domain.weixin.qrCode.db.QrCodeRecord;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author bin
 */
@Data
public class QrCodeRecordDto implements Serializable {

    private String id;
    private String userId;
    private String channelId;
    private String remark;
    private Long creatorId;
    private Date createTime;
    private Long updaterId;
    private Date updateTime;

    private String userName;
    private String channelName;

    public QrCodeRecordDto() {
    }

    public QrCodeRecordDto(QrCodeRecord record) {
        super();
        BeanUtils.copyProperties(record, this);
    }
}
