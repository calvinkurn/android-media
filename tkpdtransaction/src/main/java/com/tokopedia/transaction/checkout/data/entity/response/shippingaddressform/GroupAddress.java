package com.tokopedia.transaction.checkout.data.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupAddress {
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("messages")
    @Expose
    private List<String> messages = new ArrayList<>();
    @SerializedName("user_address")
    @Expose
    private UserAddress userAddress;
    @SerializedName("group_shop")
    @Expose
    private List<GroupShop> groupShop = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public List<GroupShop> getGroupShop() {
        return groupShop;
    }

    public List<String> getMessages() {
        return messages;
    }
}
