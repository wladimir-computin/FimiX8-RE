package com.fimi.libperson.ui.web;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.fimi.kernel.percent.PercentRelativeLayout.LayoutParams;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.R;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.NetworkDialog;
import com.xiaomi.account.openauth.XiaomiOAuthConstants;

public class UserProtocolWebViewActivity extends BasePersonActivity {
    private static final String TAG = "UserProtocolWebViewActi";
    private Button mBtnBack;
    NetworkDialog mNetworkDialog;
    private TitleView mTitleView;
    private WebView webView;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_user_protocol_web_view;
    }

    public void initData() {
        initView();
        LayoutParams params = (LayoutParams) this.mTitleView.getLayoutParams();
        params.setMargins(0, this.statusBarHeight + this.marginStatus, 0, 0);
        this.mTitleView.setLayoutParams(params);
    }

    private void initView() {
        final String url = getIntent().getStringExtra("web_url");
        String title = getIntent().getStringExtra("web_title");
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(title);
        this.mBtnBack = (Button) findViewById(R.id.btn_back);
        this.webView = (WebView) findViewById(R.id.web_view);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (AbViewUtil.getScreenHeight(this) >= XiaomiOAuthConstants.SCOPE_MI_CLOUD_CONTACT) {
            this.webView.getSettings().setTextSize(TextSize.SMALLER);
        }
        this.mNetworkDialog = new NetworkDialog(this.mContext, com.fimi.sdk.R.style.network_load_progress_dialog, true);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                Builder builder = new Builder(UserProtocolWebViewActivity.this);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("continue", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                builder.create().show();
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (!((UserProtocolWebViewActivity) UserProtocolWebViewActivity.this.mContext).isFinishing()) {
                    if (newProgress != 100) {
                        try {
                            if (!UserProtocolWebViewActivity.this.mNetworkDialog.isShowing()) {
                                UserProtocolWebViewActivity.this.mNetworkDialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (UserProtocolWebViewActivity.this.mNetworkDialog != null) {
                        try {
                            UserProtocolWebViewActivity.this.mNetworkDialog.dismiss();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });
        this.webView.postDelayed(new Runnable() {
            public void run() {
                UserProtocolWebViewActivity.this.webView.loadUrl(url);
            }
        }, 500);
    }

    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            finish();
        }
    }

    public void doTrans() {
        this.mTitleView.setIvLeftListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (UserProtocolWebViewActivity.this.webView.canGoBack()) {
                    UserProtocolWebViewActivity.this.webView.goBack();
                } else {
                    UserProtocolWebViewActivity.this.finish();
                }
            }
        });
    }
}
