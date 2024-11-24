package com.agileboot.domain.weixin.qrCode.db;

import com.agileboot.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 二维码记录表
 */
@Getter
@Setter
@TableName("qr_code_record")
@ApiModel(value = "QrCodeRecord", description = "二维码记录表")
public class QrCodeRecord extends BaseEntity<QrCodeRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty("关联员工id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty("渠道id")
    @TableField("channel_id")
    private String channelId;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
