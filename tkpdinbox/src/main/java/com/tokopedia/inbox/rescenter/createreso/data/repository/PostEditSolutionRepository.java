package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface PostEditSolutionRepository {
    Observable<EditAppealResolutionSolutionDomain> postEditSolutionDataCloud(RequestParams requestParams);
}
