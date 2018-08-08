package com.tokopedia.discovery.newdiscovery.base;

/**
 * @author by errysuprayogi on 11/14/17.
 */

public interface EmptyStateClickListener {
    void onEmptyButtonClicked();
    void onBannerAdsClicked(String appLink);
    void onSelectedFilterRemoved(String uniqueId);
}
