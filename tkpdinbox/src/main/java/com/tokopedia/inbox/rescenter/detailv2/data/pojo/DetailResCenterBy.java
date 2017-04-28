package com.tokopedia.inbox.rescenter.detailv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/20/17.
 */

@SuppressWarnings("ALL")
public class DetailResCenterBy {

    @SerializedName("customer")
    private int customer;

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }
}
