package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.fimi.app.x8s.R;

public class X8ShutterImageView extends TwoStateImageView {
    public static final int DELAYED_PHOTO_MODULE_INDEX = 3;
    public static final int DELAYED_VIDEO_MODULE_INDEX = 4;
    private static final int DISABLED_ALPHA = 102;
    private static final int[] DRAW_IDS = new int[]{R.drawable.x8_btn_photo_select, R.drawable.x8_main_record_state, R.drawable.x8_main_recording_state, R.drawable.x8_btn_delayed_photo_select, R.drawable.x8_btn_delayed_record_select, R.drawable.x8_btn_panorama_phototaking_one_select, R.drawable.x8_btn_panorama_phototaking_two_select, R.drawable.x8_btn_panorama_phototaking_three_select};
    private static final int ENABLED_ALPHA = 255;
    public static final int PANORAMA_PHOTOTAKING_ONE = 5;
    public static final int PANORAMA_PHOTOTAKING_THREE = 7;
    public static final int PANORAMA_PHOTOTAKING_TWO = 6;
    public static final int PHOTO_MODULE_INDEX = 0;
    public static final int STATE_IDEL = 0;
    public static final int STATE_RECORDING = 1;
    public static final int VIDEO_MODULE_INDEX = 1;
    public static int stateIndex = 0;
    private int moduleIndex;

    public X8ShutterImageView(Context context) {
        this(context, null);
    }

    public X8ShutterImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8ShutterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.moduleIndex = 0;
        init(context);
    }

    private void init(Context context) {
    }

    public void setCurrentIndex(int i, int state) {
        this.moduleIndex = i;
        stateIndex = state;
        if (1 == state) {
            setImageResource(DRAW_IDS[2]);
        } else {
            setImageResource(DRAW_IDS[i]);
        }
    }

    public int getStateIndex() {
        return stateIndex;
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(DRAW_IDS[this.moduleIndex]);
    }
}
