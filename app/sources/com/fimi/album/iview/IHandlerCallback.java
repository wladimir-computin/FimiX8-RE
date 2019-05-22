package com.fimi.album.iview;

import android.os.Handler.Callback;
import android.os.Message;

public interface IHandlerCallback extends Callback {
    boolean handleMessage(Message message);
}
