
package com.tokopedia.inbox.inboxmessageold.model.inboxmessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;

public class InboxMessage {

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private java.util.List<InboxMessageItem> list = new ArrayList<InboxMessageItem>();

    public InboxMessage(ArrayList<InboxMessageItem> list) {
        this.list = list;
        this.paging = new PagingHandler.PagingHandlerModel();
    }

    /**
     * @return The paging
     */
    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    /**
     * @param paging The paging
     */
    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    /**
     * @return The list
     */
    public java.util.List<InboxMessageItem> getList() {
        return list;
    }

    /**
     * @param list The list
     */
    public void setList(java.util.List<InboxMessageItem> list) {
        this.list = list;
    }

}
