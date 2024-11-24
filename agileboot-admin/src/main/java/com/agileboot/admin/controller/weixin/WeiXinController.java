package com.agileboot.admin.controller.weixin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.agileboot.admin.utils.WxXmlUtils;
import com.agileboot.admin.utils.WxXmlUtils.WxMsg;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.weixin.WeiXinService;
import com.agileboot.domain.weixin.shareRecord.db.ShareRecord;
import com.agileboot.domain.weixin.shareRecord.db.ShareRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weixin")
@RequiredArgsConstructor
@Tag(name = "微信API", description = "微信API")
public class WeiXinController {

    private final static Logger logger = LoggerFactory.getLogger(WeiXinController.class);

    @Resource
    private WeiXinService weiXinService;
    @Resource
    private ShareRecordService shareRecordService;
    @Value("${spring.profiles.active}")
    private String profilesActive;

    /**
     * 微信token 验证
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("")
    public void checkToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!weiXinService.wxMpService().checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            logger.info("微信连接成功！");
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage outMessage = weiXinService.wxMpMessageRouter().route(inMessage);
            if (outMessage == null) {
                //为null，说明路由配置有问题，需要注意
                response.getWriter().write("");
            }
            response.getWriter().write(outMessage.toXml());
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(),
                    weiXinService.wxMpConfigStorage(), timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = weiXinService.wxMpMessageRouter().route(inMessage);
            if (outMessage == null) {
                //为null，说明路由配置有问题，需要注意
                response.getWriter().write("");
            }
            response.getWriter().write(outMessage.toEncryptedXml(weiXinService.wxMpConfigStorage()));
            return;
        }

        response.getWriter().println("不可识别的加密类型");
    }


    /**
     * 处理微信事件回调
     */
    private String handleCallback(HttpServletRequest request) throws Exception {
        String xml = WxXmlUtils.inputStream2String(request.getInputStream(), "UTF-8");
        // todo 以后删掉
        logger.info(xml);

        // 解析xml
        WxMsg wxmsg = WxXmlUtils.parseXml(xml);
        String event = wxmsg.getEvent();
        String eventKey = wxmsg.getEventKey();

        // 处理 订阅事件，保存h5用户openId信息
        if ("subscribe".equals(event) || "SCAN".equals(event)) {
            String openId = wxmsg.getFromUserName();
            try {
                // 访问接口获取unionId
                String accessToken = weiXinService.wxMpService().getAccessToken();
                Map<String, Object> params = new HashMap<>();
                params.put("access_token", accessToken);
                params.put("openid", openId);
                HttpResponse response = HttpRequest.get("https://api.weixin.qq.com/cgi-bin/user/info")
                        .form(params).execute();
                if (response.isOk()) {
                    JSONObject followResult = new JSONObject(response.body());
                    String unionId = followResult.getString("unionid");
                    /*
                    // 看看是否已经在小程序授权过，得到userId
                    User user = userService.getUserByWxunionid(unionId);
                    String userId = user != null ? user.getId() : null;
                    gxOpenIdInfoService.addByFollow(openId, weiXinShareUtil.getAppId(), unionId, userId);

                     */
                }
            } catch (Exception e) {
                logger.info("处理订阅事件保存h5用户openId:{} 信息失败", openId, e);
            }
        }

        // 要返回success代表成功
        return "success";
    }


    @PostMapping("genQr")
    public void genQr(HttpServletResponse response) throws WxErrorException, IOException {
        ShareRecord shareRecord = new ShareRecord();
        shareRecord.setId(IdUtil.simpleUUID());
        shareRecord.setModelId("11");
        shareRecord.setType(1);
        ;
        shareRecordService.save(shareRecord);

        WxMpQrCodeTicket ticket = weiXinService.wxMpService()
                .getQrcodeService().qrCodeCreateLastTicket(shareRecord.getId());
        File file = weiXinService.wxMpService().getQrcodeService().qrCodePicture(ticket);

        IoUtil.write(response.getOutputStream(), true, new FileReader(file).readBytes());

    }

    /**
     * 日志打印accessToken
     *
     * @param
     * @return
     */
    @GetMapping("outputH5AccessToen")
    public ResponseDTO getH5AccessToken(@RequestParam String pwd) throws WxErrorException {
        if (!"fuiabigbaw5753243".equals(pwd)) {
            return ResponseDTO.fail();
        }

        logger.info("accessToken:" + weiXinService.wxMpService().getAccessToken());
        return ResponseDTO.ok();
    }

}
