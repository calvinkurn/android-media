package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactory;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class TypingChatModel implements Visitable<ChatRoomTypeFactory> {

    String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int type(ChatRoomTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

}
