package com.fimi.app.x8s.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.widget.X8VerticalSeekBar.SlideChangeListener;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.modulestate.X8CameraSettings;
import java.util.concurrent.TimeUnit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class X8VerticalSeekBarValueLayout extends RelativeLayout implements SlideChangeListener {
    private CameraManager cameraManager;
    private int curValue = 10;
    private int lastValue = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private PublishSubject<Integer> mSearchResultsSubject;
    private Subscription mTextWatchSubscription;
    private String prex;
    private int seekBarMax = 30;
    private int seekBarMin = 10;
    private TextView tvValue;
    private X8VerticalSeekBar verticalSeekBar;
    private View view;

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTextWatchSubscription != null && !this.mTextWatchSubscription.isUnsubscribed()) {
            this.mTextWatchSubscription.unsubscribe();
        }
    }

    public X8VerticalSeekBarValueLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.view = LayoutInflater.from(context).inflate(R.layout.x8_vertical_seek_bar_value_layout, this, true);
        this.verticalSeekBar = (X8VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        this.tvValue = (TextView) findViewById(R.id.tv_value);
        this.verticalSeekBar.setProgress(0);
        this.verticalSeekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.verticalSeekBar.setOrientation(0);
        this.view.measure(0, 0);
        this.tvValue.measure(0, 0);
        this.verticalSeekBar.setTextHeight(this.view.getMeasuredHeight(), this.tvValue.getMeasuredHeight());
        this.verticalSeekBar.setOnSlideChangeListener(this);
        this.prex = "x";
        runRxAnroid();
    }

    public void setMinMax(int[] minMax, CameraManager cameraManager) {
        this.seekBarMax = minMax[1];
        this.seekBarMin = minMax[0];
        this.verticalSeekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.cameraManager = cameraManager;
    }

    public void setProgress(int value) {
        if (value > this.seekBarMax) {
            value = this.seekBarMax;
        }
        if (value < this.seekBarMin) {
            value = this.seekBarMin;
        }
        this.curValue = value;
        this.lastValue = value;
        this.prex = this.prex;
        this.verticalSeekBar.setProgress(value - this.seekBarMin);
    }

    public void changeProcess(boolean isDown) {
        int progess = this.verticalSeekBar.getProcess();
        if (isDown) {
            progess--;
            if (progess < 0) {
                progess = 0;
            }
        } else {
            progess++;
            if (progess > this.seekBarMax - this.seekBarMin) {
                progess = this.seekBarMax - this.seekBarMin;
            }
        }
        setProgress(progess + this.seekBarMin);
        sendJsonCmdSetFocuse(this.lastValue);
    }

    public void onStart(X8VerticalSeekBar slideView, int progress) {
    }

    public void onProgress(X8VerticalSeekBar slideView, int progress) {
        LayoutParams lp = new LayoutParams(this.tvValue.getLayoutParams());
        lp.setMargins(this.verticalSeekBar.getDestX(), this.verticalSeekBar.getDestY(), 0, 0);
        this.tvValue.setLayoutParams(lp);
        this.curValue = this.seekBarMin + progress;
        this.tvValue.setText("" + (((float) this.curValue) / 10.0f) + this.prex);
        this.mSearchResultsSubject.onNext(Integer.valueOf(this.curValue));
    }

    public void onStop(X8VerticalSeekBar slideView, int progress) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                X8VerticalSeekBarValueLayout.this.mSearchResultsSubject.onNext(Integer.valueOf(X8VerticalSeekBarValueLayout.this.curValue));
            }
        }, 510);
    }

    public String getCurrentProcess() {
        return "" + (((float) this.curValue) / 10.0f);
    }

    public void sendJsonCmdSetFocuse(int param) {
        final String s = "" + (((float) param) / 10.0f);
        this.cameraManager.setCameraFocuse(s, new JsonUiCallBackListener() {
            public void onComplete(JSONObject rt, Object o) {
                if (rt == null) {
                    X8VerticalSeekBarValueLayout.this.setProgress(X8CameraSettings.getFocuse());
                } else if (((CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class)) != null) {
                    X8CameraSettings.setFocusSetting(s);
                } else {
                    X8VerticalSeekBarValueLayout.this.setProgress(X8CameraSettings.getFocuse());
                }
            }
        });
    }

    private void runRxAnroid() {
        this.mSearchResultsSubject = PublishSubject.create();
        this.mTextWatchSubscription = this.mSearchResultsSubject.throttleLast(500, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
            }

            public void onNext(Integer cities) {
                if (X8VerticalSeekBarValueLayout.this.lastValue != cities.intValue()) {
                    X8VerticalSeekBarValueLayout.this.lastValue = cities.intValue();
                    X8VerticalSeekBarValueLayout.this.sendJsonCmdSetFocuse(X8VerticalSeekBarValueLayout.this.lastValue);
                }
            }
        });
    }
}
