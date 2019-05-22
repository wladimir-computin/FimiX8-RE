package com.fimi.network.entity;

public class RestPswDto extends BaseModel {
    private String checkPsw;
    private String code;
    private String confirmPassword;
    private String email;
    private String password;
    private String phone;

    public String getCheckPsw() {
        return this.checkPsw;
    }

    public void setCheckPsw(String checkPsw) {
        this.checkPsw = checkPsw;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
