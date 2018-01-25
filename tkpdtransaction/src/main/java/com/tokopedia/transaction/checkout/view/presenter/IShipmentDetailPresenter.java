package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailPresenter extends CustomerPresenter<IShipmentDetailView> {

    void loadShipmentData();

}
