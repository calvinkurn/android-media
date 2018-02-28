package com.tokopedia.transaction.checkout.view.view.shippingoptions;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentChoiceView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void showData();
}
