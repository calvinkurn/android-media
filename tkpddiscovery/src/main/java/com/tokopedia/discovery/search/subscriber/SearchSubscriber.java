package com.tokopedia.discovery.search.subscriber;

import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.view.SearchContract;

import java.util.List;

import rx.Subscriber;

public class SearchSubscriber extends Subscriber<List<SearchData>> {
    public static final String RECENT_SEARCH = "recent_search";
    public static final String RECENT_VIEW = "recent_view";
    public static final String POPULAR_SEARCH = "popular_search";
    public static final String DIGITAL = "top_digital";
    public static final String CATEGORY = "category";
    public static final String AUTOCOMPLETE = "autocomplete";
    public static final String HOTLIST = "hotlist";
    public static final String IN_CATEGORY = "in_category";
    public static final String SHOP = "shop";
    public static final String PROFILE = "profile";

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
                    case RECENT_SEARCH:
                    case RECENT_VIEW:
                    case POPULAR_SEARCH:
                        defaultAutoCompleteViewModel.setSearchTerm(querySearch);
                        defaultAutoCompleteViewModel.addList(searchData);
                        continue;
                    case DIGITAL:
                    case CATEGORY:
                    case AUTOCOMPLETE:
                    case HOTLIST:
                    case IN_CATEGORY:
                    case SHOP:
                    case PROFILE:
                        tabAutoCompleteViewModel.setSearchTerm(querySearch);
                        tabAutoCompleteViewModel.addList(searchData);
                        continue;
                }
            }
        }
        view.showAutoCompleteResult(defaultAutoCompleteViewModel, tabAutoCompleteViewModel);
    }
}