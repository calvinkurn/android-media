package com.tokopedia.gm.subscribe.view.fragment;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GmCheckoutFragmentCallback {

    void changeCurrentSelected(Integer selectedProduct);

    void selectAutoSubscribePackageFirstTime();

    void changeAutoSubscribePackage(Integer autoExtendSelectedProduct);

    void goToDynamicPayment(String url, String gmCheckoutDomainModelParameter, String callbackUrl, Integer paymentId);
}
