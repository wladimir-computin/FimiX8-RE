package com.fimi.soul.media.player;

import android.app.Dialog;
import android.content.Context;

class FermiPlayerDialog extends Dialog {
    public FermiPlayerDialog(Context context) {
        super(context);
    }

    public FermiPlayerDialog(Context context, int theme) {
        super(context, theme);
        setCancelable(false);
    }
}
