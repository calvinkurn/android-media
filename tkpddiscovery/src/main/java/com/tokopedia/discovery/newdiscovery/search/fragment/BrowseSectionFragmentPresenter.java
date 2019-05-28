package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.HashMap;

/**
 * Created by henrypriyono on 10/24/17.
 */

public interface BrowseSectionFragmentPresenter<V extends CustomerView> extends CustomerPresenter<V> {
    void requestDynamicFilter();
}
