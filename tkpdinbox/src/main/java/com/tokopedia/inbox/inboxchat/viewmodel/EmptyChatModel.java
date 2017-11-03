package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactory;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class EmptyChatModel implements Visitable<InboxChatTypeFactory> {

    String logo;
    String titleText;
    String subtitleText;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSubtitleText() {
        return subtitleText;
    }

    public void setSubtitleText(String subtitleText) {
        this.subtitleText = subtitleText;
    }

    @Override
    public int type(InboxChatTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

}
