package com.fimi.widget.sticklistview.util;

import android.content.Context;
import android.widget.Checkable;

class CheckableWrapperView extends WrapperView implements Checkable {
    public CheckableWrapperView(Context context) {
        super(context);
    }

    public boolean isChecked() {
        return ((Checkable) this.mItem).isChecked();
    }

    public void setChecked(boolean checked) {
        ((Checkable) this.mItem).setChecked(checked);
    }

    public void toggle() {
        setChecked(!isChecked());
    }
}
