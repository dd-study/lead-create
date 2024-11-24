package com.agileboot.domain.weixin.channel.db;

import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.weixin.channel.dto.ChannelDto;
import com.agileboot.domain.weixin.channel.dto.ChannelEditDto;
import com.agileboot.domain.weixin.channel.query.ChannelQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 渠道 服务类
 */
public interface ChannelService extends IService<Channel> {

    /**
     * 创建
     *
     * @Param:
     * @return:
     */
    ResponseDTO create(ChannelEditDto editDto);

    /**
     * 修改
     *
     * @return
     * @Param
     */
    ResponseDTO edit(ChannelEditDto editDto);

    List<ChannelDto> listChannel(ChannelQuery query);
}
