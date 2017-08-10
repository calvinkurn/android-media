package com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendry on 4/5/2017.
 */

public class Result {

    @SerializedName("search_url")
    @Expose
    private String searchUrl;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("total_record")
    @Expose
    private int totalRecord;
    @SerializedName("catalogs")
    @Expose
    private List<Catalog> catalogs = null;
    @SerializedName("category")
    @Expose
    private Object category;

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

}
