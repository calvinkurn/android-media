package com.tokopedia;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

import okhttp3.Interceptor;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getHomeIntent(Context context);

    BaseDaggerFragment getKolPostFragment(String userId);

    Intent getTopProfileIntent(Context context, String userId);

    Interceptor getChuckInterceptor();

    Intent getShopPageIntent(Context context, String shopId);
}
