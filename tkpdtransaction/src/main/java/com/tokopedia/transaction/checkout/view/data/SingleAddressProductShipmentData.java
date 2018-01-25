package com.tokopedia.transaction.checkout.view.data;

import java.util.List;

/**
 * The type Single address product shipment data.
 *
 * @author Aghny A. Putra on 25/01/18
 */
public class SingleAddressProductShipmentData {

    private SingleAddressShipmentFeeModel singleAddressShipmentFeeModel;
    private SingleAddressRecipientAddressModel singleAddressRecipientAddressModel;
    private SingleAddressDropshipperOptionModel singleAddressDropshipperOptionModel;
    private List<SingleAddressShippedItemModel> singleAddressShippedItemModelList;
    private SingleAddressCartPayableDetailModel singleAddressCartPayableDetailModel;

    public SingleAddressShipmentFeeModel getSingleAddressShipmentFeeModel() {
        return singleAddressShipmentFeeModel;
    }

    public void setSingleAddressShipmentFeeModel(SingleAddressShipmentFeeModel singleAddressShipmentFeeModel) {
        this.singleAddressShipmentFeeModel = singleAddressShipmentFeeModel;
    }

    public SingleAddressRecipientAddressModel getSingleAddressRecipientAddressModel() {
        return singleAddressRecipientAddressModel;
    }

    public void setSingleAddressRecipientAddressModel(SingleAddressRecipientAddressModel singleAddressRecipientAddressModel) {
        this.singleAddressRecipientAddressModel = singleAddressRecipientAddressModel;
    }

    public SingleAddressDropshipperOptionModel getSingleAddressDropshipperOptionModel() {
        return singleAddressDropshipperOptionModel;
    }

    public void setSingleAddressDropshipperOptionModel(SingleAddressDropshipperOptionModel singleAddressDropshipperOptionModel) {
        this.singleAddressDropshipperOptionModel = singleAddressDropshipperOptionModel;
    }

    public List<SingleAddressShippedItemModel> getSingleAddressShippedItemModelList() {
        return singleAddressShippedItemModelList;
    }

    public void setSingleAddressShippedItemModelList(List<SingleAddressShippedItemModel> singleAddressShippedItemModelList) {
        this.singleAddressShippedItemModelList = singleAddressShippedItemModelList;
    }

    public SingleAddressCartPayableDetailModel getSingleAddressCartPayableDetailModel() {
        return singleAddressCartPayableDetailModel;
    }

    public void setSingleAddressCartPayableDetailModel(SingleAddressCartPayableDetailModel singleAddressCartPayableDetailModel) {
        this.singleAddressCartPayableDetailModel = singleAddressCartPayableDetailModel;
    }

}
