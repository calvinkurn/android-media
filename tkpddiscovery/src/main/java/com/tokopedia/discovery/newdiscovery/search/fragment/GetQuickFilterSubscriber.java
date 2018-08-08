package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListFragmentView;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;

import java.lang.reflect.Type;
import java.util.List;

import rx.Subscriber;

/**
 * Created by henrypriyono on 14/03/18.
 */

public class GetQuickFilterSubscriber extends Subscriber<DynamicFilterModel> {
    protected final ProductListFragmentView view;

    public GetQuickFilterSubscriber(ProductListFragmentView view) {
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
    public void onNext(DynamicFilterModel dynamicFilterModel) {
        if (dynamicFilterModel != null
                && dynamicFilterModel.getData() != null
                && dynamicFilterModel.getData().getFilter() != null) {
            view.renderQuickFilter(dynamicFilterModel);
        }
    }
}
