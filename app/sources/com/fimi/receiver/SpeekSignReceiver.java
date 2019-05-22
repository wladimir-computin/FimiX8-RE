package com.fimi.receiver;

import android.content.Context;
import com.fimi.kernel.ttsspeak.SpeakTTs;
import com.fimi.receiver.NetworkStateReceiver.NetworkType;

public class SpeekSignReceiver extends NetworkStateReceiver {
    public void onNetworkStateChange(NetworkType type, Context context) {
        if (type == NetworkType.Wifi || type == NetworkType.Mobile) {
            SpeakTTs mSpeakTTs = SpeakTTs.initContext(context);
            if (!mSpeakTTs.isAuthTTS()) {
                mSpeakTTs.initTTSAuth();
            }
        }
    }
}
