package com.fimi.kernel.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtilByte {
    public static byte[] getbyte(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4048];
        while (true) {
            try {
                int length = in.read(buffer);
                if (length == -1) {
                    break;
                }
                out.write(buffer, 0, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        out.flush();
        out.close();
        return out.toByteArray();
    }
}
