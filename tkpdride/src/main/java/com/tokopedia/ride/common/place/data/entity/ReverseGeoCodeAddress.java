package com.tokopedia.ride.common.place.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 29/12/17.
 */

public class ReverseGeoCodeAddress {

    @SerializedName("address_components")
    @Expose
    AddressComponents addressComponents[];
    public class AddressComponents{
        @SerializedName("short_name")
        @Expose
        String shortName;

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }
    }

    @SerializedName("formatted_address")
    @Expose
    String formattedAddress;

    public AddressComponents[] getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(AddressComponents addressComponents[]) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
