package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.chat;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nisie on 3/29/17.
 */

public class DiscussionItemViewModel implements Parcelable{

    public static final String DISCUSSION_DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
    private String message;
    private String userName;
    private String userLabel;
    private int userLabelId;
    private String messageReplyTimeFmt;
    private String messageCreateBy;
    private List<AttachmentViewModel> listAttachment;
    private String conversationId;

    public DiscussionItemViewModel() {
    }

    protected DiscussionItemViewModel(Parcel in) {
        message = in.readString();
        userName = in.readString();
        userLabel = in.readString();
        userLabelId = in.readInt();
        messageReplyTimeFmt = in.readString();
        messageCreateBy = in.readString();
        listAttachment = in.createTypedArrayList(AttachmentViewModel.CREATOR);
        conversationId = in.readString();
    }

    public static final Creator<DiscussionItemViewModel> CREATOR = new Creator<DiscussionItemViewModel>() {
        @Override
        public DiscussionItemViewModel createFromParcel(Parcel in) {
            return new DiscussionItemViewModel(in);
        }

        @Override
        public DiscussionItemViewModel[] newArray(int size) {
            return new DiscussionItemViewModel[size];
        }
    };

    public Spanned getMessage() {
        return MethodChecker.fromHtml(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public int getUserLabelId() {
        return userLabelId;
    }

    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    public String getMessageReplyTimeFmt() {
        return messageReplyTimeFmt;
    }

    public void setMessageReplyTimeFmt(String messageReplyTimeFmt) {
        this.messageReplyTimeFmt = messageReplyTimeFmt;
    }

    public String getMessageCreateBy() {
        return messageCreateBy;
    }

    public void setMessageCreateBy(String messageCreateBy) {
        this.messageCreateBy = messageCreateBy;
    }

    public String getMessageReplyDateFmt() {
        try {
            Locale id = new Locale("in", "ID");
            SimpleDateFormat e = new SimpleDateFormat(DISCUSSION_DATE_TIME_FORMAT, id);
            SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Calendar calNow = Calendar.getInstance();
            calNow.setTime(new Date());
            Calendar calYesterday = Calendar.getInstance();
            calYesterday.setTime(new Date());
            calYesterday.add(5, -1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(e.parse(this.getMessageReplyTimeFmt()));
            return cal.get(6) == calNow.get(6) && cal.get(1) == calNow.get(1) ? "Hari ini" : (cal.get(6) == calYesterday.get(6) && cal.get(1) == calYesterday.get(1) ? "Kemarin" : newSdf.format(e.parse(this.getMessageReplyTimeFmt())));
        } catch (ParseException var7) {
            return "";
        }
    }

    public String getMessageReplyHourFmt() {
        try {
            Locale id = new Locale("in", "ID");
            SimpleDateFormat e = new SimpleDateFormat(DISCUSSION_DATE_TIME_FORMAT, id);
            SimpleDateFormat newSdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            String hour = newSdf.format(e.parse(this.getMessageReplyTimeFmt()));
            return hour;
        } catch (ParseException var5) {
            return "";
        }
    }

    public void setAttachment(List<AttachmentViewModel> listAttachment) {
        this.listAttachment = listAttachment;
    }

    public List<AttachmentViewModel> getAttachment() {
        return listAttachment;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(userName);
        dest.writeString(userLabel);
        dest.writeInt(userLabelId);
        dest.writeString(messageReplyTimeFmt);
        dest.writeString(messageCreateBy);
        dest.writeTypedList(listAttachment);
        dest.writeString(conversationId);
    }
}

