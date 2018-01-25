package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailPresenter extends BaseDaggerPresenter<IShipmentDetailView>
        implements IShipmentDetailPresenter {

    private ShipmentDetailData shipmentDetailData;

    @Override
    public void attachView(IShipmentDetailView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadShipmentData() {

    }
}
