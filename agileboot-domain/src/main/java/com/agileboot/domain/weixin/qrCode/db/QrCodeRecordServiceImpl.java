package com.agileboot.domain.weixin.qrCode.db;

import cn.hutool.core.util.IdUtil;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.utils.Misc;
import com.agileboot.domain.system.user.db.SysUserEntity;
import com.agileboot.domain.system.user.db.SysUserService;
import com.agileboot.domain.weixin.channel.db.Channel;
import com.agileboot.domain.weixin.channel.db.ChannelService;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordDto;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordEditDto;
import com.agileboot.domain.weixin.qrCode.query.QrCodeRecordQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 二维码记录表 服务实现类
 */
@Service
@RequiredArgsConstructor
public class QrCodeRecordServiceImpl extends ServiceImpl<QrCodeRecordMapper, QrCodeRecord> implements QrCodeRecordService {

    @Resource
    private ChannelService channelService;
    @Resource
    private SysUserService userService;

    @Override
    public ResponseDTO add(QrCodeRecordEditDto editDto) {
        if (!Misc.isEmpty(editDto.getUserId())
                && userService.getById(editDto.getUserId()) == null) {
            return ResponseDTO.fail("员工不存在");
        }
        if (!Misc.isEmpty(editDto.getChannelId())
                && channelService.getById(editDto.getChannelId()) == null) {
            return ResponseDTO.fail("渠道不存在");
        }

        QrCodeRecord record = new QrCodeRecord();
        record.setId(IdUtil.simpleUUID());
        record.setUserId(editDto.getUserId());
        record.setChannelId(editDto.getChannelId());
        record.setRemark(editDto.getRemark());

        // todo 二维码

        return this.save(record) ? ResponseDTO.ok() : ResponseDTO.fail();
    }

    @Override
    public ResponseDTO edit(QrCodeRecordEditDto editDto) {
        if (Misc.isEmpty(editDto.getId())) {
            return ResponseDTO.fail("id参数为空");
        }
        QrCodeRecord record = this.getById(editDto.getId());
        if (null == record) {
            return ResponseDTO.fail("数据不存在");
        }


        record.setRemark(editDto.getRemark());
        return this.updateById(record) ? ResponseDTO.ok() : ResponseDTO.fail();
    }

    @Override
    public PageDTO<QrCodeRecordDto> listRecord(QrCodeRecordQuery query) {

        Page<QrCodeRecord> page = this.page(query.toPage(), query.toQueryWrapper());
        List<QrCodeRecordDto> list = page.getRecords().stream().map(record -> {
            QrCodeRecordDto dto = new QrCodeRecordDto(record);
            if (!Misc.isEmpty(record.getUserId())) {
                SysUserEntity user = userService.getById(record.getUserId());
                if (null != user) {
                    dto.setUserName(user.getUsername());
                }
            }
            if (!Misc.isEmpty(record.getChannelId())) {
                Channel channel = channelService.getById(record.getChannelId());
                if (channel != null) {
                    dto.setChannelName(channel.getName());
                }
            }

            return dto;
        }).collect(Collectors.toList());
        return new PageDTO<>(list, page.getTotal());
    }

}
