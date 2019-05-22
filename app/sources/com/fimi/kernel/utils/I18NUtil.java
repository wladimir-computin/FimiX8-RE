package com.fimi.kernel.utils;

import com.fimi.kernel.GlobalConfig;
import com.fimi.network.entity.FwContenti18N;

public class I18NUtil {
    public static String getI18NStrin(FwContenti18N fwContenti18N) {
        String str = "";
        if ("zh_CN".equalsIgnoreCase(GlobalConfig.getInstance().getLanguageModel().getCountryLanguage())) {
            return fwContenti18N.getZh_CN();
        }
        if ("zh_TW".equalsIgnoreCase(GlobalConfig.getInstance().getLanguageModel().getCountryLanguage())) {
            return fwContenti18N.getZh_TW();
        }
        return fwContenti18N.getEn_US();
    }
}
