package com.tokopedia.discovery.newdiscovery.category.presentation.base;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.ProductContract;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/16/17.
 */

public class GetDynamicFilterSubscriber extends Subscriber<DynamicFilterModel> {

    private final ProductContract.View view;

    public GetDynamicFilterSubscriber(ProductContract.View view) {
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
