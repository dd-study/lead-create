package com.agileboot.domain.weixin.qrCode.db;

import cn.hutool.core.util.IdUtil;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.utils.Misc;
import com.agileboot.domain.system.user.db.SysUserService;
import com.agileboot.domain.weixin.channel.db.ChannelService;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordEditDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public ResponseDTO create(QrCodeRecordEditDto editDto) {
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

        return this.save(record) ? ResponseDTO.ok() : ResponseDTO.fail();
    }
}
