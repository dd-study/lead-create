package com.agileboot.admin.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 解析微信报文工具
 *
 * @author ZhanWeiBin
 */
public class WxXmlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxXmlUtils.class);
    // 将 xml 文件解析为指定类型的实体对象。此方法只能解析简单的只有一层的xml
    private static DocumentBuilderFactory documentBuilderFactory = null;

    /**
     * 将输入流使用指定编码转化为字符串
     *
     * @param
     * @return
     */
    public static String inputStream2String(InputStream inputStream, String charset) throws Exception {
        // 建立输入流读取类
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        // 设定每次读取字符个数
        char[] data = new char[512];
        int dataSize = 0;
        // 循环读取
        StringBuilder stringBuilder = new StringBuilder();
        while ((dataSize = reader.read(data)) != -1) {
            stringBuilder.append(data, 0, dataSize);
        }
        return stringBuilder.toString();
    }

    public static WxMsg parseXml(String xml) throws Exception {
        if (StringUtils.isEmpty(xml)) {
            throw new RuntimeException("要解析的xml字符串不能为空。");
        }
        if (documentBuilderFactory == null) { // 文档解析器工厂初始
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
        }
        // 拿到一个文档解析器。
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        // 准备数据并解析。
        byte[] bytes = xml.getBytes("UTF-8");
        Document parsed = documentBuilder.parse(new ByteArrayInputStream(bytes));
        // 获取数据
        WxMsg obj = new WxMsg();
        Class<WxMsg> wxMsgClass = WxMsg.class;
        Element documentElement = parsed.getDocumentElement();
        NodeList childNodes = documentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            // 节点类型是 ELEMENT 才读取值
            // 进行此判断是因为如果xml不是一行，而是多行且有很好的格式的，就会产生一些文本的node，这些node内容只有换行符或空格
            // 所以排除这些换行符和空格。
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                String key = item.getNodeName();
                String value = item.getTextContent();

                Method declaredMethod = null;
                try {
                    // 拿到设置值的set方法。
                    declaredMethod = wxMsgClass.getDeclaredMethod("set" + key, String.class);
                } catch (NoSuchMethodException exception) {
                    LOGGER.info("微信xml的key:{}不存在对应的set方法", key);
                }
                if (null != declaredMethod) {
                    declaredMethod.setAccessible(true);
                    // 设置值
                    declaredMethod.invoke(obj, value);
                }
            }
        }
        return obj;
    }

    public static class WxMsg {

        private String Encrypt;
        private String ToUserName;     // 开发者微信号
        private String FromUserName;   // 发送方帐号（一个OpenID）
        private String CreateTime;     // 消息创建时间 （整型）
        private String MsgType;        // 消息类型，event
        private String MsgId;          // MsgId
        private String Event;          // 事件类型，subscribe
        private String EventKey;       // 事件KEY值，qrscene_为前缀，后面为二维码的参数值
        private String MenuId;         // 事件：按钮点击
        private String Content;        // 文本
        private String PicUrl;         // 图片
        private String MediaId;        // 图片 语音 视频 小视频
        private String Format;         // 语音
        private String Recognition;    // 语音
        private String ThumbMediaId;   // 视频 小视频
        private String Location_X;     // 地理位置
        private String Location_Y;     // 地理位置
        private String Scale;          // 地理位置
        private String Label;          // 地理位置
        private String Title;          // 链接
        private String Description;    // 链接
        private String URL;            // 链接
        private String Ticket;         // 事件：扫描参数二维码、关注公众号
        private String Latitude;       // 事件：上报地理位置
        private String Longitude;      // 事件：上报地理位置
        private String Precision;      // 事件：上报地理位置

        public String getEncrypt() {
            return Encrypt;
        }

        public WxMsg setEncrypt(String encrypt) {
            Encrypt = encrypt;
            return this;
        }

        public String getToUserName() {
            return ToUserName;
        }

        public WxMsg setToUserName(String toUserName) {
            ToUserName = toUserName;
            return this;
        }

        public String getFromUserName() {
            return FromUserName;
        }

        public WxMsg setFromUserName(String fromUserName) {
            FromUserName = fromUserName;
            return this;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public WxMsg setCreateTime(String createTime) {
            CreateTime = createTime;
            return this;
        }

        public String getMsgType() {
            return MsgType;
        }

        public WxMsg setMsgType(String msgType) {
            MsgType = msgType;
            return this;
        }

        public String getMsgId() {
            return MsgId;
        }

        public WxMsg setMsgId(String msgId) {
            MsgId = msgId;
            return this;
        }

        public String getEvent() {
            return Event;
        }

        public WxMsg setEvent(String event) {
            Event = event;
            return this;
        }

        public String getEventKey() {
            return EventKey;
        }

        public WxMsg setEventKey(String eventKey) {
            EventKey = eventKey;
            return this;
        }

        public String getMenuId() {
            return MenuId;
        }

        public WxMsg setMenuId(String menuId) {
            MenuId = menuId;
            return this;
        }

        public String getContent() {
            return Content;
        }

        public WxMsg setContent(String content) {
            Content = content;
            return this;
        }

        public String getPicUrl() {
            return PicUrl;
        }

        public WxMsg setPicUrl(String picUrl) {
            PicUrl = picUrl;
            return this;
        }

        public String getMediaId() {
            return MediaId;
        }

        public WxMsg setMediaId(String mediaId) {
            MediaId = mediaId;
            return this;
        }

        public String getFormat() {
            return Format;
        }

        public WxMsg setFormat(String format) {
            Format = format;
            return this;
        }

        public String getRecognition() {
            return Recognition;
        }

        public WxMsg setRecognition(String recognition) {
            Recognition = recognition;
            return this;
        }

        public String getThumbMediaId() {
            return ThumbMediaId;
        }

        public WxMsg setThumbMediaId(String thumbMediaId) {
            ThumbMediaId = thumbMediaId;
            return this;
        }

        public String getLocation_X() {
            return Location_X;
        }

        public WxMsg setLocation_X(String location_X) {
            Location_X = location_X;
            return this;
        }

        public String getLocation_Y() {
            return Location_Y;
        }

        public WxMsg setLocation_Y(String location_Y) {
            Location_Y = location_Y;
            return this;
        }

        public String getScale() {
            return Scale;
        }

        public WxMsg setScale(String scale) {
            Scale = scale;
            return this;
        }

        public String getLabel() {
            return Label;
        }

        public WxMsg setLabel(String label) {
            Label = label;
            return this;
        }

        public String getTitle() {
            return Title;
        }

        public WxMsg setTitle(String title) {
            Title = title;
            return this;
        }

        public String getDescription() {
            return Description;
        }

        public WxMsg setDescription(String description) {
            Description = description;
            return this;
        }

        public String getURL() {
            return URL;
        }

        public WxMsg setURL(String URL) {
            this.URL = URL;
            return this;
        }

        public String getTicket() {
            return Ticket;
        }

        public WxMsg setTicket(String ticket) {
            Ticket = ticket;
            return this;
        }

        public String getLatitude() {
            return Latitude;
        }

        public WxMsg setLatitude(String latitude) {
            Latitude = latitude;
            return this;
        }

        public String getLongitude() {
            return Longitude;
        }

        public WxMsg setLongitude(String longitude) {
            Longitude = longitude;
            return this;
        }

        public String getPrecision() {
            return Precision;
        }

        public WxMsg setPrecision(String precision) {
            Precision = precision;
            return this;
        }
    }

}
