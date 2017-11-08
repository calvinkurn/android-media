package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoWithoutAttachmentRepositoryImpl implements CreateResoWithoutAttachmentRepository {

    CreateResolutionFactory createResolutionFactory;

    public CreateResoWithoutAttachmentRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<CreateResoWithoutAttachmentDomain> createResoWithoutAttachment(RequestParams requestParams) {
        return createResolutionFactory.createResoStep1CloudSource().
                createResoWithoutAttachmentResponse(requestParams);
    }
}
