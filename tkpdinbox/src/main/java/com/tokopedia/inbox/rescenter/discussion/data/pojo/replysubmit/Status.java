package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/5/17.
 */

public class Status {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
