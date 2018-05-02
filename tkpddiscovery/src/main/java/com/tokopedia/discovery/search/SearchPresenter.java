package com.tokopedia.discovery.search;

import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.discovery.search.domain.DeleteParam;
import com.tokopedia.discovery.search.domain.SearchParam;
import com.tokopedia.discovery.search.domain.interactor.DeleteSearchUseCase;
import com.tokopedia.discovery.search.domain.interactor.SearchUseCase;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.view.SearchContract;
import com.tokopedia.discovery.search.view.adapter.viewmodel.DefaultViewModel;
import com.tokopedia.discovery.search.view.adapter.viewmodel.ShopViewModel;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
    private final SearchUseCase searchUseCase;
    private final DeleteSearchUseCase deleteSearchUseCase;

    public SearchPresenter(Context context) {
        this.context = context;
        this.searchUseCase = new SearchUseCase(context);
        this.deleteSearchUseCase = new DeleteSearchUseCase(context);
    }

    @Override
    public void search(String query) {
        this.querySearch = query;
        SearchParam searchParam = new SearchParam(context);
        searchParam.getParam().put(SearchParam.KEY_QUERY, (query.isEmpty() ? "" : query));
        searchUseCase.execute(searchParam, new SearchSubscriber(querySearch));
    }

    @Override
    public void deleteRecentSearchItem(String keyword) {
        DeleteParam param = new DeleteParam(context);
        param.getParam().put(DeleteParam.KEY_Q, keyword);
        deleteSearchUseCase.execute(param, new DeleteSubscriber());
    }

    @Override
    public void deleteAllRecentSearch() {
        DeleteParam param = new DeleteParam(context);
        param.getParam().put(DeleteParam.KEY_DELETE_ALL, "true");
        deleteSearchUseCase.execute(param, new DeleteSubscriber());
    }

    @Override
    public void initializeDataSearch() {
        checkViewAttached();
    }

    @Override
    public void detachView() {
        super.detachView();
        searchUseCase.unsubscribe();
        deleteSearchUseCase.unsubscribe();
    }

    private DefaultViewModel prepareDefaultViewModel(SearchData data) {
        DefaultViewModel viewModel = new DefaultViewModel();
        viewModel.setId(data.getId());
        viewModel.setSearchItems(data.getItems());
        viewModel.setSearchTerm(querySearch);
        return viewModel;
    }

    private ShopViewModel prepareShopViewModel(SearchData data) {
        ShopViewModel viewModel = new ShopViewModel();
        viewModel.setId(data.getId());
        viewModel.setSearchItems(data.getItems());
        viewModel.setSearchTerm(querySearch);
        return viewModel;
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
