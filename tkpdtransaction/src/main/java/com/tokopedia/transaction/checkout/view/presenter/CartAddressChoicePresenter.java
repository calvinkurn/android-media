package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoicePresenter extends BaseDaggerPresenter<ICartAddressChoiceView>
        implements ICartAddressChoicePresenter {

    private ShipmentRecipientModel shipmentRecipientModel;

    @Override
    public void attachView(ICartAddressChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void setRecipientAddress(ShipmentRecipientModel recipientAddress) {
        shipmentRecipientModel = recipientAddress;
        getView().renderRecipientData(shipmentRecipientModel);
    }

    @Override
    public ShipmentRecipientModel getRecipientAddress() {
        return shipmentRecipientModel;
    }
}
