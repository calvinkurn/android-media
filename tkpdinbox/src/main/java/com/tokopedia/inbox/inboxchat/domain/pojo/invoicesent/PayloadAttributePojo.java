package com.tokopedia.inbox.inboxchat.domain.pojo.invoicesent;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 16/05/18.
 */
public class PayloadAttributePojo {

    @SerializedName("id")
    private int id;
    @SerializedName("code")
    private String code;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private Object description;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("href_url")
    private Object hrefUrl;
    @SerializedName("status_id")
    private int statusId;
    @SerializedName("status")
    private String status;
    @SerializedName("total_amount")
    private String totalAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(Object hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
