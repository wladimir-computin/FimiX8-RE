package com.fimi.kernel.language;

import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;
import java.io.Serializable;
import java.util.Locale;

public class LanguageModel implements Serializable {
    private String country;
    private String internalCoutry;
    private String languageCode;
    private int languageName;
    Locale locale;

    public LanguageModel(int languageName, Locale locale, String internalCoutry) {
        this.languageName = languageName;
        this.locale = locale;
        this.languageCode = locale.getLanguage();
        this.country = locale.getCountry();
        this.internalCoutry = internalCoutry;
    }

    public int getLanguageName() {
        return this.languageName;
    }

    public void setLanguageName(int languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getInternalCoutry() {
        return this.internalCoutry;
    }

    public String getCountryLanguage() {
        return this.locale.getLanguage() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + this.locale.getCountry();
    }

    public String getDefaultCountryLanguage() {
        return "en_US";
    }

    public void setInternalCoutry(String internalCoutry) {
        this.internalCoutry = internalCoutry;
    }
}
