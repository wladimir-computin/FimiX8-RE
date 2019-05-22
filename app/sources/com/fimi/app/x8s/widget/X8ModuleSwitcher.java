package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import com.fimi.app.x8s.R;

@SuppressLint({"AppCompatCustomView"})
public class X8ModuleSwitcher extends ImageButton {
    private static final int[] DRAW_IDS = new int[]{R.drawable.x8_btn_photo_switch_record_select, R.drawable.x8_btn_record_switch_photo_select, R.drawable.x8_btn_photo_switch_record_unclickable, R.drawable.x8_btn_record_switch_photo_unclickable};
    public static final int PHOTO_MODULE_INDEX = 0;
    public static final int PHOTO_MODULE_UNCLICKABLE_INDEX = 2;
    public static final int VIDEO_MODULE_INDEX = 1;
    public static final int VIDEO_MODULE_UNCLICKABLE_INDEX = 3;
    private int moduleIndex;

    public X8ModuleSwitcher(Context context) {
        this(context, null);
    }

    public X8ModuleSwitcher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8ModuleSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.moduleIndex = 0;
        init(context);
    }

    private void init(Context context) {
    }

    public void setCurrentIndex(int i) {
        this.moduleIndex = i;
        setBackgroundResource(DRAW_IDS[this.moduleIndex]);
    }

    public int getCurrentIndex() {
        return this.moduleIndex;
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundResource(DRAW_IDS[this.moduleIndex]);
    }
}
