package com.fimi.soul.media.player;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.internal.AnalyticsEvents;
import com.fimi.thirdpartysdk.ThirdPartyConstants;
import com.twitter.sdk.android.core.internal.scribe.ScribeConfig;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class FimiMediaCodecInfo {
    public static int RANK_ACCEPTABLE = 700;
    public static int RANK_LAST_CHANCE = ScribeConfig.DEFAULT_SEND_INTERVAL_SECONDS;
    public static int RANK_MAX = 1000;
    public static int RANK_NON_STANDARD = 100;
    public static int RANK_NO_SENSE = 0;
    public static int RANK_SECURE = GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION;
    public static int RANK_SOFTWARE = 200;
    public static int RANK_TESTED = 800;
    private static final String TAG = "FimiMediaCodecInfo";
    private static Map<String, Integer> sKnownCodecList;
    public MediaCodecInfo mCodecInfo;
    public String mMimeType;
    public int mRank = 0;

    private static synchronized Map<String, Integer> getKnownCodecList() {
        Map<String, Integer> map;
        synchronized (FimiMediaCodecInfo.class) {
            if (sKnownCodecList != null) {
                map = sKnownCodecList;
            } else {
                sKnownCodecList = new TreeMap(String.CASE_INSENSITIVE_ORDER);
                sKnownCodecList.put("OMX.Nvidia.h264.decode", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.Nvidia.h264.decode.secure", Integer.valueOf(RANK_SECURE));
                sKnownCodecList.put("OMX.Intel.hw_vd.h264", Integer.valueOf(RANK_TESTED + 1));
                sKnownCodecList.put("OMX.Intel.VideoDecoder.AVC", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.qcom.video.decoder.avc", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.ittiam.video.decoder.avc", Integer.valueOf(RANK_NO_SENSE));
                sKnownCodecList.put("OMX.SEC.avc.dec", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.SEC.AVC.Decoder", Integer.valueOf(RANK_TESTED - 1));
                sKnownCodecList.put("OMX.SEC.avcdec", Integer.valueOf(RANK_TESTED - 2));
                sKnownCodecList.put("OMX.SEC.avc.sw.dec", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.Exynos.avc.dec", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.Exynos.AVC.Decoder", Integer.valueOf(RANK_TESTED - 1));
                sKnownCodecList.put("OMX.k3.video.decoder.avc", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.IMG.MSVDX.Decoder.AVC", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.TI.DUCATI1.VIDEO.DECODER", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.rk.video_decoder.avc", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.amlogic.avc.decoder.awesome", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.MARVELL.VIDEO.HW.CODA7542DECODER", Integer.valueOf(RANK_TESTED));
                sKnownCodecList.put("OMX.MARVELL.VIDEO.H264DECODER", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.remove("OMX.BRCM.vc4.decoder.avc");
                sKnownCodecList.remove("OMX.brcm.video.h264.hw.decoder");
                sKnownCodecList.remove("OMX.brcm.video.h264.decoder");
                sKnownCodecList.remove("OMX.ST.VFM.H264Dec");
                sKnownCodecList.remove("OMX.allwinner.video.decoder.avc");
                sKnownCodecList.remove("OMX.MS.AVC.Decoder");
                sKnownCodecList.remove("OMX.hantro.81x0.video.decoder");
                sKnownCodecList.remove("OMX.hisi.video.decoder");
                sKnownCodecList.remove("OMX.cosmo.video.decoder.avc");
                sKnownCodecList.remove("OMX.duos.h264.decoder");
                sKnownCodecList.remove("OMX.bluestacks.hw.decoder");
                sKnownCodecList.put("OMX.google.h264.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.google.h264.lc.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.k3.ffmpeg.decoder", Integer.valueOf(RANK_SOFTWARE));
                sKnownCodecList.put("OMX.ffmpeg.video.decoder", Integer.valueOf(RANK_SOFTWARE));
                map = sKnownCodecList;
            }
        }
        return map;
    }

    @TargetApi(16)
    public static FimiMediaCodecInfo setupCandidate(MediaCodecInfo codecInfo, String mimeType) {
        if (codecInfo == null || VERSION.SDK_INT < 16) {
            return null;
        }
        String name = codecInfo.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        name = name.toLowerCase(Locale.US);
        int rank = RANK_NO_SENSE;
        if (!name.startsWith("omx.")) {
            rank = RANK_NON_STANDARD;
        } else if (name.startsWith("omx.pv")) {
            rank = RANK_SOFTWARE;
        } else if (name.startsWith("omx.google.")) {
            rank = RANK_SOFTWARE;
        } else if (name.startsWith("omx.ffmpeg.")) {
            rank = RANK_SOFTWARE;
        } else if (name.startsWith("omx.k3.ffmpeg.")) {
            rank = RANK_SOFTWARE;
        } else if (name.startsWith("omx.avcodec.")) {
            rank = RANK_SOFTWARE;
        } else if (name.startsWith("omx.ittiam.")) {
            rank = RANK_NO_SENSE;
        } else if (!name.startsWith("omx.mtk.")) {
            Integer knownRank = (Integer) getKnownCodecList().get(name);
            if (knownRank != null) {
                rank = knownRank.intValue();
            } else {
                try {
                    if (codecInfo.getCapabilitiesForType(mimeType) != null) {
                        rank = RANK_ACCEPTABLE;
                    } else {
                        rank = RANK_LAST_CHANCE;
                    }
                } catch (Throwable th) {
                    rank = RANK_LAST_CHANCE;
                }
            }
        } else if (VERSION.SDK_INT < 18) {
            rank = RANK_NO_SENSE;
        } else {
            rank = RANK_TESTED;
        }
        FimiMediaCodecInfo candidate = new FimiMediaCodecInfo();
        candidate.mCodecInfo = codecInfo;
        candidate.mRank = rank;
        candidate.mMimeType = mimeType;
        return candidate;
    }

    @TargetApi(16)
    public void dumpProfileLevels(String mimeType) {
        if (VERSION.SDK_INT >= 16) {
            try {
                CodecCapabilities caps = this.mCodecInfo.getCapabilitiesForType(mimeType);
                int maxProfile = 0;
                int maxLevel = 0;
                if (!(caps == null || caps.profileLevels == null)) {
                    for (CodecProfileLevel profileLevel : caps.profileLevels) {
                        if (profileLevel != null) {
                            maxProfile = Math.max(maxProfile, profileLevel.profile);
                            maxLevel = Math.max(maxLevel, profileLevel.level);
                        }
                    }
                }
                Log.i(TAG, String.format(Locale.US, "%s", new Object[]{getProfileLevelName(maxProfile, maxLevel)}));
            } catch (Throwable th) {
                Log.i(TAG, "profile-level: exception");
            }
        }
    }

    public static String getProfileLevelName(int profile, int level) {
        return String.format(Locale.US, " %s Profile Level %s (%d,%d)", new Object[]{getProfileName(profile), getLevelName(level), Integer.valueOf(profile), Integer.valueOf(level)});
    }

    public static String getProfileName(int profile) {
        switch (profile) {
            case 1:
                return "Baseline";
            case 2:
                return "Main";
            case 4:
                return "Extends";
            case 8:
                return "High";
            case 16:
                return "High10";
            case 32:
                return "High422";
            case 64:
                return "High444";
            default:
                return AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        }
    }

    public static String getLevelName(int level) {
        switch (level) {
            case 1:
                return "1";
            case 2:
                return "1b";
            case 4:
                return "11";
            case 8:
                return "12";
            case 16:
                return "13";
            case 32:
                return "2";
            case 64:
                return "21";
            case 128:
                return "22";
            case 256:
                return ThirdPartyConstants.LOGIN_CHANNEL_TW;
            case 512:
                return ANSIConstants.RED_FG;
            case 1024:
                return ANSIConstants.GREEN_FG;
            case 2048:
                return "4";
            case 4096:
                return "41";
            case 8192:
                return "42";
            case 16384:
                return "5";
            case 32768:
                return "51";
            case 65536:
                return "52";
            default:
                return "0";
        }
    }
}
