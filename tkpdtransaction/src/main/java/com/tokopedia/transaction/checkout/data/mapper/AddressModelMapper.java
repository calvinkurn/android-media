package com.tokopedia.transaction.checkout.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 23/02/18
 */

public class AddressModelMapper {

    public AddressModelMapper() {

    }

    public RecipientAddressModel transform(AddressModel addressModel) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        recipientAddress.setId(addressModel.getAddressId());
        recipientAddress.setAddressStatus(addressModel.getAddressStatus());
        recipientAddress.setAddressName(addressModel.getAddressName());
        recipientAddress.setAddressStreet(addressModel.getAddressStreet());
        recipientAddress.setDestinationDistrictId(addressModel.getDistrictId());
        recipientAddress.setDestinationDistrictName(addressModel.getDistrictName());
        recipientAddress.setAddressCityName(addressModel.getCityName());
        recipientAddress.setAddressProvinceName(addressModel.getProvinceName());
        recipientAddress.setAddressCountryName(addressModel.getCountryName());
        recipientAddress.setAddressPostalCode(addressModel.getPostalCode());
        recipientAddress.setRecipientName(addressModel.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(addressModel.getReceiverPhone());
        recipientAddress.setLatitude(!TextUtils.isEmpty(addressModel.getLatitude()) ?
                Double.parseDouble(addressModel.getLatitude()) : null);
        recipientAddress.setLongitude(!TextUtils.isEmpty(addressModel.getLongitude()) ?
                Double.parseDouble(addressModel.getLongitude()) : null);

        return recipientAddress;
    }

    public List<RecipientAddressModel> transform(List<AddressModel> addressModels) {
        List<RecipientAddressModel> recipientAddressModels = new ArrayList<>();
        for (AddressModel addressModel : addressModels) {
            recipientAddressModels.add(transform(addressModel));
        }

        return recipientAddressModels;
    }

    public AddressModel transform(RecipientAddressModel recipientAddress) {
        AddressModel addressModel = new AddressModel();

        addressModel.setAddressId(recipientAddress.getId());
        addressModel.setAddressStatus(recipientAddress.getAddressStatus());
        addressModel.setAddressName(recipientAddress.getAddressName());
        addressModel.setAddressStreet(recipientAddress.getAddressStreet());
        addressModel.setDistrictId(recipientAddress.getDestinationDistrictId());
        addressModel.setDistrictName(recipientAddress.getDestinationDistrictName());
        addressModel.setCityName(recipientAddress.getAddressCityName());
        addressModel.setProvinceName(recipientAddress.getAddressProvinceName());
        addressModel.setCountryName(recipientAddress.getAddressCountryName());
        addressModel.setPostalCode(recipientAddress.getAddressPostalCode());
        addressModel.setReceiverName(recipientAddress.getRecipientName());
        addressModel.setReceiverPhone(recipientAddress.getRecipientPhoneNumber());

        return addressModel;
    }

}