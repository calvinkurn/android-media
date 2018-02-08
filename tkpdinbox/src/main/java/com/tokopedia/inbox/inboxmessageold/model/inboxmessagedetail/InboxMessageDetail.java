
package com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;

public class InboxMessageDetail implements Parcelable {

    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("textarea_reply")
    @Expose
    private int textareaReply;
    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("conversation_between")
    @Expose
    private java.util.List<ConversationBetween> conversationBetween = new ArrayList<ConversationBetween>();
    @SerializedName("message_title")
    @Expose
    private String messageTitle;
    @SerializedName("message_total")
    @Expose
    private int messageTotal;
    @SerializedName("list")
    @Expose
    private java.util.List<InboxMessageDetailItem> list = new ArrayList<InboxMessageDetailItem>();

    protected InboxMessageDetail(Parcel in) {
        messageId = in.readString();
        textareaReply = in.readInt();
        conversationBetween = in.createTypedArrayList(ConversationBetween.CREATOR);
        messageTitle = in.readString();
        messageTotal = in.readInt();
        list = in.createTypedArrayList(InboxMessageDetailItem.CREATOR);
    }

    public static final Creator<InboxMessageDetail> CREATOR = new Creator<InboxMessageDetail>() {
        @Override
        public InboxMessageDetail createFromParcel(Parcel in) {
            return new InboxMessageDetail(in);
        }

        @Override
        public InboxMessageDetail[] newArray(int size) {
            return new InboxMessageDetail[size];
        }
    };

    /**
     * @return The messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId The message_id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return The textareaReply
     */
    public int getTextareaReply() {
        return textareaReply;
    }

    /**
     * @param textareaReply The textarea_reply
     */
    public void setTextareaReply(int textareaReply) {
        this.textareaReply = textareaReply;
    }

    /**
     * @return The paging
     */
    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    /**
     * @param paging The paging
     */
    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    /**
     * @return The conversationBetween
     */
    public java.util.List<ConversationBetween> getConversationBetween() {
        return conversationBetween;
    }

    /**
     * @param conversationBetween The conversation_between
     */
    public void setConversationBetween(java.util.List<ConversationBetween> conversationBetween) {
        this.conversationBetween = conversationBetween;
    }

    /**
     * @return The messageTitle
     */
    public String getMessageTitle() {
        return MethodChecker.fromHtml(messageTitle).toString();
    }

    /**
     * @param messageTitle The message_title
     */
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    /**
     * @return The messageTotal
     */
    public int getMessageTotal() {
        return messageTotal;
    }

    /**
     * @param messageTotal The message_total
     */
    public void setMessageTotal(int messageTotal) {
        this.messageTotal = messageTotal;
    }

    /**
     * @return The list
     */
    public java.util.List<InboxMessageDetailItem> getList() {
        return list;
    }

    /**
     * @param list The list
     */
    public void setList(java.util.List<InboxMessageDetailItem> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageId);
        dest.writeInt(textareaReply);
        dest.writeTypedList(conversationBetween);
        dest.writeString(messageTitle);
        dest.writeInt(messageTotal);
        dest.writeTypedList(list);
    }

    public ConversationBetween getOpponent() {
        for (ConversationBetween c : conversationBetween) {
            if (c.getUserId() != null
                    && !String.valueOf(c.getUserId()).equals(SessionHandler.getLoginID(MainApplication.getAppContext()))) {
                return c;
            }
        }
        return null;
    }
}
