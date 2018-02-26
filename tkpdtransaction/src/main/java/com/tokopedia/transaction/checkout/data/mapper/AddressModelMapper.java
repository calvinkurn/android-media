package com.tokopedia.transaction.checkout.data.mapper;

import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 23/02/18
 */

public class AddressModelMapper {

    public AddressModelMapper() {

    }

    public RecipientAddressModel transform(AddressModel addressModel) {
        RecipientAddressModel recipientAddressModel = new RecipientAddressModel();

        recipientAddressModel.setId(addressModel.getAddressId());
        recipientAddressModel.setAddressStatus(addressModel.getAddressStatus());
        recipientAddressModel.setAddressName(addressModel.getAddressName());
        recipientAddressModel.setAddressStreet(addressModel.getAddressStreet());
        recipientAddressModel.setDestinationDistrictId(addressModel.getDistrictId());
        recipientAddressModel.setDestinationDistrictName(addressModel.getDistrictName());
        recipientAddressModel.setAddressCityName(addressModel.getCityName());
        recipientAddressModel.setAddressProvinceName(addressModel.getProvinceName());
        recipientAddressModel.setAddressCountryName(addressModel.getCountryName());
        recipientAddressModel.setAddressPostalCode(addressModel.getPostalCode());
        recipientAddressModel.setRecipientName(addressModel.getReceiverName());
        recipientAddressModel.setRecipientPhoneNumber(addressModel.getReceiverPhone());

        return recipientAddressModel;
    }

    public List<RecipientAddressModel> transform(List<AddressModel> addressModels) {
        List<RecipientAddressModel> recipientAddressModels = new ArrayList<>();
        for (AddressModel addressModel : addressModels) {
            recipientAddressModels.add(transform(addressModel));
        }

        return recipientAddressModels;
    }

    public AddressModel transform(RecipientAddressModel recipientAddressModel) {
        AddressModel addressModel = new AddressModel();

        addressModel.setAddressId(recipientAddressModel.getId());
        addressModel.setAddressStatus(recipientAddressModel.getAddressStatus());
        addressModel.setAddressName(recipientAddressModel.getAddressName());
        addressModel.setAddressStreet(recipientAddressModel.getAddressStreet());
        addressModel.setDistrictId(recipientAddressModel.getDestinationDistrictId());
        addressModel.setDistrictName(recipientAddressModel.getDestinationDistrictName());
        addressModel.setCityName(recipientAddressModel.getAddressCityName());
        addressModel.setProvinceName(recipientAddressModel.getAddressProvinceName());
        addressModel.setCountryName(recipientAddressModel.getAddressCountryName());
        addressModel.setPostalCode(recipientAddressModel.getAddressPostalCode());
        addressModel.setReceiverName(recipientAddressModel.getRecipientName());
        addressModel.setReceiverPhone(recipientAddressModel.getRecipientPhoneNumber());

        return addressModel;
    }

}
