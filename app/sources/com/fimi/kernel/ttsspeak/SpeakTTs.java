package com.fimi.kernel.ttsspeak;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.util.Log;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.fimi.kernel.R;
import com.fimi.thirdpartysdk.ThirdPartyConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpeakTTs implements SpeechSynthesizerListener {
    private static final String CH_SPEECH_FEMALE = "bd_etts_speech_female.dat";
    private static final String CH_TEXT = "bd_etts_text.dat";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static boolean TTS_PLAY_FLAGE = false;
    private static SpeakTTs mSpeakTTs;
    private static boolean playFlag = false;
    private static Context ttsContext;
    private AudioManager audioManager;
    private int currentAudio;
    private boolean isAuthTTS;
    private boolean isChangeAudio;
    private SpeakStatusListener listener;
    private String mSampleDirPath;
    private SpeechSynthesizer mSpeechSynthesizer;
    private int maxAudio;
    private List<String> speakList = new ArrayList();

    public interface SpeakStatusListener {
        void speakFinish(boolean z);
    }

    private SpeakTTs() {
        if (ttsContext != null) {
            this.audioManager = (AudioManager) ttsContext.getSystemService("audio");
            this.currentAudio = this.audioManager.getStreamVolume(3);
            this.maxAudio = this.audioManager.getStreamMaxVolume(3);
        }
    }

    public static SpeakTTs initContext(Context context) {
        ttsContext = context;
        if (mSpeakTTs == null) {
            synchronized (SpeakTTs.class) {
                mSpeakTTs = new SpeakTTs();
            }
        }
        return mSpeakTTs;
    }

    public static SpeakTTs obtain(Context context) {
        ttsContext = context;
        return mSpeakTTs;
    }

    public void initTTSAuth() {
        if (!this.isAuthTTS) {
            initialEnv();
            initialTts();
        }
    }

    public void addSpeaksListener(SpeakStatusListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public void speakMessage(String str) {
        if (str != null && this.mSpeechSynthesizer != null) {
            if (!(this.audioManager == null || this.currentAudio > this.maxAudio || this.isChangeAudio)) {
                this.isChangeAudio = true;
                this.audioManager.setStreamVolume(3, this.audioManager.getStreamMaxVolume(3), 4);
            }
            try {
                if (playFlag) {
                    this.mSpeechSynthesizer.stop();
                }
                this.mSpeechSynthesizer.speak(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSpeakMessage() {
        if (this.mSpeechSynthesizer != null) {
            this.mSpeechSynthesizer.stop();
        }
    }

    public void recyleTTS() {
        this.mSpeechSynthesizer.release();
    }

    public void setPaPamSpeek(String value) {
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, value);
    }

    public void setSpeekVoice(String value) {
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, value);
    }

    public void setSpeekPich(String value) {
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, value);
    }

    public void onSynthesizeStart(String s) {
    }

    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
    }

    public void onSynthesizeFinish(String s) {
    }

    public void onSpeechStart(String s) {
        playFlag = true;
        if (this.listener != null) {
            this.listener.speakFinish(false);
        }
    }

    public void onSpeechProgressChanged(String s, int i) {
    }

    public void onSpeechFinish(String s) {
        playFlag = false;
        if (this.listener != null) {
            this.listener.speakFinish(true);
        }
        this.speakList.add(s);
    }

    public void onError(String s, SpeechError speechError) {
        if (this.listener != null) {
            this.listener.speakFinish(true);
        }
    }

    private void initialEnv() {
        if (this.mSampleDirPath == null) {
            this.mSampleDirPath = Environment.getExternalStorageDirectory().toString() + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(this.mSampleDirPath);
        copyFromAssetsToSdcard(false, CH_SPEECH_FEMALE, this.mSampleDirPath + "/" + CH_SPEECH_FEMALE);
        copyFromAssetsToSdcard(false, CH_TEXT, this.mSampleDirPath + "/" + CH_TEXT);
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, this.mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/bd_etts_speech_female_en.dat", this.mSampleDirPath + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/bd_etts_text_en.dat", this.mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:40:0x0068=Splitter:B:40:0x0068, B:17:0x003a=Splitter:B:17:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0083 A:{SYNTHETIC, Splitter:B:54:0x0083} */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0088 A:{SYNTHETIC, Splitter:B:57:0x0088} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003f A:{SYNTHETIC, Splitter:B:20:0x003f} */
    /* JADX WARNING: Removed duplicated region for block: B:73:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0044 A:{SYNTHETIC, Splitter:B:23:0x0044} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x006d A:{SYNTHETIC, Splitter:B:43:0x006d} */
    /* JADX WARNING: Removed duplicated region for block: B:77:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0072 A:{SYNTHETIC, Splitter:B:46:0x0072} */
    private void copyFromAssetsToSdcard(boolean r11, java.lang.String r12, java.lang.String r13) {
        /*
        r10 = this;
        r2 = new java.io.File;
        r2.<init>(r13);
        if (r11 != 0) goto L_0x000f;
    L_0x0007:
        if (r11 != 0) goto L_0x0047;
    L_0x0009:
        r8 = r2.exists();
        if (r8 != 0) goto L_0x0047;
    L_0x000f:
        r5 = 0;
        r3 = 0;
        r8 = ttsContext;	 Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x0067 }
        r8 = r8.getApplicationContext();	 Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x0067 }
        r8 = r8.getAssets();	 Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x0067 }
        r5 = r8.open(r12);	 Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x0067 }
        r6 = r13;
        r4 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x0067 }
        r4.<init>(r6);	 Catch:{ FileNotFoundException -> 0x009c, IOException -> 0x0067 }
        r8 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r8];	 Catch:{ FileNotFoundException -> 0x0038, IOException -> 0x0099, all -> 0x0096 }
        r7 = 0;
    L_0x002a:
        r8 = 0;
        r9 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r7 = r5.read(r0, r8, r9);	 Catch:{ FileNotFoundException -> 0x0038, IOException -> 0x0099, all -> 0x0096 }
        if (r7 < 0) goto L_0x0048;
    L_0x0033:
        r8 = 0;
        r4.write(r0, r8, r7);	 Catch:{ FileNotFoundException -> 0x0038, IOException -> 0x0099, all -> 0x0096 }
        goto L_0x002a;
    L_0x0038:
        r1 = move-exception;
        r3 = r4;
    L_0x003a:
        r1.printStackTrace();	 Catch:{ all -> 0x0080 }
        if (r3 == 0) goto L_0x0042;
    L_0x003f:
        r3.close();	 Catch:{ IOException -> 0x005d }
    L_0x0042:
        if (r5 == 0) goto L_0x0047;
    L_0x0044:
        r5.close();	 Catch:{ IOException -> 0x0062 }
    L_0x0047:
        return;
    L_0x0048:
        if (r4 == 0) goto L_0x004d;
    L_0x004a:
        r4.close();	 Catch:{ IOException -> 0x0058 }
    L_0x004d:
        if (r5 == 0) goto L_0x0047;
    L_0x004f:
        r5.close();	 Catch:{ IOException -> 0x0053 }
        goto L_0x0047;
    L_0x0053:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0047;
    L_0x0058:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x004d;
    L_0x005d:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0042;
    L_0x0062:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0047;
    L_0x0067:
        r1 = move-exception;
    L_0x0068:
        r1.printStackTrace();	 Catch:{ all -> 0x0080 }
        if (r3 == 0) goto L_0x0070;
    L_0x006d:
        r3.close();	 Catch:{ IOException -> 0x007b }
    L_0x0070:
        if (r5 == 0) goto L_0x0047;
    L_0x0072:
        r5.close();	 Catch:{ IOException -> 0x0076 }
        goto L_0x0047;
    L_0x0076:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0047;
    L_0x007b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0070;
    L_0x0080:
        r8 = move-exception;
    L_0x0081:
        if (r3 == 0) goto L_0x0086;
    L_0x0083:
        r3.close();	 Catch:{ IOException -> 0x008c }
    L_0x0086:
        if (r5 == 0) goto L_0x008b;
    L_0x0088:
        r5.close();	 Catch:{ IOException -> 0x0091 }
    L_0x008b:
        throw r8;
    L_0x008c:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0086;
    L_0x0091:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x008b;
    L_0x0096:
        r8 = move-exception;
        r3 = r4;
        goto L_0x0081;
    L_0x0099:
        r1 = move-exception;
        r3 = r4;
        goto L_0x0068;
    L_0x009c:
        r1 = move-exception;
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fimi.kernel.ttsspeak.SpeakTTs.copyFromAssetsToSdcard(boolean, java.lang.String, java.lang.String):void");
    }

    private void initialTts() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(ttsContext);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, this.mSampleDirPath + "/" + CH_TEXT);
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, this.mSampleDirPath + "/" + CH_SPEECH_FEMALE);
        this.mSpeechSynthesizer.setAppId(ttsContext.getResources().getString(R.string.baidu_app_key));
        this.mSpeechSynthesizer.setApiKey(ttsContext.getResources().getString(R.string.baidu_api_key), ttsContext.getResources().getString(R.string.baidu_secret_key));
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        if (authInfo.isSuccess()) {
            this.isAuthTTS = true;
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, ThirdPartyConstants.LOGIN_CHANNEL_TW);
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
            this.mSpeechSynthesizer.initTts(TtsMode.MIX);
            this.mSpeechSynthesizer.loadEnglishModel(this.mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, this.mSampleDirPath + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
            return;
        }
        this.isAuthTTS = false;
        Log.i("ljh", "authInfo Error : " + authInfo.getTtsError().getDetailMessage());
    }

    public boolean isAuthTTS() {
        return this.isAuthTTS;
    }

    public void stopTTPs() {
        if (playFlag) {
            this.mSpeechSynthesizer.stop();
        }
    }

    public void setAuthTTS(boolean authTTS) {
        this.isAuthTTS = authTTS;
    }

    public boolean isChangeAudio() {
        return this.isChangeAudio;
    }

    public void setChangeAudio(boolean changeAudio) {
        this.isChangeAudio = changeAudio;
    }

    public int speakListMsg(List<String> msgList) {
        List<SpeechSynthesizeBag> bags = new ArrayList();
        if (msgList != null && msgList.size() > 0) {
            for (int j = 0; j < msgList.size(); j++) {
                if (!this.speakList.contains((String) msgList.get(j))) {
                    bags.add(getSpeechSynthesizeBag((String) msgList.get(j), String.valueOf(j)));
                }
            }
        }
        if (this.mSpeechSynthesizer == null) {
            return -1;
        }
        this.speakList.clear();
        return this.mSpeechSynthesizer.batchSpeak(bags);
    }

    private SpeechSynthesizeBag getSpeechSynthesizeBag(String text, String utteranceId) {
        SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
        speechSynthesizeBag.setText(text);
        speechSynthesizeBag.setUtteranceId(utteranceId);
        return speechSynthesizeBag;
    }
}
