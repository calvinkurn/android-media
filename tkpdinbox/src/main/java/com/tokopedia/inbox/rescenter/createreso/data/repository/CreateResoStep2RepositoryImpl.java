package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep2Domain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoStep2RepositoryImpl implements CreateResoStep2Repository {

    CreateResolutionFactory createResolutionFactory;

    public CreateResoStep2RepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<CreateResoStep2Domain> createResoStep2(RequestParams requestParams) {
        return createResolutionFactory.createResoStep2CloudSource().createResoStep2Response(requestParams);
    }
}
