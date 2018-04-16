package com.tokopedia.tkpd.tkpdfeed.feedplus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.ReactNativeExploreContentFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeExplorePageActivity extends ReactFragmentActivity<ReactNativeExploreContentFragment> {

    public static final String EXPLORE_PAGE = "Explore-Page";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String KEY_CAT = "category_id";
    public static final String KEY_TAB = "tab_name";


    @DeepLink({Constants.Applinks.CONTENT_EXPLORE})
    public static Intent getContentExplorePageApplinkCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(EXPLORE_PAGE);
        bundle.getString(KEY_CAT);
        bundle.getString(KEY_TAB);
        return ReactNativeExplorePageActivity.createApplinkCallingIntent(
                context,
                ReactConst.Screen.EXPLORE_PAGE,
                context.getString(R.string.react_native_explore_page_title),
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
