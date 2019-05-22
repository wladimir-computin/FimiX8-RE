package com.fimi.app.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.fimi.android.app.R;
import com.fimi.app.presenter.HostMainPresenter;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.x8sdk.common.Constants;
import router.Router;

public class HostMainHeader extends FrameLayout {
    ImageButton ibtnFeedback = ((ImageButton) findViewById(R.id.ibtn_feedback));
    ImageButton ibtnMore = ((ImageButton) findViewById(R.id.ibtn_more));
    private Context mContext;
    private HostMainPresenter presenter;
    TextView tvDeviceName = ((TextView) findViewById(R.id.tv_device_name));

    public HostMainHeader(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_host_main_header, this);
        FontUtil.changeFontLanTing(context.getAssets(), this.tvDeviceName);
        this.ibtnMore.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((HostNewMainActivity) context).stopAnim();
                ((HostNewMainActivity) context).startActivityForResult((Intent) Router.invoke(context, "activity://person.setting"), Constants.A12_TCP_CMD_PORT);
            }
        });
        this.ibtnFeedback.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HostMainHeader.this.presenter.gotoTeacher("activity://gh2.teacher");
            }
        });
    }

    public void setDeviceName(int resid) {
        this.tvDeviceName.setText(resid);
    }

    public void setPositon(int positon) {
        if (com.fimi.kernel.Constants.productType == ProductEnum.GH2) {
            this.ibtnFeedback.setImageResource(R.drawable.teacher_btn_selector);
            this.ibtnFeedback.setVisibility(0);
            return;
        }
        this.ibtnFeedback.setVisibility(8);
    }

    public void setPresenter(HostMainPresenter presenter) {
        this.presenter = presenter;
    }
}
