package com.tokopedia.seller.gmsubscribe.view.checkout.presenter;

import android.util.Log;

import com.tokopedia.seller.common.presentation.BasePresenter;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GMVoucherCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckGMSubscribeVoucherUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGMSubscribeUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.interactor.CheckoutGMSubscribeWithVoucherCheckUseCase;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetAutoSubscribeSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetCurrentSelectedProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMAutoSubscribeDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.fragment.GMCheckoutView;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMAutoSubscribeViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMCheckoutCurrentSelectedViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMCheckoutViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMVoucherViewModel;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 2/3/17.
 */

public class GMCheckoutPresenterImpl extends BasePresenter<GMCheckoutView> implements GMCheckoutPresenter{

    private static final String TAG = "Checkout Presenter";
    private final GetCurrentSelectedProductUseCase getCurrentSelectedProduct;
    private final GetAutoSubscribeSelectedProductUseCase getAutoSubscribeSelectedProductUseCase;
    private final CheckGMSubscribeVoucherUseCase checkGMSubscribeVoucherUseCase;
    private final CheckoutGMSubscribeUseCase checkoutGMSubscribeUseCase;
    private final CheckoutGMSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase;

    public GMCheckoutPresenterImpl(GetCurrentSelectedProductUseCase getCurrentSelectedProduct,
                                   GetAutoSubscribeSelectedProductUseCase getAutoSubscribeSelectedProductUseCase,
                                   CheckGMSubscribeVoucherUseCase checkGMSubscribeVoucherUseCase,
                                   CheckoutGMSubscribeUseCase checkoutGMSubscribeUseCase,
                                   CheckoutGMSubscribeWithVoucherCheckUseCase checkoutGMSubscribeWithVoucherCheckUseCase) {
        this.getCurrentSelectedProduct = getCurrentSelectedProduct;
        this.getAutoSubscribeSelectedProductUseCase = getAutoSubscribeSelectedProductUseCase;
        this.checkGMSubscribeVoucherUseCase = checkGMSubscribeVoucherUseCase;
        this.checkoutGMSubscribeUseCase = checkoutGMSubscribeUseCase;
        this.checkoutGMSubscribeWithVoucherCheckUseCase = checkoutGMSubscribeWithVoucherCheckUseCase;
    }


    @Override
    public void getCurrentSelectedProduct(int productId) {
        getView().showProgressDialog();
        getCurrentSelectedProduct.execute(
                GetCurrentSelectedProductUseCase.createRequestParams(productId),
                new GetCurrentSelectedProductSubscriber()
        );
    }

    @Override
    public void getExtendSelectedProduct(int currentProductId, int autoSubscribeProductId) {
        getView().showProgressDialog();
        getAutoSubscribeSelectedProductUseCase.execute(
                GetAutoSubscribeSelectedProductUseCase.createRequestParams(currentProductId, autoSubscribeProductId),
                new GetAutoSubcribeSelectedProductSubscriber()
        );
    }

    @Override
    public void checkVoucherCode(String voucherCode, Integer selectedProduct) {
        getView().showProgressDialog();
        checkGMSubscribeVoucherUseCase.execute(
                CheckGMSubscribeVoucherUseCase.createRequestParams(selectedProduct, voucherCode),
                new GetCheckGMSubscribeVoucherCodeSubscriber()
        );
    }

    @Override
    public void checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        getView().showProgressDialog();
        checkoutGMSubscribeUseCase.execute(
                CheckoutGMSubscribeUseCase.generateParams(selectedProduct, autoExtendSelectedProduct, voucherCode),
                new CheckoutGMSubscribeSubsriber()
        );
    }

    @Override
    public void checkoutWithVoucherCheckGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        getView().showProgressDialog();
        checkoutGMSubscribeWithVoucherCheckUseCase.execute(
                CheckoutGMSubscribeUseCase.generateParams(selectedProduct, autoExtendSelectedProduct, voucherCode),
                new CheckoutGMSubscribeWithVoucherCheckSubscriber()
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        getCurrentSelectedProduct.unsubscribe();
        getAutoSubscribeSelectedProductUseCase.unsubscribe();
        checkGMSubscribeVoucherUseCase.unsubscribe();
        checkoutGMSubscribeUseCase.unsubscribe();
        checkoutGMSubscribeWithVoucherCheckUseCase.unsubscribe();
    }

    private class GetCurrentSelectedProductSubscriber extends Subscriber<GMProductDomainModel> {
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
        public void onNext(GMProductDomainModel gmProductDomainModel) {
            getView().dismissProgressDialog();
            getView().renderCurrentSelectedProduct(new GMCheckoutCurrentSelectedViewModel(gmProductDomainModel));
        }
    }

    private class GetAutoSubcribeSelectedProductSubscriber extends Subscriber<GMAutoSubscribeDomainModel> {
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
        public void onNext(GMAutoSubscribeDomainModel gmAutoSubscribeDomainModel) {
            getView().dismissProgressDialog();
            getView().renderAutoSubscribeProduct(new GMAutoSubscribeViewModel(gmAutoSubscribeDomainModel));
        }
    }

    private class GetCheckGMSubscribeVoucherCodeSubscriber extends Subscriber<GMVoucherCheckDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            if(e instanceof GMVoucherCheckException){
                getView().renderVoucherView(GMVoucherViewModel.generateClassWithError(e.getMessage()));
            } else {
                getView().showGenericError();
            }
        }

        @Override
        public void onNext(GMVoucherCheckDomainModel gmVoucherCheckDomainModel) {
            getView().dismissProgressDialog();
            getView().renderVoucherView(GMVoucherViewModel.mapFromDomain(gmVoucherCheckDomainModel));
        }
    }

    private class CheckoutGMSubscribeSubsriber extends Subscriber<GMCheckoutDomainModel> {
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
        public void onNext(GMCheckoutDomainModel gmCheckoutDomainModel) {
            getView().dismissProgressDialog();
            getView().goToDynamicPayment(GMCheckoutViewModel.mapFromDomain(gmCheckoutDomainModel));
        }
    }

    private class CheckoutGMSubscribeWithVoucherCheckSubscriber extends Subscriber<GMCheckoutDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().dismissProgressDialog();
            if(e instanceof GMVoucherCheckException){
                getView().renderVoucherView(GMVoucherViewModel.generateClassWithError(e.getMessage()));
            } else {
                getView().failedCheckout();
            }
        }

        @Override
        public void onNext(GMCheckoutDomainModel gmCheckoutDomainModel) {
            getView().dismissProgressDialog();
            getView().goToDynamicPayment(GMCheckoutViewModel.mapFromDomain(gmCheckoutDomainModel));
        }
    }
}

