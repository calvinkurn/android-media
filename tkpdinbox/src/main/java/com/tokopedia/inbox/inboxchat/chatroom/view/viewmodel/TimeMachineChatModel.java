package com.tokopedia.inbox.inboxchat.chatroom.viewmodel;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.ChatRoomTypeFactory;

/**
 * @author by nisie on 10/24/17.
 */

public class TimeMachineChatModel implements Visitable<ChatRoomTypeFactory> {

    private String url = TkpdBaseURL.User.URL_INBOX_MESSAGE_TIME_MACHINE;

    public TimeMachineChatModel(String url) {
        if (!TextUtils.isEmpty(url))
            this.url = url;
    }

    @Override
    public int type(ChatRoomTypeFactory chatRoomTypeFactory) {
        return chatRoomTypeFactory.type(this);
    }

    public String getUrl() {
        return url;
    }
}
