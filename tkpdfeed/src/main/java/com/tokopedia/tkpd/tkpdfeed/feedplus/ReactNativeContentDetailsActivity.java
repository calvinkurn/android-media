package com.tokopedia.tkpd.tkpdfeed.feedplus;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.ReactNativeContentDetailFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeContentDetailsActivity extends ReactFragmentActivity<ReactNativeContentDetailFragment> {

    public static final String EXPLORE_PAGE = "Explore Page";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String KEY_CONTENT = "content";
    public static final String CONTENT_TITLE = "Detail";
    public static final String KEY_ID = "post_id";


    @DeepLink({Constants.Applinks.CONTENT_DETAIL})
    public static Intent getContentDetailPageApplinkCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(EXPLORE_PAGE);
        return ReactNativeContentDetailsActivity.createApplinkCallingIntent(
                context,
                ReactConst.Screen.CONTENT_DETAIL,
                bundle.getString(KEY_ID),
                bundle
        );
    }

    public static Intent createApplinkCallingIntent(Context context, String reactScreenName, String url, Bundle extras) {
        Intent intent = new Intent(context, ReactNativeContentDetailsActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, "");
        extras.putString(EXTRA_URL, url);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public static Intent createCallingIntent(Context context,
                                             String reactScreenName,
                                             String pageTitle) {
        Intent intent = new Intent(context, ReactNativeContentDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativeContentDetailFragment getReactNativeFragment() {
        return ReactNativeContentDetailFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
//        if (getIntent() != null &amp;&amp; getIntent().getExtras() != null) {
//            return getIntent().getExtras().getString(EXTRA_TITLE);
//        }

        return "Post Detail";
    }
}