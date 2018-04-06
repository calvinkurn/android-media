package com.tokopedia.transaction.addtocart.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 4/6/18.
 */

public class LogisticsData {

    @SerializedName("data")
    @Expose
    private OngkirData ongkirData;


    public OngkirData getOngkirData() {
        return ongkirData;
    }

    public void setOngkirData(OngkirData ongkirData) {
        this.ongkirData = ongkirData;
    }
}
