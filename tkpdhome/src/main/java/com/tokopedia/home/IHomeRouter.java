package com.tokopedia.home;

import android.app.Activity;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface IHomeRouter {

    void openIntermediaryActivity(Activity activity, String depID, String title);

    void onDigitalItemClick(Activity activity, DigitalCategoryDetailPassData passData, String appLink);
}
