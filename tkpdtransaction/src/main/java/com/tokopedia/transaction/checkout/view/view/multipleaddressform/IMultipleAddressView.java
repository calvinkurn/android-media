package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressView {

    void successMakeShipmentData();

    void receiveData(List<MultipleAddressAdapterData> listOfAddressData);

}
