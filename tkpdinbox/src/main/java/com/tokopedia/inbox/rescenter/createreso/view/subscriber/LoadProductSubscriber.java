package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class LoadProductSubscriber extends Subscriber<ProductProblemResponseDomain> {
    private final CreateResolutionCenter.View mainView;

    public LoadProductSubscriber(CreateResolutionCenter.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.errorLoadProductProblemData(e.getLocalizedMessage());
    }

    @Override
    public void onNext(ProductProblemResponseDomain responseDomain) {
        mainView.successLoadProductProblemData(responseDomain);
    }
}
