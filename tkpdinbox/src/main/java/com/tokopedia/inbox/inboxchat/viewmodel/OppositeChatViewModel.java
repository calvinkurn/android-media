package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class OppositeChatViewModel extends ListReplyViewModel {

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
