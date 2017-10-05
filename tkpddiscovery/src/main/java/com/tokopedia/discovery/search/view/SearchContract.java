package com.tokopedia.discovery.search.view;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public interface SearchContract {
    interface View extends CustomerView {
        void showSearchResult(List<Visitable> list);
        void showNetworkErrorMessage();
    }

    interface Presenter extends CustomerPresenter<View> {
        void search(String query);

        void deleteRecentSearchItem(String keyword);

        void deleteAllRecentSearch();

        void initializeDataSearch();
    }
}