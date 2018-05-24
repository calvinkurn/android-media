package com.tokopedia.discovery.search;

import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.discovery.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.view.SearchContract;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;

/**
 * @author erry on 23/02/17.
 */

public class SearchPresenter extends BaseDaggerPresenter<SearchContract.View>
        implements SearchContract.Presenter {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    private final Context context;
    private String querySearch = "";

    @Inject
    AutoCompleteUseCase autoCompleteUseCase;

    @Inject
    DeleteRecentSearchUseCase deleteRecentSearchUseCase;

    public SearchPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void search(String query) {
        this.querySearch = query;
        autoCompleteUseCase.execute(
                AutoCompleteUseCase.getParams(
                        this.querySearch,
                        GCMHandler.getRegistrationId(context),
                        SessionHandler.getLoginID(context)
                ),
                new SearchSubscriber(querySearch)
        );
    }

    @Override
    public void deleteRecentSearchItem(String keyword) {
        RequestParams params = DeleteRecentSearchUseCase.getParams(
                keyword,
                GCMHandler.getRegistrationId(context),
                SessionHandler.getLoginID(context)
        );
        deleteRecentSearchUseCase.execute(
                params,
                new SearchSubscriber(querySearch)
        );
    }

    @Override
    public void deleteAllRecentSearch() {
        RequestParams params = DeleteRecentSearchUseCase.getParams(
                GCMHandler.getRegistrationId(context),
                SessionHandler.getLoginID(context)
        );
        deleteRecentSearchUseCase.execute(
                params,
                new SearchSubscriber("")
        );
    }

    @Override
    public void initializeDataSearch() {
        checkViewAttached();
    }

    @Override
    public void detachView() {
        super.detachView();
        autoCompleteUseCase.unsubscribe();
    }

    private class SearchSubscriber extends Subscriber<List<SearchData>> {
        private final String querySearch;

        public SearchSubscriber(String querySearch) {
            this.querySearch = querySearch;
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
            DefaultAutoCompleteViewModel defaultAutoCompleteViewModel = new DefaultAutoCompleteViewModel();
            TabAutoCompleteViewModel tabAutoCompleteViewModel = new TabAutoCompleteViewModel();
            List<Visitable> list = new ArrayList<>();
            for (SearchData searchData : searchDatas) {
                if (searchData.getItems().size() > 0) {
                    switch (searchData.getId()) {
                        case "popular_search":
                        case "recent_search":
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
            getView().showAutoCompleteResult(defaultAutoCompleteViewModel, tabAutoCompleteViewModel);
        }
    }

    private class DeleteSubscriber extends Subscriber<Response<Void>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException) {
                getView().showNetworkErrorMessage();
            }
        }

        @Override
        public void onNext(Response<Void> voidResponse) {
            search("");
        }
    }
}
