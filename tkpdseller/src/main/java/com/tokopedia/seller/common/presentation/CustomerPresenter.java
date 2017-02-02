package com.tokopedia.seller.common.presentation;

/**
 * @author kulomady on 11/20/16.
 */

public interface CustomerPresenter<V extends CustomerView> {

    void attachView(V view);

    void detachView();
}
