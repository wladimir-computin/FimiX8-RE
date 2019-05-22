package com.fimi.network.oauth2;

public class OAuthClientRequest {
    private String mClientId;
    private String mClientSecret;
    private String mCode;
    private String mGrantType;
    private String mRedirectURI;
    private String mResponseType;

    public String getCode() {
        return this.mCode;
    }

    public void setClientId(String clientId) {
        this.mClientId = clientId;
    }

    public void setResponseType(String responseType) {
        this.mResponseType = responseType;
    }

    public void setRedirectURI(String redirectURI) {
        this.mRedirectURI = redirectURI;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public String getResponseType() {
        return this.mResponseType;
    }

    public String getRedirectURI() {
        return this.mRedirectURI;
    }

    public void setClientSecret(String clientSecret) {
        this.mClientSecret = clientSecret;
    }

    public void setGrantType(String grantType) {
        this.mGrantType = grantType;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public String getClientSecret() {
        return this.mClientSecret;
    }

    public String getGrantType() {
        return this.mGrantType;
    }
}
