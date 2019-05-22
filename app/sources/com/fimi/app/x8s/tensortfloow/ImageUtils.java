package com.fimi.app.x8s.tensortfloow;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils {
    static final int kMaxChannelValue = 262143;
    private static boolean useNativeConversion = true;

    private static native void convertARGB8888ToYUV420SP(int[] iArr, byte[] bArr, int i, int i2);

    private static native void convertRGB565ToYUV420SP(byte[] bArr, byte[] bArr2, int i, int i2);

    private static native void convertYUV420SPToARGB8888(byte[] bArr, int[] iArr, int i, int i2, boolean z);

    private static native void convertYUV420SPToRGB565(byte[] bArr, byte[] bArr2, int i, int i2);

    private static native void convertYUV420ToARGB8888(byte[] bArr, byte[] bArr2, byte[] bArr3, int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z);

    static {
        try {
            System.loadLibrary("tensorflow_demo");
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public static int getYUVByteSize(int width, int height) {
        return (width * height) + ((((width + 1) / 2) * ((height + 1) / 2)) * 2);
    }

    public static void saveBitmap(Bitmap bitmap) {
        saveBitmap(bitmap, "preview.png");
    }

    public static void saveBitmap(Bitmap bitmap, String filename) {
        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "tensorflow");
        if (!myDir.mkdirs()) {
        }
        File file = new File(myDir, filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 99, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public static void convertYUV420SPToARGB8888(byte[] input, int width, int height, int[] output) {
        if (useNativeConversion) {
            try {
                convertYUV420SPToARGB8888(input, output, width, height, false);
                return;
            } catch (UnsatisfiedLinkError e) {
                useNativeConversion = false;
            }
        }
        int frameSize = width * height;
        int yp = 0;
        for (int j = 0; j < height; j++) {
            int u = 0;
            int v = 0;
            int i = 0;
            int uvp = frameSize + ((j >> 1) * width);
            while (i < width) {
                int uvp2;
                int y = input[yp] & 255;
                if ((i & 1) == 0) {
                    uvp2 = uvp + 1;
                    v = input[uvp] & 255;
                    uvp = uvp2 + 1;
                    u = input[uvp2] & 255;
                }
                uvp2 = uvp;
                output[yp] = YUV2RGB(y, u, v);
                i++;
                yp++;
                uvp = uvp2;
            }
        }
    }

    private static int YUV2RGB(int y, int u, int v) {
        u -= 128;
        v -= 128;
        int y1192 = (y + -16 < 0 ? 0 : y - 16) * 1192;
        int r = y1192 + (v * 1634);
        int g = (y1192 - (v * 833)) - (u * 400);
        int b = y1192 + (u * 2066);
        if (r > kMaxChannelValue) {
            r = kMaxChannelValue;
        } else if (r < 0) {
            r = 0;
        }
        if (g > kMaxChannelValue) {
            g = kMaxChannelValue;
        } else if (g < 0) {
            g = 0;
        }
        if (b > kMaxChannelValue) {
            b = kMaxChannelValue;
        } else if (b < 0) {
            b = 0;
        }
        return ((ViewCompat.MEASURED_STATE_MASK | ((r << 6) & 16711680)) | ((g >> 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | ((b >> 10) & 255);
    }

    public static void convertYUV420ToARGB8888(byte[] yData, byte[] uData, byte[] vData, int width, int height, int yRowStride, int uvRowStride, int uvPixelStride, int[] out) {
        if (useNativeConversion) {
            try {
                convertYUV420ToARGB8888(yData, uData, vData, out, width, height, yRowStride, uvRowStride, uvPixelStride, false);
                return;
            } catch (UnsatisfiedLinkError e) {
                useNativeConversion = false;
            }
        }
        int yp = 0;
        int j = 0;
        while (j < height) {
            int yp2;
            int pY = yRowStride * j;
            int pUV = uvRowStride * (j >> 1);
            int i = 0;
            while (true) {
                yp2 = yp;
                if (i >= width) {
                    break;
                }
                int uv_offset = pUV + ((i >> 1) * uvPixelStride);
                yp = yp2 + 1;
                out[yp2] = YUV2RGB(yData[pY + i] & 255, uData[uv_offset] & 255, vData[uv_offset] & 255);
                i++;
            }
            j++;
            yp = yp2;
        }
    }

    public static Matrix getTransformationMatrix(int srcWidth, int srcHeight, int dstWidth, int dstHeight, int applyRotation, boolean maintainAspectRatio) {
        int inWidth;
        int inHeight;
        Matrix matrix = new Matrix();
        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
            }
            matrix.postTranslate(((float) (-srcWidth)) / 2.0f, ((float) (-srcHeight)) / 2.0f);
            matrix.postRotate((float) applyRotation);
        }
        boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
        if (transpose) {
            inWidth = srcHeight;
        } else {
            inWidth = srcWidth;
        }
        if (transpose) {
            inHeight = srcWidth;
        } else {
            inHeight = srcHeight;
        }
        if (!(inWidth == dstWidth && inHeight == dstHeight)) {
            float scaleFactorX = ((float) dstWidth) / ((float) inWidth);
            float scaleFactorY = ((float) dstHeight) / ((float) inHeight);
            if (maintainAspectRatio) {
                float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }
        if (applyRotation != 0) {
            matrix.postTranslate(((float) dstWidth) / 2.0f, ((float) dstHeight) / 2.0f);
        }
        return matrix;
    }
}
