package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel {
    private String title;
    private int totalParticipantsOnline;
    private String channelUrl;
    private String bannerUrl;

    public ChannelInfoViewModel(String channelUrl, String bannerUrl, String title, int totalParticipantsOnline) {
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.totalParticipantsOnline = totalParticipantsOnline;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalParticipantsOnline() {
        return totalParticipantsOnline;
    }

    public void setTotalParticipant(int totalParticipant) {
        this.totalParticipantsOnline = totalParticipant;
    }
}
