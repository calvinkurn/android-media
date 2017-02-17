package com.tokopedia.seller.gmsubscribe.view.home.presenter;

import android.util.Log;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.presentation.BasePresenter;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.ClearGmSubscribeProductCacheUseCase;
import com.tokopedia.seller.gmsubscribe.view.home.fragment.GmHomeView;

/**
 * Created by sebastianuskh on 2/9/17.
 */
public class GmHomePresenterImpl extends BasePresenter<GmHomeView> implements GmHomePresenter {
    private static final String TAG = "GMHomePresenter";
    private final ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache;

    public GmHomePresenterImpl(ClearGmSubscribeProductCacheUseCase clearGMSubscribeProductCache) {
        this.clearGMSubscribeProductCache = clearGMSubscribeProductCache;
    }

    @Override
    public void clearGMProductCache() {
        checkViewAttached();
        getView().showProgressDialog();
        clearGMSubscribeProductCache.execute(RequestParams.EMPTY, new ClearGMSubscribeProductCacheSubscriber());
    }

    private class ClearGMSubscribeProductCacheSubscriber extends rx.Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Error occured, but because it's mean the cache is broken, then it will ok");
            getView().dismissProgressDialog();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            getView().dismissProgressDialog();
        }
    }
}
