package com.tokopedia.transaction.checkout.domain.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressRequestList {

    @SerializedName("carts")
    @Expose
    List<MultipleAddressRequest> addressRequests;

    public void setAddressRequests(List<MultipleAddressRequest> addressRequests) {
        this.addressRequests = addressRequests;
    }
}
