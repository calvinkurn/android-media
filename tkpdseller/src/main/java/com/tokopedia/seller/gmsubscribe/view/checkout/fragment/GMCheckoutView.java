package com.tokopedia.seller.gmsubscribe.view.checkout.fragment;


import com.tokopedia.seller.common.presentation.CustomerView;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMAutoSubscribeViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMCheckoutCurrentSelectedViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMCheckoutViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMVoucherViewModel;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GMCheckoutView extends CustomerView {
    void renderCurrentSelectedProduct(GMCheckoutCurrentSelectedViewModel gmCheckoutCurrentSelectedViewModel);

    void updateSelectedProduct(int selectedProduct);

    void updateSelectedAutoProduct(int autoExtendSelectedProduct);

    void renderAutoSubscribeProduct(GMAutoSubscribeViewModel gmAutoSubscribeViewModel);

    void renderVoucherView(GMVoucherViewModel gmVoucherViewModel);

    void goToDynamicPayment(GMCheckoutViewModel gmCheckoutDomainModel);

    void failedGetCurrentProduct();

    void failedGetAutoSubscribeProduct();

    void failedCheckout();

    void showProgressDialog();

    void dismissProgressDialog();

    void showGenericError();
}
