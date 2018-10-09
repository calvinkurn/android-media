package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.loyalty.view.view.IMvpView;

public interface IPresenter<V extends IMvpView> {

    void attachView(V mvpView);

    void detachView();

}
