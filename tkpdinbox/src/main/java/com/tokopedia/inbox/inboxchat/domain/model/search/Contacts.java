
package com.tokopedia.inbox.inboxchat.domain.model.search;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contacts {

    @SerializedName("has_next")
    @Expose
    private boolean hasNext;
    @SerializedName("data")
    @Expose
    private List<RepliesContent> data = null;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<RepliesContent> getData() {
        return data;
    }

    public void setData(List<RepliesContent> data) {
        this.data = data;
    }

}
