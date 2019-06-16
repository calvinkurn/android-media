package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.discovery.common.data.DynamicFilterModel;

import java.util.HashMap;

/**
 * Created by henrypriyono on 10/24/17.
 */

public interface BrowseSectionFragmentView extends CustomerView {
    HashMap<String, String> getSelectedSort();

    void setSelectedSort(HashMap<String, String> selectedSort);

    HashMap<String, String> getSelectedFilter();

    HashMap<String, String> getExtraFilter();

    void setSelectedFilter(HashMap<String, String> selectedFilter);

    void getDynamicFilter();

    void renderDynamicFilter(DynamicFilterModel dynamicFilterModel);

    void renderFailGetDynamicFilter();

    void showRefreshLayout();

    void hideRefreshLayout();

    String getScreenNameId();

    void setTotalSearchResultCount(String formattedResultCount);
}
