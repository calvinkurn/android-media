package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentChoiceView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void showData();
}
