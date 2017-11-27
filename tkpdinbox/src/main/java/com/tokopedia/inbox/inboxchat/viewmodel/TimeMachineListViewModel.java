package com.tokopedia.inbox.inboxchat.viewmodel;

import android.text.TextUtils;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatTypeFactory;

/**
 * @author by nisie on 10/26/17.
 */

public class TimeMachineListViewModel implements Visitable<InboxChatTypeFactory> {

    private String url = TkpdBaseURL.User.URL_INBOX_MESSAGE_TIME_MACHINE;

    public TimeMachineListViewModel(String url) {
        if (!TextUtils.isEmpty(url))
            this.url = url;
    }

    @Override
    public int type(InboxChatTypeFactory inboxChatTypeFactory) {
        return inboxChatTypeFactory.type(this);
    }

    public String getUrl() {
        return url;
    }
}
