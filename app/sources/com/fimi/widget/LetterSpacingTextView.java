package com.fimi.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class LetterSpacingTextView extends TextView {
    private float spacing = 0.0f;

    public class Spacing {
        public static final float NORMAL = 0.0f;
    }

    public LetterSpacingTextView(Context context) {
        super(context);
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = (float) dip2px(spacing);
        applySpacing();
    }

    public int dip2px(float dipValue) {
        return (int) ((dipValue * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    private void applySpacing() {
        CharSequence originalText = getText();
        if (this != null && originalText != null) {
            int i;
            StringBuilder builder = new StringBuilder();
            for (i = 0; i < originalText.length(); i++) {
                builder.append(originalText.charAt(i));
                if (i + 1 < originalText.length()) {
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
