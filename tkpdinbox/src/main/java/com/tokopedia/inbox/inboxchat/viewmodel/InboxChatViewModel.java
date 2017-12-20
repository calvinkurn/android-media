package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;

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

    int contactSize;

    int chatSize;

    String keyword;

    ArrayList<Visitable> listContact;

    ArrayList<Visitable> listReplies;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ArrayList<Visitable> getListContact() {
        return listContact;
    }

    public void setListContact(ArrayList<Visitable> listContact) {
        this.listContact = listContact;
    }

    public ArrayList<Visitable> getListReplies() {
        return listReplies;
    }

    public void setListReplies(ArrayList<Visitable> listReplies) {
        this.listReplies = listReplies;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getContactSize() {
        return contactSize;
    }

    public void setContactSize(int contactSize) {
        this.contactSize = contactSize;
    }

    public int getChatSize() {
        return chatSize;
    }

    public void setChatSize(int chatSize) {
        this.chatSize = chatSize;
    }
}
