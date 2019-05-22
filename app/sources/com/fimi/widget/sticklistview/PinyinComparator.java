package com.fimi.widget.sticklistview;

import java.util.Comparator;

public class PinyinComparator implements Comparator<SortModel> {
    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetter().equals("@") || o2.getSortLetter().equals("#")) {
            return -1;
        }
        if (o1.getSortLetter().equals("#") || o2.getSortLetter().equals("@")) {
            return 1;
        }
        return o1.getSortLetter().compareTo(o2.getSortLetter());
    }
}
