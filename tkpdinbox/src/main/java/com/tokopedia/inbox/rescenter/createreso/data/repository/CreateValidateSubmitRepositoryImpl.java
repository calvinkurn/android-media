package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateValidateSubmitRepositoryImpl implements CreateValidateSubmitRepository {

    CreateResolutionFactory createResolutionFactory;

    public CreateValidateSubmitRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<CreateValidateDomain> validate(RequestParams requestParams) {
        return createResolutionFactory.getCreateValidateCloudSource().createValidate(requestParams);
    }

    @Override
    public Observable<CreateSubmitDomain> submit(RequestParams requestParams) {
        return createResolutionFactory.createSubmitCloudSource().createSubmit(requestParams);
    }
}
