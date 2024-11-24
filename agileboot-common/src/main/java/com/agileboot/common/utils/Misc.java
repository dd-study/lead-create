package com.agileboot.common.utils;

import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Misc 工具类
 */
public class Misc {

    /**
     * 日志记录器
     */
    public static final org.slf4j.Logger Logger = LoggerFactory.getLogger(Misc.class);

    /**
     * UTF-16的最后一个字符
     */
    public static final char UNICODE_REPLACEMENT_CHAR = 0xfffd;
    /**
     * UTF-16的最后一个字符
     */
    public static final String UNICODE_REPLACEMENT_STRING = String.valueOf(UNICODE_REPLACEMENT_CHAR);

    /**
     * 用来将字节转换成 16 进制表示的字符表
     */
    public static final char _hexDigits[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * <p>
     * The maximum size to which the padding constant(s) can expand.
     * </p>
     */
    private static final int PAD_LIMIT = 8192;

    private Misc() {
    }

    /**
     * 比较两字串是否相同
     *
     * @param s1 字串1
     * @param s2 字串2
     * @return 相同则返回true
     */
    public static final boolean eq(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        return (null != s1 && s1.equals(s2));
    }

    /**
     * 判断字符是否为邮箱地址格式
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (Misc.isEmpty(str)) {
            return false;
        }
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,5}$";
        return str.matches(regex);
    }

    /**
     * 判断字符串是否为 "" || null
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return null == str || 0 == str.length();
    }

    /**
     * 判断集合是否为 空 null
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || 0 == collection.size();
    }

    /**
     * 判断数组是否为 空 null
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return null == array || 0 == array.length;
    }

    /**
     * 判断map 是否为 空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || 0 == map.size();
    }

    /**
     * 是否数字
     *
     * @param ch 字符
     * @return 是则返回true
     */
    public static final boolean isNumber(char ch) {
        return (ch > 0x30 - 1 && ch < 0x39 + 1);
    }

    public static final boolean isNumber(String str) {
        if (null == str || 0 == str.length()) {
            return false;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (!isNumber(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static final boolean isNumber(char[] chars, int offset, int count) {
        count += offset;
        for (int i = offset; i < count; i++) {
            if (!isNumber(chars[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String str = "sunny";
        String em = "suse33dq@q3qee.comdse";
        Logger.info("" + isEmail(em));
        Logger.info(toHex64((System.currentTimeMillis() - 946721219851L) / 4));
    }

    public static double toDouble(String str, double defaultDouble) {
        if (isEmpty(str)) {
            return defaultDouble;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            if (Logger.isDebugEnabled()) {
                Logger.debug("转换数据失败：" + str, e);
            }
            return defaultDouble;
        }
    }

    /**
     * 转换为16进制格式的字串
     *
     * @param val 32位数值
     * @return 16进制格式串（如：a1f）
     */
    public static String toHex(int val) {
        return toHex(val, new StringBuilder(8)).toString();
    }

    /**
     * 转为 HEX字串
     *
     * @param val 32位数值
     * @param sb  转换HEX后的追加字串缓冲区
     * @return 追加后的字串缓冲区
     */
    public static StringBuilder toHex(int val, StringBuilder sb) {
        if (val < 0 || val >= 0x10000000) {
            sb.append(_hexDigits[(val >> 28) & 0xF]);
            sb.append(_hexDigits[(val >> 24) & 0xF]);
            sb.append(_hexDigits[(val >> 20) & 0xF]);
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x01000000) {
            sb.append(_hexDigits[(val >> 24) & 0xF]);
            sb.append(_hexDigits[(val >> 20) & 0xF]);
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00100000) {
            sb.append(_hexDigits[(val >> 20) & 0xF]);
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00010000) {
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00001000) {
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00000100) {
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00000010) {
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00000001) {
            sb.append(_hexDigits[(val) & 0xF]);
        } else {
            sb.append("0");
            return sb;
        }
        return sb;
    }

    /**
     * 64位整数HEX字串，不足16个字符前端补0
     *
     * @param val 整数
     * @return hex格式串
     */
    public static String toHex64(long val) {
        if (0 == val) {
            return "0000000000000000";
        }
        return toHexFixed(val, new StringBuilder(16)).toString();
    }

    /**
     * 32位整数HEX字串，不足8个字符前端补0
     *
     * @param val 32位数字
     * @param sb  字串缓冲区，若为null自动创建新的
     * @return 8字符的HEX编码串
     */
    public static StringBuilder toHexFixed(int val, StringBuilder sb) {
        if (null == sb) {
            sb = new StringBuilder(8);
        }
        if (val < 0 || val >= 0x10000000) {
            sb.append(_hexDigits[(val >> 28) & 0xF]);
            sb.append(_hexDigits[(val >> 24) & 0xF]);
            sb.append(_hexDigits[(val >> 20) & 0xF]);
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x01000000) {
            sb.append('0');
            sb.append(_hexDigits[(val >> 24) & 0xF]);
            sb.append(_hexDigits[(val >> 20) & 0xF]);
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00100000) {
            sb.append("00");
            sb.append(_hexDigits[(val >> 20) & 0xF]);
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00010000) {
            sb.append("000");
            sb.append(_hexDigits[(val >> 16) & 0xF]);
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00001000) {
            sb.append("0000");
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00000100) {
            sb.append("00000");
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00000010) {
            sb.append("000000");
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[(val) & 0xF]);
        } else if (val >= 0x00000001) {
            sb.append("0000000");
            sb.append(_hexDigits[(val) & 0xF]);
        } else {
            sb.append("00000000");
            return sb;
        }
        return sb;
    }

    /**
     * 64位整数HEX字串，不足16个字符前端补0
     *
     * @param val 64位数值
     * @param sb  字串缓冲区，若为null自动创建新的
     * @return 16个字符的HEX编码串
     */
    public static StringBuilder toHexFixed(long val, StringBuilder sb) {
        if (null == sb) {
            sb = new StringBuilder(16);
        }
        // 高32位
        int i32 = (int) ((val >> 32) & 0xFFFFFFFF);
        toHexFixed(i32, sb);
        // 低32位
        i32 = (int) (val & 0xFFFFFFFF);
        toHexFixed(i32, sb);
        return sb;
    }

    /**
     * 64位整数HEX字串，不足12个字符前端补0
     *
     * @param val 64位数值
     * @param sb
     * @return 12个字符的HEX编码串
     */
    public static StringBuilder toHexFixed12(long val, StringBuilder sb) {
        if (null == sb) {
            sb = new StringBuilder(16);
        }
        // 高32位 只保留4位
        short short4 = (short) ((val >> 32) & 0xFFFF);
        toHexFixed(short4, sb);
        // 低32位
        int i32 = (int) (val & 0xFFFFFFFF);
        toHexFixed(i32, sb);
        return sb;
    }

    /**
     * 16位整数HEX字串，不足4个字符前端补0
     *
     * @param val
     * @param sb
     * @return
     */
    public static StringBuilder toHexFixed(short val, StringBuilder sb) {
        if (null == sb) {
            sb = new StringBuilder(4);
        }

        if (val < 0 || val >= 0x1000) {
            sb.append(_hexDigits[(val >> 12) & 0xF]);
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[val & 0x0F]);
        } else if (val >= 0x0100) {
            sb.append('0');
            sb.append(_hexDigits[(val >> 8) & 0xF]);
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[val & 0x0F]);
        } else if (val >= 0x0010) {
            sb.append("00");
            sb.append(_hexDigits[(val >> 4) & 0xF]);
            sb.append(_hexDigits[val & 0x0F]);
        } else if (val >= 0x0001) {
            sb.append("000");
            sb.append(_hexDigits[val & 0x0F]);
        } else {
            sb.append("0000");
            return sb;
        }
        return sb;
    }

    public static int toInt(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    /**
     * 字符串转成数字
     *
     * @param str
     * @param defaultInt 默认值
     * @return
     */
    public static int toInt(String str, int defaultInt) {
        // 如果字串为空时，返回defaultValue
        if (null == str || 0 == str.length()) {
            return defaultInt;
        }

        try {
            char first = str.charAt(0);
            if ('0' == first && str.length() > 2) {
                // 如果是以0x开首表示是十六进制
                first = str.charAt(1);
                if ('x' == first || 'X' == first) {
                    return Integer.parseInt(str.substring(2), 16);
                }
            } else if (str.length() > 1 && ('x' == first || 'X' == first)) {
                // x开首以十六进制
                return Integer.parseInt(str.substring(1), 16);
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            if (Logger.isDebugEnabled()) {
                Logger.debug("数字转换失败:" + str, e);
            }
        }
        return defaultInt;
    }

    public static String toString(Object obj) {
        if (null == obj) {
            return "";
        }
        return obj.toString();
    }

    public static String toString(String str) {
        return null == str ? "" : str;
    }

    public static String toString(String str, String defaultStr) {
        return Misc.isEmpty(str) ? defaultStr : str;
    }

    /**
     * <p>
     * Right pad a String with a specified String.
     * </p>
     * <p>
     * The String is padded to the size of <code>size</code>.
     * </p>
     *
     * <pre>
     * StringUtils.rightPad(null, *, *)      = null
     * StringUtils.rightPad("", 3, "z")      = "zzz"
     * StringUtils.rightPad("bat", 3, "yz")  = "bat"
     * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
     * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StringUtils.rightPad("bat", 1, "yz")  = "bat"
     * StringUtils.rightPad("bat", -1, "yz") = "bat"
     * StringUtils.rightPad("bat", 5, null)  = "bat  "
     * StringUtils.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return right padded String or original String if no padding is necessary, <code>null</code> if null String input
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * <p>
     * Right pad a String with a specified character.
     * </p>
     * <p>
     * The String is padded to the size of <code>size</code>.
     * </p>
     *
     * <pre>
     * StringUtils.rightPad(null, *, *)     = null
     * StringUtils.rightPad("", 3, 'z')     = "zzz"
     * StringUtils.rightPad("bat", 3, 'z')  = "bat"
     * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
     * StringUtils.rightPad("bat", 1, 'z')  = "bat"
     * StringUtils.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return right padded String or original String if no padding is necessary, <code>null</code> if null String input
     * @since 2.0
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }

    /**
     * <p>
     * Returns padding using the specified delimiter repeated to a given length.
     * </p>
     *
     * <pre>
     * StringUtils.padding(0, 'e')  = ""
     * StringUtils.padding(3, 'e')  = "eee"
     * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
     * </pre>
     * <p>
     * Note: this method doesn't not support padding with
     * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a> as they
     * require a pair of <code>char</code>s to be represented. If you are needing to support full I18N of your
     * applications consider using {@link #repeat(String, int)} instead.
     * </p>
     *
     * @param repeat  number of times to repeat delim
     * @param padChar character to repeat
     * @return String with repeated character
     * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
     * @see #repeat(String, int)
     */
    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    /**
     * 对文本进行XML编码
     *
     * @param text   要处理的文本串
     * @param buffer 输出的字串缓冲区
     * @deprecated 使用encodeXml代替
     */
    static public void escapeXML(String text, StringBuilder buffer) {
        encodeXml(text, buffer);
    }

    /**
     * 对文本进行XML编码
     *
     * @param text   要处理的文本串
     * @param buffer 输出的字串缓冲区
     */
    static public StringBuilder encodeXml(String text, StringBuilder buffer) {
        if (null == text || 0 == text.length()) {
            return buffer;
        }
        for (int i = 0; i < text.length(); i++) {
            int ch = text.charAt(i);
            if ((ch >= 0 && ch < 0x20) || (ch >= 0x7F && ch <= 0xFF)) {
                buffer.append("&#")
                        .append(ch)
                        .append(';');
                continue;
            }

            switch (ch) {
                /*
                 * case 0x7E: case 0x21: case 0x23: case 0x24: case 0x25: case 0x28:
                 * case 0x29: case 0x2B: case 0x7C: case 0x7B: case 0x7D: case 0x39:
                 * case 0x3A: case 0x3F: case 0x60: case 0x3D: case 0x5C: case 0x5B:
                 * case 0x5D: case 0x3B: case 0x27: case 0x2C: case 0x2F: xml +=
                 * "&#"; ///String hex=Integer.toHexString(ch); ///前面补够4位
                 * ///if(1==hex.length()) xml+="000"; ///else xml+="00"; ///xml +=
                 * hex; xml+=String.valueOf(ch); xml += ';'; break;
                 */
                case 0x3e: // >
                    buffer.append("&gt;");
                    break;
                case 0x3c: // <
                    buffer.append("&lt;");
                    break;
                case 0x22: // "
                    buffer.append("&quot;");
                    break;
                case 0x26:
                    buffer.append("&amp;");
                    break;
                default:
                    buffer.append(text.charAt(i));
                    break;
            }
        }
        return buffer;
    }

    /**
     * 对文本进行XML编码
     *
     * @param text 要编码的字串
     * @return XML编码后的字串
     */
    static public String encodeXml(String text) {
        if (null == text || 0 == text.length()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(text.length() * 2);
        encodeXml(text, buffer);
        return buffer.toString();
    }

    /**
     * 对文本进行XML编码
     *
     * @param text 要编码的字串
     * @return XML编码后的字串
     * @deprecated 使用encodeXml代替
     */
    static public String escapeXML(String text) {
        return encodeXml(text);
    }

}

