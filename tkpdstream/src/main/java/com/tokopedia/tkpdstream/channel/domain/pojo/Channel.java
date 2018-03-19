
package com.tokopedia.tkpdstream.channel.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private int totalParticipantsOnline;
    @SerializedName("is_active")
    @Expose
    private boolean isActive;
    @SerializedName("is_freeze")
    @Expose
    private boolean isFreeze;
    @SerializedName("active_poll")
    @Expose
    private ActivePoll activePoll;
    @SerializedName("moderator_sendbird_id")
    @Expose
    private String moderatorSendbirdId;
    @SerializedName("moderator_name")
    @Expose
    private String moderatorName;
    @SerializedName("moderator_thumb_url")
    @Expose
    private String moderatorThumbUrl;

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

    public int getTotalParticipantsOnline() {
        return totalParticipantsOnline;
    }

    public void setTotalParticipantsOnline(int totalParticipantsOnline) {
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

    public ActivePoll getActivePoll() {
        return activePoll;
    }

    public void setActivePoll(ActivePoll activePoll) {
        this.activePoll = activePoll;
    }

    public String getModeratorSendbirdId() {
        return moderatorSendbirdId;
    }

    public void setModeratorSendbirdId(String moderatorSendbirdId) {
        this.moderatorSendbirdId = moderatorSendbirdId;
    }

    public String getModeratorName() {
        return moderatorName;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }

    public String getModeratorThumbUrl() {
        return moderatorThumbUrl;
    }

    public void setModeratorThumbUrl(String moderatorThumbUrl) {
        this.moderatorThumbUrl = moderatorThumbUrl;
    }

}
