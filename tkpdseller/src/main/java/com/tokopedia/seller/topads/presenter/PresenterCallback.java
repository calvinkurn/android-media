package com.tokopedia.seller.topads.presenter;

/**
 * Created by Nathaniel on 11/23/2016.
 */

public interface PresenterCallback<T> {

    void onSuccess(T t);

    void onError();
}
