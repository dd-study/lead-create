package com.agileboot.domain.weixin.channel.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ChannelEditDto implements Serializable {

    /**
     * id
     */
    private String id;
    /**
     * 渠道名
     */
    @Size(max = 200, message = "渠道名长度不能超过200个字符")
    @NotEmpty
    private String name;
    /**
     * 备注
     */
    @Size(max = 200, message = "备注长度不能超过200个字符")
    private String remark;
}
