package com.tokopedia.tkpdstream.vote.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteInfoViewModel {

    private final String pollId;
    private String voteType;
    private String voteStatus;
    private String voteInfoString;
    private String voteInfoUrl;
    private boolean voted;
    private String title;
    private List<Visitable> listOption;
    private long startTime, endTime;
    private String participant;

    public VoteInfoViewModel(String pollId, String title, List<Visitable> listOption, String participant,
                             String voteType, String voteStatus, boolean voted,
                             String voteInfoString, String voteInfoUrl, long startTime, long endTime) {
        this.pollId= pollId;
        this.title = title;
        this.listOption = listOption;
        this.participant = participant;
        this.voteStatus = voteStatus;
        this.voteType = voteType;
        this.voted = voted;
        this.voteInfoString = voteInfoString;
        this.voteInfoUrl = voteInfoUrl;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getVoteType() {
        return voteType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Visitable> getListOption() {
        return listOption;
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

    public boolean isVoted() {
        return voted;
    }

    public String getVoteInfoString() {
        return voteInfoString;
    }

    public String getVoteInfoUrl() {
        return voteInfoUrl;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getPollId() {
        return pollId;
    }
}

