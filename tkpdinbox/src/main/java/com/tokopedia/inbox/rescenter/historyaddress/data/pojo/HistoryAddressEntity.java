package com.tokopedia.inbox.rescenter.historyaddress.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/27/17.
 */

public class HistoryAddressEntity {

    @SerializedName("listHistoryAddress")
    private List<ListHistoryAddress> listHistoryAddress;

    public List<ListHistoryAddress> getListHistoryAddress() {
        return listHistoryAddress;
    }

    public void setListHistoryAddress(List<ListHistoryAddress> listHistoryAddress) {
        this.listHistoryAddress = listHistoryAddress;
    }

}
