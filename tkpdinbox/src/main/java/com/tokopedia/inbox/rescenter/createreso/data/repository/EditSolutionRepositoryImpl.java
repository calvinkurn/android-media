package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class EditSolutionRepositoryImpl implements EditSolutionRepository {

    CreateResolutionFactory createResolutionFactory;

    public EditSolutionRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<EditSolutionResponseDomain> getEditSolutionFromCloud(RequestParams requestParams) {
        return createResolutionFactory.createEditSolutionCloudSource().getEditSolutionResponse(requestParams);
    }
}
