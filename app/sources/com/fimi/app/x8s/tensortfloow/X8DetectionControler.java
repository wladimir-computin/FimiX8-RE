package com.fimi.app.x8s.tensortfloow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.fimi.app.x8s.media.FimiH264Video;
import com.fimi.app.x8s.media.OnX8VideoFrameBufferListener;
import com.fimi.app.x8s.widget.X8TrackOverlayView;
import com.fimi.kernel.FimiAppContext;
import java.io.IOException;
import java.nio.ByteBuffer;

public class X8DetectionControler implements OnX8VideoFrameBufferListener {
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.2f;
    private static final int TF_OD_API_INPUT_SIZE = 300;
    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/coco_labels_list.txt";
    private static final String TF_OD_API_MODEL_FILE = "detect_ssd_new.mp3";
    private int MAX_UNSIGNED_SHORT = 65535;
    private int SIZE_HEIGHT;
    private int SIZE_WIDTH;
    private boolean TF_OD_API_IS_QUANTIZED = true;
    private int VIDEO_HEIGHT = FimiAppContext.UI_WIDTH;
    private int VIDEO_WIDTH = FimiAppContext.UI_HEIGHT;
    private Bitmap bitmap;
    private Integer classfier;
    private Activity context;
    int cropSize = 300;
    private Matrix cropToFrameTransform;
    private Bitmap croppedBitmap = null;
    private Classifier detector;
    private Matrix frameToCropTransform;
    private boolean isFirst = true;
    private boolean isFirstWaiting;
    private boolean isLog = true;
    private boolean isRev;
    private boolean isTouch;
    private long lastProcessingTimeMs;
    private onDetectionListener listener;
    private FimiH264Video mFimiH264Video;
    private int mFrameHeight;
    private int mFrameWidth;
    private String objTitle = "";
    private X8TrackOverlayView overlay;
    private int rectH;
    private int rectW;
    private int rectX;
    private int rectY;
    private TestOverlay testoverlay;

    public interface onDetectionListener {
        void onDetectionFailed();

        void onDetectionResult(int i, int i2, int i3, int i4, int i5);
    }

    public void initView(Activity context, X8TrackOverlayView overlay, FimiH264Video mFimiH264Video, TestOverlay testoverlay, onDetectionListener listener) {
        this.context = context;
        this.overlay = overlay;
        this.testoverlay = testoverlay;
        this.listener = listener;
        overlay.setCustomOverlay(false);
        this.mFimiH264Video = mFimiH264Video;
        mFimiH264Video.setX8VideoFrameBufferListener(this);
        initTensortfloow();
        this.bitmap = Bitmap.createBitmap(this.VIDEO_WIDTH, this.VIDEO_HEIGHT, Config.ARGB_8888);
    }

    private void initTensortfloow() {
        new Thread() {
            public void run() {
                if (X8DetectionControler.this.detector == null) {
                    try {
                        X8DetectionControler.this.detector = TFLiteObjectDetectionAPIModel.create(X8DetectionControler.this.context.getAssets(), X8DetectionControler.TF_OD_API_MODEL_FILE, X8DetectionControler.TF_OD_API_LABELS_FILE, 300, X8DetectionControler.this.TF_OD_API_IS_QUANTIZED);
                    } catch (IOException e) {
                    }
                }
            }
        }.start();
    }

    public void onTouchActionUp(int x, int y, int w, int h, int x1, int y1, int x2, int y2) {
        if (w < 0 || h < 0) {
            this.overlay.cleanTrackerRect();
        } else {
            if (w < 25 || h < 25) {
                w = 0;
                h = 0;
            }
            this.rectX = x;
            this.rectY = y;
            this.rectW = w;
            this.rectH = h;
            this.mFrameWidth = this.VIDEO_WIDTH;
            this.mFrameHeight = this.VIDEO_HEIGHT;
        }
        if (this.detector != null && !this.isRev && !this.isFirstWaiting && !this.isTouch) {
            this.isRev = true;
            this.isTouch = true;
            this.mFimiH264Video.setEnableCallback(1);
        }
    }

    public void onTouchActionDown() {
    }

