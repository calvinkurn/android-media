package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel implements Parcelable {
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private String blurredBannerUrl;
    private String adsImageUrl;
    private String adsLink;
    private String bannerName;
    private String sendBirdToken;

    @Nullable
    private VoteInfoViewModel voteInfoViewModel;

    private ChannelViewModel channelViewModel;

    @Nullable
    private SprintSaleViewModel sprintSaleViewModel;

    @Nullable
    private GroupChatPointsViewModel groupChatPointsViewModel;

    public ChannelInfoViewModel(String channelUrl, String bannerUrl, String blurredBannerUrl,
                                String title,
                                String adsImageUrl, String adsLink, String bannerName,
                                @Nullable VoteInfoViewModel voteInfoViewModel,
                                ChannelViewModel channelViewModel,
                                @Nullable SprintSaleViewModel sprintSaleViewModel, String sendBirdToken,
                                @Nullable GroupChatPointsViewModel groupChatPointsViewModel) {
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.blurredBannerUrl = blurredBannerUrl;
        this.title = title;
        this.adsImageUrl = adsImageUrl;
        this.adsLink = adsLink;
        this.bannerName = bannerName;
        this.voteInfoViewModel = voteInfoViewModel;
        this.channelViewModel = channelViewModel;
        this.sprintSaleViewModel = sprintSaleViewModel;
        this.sendBirdToken = sendBirdToken;
        this.groupChatPointsViewModel = groupChatPointsViewModel;
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

    public String getChannelUrl() {
        return channelUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTotalView() {
        return channelViewModel != null ? channelViewModel.getTotalView() : "0";
    }

    public void setTotalView(String totalView) {
        if (channelViewModel != null) {
            channelViewModel.setTotalView(totalView);
        }
    }

    public String getAdsImageUrl() {
        return adsImageUrl;
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


    public String getBlurredBannerUrl() {
        return blurredBannerUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Nullable
    public GroupChatPointsViewModel getGroupChatPointsViewModel() {
        return groupChatPointsViewModel;
    }

    public void setGroupChatPointsViewModel(@Nullable GroupChatPointsViewModel groupChatPointsViewModel) {
        this.groupChatPointsViewModel = groupChatPointsViewModel;
    }

    protected ChannelInfoViewModel(Parcel in) {
        title = in.readString();
        channelUrl = in.readString();
        bannerUrl = in.readString();
        adsLink = in.readString();
        bannerName = in.readString();
        voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        channelViewModel = in.readParcelable(ChannelViewModel.class.getClassLoader());
        sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
        sendBirdToken = in.readString();
        groupChatPointsViewModel = in.readParcelable(GroupChatPointsViewModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(channelUrl);
        dest.writeString(bannerUrl);
        dest.writeString(blurredBannerUrl);
        dest.writeString(adsImageUrl);
        dest.writeString(adsLink);
        dest.writeString(bannerName);
        dest.writeString(sendBirdToken);
        dest.writeParcelable(voteInfoViewModel, flags);
        dest.writeParcelable(channelViewModel, flags);
        dest.writeParcelable(sprintSaleViewModel, flags);
        dest.writeString(sendBirdToken);
        dest.writeParcelable(groupChatPointsViewModel, flags);
    }
}
