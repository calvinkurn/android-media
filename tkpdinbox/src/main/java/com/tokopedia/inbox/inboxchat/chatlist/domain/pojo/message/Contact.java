
package com.tokopedia.inbox.inboxchat.chatlist.domain.pojo.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.chatlist.domain.pojo.message.ContactAttributes;

public class Contact {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("attributes")
    @Expose
    private ContactAttributes attributes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ContactAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(ContactAttributes attributes) {
        this.attributes = attributes;
    }

}
