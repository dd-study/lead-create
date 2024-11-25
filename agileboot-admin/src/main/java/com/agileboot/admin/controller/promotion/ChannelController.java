package com.agileboot.admin.controller.promotion;

import com.agileboot.admin.customize.aop.accessLog.AccessLog;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.enums.common.BusinessTypeEnum;
import com.agileboot.domain.weixin.channel.db.ChannelService;
import com.agileboot.domain.weixin.channel.dto.ChannelDto;
import com.agileboot.domain.weixin.channel.dto.ChannelEditDto;
import com.agileboot.domain.weixin.channel.query.ChannelQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 获取渠道列表
     */
    @Operation(summary = "渠道列表")
    @GetMapping("/list")
    public ResponseDTO<PageDTO<ChannelDto>> list(ChannelQuery query) {
        PageDTO<ChannelDto> pageDTO = channelService.listChannel(query);
        return ResponseDTO.ok(pageDTO);
    }

    /**
     * 添加渠道
     */
    @Operation(summary = "添加渠道")
    @AccessLog(title = "渠道管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping("/add")
    public ResponseDTO<Void> add(@RequestBody ChannelEditDto dto) {
        return channelService.add(dto);
    }

    /**
     * 修改渠道
     */
    @Operation(summary = "修改渠道")
    @AccessLog(title = "岗位管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("edit")
    public ResponseDTO<Void> edit(@RequestBody ChannelEditDto dto) {
        return channelService.edit(dto);
    }

}
