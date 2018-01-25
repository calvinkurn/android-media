package com.tokopedia.gm.subscribe.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;
import com.tokopedia.gm.subscribe.domain.cart.exception.GmVoucherCheckException;
import com.tokopedia.gm.subscribe.domain.cart.interactor.CheckGmSubscribeVoucherUseCase;
import com.tokopedia.gm.subscribe.domain.cart.interactor.CheckoutGmSubscribeUseCase;
import com.tokopedia.gm.subscribe.domain.cart.interactor.CheckoutGmSubscribeWithVoucherCheckUseCase;
import com.tokopedia.gm.subscribe.domain.cart.model.GmCheckoutDomainModel;
import com.tokopedia.gm.subscribe.domain.cart.model.GmVoucherCheckDomainModel;
import com.tokopedia.gm.subscribe.domain.product.interactor.GetGmAutoSubscribeSelectedProductUseCase;
import com.tokopedia.gm.subscribe.domain.product.interactor.GetGmCurrentSelectedProductUseCase;
import com.tokopedia.gm.subscribe.domain.product.model.GmAutoSubscribeDomainModel;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.gm.subscribe.view.fragment.GmCheckoutView;
import com.tokopedia.gm.subscribe.view.viewmodel.GmAutoSubscribeViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutCurrentSelectedViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmVoucherViewModel;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 2/3/17.
 */

public class GmCheckoutPresenterImpl extends BaseDaggerPresenter<GmCheckoutView> implements GmCheckoutPresenter {

    private static final String TAG = "Checkout Presenter";
    private final GetGmCurrentSelectedProductUseCase getCurrentSelectedProduct;
    private final GetGmAutoSubscribeSelectedProductUseCase getGmAutoSubscribeSelectedProductUseCase;
    private final CheckGmSubscribeVoucherUseCase checkGmSubscribeVoucherUseCase;
    private final CheckoutGmSubscribeUseCase checkoutGmSubscribeUseCase;
    private final CheckoutGmSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase;
    private final DeleteShopInfoUseCase deleteShopInfoUseCase;

    @Inject
    public GmCheckoutPresenterImpl(GetGmCurrentSelectedProductUseCase getCurrentSelectedProduct,
                                   GetGmAutoSubscribeSelectedProductUseCase getGmAutoSubscribeSelectedProductUseCase,
                                   CheckGmSubscribeVoucherUseCase checkGmSubscribeVoucherUseCase,
                                   CheckoutGmSubscribeUseCase checkoutGmSubscribeUseCase,
                                   CheckoutGmSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase,
                                   DeleteShopInfoUseCase deleteShopInfoUseCase) {
        this.getCurrentSelectedProduct = getCurrentSelectedProduct;
        this.getGmAutoSubscribeSelectedProductUseCase = getGmAutoSubscribeSelectedProductUseCase;
        this.checkGmSubscribeVoucherUseCase = checkGmSubscribeVoucherUseCase;
        this.checkoutGmSubscribeUseCase = checkoutGmSubscribeUseCase;
        this.checkoutGMSubscribeWithVoucherCheckUseCase = checkoutGMSubscribeWithVoucherCheckUseCase;
        this.deleteShopInfoUseCase = deleteShopInfoUseCase;
    }


    @Override
    public void getCurrentSelectedProduct(int productId) {
        getView().showProgressDialog();
        getCurrentSelectedProduct.execute(
                GetGmCurrentSelectedProductUseCase.createRequestParams(productId),
                new GetCurrentSelectedProductSubscriber()
        );
    }

    @Override
    public void getExtendSelectedProduct(int currentProductId, int autoSubscribeProductId) {
        getView().showProgressDialog();
        getGmAutoSubscribeSelectedProductUseCase.execute(
                GetGmAutoSubscribeSelectedProductUseCase.createRequestParams(currentProductId, autoSubscribeProductId),
                new GetAutoSubcribeSelectedProductSubscriber()
        );
    }

    @Override
    public void checkVoucherCode(String voucherCode, Integer selectedProduct) {
        getView().showProgressDialog();
        getView().dismissKeyboardFromVoucherEditText();
        checkGmSubscribeVoucherUseCase.execute(
                CheckGmSubscribeVoucherUseCase.createRequestParams(selectedProduct, voucherCode),
                new GetCheckGMSubscribeVoucherCodeSubscriber()
        );
    }

