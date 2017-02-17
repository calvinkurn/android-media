package com.tokopedia.seller.gmsubscribe.view.checkout.presenter;

import android.util.Log;

import com.tokopedia.seller.common.presentation.BasePresenter;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GmVoucherCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckGmSubscribeVoucherUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGmSubscribeUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGmSubscribeWithVoucherCheckUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmAutoSubscribeSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmCurrentSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmAutoSubscribeDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.fragment.GmCheckoutView;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmAutoSubscribeViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmCheckoutCurrentSelectedViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmCheckoutViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmVoucherViewModel;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 2/3/17.
 */

public class GmCheckoutPresenterImpl extends BasePresenter<GmCheckoutView> implements GmCheckoutPresenter {

    private static final String TAG = "Checkout Presenter";
    private final GetGmCurrentSelectedProductUseCase getCurrentSelectedProduct;
    private final GetGmAutoSubscribeSelectedProductUseCase getGmAutoSubscribeSelectedProductUseCase;
    private final CheckGmSubscribeVoucherUseCase checkGmSubscribeVoucherUseCase;
    private final CheckoutGmSubscribeUseCase checkoutGmSubscribeUseCase;
    private final CheckoutGmSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase;

    public GmCheckoutPresenterImpl(GetGmCurrentSelectedProductUseCase getCurrentSelectedProduct,
                                   GetGmAutoSubscribeSelectedProductUseCase getGmAutoSubscribeSelectedProductUseCase,
                                   CheckGmSubscribeVoucherUseCase checkGmSubscribeVoucherUseCase,
                                   CheckoutGmSubscribeUseCase checkoutGmSubscribeUseCase,
                                   CheckoutGmSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase) {
        this.getCurrentSelectedProduct = getCurrentSelectedProduct;
        this.getGmAutoSubscribeSelectedProductUseCase = getGmAutoSubscribeSelectedProductUseCase;
        this.checkGmSubscribeVoucherUseCase = checkGmSubscribeVoucherUseCase;
        this.checkoutGmSubscribeUseCase = checkoutGmSubscribeUseCase;
        this.checkoutGMSubscribeWithVoucherCheckUseCase = checkoutGMSubscribeWithVoucherCheckUseCase;
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
    public void detachView() {
        super.detachView();
        getCurrentSelectedProduct.unsubscribe();
        getGmAutoSubscribeSelectedProductUseCase.unsubscribe();
        checkGmSubscribeVoucherUseCase.unsubscribe();
        checkoutGmSubscribeUseCase.unsubscribe();
        checkoutGMSubscribeWithVoucherCheckUseCase.unsubscribe();
    }

    private class GetCurrentSelectedProductSubscriber extends Subscriber<GmProductDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Error");
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
            Log.e(TAG, "Error");
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
            Log.e(TAG, "Error");
            getView().dismissProgressDialog();
            getView().failedCheckout();
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
            } else {
                getView().failedCheckout();
            }
        }

        @Override
        public void onNext(GmCheckoutDomainModel gmCheckoutDomainModel) {
            getView().dismissProgressDialog();
            getView().goToDynamicPayment(GmCheckoutViewModel.mapFromDomain(gmCheckoutDomainModel));
        }
    }
}

