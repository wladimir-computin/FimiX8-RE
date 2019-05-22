package com.fimi.network.entity;

import com.alibaba.fastjson.JSON;

public class ThirdAcountDto extends BaseModel {
    private String country;
    private String email;
    private String loginChannel;
    private String name;
    private String nickName;
    private String note;
    private String passWord;
    private String phone;
    private String sex;
    private String signature;
    private String status;
    private String thirdId;
    private String userImgUrl;

    public String getThirdId() {
        return this.thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserImgUrl() {
        return this.userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginChannel() {
        return this.loginChannel;
    }

    public void setLoginChannel(String loginChannel) {
        this.loginChannel = loginChannel;
    }

    public static void main(String[] args) {
        ThirdAcountDto t = new ThirdAcountDto();
        t.setCountry("cn");
        t.setEmail("moweiru@fimi.cn");
        t.setCountry("cn");
        t.setName("test");
        t.setNickName("test");
        t.setNote("备注");
        t.setPhone("15013027566");
        t.setSex("0");
        t.setSignature("个性签名");
        t.setStatus("1");
        t.setUserImgUrl("用户头像URL");
        t.setLoginChannel("1");
        System.out.println(JSON.toJSON(t));
    }
}
