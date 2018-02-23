package com.tokopedia.transaction.checkout.view.data.mapper;

import com.tokopedia.transaction.checkout.domain.model.ShipmentAddressModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 23/02/18.
 */

public class ShipmentRecipientViewModelMapper {

    public ShipmentRecipientViewModelMapper() {

    }

    public ShipmentRecipientModel transform(ShipmentAddressModel shipmentAddressModel) {
        ShipmentRecipientModel shipmentRecipientModel = new ShipmentRecipientModel();

        return shipmentRecipientModel;
    }

    public List<ShipmentRecipientModel> transform(List<ShipmentAddressModel> shipmentAddressModels) {
        List<ShipmentRecipientModel> shipmentRecipientModels = new ArrayList<>();

        return shipmentRecipientModels;
    }

}
