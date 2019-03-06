package com.tokopedia.transaction.orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public interface UnifiedOrderListRouter {
    Intent getWebviewActivityWithIntent(Context context, String url);
    Fragment getFlightOrderListFragment();
    boolean getBooleanRemoteConfig(String key, boolean defaultValue);
    void actionOpenGeneralWebView(Activity activity, String url);
}
