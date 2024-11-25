package com.agileboot.admin.controller.promotion;

import com.agileboot.admin.customize.aop.accessLog.AccessLog;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.enums.common.BusinessTypeEnum;
import com.agileboot.domain.weixin.qrCode.db.QrCodeRecordService;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordDto;
import com.agileboot.domain.weixin.qrCode.dto.QrCodeRecordEditDto;
import com.agileboot.domain.weixin.qrCode.query.QrCodeRecordQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 二维码管理
 *
 * @author bin
 */
@RestController
@RequestMapping("/promotion/qrCode")
@Validated
@RequiredArgsConstructor
@Tag(name = "二维码管理API", description = "二维码管理")
public class QrCodeRecordController extends BaseController {

    private final QrCodeRecordService qrCodeRecordService;

    /**
     * 获取二维码列表
     */
    @Operation(summary = "二维码列表")
    @GetMapping("/list")
    public ResponseDTO<PageDTO<QrCodeRecordDto>> list(QrCodeRecordQuery query) {
        PageDTO<QrCodeRecordDto> pageDTO = qrCodeRecordService.listRecord(query);
        return ResponseDTO.ok(pageDTO);
    }

    /**
     * 添加二维码
     */
    @Operation(summary = "添加二维码")
    @AccessLog(title = "二维码管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping("/add")
    public ResponseDTO<Void> add(@RequestBody QrCodeRecordEditDto dto) {
        return qrCodeRecordService.add(dto);
    }

    /**
     * 修改二维码
     */
    @Operation(summary = "修改二维码")
    @AccessLog(title = "二维码管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("edit")
    public ResponseDTO<Void> edit(@RequestBody QrCodeRecordEditDto dto) {
        return qrCodeRecordService.edit(dto);
    }

}
