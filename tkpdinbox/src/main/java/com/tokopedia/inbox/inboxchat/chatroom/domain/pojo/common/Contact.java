
package com.tokopedia.inbox.inboxchat.domain.pojo.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attributes;

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

    public boolean isInterlocutor() {
        return interlocutor;
    }

    public String getRole() {
        return role;
    }

    public int getUserId() {
        return userId;
    }

    public int getShopId() {
        return shopId;
    }

}
