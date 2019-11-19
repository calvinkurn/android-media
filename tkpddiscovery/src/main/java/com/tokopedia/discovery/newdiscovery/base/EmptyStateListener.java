package com.tokopedia.discovery.newdiscovery.base;

import androidx.annotation.Nullable;

import com.tokopedia.filter.common.data.Option;

import java.util.List;

/**
 * @author by errysuprayogi on 11/14/17.
 */

public interface EmptyStateListener {
    void onEmptyButtonClicked();
    void onBannerAdsClicked(String appLink);
    void onSelectedFilterRemoved(String uniqueId);
    boolean isUserHasLogin();
    String getUserId();
    @Nullable
    List<Option> getSelectedFilterAsOptionList();
}
