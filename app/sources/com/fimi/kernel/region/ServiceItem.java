package com.fimi.kernel.region;

import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.R;
import java.io.Serializable;

public class ServiceItem implements Serializable {
    public static final int[] SERVICECODE = new int[]{1, 2, 3, 4, 5, 6, 7};
    private static final int[] SERVICENAME = new int[]{R.string.region_Argentina, R.string.region_United_Arab_Emirates, R.string.region_Poland, R.string.region_germany, R.string.region_Russia, R.string.region_France, R.string.region_Philippines, R.string.region_Malaysia, R.string.region_The_US, R.string.region_Burma, R.string.region_Mexico, R.string.region_Saudi_Arabia, R.string.region_Slovakia, R.string.region_Thailand, R.string.region_Turkey, R.string.region_Ukraine, R.string.region_Spain, R.string.region_hk, R.string.region_Singapore, R.string.region_Iran, R.string.region_italy, R.string.region_Indonesia, R.string.region_Vietnam, R.string.region_Mainland_China};
    private static final int[] SERVICENAME_GH2 = new int[]{R.string.region_Argentina, R.string.region_United_Arab_Emirates, R.string.region_egpyt, R.string.region_pakistan, R.string.region_belarus, R.string.region_Poland, R.string.region_germany, R.string.region_Russia, R.string.region_France, R.string.region_Philippines, R.string.region_colombia, R.string.region_korea, R.string.region_Malaysia, R.string.region_The_US, R.string.region_maroc, R.string.region_Mexico, R.string.region_portugal, R.string.region_japan, R.string.region_Saudi_Arabia, R.string.region_Thailand, R.string.region_Turkey, R.string.region_Ukraine, R.string.region_Spain, R.string.region_greco, R.string.region_hk, R.string.region_Singapore, R.string.region_Iran, R.string.region_israel, R.string.region_italy, R.string.region_Indonesia, R.string.region_uk, R.string.region_Vietnam, R.string.region_Mainland_China};
    private static final int[] SERVICENAME_X8 = new int[]{R.string.region_Argentina, R.string.region_United_Arab_Emirates, R.string.region_egpyt, R.string.region_pakistan, R.string.region_belarus, R.string.region_Poland, R.string.region_germany, R.string.region_Russia, R.string.region_France, R.string.region_Philippines, R.string.region_colombia, R.string.region_korea, R.string.region_Malaysia, R.string.region_The_US, R.string.region_maroc, R.string.region_Burma, R.string.region_Mexico, R.string.region_portugal, R.string.region_japan, R.string.region_Saudi_Arabia, R.string.region_Slovakia, R.string.region_Thailand, R.string.region_Turkey, R.string.region_Ukraine, R.string.region_Spain, R.string.region_greco, R.string.region_hk, R.string.region_Singapore, R.string.region_Iran, R.string.region_israel, R.string.region_italy, R.string.region_Indonesia, R.string.region_uk, R.string.region_Vietnam, R.string.region_Mainland_China, R.string.region_other};
    public static final int[] THE_EUROPE_SERVICE = new int[]{R.string.region_Spain, R.string.region_Poland, R.string.region_Ukraine, R.string.region_Slovakia, R.string.region_France, R.string.region_Turkey, R.string.region_Europe};
    public static final int[] THE_SINGAPORE_SERVICE = new int[]{R.string.region_Indonesia, R.string.region_Malaysia, R.string.region_Burma, R.string.region_Thailand, R.string.region_Vietnam, R.string.region_Singapore, R.string.region_Philippines, R.string.region_taiwan};
    public static final int[] THE_US_SERVICE = new int[]{R.string.region_United_Arab_Emirates, R.string.region_Saudi_Arabia, R.string.region_Iran, R.string.region_Argentina, R.string.region_Mexico, R.string.region_The_US};
    public static final String chinaService = "https://paas-beijing6.fimi.com/";
    public static final String frankfurtService = "https://fimiapp-server-frankfurt.mi-ae.com.de/";
    public static final String moscowService = "https://paas-moscow.fimi.com/fimi-cms-web-interface/";
    public static final String newusService = "https://fimiservice-newus.mi-ae.com/";
    public static final String singaporeService = "https://paas-singapore.fimi.com/fimi-cms-web-interface/";
    private int code;
    private String countryCode;
    private int info;
    private boolean isSelect;
    private String serviceUrl;

    public static int[] getServicename() {
        if (Constants.productType == ProductEnum.GH2) {
            return SERVICENAME_GH2;
        }
        if (Constants.productType == ProductEnum.X9) {
            return SERVICENAME;
        }
        return SERVICENAME_X8;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public int getInfo() {
        return this.info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }
}
