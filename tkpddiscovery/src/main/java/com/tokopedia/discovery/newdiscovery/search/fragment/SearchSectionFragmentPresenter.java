package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

import java.util.HashMap;

/**
 * Created by henrypriyono on 10/24/17.
 */

public interface SearchSectionFragmentPresenter<V extends CustomerView> extends CustomerPresenter<V> {
    void requestDynamicFilter();
    void requestDynamicFilter(HashMap<String, String> additionalParams);
}
