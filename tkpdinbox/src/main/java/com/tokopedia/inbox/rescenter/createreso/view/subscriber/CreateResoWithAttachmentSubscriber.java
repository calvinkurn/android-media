package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class CreateResoWithAttachmentSubscriber extends Subscriber<CreateSubmitDomain> {
    private final CreateResolutionCenter.View mainView;

    public CreateResoWithAttachmentSubscriber(CreateResolutionCenter.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.errorCreateResoWithAttachment(e.getLocalizedMessage());
    }

    @Override
    public void onNext(CreateSubmitDomain createSubmitDomain) {
        mainView.successCreateResoWithAttachment(String.valueOf(createSubmitDomain.getResolution().getId()), createSubmitDomain.getSuccessMessage());
    }
}
