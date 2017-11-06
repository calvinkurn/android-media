package com.tokopedia.seller.shopsettings.address.presenter;

import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 02/11/17.
 */

public class ShopAddressFormPresenterImpl implements ShopAddressFormPresenter {

    private ArrayList<String> zipCodes;
    private Address selectedAddress;

    @Override
    public void saveAddress() {

    }

    @Override
    public ArrayList<String> getZipCodes() {
        return zipCodes;
    }

    @Override
    public void setZipCodes(ArrayList<String> zipCodes) {
        this.zipCodes = zipCodes;
    }

    @Override
    public Address getSelectedAddress() {
        return selectedAddress;
    }

    @Override
    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

}
