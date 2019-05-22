package com.fimi.soul.media.player;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.fimi.soul.media.player.annotations.AccessedByNative;
import com.fimi.soul.media.player.annotations.CalledByNative;
import com.fimi.soul.media.player.pragma.DebugLog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public final class FimiMediaPlayer extends AbstractMediaPlayer {
    public static final int IJK_LOG_DEBUG = 3;
    public static final int IJK_LOG_DEFAULT = 1;
    public static final int IJK_LOG_ERROR = 6;
    public static final int IJK_LOG_FATAL = 7;
    public static final int IJK_LOG_INFO = 4;
    public static final int IJK_LOG_SILENT = 8;
    public static final int IJK_LOG_UNKNOWN = 0;
    public static final int IJK_LOG_VERBOSE = 2;
    public static final int IJK_LOG_WARN = 5;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_ERROR = 100;
    private static final int MEDIA_INFO = 200;
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    protected static final int MEDIA_SET_VIDEO_SAR = 10001;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_TIMED_TEXT = 99;
    public static final int OPT_CATEGORY_CODEC = 2;
    public static final int OPT_CATEGORY_FORMAT = 1;
    public static final int OPT_CATEGORY_PLAYER = 4;
    public static final int OPT_CATEGORY_SWS = 3;
    public static final int SDL_FCC_RV16 = 909203026;
    public static final int SDL_FCC_RV32 = 842225234;
    public static final int SDL_FCC_YV12 = 842094169;
    private static final String TAG = FimiMediaPlayer.class.getName();
    private static FimiMediaPlayer fimiMediaPlayer;
    private static volatile boolean mIsLibLoaded = false;
    private static volatile boolean mIsNativeInitialized = false;
    private static FimiLibLoader sLocalLibLoader = new FimiLibLoader() {
        public void loadLibrary(String libName) throws UnsatisfiedLinkError, SecurityException {
            System.loadLibrary(libName);
        }
    };
    private String mDataSource;
    private EventHandler mEventHandler;
    @AccessedByNative
    private int mListenerContext;
    @AccessedByNative
    private long mNativeMediaPlayer;
    @AccessedByNative
    private int mNativeSurfaceTexture;
    private OnControlMessageListener mOnControlMessageListener;
    private OnMediaCodecSelectListener mOnMediaCodecSelectListener;
    private OnNativeInvokeListener mOnNativeInvokeListener;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private SurfaceHolder mSurfaceHolder;
    private int mVideoHeight;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WakeLock mWakeLock;

    public interface OnMediaCodecSelectListener {
        String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String str, int i, int i2);
    }

    public static class DefaultMediaCodecSelector implements OnMediaCodecSelectListener {
        public static DefaultMediaCodecSelector sInstance = new DefaultMediaCodecSelector();

        @TargetApi(16)
        public String onMediaCodecSelect(IMediaPlayer mp, String mimeType, int profile, int level) {
            if (VERSION.SDK_INT < 16) {
                return null;
            }
            if (TextUtils.isEmpty(mimeType)) {
                return null;
            }
            Log.i(FimiMediaPlayer.TAG, String.format(Locale.US, "onSelectCodec: mime=%s, profile=%d, level=%d", new Object[]{mimeType, Integer.valueOf(profile), Integer.valueOf(level)}));
            ArrayList<FimiMediaCodecInfo> candidateCodecList = new ArrayList();
            int numCodecs = MediaCodecList.getCodecCount();
            for (int i = 0; i < numCodecs; i++) {
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
                Log.d(FimiMediaPlayer.TAG, String.format(Locale.US, "  found codec: %s", new Object[]{codecInfo.getName()}));
                if (!codecInfo.isEncoder()) {
                    String[] types = codecInfo.getSupportedTypes();
                    if (types != null) {
                        for (String type : types) {
                            if (!TextUtils.isEmpty(type)) {
                                Log.d(FimiMediaPlayer.TAG, String.format(Locale.US, "    mime: %s", new Object[]{type}));
                                if (type.equalsIgnoreCase(mimeType)) {
                                    FimiMediaCodecInfo candidate = FimiMediaCodecInfo.setupCandidate(codecInfo, mimeType);
                                    if (candidate != null) {
                                        candidateCodecList.add(candidate);
                                        Log.i(FimiMediaPlayer.TAG, String.format(Locale.US, "candidate codec: %s rank=%d", new Object[]{codecInfo.getName(), Integer.valueOf(candidate.mRank)}));
                                        candidate.dumpProfileLevels(mimeType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (candidateCodecList.isEmpty()) {
                return null;
            }
            FimiMediaCodecInfo bestCodec = (FimiMediaCodecInfo) candidateCodecList.get(0);
            Iterator it = candidateCodecList.iterator();
            while (it.hasNext()) {
                FimiMediaCodecInfo codec = (FimiMediaCodecInfo) it.next();
                if (codec.mRank > bestCodec.mRank) {
                    bestCodec = codec;
                }
            }
            if (bestCodec.mRank < FimiMediaCodecInfo.RANK_LAST_CHANCE) {
                Log.w(FimiMediaPlayer.TAG, String.format(Locale.US, "unaccetable codec: %s", new Object[]{bestCodec.mCodecInfo.getName()}));
                return null;
            }
            Log.i(FimiMediaPlayer.TAG, String.format(Locale.US, "selected codec: %s rank=%d", new Object[]{bestCodec.mCodecInfo.getName(), Integer.valueOf(bestCodec.mRank)}));
            return bestCodec.mCodecInfo.getName();
        }
    }

    private static class EventHandler extends Handler {
        private WeakReference<FimiMediaPlayer> mWeakPlayer;

        public EventHandler(FimiMediaPlayer mp, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference(mp);
        }

        public void handleMessage(Message msg) {
            FimiMediaPlayer player = (FimiMediaPlayer) this.mWeakPlayer.get();
            if (player == null || player.mNativeMediaPlayer == 0) {
                DebugLog.w(FimiMediaPlayer.TAG, "FimiMediaPlayer went away with unhandled events");
                return;
            }
            switch (msg.what) {
                case 0:
                case 99:
                    return;
                case 1:
                    player.notifyOnPrepared();
                    return;
                case 2:
                    player.notifyOnCompletion();
                    player.stayAwake(false);
                    return;
                case 3:
                    long bufferPosition = (long) msg.arg1;
                    if (bufferPosition < 0) {
                        bufferPosition = 0;
                    }
                    long percent = 0;
                    long duration = player.getDuration();
                    if (duration > 0) {
                        percent = (100 * bufferPosition) / duration;
                    }
                    if (percent >= 100) {
                        percent = 100;
                    }
                    player.notifyOnBufferingUpdate((int) percent);
                    return;
                case 4:
                    player.notifyOnSeekComplete();
                    return;
                case 5:
                    player.mVideoWidth = msg.arg1;
                    player.mVideoHeight = msg.arg2;
                    player.notifyOnVideoSizeChanged(player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                    return;
                case 100:
                    DebugLog.e(FimiMediaPlayer.TAG, "Error (" + msg.arg1 + "," + msg.arg2 + ")");
                    if (!player.notifyOnError(msg.arg1, msg.arg2)) {
                        player.notifyOnCompletion();
                    }
                    player.stayAwake(false);
                    return;
                case 200:
                    switch (msg.arg1) {
                        case 3:
                            DebugLog.i(FimiMediaPlayer.TAG, "Info: MEDIA_INFO_VIDEO_RENDERING_START\n");
                            break;
                    }
                    player.notifyOnInfo(msg.arg1, msg.arg2);
                    return;
                case 10001:
                    player.mVideoSarNum = msg.arg1;
                    player.mVideoSarDen = msg.arg2;
                    player.notifyOnVideoSizeChanged(player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                    return;
                default:
                    DebugLog.e(FimiMediaPlayer.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    public interface OnControlMessageListener {
        String onControlResolveSegmentUrl(int i);
    }

    public interface OnNativeInvokeListener {
        boolean onNativeInvoke(int i, Bundle bundle);
    }

    private native String _getAudioCodecInfo();

    private static native String _getColorFormatName(int i);

    private native Bundle _getMediaMeta();

    private native String _getVideoCodecInfo();

    private native void _pause() throws IllegalStateException;

    private native void _release();

    private native void _reset();

    private native void _setDataSource(String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSourceFd(int i) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setOption(int i, String str, long j);

    private native void _setOption(int i, String str, String str2);

    private native void _setVideoSurface(Surface surface);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native int _stream2jpg(String str, String str2);

    private native void native_finalize();

    private static native void native_init();

    private native void native_message_loop(Object obj);

    public static native void native_profileBegin(String str);

    public static native void native_profileEnd();

    public static native void native_setLogLevel(int i);

    private native void native_setup(Object obj);

    public native int GetNetworkInfo(int[] iArr) throws IllegalStateException;

    public native int PreviewInputNetData(byte[] bArr, int i, int i2) throws IllegalStateException;

    public native int RtmpStart(String str, int i, int i2) throws IllegalStateException;

    public native int RtmpStop(int i) throws IllegalStateException;

    public native void _prepareAsync(int i, int i2) throws IllegalStateException;

    public native int fm_avp_concatVid(String str, String str2);

    public native int fm_avp_mergeAV(String str, String str2, String str3);

    public native int fm_avp_splitVid(String str, String str2, int i, int i2);

    public native int fm_avp_vid2img(String str, String str2);

    public native int getAudioSessionId();

    public native long getCurrentPosition();

    public native long getDuration();

    public native boolean isPlaying();

    public native void seekTo(long j) throws IllegalStateException;

    public native void setVolume(float f, float f2);

    public static void loadLibrariesOnce(FimiLibLoader libLoader) {
        synchronized (FimiMediaPlayer.class) {
            if (!mIsLibLoaded) {
                if (libLoader == null) {
                    libLoader = sLocalLibLoader;
                }
                libLoader.loadLibrary("fmffmpeg");
                libLoader.loadLibrary("fmsdl");
                libLoader.loadLibrary("fmplayer");
                mIsLibLoaded = true;
            }
        }
    }

    private static void initNativeOnce() {
        synchronized (FimiMediaPlayer.class) {
            long time = new File("FimiMediaPlayer.java").lastModified();
            if (!mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
                DebugLog.w(TAG, "aizj version=" + time);
            }
        }
    }

    public FimiMediaPlayer() {
        this(sLocalLibLoader);
    }

    public static FimiMediaPlayer getIntance() {
        if (fimiMediaPlayer == null) {
            return new FimiMediaPlayer();
        }
        return fimiMediaPlayer;
    }

    public FimiMediaPlayer(FimiLibLoader libLoader) {
        this.mWakeLock = null;
        initPlayer(libLoader);
    }

    private void initPlayer(FimiLibLoader libLoader) {
        loadLibrariesOnce(libLoader);
        initNativeOnce();
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            looper = Looper.getMainLooper();
            if (looper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                this.mEventHandler = null;
            }
        }
        native_setup(new WeakReference(this));
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurface(Surface surface) {
        if (this.mScreenOnWhilePlaying && surface != null) {
            DebugLog.w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        this.mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, null);
    }

    /* JADX WARNING: Missing block: B:28:0x007b, code skipped:
            android.util.Log.d(TAG, "Couldn't open file on client side, trying server side");
            setDataSource(r11.toString(), (java.util.Map) r12);
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            return;
     */
    @android.annotation.TargetApi(14)
    public void setDataSource(android.content.Context r10, android.net.Uri r11, java.util.Map<java.lang.String, java.lang.String> r12) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.SecurityException, java.lang.IllegalStateException {
        /*
        r9 = this;
        r8 = r11.getScheme();
        r0 = "file";
        r0 = r0.equals(r8);
        if (r0 == 0) goto L_0x0014;
    L_0x000c:
        r0 = r11.getPath();
        r9.setDataSource(r0);
    L_0x0013:
        return;
    L_0x0014:
        r0 = "content";
        r0 = r0.equals(r8);
        if (r0 == 0) goto L_0x003a;
    L_0x001c:
        r0 = "settings";
        r1 = r11.getAuthority();
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x003a;
    L_0x0028:
        r0 = android.media.RingtoneManager.getDefaultType(r11);
        r11 = android.media.RingtoneManager.getActualDefaultRingtoneUri(r10, r0);
        if (r11 != 0) goto L_0x003a;
    L_0x0032:
        r0 = new java.io.FileNotFoundException;
        r1 = "Failed to resolve default ringtone";
        r0.<init>(r1);
        throw r0;
    L_0x003a:
        r6 = 0;
        r7 = r10.getContentResolver();	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        r0 = "r";
        r6 = r7.openAssetFileDescriptor(r11, r0);	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        if (r6 != 0) goto L_0x004d;
    L_0x0047:
        if (r6 == 0) goto L_0x0013;
    L_0x0049:
        r6.close();
        goto L_0x0013;
    L_0x004d:
        r0 = r6.getDeclaredLength();	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 >= 0) goto L_0x0064;
    L_0x0057:
        r0 = r6.getFileDescriptor();	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        r9.setDataSource(r0);	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
    L_0x005e:
        if (r6 == 0) goto L_0x0013;
    L_0x0060:
        r6.close();
        goto L_0x0013;
    L_0x0064:
        r1 = r6.getFileDescriptor();	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        r2 = r6.getStartOffset();	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        r4 = r6.getDeclaredLength();	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        r0 = r9;
        r0.setDataSource(r1, r2, r4);	 Catch:{ SecurityException -> 0x0075, IOException -> 0x008a, all -> 0x0091 }
        goto L_0x005e;
    L_0x0075:
        r0 = move-exception;
        if (r6 == 0) goto L_0x007b;
    L_0x0078:
        r6.close();
    L_0x007b:
        r0 = TAG;
        r1 = "Couldn't open file on client side, trying server side";
        android.util.Log.d(r0, r1);
        r0 = r11.toString();
        r9.setDataSource(r0, r12);
        goto L_0x0013;
    L_0x008a:
        r0 = move-exception;
        if (r6 == 0) goto L_0x007b;
    L_0x008d:
        r6.close();
        goto L_0x007b;
    L_0x0091:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0097;
    L_0x0094:
        r6.close();
    L_0x0097:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.soul.media.player.FimiMediaPlayer.setDataSource(android.content.Context, android.net.Uri, java.util.Map):void");
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mDataSource = path;
        _setDataSource(path, null, null);
    }

    public void setDataSource(String path, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (!(headers == null || headers.isEmpty())) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, String> entry : headers.entrySet()) {
                sb.append((String) entry.getKey());
                sb.append(":");
                if (!TextUtils.isEmpty((String) entry.getValue())) {
                    sb.append((String) entry.getValue());
                }
                sb.append("\r\n");
            }
        }
        setDataSource(path);
    }

    @TargetApi(13)
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        if (VERSION.SDK_INT < 12) {
            try {
                Field f = fd.getClass().getDeclaredField("descriptor");
                f.setAccessible(true);
                _setDataSourceFd(f.getInt(fd));
                return;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
        ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(fd);
        try {
            _setDataSourceFd(pfd.getFd());
        } finally {
            pfd.close();
        }
    }

    private void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        setDataSource(fd);
    }

    public String getDataSource() {
        return this.mDataSource;
    }

    public void prepareAsync(int net_mode, int buffer_count) throws IllegalStateException {
        _prepareAsync(net_mode, buffer_count);
    }

    public int PlayerPreviewInputNetData(byte[] net_buffer, int start_pos, int data_len) throws IllegalStateException {
        PreviewInputNetData(net_buffer, start_pos, data_len);
        return 0;
    }

    public int PlayerGetNetworkInfo(int[] net_buffer) throws IllegalStateException {
        return GetNetworkInfo(net_buffer);
    }

    public void start() throws IllegalStateException {
        stayAwake(true);
        _start();
    }

    public void stop() throws IllegalStateException {
        stayAwake(false);
        _stop();
    }

    public void pause() throws IllegalStateException {
        stayAwake(false);
        _pause();
    }

    @SuppressLint({"Wakelock"})
    public void setWakeMode(Context context, int mode) {
        boolean washeld = false;
        if (this.mWakeLock != null) {
            if (this.mWakeLock.isHeld()) {
                washeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(NTLMConstants.FLAG_NEGOTIATE_128_BIT_ENCRYPTION | mode, FimiMediaPlayer.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (washeld) {
            this.mWakeLock.acquire();
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            if (screenOn && this.mSurfaceHolder == null) {
                DebugLog.w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    @SuppressLint({"Wakelock"})
    private void stayAwake(boolean awake) {
        if (this.mWakeLock != null) {
            if (awake && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            SurfaceHolder surfaceHolder = this.mSurfaceHolder;
            boolean z = this.mScreenOnWhilePlaying && this.mStayAwake;
            surfaceHolder.setKeepScreenOn(z);
        }
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public int getVideoSarNum() {
        return this.mVideoSarNum;
    }

    public int getVideoSarDen() {
        return this.mVideoSarDen;
    }

    public void release() {
        stayAwake(false);
        updateSurfaceScreenOn();
        resetListeners();
        _release();
    }

    public void reset() {
        stayAwake(false);
        _reset();
        this.mEventHandler.removeCallbacksAndMessages(null);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public MediaInfo getMediaInfo() {
        String[] nodes;
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.mMediaPlayerName = "ijkplayer";
        String videoCodecInfo = _getVideoCodecInfo();
        if (!TextUtils.isEmpty(videoCodecInfo)) {
            nodes = videoCodecInfo.split(",");
            if (nodes.length >= 2) {
                mediaInfo.mVideoDecoder = nodes[0];
                mediaInfo.mVideoDecoderImpl = nodes[1];
            } else if (nodes.length >= 1) {
                mediaInfo.mVideoDecoder = nodes[0];
                mediaInfo.mVideoDecoderImpl = "";
            }
        }
        String audioCodecInfo = _getAudioCodecInfo();
        if (!TextUtils.isEmpty(audioCodecInfo)) {
            nodes = audioCodecInfo.split(",");
            if (nodes.length >= 2) {
                mediaInfo.mAudioDecoder = nodes[0];
                mediaInfo.mAudioDecoderImpl = nodes[1];
            } else if (nodes.length >= 1) {
                mediaInfo.mAudioDecoder = nodes[0];
                mediaInfo.mAudioDecoderImpl = "";
            }
        }
        try {
            mediaInfo.mMeta = FimiMediaMeta.parse(_getMediaMeta());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mediaInfo;
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return true;
    }

    public boolean videoToJpg(String remoteUrl, String filename) {
        if (remoteUrl == null || "".equals(remoteUrl) || _stream2jpg(remoteUrl, filename) != 0) {
            return false;
        }
        return true;
    }

    public void setOption(int category, String name, String value) {
        _setOption(category, name, value);
    }

    public void setOption(int category, String name, long value) {
        _setOption(category, name, value);
    }

    public Bundle getMediaMeta() {
        return _getMediaMeta();
    }

    public static String getColorFormatName(int mediaCodecColorFormat) {
        return _getColorFormatName(mediaCodecColorFormat);
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setKeepInBackground(boolean keepInBackground) {
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
    }

    @CalledByNative
    private static void postEventFromNative(Object weakThiz, int what, int arg1, int arg2, Object obj) {
        if (weakThiz != null) {
            FimiMediaPlayer mp = (FimiMediaPlayer) ((WeakReference) weakThiz).get();
            if (mp != null) {
                if (what == 200 && arg1 == 2) {
                    mp.start();
                }
                if (mp.mEventHandler != null) {
                    mp.mEventHandler.sendMessage(mp.mEventHandler.obtainMessage(what, arg1, arg2, obj));
                }
            }
        }
    }

    public void setOnControlMessageListener(OnControlMessageListener listener) {
        this.mOnControlMessageListener = listener;
    }

    @CalledByNative
    private static String onControlResolveSegmentUrl(Object weakThiz, int segment) {
        DebugLog.ifmt(TAG, "onControlResolveSegmentUrl %d", Integer.valueOf(segment));
        if (weakThiz == null || !(weakThiz instanceof WeakReference)) {
            return null;
        }
        FimiMediaPlayer player = (FimiMediaPlayer) ((WeakReference) weakThiz).get();
        if (player == null) {
            return null;
        }
        OnControlMessageListener listener = player.mOnControlMessageListener;
        if (listener != null) {
            return listener.onControlResolveSegmentUrl(segment);
        }
        return null;
    }

    public void setOnNativeInvokeListener(OnNativeInvokeListener listener) {
        this.mOnNativeInvokeListener = listener;
    }

    @CalledByNative
    private static boolean onNativeInvoke(Object weakThiz, int what, Bundle args) {
        DebugLog.ifmt(TAG, "onNativeInvoke %d", Integer.valueOf(what));
        if (weakThiz == null || !(weakThiz instanceof WeakReference)) {
            return false;
        }
        FimiMediaPlayer player = (FimiMediaPlayer) ((WeakReference) weakThiz).get();
        if (player == null) {
            return false;
        }
        OnNativeInvokeListener listener = player.mOnNativeInvokeListener;
        if (listener != null) {
            return listener.onNativeInvoke(what, args);
        }
        return false;
    }

    public void setOnMediaCodecSelectListener(OnMediaCodecSelectListener listener) {
        this.mOnMediaCodecSelectListener = listener;
    }

    public void resetListeners() {
        super.resetListeners();
        this.mOnMediaCodecSelectListener = null;
    }

    @CalledByNative
    private static String onSelectCodec(Object weakThiz, String mimeType, int profile, int level) {
        if (weakThiz == null || !(weakThiz instanceof WeakReference)) {
            return null;
        }
        FimiMediaPlayer player = (FimiMediaPlayer) ((WeakReference) weakThiz).get();
        if (player == null) {
            return null;
        }
        OnMediaCodecSelectListener listener = player.mOnMediaCodecSelectListener;
        if (listener == null) {
            listener = DefaultMediaCodecSelector.sInstance;
        }
        return listener.onMediaCodecSelect(player, mimeType, profile, level);
    }

    public int vid2imgArray(String inputUrl, String outUrl) {
        return fm_avp_vid2img(inputUrl, outUrl);
    }

    public int spliteVedio(String inputUrl, String outUrl, int start, int end) {
        return fm_avp_splitVid(inputUrl, outUrl, start, end);
    }

    public int concatVedio(String input_array, String outUrl) {
        return fm_avp_concatVid(input_array, outUrl);
    }

    public int mergeAudio2Vedio(String vedioUrl, String audioString, String outUrl) {
        return fm_avp_mergeAV(vedioUrl, audioString, outUrl);
    }

    public int PlayerRtmpStart(String str_url, int net_mode, int audio_en) throws IllegalStateException {
        return RtmpStart(str_url, net_mode, audio_en);
    }

    public int PlayerRtmpStop(int flag) throws IllegalStateException {
        return RtmpStop(flag);
    }
}
