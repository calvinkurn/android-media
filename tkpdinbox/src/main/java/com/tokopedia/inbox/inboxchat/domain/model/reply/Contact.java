
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("interlocutor")
    @Expose
    private boolean interlocutor;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("shop_id")
    @Expose
    private int shopId;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public boolean isInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(boolean interlocutor) {
        this.interlocutor = interlocutor;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

}