    public void onFrameBuffer(byte[] rgb) {
        if (this.isRev && this.isTouch) {
            this.bitmap.setPixels(H264ToBitmap.convertByteToColor(rgb), 0, this.VIDEO_WIDTH, 0, 0, this.VIDEO_WIDTH, this.VIDEO_HEIGHT);
            this.isRev = false;
            this.mFimiH264Video.setEnableCallback(0);
            runThread();
        }
    }

    public void onH264Frame(ByteBuffer buffer) {
    }

    public void runThread() {
        if (this.isTouch && !this.isFirstWaiting) {
            this.isFirstWaiting = true;
            new Thread() {
                public void run() {
                    X8DetectionControler.this.runTf();
                }
            }.start();
        }
    }

    public Bitmap cropBitmap(Bitmap src, int x, int y, int width, int height, boolean isRecycle) {
        if (x == 0 && y == 0 && width == src.getWidth() && height == src.getHeight()) {
            return src;
        }
        Bitmap dst = Bitmap.createBitmap(src, x, y, width, height);
        if (isRecycle && dst != src) {
            src.recycle();
        }
        return dst;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean RectOverlap(RectF r, RectF rOther) {
        if (r.right <= rOther.left || rOther.right <= r.left || r.bottom <= rOther.top || rOther.bottom <= r.top) {
            return false;
        }
        return true;
    }

    public void onResult(float x1, float y1, float x2, float y2, boolean b, int classfier) {
        if (this.listener != null) {
            this.listener.onDetectionResult((int) (((1.0f * x1) / ((float) this.overlay.getMaxWidth())) * ((float) this.MAX_UNSIGNED_SHORT)), (int) (((1.0f * y1) / ((float) this.overlay.getMaxHeight())) * ((float) this.MAX_UNSIGNED_SHORT)), (int) ((((x2 - x1) * 1.0f) / ((float) this.overlay.getMaxWidth())) * ((float) this.MAX_UNSIGNED_SHORT)), (int) ((((y2 - y1) * 1.0f) / ((float) this.overlay.getMaxHeight())) * ((float) this.MAX_UNSIGNED_SHORT)), classfier);
        }
    }

    public void onResult2(float x1, float y1, float x2, float y2, boolean b, int classfier) {
        if (this.listener != null) {
            this.listener.onDetectionResult((int) (((1.0f * x1) / ((float) this.overlay.getMaxWidth())) * ((float) this.MAX_UNSIGNED_SHORT)), (int) (((1.0f * y1) / ((float) this.overlay.getMaxHeight())) * ((float) this.MAX_UNSIGNED_SHORT)), (int) (((1.0f * x2) / ((float) this.overlay.getMaxWidth())) * ((float) this.MAX_UNSIGNED_SHORT)), (int) (((1.0f * y2) / ((float) this.overlay.getMaxHeight())) * ((float) this.MAX_UNSIGNED_SHORT)), classfier);
        }
    }

    private float getScaleX(int f, int c) {
        if (f <= c) {
            int tmp = c;
            c = f;
            f = tmp;
        }
        return ((float) c) / ((float) f);
    }

    private float getScaleX1(int f, int c) {
        return ((float) c) / ((float) f);
    }

    public void runTf() {
        if (this.SIZE_WIDTH == 0) {
            this.SIZE_WIDTH = this.overlay.getMaxWidth();
            this.SIZE_HEIGHT = this.overlay.getMaxHeight();
        }
        tfLiteApiModel();
    }

    /* JADX WARNING: Removed duplicated region for block: B:90:0x0515  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0440  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x027a  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x02a2  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0440  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0515  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x027a  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x02a2  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0515  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0440  */
    public void tfLiteApiModel() {
        /*
        r51 = this;
        r0 = r51;
        r4 = r0.isTouch;
        if (r4 == 0) goto L_0x04f0;
    L_0x0006:
        r46 = 0;
        r49 = 0;
        r47 = 0;
        r50 = 0;
        r22 = 0;
        r0 = r51;
        r4 = r0.mFrameWidth;
        r0 = r51;
        r5 = r0.SIZE_WIDTH;
        r0 = r51;
        r35 = r0.getScaleX(r4, r5);
        r0 = r51;
        r4 = r0.mFrameHeight;
        r0 = r51;
        r5 = r0.SIZE_HEIGHT;
        r0 = r51;
        r36 = r0.getScaleX(r4, r5);
        r0 = r51;
        r4 = r0.rectH;
        if (r4 == 0) goto L_0x0038;
    L_0x0032:
        r0 = r51;
        r4 = r0.rectW;
        if (r4 != 0) goto L_0x00b2;
    L_0x0038:
        r22 = 1;
        r0 = r51;
        r4 = r0.rectX;
        r4 = (float) r4;
        r4 = r4 * r35;
        r4 = (int) r4;
        r0 = r51;
        r5 = r0.cropSize;
        r5 = r5 / 2;
        r46 = r4 - r5;
        r0 = r51;
        r4 = r0.rectY;
        r4 = (float) r4;
        r4 = r4 * r36;
        r4 = (int) r4;
        r0 = r51;
        r5 = r0.cropSize;
        r5 = r5 / 2;
        r49 = r4 - r5;
        r0 = r51;
        r4 = r0.rectX;
        r4 = (float) r4;
        r4 = r4 * r35;
        r4 = (int) r4;
        r0 = r51;
        r5 = r0.cropSize;
        r5 = r5 / 2;
        r47 = r4 + r5;
        r0 = r51;
        r4 = r0.rectY;
        r4 = (float) r4;
        r4 = r4 * r36;
        r4 = (int) r4;
        r0 = r51;
        r5 = r0.cropSize;
        r5 = r5 / 2;
        r50 = r4 + r5;
        if (r46 >= 0) goto L_0x0080;
    L_0x007c:
        r47 = r47 - r46;
        r46 = 0;
    L_0x0080:
        if (r49 >= 0) goto L_0x0086;
    L_0x0082:
        r50 = r50 - r49;
        r49 = 0;
    L_0x0086:
        r0 = r51;
        r4 = r0.mFrameWidth;
        r0 = r47;
        if (r0 <= r4) goto L_0x009c;
    L_0x008e:
        r0 = r51;
        r4 = r0.mFrameWidth;
        r4 = r47 - r4;
        r46 = r46 - r4;
        r0 = r51;
        r0 = r0.mFrameWidth;
        r47 = r0;
    L_0x009c:
        r0 = r51;
        r4 = r0.mFrameHeight;
        r0 = r50;
        if (r0 <= r4) goto L_0x00b2;
    L_0x00a4:
        r0 = r51;
        r4 = r0.mFrameHeight;
        r4 = r50 - r4;
        r49 = r49 - r4;
        r0 = r51;
        r0 = r0.mFrameHeight;
        r50 = r0;
    L_0x00b2:
        r41 = 0;
        r42 = 0;
        r40 = 0;
        r37 = 0;
        r45 = 0;
        r48 = 0;
        r44 = 0;
        r21 = 0;
        r17 = 0;
        r4 = 1;
        r0 = r22;
        if (r0 != r4) goto L_0x0553;
    L_0x00c9:
        r0 = r51;
        r4 = r0.croppedBitmap;
        if (r4 != 0) goto L_0x00e1;
    L_0x00cf:
        r0 = r51;
        r4 = r0.cropSize;
        r0 = r51;
        r5 = r0.cropSize;
        r6 = android.graphics.Bitmap.Config.ARGB_8888;
        r4 = android.graphics.Bitmap.createBitmap(r4, r5, r6);
        r0 = r51;
        r0.croppedBitmap = r4;
    L_0x00e1:
        r4 = r47 - r46;
        r5 = r50 - r49;
        r0 = r51;
        r6 = r0.cropSize;
        r0 = r51;
        r7 = r0.cropSize;
        r0 = r51;
        r8 = r0.context;
        r8 = r8.getRequestedOrientation();
        r9 = 0;
        r4 = com.fimi.app.x8s.tensortfloow.ImageUtils.getTransformationMatrix(r4, r5, r6, r7, r8, r9);
        r0 = r51;
        r0.frameToCropTransform = r4;
        r4 = new android.graphics.Matrix;
        r4.<init>();
        r0 = r51;
        r0.cropToFrameTransform = r4;
        r0 = r51;
        r4 = r0.frameToCropTransform;
        r0 = r51;
        r5 = r0.cropToFrameTransform;
        r4.invert(r5);
        r30 = new android.graphics.RectF;
        r0 = r51;
        r4 = r0.rectX;
        r4 = (float) r4;
        r0 = r51;
        r5 = r0.rectY;
        r5 = (float) r5;
        r0 = r51;
        r6 = r0.rectX;
        r0 = r51;
        r7 = r0.rectW;
        r6 = r6 + r7;
        r6 = (float) r6;
        r0 = r51;
        r7 = r0.rectY;
        r0 = r51;
        r8 = r0.rectH;
        r7 = r7 + r8;
        r7 = (float) r7;
        r0 = r30;
        r0.<init>(r4, r5, r6, r7);
        r0 = r51;
        r4 = r0.isLog;
        if (r4 == 0) goto L_0x0162;
    L_0x013d:
        r4 = com.fimi.TcpClient.getIntance();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "选框屏幕坐标：-<";
        r5 = r5.append(r6);
        r6 = r30.toString();
        r5 = r5.append(r6);
        r6 = "->    ";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.sendLog(r5);
    L_0x0162:
        r31 = new android.graphics.RectF;	 Catch:{ Exception -> 0x04f1 }
        r0 = r46;
        r4 = (float) r0;	 Catch:{ Exception -> 0x04f1 }
        r0 = r49;
        r5 = (float) r0;	 Catch:{ Exception -> 0x04f1 }
        r0 = r47;
        r6 = (float) r0;	 Catch:{ Exception -> 0x04f1 }
        r0 = r50;
        r7 = (float) r0;	 Catch:{ Exception -> 0x04f1 }
        r0 = r31;
        r0.<init>(r4, r5, r6, r7);	 Catch:{ Exception -> 0x04f1 }
        r0 = r51;
        r4 = r0.isLog;	 Catch:{ Exception -> 0x054f }
        if (r4 == 0) goto L_0x019e;
    L_0x017b:
        r4 = com.fimi.TcpClient.getIntance();	 Catch:{ Exception -> 0x054f }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x054f }
        r5.<init>();	 Catch:{ Exception -> 0x054f }
        r6 = "识别图片在PFV坐标： -<";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x054f }
        r0 = r31;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x054f }
        r6 = "->   ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x054f }
        r5 = r5.toString();	 Catch:{ Exception -> 0x054f }
        r4.sendLog(r5);	 Catch:{ Exception -> 0x054f }
    L_0x019e:
        r0 = r51;
        r5 = r0.bitmap;	 Catch:{ Exception -> 0x054f }
        r8 = r47 - r46;
        r9 = r50 - r49;
        r10 = 0;
        r4 = r51;
        r6 = r46;
        r7 = r49;
        r34 = r4.cropBitmap(r5, r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x054f }
        r18 = new android.graphics.Canvas;	 Catch:{ Exception -> 0x054f }
        r0 = r51;
        r4 = r0.croppedBitmap;	 Catch:{ Exception -> 0x054f }
        r0 = r18;
        r0.<init>(r4);	 Catch:{ Exception -> 0x054f }
        r0 = r51;
        r4 = r0.frameToCropTransform;	 Catch:{ Exception -> 0x054f }
        r5 = 0;
        r0 = r18;
        r1 = r34;
        r0.drawBitmap(r1, r4, r5);	 Catch:{ Exception -> 0x054f }
        r34.recycle();	 Catch:{ Exception -> 0x054f }
        r30 = r31;
    L_0x01cd:
        r38 = android.os.SystemClock.uptimeMillis();
        r0 = r51;
        r4 = r0.detector;
        r0 = r51;
        r5 = r0.croppedBitmap;
        r27 = r4.recognizeImage(r5);
        r4 = android.os.SystemClock.uptimeMillis();
        r4 = r4 - r38;
        r0 = r51;
        r0.lastProcessingTimeMs = r4;
        r4 = "";
        r0 = r51;
        r0.objTitle = r4;
        r30 = new android.graphics.RectF;
        r0 = r51;
        r4 = r0.rectX;
        r4 = (float) r4;
        r0 = r51;
        r5 = r0.rectY;
        r5 = (float) r5;
        r0 = r51;
        r6 = r0.rectX;
        r0 = r51;
        r7 = r0.rectW;
        r6 = r6 + r7;
        r6 = (float) r6;
        r0 = r51;
        r7 = r0.rectY;
        r0 = r51;
        r8 = r0.rectH;
        r7 = r7 + r8;
        r7 = (float) r7;
        r0 = r30;
        r0.<init>(r4, r5, r6, r7);
        r0 = r30;
        r4 = r0.left;
        r4 = r4 * r35;
        r0 = (int) r4;
        r45 = r0;
        r0 = r30;
        r4 = r0.top;
        r4 = r4 * r36;
        r0 = (int) r4;
        r48 = r0;
        r0 = r30;
        r4 = r0.right;
        r4 = r4 * r35;
        r0 = (int) r4;
        r44 = r0;
        r0 = r30;
        r4 = r0.bottom;
        r4 = r4 * r36;
        r0 = (int) r4;
        r21 = r0;
        r43 = new android.graphics.RectF;
        r0 = r45;
        r4 = (float) r0;
        r0 = r48;
        r5 = (float) r0;
        r0 = r44;
        r6 = (float) r0;
        r0 = r21;
        r7 = (float) r0;
        r0 = r43;
        r0.<init>(r4, r5, r6, r7);
        r0 = r51;
        r4 = r0.isLog;
        if (r4 == 0) goto L_0x0274;
    L_0x024f:
        r4 = com.fimi.TcpClient.getIntance();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "选框在Fpv坐标：";
        r5 = r5.append(r6);
        r6 = r43.toString();
        r5 = r5.append(r6);
        r6 = "    ";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.sendLog(r5);
    L_0x0274:
        r0 = r51;
        r4 = r0.isLog;
        if (r4 == 0) goto L_0x0298;
    L_0x027a:
        r4 = com.fimi.TcpClient.getIntance();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "results size= ";
        r5 = r5.append(r6);
        r6 = r27.size();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.sendLog(r5);
    L_0x0298:
        r4 = r27.iterator();
    L_0x029c:
        r5 = r4.hasNext();
        if (r5 == 0) goto L_0x0553;
    L_0x02a2:
        r26 = r4.next();
        r26 = (com.fimi.app.x8s.tensortfloow.Classifier.Recognition) r26;
        r23 = r26.getLocation();
        r0 = r51;
        r5 = r0.isLog;
        if (r5 == 0) goto L_0x02e4;
    L_0x02b2:
        r5 = com.fimi.TcpClient.getIntance();
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = " getConfidence= ";
        r6 = r6.append(r7);
        r7 = r26.getConfidence();
        r6 = r6.append(r7);
        r7 = " 识别结果： ";
        r6 = r6.append(r7);
        r7 = r23.toString();
        r6 = r6.append(r7);
        r7 = "   ";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.sendLog(r6);
    L_0x02e4:
        if (r23 == 0) goto L_0x029c;
    L_0x02e6:
        r5 = r26.getConfidence();
        r5 = r5.floatValue();
        r6 = 1045220557; // 0x3e4ccccd float:0.2 double:5.164075695E-315;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 < 0) goto L_0x029c;
    L_0x02f5:
        r0 = r23;
        r5 = r0.left;
        r5 = java.lang.Math.abs(r5);
        r6 = 1133576192; // 0x43910000 float:290.0 double:5.600610534E-315;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x029c;
    L_0x0303:
        r0 = r23;
        r5 = r0.top;
        r5 = java.lang.Math.abs(r5);
        r6 = 1133576192; // 0x43910000 float:290.0 double:5.600610534E-315;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x029c;
    L_0x0311:
        r0 = r23;
        r5 = r0.right;
        r5 = java.lang.Math.abs(r5);
        r6 = 1135542272; // 0x43af0000 float:350.0 double:5.61032426E-315;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x029c;
    L_0x031f:
        r0 = r23;
        r5 = r0.bottom;
        r5 = java.lang.Math.abs(r5);
        r6 = 1135542272; // 0x43af0000 float:350.0 double:5.61032426E-315;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x029c;
    L_0x032d:
        r0 = r23;
        r5 = r0.left;
        r5 = (int) r5;
        r29 = r46 + r5;
        r0 = r23;
        r5 = r0.top;
        r5 = (int) r5;
        r33 = r49 + r5;
        r0 = r23;
        r5 = r0.right;
        r5 = (int) r5;
        r32 = r46 + r5;
        r0 = r23;
        r5 = r0.bottom;
        r5 = (int) r5;
        r28 = r49 + r5;
        if (r29 >= 0) goto L_0x034d;
    L_0x034b:
        r29 = 0;
    L_0x034d:
        if (r33 >= 0) goto L_0x0351;
    L_0x034f:
        r33 = 0;
    L_0x0351:
        r0 = r51;
        r5 = r0.VIDEO_WIDTH;
        r0 = r32;
        if (r0 <= r5) goto L_0x035f;
    L_0x0359:
        r0 = r51;
        r0 = r0.VIDEO_WIDTH;
        r32 = r0;
    L_0x035f:
        r0 = r51;
        r5 = r0.VIDEO_HEIGHT;
        r0 = r28;
        if (r0 <= r5) goto L_0x036d;
    L_0x0367:
        r0 = r51;
        r0 = r0.VIDEO_HEIGHT;
        r28 = r0;
    L_0x036d:
        r24 = new android.graphics.RectF;
        r0 = r29;
        r5 = (float) r0;
        r0 = r33;
        r6 = (float) r0;
        r0 = r32;
        r7 = (float) r0;
        r0 = r28;
        r8 = (float) r0;
        r0 = r24;
        r0.<init>(r5, r6, r7, r8);
        r25 = 0;
        if (r22 != 0) goto L_0x038e;
    L_0x0384:
        r0 = r51;
        r1 = r24;
        r2 = r43;
        r25 = r0.RectOverlap(r1, r2);
    L_0x038e:
        r0 = r51;
        r5 = r0.isLog;
        if (r5 == 0) goto L_0x03c4;
    L_0x0394:
        r5 = com.fimi.TcpClient.getIntance();
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = " 识别结果在Fpv坐标: ";
        r6 = r6.append(r7);
        r7 = r24.toString();
        r6 = r6.append(r7);
        r7 = " --- ";
        r6 = r6.append(r7);
        r0 = r25;
        r6 = r6.append(r0);
        r7 = "         ===";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.sendLog(r6);
    L_0x03c4:
        if (r17 != 0) goto L_0x029c;
    L_0x03c6:
        r5 = 1;
        r0 = r22;
        if (r0 == r5) goto L_0x03d2;
    L_0x03cb:
        if (r22 != 0) goto L_0x029c;
    L_0x03cd:
        r5 = 1;
        r0 = r25;
        if (r0 != r5) goto L_0x029c;
    L_0x03d2:
        r9 = 1;
        r0 = r24;
        r4 = r0.left;
        r0 = (int) r4;
        r41 = r0;
        r0 = r24;
        r4 = r0.top;
        r0 = (int) r4;
        r42 = r0;
        r4 = r24.width();
        r0 = (int) r4;
        r40 = r0;
        r4 = r24.height();
        r0 = (int) r4;
        r37 = r0;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = r26.getTitle();
        r4 = r4.append(r5);
        r5 = "-[procTime=(";
        r4 = r4.append(r5);
        r0 = r51;
        r6 = r0.lastProcessingTimeMs;
        r4 = r4.append(r6);
        r5 = ")ms,";
        r4 = r4.append(r5);
        r5 = "confidence=";
        r4 = r4.append(r5);
        r5 = r26.getConfidence();
        r4 = r4.append(r5);
        r5 = "]";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r0 = r51;
        r0.objTitle = r4;
        r4 = r26.getId();
        if (r4 == 0) goto L_0x043e;
    L_0x0432:
        r4 = r26.getId();
        r4 = java.lang.Integer.valueOf(r4);
        r0 = r51;
        r0.classfier = r4;
    L_0x043e:
        if (r9 == 0) goto L_0x0515;
    L_0x0440:
        r0 = r51;
        r4 = r0.mFrameWidth;
        r0 = r51;
        r5 = r0.SIZE_WIDTH;
        r0 = r51;
        r35 = r0.getScaleX1(r4, r5);
        r0 = r51;
        r4 = r0.mFrameHeight;
        r0 = r51;
        r5 = r0.SIZE_HEIGHT;
        r0 = r51;
        r36 = r0.getScaleX1(r4, r5);
        r0 = r41;
        r4 = (float) r0;
        r4 = r4 * r35;
        r0 = (int) r4;
        r45 = r0;
        r0 = r42;
        r4 = (float) r0;
        r4 = r4 * r36;
        r0 = (int) r4;
        r48 = r0;
        r0 = r40;
        r4 = (float) r0;
        r4 = r4 * r35;
        r4 = (int) r4;
        r44 = r45 + r4;
        r0 = r37;
        r4 = (float) r0;
        r4 = r4 * r36;
        r4 = (int) r4;
        r21 = r48 + r4;
        r19 = new android.graphics.RectF;
        r0 = r45;
        r4 = (float) r0;
        r0 = r48;
        r5 = (float) r0;
        r0 = r44;
        r6 = (float) r0;
        r0 = r21;
        r7 = (float) r0;
        r0 = r19;
        r0.<init>(r4, r5, r6, r7);
        r0 = r51;
        r4 = r0.isLog;
        if (r4 == 0) goto L_0x04bf;
    L_0x0495:
        r4 = com.fimi.TcpClient.getIntance();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = " 识别结果FPV坐标还原到屏幕坐标=  --- ";
        r5 = r5.append(r6);
        r0 = r35;
        r5 = r5.append(r0);
        r6 = " ";
        r5 = r5.append(r6);
        r6 = r19.toString();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.sendLog(r5);
    L_0x04bf:
        r0 = r19;
        r5 = r0.left;
        r0 = r19;
        r6 = r0.top;
        r0 = r19;
        r7 = r0.right;
        r0 = r19;
        r8 = r0.bottom;
        r0 = r51;
        r4 = r0.classfier;
        r10 = r4.intValue();
        r4 = r51;
        r4.onResult(r5, r6, r7, r8, r9, r10);
    L_0x04dc:
        r4 = 0;
        r0 = r51;
        r0.isTouch = r4;
        r4 = 0;
        r0 = r51;
        r0.isFirst = r4;
        r4 = 0;
        r0 = r51;
        r0.isRev = r4;
        r4 = 0;
        r0 = r51;
        r0.isFirstWaiting = r4;
    L_0x04f0:
        return;
    L_0x04f1:
        r20 = move-exception;
    L_0x04f2:
        r20.printStackTrace();
        r4 = com.fimi.TcpClient.getIntance();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "roiRect ";
        r5 = r5.append(r6);
        r6 = r20.toString();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.sendLog(r5);
        goto L_0x01cd;
    L_0x0515:
        if (r22 != 0) goto L_0x0534;
    L_0x0517:
        r0 = r51;
        r4 = r0.rectX;
        r11 = (float) r4;
        r0 = r51;
        r4 = r0.rectY;
        r12 = (float) r4;
        r0 = r51;
        r4 = r0.rectW;
        r13 = (float) r4;
        r0 = r51;
        r4 = r0.rectH;
        r14 = (float) r4;
        r15 = 1;
        r16 = 0;
        r10 = r51;
        r10.onResult2(r11, r12, r13, r14, r15, r16);
        goto L_0x04dc;
    L_0x0534:
        r0 = r51;
        r4 = r0.rectX;
        r4 = r4 + -50;
        r11 = (float) r4;
        r0 = r51;
        r4 = r0.rectY;
        r4 = r4 + -50;
        r12 = (float) r4;
        r13 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r14 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r15 = 1;
        r16 = 0;
        r10 = r51;
        r10.onResult2(r11, r12, r13, r14, r15, r16);
        goto L_0x04dc;
    L_0x054f:
        r20 = move-exception;
        r30 = r31;
        goto L_0x04f2;
    L_0x0553:
        r9 = r17;
        goto L_0x043e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.app.x8s.tensortfloow.X8DetectionControler.tfLiteApiModel():void");
    }
}
