package com.tokopedia.topads.sdk.listener;

/**
 * @author by errysuprayogi on 4/4/17.
 */

public interface TopAdsListener {

    void onTopAdsLoading();

    void onTopAdsLoaded();

    void onTopAdsFailToLoad(int errorCode, String message);

}
