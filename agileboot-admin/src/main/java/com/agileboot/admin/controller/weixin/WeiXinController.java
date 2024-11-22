package com.agileboot.admin.controller.weixin;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.agileboot.admin.utils.CheckUtil;
import com.agileboot.admin.utils.Signature;
import com.agileboot.admin.utils.WxXmlUtils;
import com.agileboot.admin.utils.WxXmlUtils.WxMsg;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class WeiXinController {

    private final static Logger logger = LoggerFactory.getLogger(WeixinController.class);

    @Resource
    private WeiXinShareUtil weiXinMpShareUtil;
    @Resource
    private WeiXinShareUtil weiXinShareUtil;
    @Resource
    private XgjAgentComponent agentComponent;
    @Resource
    private DayiComponentConstant dayiComponentConstant;
    @Resource
    private ShareService shareService;
    @Resource
    private UserService userService;
    @Resource
    private GxOpenIdInfoService gxOpenIdInfoService;
    @Value("${spring.profiles.active}")
    private String profilesActive;

    /**
     * 微信token 验证
     *
     * @param req
     * @param response
     * @return
     *
     * @throws Exception
     */
    @RequestMapping("")
    public String checkToken(HttpServletRequest req, HttpServletResponse response) throws Exception {
        String method = req.getMethod();
        // 如果是微信发过来的GET请求
        if ("GET".equals(method)) {
            Signature sg = new Signature(req.getParameter("signature"), req.getParameter("timestamp"),
                    req.getParameter("nonce"), req.getParameter("echostr"));
            if (sg.getSignature() != null && CheckUtil.checkSignature(sg)) {
                logger.info("微信连接成功！");
                return sg.getEchostr();
            }
        } else if ("POST".equals(method)) {
            // 回调数据未做加密
            return this.handleCallback(req);
        }

        return "";
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
                String accessToken = weiXinShareUtil.getAccessToken();
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

    /**
     * 分享api
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/share")
    public BizResult<WeiXinShareSignResult> share(HttpServletRequest request, String url, String shareUrl,
            String productId) throws Exception {

        String shareImgUrl = "";
        ShareRecord record = getUrl(request, shareUrl, productId);
        String qR0Url = record == null ? shareUrl : record.getUrl();
        WeiXinShareSignResult wx = weiXinShareUtil.getSign(url, qR0Url, shareImgUrl);
        return BizResult.succ(wx);
    }

    /**
     * 分享二维码
     *
     * @param request
     * @param response
     * @param shareUrl
     * @return BizResult
     *
     * @throws Exception
     */
    @GetMapping("/qr")
    public void qr(HttpServletRequest request, HttpServletResponse response, String avatar, String shareUrl)
            throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        QrCodeUtil.writeToStream(shareUrl, avatar, 200, 200, "gif", response.getOutputStream());
    }

    /**
     * 微信小程序码
     *
     * @param scene 路径参数 不要乱写 a=1&b=2...
     * @param pages 路径url 必须在小程序中发布过的页面 /pages/home/index
     */
    @GetMapping("/mini/qr")
    public void miniQr(HttpServletResponse response, String pages, String scene) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        if (Misc.isEmpty(pages)) {
            pages = "pages/home/index";
        }
        if (Misc.isEmpty(scene)) {
            scene = "t=" + System.currentTimeMillis();
        }
        if ("prod".equals(profilesActive)) {
            weiXinMpShareUtil.getMiniQr(scene, pages, response.getOutputStream());
        } else {
            // refreshAccessToken()强刷为了解决nk、dev环境时间不一致导致accessToken过期时间错乱，引起accessToken失效问题。
            weiXinMpShareUtil.refreshAccessToken();
            weiXinMpShareUtil.getMiniQr(scene, pages, response.getOutputStream());
        }
    }

    /**
     * 微信小程序获取邀请码
     *
     * @param shareUrl  分享页面，不需要完整的url
     * @param productId 没有可以不填
     */
    @GetMapping("/mini/qR0")
    public BizResult miniQr0(HttpServletRequest request, String shareUrl, String productId) {
        if (Misc.isEmpty(shareUrl)) {
            return BizResult.fail(ResultCode.PARAM_NOT_COMPLETE);
        }
        ShareRecord record = getUrl(request, shareUrl, productId);
        if (record != null) {
            return BizResult.succ(IdConvertUtil.to62(record.getId()), BizResult.SUCCESS.getMsg());
        }
        return BizResult.succ("未登录，无法获取邀请");
    }

    /**
     * app 商品分享
     *
     * @param request
     * @param productId
     * @return
     *
     * @throws Exception
     */
    /*
    @GetMapping("/shareProductByApp")
    public BizResult<Map<String, String>> shareProductByApp(HttpServletRequest request, String productId) {
        ProductInfo product = this.productSearchService.getProductInfo(productId);
        if (product != null) {
            String shareUrl =
                    dayiComponentConstant.getXgjAgentHost() + "/pages/shopkeeper/goods/index?productId=" + productId;
            String shareImageUrl = dayiComponentConstant.getStorageHost() + this.getSharePicture(product);
            ShareRecord record = getUrl(request, shareUrl, productId);
            String qR0Url = record == null ? shareUrl : record.getUrl();
            Map<String, String> map = new HashMap<>(10);
            map.put("title", product.getTitle());
            map.put("description", product.getSubTitle());
            map.put("shareImgUrl", shareImageUrl);
            map.put("shareUrl", qR0Url);
            return BizResult.succ(map);
        } else {
            return BizResult.FAIL;
        }
    }
*/
    /**
     * 获取商品分享图（产品决定用第一个轮播图）
     *
     * @param productInfo 商品信息
     * @return 分享图
     */
    /*
    private String getSharePicture(ProductInfo productInfo) {
        if (productInfo == null) {
            return "";
        }
        // 优先返回分享图（轮播图）
        if (!Misc.isEmpty(productInfo.getPictures())) {
            return productInfo.getPictures().get(0);
        }
        // 没有设置分享图（轮播图），则返回主图
        return productInfo.getPicture();
    }
*/

    /**
     * app 平台分享
     *
     * @param request
     * @return
     */
    @GetMapping("/shareHomeByApp")
    public BizResult<Map<String, String>> shareHomeByApp(HttpServletRequest request) {
        XgjAgent agent = agentComponent.getCurrentXgjAgent(request);
        if (agent != null) {
            String shareUrl = dayiComponentConstant.getXgjAgentHost() + "/pages/shopkeeper/uc/index";
            String shareImageUrl = dayiComponentConstant.getXgjAgentHost() + "/static2/img/common/thumb.jpg";
            ShareRecord record = getUrl(request, shareUrl, "");
            String qR0Url = record == null ? shareUrl : record.getUrl();
            Map<String, String> map = new HashMap<>(4);
            map.put("title", " 六客云帮，代运营分销业务");
            map.put("description", "一个全新的分销平台，新零售，新模式，新机会");
            map.put("shareImgUrl", shareImageUrl);
            map.put("shareUrl", qR0Url);
            return BizResult.succ(map);
        } else {
            return BizResult.FAIL;
        }
    }

    /**
     * app 店铺分享
     *
     * @param request
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/shareMemberShopByApp")
    public BizResult<Map<String, String>> shareMemberShopByApp(HttpServletRequest request, String shopId) {
        XgjAgent agent = agentComponent.getCurrentXgjAgent(request);
        if (agent != null) {
            String shareUrl =
                    dayiComponentConstant.getXgjAgentHost() + "/pages/shopkeeper/shop/shop-preview?shopId=" + shopId;
            String shareImageUrl = dayiComponentConstant.getXgjAgentHost() + "/static2/img/common/logo.png";
            ShareRecord record = getUrl(request, shareUrl, "");
            String qR0Url = record == null ? shareUrl : record.getUrl();
            Map<String, String> map = new HashMap<>(4);
            map.put("title", agent.getWxName() + "的雀喜店铺上新了，向你推荐");
            map.put("description", "雀喜易购-花更少、赚更多、买更好");
            map.put("shareImgUrl", shareImageUrl);
            map.put("shareUrl", qR0Url);
            return BizResult.succ(map);
        } else {
            return BizResult.FAIL;
        }
    }

    /**
     * 是否关注了公众号
     *
     * @return true或者false
     */
    @GetMapping("isFollowH5")
    public BizResult<Boolean> isFollowH5(HttpServletRequest request) {
        XgjAgent xgjAgent = agentComponent.getCurrentXgjAgent(request);
        if (xgjAgent == null) {
            return BizResult.succ(Boolean.FALSE);
        }
        String userId = xgjAgent.getUserId();

        GxOpenIdInfo h5OpenIdInfo = gxOpenIdInfoService.getByUserIdAndAppId(userId, weiXinShareUtil.getAppId());
        if (h5OpenIdInfo == null) {
            return BizResult.succ(Boolean.FALSE);
        }
        String accessToken = weiXinShareUtil.getAccessToken();
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", h5OpenIdInfo.getOpenId());

        HttpResult httpResult = HttpUtils.doGet(
                "https://api.weixin.qq.com/cgi-bin/user/info", params);
        if (!httpResult.isOK()) {
            return BizResult.succ(Boolean.FALSE);
        }
        try {
            JSONObject followResult = new JSONObject(httpResult.getContent());
            Integer isFollow = followResult.getInt("subscribe");
            return BizResult.succ(Objects.equals(1, isFollow));
        } catch (Exception e) {
            return BizResult.succ(Boolean.FALSE);
        }
    }

    private ShareRecord getUrl(HttpServletRequest request, String shareUrl, String productId) {
        try {
            String memberId = agentComponent.getCurrentXgjAgentId(request);

            ShareDto shareDto = new ShareDto();
            shareDto.setUrl(shareUrl).setPrimaryId(memberId).setModelId(productId).setShareRole(ShareRole.XGJ);
            BizResult<ShareRecord> recordBizResult = shareService.buildShare(shareDto);
            if (recordBizResult.isSucc()) {
                return recordBizResult.getResult();
            } else {
                logger.error("获取qr0失败: " + recordBizResult.getMsg());
            }
        } catch (Exception e) {
            // logger.debug("获取分享链接错误",e);
        }

        return null;
    }

    /**
     * 日志打印h5的accessToken，前端不要调用
     *
     * @param
     * @return
     */
    @GetMapping("outputH5AccessToen")
    public BizResult getH5AccessToken(@RequestParam String pwd) {
        if (!"fuiabigbaw5753243".equals(pwd)) {
            return BizResult.FAIL;
        }

        logger.info("h5AccessToken:" + weiXinShareUtil.getAccessToken());
        return BizResult.SUCCESS;
    }

    /**
     * 日志打印小程序的accessToken，前端不要调用
     *
     * @param
     * @return
     */
    @GetMapping("outputMpAccessToen")
    public BizResult getMpAccessToken(@RequestParam String pwd) {
        if (!"fuiabigbaw5753243".equals(pwd)) {
            return BizResult.FAIL;
        }

        logger.info("mpAccessToken:" + weiXinMpShareUtil.getAccessToken());
        return BizResult.SUCCESS;
    }
}
