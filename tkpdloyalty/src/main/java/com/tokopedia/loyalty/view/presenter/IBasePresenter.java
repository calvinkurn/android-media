package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.loyalty.view.view.IMvpView;

public class IBasePresenter<T extends IMvpView> implements IPresenter<T> {
    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    boolean isViewAttached() {
        return mMvpView != null;
    }

    T getMvpView() {
        return mMvpView;
    }

    void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    private static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
