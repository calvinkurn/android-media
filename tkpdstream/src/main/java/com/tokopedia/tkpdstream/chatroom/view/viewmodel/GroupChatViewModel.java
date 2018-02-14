package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

/**
 * @author by nisie on 2/14/18.
 */

public class GroupChatViewModel {

    String channelUrl;

    public GroupChatViewModel(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public GroupChatViewModel() {
        this.channelUrl = "pub1";
    }

    public String getChannelUrl() {
        return channelUrl;
    }
}
