package com.tokopedia;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import okhttp3.Interceptor;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getHomeIntent(Context context);

    Intent getTopProfileIntent(Context context, String userId);

    Interceptor getChuckInterceptor();

    Intent getShopPageIntent(Context context, String shopId);
}
