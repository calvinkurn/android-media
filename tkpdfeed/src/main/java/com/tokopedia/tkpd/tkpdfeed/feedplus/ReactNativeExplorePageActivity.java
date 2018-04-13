package com.tokopedia.tkpd.tkpdfeed.feedplus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.ReactNativeExploreContentFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeExplorePageActivity extends ReactFragmentActivity<ReactNativeExploreContentFragment> {

    public static final String EXPLORE_PAGE = "Explore Page";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String KEY_CONTENT = "content";
    public static final String CONTENT_TITLE = "Explore Content";
    public static final String KEY_CAT_ID = "cat_id";
    public static final String KEY_TAB_ID = "tab";


    @DeepLink({Constants.Applinks.CONTENT_EXPLORE_CAT})
    public static Intent getContentExplorePageApplinkCatCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(EXPLORE_PAGE);
        return ReactNativeExplorePageActivity.createApplinkCallingIntent(
                context,
                ReactConst.Screen.EXPLORE_PAGE,
                bundle.getString(KEY_CAT_ID),
                bundle
        );
    }

    @DeepLink({Constants.Applinks.CONTENT_EXPLORE_TAB})
    public static Intent getContentExplorePageApplinkTabCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(EXPLORE_PAGE);
        return ReactNativeExplorePageActivity.createApplinkCallingIntent(
                context,
                ReactConst.Screen.EXPLORE_PAGE,
                bundle.getString(KEY_TAB_ID),
                bundle
        );
    }

    public static Intent createApplinkCallingIntent(Context context,
                                                    String reactScreenName,
                                                    String pageTitle,
                                                    Bundle extras) {
        Intent intent = new Intent(context, ReactNativeExplorePageActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public static Intent createCallingIntent(Context context,
                                             String reactScreenName,
                                             String pageTitle) {
        Intent intent = new Intent(context, ReactNativeExplorePageActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativeExploreContentFragment getReactNativeFragment() {
        return ReactNativeExploreContentFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }
        return "";
    }
}
