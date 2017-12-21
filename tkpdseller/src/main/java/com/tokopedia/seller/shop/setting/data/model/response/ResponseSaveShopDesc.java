package com.tokopedia.seller.shop.setting.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class ResponseSaveShopDesc {

    @SerializedName("ServerProcessTime")
    @Expose
    private String serverProcessTime;
    @SerializedName("message_status")
    @Expose
    private String messageStatus;
    @SerializedName("reserve_status")
    @Expose
    private String reserveStatus;

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(String reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

}
