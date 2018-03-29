package com.tokopedia.inbox.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Hendri on 28/03/18.
 */

public class GetInvoicePostRequest {

    @SerializedName("message_id")
    @Expose
    int messageId;
    @SerializedName("user_id")
    @Expose
    int userId;
    @SerializedName("show_all")
    @Expose
    boolean isShowAll;
    @SerializedName("page")
    @Expose
    int page;
    @SerializedName("limit")
    @Expose
    int limit;
    @SerializedName("start_time")
    @Expose
    String startTime;

    public GetInvoicePostRequest(int messageId, int userId, boolean isShowAll, int page, int limit) {
        this.messageId = messageId;
        this.userId = userId;
        this.isShowAll = isShowAll;
        this.page = page;
        this.limit = limit;

        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        this.startTime = date.format(Calendar.getInstance().getTime());
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    public void setShowAll(boolean showAll) {
        isShowAll = showAll;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
