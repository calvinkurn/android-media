package com.tokopedia.transaction.orders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public interface UnifiedOrderListRouter {
    Intent getWebviewActivityWithIntent(Context context, String url);
    Fragment getFlightOrderListFragment();
    boolean getBooleanRemoteConfig(String key, boolean defaultValue);
}
