package com.tokopedia;

import android.content.Context;
import android.content.Intent;

import okhttp3.Interceptor;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {

    String ENABLE_LOGIN_INACTIVE_PHONE_LINK = "mainapp_login_inactive_phone_no_link";

    Intent getHomeIntent(Context context);

    Intent getTopProfileIntent(Context context, String userId);

    Interceptor getChuckInterceptor();

    Intent getShopPageIntent(Context context, String shopId);

    boolean isLoginInactivePhoneLinkEnabled();
}
