package com.tokopedia.seller.shopsettings.address.presenter;

import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 02/11/17.
 */

public interface ShopAddressFormPresenter {

    void saveAddress();

    ArrayList<String> getZipCodes();

    void setZipCodes(ArrayList<String> zipCodes);

    Address getSelectedAddress();

    void setSelectedAddress(Address selectedAddress);

}
