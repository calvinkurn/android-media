package com.tokopedia.discovery.search.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameterModel;

/**
 * @author erry on 23/02/17.
 */

public interface SearchContract {
    interface View extends CustomerView {
        void showNetworkErrorMessage();
        void showAutoCompleteResult(DefaultAutoCompleteViewModel defaultAutoCompleteViewModel, TabAutoCompleteViewModel tabAutoCompleteViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void search(SearchParameterModel searchParameterModel);

        void deleteRecentSearchItem(String keyword);

        void deleteAllRecentSearch();

        void initializeDataSearch();
    }
}