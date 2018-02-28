package com.tokopedia.transaction.checkout.data.entity.response.notifcounter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class NotifCounterCartDataResponse {
    @SerializedName("counter")
    @Expose
    private int counter;

    public int getCounter() {
        return counter;
    }
}
