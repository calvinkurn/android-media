package com.tokopedia.seller.gmsubscribe.view.checkout.fragment;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GMCheckoutFragmentCallback {

    void changeCurrentSelected(Integer selectedProduct);

    void selectAutoSubscribePackageFirstTime();

    void changeAutoSubscribePackage(Integer autoExtendSelectedProduct);

    void goToDynamicPayment(String paymentUrl, String parameter);
}
