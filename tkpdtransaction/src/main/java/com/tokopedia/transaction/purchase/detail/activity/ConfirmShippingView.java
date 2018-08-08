package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;

import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

/**
 * Created by kris on 1/5/18. Tokopedia
 */

public interface ConfirmShippingView {

    void receiveShipmentData(ListCourierViewModel model);

    void onSuccessConfirm(String successMessage);

    void showLoading();

    void hideLoading();

    void onShowError(String errorMessage);
}
