
package com.tokopedia.inbox.inboxchat.domain.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchedMessage {

    @SerializedName("contacts")
    @Expose
    private Contacts contacts;
    @SerializedName("replies")
    @Expose
    private Replies replies;

    @SerializedName("time_machine_status")
    @Expose
    private int timeMachineStatus;


    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public Replies getReplies() {
        return replies;
    }

    public void setReplies(Replies replies) {
        this.replies = replies;
    }

    public int getTimeMachineStatus() {
        return timeMachineStatus;
    }

    public void setTimeMachineStatus(int timeMachineStatus) {
        this.timeMachineStatus = timeMachineStatus;
    }
}
