package com.fimi.kernel.network.okhttp;

import com.fimi.thirdpartysdk.ThirdPartyConstants;
import com.umeng.commonsdk.proguard.g;

public class HttpConstant {
    public static final String AD_DATA_FAILED = "202";
    public static final String AD_DATA_SUCCESS = "200";
    public static final String AD_PLAY_FAILED = "301";
    public static final String AD_PLAY_SUCCESS = "300";
    public static final String AVS = "avs";
    public static final String IE = "ie";
    public static final String MD5_PASSWORD = "fimi123??686";
    public static final String SID = "sid";
    public static final String STEP_CD = "cd";

    public enum Params {
        lvs("lvs", "4"),
        st("st", "12"),
        bt_phone("bt", "1"),
        bt_pad("bt", "0"),
        os(g.w, "1"),
        p(g.ao, "2"),
        appid("appid", "xya"),
        ad_analize("sp", "2"),
        ad_load("sp", ThirdPartyConstants.LOGIN_CHANNEL_TW);
        
        private String key;
        private String value;

        private Params(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String getKey() {
            return this.key;
        }
    }
}
