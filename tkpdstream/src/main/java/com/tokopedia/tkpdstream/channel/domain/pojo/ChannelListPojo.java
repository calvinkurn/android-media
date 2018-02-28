
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
    private List<ChannelPojo> channels = null;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<ChannelPojo> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelPojo> channels) {
        this.channels = channels;
    }

}
