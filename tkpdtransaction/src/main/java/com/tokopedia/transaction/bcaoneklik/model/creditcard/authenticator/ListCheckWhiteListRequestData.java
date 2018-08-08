package com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 10/13/17. Tokopedia
 */

public class ListCheckWhiteListRequestData {

    @SerializedName("data")
    @Expose
    private List<CheckWhiteListRequestData> listData;

    public List<CheckWhiteListRequestData> getListData() {
        return listData;
    }

    public void setListData(List<CheckWhiteListRequestData> listData) {
        this.listData = listData;
    }
}
