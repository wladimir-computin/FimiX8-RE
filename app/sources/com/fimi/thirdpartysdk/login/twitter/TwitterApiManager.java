package com.fimi.thirdpartysdk.login.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class TwitterApiManager {
    final WeakReference<Context> activityRef;
    volatile TwitterAuthClient authClient;

    public interface LoginCallback {
        void onFailure();

        void onSuccess(Map<String, String> map);
    }

    public TwitterApiManager(Context context) {
        this.activityRef = new WeakReference(context);
    }

    public void login(final LoginCallback loginCallback) {
        getTwitterAuthClient().authorize((Activity) this.activityRef.get(), new Callback<TwitterSession>() {
            public void success(Result<TwitterSession> result) {
                TwitterApiManager.this.getTwitterUserInfo(((TwitterSession) result.data).getUserName(), ((TwitterSession) result.data).getUserId(), loginCallback);
            }

            public void failure(TwitterException e) {
                e.printStackTrace();
                loginCallback.onFailure();
            }
        });
    }

    /* Access modifiers changed, original: 0000 */
    public TwitterAuthClient getTwitterAuthClient() {
        if (this.authClient == null) {
            this.authClient = new TwitterAuthClient();
        }
        return this.authClient;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getTwitterAuthClient().getRequestCode()) {
            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getTwitterUserInfo(String name, final long userId, final LoginCallback callback) {
        new TwitterApiClient((TwitterSession) TwitterCore.getInstance().getSessionManager().getActiveSession()).getAccountService().verifyCredentials(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false)).enqueue(new Callback<User>() {
            public void success(Result<User> result) {
                User data = result.data;
                String profileImageUrl = data.profileImageUrl.replace("_normal", "");
                String name = data.name;
                Map<String, String> map = new HashMap();
                map.put("name", name);
                map.put("userId", userId + "");
                map.put("iconurl", profileImageUrl);
                callback.onSuccess(map);
            }

            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
