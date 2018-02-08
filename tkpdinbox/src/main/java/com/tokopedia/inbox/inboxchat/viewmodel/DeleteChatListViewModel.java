package com.tokopedia.inbox.inboxchat.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stevenfredian on 10/30/17.
 */

public class DeleteChatListViewModel {

    @SerializedName("list")
    @Expose
    private List<DeleteChatViewModel> list;

    public List<DeleteChatViewModel> getList() {
        return list;
    }

    public void setList(List<DeleteChatViewModel> list) {
        this.list = list;
    }
}