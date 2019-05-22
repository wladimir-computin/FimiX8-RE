package com.fimi.kernel.utils;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
    public int compare(File o1, File o2) {
        if (o1 == null || o2 == null || o1.lastModified() == o2.lastModified()) {
            return 0;
        }
        return o1.lastModified() > o2.lastModified() ? -1 : 1;
    }
}
