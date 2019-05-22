package com.fimi.widget.sticklistview;

public class BaseSortModel {
    private String pinyin;
    private String sortLetter;

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSortLetter() {
        return this.sortLetter;
    }

    public void setSortLetter(String sortLetters) {
        this.sortLetter = sortLetters;
    }
}
