package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep2Domain;

import rx.Observable;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface CreateResoStep2Repository {
    Observable<CreateResoStep2Domain> createResoStep2(RequestParams requestParams);
}
