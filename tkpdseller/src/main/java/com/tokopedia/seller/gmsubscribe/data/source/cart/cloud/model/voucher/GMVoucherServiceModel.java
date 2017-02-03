
package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GMVoucherServiceModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
