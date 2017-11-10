package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class AppealSolutionRepositoryImpl implements AppealSolutionRepository {

    CreateResolutionFactory createResolutionFactory;

    public AppealSolutionRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<AppealSolutionResponseDomain> getAppealSolutionFromCloud(RequestParams requestParams) {
        return createResolutionFactory.createAppealSolutionCloudSource().getAppealSolutionResponse(requestParams);
    }
}
