package com.tokopedia.events.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchPresenter
        extends BaseDaggerPresenter<EventSearchContract.EventSearchView>
        implements EventSearchContract.EventSearchPresenter {

    public GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase;

    @Inject
    public EventSearchPresenter(GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase) {
        this.getSearchEventsListRequestUseCase = getSearchEventsListRequestUseCase;

    }
    @Override
    public void getEventsListBySearch(String searchText) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getSearchEventsListRequestUseCase.TAG, searchText);

        getSearchEventsListRequestUseCase.execute(requestParams, new Subscriber<List<EventsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(List<EventsCategoryDomain> categoryEntities) {
                List<CategoryViewModel> categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListVeiwModel(categoryEntities);
                getView().renderFromSearchResults(categoryViewModels);
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void searchTextChanged(String searchText) {

    }

    @Override
    public void searchSubmitted(String searchText) {

    }
}
