package com.tokopedia.seller.topads.interactor;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public interface ListenerInteractor<T>  {
    void onSuccess(T t);

    void onError(Throwable throwable);
}
