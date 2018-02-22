package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupAddress {
    private List<String> errors = new ArrayList<>();
    private UserAddress userAddress;

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
}
