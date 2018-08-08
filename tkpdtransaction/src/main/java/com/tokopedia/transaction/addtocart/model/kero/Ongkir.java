package com.tokopedia.transaction.addtocart.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 4/6/18.
 */

public class Ongkir {

    @SerializedName("rates")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
