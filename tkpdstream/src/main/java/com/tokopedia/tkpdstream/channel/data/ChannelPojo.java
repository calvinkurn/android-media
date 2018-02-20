package com.tokopedia.tkpdstream.channel.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by StevenFredian on 13/02/18.
 */

public class ChannelPojo {

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
    @SerializedName("participants")
    @Expose
    private Object participants;
    @SerializedName("operators")
    @Expose
    private Object operators;
    @SerializedName("start_time")
    @Expose
    private int startTime;
    @SerializedName("end_time")
    @Expose
    private int endTime;
    @SerializedName("total_participants_online")
    @Expose
    private int totalParticipantsOnline;
    @SerializedName("active")
    @Expose
    private boolean active;

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

    public Object getParticipants() {
        return participants;
    }

    public void setParticipants(Object participants) {
        this.participants = participants;
    }

    public Object getOperators() {
        return operators;
    }

    public void setOperators(Object operators) {
        this.operators = operators;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getTotalParticipantsOnline() {
        return totalParticipantsOnline;
    }

    public void setTotalParticipantsOnline(int totalParticipantsOnline) {
        this.totalParticipantsOnline = totalParticipantsOnline;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
