package com.tokopedia.tkpdstream.vote.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteInfoViewModel {

    private int voteType;
    private String voteStatus;
    private String voteInfoString;
    private String voteInfoUrl;
    private boolean voted;
    private String title;
    private List<Visitable> list;
    private long startTime, endTime;
    private String participant;

    public VoteInfoViewModel(String title, List<Visitable> list, String participant, int voteType
            , String voteStatus, boolean voted, String voteInfoString
            , String voteInfoUrl, long startTime, long endTime) {
        this.title = title;
        this.list = list;
        this.participant = participant;
        this.voteStatus = voteStatus;
        this.voteType = voteType;
        this.voted = voted;
        this.voteInfoString = voteInfoString;
        this.voteInfoUrl = voteInfoUrl;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Visitable> getList() {
        return list;
    }

    public void setList(List<Visitable> list) {
        this.list = list;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(String voteStatus) {
        this.voteStatus = voteStatus;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public String getVoteInfoString() {
        return voteInfoString;
    }

    public void setVoteInfoString(String voteInfoString) {
        this.voteInfoString = voteInfoString;
    }

    public String getVoteInfoUrl() {
        return voteInfoUrl;
    }

    public void setVoteInfoUrl(String voteInfoUrl) {
        this.voteInfoUrl = voteInfoUrl;
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
}
