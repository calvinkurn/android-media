package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class SolutionRepositoryImpl implements SolutionRepository {

    CreateResolutionFactory createResolutionFactory;

    public SolutionRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<SolutionResponseDomain> getSolutionFromCloud(
            RequestParams requestParams) {
        return createResolutionFactory.getSolutionCloudSource().
                getSolutionResponse(requestParams);
    }
}
