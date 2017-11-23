package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;

import rx.Observable;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface CreateValidateSubmitRepository {
    Observable<CreateValidateDomain> validate(RequestParams requestParams);

    Observable<CreateSubmitDomain> submit(RequestParams requestParams);
}
