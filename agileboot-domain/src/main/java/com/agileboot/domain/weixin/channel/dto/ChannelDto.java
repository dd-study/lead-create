package com.agileboot.domain.weixin.channel.dto;

import com.agileboot.domain.weixin.channel.db.Channel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author bin
 */
@Data
public class ChannelDto implements Serializable {

    private String id;
    private String name;
    private String remark;
    private Long creatorId;
    private Date createTime;
    private Long updaterId;
    private Date updateTime;

    public ChannelDto() {
    }

    public ChannelDto(Channel channel) {
        super();
        BeanUtils.copyProperties(channel, this);
    }
}
