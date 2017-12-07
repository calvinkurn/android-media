package com.tokopedia.inbox.inboxmessageold.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 5/10/16.
 */
public class InboxMessagePass implements Parcelable {


    private static final String PARAM_NAV = "nav";
    private static final String PARAM_FILTER = "filter";
    private static final String PARAM_KEYWORD = "keyword";
    private static final String PARAM_PAGE = "page";

    private static final String PARAM_MESSAGE_ID = "message_id";
    private static final String PARAM_PER_PAGE = "per_page";
    private static final String PER_PAGE = "10";


    String nav;
    String filter;
    String keyword;
    String page;
    String messageId;

    public InboxMessagePass(){

    }

    protected InboxMessagePass(Parcel in) {
        nav = in.readString();
        filter = in.readString();
        keyword = in.readString();
        page = in.readString();
        messageId = in.readString();
    }

    public static final Creator<InboxMessagePass> CREATOR = new Creator<InboxMessagePass>() {
        @Override
        public InboxMessagePass createFromParcel(Parcel in) {
            return new InboxMessagePass(in);
        }

        @Override
        public InboxMessagePass[] newArray(int size) {
            return new InboxMessagePass[size];
        }
    };

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, String> getInboxMessageParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE, getPage());
        params.put(PARAM_FILTER, getFilter());
        params.put(PARAM_KEYWORD, getKeyword());
        params.put(PARAM_NAV, getNav());
        return params;
    }

    public Map<String, String> getInboxMessageDetailParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE, getPage());
        params.put(PARAM_PER_PAGE, PER_PAGE);
        params.put(PARAM_MESSAGE_ID, getMessageId());
        params.put(PARAM_NAV, getNav());
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nav);
        dest.writeString(filter);
        dest.writeString(keyword);
        dest.writeString(page);
        dest.writeString(messageId);
    }
}
