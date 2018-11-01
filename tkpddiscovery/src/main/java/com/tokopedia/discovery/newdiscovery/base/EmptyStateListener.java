package com.tokopedia.discovery.newdiscovery.base;

/**
 * @author by errysuprayogi on 11/14/17.
 */

public interface EmptyStateListener {
    void onEmptyButtonClicked();
    void onBannerAdsClicked(String appLink);
    void onSelectedFilterRemoved(String uniqueId);
    boolean isUserHasLogin();
    String getUserId();
}
