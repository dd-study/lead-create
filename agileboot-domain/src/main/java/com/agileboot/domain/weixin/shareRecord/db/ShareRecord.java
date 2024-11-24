package com.agileboot.domain.weixin.shareRecord.db;

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
 * 分享记录表
 */
@Getter
@Setter
@TableName("share_record")
@ApiModel(value = "ShareRecord对象", description = "分享记录表")
public class ShareRecord extends BaseEntity<ShareRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty("关联实体id")
    @TableField("model_id")
    private String modelId;

    @ApiModelProperty("分享类型")
    @TableField("type")
    private Integer type;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
