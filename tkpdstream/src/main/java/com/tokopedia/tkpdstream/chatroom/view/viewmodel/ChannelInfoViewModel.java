package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

import java.util.List;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel implements Parcelable {
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private String blurredBannerUrl;
    private boolean hasPoll;
    private String adsImageUrl;
    private String adsLink;
    private String bannerName;
    private String sendBirdToken;
    private String adminName;
    private String image;
    private String adminPicture;
    private String description;
    private String totalView;
    private List<ChannelPartnerViewModel> channelPartnerViewModels;

    @Nullable
    private VoteInfoViewModel voteInfoViewModel;

    @Nullable
    private SprintSaleViewModel sprintSaleViewModel;

    public ChannelInfoViewModel(String title, String channelUrl, String bannerUrl,
                                String blurredBannerUrl, boolean hasPoll, String adsImageUrl,
                                String adsLink, String bannerName, String sendBirdToken,
                                String adminName, String image, String adminPicture,
                                String description, String totalView,
                                List<ChannelPartnerViewModel> channelPartnerViewModels,
                                @Nullable VoteInfoViewModel voteInfoViewModel,
                                @Nullable SprintSaleViewModel sprintSaleViewModel) {
        this.title = title;
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.blurredBannerUrl = blurredBannerUrl;
        this.title = title;
        this.hasPoll = hasPoll;
        this.adsImageUrl = adsImageUrl;
        this.adsLink = adsLink;
        this.bannerName = bannerName;
        this.sendBirdToken = sendBirdToken;
        this.adminName = adminName;
        this.image = image;
        this.adminPicture = adminPicture;
        this.description = description;
        this.totalView = totalView;
        this.channelPartnerViewModels = channelPartnerViewModels;
        this.voteInfoViewModel = voteInfoViewModel;
        this.sprintSaleViewModel = sprintSaleViewModel;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getBlurredBannerUrl() {
        return blurredBannerUrl;
    }

    public String getTitle() {
        return title;
    }

    public boolean isHasPoll() {
        return hasPoll;
    }

    public String getAdsImageUrl() {
        return adsImageUrl;
    }

    public String getAdsLink() {
        return adsLink;
    }

    public String getBannerName() {
        return bannerName;
    }

    public String getSendBirdToken() {
        return sendBirdToken;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getImage() {
        return image;
    }

    public String getAdminPicture() {
        return adminPicture;
    }

    public String getDescription() {
        return description;
    }

    void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getTotalView() {
        return totalView;
    }

    public List<ChannelPartnerViewModel> getChannelPartnerViewModels() {
        return channelPartnerViewModels;
    }

    @Nullable
    public VoteInfoViewModel getVoteInfoViewModel() {
        return voteInfoViewModel;
    }

    public void setVoteInfoViewModel(@Nullable VoteInfoViewModel voteInfoViewModel) {
        this.voteInfoViewModel = voteInfoViewModel;
    }

    @Nullable
    public SprintSaleViewModel getSprintSaleViewModel() {
        return sprintSaleViewModel;
    }

    public void setSprintSaleViewModel(@Nullable SprintSaleViewModel sprintSaleViewModel) {
        this.sprintSaleViewModel = sprintSaleViewModel;
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
        dest.writeString(this.blurredBannerUrl);
        dest.writeByte(this.hasPoll ? (byte) 1 : (byte) 0);
        dest.writeString(this.adsImageUrl);
        dest.writeString(this.adsLink);
        dest.writeString(this.bannerName);
        dest.writeString(this.sendBirdToken);
        dest.writeString(this.adminName);
        dest.writeString(this.image);
        dest.writeString(this.adminPicture);
        dest.writeString(this.description);
        dest.writeString(this.totalView);
        dest.writeTypedList(this.channelPartnerViewModels);
        dest.writeParcelable(this.voteInfoViewModel, flags);
        dest.writeParcelable(this.sprintSaleViewModel, flags);
    }

    protected ChannelInfoViewModel(Parcel in) {
        this.title = in.readString();
        this.channelUrl = in.readString();
        this.bannerUrl = in.readString();
        this.blurredBannerUrl = in.readString();
        this.hasPoll = in.readByte() != 0;
        this.adsImageUrl = in.readString();
        this.adsLink = in.readString();
        this.bannerName = in.readString();
        this.sendBirdToken = in.readString();
        this.adminName = in.readString();
        this.image = in.readString();
        this.adminPicture = in.readString();
        this.description = in.readString();
        this.totalView = in.readString();
        this.channelPartnerViewModels = in.createTypedArrayList(ChannelPartnerViewModel.CREATOR);
        this.voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        this.sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
    }

    public static final Creator<ChannelInfoViewModel> CREATOR = new Creator<ChannelInfoViewModel>() {
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
