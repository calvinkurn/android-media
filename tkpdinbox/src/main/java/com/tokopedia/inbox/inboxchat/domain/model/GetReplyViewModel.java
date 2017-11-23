package com.tokopedia.inbox.inboxchat.domain.model;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;

import java.util.List;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class GetReplyViewModel {
    private List<Visitable> list;
    private boolean hasNext;
    private int textAreaReply;
    private boolean hasTimeMachine;

    public List<Visitable> getList() {
        return list;
    }

    public void setList(List<Visitable> list) {
        this.list = list;
    }

    public int getTextAreaReply() {
        return textAreaReply;
    }

    public void setTextAreaReply(int textAreaReply) {
        this.textAreaReply = textAreaReply;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setHasTimeMachine(boolean hasTimeMachine) {
        this.hasTimeMachine = hasTimeMachine;
    }

    public boolean isHasTimeMachine() {
        return hasTimeMachine;
    }
}
