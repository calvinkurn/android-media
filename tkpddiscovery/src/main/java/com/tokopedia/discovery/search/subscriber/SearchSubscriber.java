package com.tokopedia.discovery.search.subscriber;

import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.view.SearchContract;

import java.util.List;

import rx.Subscriber;

public class SearchSubscriber extends Subscriber<List<SearchData>> {
    private final String querySearch;
    private final DefaultAutoCompleteViewModel defaultAutoCompleteViewModel;
    private final TabAutoCompleteViewModel tabAutoCompleteViewModel;
    private final SearchContract.View view;

    public SearchSubscriber(String querySearch,
                            DefaultAutoCompleteViewModel defaultAutoCompleteViewModel,
                            TabAutoCompleteViewModel tabAutoCompleteViewModel,
                            SearchContract.View view) {
        this.querySearch = querySearch;
        this.defaultAutoCompleteViewModel = defaultAutoCompleteViewModel;
        this.tabAutoCompleteViewModel = tabAutoCompleteViewModel;
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(List<SearchData> searchDatas) {
        for (SearchData searchData : searchDatas) {
            if (searchData.getItems().size() > 0) {
                switch (searchData.getId()) {
                    case "recent_search":
                    case "recent_view":
                    case "popular_search":
                        defaultAutoCompleteViewModel.setSearchTerm(querySearch);
                        defaultAutoCompleteViewModel.addList(searchData);
                        continue;
                    case "digital":
                    case "category":
                    case "autocomplete":
                    case "hotlist":
                    case "in_category":
                    case "shop":
                        tabAutoCompleteViewModel.setSearchTerm(querySearch);
                        tabAutoCompleteViewModel.addList(searchData);
                        continue;
                }
            }
        }
        view.showAutoCompleteResult(defaultAutoCompleteViewModel, tabAutoCompleteViewModel);
    }
}