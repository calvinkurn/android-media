package com.tokopedia.seller.gmsubscribe.view.checkout.fragment;


import com.tokopedia.seller.common.presentation.CustomerView;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmAutoSubscribeViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmCheckoutCurrentSelectedViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmCheckoutViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GmVoucherViewModel;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GmCheckoutView extends CustomerView {
    void renderCurrentSelectedProduct(GmCheckoutCurrentSelectedViewModel gmCheckoutCurrentSelectedViewModel);

    void updateSelectedProduct(int selectedProduct);

    void updateSelectedAutoProduct(int autoExtendSelectedProduct);

    void renderAutoSubscribeProduct(GmAutoSubscribeViewModel gmAutoSubscribeViewModel);

    void renderVoucherView(GmVoucherViewModel gmVoucherViewModel);

    void goToDynamicPayment(GmCheckoutViewModel gmCheckoutDomainModel);

    void failedGetCurrentProduct();

    void failedGetAutoSubscribeProduct();

    void failedCheckout();

    void showProgressDialog();

    void dismissProgressDialog();

    void showGenericError();

    void dismissKeyboardFromVoucherEditText();
}
