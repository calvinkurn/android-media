package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.support.annotation.Nullable;

import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel {
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private boolean hasPoll;

    @Nullable
    private VoteInfoViewModel voteInfoViewModel;
    private ChannelViewModel channelViewModel;

    public ChannelInfoViewModel(String channelUrl, String bannerUrl, String title, boolean hasPoll,
                                @Nullable VoteInfoViewModel voteInfoViewModel,
                                ChannelViewModel channelViewModel) {
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.hasPoll = hasPoll;
        this.voteInfoViewModel = voteInfoViewModel;
        this.channelViewModel = channelViewModel;
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
        return channelViewModel != null ? channelViewModel.getParticipant() : 0;
    }

    public void setTotalParticipant(int totalParticipant) {
        if (channelViewModel != null) {
            channelViewModel.setParticipant(totalParticipant);
        }
    }

    public void setHasPoll(boolean hasPoll) {
        this.hasPoll = hasPoll;
    }

    public boolean isHasPoll() {
        return hasPoll;
    }

    @Nullable
    public VoteInfoViewModel getVoteInfoViewModel() {
        return voteInfoViewModel;
    }

    public ChannelViewModel getChannelViewModel() {
        return channelViewModel;
    }

    public void setChannelViewModel(ChannelViewModel channelViewModel) {
        this.channelViewModel = channelViewModel;
    }
}
