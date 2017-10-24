
package com.tokopedia.inbox.inboxchat.domain.model.search;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contacts {

    @SerializedName("has_next")
    @Expose
    private int hasNext;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public int getHasNext() {
        return hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
