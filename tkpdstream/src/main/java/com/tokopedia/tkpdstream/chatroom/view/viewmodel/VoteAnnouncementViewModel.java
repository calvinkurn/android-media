package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class VoteAnnouncementViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    public static final String POLLING_START = "polling_start";
    public static final String POLLING_FINISHED = "polling_finished";
    public static final String POLLING_CANCEL = "polling_cancel";
    public static final String POLLING_UPDATE = "polling_update";

    private final String voteType;
    private VoteInfoViewModel voteInfoViewModel;

    public VoteAnnouncementViewModel(String message, String voteType, long createdAt,
                                     long updatedAt, String messageId, String senderId,
                                     String senderName, String senderIconUrl,
                                     boolean isInfluencer, boolean isAdministrator,
                                     VoteInfoViewModel voteInfoViewModel) {
        super(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
        this.voteType = voteType;
        this.voteInfoViewModel = voteInfoViewModel;
    }

    public String getVoteType() {
        return voteType;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public VoteInfoViewModel getVoteInfoViewModel() {
        return voteInfoViewModel;
    }
}
