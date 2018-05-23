package com.tokopedia.inbox.inboxchat.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;

import java.util.List;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachInvoiceSelectionViewModel implements Visitable<ChatRoomTypeFactory> {
    private String replyTime;
    List<AttachInvoiceSingleViewModel> list;

    public AttachInvoiceSelectionViewModel(String replyTime, List<AttachInvoiceSingleViewModel> list) {
        this.replyTime = replyTime;
        this.list = list;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public List<AttachInvoiceSingleViewModel> getList() {
        return list;
    }

    public void setList(List<AttachInvoiceSingleViewModel> list) {
        this.list = list;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
