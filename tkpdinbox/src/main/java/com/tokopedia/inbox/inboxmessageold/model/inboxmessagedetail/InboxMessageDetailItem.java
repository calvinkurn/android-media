
package com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxmessage.model.UserReputation;
import com.tokopedia.core.util.MethodChecker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InboxMessageDetailItem implements Parcelable {

    @SerializedName("message_action")
    @Expose
    private int messageAction;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("page_next")
    @Expose
    private int pageNext;
    @SerializedName("is_moderator")
    @Expose
    private int isModerator;
    @SerializedName("message_reply_date")
    @Expose
    private String messageReplyDate;
    @SerializedName("message_button_spam")
    @Expose
    private int messageButtonSpam;
    @SerializedName("message_reply_time_ago")
    @Expose
    private String messageReplyTimeAgo;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("message_reply")
    @Expose
    private String messageReply;
    @SerializedName("message_reply_id")
    @Expose
    private int messageReplyId;
    @SerializedName("message_reply_time_fmt")
    @Expose
    private String messageReplyTimeFmt;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("message_create_by")
    @Expose
    private String messageCreateBy;
    @SerializedName("user_id")
    @Expose
    private int userId;

    public InboxMessageDetailItem() {
    }

    protected InboxMessageDetailItem(Parcel in) {
        messageAction = in.readInt();
        userReputation = in.readParcelable(UserReputation.class.getClassLoader());
        userLabel = in.readString();
        pageNext = in.readInt();
        isModerator = in.readInt();
        messageReplyDate = in.readString();
        messageButtonSpam = in.readInt();
        messageReplyTimeAgo = in.readString();
        userImage = in.readString();
        messageReply = in.readString();
        messageReplyId = in.readInt();
        messageReplyTimeFmt = in.readString();
        userLabelId = in.readInt();
        userName = in.readString();
        messageCreateBy = in.readString();
        userId = in.readInt();
    }

    public static final Creator<InboxMessageDetailItem> CREATOR = new Creator<InboxMessageDetailItem>() {
        @Override
        public InboxMessageDetailItem createFromParcel(Parcel in) {
            return new InboxMessageDetailItem(in);
        }

        @Override
        public InboxMessageDetailItem[] newArray(int size) {
            return new InboxMessageDetailItem[size];
        }
    };

    /**
     * @return The messageAction
     */
    public int getMessageAction() {
        return messageAction;
    }

    /**
     * @param messageAction The message_action
     */
    public void setMessageAction(int messageAction) {
        this.messageAction = messageAction;
    }

    /**
     * @return The userReputation
     */
    public UserReputation getUserReputation() {
        return userReputation;
    }

    /**
     * @param userReputation The user_reputation
     */
    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

    /**
     * @return The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * @param userLabel The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * @return The pageNext
     */
    public int getPageNext() {
        return pageNext;
    }

    /**
     * @param pageNext The page_next
     */
    public void setPageNext(int pageNext) {
        this.pageNext = pageNext;
    }

    /**
     * @return The isModerator
     */
    public int getIsModerator() {
        return isModerator;
    }

    /**
     * @param isModerator The is_moderator
     */
    public void setIsModerator(int isModerator) {
        this.isModerator = isModerator;
    }

    /**
     * @return The messageReplyDate
     */
    public String getMessageReplyDate() {
        return messageReplyDate;
    }

    /**
     * @param messageReplyDate The message_reply_date
     */
    public void setMessageReplyDate(String messageReplyDate) {
        this.messageReplyDate = messageReplyDate;
    }

    /**
     * @return The messageButtonSpam
     */
    public int getMessageButtonSpam() {
        return messageButtonSpam;
    }

    /**
     * @param messageButtonSpam The message_button_spam
     */
    public void setMessageButtonSpam(int messageButtonSpam) {
        this.messageButtonSpam = messageButtonSpam;
    }

    /**
     * @return The messageReplyTimeAgo
     */
    public String getMessageReplyTimeAgo() {
        return messageReplyTimeAgo;
    }

    /**
     * @param messageReplyTimeAgo The message_reply_time_ago
     */
    public void setMessageReplyTimeAgo(String messageReplyTimeAgo) {
        this.messageReplyTimeAgo = messageReplyTimeAgo;
    }

    /**
     * @return The userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     * @param userImage The user_image
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    /**
     * @return The messageReply
     */
    public Spanned getMessageReply() {
        return MethodChecker.fromHtml(messageReply);
    }

    public String getMessageReplyString() {
        return messageReply;
    }

    /**
     * @param messageReply The message_reply
     */
    public void setMessageReply(String messageReply) {
        this.messageReply = messageReply;
    }

    /**
     * @return The messageReplyId
     */
    public int getMessageReplyId() {
        return messageReplyId;
    }

    /**
     * @param messageReplyId The message_reply_id
     */
    public void setMessageReplyId(int messageReplyId) {
        this.messageReplyId = messageReplyId;
    }

    /**
     * @return The messageReplyTimeFmt
     */
    public String getMessageReplyTimeFmt() {
        return messageReplyTimeFmt;
    }

    /**
     * @param messageReplyTimeFmt The message_reply_time_fmt
     */
    public void setMessageReplyTimeFmt(String messageReplyTimeFmt) {
        this.messageReplyTimeFmt = messageReplyTimeFmt;
    }

    /**
     * @return The userLabelId
     */
    public int getUserLabelId() {
        return userLabelId;
    }

    /**
     * @param userLabelId The user_label_id
     */
    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * @return The userName
     */
    public Spanned getUserName() {
        return MethodChecker.fromHtml(userName);
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The messageCreateBy
     */
    public String getMessageCreateBy() {
        return messageCreateBy;
    }

    /**
     * @param messageCreateBy The message_create_by
     */
    public void setMessageCreateBy(String messageCreateBy) {
        this.messageCreateBy = messageCreateBy;
    }

    /**
     * @return The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(messageAction);
        dest.writeParcelable(userReputation, flags);
        dest.writeString(userLabel);
        dest.writeInt(pageNext);
        dest.writeInt(isModerator);
        dest.writeString(messageReplyDate);
        dest.writeInt(messageButtonSpam);
        dest.writeString(messageReplyTimeAgo);
        dest.writeString(userImage);
        dest.writeString(messageReply);
        dest.writeInt(messageReplyId);
        dest.writeString(messageReplyTimeFmt);
        dest.writeInt(userLabelId);
        dest.writeString(userName);
        dest.writeString(messageCreateBy);
        dest.writeInt(userId);
    }

    public String getMessageReplyDateFmt() {
        try {
            SimpleDateFormat sdf;
            Locale id;
            id = new Locale("in", "ID");
            sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
            SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Calendar calNow = Calendar.getInstance();
            calNow.setTime(new Date());
            Calendar calYesterday = Calendar.getInstance();
            calYesterday.setTime(new Date());
            calYesterday.add(Calendar.DATE,-1);
            Calendar cal = Calendar.getInstance();

            cal.setTime(sdf.parse(getMessageReplyTimeFmt()));
            if (cal.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR)
                    && cal.get(Calendar.YEAR) == calNow.get(Calendar.YEAR)) {
                return "Hari ini";
            } else if (cal.get(Calendar.DAY_OF_YEAR) == calYesterday.get(Calendar.DAY_OF_YEAR)
                    && cal.get(Calendar.YEAR) == calYesterday.get(Calendar.YEAR)){
                return "Kemarin";
            }
            else {
                return newSdf.format(sdf.parse(getMessageReplyTimeFmt()));
            }
        } catch (ParseException e) {
            return "";
        }
    }

    public String getMessageReplyHourFmt() {
        try {
            SimpleDateFormat sdf;
            Locale id;
            id = new Locale("in", "ID");
            sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
            SimpleDateFormat newSdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            String hour = newSdf.format(sdf.parse(getMessageReplyTimeFmt()));
            return hour;
        } catch (ParseException e) {
            return "";
        }
    }
}
