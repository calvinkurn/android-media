package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;

import rx.Observable;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface CreateResoStep1Repository {
    Observable<CreateResoStep1Domain> createResoStep1(RequestParams requestParams);
}
