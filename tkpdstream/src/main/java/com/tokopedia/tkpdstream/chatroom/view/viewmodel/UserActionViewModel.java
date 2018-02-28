package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;

import java.util.Date;

/**
 * @author by nisie on 2/22/18.
 */

public class UserActionViewModel extends BaseChatViewModel implements
        Visitable<GroupChatTypeFactory> {
    public static final int ACTION_ENTER = 1;
    public static final int ACTION_EXIT = 2;

    private final String userId;
    private final String userName;
    private final String avatarUrl;
    private final int actionType;

    public UserActionViewModel(String userId, String userName, String avatarUrl, int actionType) {
        super("", System.currentTimeMillis(), 0, "");
        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.actionType = actionType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getActionType() {
        return actionType;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
