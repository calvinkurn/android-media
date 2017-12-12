package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatTypeFactory;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class TemplateChatModel implements Visitable<TemplateChatTypeFactory>{

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int type(TemplateChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
