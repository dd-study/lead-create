package com.agileboot.admin.utils;

/**
 * 微信签名实体
 */
public class Signature {

    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;

    public Signature() {
    }

    public Signature(String signature, String timestamp, String nonce, String echostr) {
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.echostr = echostr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }

    @Override
    public String toString() {
        return "Signature [signature=" + signature + ", timestamp=" + timestamp + ", nonce=" + nonce + ", echostr="
                + echostr + "]";
    }
}