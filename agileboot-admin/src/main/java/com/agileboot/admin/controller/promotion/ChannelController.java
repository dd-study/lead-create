package com.agileboot.admin.controller.promotion;

import com.agileboot.admin.customize.aop.accessLog.AccessLog;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.enums.common.BusinessTypeEnum;
import com.agileboot.domain.weixin.channel.db.ChannelService;
import com.agileboot.domain.weixin.channel.dto.ChannelDto;
import com.agileboot.domain.weixin.channel.dto.ChannelEditDto;
import com.agileboot.domain.weixin.channel.query.ChannelQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 渠道
 *
 * @author bin
 */
@RestController
@RequestMapping("/promotion/channel")
@Validated
@RequiredArgsConstructor
@Tag(name = "渠道API", description = "渠道管理")
public class ChannelController extends BaseController {

    private final ChannelService channelService;

    /**
     * 获取部门列表
     */
    @Operation(summary = "渠道列表")
    @GetMapping("/list")
    public ResponseDTO<List<ChannelDto>> list(ChannelQuery query) {
        List<ChannelDto> deptList = channelService.listChannel(query);
        return ResponseDTO.ok(deptList);
    }

    /**
     * 添加渠道
     */
    @Operation(summary = "添加渠道")
    @AccessLog(title = "渠道管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping("/add")
    public ResponseDTO<Void> add(@RequestBody ChannelEditDto dto) {
        return channelService.create(dto);
    }

}
