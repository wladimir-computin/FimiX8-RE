package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.modulestate.StateManager;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8BatteryReturnLandingView extends View {
    private X8WithTimeDoubleCustomDialog dialogLanding;
    private X8WithTimeDoubleCustomDialog dialogReturn;
    private int h;
    private boolean hasSHowLanding;
    private boolean hasShowReturn;
    private Bitmap imgLanding;
    private Bitmap imgReturn;
    private int landingCapacity;
    private Paint paint;
    private int power;
    private int rhtCapacity;
    private int w;
    private X8sMainActivity x8sMainActivity;

    public X8BatteryReturnLandingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSurface(context);
    }

    private void initSurface(Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        initBitmap();
    }

    private void initBitmap() {
        this.imgReturn = createBitMap(R.drawable.x8_img_top_return_home_battery);
        this.imgLanding = createBitMap(R.drawable.x8_img_top_landing_battery);
        this.h = this.imgLanding.getHeight();
        this.w = this.imgLanding.getWidth();
    }

    private Bitmap createBitMap(int resID) {
        return BitmapFactory.decodeResource(getResources(), resID);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recyleBitMap(this.imgReturn);
        recyleBitMap(this.imgLanding);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!StateManager.getInstance().getX8Drone().isOnGround() && getWidth() > 0) {
            int start;
            float x = ((float) (getWidth() * 1624)) / 1920.0f;
            if (this.rhtCapacity > 0) {
                if (this.rhtCapacity <= 50) {
                    start = (int) ((((float) this.rhtCapacity) * x) / 100.0f);
                } else {
                    start = (int) (((float) ((int) (((float) getWidth()) - (0.5f * x)))) + ((((float) (this.rhtCapacity - 50)) / 100.0f) * x));
                }
                canvas.drawBitmap(this.imgReturn, (float) ((int) (((float) start) - (((float) this.w) * 0.05f))), 0.0f, this.paint);
            }
            if (this.landingCapacity > 0) {
                if (this.landingCapacity <= 50) {
                    start = (int) ((((float) this.landingCapacity) * x) / 100.0f);
                } else {
                    start = (int) (((float) ((int) (((float) getWidth()) - (0.5f * x)))) + ((((float) (this.landingCapacity - 50)) / 100.0f) * x));
                }
                canvas.drawBitmap(this.imgLanding, (float) ((int) (((float) start) - (((float) this.w) * 0.05f))), 0.0f, this.paint);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), measureHeight(heightMeasureSpec, this.h));
    }

    private void recyleBitMap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    private int measureHeight(int measureSpec, int height) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE) {
            return size;
        }
        int result = height;
        if (mode == Integer.MIN_VALUE) {
            return Math.min(result, size);
        }
        return result;
    }

    public void setPercent(int landing, int rht, int total, int power) {
        int mode;
        if (landing < rht) {
            landing = 0;
        }
        this.landingCapacity = (int) (((((float) landing) * 1.0f) / ((float) total)) * 100.0f);
        this.rhtCapacity = (int) (((((float) rht) * 1.0f) / ((float) total)) * 100.0f);
        this.power = power;
        if (this.hasShowReturn && StateManager.getInstance().getX8Drone().isOnGround()) {
            this.hasShowReturn = false;
        }
        if (this.hasSHowLanding && StateManager.getInstance().getX8Drone().isOnGround()) {
            this.hasSHowLanding = false;
        }
        if (power == this.rhtCapacity && !this.hasShowReturn && GlobalConfig.getInstance().isLowReturn() && StateManager.getInstance().getX8Drone().isInSky()) {
            mode = StateManager.getInstance().getX8Drone().getCtrlMode();
            if (!(mode == 7 || mode == 8 || mode == 3)) {
                showReturn();
            }
        }
        if (power == this.landingCapacity && !this.hasSHowLanding && GlobalConfig.getInstance().isLowLanding() && StateManager.getInstance().getX8Drone().isInSky()) {
            mode = StateManager.getInstance().getX8Drone().getCtrlMode();
            if (!(mode == 7 || mode == 8 || mode == 3)) {
                showLanding();
            }
        }
        postInvalidate();
    }

    public void resetByDidconnect() {
        if (this.landingCapacity != 0 || this.rhtCapacity != 0) {
            this.landingCapacity = 0;
            this.rhtCapacity = 0;
            if (this.dialogLanding != null) {
                this.dialogLanding.dismiss();
                this.dialogLanding = null;
            }
            if (this.dialogReturn != null) {
                this.dialogReturn.dismiss();
                this.dialogReturn = null;
            }
            postInvalidate();
        }
    }

    public void showLanding() {
        if (this.dialogLanding == null || !this.dialogLanding.isShowing()) {
            this.dialogLanding = new X8WithTimeDoubleCustomDialog(getContext(), getContext().getString(R.string.x8_battery_low_landing_title), getContext().getString(R.string.x8_ai_fly_land_title), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8BatteryReturnLandingView.this.onLandingClick();
                }
            });
            this.hasSHowLanding = true;
            this.dialogLanding.setCanceledOnTouchOutside(false);
            this.dialogLanding.show();
        }
    }

    public void showReturn() {
        if (this.dialogReturn == null || !this.dialogReturn.isShowing()) {
            this.dialogReturn = new X8WithTimeDoubleCustomDialog(getContext(), getContext().getString(R.string.x8_battery_low_return_title), getContext().getString(R.string.x8_ai_fly_return_home_title), new onDialogButtonClickListener() {
                public void onLeft() {
                }

                public void onRight() {
                    X8BatteryReturnLandingView.this.onRetureHomeClick();
                }
            });
            this.hasShowReturn = true;
            this.dialogReturn.setCanceledOnTouchOutside(false);
            this.dialogReturn.show();
        }
    }

    public void setX8sMainActivity(X8sMainActivity x8sMainActivity) {
        this.x8sMainActivity = x8sMainActivity;
    }

    public void onRetureHomeClick() {
        this.x8sMainActivity.getFcManager().setAiRetureHome(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess() && X8BatteryReturnLandingView.this.dialogReturn != null && X8BatteryReturnLandingView.this.dialogReturn.isShowing()) {
                    X8BatteryReturnLandingView.this.dialogReturn.dismiss();
                }
            }
        });
    }

    public void onLandingClick() {
        this.x8sMainActivity.getFcManager().land(new UiCallBackListener() {
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess() && X8BatteryReturnLandingView.this.dialogLanding != null && X8BatteryReturnLandingView.this.dialogLanding.isShowing()) {
                    X8BatteryReturnLandingView.this.dialogLanding.dismiss();
                }
            }
        });
    }
}
