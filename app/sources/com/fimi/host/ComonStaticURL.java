package com.fimi.host;

import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.FimiAppContext;
import com.fimi.kernel.GlobalConfig;
import com.fimi.kernel.R;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;

public class ComonStaticURL {
    private static String[] staticUrls = new String[]{"https://paas-beijing6-static-file.fimi.com/h5/", "https://fimiapp-web-us.mi-ae.com/h5/"};

    public static String getGuideBookUrl() {
        return getCommonFormateURL(R.string.kernal_gh2_guidebook);
    }

    public static String getFaqUrl() {
        return getCommonFormateURL(R.string.kernel_faq);
    }

    public static String getPrivacyUrl() {
        return getCommonFormateLocalURL(R.string.kernel_privacy);
    }

    public static String getPolicyUrl() {
        return getCommonFormateLocalURL(R.string.kernel_policy);
    }

    private static String getCommonFormateLocalURL(int resourceId) {
        StringBuffer stringBuffer = new StringBuffer();
        String url = FimiAppContext.getContext().getResources().getString(resourceId);
        ServiceItem serviceItem = (ServiceItem) SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
        String iCountry = "";
        if (serviceItem == null || serviceItem.getCountryCode().equals("")) {
            iCountry = "en";
        } else if (Constants.productType == ProductEnum.GH2) {
            if (serviceItem.getCountryCode().equalsIgnoreCase("en") || serviceItem.getCountryCode().equalsIgnoreCase("cn")) {
                iCountry = serviceItem.getCountryCode();
            } else {
                iCountry = "en";
            }
        } else if (Constants.productType == ProductEnum.FIMIAPP) {
            if (serviceItem.getCountryCode().equalsIgnoreCase("en")) {
                iCountry = serviceItem.getCountryCode();
            } else {
                iCountry = "en";
            }
        } else if (Constants.productType != ProductEnum.X8S) {
            iCountry = serviceItem.getCountryCode();
        } else if (serviceItem.getCountryCode().equalsIgnoreCase("en")) {
            iCountry = serviceItem.getCountryCode();
        } else {
            iCountry = "en";
        }
        stringBuffer.append(String.format(url, new Object[]{Constants.productType.toString().toLowerCase(), iCountry}));
        return stringBuffer.toString();
    }

    private static String getCommonFormateURL(int resourceId) {
        StringBuffer stringBuffer = new StringBuffer();
        if (((ServiceItem) SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class)).getInfo() == R.string.region_Mainland_China) {
            stringBuffer.append(staticUrls[0]);
        } else {
            stringBuffer.append(staticUrls[1]);
        }
        String url = FimiAppContext.getContext().getResources().getString(resourceId);
        String iCountry = GlobalConfig.getInstance().getLanguageModel().getInternalCoutry();
        stringBuffer.append(String.format(url, new Object[]{Constants.productType.toString().toLowerCase()}));
        stringBuffer.append("?language=");
        stringBuffer.append(iCountry);
        return stringBuffer.toString();
    }
}
