package com.tokopedia.inbox.rescenter.discussion.view.viewmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nisie on 3/29/17.
 */

public class DiscussionItemViewModel {

    public static final java.lang.String DISCUSSION_DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
    private String message;
    private String userName;
    private String userLabel;
    private int userLabelId;
    private String messageReplyTimeFmt;
    private String messageCreateBy;
    private List<AttachmentViewModel> listAttachment;
    private String conversationId;

    public DiscussionItemViewModel(String message, String messageReplyTimeFmt, String messageCreateBy) {
        this.userName = "James";
        this.message = message;
        this.userLabel = "Pengguna";
        this.userLabelId = 1;
        this.messageReplyTimeFmt = messageReplyTimeFmt;
        this.messageCreateBy = messageCreateBy;
    }

    public DiscussionItemViewModel() {
    }

    public String getMessage() {
        return message;
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
}

