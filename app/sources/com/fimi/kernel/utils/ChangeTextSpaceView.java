package com.fimi.kernel.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class ChangeTextSpaceView extends TextView {
    private CharSequence originalText = "";
    private float spacing = 0.0f;

    public class Spacing {
        public static final float NORMAL = 0.0f;
    }

    public ChangeTextSpaceView(Context context) {
        super(context);
    }

    public ChangeTextSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeTextSpaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    public void setText(CharSequence text, BufferType type) {
        this.originalText = text;
        applySpacing();
    }

    public CharSequence getText() {
        return this.originalText;
    }

    private void applySpacing() {
        if (this != null && this.originalText != null) {
            int i;
            StringBuilder builder = new StringBuilder();
            for (i = 0; i < this.originalText.length(); i++) {
                builder.append(this.originalText.charAt(i));
                if (i + 1 < this.originalText.length()) {
                    builder.append(" ");
                }
            }
            SpannableString finalText = new SpannableString(builder.toString());
            if (builder.toString().length() > 1) {
                for (i = 1; i < builder.toString().length(); i += 2) {
                    finalText.setSpan(new ScaleXSpan((this.spacing + 1.0f) / 10.0f), i, i + 1, 33);
                }
            }
            super.setText(finalText, BufferType.SPANNABLE);
        }
    }
}
