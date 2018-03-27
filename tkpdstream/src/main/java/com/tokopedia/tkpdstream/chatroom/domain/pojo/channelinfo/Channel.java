
package com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.ActivePollPojo;

public class Channel {

    @SerializedName("channel_id")
    @Expose
    private int channelId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("channel_url")
    @Expose
    private String channelUrl;
    @SerializedName("cover_url")
    @Expose
    private String coverUrl;
    @SerializedName("start_time")
    @Expose
    private long startTime;
    @SerializedName("end_time")
    @Expose
    private long endTime;
    @SerializedName("total_participants_online")
    @Expose
    private String totalParticipantsOnline;
    @SerializedName("total_views")
    @Expose
    private String totalViews;
    @SerializedName("is_active")
    @Expose
    private boolean isActive;
    @SerializedName("is_freeze")
    @Expose
    private boolean isFreeze;
    @SerializedName("active_poll")
    @Expose
    private ActivePollPojo activePolls = null;
    @SerializedName("moderator_id")
    @Expose
    private String moderatorId;
    @SerializedName("moderator_name")
    @Expose
    private String moderatorName;
    @SerializedName("moderator_thumb_url")
    @Expose
    private String moderatorProfileUrl;
    @SerializedName("banner_url")
    @Expose
    private String bannerImageUrl;
    @SerializedName("ads_url")
    @Expose
    private String adsUrl;
    @SerializedName("ads_link")
    @Expose
    private String adsLink;
    @SerializedName("banner_name")
    @Expose
    private String bannerName;
    @SerializedName("sendbird_access_token")
    @Expose
    private String sendBirdToken;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTotalParticipantsOnline() {
        return totalParticipantsOnline;
    }

    public void setTotalParticipantsOnline(String totalParticipantsOnline) {
        this.totalParticipantsOnline = totalParticipantsOnline;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(boolean isFreeze) {
        this.isFreeze = isFreeze;
    }

    public ActivePollPojo getActivePolls() {
        return activePolls;
    }

    public void setActivePolls(ActivePollPojo activePolls) {
        this.activePolls = activePolls;
    }

    public String getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }

    public String getModeratorName() {
        return moderatorName;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }

    public String getModeratorProfileUrl() {
        return moderatorProfileUrl;
    }

    public void setModeratorProfileUrl(String moderatorProfileUrl) {
        this.moderatorProfileUrl = moderatorProfileUrl;
    }

    public String getTotalViews() {
        return totalViews;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public String getAdsUrl() {
        return adsUrl;
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
}
