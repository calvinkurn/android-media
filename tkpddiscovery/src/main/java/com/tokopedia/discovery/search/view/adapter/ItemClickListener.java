package com.tokopedia.discovery.search.view.adapter;

import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchItem;

/**
 * Created by eriksuprayogi on 2/21/17.
 */

public interface ItemClickListener {
    void onItemClicked(SearchItem item);

    void copyTextToSearchView(String text);

    void onDeleteRecentSearchItem(SearchItem item);

    void onDeleteAllRecentSearch();
}