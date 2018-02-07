package com.tokopedia.seller.shop.open.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseCreateShop {
    @SerializedName("reserve_status")
    @Expose
    private String reserveStatus;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError;
    @SerializedName("shop_id")
    @Expose
    private int shopId;

    public int getShopId() {
        return shopId;
    }

    public String getReserveStatus() {
        return reserveStatus;
    }

    public List<String> getMessageError() {
        return messageError;
    }
}
