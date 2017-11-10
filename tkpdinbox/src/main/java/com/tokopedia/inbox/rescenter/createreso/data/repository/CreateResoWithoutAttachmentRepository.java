package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;

import rx.Observable;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface CreateResoWithoutAttachmentRepository {
    Observable<CreateResoWithoutAttachmentDomain> createResoWithoutAttachment(RequestParams requestParams);
}
