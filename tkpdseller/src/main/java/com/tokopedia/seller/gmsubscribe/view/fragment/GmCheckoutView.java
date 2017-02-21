package com.tokopedia.seller.gmsubscribe.view.fragment;


import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.gmsubscribe.view.viewmodel.GmAutoSubscribeViewModel;
import com.tokopedia.seller.gmsubscribe.view.viewmodel.GmCheckoutCurrentSelectedViewModel;
import com.tokopedia.seller.gmsubscribe.view.viewmodel.GmCheckoutViewModel;
import com.tokopedia.seller.gmsubscribe.view.viewmodel.GmVoucherViewModel;

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
