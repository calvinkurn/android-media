package com.tokopedia.transaction.checkout.data.mapper;

import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.transaction.checkout.domain.model.ShipmentAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 23/02/18
 */

public class PeopleAddressMapper {

    public PeopleAddressMapper() {

    }

    public ShipmentAddressModel transform(AddressModel addressModel) {
        ShipmentAddressModel shipmentAddressModel = new ShipmentAddressModel();
        return shipmentAddressModel;
    }

    public List<ShipmentAddressModel> transform(List<AddressModel> addressModels) {
        List<ShipmentAddressModel> shipmentAddressModels = new ArrayList<>();
        for (AddressModel addressModel : addressModels) {

        }

        return shipmentAddressModels;
    }

}
