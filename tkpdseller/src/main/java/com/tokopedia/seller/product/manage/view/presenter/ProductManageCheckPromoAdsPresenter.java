package com.tokopedia.seller.product.manage.view.presenter;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.seller.product.manage.view.listener.ProductManageCheckPromoAdsView;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductCheckPromoAdsUseCase;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 12/04/18.
 */

public class ProductManageCheckPromoAdsPresenter implements CustomerPresenter<ProductManageCheckPromoAdsView> {
    private final GetProductCheckPromoAdsUseCase useCase;
    private ProductManageCheckPromoAdsView view;
    private static final String IS_UNPROMOTED_PRODUCT = "0";

    @Inject
    public ProductManageCheckPromoAdsPresenter(GetProductCheckPromoAdsUseCase useCase) {
        this.useCase = useCase;
    }

    public void checkPromoAds(String shopId, String itemId, String userId){
        useCase.execute(GetProductCheckPromoAdsUseCase.createRequestParams(shopId, itemId, userId), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MessageErrorException) {
                    view.renderErrorView(e.getMessage());
                } else if (e instanceof RuntimeHttpErrorException) {
                    view.renderErrorView(e.getMessage());
                } else if (e instanceof IOException) {
                    view.renderRetryRefresh();
                } else {
                    view.renderErrorView(null);
                }
                view.finishLoadingProgress();
            }

            @Override
            public void onNext(String s) {
                view.finishLoadingProgress();
                if (s.equalsIgnoreCase(IS_UNPROMOTED_PRODUCT)){
                    view.moveToCreateAds();
                } else {
                    view.moveToAdsDetail(s);
                }
            }
        });
    }


    @Override
    public void attachView(ProductManageCheckPromoAdsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        useCase.unsubscribe();
    }
}
