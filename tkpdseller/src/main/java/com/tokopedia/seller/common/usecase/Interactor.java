package com.tokopedia.seller.common.usecase;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Subscriber;

/**
 * @author Kulomady on 12/7/16.
 */

/**
 * Use Usecase from tkpd usecase
 */
@Deprecated
public interface Interactor<T> {

    void execute(RequestParams requestParams, Subscriber<T> subscriber);

}
