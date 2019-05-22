package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import java.util.ArrayList;
import java.util.List;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class LabelsView extends ViewGroup implements OnClickListener {
    private static final String KEY_BG_RES_ID_STATE = "key_bg_res_id_state";
    private static final String KEY_COMPULSORY_LABELS_STATE = "key_select_compulsory_state";
    private static final int KEY_DATA = R.id.tag_key_data;
    private static final String KEY_LABELS_STATE = "key_labels_state";
    private static final String KEY_LINE_MARGIN_STATE = "key_line_margin_state";
    private static final String KEY_MAX_SELECT_STATE = "key_max_select_state";
    private static final String KEY_PADDING_STATE = "key_padding_state";
    private static final int KEY_POSITION = R.id.tag_key_position;
    private static final String KEY_SELECT_LABELS_STATE = "key_select_labels_state";
    private static final String KEY_SELECT_TYPE_STATE = "key_select_type_state";
    private static final String KEY_SUPER_STATE = "key_super_state";
    private static final String KEY_TEXT_COLOR_STATE = "key_text_color_state";
    private static final String KEY_TEXT_SIZE_STATE = "key_text_size_state";
    private static final String KEY_WORD_MARGIN_STATE = "key_word_margin_state";
    private ArrayList<Integer> mCompulsorys = new ArrayList();
    private Context mContext;
    private Drawable mLabelBg;
    private OnLabelClickListener mLabelClickListener;
    private OnLabelSelectChangeListener mLabelSelectChangeListener;
    private ArrayList<Object> mLabels = new ArrayList();
    private int mLineMargin;
    private int mMaxSelect;
    private ArrayList<Integer> mSelectLabels = new ArrayList();
    private SelectType mSelectType;
    private ColorStateList mTextColor;
    private int mTextPaddingBottom;
    private int mTextPaddingLeft;
    private int mTextPaddingRight;
    private int mTextPaddingTop;
    private float mTextSize;
    private int mWordMargin;

    public interface LabelTextProvider<T> {
        CharSequence getLabelText(TextView textView, int i, T t);
    }

    public interface OnLabelClickListener {
        void onLabelClick(TextView textView, Object obj, int i);
    }

    public interface OnLabelSelectChangeListener {
        void onLabelSelectChange(TextView textView, Object obj, boolean z, int i);
    }

    public enum SelectType {
        NONE(1),
        SINGLE(2),
        SINGLE_IRREVOCABLY(3),
        MULTI(4);
        
        int value;

        private SelectType(int value) {
            this.value = value;
        }

        static SelectType get(int value) {
            switch (value) {
                case 1:
                    return NONE;
                case 2:
                    return SINGLE;
                case 3:
                    return SINGLE_IRREVOCABLY;
                case 4:
                    return MULTI;
                default:
                    return NONE;
            }
        }
    }

    public LabelsView(Context context) {
        super(context);
        this.mContext = context;
    }

    public LabelsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        getAttrs(context, attrs);
    }

    public LabelsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.labels_view);
            this.mSelectType = SelectType.get(mTypedArray.getInt(R.styleable.labels_view_selectType, 1));
            this.mMaxSelect = mTypedArray.getInteger(R.styleable.labels_view_maxSelect, 0);
            this.mTextColor = mTypedArray.getColorStateList(R.styleable.labels_view_labelTextColor);
            this.mTextSize = mTypedArray.getDimension(R.styleable.labels_view_labelTextSize, (float) sp2px(context, 14.0f));
            this.mTextPaddingLeft = mTypedArray.getDimensionPixelOffset(R.styleable.labels_view_labelTextPaddingLeft, 0);
            this.mTextPaddingTop = mTypedArray.getDimensionPixelOffset(R.styleable.labels_view_labelTextPaddingTop, 0);
            this.mTextPaddingRight = mTypedArray.getDimensionPixelOffset(R.styleable.labels_view_labelTextPaddingRight, 0);
            this.mTextPaddingBottom = mTypedArray.getDimensionPixelOffset(R.styleable.labels_view_labelTextPaddingBottom, 0);
            this.mLineMargin = mTypedArray.getDimensionPixelOffset(R.styleable.labels_view_lineMargin, 0);
            this.mWordMargin = mTypedArray.getDimensionPixelOffset(R.styleable.labels_view_wordMargin, 0);
            int labelBgResId = mTypedArray.getResourceId(R.styleable.labels_view_labelBackground, 0);
            if (labelBgResId != 0) {
                this.mLabelBg = getResources().getDrawable(labelBgResId);
            } else {
                this.mLabelBg = new ColorDrawable(mTypedArray.getColor(R.styleable.labels_view_labelBackground, 0));
            }
            mTypedArray.recycle();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxWidth = (MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight();
        int contentHeight = 0;
        int lineWidth = 0;
        int maxLineWidth = 0;
        int maxItemHeight = 0;
        boolean begin = true;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if (begin) {
                begin = false;
            } else {
                lineWidth += this.mWordMargin;
            }
            if (maxWidth <= view.getMeasuredWidth() + lineWidth) {
                contentHeight = (contentHeight + this.mLineMargin) + maxItemHeight;
                maxItemHeight = 0;
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineWidth = 0;
                begin = true;
            }
            maxItemHeight = Math.max(maxItemHeight, view.getMeasuredHeight());
            lineWidth += view.getMeasuredWidth();
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec, Math.max(maxLineWidth, lineWidth)), measureHeight(heightMeasureSpec, contentHeight + maxItemHeight));
    }

    private int measureWidth(int measureSpec, int contentWidth) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
            result = specSize;
        } else {
            result = (getPaddingLeft() + contentWidth) + getPaddingRight();
            if (specMode == Integer.MIN_VALUE) {
                result = Math.min(result, specSize);
            }
        }
        return Math.max(result, getSuggestedMinimumWidth());
    }

    private int measureHeight(int measureSpec, int contentHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
            result = specSize;
        } else {
            result = (getPaddingTop() + contentHeight) + getPaddingBottom();
            if (specMode == Integer.MIN_VALUE) {
                result = Math.min(result, specSize);
            }
        }
        return Math.max(result, getSuggestedMinimumHeight());
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int contentWidth = right - left;
        int maxItemHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (contentWidth < (view.getMeasuredWidth() + x) + getPaddingRight()) {
                x = getPaddingLeft();
                y = (y + this.mLineMargin) + maxItemHeight;
                maxItemHeight = 0;
            }
            view.layout(x, y, view.getMeasuredWidth() + x, view.getMeasuredHeight() + y);
            x = (x + view.getMeasuredWidth()) + this.mWordMargin;
            maxItemHeight = Math.max(maxItemHeight, view.getMeasuredHeight());
        }
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        if (this.mTextColor != null) {
            bundle.putParcelable(KEY_TEXT_COLOR_STATE, this.mTextColor);
        }
        bundle.putFloat(KEY_TEXT_SIZE_STATE, this.mTextSize);
        bundle.putIntArray(KEY_PADDING_STATE, new int[]{this.mTextPaddingLeft, this.mTextPaddingTop, this.mTextPaddingRight, this.mTextPaddingBottom});
        bundle.putInt(KEY_WORD_MARGIN_STATE, this.mWordMargin);
        bundle.putInt(KEY_LINE_MARGIN_STATE, this.mLineMargin);
        bundle.putInt(KEY_SELECT_TYPE_STATE, this.mSelectType.value);
        bundle.putInt(KEY_MAX_SELECT_STATE, this.mMaxSelect);
        if (!this.mSelectLabels.isEmpty()) {
            bundle.putIntegerArrayList(KEY_SELECT_LABELS_STATE, this.mSelectLabels);
        }
        if (!this.mCompulsorys.isEmpty()) {
            bundle.putIntegerArrayList(KEY_COMPULSORY_LABELS_STATE, this.mCompulsorys);
        }
        return bundle;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE));
            ColorStateList color = (ColorStateList) bundle.getParcelable(KEY_TEXT_COLOR_STATE);
            if (color != null) {
                setLabelTextColor(color);
            }
            setLabelTextSize(bundle.getFloat(KEY_TEXT_SIZE_STATE, this.mTextSize));
            int[] padding = bundle.getIntArray(KEY_PADDING_STATE);
            if (padding != null && padding.length == 4) {
                setLabelTextPadding(padding[0], padding[1], padding[2], padding[3]);
            }
            setWordMargin(bundle.getInt(KEY_WORD_MARGIN_STATE, this.mWordMargin));
            setLineMargin(bundle.getInt(KEY_LINE_MARGIN_STATE, this.mLineMargin));
            setSelectType(SelectType.get(bundle.getInt(KEY_SELECT_TYPE_STATE, this.mSelectType.value)));
            setMaxSelect(bundle.getInt(KEY_MAX_SELECT_STATE, this.mMaxSelect));
            List compulsory = bundle.getIntegerArrayList(KEY_COMPULSORY_LABELS_STATE);
            if (!(compulsory == null || compulsory.isEmpty())) {
                setCompulsorys(compulsory);
            }
            ArrayList<Integer> selectLabel = bundle.getIntegerArrayList(KEY_SELECT_LABELS_STATE);
            if (selectLabel != null && !selectLabel.isEmpty()) {
                int size = selectLabel.size();
                int[] positions = new int[size];
                for (int i = 0; i < size; i++) {
                    positions[i] = ((Integer) selectLabel.get(i)).intValue();
                }
                setSelects(positions);
                return;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setLabels(List<String> labels) {
        setLabels(labels, new LabelTextProvider<String>() {
            public CharSequence getLabelText(TextView label, int position, String data) {
                return data.trim();
            }
        });
    }

    public <T> void setLabels(List<T> labels, LabelTextProvider<T> provider) {
        innerClearAllSelect();
        removeAllViews();
        this.mLabels.clear();
        if (labels != null) {
            this.mLabels.addAll(labels);
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                addLabel(labels.get(i), i, provider);
            }
            ensureLabelClickable();
        }
        if (this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
            setSelects(0);
        }
    }

    public <T> List<T> getLabels() {
        return this.mLabels;
    }

    private <T> void addLabel(T data, int position, LabelTextProvider<T> provider) {
        TextView label = new TextView(this.mContext);
        label.setPadding(this.mTextPaddingLeft, this.mTextPaddingTop, this.mTextPaddingRight, this.mTextPaddingBottom);
        label.setTextSize(0, this.mTextSize);
        label.setTextColor(this.mTextColor != null ? this.mTextColor : ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        label.setBackgroundDrawable(this.mLabelBg.getConstantState().newDrawable());
        label.setTag(KEY_DATA, data);
        label.setTag(KEY_POSITION, Integer.valueOf(position));
        label.setOnClickListener(this);
        addView(label);
        label.setText(provider.getLabelText(label, position, data));
    }

    private void ensureLabelClickable() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView label = (TextView) getChildAt(i);
            boolean z = (this.mLabelClickListener == null && this.mSelectType == SelectType.NONE) ? false : true;
            label.setClickable(z);
        }
    }

    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView label = (TextView) v;
            if (this.mSelectType != SelectType.NONE) {
                if (label.isSelected()) {
                    if (!(this.mSelectType == SelectType.SINGLE_IRREVOCABLY || this.mCompulsorys.contains((Integer) label.getTag(KEY_POSITION)))) {
                        setLabelSelect(label, false);
                    }
                } else if (this.mSelectType == SelectType.SINGLE || this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
                    innerClearAllSelect();
                    setLabelSelect(label, true);
                } else if (this.mSelectType == SelectType.MULTI && (this.mMaxSelect <= 0 || this.mMaxSelect > this.mSelectLabels.size())) {
                    setLabelSelect(label, true);
                }
            }
            if (this.mLabelClickListener != null) {
                this.mLabelClickListener.onLabelClick(label, label.getTag(KEY_DATA), ((Integer) label.getTag(KEY_POSITION)).intValue());
            }
        }
    }

    private void setLabelSelect(TextView label, boolean isSelect) {
        if (label.isSelected() != isSelect) {
            label.setSelected(isSelect);
            if (isSelect) {
                this.mSelectLabels.add((Integer) label.getTag(KEY_POSITION));
            } else {
                this.mSelectLabels.remove((Integer) label.getTag(KEY_POSITION));
            }
            if (this.mLabelSelectChangeListener != null) {
                this.mLabelSelectChangeListener.onLabelSelectChange(label, label.getTag(KEY_DATA), isSelect, ((Integer) label.getTag(KEY_POSITION)).intValue());
            }
        }
    }

    public void clearAllSelect() {
        if (this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
            return;
        }
        if (this.mSelectType != SelectType.MULTI || this.mCompulsorys.isEmpty()) {
            innerClearAllSelect();
        } else {
            clearNotCompulsorySelect();
        }
    }

    private void innerClearAllSelect() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            setLabelSelect((TextView) getChildAt(i), false);
        }
        this.mSelectLabels.clear();
    }

    private void clearNotCompulsorySelect() {
        int count = getChildCount();
        List<Integer> temps = new ArrayList();
        for (int i = 0; i < count; i++) {
            if (!this.mCompulsorys.contains(Integer.valueOf(i))) {
                setLabelSelect((TextView) getChildAt(i), false);
                temps.add(Integer.valueOf(i));
            }
        }
        this.mSelectLabels.removeAll(temps);
    }

    public void setSelects(List<Integer> positions) {
        if (positions != null) {
            int size = positions.size();
            int[] ps = new int[size];
            for (int i = 0; i < size; i++) {
                ps[i] = ((Integer) positions.get(i)).intValue();
            }
            setSelects(ps);
        }
    }

    public void setSelects(int... positions) {
        if (this.mSelectType != SelectType.NONE) {
            TextView label;
            ArrayList<TextView> selectLabels = new ArrayList();
            int count = getChildCount();
            int size = (this.mSelectType == SelectType.SINGLE || this.mSelectType == SelectType.SINGLE_IRREVOCABLY) ? 1 : this.mMaxSelect;
            for (int p : positions) {
                if (p < count) {
                    label = (TextView) getChildAt(p);
                    if (!selectLabels.contains(label)) {
                        setLabelSelect(label, true);
                        selectLabels.add(label);
                    }
                    if (size > 0 && selectLabels.size() == size) {
                        break;
                    }
                }
            }
            for (int i = 0; i < count; i++) {
                label = (TextView) getChildAt(i);
                if (!selectLabels.contains(label)) {
                    setLabelSelect(label, false);
                }
            }
        }
    }

    public void setCompulsorys(List<Integer> positions) {
        if (this.mSelectType == SelectType.MULTI && positions != null) {
            this.mCompulsorys.clear();
            this.mCompulsorys.addAll(positions);
            innerClearAllSelect();
            setSelects((List) positions);
        }
    }

    public void setCompulsorys(int... positions) {
        if (this.mSelectType == SelectType.MULTI && positions != null) {
            List ps = new ArrayList(positions.length);
            for (int i : positions) {
                ps.add(Integer.valueOf(i));
            }
            setCompulsorys(ps);
        }
    }

    public List<Integer> getCompulsorys() {
        return this.mCompulsorys;
    }

    public void clearCompulsorys() {
        if (this.mSelectType == SelectType.MULTI && !this.mCompulsorys.isEmpty()) {
            this.mCompulsorys.clear();
            innerClearAllSelect();
        }
    }

    public List<Integer> getSelectLabels() {
        return this.mSelectLabels;
    }

    public <T> List<T> getSelectLabelDatas() {
        List<T> list = new ArrayList();
        int size = this.mSelectLabels.size();
        for (int i = 0; i < size; i++) {
            Object data = getChildAt(((Integer) this.mSelectLabels.get(i)).intValue()).getTag(KEY_DATA);
            if (data != null) {
                list.add(data);
            }
        }
        return list;
    }

    public void setLabelBackgroundResource(int resId) {
        setLabelBackgroundDrawable(getResources().getDrawable(resId));
    }

    public void setLabelBackgroundColor(int color) {
        setLabelBackgroundDrawable(new ColorDrawable(color));
    }

    public void setLabelBackgroundDrawable(Drawable drawable) {
        this.mLabelBg = drawable;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ((TextView) getChildAt(i)).setBackgroundDrawable(this.mLabelBg.getConstantState().newDrawable());
        }
    }

    public void setLabelTextPadding(int left, int top, int right, int bottom) {
        if (this.mTextPaddingLeft != left || this.mTextPaddingTop != top || this.mTextPaddingRight != right || this.mTextPaddingBottom != bottom) {
            this.mTextPaddingLeft = left;
            this.mTextPaddingTop = top;
            this.mTextPaddingRight = right;
            this.mTextPaddingBottom = bottom;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                ((TextView) getChildAt(i)).setPadding(left, top, right, bottom);
            }
        }
    }

    public int getTextPaddingLeft() {
        return this.mTextPaddingLeft;
    }

    public int getTextPaddingTop() {
        return this.mTextPaddingTop;
    }

    public int getTextPaddingRight() {
        return this.mTextPaddingRight;
    }

    public int getTextPaddingBottom() {
        return this.mTextPaddingBottom;
    }

    public void setLabelTextSize(float size) {
        if (this.mTextSize != size) {
            this.mTextSize = size;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                ((TextView) getChildAt(i)).setTextSize(0, size);
            }
        }
    }

    public float getLabelTextSize() {
        return this.mTextSize;
    }

    public void setLabelTextColor(int color) {
        setLabelTextColor(ColorStateList.valueOf(color));
    }

    public void setLabelTextColor(ColorStateList color) {
        this.mTextColor = color;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ((TextView) getChildAt(i)).setTextColor(this.mTextColor != null ? this.mTextColor : ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        }
    }

    public ColorStateList getLabelTextColor() {
        return this.mTextColor;
    }

    public void setLineMargin(int margin) {
        if (this.mLineMargin != margin) {
            this.mLineMargin = margin;
            requestLayout();
        }
    }

    public int getLineMargin() {
        return this.mLineMargin;
    }

    public void setWordMargin(int margin) {
        if (this.mWordMargin != margin) {
            this.mWordMargin = margin;
            requestLayout();
        }
    }

    public int getWordMargin() {
        return this.mWordMargin;
    }

    public void setSelectType(SelectType selectType) {
        if (this.mSelectType != selectType) {
            this.mSelectType = selectType;
            innerClearAllSelect();
            if (this.mSelectType == SelectType.SINGLE_IRREVOCABLY) {
                setSelects(0);
            }
            if (this.mSelectType != SelectType.MULTI) {
                this.mCompulsorys.clear();
            }
            ensureLabelClickable();
        }
    }

    public SelectType getSelectType() {
        return this.mSelectType;
    }

    public void setMaxSelect(int maxSelect) {
        if (this.mMaxSelect != maxSelect) {
            this.mMaxSelect = maxSelect;
            if (this.mSelectType == SelectType.MULTI) {
                innerClearAllSelect();
            }
        }
    }

    public int getMaxSelect() {
        return this.mMaxSelect;
    }

    public void setOnLabelClickListener(OnLabelClickListener l) {
        this.mLabelClickListener = l;
        ensureLabelClickable();
    }

    public void setOnLabelSelectChangeListener(OnLabelSelectChangeListener l) {
        this.mLabelSelectChangeListener = l;
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(2, spVal, context.getResources().getDisplayMetrics());
    }
}
