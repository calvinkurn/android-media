package com.tokopedia.tkpdstream.vote.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.R;

import java.util.List;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteInfoViewModel {


    public static final int STATUS_ACTIVE = 2;
    public static final int STATUS_FORCE_ACTIVE = 3;
    public static final int STATUS_FINISH = 4;
    public static final int STATUS_FORCE_FINISH = 5;
    public static final int STATUS_CANCELED = 6;

    public static final int VOTE_TYPE_GIFT = 1;


    private String pollId;
    private int statusId;
    private String voteOptionType;
    private String voteGiftType;
    private String voteStatus;
    private int voteInfoStringResId;
    private String voteInfoUrl;
    private boolean voted;
    private String title;
    private List<Visitable> listOption;
    private long startTime, endTime;
    private String participant;

    public VoteInfoViewModel(String pollId, String title, List<Visitable> listOption, String participant,
                             String voteGiftType, String voteOptionType, String voteStatus, int statusId, boolean voted,
                             int voteInfoStringResId, String voteInfoUrl, long startTime, long endTime) {
        this.pollId = pollId;
        this.title = title;
        this.listOption = listOption;
        this.participant = participant;
        this.statusId = statusId;
        this.voteStatus = voteStatus;
        this.voteOptionType = voteOptionType;
        this.voteGiftType = voteGiftType;
        this.voted = voted;
        this.voteInfoStringResId = voteInfoStringResId;
        this.voteInfoUrl = voteInfoUrl;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getVoteOptionType() {
        return voteOptionType;
    }

    public void setVoteOptionType(String voteOptionType) {
        this.voteOptionType = voteOptionType;
    }

    public String getVoteGiftType() {
        return voteGiftType;
    }

    public void setVoteGiftType(String voteGiftType) {
        this.voteGiftType = voteGiftType;
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

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public int getVoteInfoStringResId() {
        return voteInfoStringResId;
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

    public int getStatusId() {
        return statusId;
    }

    public static int getStringVoteInfo(int pollTypeId) {
        switch (pollTypeId) {
            //TODO : Implement this in next sprint.
//            case VoteInfoViewModel.VOTE_TYPE_GIFT:
//                return R.string.info_prize;
            default:
                return R.string.info_polling;
        }
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}

