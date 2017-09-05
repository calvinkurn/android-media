package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class CreateResoStep1Subscriber extends Subscriber<CreateResoStep1Domain> {
    private final CreateResolutionCenter.View mainView;

    public CreateResoStep1Subscriber(CreateResolutionCenter.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.errorCreateResoStep1(e.getLocalizedMessage());
    }

    @Override
    public void onNext(CreateResoStep1Domain createResoStep1Domain) {
        mainView.successCreateResoStep1(createResoStep1Domain);
    }

}