    @Override
    public void checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        getView().showProgressDialog();
        checkoutGmSubscribeUseCase.execute(
                CheckoutGmSubscribeUseCase.generateParams(selectedProduct, autoExtendSelectedProduct, voucherCode),
                new CheckoutGMSubscribeSubsriber()
        );
    }

    @Override
    public void checkoutWithVoucherCheckGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        getView().showProgressDialog();
        checkoutGMSubscribeWithVoucherCheckUseCase.execute(
                CheckoutGmSubscribeUseCase.generateParams(selectedProduct, autoExtendSelectedProduct, voucherCode),
                new CheckoutGMSubscribeWithVoucherCheckSubscriber()
        );
    }

    @Override
    public void clearCacheShopInfo() {
        deleteShopInfoUseCase.execute(getSubscriberClearCacheShopInfo());
    }

    private Subscriber<Boolean> getSubscriberClearCacheShopInfo() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        getCurrentSelectedProduct.unsubscribe();
        getGmAutoSubscribeSelectedProductUseCase.unsubscribe();
        checkGmSubscribeVoucherUseCase.unsubscribe();
        checkoutGmSubscribeUseCase.unsubscribe();
        checkoutGMSubscribeWithVoucherCheckUseCase.unsubscribe();
        deleteShopInfoUseCase.unsubscribe();
    }

    private class GetCurrentSelectedProductSubscriber extends Subscriber<GmProductDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            getView().failedGetCurrentProduct();
        }

        @Override
        public void onNext(GmProductDomainModel gmProductDomainModel) {
            getView().dismissProgressDialog();
            getView().renderCurrentSelectedProduct(new GmCheckoutCurrentSelectedViewModel(gmProductDomainModel));
        }
    }

    private class GetAutoSubcribeSelectedProductSubscriber extends Subscriber<GmAutoSubscribeDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            getView().failedGetAutoSubscribeProduct();
        }

        @Override
        public void onNext(GmAutoSubscribeDomainModel gmAutoSubscribeDomainModel) {
            getView().dismissProgressDialog();
            getView().renderAutoSubscribeProduct(new GmAutoSubscribeViewModel(gmAutoSubscribeDomainModel));
        }
    }

    private class GetCheckGMSubscribeVoucherCodeSubscriber extends Subscriber<GmVoucherCheckDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            if (e instanceof GmVoucherCheckException) {
                getView().renderVoucherView(GmVoucherViewModel.generateClassWithError(e.getMessage()));
            } else {
                getView().showGenericError();
            }
        }

        @Override
        public void onNext(GmVoucherCheckDomainModel gmVoucherCheckDomainModel) {
            getView().dismissProgressDialog();
            getView().renderVoucherView(GmVoucherViewModel.mapFromDomain(gmVoucherCheckDomainModel));
        }
    }

    private class CheckoutGMSubscribeSubsriber extends Subscriber<GmCheckoutDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof ResponseV4ErrorException){
                String string = ((ResponseV4ErrorException)e).getErrorList().get(0);
                getView().showMessageError(string);
                getView().dismissProgressDialog();
            } else {
                getView().dismissProgressDialog();
                getView().showGenericError();
            }
        }

        @Override
        public void onNext(GmCheckoutDomainModel gmCheckoutDomainModel) {
            getView().dismissProgressDialog();
            getView().goToDynamicPayment(GmCheckoutViewModel.mapFromDomain(gmCheckoutDomainModel));
        }
    }

    private class CheckoutGMSubscribeWithVoucherCheckSubscriber extends Subscriber<GmCheckoutDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            if (e instanceof GmVoucherCheckException) {
                getView().renderVoucherView(GmVoucherViewModel.generateClassWithError(e.getMessage()));
            } else if (e instanceof ResponseV4ErrorException){
                String string = ((ResponseV4ErrorException)e).getErrorList().get(0);
                getView().showMessageError(string);
                getView().dismissProgressDialog();
            } else {
                getView().dismissProgressDialog();
                getView().showGenericError();
            }
        }

        @Override
        public void onNext(GmCheckoutDomainModel gmCheckoutDomainModel) {
            getView().dismissProgressDialog();
            getView().goToDynamicPayment(GmCheckoutViewModel.mapFromDomain(gmCheckoutDomainModel));
        }
    }
}

