package com.agileboot.domain.weixin.channel.db;

import cn.hutool.core.util.IdUtil;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.utils.Misc;
import com.agileboot.domain.weixin.channel.dto.ChannelDto;
import com.agileboot.domain.weixin.channel.dto.ChannelEditDto;
import com.agileboot.domain.weixin.channel.query.ChannelQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 渠道 服务实现类
 */
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {


    @Override
    public ResponseDTO create(ChannelEditDto editDto) {
        QueryWrapper<Channel> wrapper = new QueryWrapper<>();
        wrapper.eq("name", editDto.getName());
        if (!this.list(wrapper).isEmpty()) {
            return ResponseDTO.fail("渠道名已存在");
        }

        Channel channel = new Channel();
        channel.setId(IdUtil.simpleUUID());
        channel.setName(editDto.getName());
        channel.setRemark(editDto.getRemark());

        return this.save(channel) ? ResponseDTO.ok() : ResponseDTO.fail();
    }

    @Override
    public ResponseDTO edit(ChannelEditDto editDto) {
        if (Misc.isEmpty(editDto.getId())) {
            return ResponseDTO.fail("id参数为空");
        }
        Channel channel = this.getById(editDto.getId());
        if (null == channel) {
            return ResponseDTO.fail("数据不存在");
        }
        QueryWrapper<Channel> wrapper = new QueryWrapper<>();
        wrapper.eq("name", editDto.getName());
        Channel existOne = this.getOne(wrapper, false);
        if (null != existOne && !existOne.getId().equals(editDto.getId())) {
            return ResponseDTO.fail("改名称已存在");
        }

        channel.setName(editDto.getName());
        channel.setRemark(editDto.getRemark());
        return this.save(channel) ? ResponseDTO.ok() : ResponseDTO.fail();
    }

    @Override
    public List<ChannelDto> listChannel(ChannelQuery query) {
        List<Channel> list = this.list(query.toQueryWrapper());
        return list.stream().map(ChannelDto::new).collect(Collectors.toList());
    }


}
