package com.tokopedia.tkpdstream.vote.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * Created by StevenFredian on 21/02/18.
 */

public class VoteInfoViewModel {

    int voteType;

    String voteStatus;

    String voteInfoString;

    String voteInfoUrl;

    boolean voted;

    String title;

    List<Visitable> list;

    int participant;

    public VoteInfoViewModel(String title, List<Visitable> list, int participant, int voteType, String voteStatus, boolean voted, String voteInfoString, String voteInfoUrl) {
        this.title = title;
        this.list = list;
        this.participant = participant;
        this.voteStatus = voteStatus;
        this.voteType = voteType;
        this.voted = voted;
        this.voteInfoString = voteInfoString;
        this.voteInfoUrl = voteInfoUrl;
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

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
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
}
