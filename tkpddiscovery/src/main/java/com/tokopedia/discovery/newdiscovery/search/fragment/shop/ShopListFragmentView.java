package com.tokopedia.discovery.newdiscovery.search.fragment.shop;

import android.os.Bundle;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;

/**
 * Created by henrypriyono on 10/23/17.
 */

public interface ShopListFragmentView extends SearchSectionFragmentView {
    void launchLoginActivity(Bundle extras);
    boolean isUserHasLogin();
    String getUserId();
    void disableFavoriteButton(int adapterPosition);
    void enableFavoriteButton(int adapterPosition);
    String getQueryKey();
    void backToTop();
}
