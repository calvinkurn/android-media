package com.tokopedia.transaction.bcaoneklik.model.bcaoneclick;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickData {

    @SerializedName("token")
    private BcaOneClickToken token;

    public BcaOneClickToken getToken() {
        return token;
    }

    public void setToken(BcaOneClickToken token) {
        this.token = token;
    }

}
