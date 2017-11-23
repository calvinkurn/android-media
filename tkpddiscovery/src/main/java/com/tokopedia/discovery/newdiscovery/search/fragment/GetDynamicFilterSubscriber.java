package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogFragmentContract;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/16/17.
 */

public class GetDynamicFilterSubscriber extends Subscriber<DynamicFilterModel> {

    private final SearchSectionFragmentView view;

    public GetDynamicFilterSubscriber(SearchSectionFragmentView view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.renderFailGetDynamicFilter();
    }

    @Override
    public void onNext(DynamicFilterModel dynamicFilterModel) {
        if (dynamicFilterModel != null) {
            view.renderDynamicFilter(dynamicFilterModel);
        } else {
            view.renderFailGetDynamicFilter();
        }
    }
}
