package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 29/01/18.
 */
public class FilterResponse {

    @SerializedName("title")
    private String title;
    @SerializedName("titleCountFullString")
    private String titleCountFullString;
    @SerializedName("filterWithDateString")
    private String filterWithDateString;
    @SerializedName("count")
    private int count;
    @SerializedName("orderValue")
    private int orderValue;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleCountFullString() {
        return titleCountFullString;
    }

    public void setTitleCountFullString(String titleCountFullString) {
        this.titleCountFullString = titleCountFullString;
    }

    public String getFilterWithDateString() {
        return filterWithDateString;
    }

    public void setFilterWithDateString(String filterWithDateString) {
        this.filterWithDateString = filterWithDateString;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }
}
