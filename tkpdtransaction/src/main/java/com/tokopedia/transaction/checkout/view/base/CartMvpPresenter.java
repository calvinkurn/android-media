package com.tokopedia.transaction.checkout.view.base;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public class CartMvpPresenter<T> {

    private T mMvpView;

    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    private static class MvpViewNotAttachedException extends RuntimeException {

        MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }

    }

}

