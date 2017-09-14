package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoStep1RepositoryImpl implements CreateResoStep1Repository {

    CreateResolutionFactory createResolutionFactory;

    public CreateResoStep1RepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<CreateResoStep1Domain> createResoStep1(RequestParams requestParams) {
        return createResolutionFactory.createResoStep1CloudSource().
                createResoStep1Response(requestParams);
    }
}
