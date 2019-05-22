package com.fimi.x8sdk.entity;

import android.os.Environment;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class ErrorTransBean {
    public static final int X8_LANGUAGE_CHINESE = 0;
    public static final int X8_LANGUAGE_ENGLISH = 1;
    private static ErrorTransBean instance = null;
    private final String X8_CHINESE_TRANS_FILE_PATH = (this.X8_ROOT_PATH + "cn.txt");
    private final String X8_ENGLISH_TRANS_FILE_PATH = (this.X8_ROOT_PATH + "en.txt");
    private final String X8_ROOT_PATH = (Environment.getExternalStorageDirectory() + File.separator);
    private HashMap<Integer, String> chineseMap = new HashMap();
    private HashMap<Integer, String> englishMap = new HashMap();

    private ErrorTransBean() {
    }

    public static ErrorTransBean getInstance() {
        if (instance == null) {
            synchronized (ErrorTransBean.class) {
                if (instance == null) {
                    ErrorTransBean errorTransBean = new ErrorTransBean();
                    instance = errorTransBean;
                    return errorTransBean;
                }
            }
        }
        return instance;
    }

    public void parse() {
        parseFileToMap(this.X8_CHINESE_TRANS_FILE_PATH, this.chineseMap);
        parseFileToMap(this.X8_ENGLISH_TRANS_FILE_PATH, this.englishMap);
    }

    public String getString(int language, int code) {
        switch (language) {
            case 0:
                return (String) this.chineseMap.get(Integer.valueOf(code));
            case 1:
                return (String) this.englishMap.get(Integer.valueOf(code));
            default:
                return null;
        }
    }

    private void parseFileToMap(String filePath, HashMap map) {
        try {
            if (new File(filePath).exists()) {
                BufferedReader bf = new BufferedReader(new FileReader(filePath));
                while (true) {
                    String s = bf.readLine();
                    if (s != null) {
                        String[] result = s.split("\"");
                        if (result.length >= 3) {
                            int index = stringToInt(result[1]);
                            if (index != -1) {
                                map.put(Integer.valueOf(index), result[3]);
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private int stringToInt(String str) {
        int i = -1;
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        try {
            return Integer.valueOf(str.trim()).intValue();
        } catch (Exception e) {
            return i;
        }
    }
}
