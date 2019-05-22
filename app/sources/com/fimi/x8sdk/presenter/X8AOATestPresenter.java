package com.fimi.x8sdk.presenter;

import android.os.Handler;
import android.os.Message;
import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.x8sdk.command.AoaTestColletion;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.ivew.IAOATestView;

public class X8AOATestPresenter extends BasePresenter {
    IAOATestView iaoaTestView;
    private boolean isInterrupt;
    private boolean isRunning;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                X8AOATestPresenter.this.iaoaTestView.showSendDataLen(X8AOATestPresenter.this.sendDataLen);
                X8AOATestPresenter.this.iaoaTestView.showRecDataLen(X8AOATestPresenter.this.recvDataLen);
                X8AOATestPresenter.this.mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
    private long recvDataLen = 0;
    private long sendDataLen = 0;
    VideodDataListener videodDataListener = new VideodDataListener() {
        public void onRawdataCallBack(byte[] bytes) {
            if (X8AOATestPresenter.this.iaoaTestView != null) {
                X8AOATestPresenter.this.recvDataLen = X8AOATestPresenter.this.recvDataLen + ((long) bytes.length);
                X8AOATestPresenter.this.iaoaTestView.vedeoData(bytes);
            }
        }
    };

    public void clearData() {
        this.recvDataLen = 0;
        this.sendDataLen = 0;
    }

    public X8AOATestPresenter(IAOATestView iaoaTestView) {
        this.iaoaTestView = iaoaTestView;
        addNoticeListener(this.videodDataListener);
        this.mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    public void killDataChanel() {
        this.isInterrupt = false;
        this.isRunning = false;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void startReceivedData() {
        sendCmd(new AoaTestColletion().getTestContent(new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 0, (byte) 0, (byte) 10, (byte) 0, (byte) 1}));
    }

    public void sendContent(final String content, boolean isLoop, final long time) {
        this.isInterrupt = isLoop;
        if (isLoop) {
            this.isRunning = true;
        }
        new Thread(new Runnable() {
            public void run() {
                do {
                    X8AOATestPresenter.this.sendCmd(new AoaTestColletion().getTestContent(content));
                    X8AOATestPresenter.this.sendDataLen = X8AOATestPresenter.this.sendDataLen + ((long) content.getBytes().length);
                    if (time > 0) {
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } while (X8AOATestPresenter.this.isInterrupt);
            }
        }).start();
    }
}
