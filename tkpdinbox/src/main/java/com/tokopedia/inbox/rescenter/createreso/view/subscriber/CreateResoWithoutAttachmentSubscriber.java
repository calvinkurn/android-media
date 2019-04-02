package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.CreateResolutionCenter;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class CreateResoWithoutAttachmentSubscriber extends Subscriber<CreateResoWithoutAttachmentDomain> {
    private final CreateResolutionCenter.View mainView;

    public CreateResoWithoutAttachmentSubscriber(CreateResolutionCenter.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.errorCreateResoWithoutAttachment(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(CreateResoWithoutAttachmentDomain createResoWithoutAttachmentDomain) {
        mainView.successCreateResoWithoutAttachment(String.valueOf(
                createResoWithoutAttachmentDomain.getResolution().getId()),
                createResoWithoutAttachmentDomain.getCacheKey(),
                createResoWithoutAttachmentDomain.getSuccessMessage(),
                createResoWithoutAttachmentDomain.getShop().getName());
    }

}
