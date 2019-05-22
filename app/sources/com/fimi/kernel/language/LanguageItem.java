package com.fimi.kernel.language;

import com.fimi.kernel.R;
import java.util.Locale;

public class LanguageItem {
    public static final LanguageModel defaultLanguage = new LanguageModel(R.string.kernal_english, Locale.US, "en");
    public static final LanguageModel[] languageModels = new LanguageModel[]{new LanguageModel(R.string.kernal_simplified_chinese, Locale.SIMPLIFIED_CHINESE, "cn"), new LanguageModel(R.string.kernal_english, Locale.US, "en")};
    private String code;
    private int info;
    private boolean isSelect;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getInfo() {
        return this.info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }
}
