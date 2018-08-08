package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationButtonResponse {

    @SerializedName("trackAwb")
    private int trackAwb;

    @SerializedName("editAwb")
    private int editAwb;

    @SerializedName("editAddress")
    private int editAddress;

    public int getTrackAwb() {
        return trackAwb;
    }

    public void setTrackAwb(int trackAwb) {
        this.trackAwb = trackAwb;
    }

    public int getEditAwb() {
        return editAwb;
    }

    public void setEditAwb(int editAwb) {
        this.editAwb = editAwb;
    }

    public int getEditAddress() {
        return editAddress;
    }

    public void setEditAddress(int editAddress) {
        this.editAddress = editAddress;
    }
}
