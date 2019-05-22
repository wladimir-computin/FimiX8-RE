package com.fimi.kernel.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.LocaleList;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import com.fimi.kernel.Constants;
import com.fimi.kernel.GlobalConfig;
import com.fimi.kernel.language.ConstantLanguages;
import com.fimi.kernel.language.LanguageItem;
import com.fimi.kernel.language.LanguageModel;
import com.fimi.kernel.store.shared.SPStoreManager;
import java.util.Locale;

public class LanguageUtil {
    public static final String[] ZH_SERVER = new String[]{"CN", ConstantLanguages.REGION_TW, "HK"};

    @RequiresApi(api = 24)
    public static void changeAppLanguage(Context context, Locale setLocale) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getLocaleByLanguage(setLocale);
        config.locale = locale;
        if (VERSION.SDK_INT >= 24) {
            LocaleList localeList = new LocaleList(new Locale[]{locale});
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }

    public static Locale getLocaleByLanguage(Locale locale) {
        if (isSupportLanguage(locale)) {
            return getLanguageModel(locale).getLocale();
        }
        return LanguageItem.defaultLanguage.getLocale();
    }

    public static LanguageModel getLanguageModel(Locale locale) {
        LanguageModel model = null;
        for (LanguageModel languageModel : LanguageItem.languageModels) {
            if (languageModel.getLanguageCode().equals(locale.getLanguage()) && languageModel.getCountry().equals(locale.getCountry())) {
                model = languageModel;
            }
        }
        if (model == null) {
            return LanguageItem.defaultLanguage;
        }
        return model;
    }

    private static boolean isSupportLanguage(Locale locale) {
        boolean isSupport = false;
        for (LanguageModel languageModel : LanguageItem.languageModels) {
            if (languageModel.getLanguageCode().equals(locale.getLanguage()) && languageModel.getCountry().equals(locale.getCountry())) {
                isSupport = true;
            }
        }
        return isSupport;
    }

    public static Context attachBaseContext(Context context) {
        return attachBaseContext(context, GlobalConfig.getInstance().getLanguageModel().getLocale());
    }

    public static Context attachBaseContext(Context context, Locale locale) {
        return updateResources(context, locale);
    }

    @TargetApi(24)
    private static Context updateResources(Context context, Locale locale2) {
        Resources resources = context.getResources();
        Locale locale = getLocaleByLanguage(locale2);
        Configuration configuration = resources.getConfiguration();
        if (VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(new Locale[]{locale}));
        } else {
            configuration.setLocale(locale);
        }
        return context.createConfigurationContext(configuration);
    }

    public static LanguageModel getCurrentLanguage() {
        LanguageModel model = (LanguageModel) SPStoreManager.getInstance().getObject(Constants.LANGUAGETYPE, LanguageModel.class);
        if (model == null) {
            return getLanguageModel(Locale.getDefault());
        }
        return model;
    }

    public static boolean isZh() {
        LanguageModel model = GlobalConfig.getInstance().getLanguageModel();
        return model.getCountry().equals(Locale.SIMPLIFIED_CHINESE.getCountry()) || model.getCountry().equals(Locale.TAIWAN.getCountry());
    }
}
