package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel {
    private String channelUrl;

    public ChannelInfoViewModel(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }
}
