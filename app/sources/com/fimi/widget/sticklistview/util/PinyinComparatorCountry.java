package com.fimi.widget.sticklistview.util;

import java.util.Comparator;

public class PinyinComparatorCountry implements Comparator<CountryCodeEntity> {
    public int compare(CountryCodeEntity o1, CountryCodeEntity o2) {
        if (o1.getSortLetter().equals("@") || o2.getSortLetter().equals("#")) {
            return -1;
        }
        if (o1.getSortLetter().equals("#") || o2.getSortLetter().equals("@")) {
            return 1;
        }
        return o1.getSortLetter().compareTo(o2.getSortLetter());
    }
}
