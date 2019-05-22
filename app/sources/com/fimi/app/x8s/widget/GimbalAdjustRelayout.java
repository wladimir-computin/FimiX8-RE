package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.app.x8s.R;

public class GimbalAdjustRelayout extends RelativeLayout {
    Button btnAdd = ((Button) findViewById(R.id.btn_add));
    Button btnSub = ((Button) findViewById(R.id.btn_sub));
    EditText etxValue = ((EditText) findViewById(R.id.etx_value));
    Context mContext;
    TextView tvGimbalModel = ((TextView) findViewById(R.id.tv_gimbal_model));

    public GimbalAdjustRelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_gimable_adjust, this);
    }

    public TextView getTvGimbalModel() {
        return this.tvGimbalModel;
    }

    public Button getBtnSub() {
        return this.btnSub;
    }

    public Button getBtnAdd() {
        return this.btnAdd;
    }

    public EditText getEtxValue() {
        return this.etxValue;
    }
}
