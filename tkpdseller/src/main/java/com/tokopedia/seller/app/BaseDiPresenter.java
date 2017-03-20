package com.tokopedia.seller.app;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public abstract class BaseDiPresenter<V extends BaseDiView> {

    protected V view;

    public BaseDiPresenter(V view) {
        this.view = view;
    }

    protected abstract void unsubscribeOnDestroy();

}
