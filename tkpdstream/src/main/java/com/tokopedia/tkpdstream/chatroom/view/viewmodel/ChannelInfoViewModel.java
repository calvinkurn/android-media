package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel implements Parcelable{
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private boolean hasPoll;
    private String sponsorUrl;

    @Nullable
    private VoteInfoViewModel voteInfoViewModel;
    private ChannelViewModel channelViewModel;

    public ChannelInfoViewModel(String channelUrl, String bannerUrl, String title, boolean hasPoll,
                                String sponsorUrl, @Nullable VoteInfoViewModel voteInfoViewModel,
                                ChannelViewModel channelViewModel) {
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.hasPoll = hasPoll;
        this.sponsorUrl = sponsorUrl;
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

    public String getTotalParticipantsOnline() {
        return channelViewModel != null ? channelViewModel.getParticipant() : "0";
    }

    public void setTotalParticipant(String totalParticipant) {
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

    public String getSponsorUrl() {
        return sponsorUrl;
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

    public void setVoteInfoViewModel(@Nullable VoteInfoViewModel voteInfoViewModel) {
        this.voteInfoViewModel = voteInfoViewModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.channelUrl);
        dest.writeString(this.bannerUrl);
        dest.writeByte(this.hasPoll ? (byte) 1 : (byte) 0);
        dest.writeString(this.sponsorUrl);
        dest.writeParcelable(this.voteInfoViewModel, flags);
        dest.writeParcelable(this.channelViewModel, flags);
    }

    protected ChannelInfoViewModel(Parcel in) {
        this.title = in.readString();
        this.channelUrl = in.readString();
        this.bannerUrl = in.readString();
        this.hasPoll = in.readByte() != 0;
        this.sponsorUrl = in.readString();
        this.voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        this.channelViewModel = in.readParcelable(ChannelViewModel.class.getClassLoader());
    }

    public static final Creator<ChannelInfoViewModel> CREATOR = new Creator<ChannelInfoViewModel>
            () {
        @Override
        public ChannelInfoViewModel createFromParcel(Parcel source) {
            return new ChannelInfoViewModel(source);
        }

        @Override
        public ChannelInfoViewModel[] newArray(int size) {
            return new ChannelInfoViewModel[size];
        }
    };
}
