package com.tokopedia.inbox.inboxmessageold.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 5/11/16.
 */
public class ActInboxMessagePass implements Parcelable {

    private static final String PARAM_DATA_ELEMENT = "data_element";

    private static final String PARAM_NAV = "nav";
    private static final String PARAM_MESSAGE_REPLY_ID = "message_reply_id";

    private static final String PARAM_MESSAGE_ID = "message_id";
    private static final String PARAM_REPLY_MESSAGE = "reply_message";


    String nav;
    String messageReplyId;
    String messageId;
    String replyMessage;
    ArrayList<InboxMessageItem> listMove;

    public ActInboxMessagePass() {
    }

    protected ActInboxMessagePass(Parcel in) {
        nav = in.readString();
        messageReplyId = in.readString();
        messageId = in.readString();
        replyMessage = in.readString();
        listMove = in.createTypedArrayList(InboxMessageItem.CREATOR);
    }

    public static final Creator<ActInboxMessagePass> CREATOR = new Creator<ActInboxMessagePass>() {
        @Override
        public ActInboxMessagePass createFromParcel(Parcel in) {
            return new ActInboxMessagePass(in);
        }

        @Override
        public ActInboxMessagePass[] newArray(int size) {
            return new ActInboxMessagePass[size];
        }
    };

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public String getMessageReplyId() {
        return messageReplyId;
    }

    public void setMessageReplyId(String messageReplyId) {
        this.messageReplyId = messageReplyId;
    }

    public ArrayList<InboxMessageItem> getListMove() {
        return listMove;
    }

    public void setListMove(ArrayList<InboxMessageItem> listMove) {
        this.listMove = listMove;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Map<String, String> getMoveInboxParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_DATA_ELEMENT, generateDataElement());
        return params;
    }

    public Map<String, String> getFlagSpamParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAV, getNav());
        params.put(PARAM_MESSAGE_REPLY_ID, getMessageReplyId());
        return params;
    }

    public Map<String, String> getSendReplyParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_MESSAGE_ID, getMessageId());
        params.put(PARAM_REPLY_MESSAGE, getReplyMessage());
        return params;
    }

    private String generateDataElement() {
        String requestJSON = "";
        int total = listMove.size();
        for (int i = 0; i < total; i++) {
            String temp = "{\"keyword\" : \"\",\"navigation\" : \"" + getNav() + "\",\"page\" : \"1\",\"msg_id\" : \"" + listMove.get(i).getMessageId() + "\",\"inbox_id\" : \"" + listMove.get(i).getMessageInboxId() + "\"}";
            if (i == 0)
                requestJSON = requestJSON + temp;
            else
                requestJSON = requestJSON + "and" + temp;
        }
        return requestJSON;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nav);
        dest.writeString(messageReplyId);
        dest.writeString(messageId);
        dest.writeString(replyMessage);
        dest.writeTypedList(listMove);
    }

    public void clearListMove() {
        for(InboxMessageItem messageItem : this.listMove){
            messageItem.setIsChecked(false);
        }
    }
}
