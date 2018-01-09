
package com.tokopedia.inbox.inboxmessageold.model.inboxmessage;

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

public class InboxMessageItem implements Parcelable {

    @SerializedName("with_user")
    @Expose
    private WithUser withUser;
    @SerializedName("message_id")
    @Expose
    private int messageId;
    @SerializedName("user_full_name")
    @Expose
    private String userFullName;
    @SerializedName("message_create_time_fmt")
    @Expose
    private String messageCreateTimeFmt;
    @SerializedName("message_read_status")
    @Expose
    private int messageReadStatus;
    @SerializedName("message_title")
    @Expose
    private String messageTitle;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("message_keyword")
    @Expose
    private String messageKeyword;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("message_reply")
    @Expose
    private String messageReply;
    @SerializedName("json_data_info")
    @Expose
    private String jsonDataInfo;
    @SerializedName("message_is_seller")
    @Expose
    private int messageIsSeller;
    @SerializedName("alert_fraud")
    @Expose
    private int alertFraud;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("message_create_time")
    @Expose
    private String messageCreateTime;
    @SerializedName("message_is_admin")
    @Expose
    private int messageIsAdmin;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("message_inbox_id")
    @Expose
    private int messageInboxId;
    private boolean isChecked;
    private int position;

    protected InboxMessageItem(Parcel in) {
        withUser = in.readParcelable(WithUser.class.getClassLoader());
        messageId = in.readInt();
        userFullName = in.readString();
        messageCreateTimeFmt = in.readString();
        messageReadStatus = in.readInt();
        messageTitle = in.readString();
        userReputation = in.readParcelable(UserReputation.class.getClassLoader());
        userLabel = in.readString();
        messageKeyword = in.readString();
        userImage = in.readString();
        messageReply = in.readString();
        jsonDataInfo = in.readString();
        messageIsSeller = in.readInt();
        alertFraud = in.readInt();
        userLabelId = in.readInt();
        messageCreateTime = in.readString();
        messageIsAdmin = in.readInt();
        userId = in.readInt();
        messageInboxId = in.readInt();
        isChecked = in.readByte() != 0;
        position = in.readInt();
    }

    public static final Creator<InboxMessageItem> CREATOR = new Creator<InboxMessageItem>() {
        @Override
        public InboxMessageItem createFromParcel(Parcel in) {
            return new InboxMessageItem(in);
        }

        @Override
        public InboxMessageItem[] newArray(int size) {
            return new InboxMessageItem[size];
        }
    };

    /**
     * 
     * @return
     *     The withUser
     */
    public WithUser getWithUser() {
        return withUser;
    }

    /**
     * 
     * @param withUser
     *     The with_user
     */
    public void setWithUser(WithUser withUser) {
        this.withUser = withUser;
    }

    /**
     * 
     * @return
     *     The messageId
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * 
     * @param messageId
     *     The message_id
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * 
     * @return
     *     The userFullName
     */
    public Spanned getUserFullName() {
        return MethodChecker.fromHtml(userFullName);
    }

    /**
     * 
     * @param userFullName
     *     The user_full_name
     */
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    /**
     * 
     * @return
     *     The messageCreateTimeFmt
     */
    public String getMessageCreateTimeFmt() {
        return messageCreateTimeFmt;
    }

    /**
     * 
     * @param messageCreateTimeFmt
     *     The message_create_time_fmt
     */
    public void setMessageCreateTimeFmt(String messageCreateTimeFmt) {
        this.messageCreateTimeFmt = messageCreateTimeFmt;
    }

    /**
     * 
     * @return
     *     The messageReadStatus
     */
    public int getMessageReadStatus() {
        return messageReadStatus;
    }

    /**
     * 
     * @param messageReadStatus
     *     The message_read_status
     */
    public void setMessageReadStatus(int messageReadStatus) {
        this.messageReadStatus = messageReadStatus;
    }

    /**
     * 
     * @return
     *     The messageTitle
     */
    public Spanned getMessageTitle() {
        return MethodChecker.fromHtml(messageTitle);
    }

    /**
     * 
     * @param messageTitle
     *     The message_title
     */
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    /**
     * 
     * @return
     *     The userReputation
     */
    public UserReputation getUserReputation() {
        return userReputation;
    }

    /**
     * 
     * @param userReputation
     *     The user_reputation
     */
    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

