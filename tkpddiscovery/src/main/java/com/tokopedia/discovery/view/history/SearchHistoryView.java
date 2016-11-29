package com.tokopedia.discovery.view.history;

import com.tokopedia.core.presenter.BaseView;

/**
 * Created by Erry on 6/30/2016.
 */
public interface SearchHistoryView extends BaseView {

    void initRecylerView();
    void sendSearchResult(String query);
    void clearSearchQuery();

    void sendHotlistResult(String selected);
}
