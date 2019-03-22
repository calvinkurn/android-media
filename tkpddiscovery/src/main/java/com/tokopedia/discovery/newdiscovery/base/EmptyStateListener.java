package com.tokopedia.discovery.newdiscovery.base;

import android.support.annotation.Nullable;

import com.tokopedia.core.discovery.model.Option;

import java.util.List;

/**
 * @author by errysuprayogi on 11/14/17.
 */

public interface EmptyStateListener {
    void onEmptyButtonClicked();
    void onBannerAdsClicked(String appLink);
    void onSelectedFilterRemoved(Option option);
    boolean isUserHasLogin();
    String getUserId();
    @Nullable
    List<Option> getSelectedFilterAsOptionList();
}
