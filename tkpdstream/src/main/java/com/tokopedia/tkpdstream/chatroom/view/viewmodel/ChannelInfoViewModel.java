package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel implements Parcelable {
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private boolean hasPoll;
    private String sponsorUrl;
    private String adsLink;
    private String bannerName;
    private String sendBirdToken;

    @Nullable
    private VoteInfoViewModel voteInfoViewModel;

    private ChannelViewModel channelViewModel;

    @Nullable
    private SprintSaleViewModel sprintSaleViewModel;

    public ChannelInfoViewModel(String channelUrl, String bannerUrl, String title, boolean hasPoll,
                                String sponsorUrl, String adsLink, String bannerName, @Nullable VoteInfoViewModel voteInfoViewModel,
                                ChannelViewModel channelViewModel,
                                @Nullable SprintSaleViewModel sprintSaleViewModel, String sendBirdToken) {
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.title = title;
        this.hasPoll = hasPoll;
        this.sponsorUrl = sponsorUrl;
        this.adsLink = adsLink;
        this.bannerName = bannerName;
        this.voteInfoViewModel = voteInfoViewModel;
        this.channelViewModel = channelViewModel;
        this.sprintSaleViewModel = sprintSaleViewModel;
        this.sendBirdToken = sendBirdToken;
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

    public String getAdsLink() {
        return adsLink;
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

    public String getBannerName() {
        return bannerName;
    }

    @Nullable
    public SprintSaleViewModel getSprintSaleViewModel() {
        return sprintSaleViewModel;
    }

    public void setSprintSaleViewModel(@Nullable SprintSaleViewModel sprintSaleViewModel) {
        this.sprintSaleViewModel = sprintSaleViewModel;
    }

    public String getSendBirdToken() {
        return sendBirdToken;
    }

    protected ChannelInfoViewModel(Parcel in) {
        title = in.readString();
        channelUrl = in.readString();
        bannerUrl = in.readString();
        hasPoll = in.readByte() != 0;
        sponsorUrl = in.readString();
        adsLink = in.readString();
        bannerName = in.readString();
        voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        channelViewModel = in.readParcelable(ChannelViewModel.class.getClassLoader());
        sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
        sendBirdToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(channelUrl);
        dest.writeString(bannerUrl);
        dest.writeByte((byte) (hasPoll ? 1 : 0));
        dest.writeString(sponsorUrl);
        dest.writeString(adsLink);
        dest.writeString(bannerName);
        dest.writeParcelable(voteInfoViewModel, flags);
        dest.writeParcelable(channelViewModel, flags);
        dest.writeParcelable(sprintSaleViewModel, flags);
        dest.writeString(sendBirdToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelInfoViewModel> CREATOR = new Creator<ChannelInfoViewModel>() {
        @Override
        public ChannelInfoViewModel createFromParcel(Parcel in) {
            return new ChannelInfoViewModel(in);
        }

        @Override
        public ChannelInfoViewModel[] newArray(int size) {
            return new ChannelInfoViewModel[size];
        }
    };
}
