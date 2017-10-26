package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class PostEditSolutionRepositoryImpl implements PostEditSolutionRepository {

    CreateResolutionFactory createResolutionFactory;

    public PostEditSolutionRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<EditAppealResolutionSolutionDomain>
    postEditSolutionDataCloud(RequestParams requestParams) {
        return createResolutionFactory.postEditSolutionDataSource().
                postEditSolutionCloudSource(requestParams);
    }
}
