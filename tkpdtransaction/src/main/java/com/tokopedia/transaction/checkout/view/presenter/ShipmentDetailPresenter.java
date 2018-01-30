package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.util.ArrayList;
import java.util.List;

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
        getView().renderInstantShipment(DummyCreator.createDummyShipmentDetailData());
    }

    @Override
    public void loadAllCourier() {

    }


    private static class DummyCreator {

        private static ShipmentDetailData createDummyShipmentDetailData() {
            ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
//            shipmentDetailData.setLatitude(-6.219532);
//            shipmentDetailData.setLongitude(106.824961);

            return shipmentDetailData;
        }

    }

}
