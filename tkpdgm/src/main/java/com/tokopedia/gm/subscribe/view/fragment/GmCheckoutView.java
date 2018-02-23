package com.tokopedia.gm.subscribe.view.fragment;


import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.gm.subscribe.view.viewmodel.GmAutoSubscribeViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutCurrentSelectedViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmCheckoutViewModel;
import com.tokopedia.gm.subscribe.view.viewmodel.GmVoucherViewModel;

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

    void showProgressDialog();

    void dismissProgressDialog();

    void showGenericError();

    void dismissKeyboardFromVoucherEditText();

    void showMessageError(String string);

    void clearCacheShopInfo();

    Context getContext();
}
