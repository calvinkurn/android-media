package com.tokopedia.tkpdstream.vote.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * Created by StevenFredian on 21/02/18.
 */

public class VoteInfoViewModel {

    int voteType;

    String voteStatus;

    String title;

    List<Visitable> list;

    int participant;

    public VoteInfoViewModel(String title, List<Visitable> list, int participant, int voteType) {
        this.title = title;
        this.list = list;
        this.participant = participant;
        this.voteType = voteType;
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
}
