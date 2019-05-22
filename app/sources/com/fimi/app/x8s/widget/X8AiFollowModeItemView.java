package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.fimi.app.x8s.R;

public class X8AiFollowModeItemView extends RelativeLayout implements OnClickListener {
    private int index = 0;
    private boolean isOpen = true;
    private final ImageView item1;
    private final ImageView item2;
    private final ImageView item3;
    private OnModeSelectListner listener;
    private int[] mode = new int[]{2, 1, 0};
    private final ImageView openClose;
    private int[] res = new int[]{R.drawable.x8_btn_ai_follow_lockup, R.drawable.x8_btn_ai_follow_parallel, R.drawable.x8_btn_ai_follow_normal};

    public interface OnModeSelectListner {
        void onModeSelect(int i);
    }

    public X8AiFollowModeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.x8_ai_follow_item_mode_layout, this, true);
        this.openClose = (ImageView) findViewById(R.id.img_open_close);
        this.item1 = (ImageView) findViewById(R.id.img_item1);
        this.item2 = (ImageView) findViewById(R.id.img_item2);
        this.item3 = (ImageView) findViewById(R.id.img_item3);
        this.openClose.setOnClickListener(this);
        this.item1.setOnClickListener(this);
        this.item2.setOnClickListener(this);
        this.item3.setSelected(true);
    }

    public void setListener(OnModeSelectListner listener) {
        this.listener = listener;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_open_close) {
            if (this.isOpen) {
                this.isOpen = false;
                this.openClose.setBackgroundResource(R.drawable.x8_btn_ai_follow_mode_open);
                this.item1.setVisibility(8);
                this.item2.setVisibility(8);
                return;
            }
            this.isOpen = true;
            this.openClose.setBackgroundResource(R.drawable.x8_btn_ai_follow_mode_close);
            this.item1.setVisibility(0);
            this.item2.setVisibility(0);
        } else if (id == R.id.img_item1) {
            this.listener.onModeSelect(this.mode[0]);
        } else if (id == R.id.img_item2) {
            this.listener.onModeSelect(this.mode[1]);
        } else {
            if (id == R.id.img_item3) {
            }
        }
    }

    public void switchItem() {
        int temp = this.mode[2];
        this.mode[2] = this.mode[this.index];
        this.mode[this.index] = temp;
        temp = this.res[2];
        this.res[2] = this.res[this.index];
        this.res[this.index] = temp;
        for (int i = 0; i < this.mode.length; i++) {
            switch (i) {
                case 0:
                    this.item1.setBackgroundResource(this.res[i]);
                    break;
                case 1:
                    this.item2.setBackgroundResource(this.res[i]);
                    break;
                case 2:
                    this.item3.setBackgroundResource(this.res[i]);
                    break;
                default:
                    break;
            }
        }
        Log.i("istep", " " + this.mode[2]);
    }

    public void findIndexByMode(int type) {
        int i = 0;
        while (i < this.mode.length) {
            if (type != this.mode[i]) {
                i++;
            } else if (i != 2) {
                this.index = i;
                switchItem();
                return;
            } else {
                return;
            }
        }
    }
}
