package com.agileboot.admin.controller.promotion;

import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.weixin.channel.db.ChannelService;
import com.agileboot.domain.weixin.channel.dto.ChannelDto;
import com.agileboot.domain.weixin.channel.query.ChannelQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 渠道
 *
 * @author bin
 */
@RestController
@RequestMapping("/channel")
@Validated
@RequiredArgsConstructor
@Tag(name = "渠道API", description = "渠道管理")
public class ChannelController extends BaseController {

    private final ChannelService channelService;

    /**
     * 获取部门列表
     */
    @Operation(summary = "部门列表")
    @GetMapping("/list")
    public ResponseDTO<List<ChannelDto>> list(ChannelQuery query) {
        List<ChannelDto> deptList = channelService.listChannel(query);
        return ResponseDTO.ok(deptList);
    }

}
