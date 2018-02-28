package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/27/18.
 */

public class VoteAnnouncementViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    public static final int VOTE_ACTIVE = 1;
    public static final int VOTE_FINISHED = 2;

    private final int voteType;

    public VoteAnnouncementViewModel(String message, int voteType, long createdAt,
                                     long updatedAt, String messageId, String senderId,
                                     String senderName, String senderIconUrl,
                                     boolean isInfluencer, boolean isAdministrator) {
        super(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl,
                isInfluencer, isAdministrator);
        this.voteType = voteType;
    }

    public int getVoteType() {
        return voteType;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
