package com.tokopedia.inbox.inboxchat.viewmodel;

import android.text.Spanned;
import android.text.SpannedString;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class ChatListViewModel {

    public static final int NO_SPAN = 0;
    public static final int SPANNED_CONTACT = 1;
    public static final int SPANNED_MESSAGE = 2;

    String name;
    String message;
    String image;
    String time;
    String id;
    String senderId;
    String label;
    int readStatus;
    int unreadCounter;
    Spanned span;
    int spanMode;
    int sectionSize;
    private String role;

    public ChatListViewModel() {
        spanMode = NO_SPAN;
    }

    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getUnreadCounter() {
        return unreadCounter;
    }

    public void setUnreadCounter(int counter) {
        this.unreadCounter = counter;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Spanned getSpan() {
        return span;
    }

    public void setSpan(Spanned span) {
        this.span = span;
    }

    public int getSpanMode() {
        return spanMode;
    }

    public void setSpanMode(int spanMode) {
        this.spanMode = spanMode;
    }

    public int getSectionSize() {
        return sectionSize;
    }

    public void setSectionSize(int sectionSize) {
        this.sectionSize = sectionSize;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
