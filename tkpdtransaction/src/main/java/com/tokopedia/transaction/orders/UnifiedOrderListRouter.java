package com.tokopedia.transaction.orders;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface UnifiedOrderListRouter {
    Intent getWebviewActivityWithIntent(Context context, String url);
    Fragment getFlightOrderListFragment();
    Intent getOrderHistoryIntent(Context context, String orderId);
}
