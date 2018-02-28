package com.tokopedia.session.register.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnResponse {

    @SerializedName("isExist")
    private boolean isExist;

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
