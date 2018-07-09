package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class MyChatViewModel extends ListReplyViewModel{

    boolean readStatus;

    boolean isRetry;

    boolean isDummy;

    public MyChatViewModel() {
        super();
        this.isDummy = false;
        this.isRetry = false;
    }

    public MyChatViewModel(boolean b) {
        super();
        this.isDummy = true;
        this.isRetry = false;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isDummy() {
        return isDummy;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public boolean isRetry() {
        return isRetry;
    }

    public void setRetry(boolean retry) {
        isRetry = retry;
    }
}
