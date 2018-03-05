package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

public interface IAddShipmentAddressPresenter {

    void initiateData(MultipleAddressAdapterData addressAdapterData,
                      MultipleAddressItemData addressItemData);

    MultipleAddressItemData confirmAddData(String quantity, String notes);

    MultipleAddressItemData confirmEditData(String quantity, String notes);

    RecipientAddressModel getEditableModel();

    void setEditableModel(RecipientAddressModel newEditableModel);

    MultipleAddressAdapterData getMultipleAddressAdapterData();

    MultipleAddressItemData getMultipleItemData();

}
