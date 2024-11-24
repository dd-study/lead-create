package com.agileboot.domain.weixin;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class WeiXinServiceImpl implements WeiXinService {

    private WxMpDefaultConfigImpl config;
    private WxMpService wxMpService;
    private WxMpMessageRouter wxMpMessageRouter;

    @Value("${lead.weiXin.appId}")
    private String appId;
    @Value("${lead.weiXin.appId}")
    private String appSecret;
    @Value("${lead.weiXin.token}")
    private String token;
    @Value("${lead.weiXin.aesKey}")
    private String aesKey;

    @PostConstruct
    public void init() {
        // todo
        config = new WxMpDefaultConfigImpl();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(appSecret); // 设置微信公众号的app corpSecret
        config.setToken(token); // 设置微信公众号的token
        config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey

        wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);

        wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
    }


    @Override
    public WxMpService wxMpService() {
        return this.wxMpService;
    }

    @Override
    public WxMpMessageRouter wxMpMessageRouter() {
        return this.wxMpMessageRouter;
    }

    @Override
    public WxMpConfigStorage wxMpConfigStorage() {
        return this.config;
    }
}
