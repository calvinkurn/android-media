package com.tokopedia.inbox.inboxchat.viewmodel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 10/23/17.
 */

public class InboxChatViewModel {

    public final static int GET_CHAT_MODE = 1;
    public final static int SEARCH_CHAT_MODE = 2;
    private boolean hasTimeMachine;

    public InboxChatViewModel() {
        mode = GET_CHAT_MODE;
    }

    int mode;

    boolean hasNext;

    boolean hasNextContacts;

    boolean hasNextReplies;

    ArrayList<ChatListViewModel> list;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ArrayList<ChatListViewModel> getList() {
        return list;
    }

    public void setList(ArrayList<ChatListViewModel> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasNextContacts() {
        return hasNextContacts;
    }

    public void setHasNextContacts(boolean hasNextContacts) {
        this.hasNextContacts = hasNextContacts;
    }

    public boolean isHasNextReplies() {
        return hasNextReplies;
    }

    public void setHasNextReplies(boolean hasNextReplies) {
        this.hasNextReplies = hasNextReplies;
    }

    public void setHasTimeMachine(boolean hasTimeMachine) {
        this.hasTimeMachine = hasTimeMachine;
    }

    public boolean isHasTimeMachine() {
        return hasTimeMachine;
    }
}
