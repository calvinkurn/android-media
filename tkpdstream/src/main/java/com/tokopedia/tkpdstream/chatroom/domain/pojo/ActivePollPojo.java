
package com.tokopedia.tkpdstream.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivePollPojo {

    @SerializedName("poll_id")
    @Expose
    private int pollId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("poll_type")
    @Expose
    private String pollType;
    @SerializedName("option_type")
    @Expose
    private String optionType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("options")
    @Expose
    private List<Object> options = null;
    @SerializedName("votes")
    @Expose
    private List<Object> votes = null;
    @SerializedName("start_time")
    @Expose
    private int startTime;
    @SerializedName("end_time")
    @Expose
    private int endTime;

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPollType() {
        return pollType;
    }

    public void setPollType(String pollType) {
        this.pollType = pollType;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public List<Object> getVotes() {
        return votes;
    }

    public void setVotes(List<Object> votes) {
        this.votes = votes;
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

}
