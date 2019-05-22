package com.fimi.thirdpartysdk;

import android.content.Context;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig.Builder;

public class ThirdLoginManager {
    public static final void initThirdLogin(Context context) {
        Twitter.initialize(context);
        Twitter.initialize(new Builder(context).logger(new DefaultLogger(3)).twitterAuthConfig(new TwitterAuthConfig(context.getResources().getString(R.string.twitter_CONSUMER_KEY), context.getResources().getString(R.string.twitter_CONSUMER_SECRET))).debug(true).build());
    }
}
