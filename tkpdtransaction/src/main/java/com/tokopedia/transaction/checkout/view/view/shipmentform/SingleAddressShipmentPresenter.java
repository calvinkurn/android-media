package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.transaction.checkout.domain.usecase.ICartListInteractor;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class SingleAddressShipmentPresenter {

    private final ICartSingleAddressView view;
    private final ICartListInteractor cartListInteractor;

    @Inject
    public SingleAddressShipmentPresenter(ICartSingleAddressView view, ICartListInteractor cartListInteractor) {
        this.view = view;
        this.cartListInteractor = cartListInteractor;
    }

    Subscriber<CheckPromoCodeCartShipmentResult> getSubscriberCheckPromoShipment() {
        return new Subscriber<CheckPromoCodeCartShipmentResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderErrorCheckPromoShipmentData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                if (!checkPromoCodeCartShipmentResult.isError()) {
                    view.renderCheckPromoShipmentDataSuccess(checkPromoCodeCartShipmentResult);
                } else {
                    view.renderErrorCheckPromoShipmentData(checkPromoCodeCartShipmentResult.getErrorMessage());
                }
            }
        };
    }

    void processCheckShipmentPrepareCheckout() {
        view.showProgressLoading();
        TKPDMapParam<String, String> paramGetShipmentForm = new TKPDMapParam<>();
        paramGetShipmentForm.put("lang", "id");
        cartListInteractor.getShipmentForm(
                new Subscriber<CartShipmentAddressFormData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.hideProgressLoading();
                    }

                    @Override
                    public void onNext(CartShipmentAddressFormData cartShipmentAddressFormData) {
                        boolean isEnableCheckout = true;
                        for (GroupAddress groupAddress : cartShipmentAddressFormData.getGroupAddress()) {
                            if (groupAddress.isError() || groupAddress.isWarning())
                                isEnableCheckout = false;
                            for (GroupShop groupShop : groupAddress.getGroupShop()) {
                                if (groupShop.isError() || groupShop.isWarning())
                                    isEnableCheckout = false;
                            }
                        }
                        if (isEnableCheckout) {
                            view.renderCheckShipmentPrepareCheckoutSuccess();
                        } else {
                            view.renderErrorDataHasChangedCheckShipmentPrepareCheckout(
                                    cartShipmentAddressFormData
                            );
                        }
                    }
                }, AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), paramGetShipmentForm)
        );
    }
}