    /**
     * 
     * @return
     *     The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * 
     * @param userLabel
     *     The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * 
     * @return
     *     The messageKeyword
     */
    public String getMessageKeyword() {
        return messageKeyword;
    }

    /**
     * 
     * @param messageKeyword
     *     The message_keyword
     */
    public void setMessageKeyword(String messageKeyword) {
        this.messageKeyword = messageKeyword;
    }

    /**
     * 
     * @return
     *     The userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     * 
     * @param userImage
     *     The user_image
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    /**
     * 
     * @return
     *     The messageReply
     */
    public Spanned getMessageReply() {
        return MethodChecker.fromHtml(messageReply);
    }

    /**
     * 
     * @param messageReply
     *     The message_reply
     */
    public void setMessageReply(String messageReply) {
        this.messageReply = messageReply;
    }

    /**
     * 
     * @return
     *     The jsonDataInfo
     */
    public String getJsonDataInfo() {
        return jsonDataInfo;
    }

    /**
     * 
     * @param jsonDataInfo
     *     The json_data_info
     */
    public void setJsonDataInfo(String jsonDataInfo) {
        this.jsonDataInfo = jsonDataInfo;
    }

    /**
     * 
     * @return
     *     The messageIsSeller
     */
    public int getMessageIsSeller() {
        return messageIsSeller;
    }

    /**
     * 
     * @param messageIsSeller
     *     The message_is_seller
     */
    public void setMessageIsSeller(int messageIsSeller) {
        this.messageIsSeller = messageIsSeller;
    }

    /**
     * 
     * @return
     *     The alertFraud
     */
    public int getAlertFraud() {
        return alertFraud;
    }

    /**
     * 
     * @param alertFraud
     *     The alert_fraud
     */
    public void setAlertFraud(int alertFraud) {
        this.alertFraud = alertFraud;
    }

    /**
     * 
     * @return
     *     The userLabelId
     */
    public int getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * 
     * @return
     *     The messageCreateTime
     */
    public String getMessageCreateTime() {
        return messageCreateTime;
    }

    /**
     * 
     * @param messageCreateTime
     *     The message_create_time
     */
    public void setMessageCreateTime(String messageCreateTime) {
        this.messageCreateTime = messageCreateTime;
    }

    /**
     * 
     * @return
     *     The messageIsAdmin
     */
    public int getMessageIsAdmin() {
        return messageIsAdmin;
    }

    /**
     * 
     * @param messageIsAdmin
     *     The message_is_admin
     */
    public void setMessageIsAdmin(int messageIsAdmin) {
        this.messageIsAdmin = messageIsAdmin;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The messageInboxId
     */
    public int getMessageInboxId() {
        return messageInboxId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * 
     * @param messageInboxId
     *     The message_inbox_id
     */
    public void setMessageInboxId(int messageInboxId) {
        this.messageInboxId = messageInboxId;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(withUser, flags);
        dest.writeInt(messageId);
        dest.writeString(userFullName);
        dest.writeString(messageCreateTimeFmt);
        dest.writeInt(messageReadStatus);
        dest.writeString(messageTitle);
        dest.writeParcelable(userReputation, flags);
        dest.writeString(userLabel);
        dest.writeString(messageKeyword);
        dest.writeString(userImage);
        dest.writeString(messageReply);
        dest.writeString(jsonDataInfo);
        dest.writeInt(messageIsSeller);
        dest.writeInt(alertFraud);
        dest.writeInt(userLabelId);
        dest.writeString(messageCreateTime);
        dest.writeInt(messageIsAdmin);
        dest.writeInt(userId);
        dest.writeInt(messageInboxId);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(position);
    }

    public String getMessageCreateDateFmt() {
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

            cal.setTime(sdf.parse(getMessageCreateTimeFmt()));
            if (cal.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR)
                    && cal.get(Calendar.YEAR) == calNow.get(Calendar.YEAR)) {
                return "Hari ini";
            } else if (cal.get(Calendar.DAY_OF_YEAR) == calYesterday.get(Calendar.DAY_OF_YEAR)
                    && cal.get(Calendar.YEAR) == calYesterday.get(Calendar.YEAR)){
                return "Kemarin";
            }
            else {
                return newSdf.format(sdf.parse(getMessageCreateTimeFmt()));
            }
        } catch (ParseException e) {
            return "";
        }
    }
}
