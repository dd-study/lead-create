package com.agileboot.domain.weixin;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;

public interface WeiXinService {

    WxMpService wxMpService();

    WxMpMessageRouter wxMpMessageRouter();

    WxMpConfigStorage wxMpConfigStorage();
}
