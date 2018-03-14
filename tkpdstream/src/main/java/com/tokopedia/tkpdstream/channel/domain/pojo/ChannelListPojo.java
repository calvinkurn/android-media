
package com.tokopedia.tkpdstream.channel.domain.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelListPojo {

    @SerializedName("cursor")
    @Expose
    private String cursor;
    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

}
